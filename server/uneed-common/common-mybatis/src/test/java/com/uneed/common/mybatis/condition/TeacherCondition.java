package com.uneed.common.mybatis.condition;

import com.uneed.common.annotation.enums.Keyword;
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
public class TeacherCondition {

    /**
     * 教师编号条件
     */
    @Condition(value = Keyword.LIKE, fields = {"teaCode"})
    private String code;

    /**
     * 教师姓名条件
     */
    @Condition(value = Keyword.LIKE, fields = {"teaName"})
    private String name;

    /**
     * 教师性别条件
     */
    @Condition(fields = {"teaSex"})
    private String sex;

}
