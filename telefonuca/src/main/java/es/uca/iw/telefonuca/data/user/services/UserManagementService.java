package es.uca.iw.telefonuca.data.user.services;

import es.uca.iw.telefonuca.data.user.domain.Role;
import es.uca.iw.telefonuca.data.user.domain.User;
import es.uca.iw.telefonuca.data.user.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserManagementService implements UserDetailsService {

    private final UserRepository repository;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserManagementService(UserRepository repository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisterCode(UUID.randomUUID().toString().substring(0, 5));

        try {
            // Save the user first
            User savedUser = repository.save(user);
        
            // Then assign the role and save again
            savedUser.addRole(Role.USER);
            repository.save(savedUser);
            emailService.sendRegistrationEmail(user);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return user.get();
        }
    }


    public boolean activateUser(String email, String registerCode) {

        Optional<User> user = repository.findByEmail(email);

        if (user.isPresent() && user.get().getRegisterCode().equals(registerCode)) {
            user.get().setActive(true);
            repository.save(user.get());
            return true;

        } else {
            return false;
        }

    }


    public Optional<User> loadUserById(UUID userId) {
        return repository.findById(userId);
    }

    public List<User> loadActiveUsers() {
        return repository.findByActiveTrue();
    }

    public void delete(User testUser) {
        repository.delete(testUser);

    }


    public int count() {
        return (int) repository.count();
    }
}
