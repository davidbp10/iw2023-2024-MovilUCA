package es.uca.iw.telefonuca;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.iw.telefonuca.user.views.UserActivationView;
import es.uca.iw.telefonuca.user.views.UserHomeView;
import es.uca.iw.telefonuca.user.views.UserRegistrationView;
import es.uca.iw.telefonuca.config.TranslationProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;

@PageTitle("Inicio")
@Route(value = "")
@AnonymousAllowed
public class LandingView extends VerticalLayout implements LocaleChangeObserver {

    private final TranslationProvider translationProvider;

    @Value("${app.version}")
    private String appVersion;

    public LandingView(TranslationProvider translationProvider) {
        this.translationProvider = translationProvider;

        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setHeightFull();
    }

    @PostConstruct
    public void init() {
        Locale currentLocale = LocaleContextHolder.getLocale();

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setAlignItems(Alignment.START);
        headerLayout.setJustifyContentMode(JustifyContentMode.END);

        RadioButtonGroup<String> languageButton = new RadioButtonGroup<>();
        languageButton.setItems("Español", "English");
        languageButton.addClassName("language-buttons");

        languageButton.addValueChangeListener(event -> {
            VaadinSession session = VaadinSession.getCurrent();
            if (event.getValue().equals("English")) {
                session.setLocale(new Locale("en", "GB"));
            } else {
                session.setLocale(new Locale("es", "ES"));
            }
        });

        headerLayout.add(languageButton);

        H1 title1 = new H1(translationProvider.getTranslation("landing.title1", currentLocale));
        title1.getElement().getStyle().set("background-color", "#f0f0f0"); // Cambia el color de fondo a gris claro
        title1.getElement().getStyle().set("color", "#000000"); // Cambia el color del texto a negro
        this.add(title1);

        add(new H2(translationProvider.getTranslation("landing.title2", currentLocale)));
        add(new H2(translationProvider.getTranslation("landing.title3", currentLocale)));
        add(new H3(translationProvider.getTranslation("landing.title4", currentLocale) + " " + appVersion));

        RouterLink homeLink = new RouterLink(translationProvider.getTranslation("landing.link1", currentLocale), UserHomeView.class);
        homeLink.getElement().getStyle().set("color", "#0000FF"); // Cambia el color del texto a azul
        homeLink.getElement().addEventListener("mouseover", event -> {
            homeLink.getElement().getStyle().set("background-color", "#FFFF00"); // Cambia el color de fondo a amarillo cuando el mouse pasa sobre el enlace
        });
        homeLink.getElement().addEventListener("mouseout", event -> {
            homeLink.getElement().getStyle().remove("background-color"); // Remueve el color de fondo cuando el mouse sale del enlace
        });
        this.add(homeLink);


        RouterLink userRegistrationLink = new RouterLink(translationProvider.getTranslation("landing.link2", currentLocale), UserRegistrationView.class);
        add(userRegistrationLink);

        RouterLink userActivationLink = new RouterLink(translationProvider.getTranslation("landing.link3", currentLocale), UserActivationView.class);
        add(userActivationLink);

        add(languageButton);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        Locale currentLocale = event.getLocale();

        ((H1)getComponentAt(0)).setText(translationProvider.getTranslation("landing.title1", currentLocale));
        ((H2)getComponentAt(1)).setText(translationProvider.getTranslation("landing.title2", currentLocale));
        ((H2)getComponentAt(2)).setText(translationProvider.getTranslation("landing.title3", currentLocale));
        ((H3)getComponentAt(3)).setText(translationProvider.getTranslation("landing.title4", currentLocale) + " " + appVersion);

        ((RouterLink)getComponentAt(4)).setText(translationProvider.getTranslation("landing.link1", currentLocale));
        ((RouterLink)getComponentAt(5)).setText(translationProvider.getTranslation("landing.link2", currentLocale));
        ((RouterLink)getComponentAt(6)).setText(translationProvider.getTranslation("landing.link3", currentLocale));
    }
}

