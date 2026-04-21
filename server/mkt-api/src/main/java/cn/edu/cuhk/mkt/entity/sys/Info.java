package cn.edu.cuhk.mkt.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统模块-配置表
 *
 * @author taok
 * @date 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_info")
public class Info extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 开放时间
     */
    @TableField("start_time")
    private String startTime;

    /**
     * 开放时间
     */
    @TableField("end_time")
    private String endTime;

    /**
     * 提示语
     */
    @TableField("message")
    private String message;


    @TableField("note_one")
    private String noteOne;

    @TableField("note_seven")
    private String noteSeven;

    @TableField("note_eight")
    private String noteEight;

    @TableField("teacher_list")
    private String teacherList;

    @TableField("code_list")
    private String codeList;

}
