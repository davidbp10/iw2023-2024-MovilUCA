package es.uca.iw.telefonuca.data.user.views.register;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import es.uca.iw.telefonuca.data.user.domain.User;
import es.uca.iw.telefonuca.data.user.views.login.LoginView;
import es.uca.iw.telefonuca.data.user.repositories.UserRepository;
import es.uca.iw.telefonuca.data.user.services.UserManagementService;

@Route("register")
@PageTitle("Register | TelefonUCA")
@AnonymousAllowed
public class RegisterView extends Composite<VerticalLayout> {

    private final UserRepository userRepository;
    private final UserManagementService userManagementService;

    public RegisterView(UserRepository userRepository, UserManagementService userManagementService) {
        this.userRepository = userRepository;
        this.userManagementService = userManagementService;

         VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField usernameField = new TextField();
        EmailField emailField = new EmailField();
        PasswordField passwordField = new PasswordField();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button registerButton = new Button();
        Button loginButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Register");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        registerButton.setText("Register");
        registerButton.setWidth("min-content");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setText("Go to Login");
        loginButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        usernameField.setLabel("Username");
        emailField.setLabel("Email");
        passwordField.setLabel("Password");
        formLayout2Col.add(usernameField);
        formLayout2Col.add(emailField);
        formLayout2Col.add(passwordField);
        layoutColumn2.add(layoutRow);
        layoutRow.add(registerButton);
        layoutRow.add(loginButton);

        registerButton.addClickListener(event -> {
            User user = new User();
            user.setUsername(usernameField.getValue());
            user.setEmail(emailField.getValue());
            user.setPassword(passwordField.getValue()); // You should hash the password before storing it
            
            userManagementService.registerUser(user);
        });

        loginButton.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });
    }
}
