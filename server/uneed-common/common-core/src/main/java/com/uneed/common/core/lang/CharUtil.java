package com.uneed.common.core.lang;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;
import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * 字符工具类
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class CharUtil {

    /**
     * 字符数组常量
     */
    private static final String[] CHAR_STRING_ARRAY = new String[128];

    /**
     * 十六进制数字数组常量
     */
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /*
      构造ASCII字符的缓存数据
     */
    static {
        for (char c = 0; c < CHAR_STRING_ARRAY.length; c++) {
            CHAR_STRING_ARRAY[c] = String.valueOf(c);
        }
    }

    /**
     * 换行符
     */
    public static final char LF = '\n';

    /**
     * 回车符
     */
    public static final char CR = '\r';

    /**
     * 空字符
     */
    public static final char NUL = '\0';

    /**
     * 空格
     */
    public static final char SPACE = ' ';

    /**
     * 制表符
     */
    public static final char TAB = '	';

    /**
     * 点
     */
    public static final char DOT = '.';

    /**
     * 正斜杠
     */
    public static final char SLASH = '/';

    /**
     * 反斜杠
     */
    public static final char BACKSLASH = '\\';

    /**
     * 下划线
     */
    public static final char UNDERLINE = '_';

    /**
     * 破折号
     */
    public static final char DASHED = '-';

    /**
     * 英文逗号
     */
    public static final char COMMA = ',';

    /**
     * 大括号开始
     */
    public static final char DELIM_START = '{';

    /**
     * 大括号结束
     */
    public static final char DELIM_END = '}';

    /**
     * 中括号开始
     */
    public static final char BRACKET_START = '[';

    /**
     * 中括号结束
     */
    public static final char BRACKET_END = ']';

    /**
     * 冒号
     */
    public static final char COLON = ':';

    /**
     * 双引号
     */
    public static final char DOUBLE_QUOTES = '"';

    /**
     * 单引号
     */
    public static final char SINGLE_QUOTE = '\'';

    /**
     * and 符号
     */
    public static final char AMP = '&';


    /**
     * 私有化构造函数，禁止实例化该类
     */
    private CharUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //-----------------------------------------------------------------------

    /**
     * 是否为ASCII字符，ASCII字符位于0~127之间
     *
     * <pre>
     *   CharUtil.isAscii('a')  = true
     *   CharUtil.isAscii('A')  = true
     *   CharUtil.isAscii('3')  = true
     *   CharUtil.isAscii('-')  = true
     *   CharUtil.isAscii('\n') = true
     *   CharUtil.isAscii('&copy;') = false
     * </pre>
     *
     * @param ch 被检查的字符处
     * @return true表示为ASCII字符，ASCII字符位于0~127之间
     */
    public static boolean isAscii(char ch) {
        return ch < 128;
    }

    /**
     * 给定对象对应的类是否为字符类，字符类包括：
     *
     * <pre>
     * Character.class
     * char.class
     * </pre>
     *
     * @param obj 被检查的对象
     * @return true表示为字符类
     */
    public static boolean isChar(Object obj) {
        return isNotNull(obj) && obj instanceof Character;
    }

    /**
     * <p>将字符串的第一个字符转换为字符，为空的情况下抛出空字符串上的异常.</p>
     *
     * <pre>
     *   CharUtils.toChar("A")  = 'A'
     *   CharUtils.toChar("BA") = 'B'
     *   CharUtils.toChar(null) throws IllegalArgumentException
     *   CharUtils.toChar("")   throws IllegalArgumentException
     * </pre>
     *
     * @param str 要转换的字符
     * @return 字符串第一个字母的字符值
     * @throws IllegalArgumentException 如果字符串为空
     */
    public static char toChar(final String str) {
        Validate.isTrue(StringUtil.isNotEmpty(str), "The String must not be empty");
        return str.charAt(0);
    }

    /**
     * <p>将字符串的第一个字符转换为字符，为空的情况下使用默认值.</p>
     *
     * <pre>
     *   CharUtils.toChar(null, 'X') = 'X'
     *   CharUtils.toChar("", 'X')   = 'X'
     *   CharUtils.toChar("A", 'X')  = 'A'
     *   CharUtils.toChar("BA", 'X') = 'B'
     * </pre>
     *
     * @param str          要转换的字符
     * @param defaultValue 字符为空时使用的值
     * @return 字符串第一个字母的字符值，如果为空则为默认值
     */
    public static char toChar(final String str, final char defaultValue) {
        return StringUtil.isEmpty(str) ? defaultValue : str.charAt(0);
    }

    /**
     * <p>将字符转换为它表示的整数，如果字符不是数字，则引发异常.</p>
     * <p>此方法将字符“1”转换为int 1，依此类推.</p>
     *
     * <pre>
     *   CharUtils.toIntValue('3')  = 3
     *   CharUtils.toIntValue(null) throws IllegalArgumentException
     *   CharUtils.toIntValue('A')  throws IllegalArgumentException
     * </pre>
     *
     * @param ch 要转换的字符, 不能为空
     * @return 字符的int值
     * @throws IllegalArgumentException 如果字符不是ascii数字或为空
     */
    public static int toInt(final Character ch) {
        Validate.isTrue(isNotNull(ch), "The character must not be null");
        if (!Character.isDigit(ch)) {
            throw new IllegalArgumentException("The character " + ch + " is not in the range '0' - '9'");
        }
        return ch - 48;
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     */
    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a';
    }

    /**
     * <p>将字符转换为它表示的整数，如果字符不是数字，使用默认值.</p>
     * <p>此方法将字符“1”转换为int 1，依此类推.</p>
     *
     * <pre>
     *   CharUtils.toIntValue(null, -1) = -1
     *   CharUtils.toIntValue('3', -1)  = 3
     *   CharUtils.toIntValue('A', -1)  = -1
     * </pre>
     *
     * @param ch           要转换的字符
     * @param defaultValue 如果字符不是数字，则使用的默认值
     * @return 字符的int值
     */
    public static int toInt(final Character ch, final int defaultValue) {
        return (isNull(ch) || !Character.isDigit(ch)) ? defaultValue : ch - 48;
    }

    /**
     * <p>将字符转换为包含一个字符的字符串.</p>
     * <p>对于ascii 7位字符，它使用一个缓存，该缓存将返回每次都是相同的字符串对象.</p>
     *
     * <pre>
     *   CharUtils.toString(null) = null
     *   CharUtils.toString(' ')  = " "
     *   CharUtils.toString('A')  = "A"
     * </pre>
     *
     * @param ch 要转换的字符
     * @return 包含一个指定字符的字符串
     */
    public static String toString(final Character ch) {
        return isNull(ch) ? null : isAscii(ch) ? CHAR_STRING_ARRAY[ch] : new String(new char[]{ch});
    }

    /**
     * <p>将字符串转换为Unicode格式 '\u0020'.</p>
     *
     * <p>此格式是Java源代码格式.</p>
     *
     * <pre>
     *   CharUtils.unicodeEscaped(null) = null
     *   CharUtils.unicodeEscaped(' ')  = "\u0020"
     *   CharUtils.unicodeEscaped('A')  = "\u0041"
     * </pre>
     *
     * @param ch 要转换的字符
     * @return 转义的Unicode字符串
     */
    public static String unicodeEscaped(final Character ch) {
        return isNull(ch) ? null : "\\u" + HEX_DIGITS[(ch >> 12) & 15] + HEX_DIGITS[(ch >> 8) & 15] + HEX_DIGITS[(ch >> 4) & 15] + HEX_DIGITS[(ch) & 15];
    }
}
