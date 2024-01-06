package es.uca.iw.telefonuca;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import es.uca.iw.telefonuca.line.views.CallRecordManagementView;
import es.uca.iw.telefonuca.line.views.CallRecordUserView;
import es.uca.iw.telefonuca.line.views.NewCallRecordView;
import es.uca.iw.telefonuca.line.views.NewCustomerLineView;
import es.uca.iw.telefonuca.line.views.NewDataRecordView;
import es.uca.iw.telefonuca.line.views.NewLineView;
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
        SideNavItem customerSection = new SideNavItem("Portal de cliente");
        customerSection.setPrefixComponent(VaadinIcon.USER.create());
        if (accessChecker.hasAccess(UserHomeView.class)) {
            customerSection.addItem(new SideNavItem("Inicio", UserHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(CallRecordUserView.class)) {
            customerSection
                    .addItem(new SideNavItem("Mi registro de llamadas", CallRecordUserView.class,
                            LineAwesomeIcon.USERS_SOLID.create()));
        }

        if (accessChecker.hasAccess(NewCallRecordView.class)) {
            customerSection.addItem(new SideNavItem("Nuevo registro de llamada", NewCallRecordView.class,
                    LineAwesomeIcon.BOOK_DEAD_SOLID.create()));
        }

        if (accessChecker.hasAccess(ContractListView.class)) {
            customerSection.addItem(new SideNavItem("Contratos", ContractListView.class,
                    LineAwesomeIcon.PAPERCLIP_SOLID.create()));
        }

        SideNavItem salesSection = new SideNavItem("Portal de ventas");
        salesSection.setPrefixComponent(VaadinIcon.USER.create());
        if (accessChecker.hasAccess(UserHomeView.class)) {
            salesSection.addItem(new SideNavItem("Inicio", UserHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookListView.class)) {
            salesSection.addItem(new SideNavItem("Usuarios", UserListView.class, LineAwesomeIcon.USERS_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookManagementView.class)) {
            salesSection.addItem(new SideNavItem("Book Management", BookManagementView.class,
                    LineAwesomeIcon.BOOK_DEAD_SOLID.create()));
        }

        if (accessChecker.hasAccess(ContractListView.class)) {
            salesSection.addItem(new SideNavItem("Contratos", ContractListView.class,
                    LineAwesomeIcon.PAPERCLIP_SOLID.create()));
        }

        SideNavItem customerServiceSection = new SideNavItem("Portal de atención al cliente");
        customerServiceSection.setPrefixComponent(VaadinIcon.USER.create());
        if (accessChecker.hasAccess(UserHomeView.class)) {
            customerServiceSection
                    .addItem(new SideNavItem("Inicio", UserHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookListView.class)) {
            customerServiceSection
                    .addItem(new SideNavItem("Usuarios", UserListView.class, LineAwesomeIcon.USERS_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookManagementView.class)) {
            customerServiceSection.addItem(new SideNavItem("Book Management", BookManagementView.class,
                    LineAwesomeIcon.BOOK_DEAD_SOLID.create()));
        }

        if (accessChecker.hasAccess(ContractListView.class)) {
            customerServiceSection.addItem(new SideNavItem("Contratos", ContractListView.class,
                    LineAwesomeIcon.PAPERCLIP_SOLID.create()));
        }

        SideNavItem financialSection = new SideNavItem("Portal de finanzas");
        financialSection.setPrefixComponent(VaadinIcon.USER.create());
        if (accessChecker.hasAccess(UserHomeView.class)) {
            financialSection
                    .addItem(new SideNavItem("Inicio", UserHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookListView.class)) {
            financialSection
                    .addItem(new SideNavItem("Usuarios", UserListView.class, LineAwesomeIcon.USERS_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookManagementView.class)) {
            financialSection.addItem(new SideNavItem("Book Management", BookManagementView.class,
                    LineAwesomeIcon.BOOK_DEAD_SOLID.create()));
        }

        if (accessChecker.hasAccess(ContractListView.class)) {
            financialSection.addItem(new SideNavItem("Contratos", ContractListView.class,
                    LineAwesomeIcon.PAPERCLIP_SOLID.create()));
        }

        SideNavItem adminSection = new SideNavItem("Portal de administración");
        adminSection.setPrefixComponent(VaadinIcon.USER.create());
        SideNavItem crudSection = new SideNavItem("CRUD");
        crudSection.setPrefixComponent(VaadinIcon.USER.create());
        adminSection.addItem(crudSection);

        if (accessChecker.hasAccess(NewCallRecordView.class)) {
            crudSection.addItem(new SideNavItem("Nuevo registro de llamada", NewCallRecordView.class,
                    LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(NewDataRecordView.class)) {
            crudSection.addItem(new SideNavItem("Nuevo registro de datos", NewDataRecordView.class,
                    LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(NewLineView.class)) {
            crudSection.addItem(new SideNavItem("Nueva línea a ofertar", NewLineView.class,
                    LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(NewCustomerLineView.class)) {
            crudSection.addItem(new SideNavItem("Nueva línea de cliente", NewCustomerLineView.class,
                    LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(UserHomeView.class)) {
            adminSection.addItem(new SideNavItem("Inicio", UserHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookListView.class)) {
            adminSection.addItem(new SideNavItem("Usuarios", UserListView.class, LineAwesomeIcon.USERS_SOLID.create()));
        }

        if (accessChecker.hasAccess(BookManagementView.class)) {
            adminSection.addItem(new SideNavItem("Book Management", BookManagementView.class,
                    LineAwesomeIcon.BOOK_DEAD_SOLID.create()));
        }

        if (accessChecker.hasAccess(ContractListView.class)) {
            adminSection.addItem(new SideNavItem("Registro de llamadas", CallRecordManagementView.class,
                    LineAwesomeIcon.PAPERCLIP_SOLID.create()));
        }

        nav.addItem(customerSection, salesSection, customerServiceSection, financialSection, adminSection);
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
