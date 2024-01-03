package es.uca.iw.telefonuca.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import es.uca.iw.telefonuca.config.TranslationProvider;

import java.io.Serial;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;

@Route(value = "userregistration")
@AnonymousAllowed
public class UserRegistrationView extends VerticalLayout implements LocaleChangeObserver {

    @Serial
    private static final long serialVersionUID = 851217309689685413L;

    private final UserManagementService service;
    private final TranslationProvider translationProvider;

    private final H1 title;

    private final TextField username;
    private final TextField name;
    private final TextField surname;
    private final EmailField email;
    private final PasswordField password;
    private final PasswordField password2;

    private final Button register;
    private final H4 status;

    private final BeanValidationBinder<User> binder;

    public UserRegistrationView(UserManagementService service, TranslationProvider translationProvider) {
        this.service = service;
        this.translationProvider = translationProvider;

        Locale currentLocale = LocaleContextHolder.getLocale();
        String titleText = translationProvider.getTranslation("userRegistration.title", currentLocale);
        title = new H1(titleText);

        username = new TextField(translationProvider.getTranslation("userRegistration.username", currentLocale));
        username.setId("username");

        name = new TextField(translationProvider.getTranslation("userRegistration.name", currentLocale));
        name.setId("name");

        surname = new TextField(translationProvider.getTranslation("userRegistration.surname", currentLocale));
        surname.setId("surname");

        email = new EmailField(translationProvider.getTranslation("userRegistration.email", currentLocale));
        email.setId("email");

        password = new PasswordField(translationProvider.getTranslation("userRegistration.password", currentLocale));
        password.setId("password");

        password2 = new PasswordField(translationProvider.getTranslation("userRegistration.password2", currentLocale));
        password2.setId("password2");

        register = new Button(translationProvider.getTranslation("userRegistration.register", currentLocale));
        register.setId("register");

        status = new H4();
        status.setId("status");
        status.setVisible(false);

        setMargin(true);

        add(title, username, name, surname, email, password, password2, register, status);

        register.addClickListener(e -> onRegisterButtonClick());

        binder = new BeanValidationBinder<>(User.class);
        binder.bindInstanceFields(this);

        binder.setBean(new User());
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        Locale currentLocale = event.getLocale();
        title.setText(translationProvider.getTranslation("userRegistration.title", currentLocale));
        username.setLabel(translationProvider.getTranslation("userRegistration.username", currentLocale));
        name.setLabel(translationProvider.getTranslation("userRegistration.name", currentLocale));
        surname.setLabel(translationProvider.getTranslation("userRegistration.surname", currentLocale));
        email.setLabel(translationProvider.getTranslation("userRegistration.email", currentLocale));
        password.setLabel(translationProvider.getTranslation("userRegistration.password", currentLocale));
        password2.setLabel(translationProvider.getTranslation("userRegistration.password2", currentLocale));
        register.setText(translationProvider.getTranslation("userRegistration.register", currentLocale));
    }

    /**
     * Handler
     */
    public void onRegisterButtonClick() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (binder.validate().isOk() & password.getValue().equals(password2.getValue())) {
            if (service.registerUser(binder.getBean())) {
                status.setText(translationProvider.getTranslation("userRegistration.success", currentLocale));
                status.setVisible(true);
                binder.setBean(new User());
                password2.setValue("");
            } else {
                Notification.show(translationProvider.getTranslation("userRegistration.usernameInUse", currentLocale));
            }
        } else {
            Notification.show(translationProvider.getTranslation("userRegistration.checkInputData", currentLocale));
        }
    }
}

