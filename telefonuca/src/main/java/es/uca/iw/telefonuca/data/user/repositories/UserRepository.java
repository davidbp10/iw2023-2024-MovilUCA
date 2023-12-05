package es.uca.iw.telefonuca.data.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.data.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findByActiveTrue();

    Optional<User> findByUsername(String username);

}