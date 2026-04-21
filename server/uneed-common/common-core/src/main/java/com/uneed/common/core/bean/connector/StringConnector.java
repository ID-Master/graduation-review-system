package com.uneed.common.core.bean.connector;

import java.io.Serializable;

/**
 * 字符串转换连接器配置
 * <p>
 * 默认的规则:
 * 1.连接符使用","
 * 2.会拼接null或者empty元素
 * 3.如果元素是null,使用""替代拼接
 * 4.最后一个元素后面不拼接拼接符
 *
 * @author diablo
 * @date 17/12/27
 */
public final class StringConnector implements Serializable {

    private static final long serialVersionUID = 3177434368139940290L;

    /**
     * 默认逗号连接
     */
    public static final String DEFAULT_CONNECTOR = ",";

    /**
     * 连接符
     */
    private String connector = DEFAULT_CONNECTOR;

    /**
     * 是否拼接null或empty
     */
    private boolean joinEmpty = true;

    public StringConnector() {

    }

    public StringConnector(String connector) {
        this.connector = connector;
    }

    public StringConnector(String connector, boolean joinEmpty) {
        this.connector = connector;
        this.joinEmpty = joinEmpty;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public boolean isJoinEmpty() {
        return joinEmpty;
    }

    public void setJoinEmpty(boolean joinEmpty) {
        this.joinEmpty = joinEmpty;
    }
}
