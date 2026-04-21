package cn.edu.cuhk.mkt.service.common;

import cn.edu.cuhk.mkt.entity.mail.MailDTO;
import com.uneed.common.support.api.Result;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {

    /**
     * 发送普通邮件（无其他资源 无html 无附件）
     *
     * @param toEmail 发送对象
     * @return 统一返回ajax
     */
    Result commonEmail(MailDTO toEmail);

    /**
     * 发送html形式邮件
     *
     * @param toEmail 发送对象
     * @return 统一返回ajax
     */
    Result htmlEmail(MailDTO toEmail);

    /**
     * 带附件邮件发送
     *
     * @param toEmail
     * @param multipartFile
     * @return
     */
    Result enclosureEmail(MailDTO toEmail, MultipartFile multipartFile);

    /**
     * 一同发送静态资源 图片等
     *
     * @param toEmail
     * @param multipartFile
     * @param resId 每个资源对应给一个Id
     * @return
     */
    Result staticEmail(MailDTO toEmail, MultipartFile multipartFile, String resId);

}
