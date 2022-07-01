package controlling.contractRegister.service;

import controlling.contractRegister.mail.TemplateParams;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

@Service
public class MailSenderService {

    @Autowired
    private Environment environment;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration configuration;

    public void sendMail(String toAddress, String title, String template, TemplateParams templateParams) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setFrom(environment.getProperty("spring.mail.username"));
        mimeMessageHelper.setSubject(title);
        String emailContent = getEmailContent(template, templateParams);
        mimeMessageHelper.setText(emailContent, true);

        javaMailSender.send(mimeMessage);
    }

    private String getEmailContent(String template, TemplateParams templateParams) throws Exception {
        Map<String, Object> model = Collections.singletonMap("templateParams", templateParams);
        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate(template).process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}