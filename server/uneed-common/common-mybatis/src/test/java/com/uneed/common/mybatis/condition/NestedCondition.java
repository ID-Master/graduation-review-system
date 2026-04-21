package com.uneed.common.mybatis.condition;

import com.uneed.common.annotation.enums.Keyword;
import com.uneed.common.annotation.enums.NestedType;
import com.uneed.common.annotation.param.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NestedCondition {

    @Condition(value = Keyword.LIKE,fields = {"stuCode","stuName"},nestedType = NestedType.OR)
    private String keyword;
}
