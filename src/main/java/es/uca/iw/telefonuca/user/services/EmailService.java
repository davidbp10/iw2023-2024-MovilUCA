package es.uca.iw.telefonuca.user.services;

import java.io.File;

import es.uca.iw.telefonuca.user.domain.User;

public interface EmailService {

    boolean sendRegistrationEmail(User user);

    boolean sendPasswordResetEmail(User user);

    boolean sendPasswordChangedEmail(User user);

    boolean sendAccountActivatedEmail(User user);

    boolean sendEmailWithBill(User user, File bill);

}