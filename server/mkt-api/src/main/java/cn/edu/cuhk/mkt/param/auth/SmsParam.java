package cn.edu.cuhk.mkt.param.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 短信验证码参数对象
 * @author taokai
 */
@ApiModel(value = "SmsParam", description = "短信验证码参数对象")
@Data
public class SmsParam implements Serializable {

    /**
     * 业务域【AD短信验证码：AD_CAPTCHA】
     */
    @ApiModelProperty(value = "业务域【AD短信验证码：AD_CAPTCHA】", name = "bizScope")
    private String bizScope;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域", name = "areaCode")
    private String areaCode;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", name = "phone")
    private String phone;

}
