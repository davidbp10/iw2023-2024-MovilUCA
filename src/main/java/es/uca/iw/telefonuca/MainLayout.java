package es.uca.iw.telefonuca;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uca.iw.telefonuca.book.BookListView;
import es.uca.iw.telefonuca.book.BookManagementView;
import es.uca.iw.telefonuca.contract.views.ContractListView;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import es.uca.iw.telefonuca.user.views.UserHomeView;
import es.uca.iw.telefonuca.user.views.UserListView;

import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Locale;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

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
        

        addToNavbar(true, toggle, viewTitle, languageButton);
    }

    private void addDrawerContent() {
        H1 appName = new H1("TelefonUCA");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(UserHomeView.class)) {
            nav.addItem(new SideNavItem("Inicio", UserHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookListView.class)) {
            nav.addItem(new SideNavItem("Usuarios", UserListView.class, LineAwesomeIcon.USERS_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookManagementView.class)) {
            nav.addItem(new SideNavItem("Book Management", BookManagementView.class,
                    LineAwesomeIcon.BOOK_DEAD_SOLID.create()));
        }

        if (accessChecker.hasAccess(ContractListView.class)) {
            nav.addItem(new SideNavItem("Contratos", ContractListView.class,
                    LineAwesomeIcon.PAPERCLIP_SOLID.create()));
        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getUsername());
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getUsername());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
