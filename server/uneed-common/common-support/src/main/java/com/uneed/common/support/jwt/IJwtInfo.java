package com.uneed.common.support.jwt;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 定义JWT认证数据的载体接口
 * </p>
 *
 * @author hcs
 * @date 2019/12/19
 */
public interface IJwtInfo extends Serializable {
    /**
     * 获取用户ID
     *
     * @return 用户的主键
     */
    String getId();

    /**
     * 获取用户名
     *
     * @return 全局唯一的名称
     */
    String getUniqueName();

    /**
     * 获取名称
     *
     * @return 用户名称
     */
    String getUserName();

    /**
     * 获取员工ID
     *
     * @return 员工ID
     */
    String getEmployeeId();

    /**
     * 获取客户（经销商）ID
     *
     * @return 客户（经销商）ID
     */
    String getCustId();

    /**
     * 获取门店ID
     * @return 门店ID
     */
    String getStoreId();

    /**
     * 获取超期时间
     * @return
     */
    Date getExpireTime();

}
