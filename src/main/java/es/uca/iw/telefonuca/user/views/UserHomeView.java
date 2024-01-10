package es.uca.iw.telefonuca.user.views;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.config.TranslationProvider;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;

@PageTitle("Inicio")
@PermitAll
@Route(value = "userhome", layout = MainLayout.class)
public class UserHomeView extends VerticalLayout implements LocaleChangeObserver {

  private final TranslationProvider translationProvider;

  public UserHomeView(TranslationProvider translationProvider, AuthenticatedUser authenticatedUser) {
        this.translationProvider = translationProvider;
        

        Locale currentLocale = LocaleContextHolder.getLocale();
        String welcomeText = translationProvider.getTranslation("userHome.welcome", currentLocale);
        String privateAreaText = translationProvider.getTranslation("userHome.message", currentLocale);

        add(new H1(welcomeText));
        add(new H2(privateAreaText));

        StreamResource imageResource = new StreamResource("logo.png",
            () -> getClass().getResourceAsStream("/images/logo.png"));

        Image image = new Image(imageResource, "Logo");
        add(image);

        // Centra los elementos vertical y horizontalmente
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
  }

  @Override
  public void localeChange(LocaleChangeEvent event) {
      Locale currentLocale = event.getLocale();
      String welcomeText = translationProvider.getTranslation("userHome.welcome", currentLocale);
      String privateAreaText = translationProvider.getTranslation("userHome.role", currentLocale);

      // Obtén los componentes existentes
      Component[] children = this.getChildren().toArray(Component[]::new);

      // Actualiza el texto de los componentes con las nuevas traducciones
      ((H1) children[0]).setText(welcomeText);
      ((H2) children[1]).setText(privateAreaText);
  }
}
