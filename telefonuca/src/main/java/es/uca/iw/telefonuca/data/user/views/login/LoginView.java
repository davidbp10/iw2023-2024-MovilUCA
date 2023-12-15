package es.uca.iw.telefonuca.data.user.views.login;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.telefonuca.data.user.domain.User;
import es.uca.iw.telefonuca.data.user.services.UserManagementService;
import es.uca.iw.telefonuca.data.user.views.register.RegisterView;
import es.uca.iw.telefonuca.views.welcome.WelcomeView;

@Route("login")
@PageTitle("Login | TelefonUCA")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private LoginForm login = new LoginForm();
    private RouterLink registerLink = new RouterLink("Register", RegisterView.class);

    public LoginView(UserManagementService userManagementService, PasswordEncoder passwordEncoder) {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");

        add(new H1("TelefonUCA"), login, registerLink);

        login.addLoginListener(event -> {
            try {
                User user = userManagementService.loadUserByUsername(event.getUsername());
                if (passwordEncoder.matches(event.getPassword(), user.getPassword())) {
                    getUI().ifPresent(ui -> ui.navigate(WelcomeView.class));
                } else {
                    login.setError(true);
                }
            } catch (UsernameNotFoundException e) {
                login.setError(true);
            }
        }
    );
        
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}


