package es.uca.iw.telefonuca.user.services;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import es.uca.iw.telefonuca.user.domain.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailRealService implements EmailService {
    private final JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailRealService.class);

    @Value("${spring.mail.username}")
    private String defaultMail;

    @Value("${server.port}")
    private int serverPort;

    public EmailRealService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String getServerUrl() {

        // Generate the server URL
        String serverUrl = "http://";
        serverUrl += InetAddress.getLoopbackAddress().getHostAddress();
        serverUrl += ":" + serverPort + "/";
        return serverUrl;

    }

    @Override
    public boolean sendRegistrationEmail(User user) {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Welcome";
        String body = "You should active your account. "
                + "Go to " + getServerUrl() + "useractivation "
                + "and introduce your mail and the following code: "
                + user.getRegisterCode();

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            logger.error("Error sending email", ex);
            return false;
        }

        return true;
    }

}
