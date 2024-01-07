package es.uca.iw.telefonuca.user;

import java.io.File;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.EmailService;

@Service
@Primary
public class EmailFakeService implements EmailService {

    @Override
    public boolean sendRegistrationEmail(User user) {

        String subject = "Welcome";
        String body = "You should active your account. "
                + "Go to http://localhost:8080/useractivation "
                + "and introduce your mail and the following code: "
                + user.getRegisterCode();

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean sendPasswordResetEmail(User user) {
        String subject = "Password reset";
        String body = "You should reset your password. "
                + "Go to http://localhost:8080/passwordreset "
                + "and introduce your mail and the following code: "
                + user.getRegisterCode();

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean sendPasswordChangedEmail(User user) {
        String subject = "Password changed";
        String body = "Your password has been changed. "
                + "If you didn't do it, please contact us.";

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean sendAccountActivatedEmail(User user) {
        String subject = "Account activated";
        String body = "Your account has been activated. "
                + "You can now login to the app.";

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean sendEmailWithBill(User user, File bill) {
        String subject = "Bill";
        String body = "Here is your bill.";

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            System.out.println("Bill: " + bill.getAbsolutePath());

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

}
