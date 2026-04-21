package com.uneed.common.core.entity;

import com.uneed.common.core.annotation.TestAnnotation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单元测试用类
 *
 * @author diablo
 * @date 2018/7/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TestAnnotation(value = "b", name = "李-parent", enable = true)
    private String name;

    @TestAnnotation(value = "c", name = "王-parent", enable = true)
    private Integer age;

    private Integer studentId;
}
