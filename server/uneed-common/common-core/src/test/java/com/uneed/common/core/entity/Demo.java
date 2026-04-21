package com.uneed.common.core.entity;

import com.uneed.common.core.annotation.TestAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单元测试用类
 *
 * @author diablo
 * @date 2018/7/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Demo extends User {

    @TestAnnotation(value = "a", name = "张", enable = true)
    private String code;

    @TestAnnotation(value = "b", name = "李", enable = true)
    private String name;

    private Integer age;

    private Integer studentId;
}
