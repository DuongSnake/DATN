package com.example.bloodbankmanagement.service.email;

import ch.qos.logback.classic.Logger;
import com.example.bloodbankmanagement.common.untils.CommonUtil;
import com.example.bloodbankmanagement.common.untils.EmailConfig;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@AllArgsConstructor
public class MailServiceImpl {
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    private final JavaMailSender javaMailSender;

    private final ThymeleafService thymeleafService;

    private final EmailConfig emailConfig;

    public boolean sendEmailByTemplate(Map<String, String> params, String templateName, String subject){
        try {
            logger.info("Email start send: " + subject);
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress(emailConfig.getUsername()));
            if(CommonUtil.trueValue.equals(params.get(CommonUtil.SendToSingleMailYes))){
                logger.info("Send single mail: ");
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(params.get(CommonUtil.EMAIL))});
            }else{
                logger.info("Send multi mail: ");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params.get(CommonUtil.EMAIL)));
            }

            message.setSubject(subject);
            message.setContent(thymeleafService.getContentForEmailTemplate(params, templateName), CONTENT_TYPE_TEXT_HTML);

            logger.info("Email setting done");
            javaMailSender.send(message);
            logger.info("Email end send: ");
            return true;
        }catch (Exception e){
            logger.error("Email start exception:" + e.getMessage());
        }
        return false;
    }
}
