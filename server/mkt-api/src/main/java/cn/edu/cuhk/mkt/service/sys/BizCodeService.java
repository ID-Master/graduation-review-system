package cn.edu.cuhk.mkt.service.sys;

import java.util.Date;

/**
 * 业务编码服务类
 *
 * @author taokai
 */
public interface BizCodeService {

    /**
     * 按天生成流水号.
     *
     * TODO: 处理分布式环境下服务器时间不一致问题
     * @param prefixKey
     * @return
     */
    String getSerialNumber(String prefixKey);

    /**
     * 按天生成流水号
     *
     * TODO: 处理分布式环境下服务器时间不一致问题
     * @param prefixKey
     * @param date
     * @param digit
     * @return
     */
    String getSerialNumber(String prefixKey, Date date, int digit);

}
