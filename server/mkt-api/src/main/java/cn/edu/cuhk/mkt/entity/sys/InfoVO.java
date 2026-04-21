package cn.edu.cuhk.mkt.entity.sys;

import cn.edu.cuhk.mkt.entity.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统模块-系统配置 vo对象
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@ApiModel(value = "UserVO", description = "系统模块-系统配置 VO对象")
public class InfoVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 开放时间
     */
    @ApiModelProperty(value = "开放时间", name = "startTime")
    private String startTime;

    /**
     * 开放时间
     */
    @ApiModelProperty(value = "开放时间", name = "endTime")
    private String endTime;

    /**
     * message
     */
    @ApiModelProperty(value = "提示语", name = "message")
    private String message;

    /**
     * invalid
     */
    @ApiModelProperty(value = "是否有效", name = "invalid")
    private Boolean invalid;

    @ApiModelProperty(value = "", name = "noteOne")
    private String noteOne;

    @ApiModelProperty(value = "", name = "noteSeven")
    private String noteSeven;

    @ApiModelProperty(value = "", name = "noteEight")
    private String noteEight;

    @ApiModelProperty(value = "", name = "teacherList")
    private String teacherList;

    @ApiModelProperty(value = "", name = "codeList")
    private String codeList;


}