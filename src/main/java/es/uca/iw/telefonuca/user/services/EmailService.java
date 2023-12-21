package es.uca.iw.telefonuca.user.services;


import es.uca.iw.telefonuca.user.domain.User;

public interface EmailService {

    boolean sendRegistrationEmail(User user);

}