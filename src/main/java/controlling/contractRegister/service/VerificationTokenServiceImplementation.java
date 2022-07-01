package controlling.contractRegister.service;

import controlling.contractRegister.mail.TemplateParams;
import controlling.contractRegister.model.VerificationToken;
import controlling.contractRegister.repository.UserRepository;
import controlling.contractRegister.repository.VerificationTokenRepository;
import controlling.contractRegister.web.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class VerificationTokenServiceImplementation implements VerificationTokenService {

    @Autowired
    Environment environment;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void save(UserDTO user) throws Exception {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(userRepository.findByEmail(user.getEmail()));
        verificationTokenRepository.save(verificationToken);
        sendVerificationEmail(user, verificationToken);
    }

    @Override
    public void sendVerificationEmail(UserDTO user, VerificationToken token) throws Exception {
        List<String> templateProperties = Arrays.asList(environment.getProperty("server.address"),
                environment.getProperty("server.port"));
        List<String> templateMessages = Arrays.asList(messageSource.getMessage("verification.email.hello", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("verification.email.body", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("verification.email.verify", null, LocaleContextHolder.getLocale()),
                token.getToken().toString(),
                user.getFirstName());

        TemplateParams templateParams = new TemplateParams(templateProperties, templateMessages);

        mailSenderService.sendMail(user.getEmail(),
                messageSource.getMessage("verification.email.title", null, LocaleContextHolder.getLocale()),
                "security/verification-token-email.html",
                templateParams);
    }

    @Override
    public VerificationToken getVerificationTokenByToken(UUID token) {
        return verificationTokenRepository.findByToken(token);
    }
}