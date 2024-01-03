package es.uca.iw.telefonuca.user.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import es.uca.iw.telefonuca.config.TranslationProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

@PageTitle("Activate User")
@Route(value = "useractivation")
@Component // Required for unit testing
@Scope("prototype") // Required for IT testing
@AnonymousAllowed
public class UserActivationView extends VerticalLayout implements LocaleChangeObserver {

    @Serial
    private static final long serialVersionUID = 851217309689685413L;

    private final UserManagementService service;
    private final TranslationProvider translationProvider;
    private final H1 title;
    private final TextField email;
    private final TextField secretCode;
    private final Button activate;
    private final H4 status;

    public UserActivationView(UserManagementService service, TranslationProvider translationProvider) {
        this.service = service;
        this.translationProvider = translationProvider;

        Locale currentLocale = LocaleContextHolder.getLocale();
        String titleText = translationProvider.getTranslation("userActivation.title", currentLocale);
        String emailText = translationProvider.getTranslation("userActivation.email", currentLocale);
        String secretCodeText = translationProvider.getTranslation("userActivation.secretCode", currentLocale);
        String activateText = translationProvider.getTranslation("userActivation.activate", currentLocale);

        title = new H1(titleText);
        email = new TextField(emailText);
        email.setId("email");

        secretCode = new TextField(secretCodeText);
        secretCode.setId("secretCode");

        status = new H4();
        status.setId("status");

        activate = new Button(activateText);
        activate.setId("activate");

        status.setVisible(false);

        setMargin(true);

        add(title, new HorizontalLayout(email, secretCode), activate, status);

        activate.addClickListener(e -> onActivateButtonClick());
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        Locale currentLocale = event.getLocale();
        String titleText = translationProvider.getTranslation("userActivation.title", currentLocale);
        String emailText = translationProvider.getTranslation("userActivation.email", currentLocale);
        String secretCodeText = translationProvider.getTranslation("userActivation.secretCode", currentLocale);
        String activateText = translationProvider.getTranslation("userActivation.activate", currentLocale);

        title.setText(titleText);
        email.setLabel(emailText);
        secretCode.setLabel(secretCodeText);
        activate.setText(activateText);
    }

    public void onActivateButtonClick() {
        status.setVisible(true);

        if (service.activateUser(email.getValue(), secretCode.getValue())) {
            status.setText(translationProvider.getTranslation("userActivation.success", LocaleContextHolder.getLocale()));
        } else {
            status.setText(translationProvider.getTranslation("userActivation.failure", LocaleContextHolder.getLocale()));
        }
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public void setSecretCode(String secretCode) {
        this.secretCode.setValue(secretCode);
    }

    public String getStatus() {
        return status.getText();
    }
}
