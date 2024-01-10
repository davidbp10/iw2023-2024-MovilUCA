package es.uca.iw.telefonuca.user.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.telefonuca.user.domain.Role;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserManagementService implements UserDetailsService {

    private final UserRepository repository;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(UserRepository repository, EmailService emailService,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisterCode(UUID.randomUUID().toString());
        user.addRole(Role.USER);

        try {
            repository.save(user);
            emailService.sendRegistrationEmail(user);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return user.get();
        }
    }

    @Transactional
    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user present with email: " + email);
        } else {
            return user.get();
        }
    }

    @Transactional
    public User loadUserById(UUID userId) throws UsernameNotFoundException {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user present with ID: " + userId);
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

    public List<User> loadAllUsers() {
        return repository.findAll();
    }

    public List<User> loadUserByName(String name) {
        return repository.findByName(name);
    }

    public List<User> loadUserBySurname(String surname) {
        return repository.findBySurname(surname);
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

    @Transactional
    public void updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        repository.save(user);
    }
}
