package es.uca.iw.telefonuca;

import es.uca.iw.telefonuca.user.domain.Role;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator implements CommandLineRunner {

    UserManagementService userService;

    public DatabasePopulator(UserManagementService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        /// Creamos usuarios para cada rol
        if (userService.count() == 0) {
            Role[] roles = Role.values();
            for (Role role : roles) {
                User user = new User();
                user.setUsername(role.name().toLowerCase());
                user.setPassword(role.name().toLowerCase());
                user.setEmail(role.name().toLowerCase() + "@uca.es");
                user.setName(role.name().toLowerCase());
                user.setSurname(role.name().toLowerCase());
                user.addRole(role);
                userService.registerUser(user);
                userService.activateUser(user.getEmail(), user.getRegisterCode());
                System.out.println(role.name() + " created");
            }
        }

    }

}
