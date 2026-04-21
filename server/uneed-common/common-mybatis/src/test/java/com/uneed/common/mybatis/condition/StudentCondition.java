package com.uneed.common.mybatis.condition;

import com.uneed.common.annotation.enums.Keyword;
import com.uneed.common.annotation.param.Condition;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/24
 */
@Data
public class StudentCondition {

    /**
     * 学生编号条件，用注解方式制定编号，like
     */
    @Condition(value = Keyword.LIKE, fields = {"stuCode"})
    private String code;

    /**
     * 学生姓名条件，like
     */
    @Condition(Keyword.LIKE)
    private String stuName;

    /**
     * 学生年龄条件，>
     */
    @Condition(Keyword.GT)
    private Integer stuAge;

    /**
     * 学生性别条件，=
     */
    private String stuSex;

    /**
     * 入学时间条件，<=
     */
    @Condition(Keyword.LE)
    private Date enrolmentTime;

    /**
     * 所属班级条件，in
     */
    @Condition(value = Keyword.IN,fields = {"classesId"})
    private List<Long> classesIds;
}
