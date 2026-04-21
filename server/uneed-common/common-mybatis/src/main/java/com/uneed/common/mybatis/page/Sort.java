package com.uneed.common.mybatis.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Sort", description = "排序信息")
public class Sort implements Serializable {

    private static final long serialVersionUID = -8644917154009847471L;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段", name = "field", position = 1)
    private String field;

    /**
     * 是否升序
     */
    @ApiModelProperty(value = "是否升序", name = "asc", position = 2)
    private boolean asc;

}
