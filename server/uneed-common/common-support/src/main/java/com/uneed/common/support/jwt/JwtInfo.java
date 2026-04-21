package com.uneed.common.support.jwt;

import java.util.Date;

import static com.uneed.common.core.lang.ObjectUtil.equal;

/**
 * <p>
 * JWT认证数据载体的实现类，这里主要是用来承载用户认证数据
 * </p>
 */
public class JwtInfo implements IJwtInfo {

    private static final long serialVersionUID = -5763829298452527274L;

    /**
     * 用户主键id
     */
    private String id;

    /**
     * 用户登录名
     */
    private String uniqueName;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 员工ID
     */
    private String employeeId;

    /**
     * 客户（经销商）ID
     */
    private String custId;

    /**
     * 门店ID
     */
    private String storeId;

    /**
     * 超期时间
     */
    private Date expireTime;

    public JwtInfo(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public JwtInfo(String uniqueName, String id, String userName, Date expireTime) {
        this.uniqueName = uniqueName;
        this.id = id;
        this.userName = userName;
        this.expireTime = expireTime;
    }

    public JwtInfo(String id, String uniqueName, String userName, String employeeId, String custId, String storeId) {
        this.id = id;
        this.uniqueName = uniqueName;
        this.userName = userName;
        this.employeeId = employeeId;
        this.custId = custId;
        this.storeId = storeId;
    }

    @Override
    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        JwtInfo info = (JwtInfo) obj;

        if (equal(uniqueName, info.getUniqueName())) {
            return true;
        }

        return equal(id, info.getId());
    }

    @Override
    public int hashCode() {
        int result = uniqueName != null ? uniqueName.hashCode() : 0;
        return 31 * result + (id != null ? id.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "JwtInfo{" +
                "uniqueName='" + uniqueName + '\'' +
                ", id=" + id +
                ", userName='" + userName + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}
