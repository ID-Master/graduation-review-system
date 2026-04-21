package com.uneed.common.mybatis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 业务实体的超类，该类主要用来定义业务实体的主键。
 *
 * @author diablo
 * @date 2020/4/1
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IdModel implements Serializable {

    private static final long serialVersionUID = -8625546314565905336L;

    /**
     * 主键
     */
    private String id;

}
