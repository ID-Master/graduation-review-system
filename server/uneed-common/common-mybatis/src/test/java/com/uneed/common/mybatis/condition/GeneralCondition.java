package com.uneed.common.mybatis.condition;

import com.uneed.common.annotation.enums.Keyword;
import com.uneed.common.annotation.param.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralCondition implements Serializable {

    private static final long serialVersionUID = 7425858528546839625L;
    /**
     * 通用编号条件，适用classCode、teaCode、stuCode
     */
    @Condition(value = Keyword.LIKE, fields = {"classCode", "teaCode", "stuCode"})
    private String code;

    /**
     * 通用名称条件，适用teaName、stuName
     */
    @Condition(value = Keyword.LIKE, fields = {"teaName", "stuName"})
    private String name;
}
