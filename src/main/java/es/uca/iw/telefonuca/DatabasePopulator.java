package es.uca.iw.telefonuca;

import com.github.javafaker.Faker;

import es.uca.iw.telefonuca.book.Book;
import es.uca.iw.telefonuca.book.BookService;
import es.uca.iw.telefonuca.user.domain.Role;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DatabasePopulator implements CommandLineRunner {

    BookService bookService;

    UserManagementService userService;

    public DatabasePopulator(BookService bookService, UserManagementService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();

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

        // Creamos libros si no hay ninguno
        if (bookService.count() == 0) {
            for (int i = 1; i < 50; i++) {
                Book book = new Book();
                book.setTitle(faker.book().title());
                book.setPublisher(faker.book().publisher());
                book.setAuthor(faker.book().author());
                bookService.createBook(book);
                System.out.println("Book created");
            }

        }

    }

}
