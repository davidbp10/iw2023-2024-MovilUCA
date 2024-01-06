package es.uca.iw.telefonuca.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findByName(String name);

    List<User> findBySurname(String surname);

    List<User> findByRoles(String role);

    Optional<User> findById(String id);

    List<User> findByActiveTrue();

    Optional<User> findByUsername(String username);

    Optional<User> findByNameAndSurname(String name, String surname);
}