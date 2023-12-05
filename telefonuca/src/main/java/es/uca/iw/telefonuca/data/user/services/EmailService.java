package es.uca.iw.telefonuca.data.user.services;


import es.uca.iw.telefonuca.data.user.domain.User;

public interface EmailService {

    boolean sendRegistrationEmail(User user);

}