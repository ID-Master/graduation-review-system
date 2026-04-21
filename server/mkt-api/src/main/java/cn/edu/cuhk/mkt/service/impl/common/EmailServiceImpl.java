package cn.edu.cuhk.mkt.service.impl.common;

import cn.edu.cuhk.mkt.config.MailConfig;
import cn.edu.cuhk.mkt.entity.mail.MailDTO;
import cn.edu.cuhk.mkt.service.common.EmailService;
import com.uneed.common.support.api.Result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Result commonEmail(MailDTO toEmail) {
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(MailConfig.getFrom());
        //谁要接收
        message.setTo(toEmail.getTos());
        //邮件标题
        message.setSubject(toEmail.getSubject());
        //邮件内容
        message.setText(toEmail.getContent());
        mailSender.send(message);
        return Result.ok(toEmail.getTos(), "发送普通邮件成功");
    }

    @SneakyThrows
    @Override
    public Result htmlEmail(MailDTO toEmail) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
        //谁发
        minehelper.setFrom(MailConfig.getFrom());
        //谁要接收
        minehelper.setTo(toEmail.getTos());
        //邮件主题
        minehelper.setSubject(toEmail.getSubject());
        //邮件内容   true 表示带有附件或html
        minehelper.setText(toEmail.getContent(), true);
        mailSender.send(message);
        return Result.ok(toEmail.getTos() + toEmail.getContent(), "HTML邮件成功");
    }

    @SneakyThrows
    @Override
    public Result enclosureEmail(MailDTO toEmail, MultipartFile multipartFile) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //谁发
        helper.setFrom(MailConfig.getFrom());
        //谁接收
        helper.setTo(toEmail.getTos());
        //邮件主题
        helper.setSubject(toEmail.getSubject());
        //邮件内容   true 表示带有附件或html
        helper.setText(toEmail.getContent(), true);
        File multipartFileToFile = MultipartFileToFile(multipartFile);
        FileSystemResource file = new FileSystemResource(multipartFileToFile);
        String filename = file.getFilename();
        // 添加附件
        helper.addAttachment(filename, file);
        mailSender.send(message);
        return Result.ok(toEmail.getTos() + toEmail.getContent(), "附件邮件成功");
    }

    @SneakyThrows
    @Override
    public Result staticEmail(MailDTO toEmail, MultipartFile multipartFile, String resId) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //谁发
        helper.setFrom(MailConfig.getFrom());
        //谁接收
        helper.setTo(toEmail.getTos());
        //邮件主题
        helper.setSubject(toEmail.getSubject());
        //邮件内容 true 表示带有附件或html
        //邮件内容拼接
        String content =
                "<html><body><img width='250px' src=\'cid:" + resId + "\'>" + toEmail.getContent()
                        + "</body></html>";
        helper.setText(content, true);
        //蒋 multpartfile 转为file
        File multipartFileToFile = MultipartFileToFile(multipartFile);
        FileSystemResource res = new FileSystemResource(multipartFileToFile);

        //添加内联资源，一个id对应一个资源，最终通过id来找到该资源
        helper.addInline(resId, res);
        mailSender.send(message);
        return Result.ok(toEmail.getTos() + toEmail.getContent(), "嵌入静态资源的邮件已经发送");
    }

    /**
     * 将 multpartfile 转为file
     *
     * @return file
     */
    @SneakyThrows
    private File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        File file = File.createTempFile(fileName, prefix);
        multiFile.transferTo(file);
        return file;
    }

}
