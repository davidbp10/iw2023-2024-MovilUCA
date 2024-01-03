package es.uca.iw.telefonuca.user.views;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.book.BookListView;
import es.uca.iw.telefonuca.config.TranslationProvider;
import jakarta.annotation.security.PermitAll;

@PageTitle("Inicio")
@PermitAll
@Route(value = "userhome", layout = MainLayout.class)
public class UserHomeView extends VerticalLayout implements LocaleChangeObserver{

    private final TranslationProvider translationProvider;

    public UserHomeView(TranslationProvider translationProvider) {
        this.translationProvider = translationProvider;

        Locale currentLocale = LocaleContextHolder.getLocale();
        String welcomeText = translationProvider.getTranslation("userHome.welcome", currentLocale);
        String privateAreaText = translationProvider.getTranslation("userHome.message", currentLocale);
        
        add(new H1(welcomeText));
        add(new H2(privateAreaText));

        RouterLink bookLink = new RouterLink(translationProvider.getTranslation("userHome.linkBook", currentLocale), BookListView.class);
        add(bookLink);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        Locale currentLocale = event.getLocale();
        String welcomeText = translationProvider.getTranslation("userHome.welcome", currentLocale);
        String privateAreaText = translationProvider.getTranslation("userHome.message", currentLocale);
        
        // Obtén los componentes existentes
        Component[] children = this.getChildren().toArray(Component[]::new);


        // Actualiza el texto de los componentes con las nuevas traducciones
        ((H1) children[0]).setText(welcomeText);
        ((H2) children[1]).setText(privateAreaText);
        ((RouterLink) children[2]).setText(translationProvider.getTranslation("userHome.linkBook", currentLocale));
    }
}

