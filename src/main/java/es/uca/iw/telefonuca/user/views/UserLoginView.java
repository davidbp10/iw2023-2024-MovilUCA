package es.uca.iw.telefonuca.user.views;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;

import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import es.uca.iw.telefonuca.config.TranslationProvider;

import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class UserLoginView extends LoginOverlay implements BeforeEnterObserver, LocaleChangeObserver {

    private final AuthenticatedUser authenticatedUser;
    private final TranslationProvider translationProvider;

    public UserLoginView(AuthenticatedUser authenticatedUser, TranslationProvider translationProvider) {
        this.authenticatedUser = authenticatedUser;
        this.translationProvider = translationProvider;

        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        final Locale currentLocale = LocaleContextHolder.getLocale();
        final LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("TelefonUCA");
        i18n.getHeader().setDescription(translationProvider.getTranslation("userLogin.description", currentLocale));
        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(true);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        final Locale currentLocale = LocaleContextHolder.getLocale();
        final LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("TelefonUCA");
        i18n.getHeader().setDescription(translationProvider.getTranslation("userLogin.description", currentLocale));
        i18n.setAdditionalInformation(null);
        setI18n(i18n);
    }
}
