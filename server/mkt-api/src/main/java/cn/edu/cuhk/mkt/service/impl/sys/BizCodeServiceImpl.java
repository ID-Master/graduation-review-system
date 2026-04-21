package cn.edu.cuhk.mkt.service.impl.sys;

import cn.edu.cuhk.mkt.common.consts.BizConst;
import cn.edu.cuhk.mkt.common.redis.RedisSequenceFactory;
import cn.edu.cuhk.mkt.service.sys.BizCodeService;
import com.uneed.common.core.date.DateUtil;
import com.uneed.common.core.lang.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 流水号服务
 * @author taokai
 */
@Slf4j
@Service
public class BizCodeServiceImpl implements BizCodeService {
    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Override
    public String getSerialNumber(String prefixKey) {
        return getSerialNumber(prefixKey, new Date(), BizConst.DEFAULT_DIGIT);
    }

    @Override
    public String getSerialNumber(String prefixKey, Date date, int digit) {
        // 设置默认值
        prefixKey = StringUtils.defaultString(prefixKey);
        date = ObjectUtil.nullToDefault(date, new Date());
        if(digit < BizConst.MIN_DIGIT || digit > BizConst.MAX_DIGIT){
            digit = BizConst.DEFAULT_DIGIT;
        }
        // key = prefixKey + date
        StringBuilder key = new StringBuilder();
        String dateStr = DateUtil.toString(date, "yyyyMMdd");
        key.append(prefixKey)
                .append(dateStr);

        // 某天最后一秒
        date = DateUtil.toDateWithMaximum(date);

        //long value = redisSequenceFactory.generate(key.toString(), date);
        long value = redisSequenceFactory.generate(key.toString(), 30, TimeUnit.SECONDS);
        String strNumber = String.valueOf(value);
        strNumber = StringUtils.leftPad(strNumber, digit, "0");

        StringBuilder serialNumber = new StringBuilder(key.toString());
        serialNumber.append(strNumber);

        log.info("=======>redis key: {}, value: {}, serialNumber: {}, 当前毫秒数: {}, 当天最后一秒: {}, 失效时间: {}",
                key.toString(),
                value,
                serialNumber,
                System.currentTimeMillis(),
                DateUtil.toString(date, "yyyy-MM-dd HH:mm:ss.sss"),
                redisSequenceFactory.getExpire(key.toString())
        );
        return serialNumber.toString();
    }
}
