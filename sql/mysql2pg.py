#!/usr/bin/env python3
"""Convert MySQL dump to PostgreSQL-compatible SQL."""

import re

INPUT = "cuhk_mkt.sql"
OUTPUT = "cuhk_mkt_pg.sql"


def convert_mysql_string_escapes(text: str) -> str:
    """Convert MySQL backslash escapes inside SQL string literals.

    MySQL uses \\' to escape quotes, \\r \\n \\t for control chars, \\\\" for double quotes.
    PostgreSQL uses '' for quote escaping and E'' strings for backslash escapes.
    We convert: \\' -> '' and \\\\" -> " inside string literals,
    and \\r/\\n/\\t to actual characters (since PG will interpret them in the string).
    """
    result = []
    i = 0
    in_string = False
    string_char = None

    while i < len(text):
        ch = text[i]

        if not in_string:
            result.append(ch)
            if ch in ("'",):
                in_string = True
                string_char = ch
            i += 1
            continue

        # We are inside a string literal
        if ch == "\\" and i + 1 < len(text):
            next_ch = text[i + 1]
            if next_ch == "'":
                # MySQL \\' -> PostgreSQL ''
                result.append("''")
                i += 2
            elif next_ch == '"':
                # MySQL \\" -> just "
                result.append('"')
                i += 2
            elif next_ch == "\\":
                # MySQL \\\\ -> PostgreSQL \\  (keep as-is for E'' compat)
                result.append("\\\\")
                i += 2
            elif next_ch == "n":
                # MySQL \\n -> actual newline
                result.append("\n")
                i += 2
            elif next_ch == "r":
                # MySQL \\r -> actual carriage return
                result.append("\r")
                i += 2
            elif next_ch == "t":
                # MySQL \\t -> actual tab
                result.append("\t")
                i += 2
            elif next_ch == "0":
                # MySQL \\0 -> NUL
                result.append("\0")
                i += 2
            else:
                # Unknown escape, keep as-is
                result.append(ch)
                result.append(next_ch)
                i += 2
            continue

        if ch == string_char:
            # Check for doubled quote (already escaped)
            if i + 1 < len(text) and text[i + 1] == string_char:
                result.append(ch + ch)
                i += 2
                continue
            else:
                # End of string
                result.append(ch)
                in_string = False
                i += 1
                continue

        result.append(ch)
        i += 1

    return "".join(result)


def convert(input_path: str, output_path: str):
    with open(input_path, "r", encoding="utf-8") as f:
        content = f.read()

    lines = content.split("\n")
    out_lines = []
    table_comments = []
    column_comments = []
    create_index_stmts = []
    current_table = None
    in_create_table = False
    constraint_lines = []

    for line in lines:
        stripped = line.strip()

        # Skip MySQL-specific SET statements
        if stripped.startswith("SET NAMES ") or stripped.startswith("SET FOREIGN_KEY_CHECKS"):
            continue

        # Track current table name
        m = re.match(r"^CREATE TABLE `(\w+)`", stripped)
        if m:
            current_table = m.group(1)
            in_create_table = True

        # DROP TABLE IF EXISTS `tbl` → DROP TABLE IF EXISTS tbl
        line = re.sub(r"^DROP TABLE IF EXISTS `(\w+)`", r"DROP TABLE IF EXISTS \1", line)

        # CREATE TABLE `tbl` → CREATE TABLE tbl
        line = re.sub(r"^CREATE TABLE `(\w+)`", r"CREATE TABLE \1", line)

        # Remove backticks everywhere
        line = line.replace("`", "")

        # int(11) → integer, bigint(20) → bigint, tinyint(1) → smallint
        line = re.sub(r"\bint\(\d+\)", "integer", line)
        line = re.sub(r"\bbigint\(\d+\)", "bigint", line)
        line = re.sub(r"\btinyint\(\d+\)", "smallint", line)
        line = re.sub(r"\bsmallint\(\d+\)", "smallint", line)
        line = re.sub(r"\bmediumint\(\d+\)", "integer", line)

        # decimal(M,N) → numeric(M,N)
        line = re.sub(r"\bdecimal\(", "numeric(", line)

        # mediumtext/longtext/tinytext → text (PG has no size variants)
        line = re.sub(r"\bmediumtext\b", "text", line)
        line = re.sub(r"\blongtext\b", "text", line)
        line = re.sub(r"\btinytext\b", "text", line)

        # datetime → timestamp
        line = re.sub(r"\bdatetime\b", "timestamp", line)

        # Remove CHARACTER SET (PG uses database-level encoding)
        line = re.sub(r"\s+CHARACTER SET \w+", "", line)

        # Remove ON UPDATE CURRENT_TIMESTAMP (PG doesn't support this)
        line = re.sub(r"\s*ON UPDATE CURRENT_TIMESTAMP", "", line)

        # Remove USING BTREE and clean up resulting " ," pattern
        line = line.replace("USING BTREE", "")
        line = re.sub(r"\s+,", ",", line)

        # Convert MySQL string escapes in INSERT lines
        if stripped.startswith("INSERT INTO"):
            line = convert_mysql_string_escapes(line)

        # Handle ENGINE=... line (table closing with comment)
        engine_match = re.match(
            r"^\) ENGINE=\S+ (?:DEFAULT CHARSET=\S+ )?(?:ROW_FORMAT=\S+ )?COMMENT='(.*)';$",
            stripped,
        )
        if engine_match and current_table:
            comment = engine_match.group(1).replace("'", "''")
            table_comments.append((current_table, comment))
            in_create_table = False
            if constraint_lines:
                last = constraint_lines[-1]
                constraint_lines[-1] = re.sub(r",\s*$", "", last)
            out_lines.extend(constraint_lines)
            constraint_lines = []
            out_lines.append(");")
            for idx_stmt in create_index_stmts:
                out_lines.append(idx_stmt)
            create_index_stmts = []
            continue

        # Handle ENGINE=... line without COMMENT
        if re.match(r"^\) ENGINE=\S+.*;$", stripped) and not stripped.startswith("--"):
            in_create_table = False
            if constraint_lines:
                last = constraint_lines[-1]
                constraint_lines[-1] = re.sub(r",\s*$", "", last)
            out_lines.extend(constraint_lines)
            constraint_lines = []
            out_lines.append(");")
            for idx_stmt in create_index_stmts:
                out_lines.append(idx_stmt)
            create_index_stmts = []
            continue

        # Extract column COMMENT before removing it
        if current_table and "COMMENT" in line and not stripped.startswith("--"):
            col_match = re.match(
                r"\s+(\w+)\s+\S+.*?COMMENT\s+'((?:[^'\\]|\\.)*)'", line
            )
            if col_match:
                col_name = col_match.group(1)
                comment = col_match.group(2).replace("'", "''")
                column_comments.append((current_table, col_name, comment))

            # Remove COMMENT '...' from column definition
            line = re.sub(r"\s*COMMENT\s+'(?:[^'\\]|\\.)*'", "", line)

        # UNIQUE KEY name (cols) → CONSTRAINT table_name UNIQUE (cols)
        # Prefix constraint name with table to avoid global name conflicts in PG
        uq_match = re.match(r"(.*)UNIQUE KEY\s+(\w+)\s*\(", line)
        if uq_match and current_table:
            prefix = uq_match.group(1)
            idx_name = uq_match.group(2)
            rest = line[uq_match.end():]
            line = f"{prefix}CONSTRAINT {current_table}_{idx_name} UNIQUE ({rest}"

        # KEY name (cols) → defer to CREATE INDEX
        key_match = re.match(r"\s*KEY\s+(\w+)\s*\((.+?)\)\s*,?\s*$", line)
        if key_match and in_create_table:
            idx_name = key_match.group(1)
            idx_cols = key_match.group(2)
            # Prefix index name with table name to avoid global name conflicts in PG
            pg_idx_name = f"{current_table}_{idx_name}"
            create_index_stmts.append(
                f"CREATE INDEX {pg_idx_name} ON {current_table} ({idx_cols});"
            )
            continue

        # DEFAULT '0' / DEFAULT '3.0' for numeric columns → unquote
        line = re.sub(
            r"DEFAULT\s+'(\d+(?:\.\d+)?)'",
            lambda m: f"DEFAULT {m.group(1)}",
            line,
        )

        # Remove unsigned
        line = re.sub(r"\bunsigned\b", "", line, flags=re.IGNORECASE)

        # Remove zerofill
        line = re.sub(r"\bzerofill\b", "", line, flags=re.IGNORECASE)

        # Handle AUTO_INCREMENT
        line = re.sub(r"\bAUTO_INCREMENT\b", "", line, flags=re.IGNORECASE)

        # Separate constraint lines from column lines
        is_constraint = bool(re.match(r"\s*(PRIMARY KEY|UNIQUE|CONSTRAINT)\s", stripped))

        if is_constraint and in_create_table:
            line = line.replace(" , ", ", ")
            constraint_lines.append(line)
        else:
            if constraint_lines and not is_constraint:
                out_lines.extend(constraint_lines)
                constraint_lines = []
            out_lines.append(line)

    # Append COMMENT ON statements
    if table_comments or column_comments:
        out_lines.append("")
        out_lines.append("-- Table and column comments")
        for tbl, comment in table_comments:
            out_lines.append(f"COMMENT ON TABLE {tbl} IS '{comment}';")
        for tbl, col, comment in column_comments:
            out_lines.append(f"COMMENT ON COLUMN {tbl}.{col} IS '{comment}';")

    with open(output_path, "w", encoding="utf-8") as f:
        f.write("\n".join(out_lines))

    print(f"Converted {input_path} → {output_path}")
    print(f"  Tables: {len(table_comments)}")
    print(f"  Column comments: {len(column_comments)}")


if __name__ == "__main__":
    convert(INPUT, OUTPUT)
