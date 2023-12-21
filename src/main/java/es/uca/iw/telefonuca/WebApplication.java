package es.uca.iw.telefonuca;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
