package cn.edu.cuhk.mkt.entity.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 邮件发送对象
 *
 * @author taokai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDTO implements Serializable {
    /**
     * 邮件接收方，可多人
     */
    private String[] tos;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

}
