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
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uca.iw.telefonuca.bill.views.ListBillUserView;
import es.uca.iw.telefonuca.bill.views.ListBillView;
import es.uca.iw.telefonuca.contract.views.ListContractView;
import es.uca.iw.telefonuca.contract.views.NewContractCustomerView;
import es.uca.iw.telefonuca.line.views.CallRecordUserView;
import es.uca.iw.telefonuca.line.views.DataRecordUserView;
import es.uca.iw.telefonuca.line.views.ListBlockedNumberUserView;
import es.uca.iw.telefonuca.line.views.NewBlockedNumberUserView;
import es.uca.iw.telefonuca.line.views.NewCallRecordView;
import es.uca.iw.telefonuca.line.views.NewCustomerLineView;
import es.uca.iw.telefonuca.line.views.NewDataRecordView;
import es.uca.iw.telefonuca.line.views.NewLineView;
import es.uca.iw.telefonuca.ticket.views.ListTicketMessageUserView;
import es.uca.iw.telefonuca.ticket.views.NewTicketMessageUserView;
import es.uca.iw.telefonuca.ticket.views.NewTicketMessageView;
import es.uca.iw.telefonuca.ticket.views.NewTicketView;
import es.uca.iw.telefonuca.user.domain.Role;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import es.uca.iw.telefonuca.user.views.UserHomeView;
import es.uca.iw.telefonuca.user.views.UserListView;
import es.uca.iw.telefonuca.user.views.UserProfileView;

import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Locale;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private final AuthenticatedUser authenticatedUser;
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

        StreamResource imageResource = new StreamResource("logo.png",
            () -> getClass().getResourceAsStream("/images/logo.png"));

        Image image = new Image(imageResource, "Logo");

        image.setHeight("250px");

        Header header = new Header(appName, image);

        Scroller scroller = new Scroller(createNavigation(authenticatedUser));

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation(AuthenticatedUser authenticatedUser) {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Inicio", UserHomeView.class, LineAwesomeIcon.HOME_SOLID.create()));

        // Fetch the currently authenticated user
        Optional<User> maybeUser = authenticatedUser.get();
        
        if (maybeUser.isPresent()){
            User user = maybeUser.get();
            
            SideNavItem customerSection = new SideNavItem("Portal de cliente");
            customerSection.setPrefixComponent(VaadinIcon.USER.create());
            if (accessChecker.hasAccess(NewContractCustomerView.class)) {
                customerSection.addItem(new SideNavItem("Adquirir línea", NewContractCustomerView.class,
                        VaadinIcon.PHONE_LANDLINE.create()));
            }

            if (accessChecker.hasAccess(CallRecordUserView.class)) {
                customerSection.addItem(new SideNavItem("Mi registro de llamadas", CallRecordUserView.class,
                        VaadinIcon.PHONE.create()));
            }

            if (accessChecker.hasAccess(DataRecordUserView.class)) {
                customerSection.addItem(new SideNavItem("Mi consumo de datos", DataRecordUserView.class,
                        VaadinIcon.CHART.create()));

            }
            if (accessChecker.hasAccess(ListBillUserView.class)) {
                customerSection
                        .addItem(new SideNavItem("Mis facturas", ListBillUserView.class,
                                VaadinIcon.MONEY.create()));
            }

            if (accessChecker.hasAccess(NewCallRecordView.class)) {
                customerSection.addItem(new SideNavItem("Nuevo registro de llamada", NewCallRecordView.class,
                        VaadinIcon.PHONE.create()));
            }

            if (accessChecker.hasAccess(NewTicketMessageUserView.class)) {
                customerSection.addItem(new SideNavItem("Nuevo ticket", NewTicketMessageUserView.class,
                        VaadinIcon.CLIPBOARD_TEXT.create()));
            }

            if (accessChecker.hasAccess(ListTicketMessageUserView.class)) {
                customerSection.addItem(new SideNavItem("Mis tickets", ListTicketMessageUserView.class,
                        VaadinIcon.CHECK_SQUARE_O.create()));
            }

            if (accessChecker.hasAccess(NewBlockedNumberUserView.class)) {
                customerSection.addItem(new SideNavItem("Nuevo bloqueo de número", NewBlockedNumberUserView.class,
                        VaadinIcon.BAN.create()));
            }

            if (accessChecker.hasAccess(ListBlockedNumberUserView.class)) {
                customerSection.addItem(new SideNavItem("Nuevo bloqueo de número", ListBlockedNumberUserView.class,
                        VaadinIcon.BULLETS.create()));

            }

            nav.addItem(customerSection);
        

            if (user.getRoles().contains(Role.SALES)){
                SideNavItem salesSection = new SideNavItem("Portal de ventas");
                salesSection.setPrefixComponent(VaadinIcon.USER.create());

                if (accessChecker.hasAccess(NewLineView.class)) {
                    salesSection.addItem(
                            new SideNavItem("Nueva Línea a ofertar", NewLineView.class, VaadinIcon.FILE_TEXT.create()));
                }

                nav.addItem(salesSection);
            }

            if (user.getRoles().contains(Role.CUSTOMER_SERVICE)){
                SideNavItem customerServiceSection = new SideNavItem("Portal de atención al cliente");
                customerServiceSection.setPrefixComponent(VaadinIcon.USER.create());
                if (accessChecker.hasAccess(NewCustomerLineView.class)) {
                    customerServiceSection
                            .addItem(new SideNavItem("Nueva línea de cliente", NewCustomerLineView.class,
                                    VaadinIcon.USER.create()));
                }
                if (accessChecker.hasAccess(ListContractView.class)) {
                    customerServiceSection
                            .addItem(new SideNavItem("Contratos", ListContractView.class,
                                    VaadinIcon.CLIPBOARD_TEXT.create()));
                }

                if (accessChecker.hasAccess(UserListView.class)) {
                    customerServiceSection
                            .addItem(new SideNavItem("Usuarios", UserListView.class, LineAwesomeIcon.USERS_SOLID.create()));
                }

                if (accessChecker.hasAccess(NewTicketMessageView.class)) {
                    customerServiceSection
                            .addItem(new SideNavItem("Responder tickets", NewTicketMessageView.class,
                                    VaadinIcon.CLIPBOARD_USER.create()));
                }
                nav.addItem(customerServiceSection);
            }

            if (user.getRoles().contains(Role.FINANCIAL)){
                SideNavItem financialSection = new SideNavItem("Portal de finanzas");
                financialSection.setPrefixComponent(VaadinIcon.USER.create());
                if (accessChecker.hasAccess(ListBillView.class)) {
                    financialSection
                            .addItem(new SideNavItem("Facturas", ListBillView.class, VaadinIcon.FILE_TEXT.create()));
                }
                nav.addItem(financialSection);
            }

            if (user.getRoles().contains(Role.ADMIN)){
                SideNavItem adminSection = new SideNavItem("Portal de administración");
                adminSection.setPrefixComponent(VaadinIcon.TOOLS.create());
                SideNavItem crudSection = new SideNavItem("CRUD");
                crudSection.setPrefixComponent(VaadinIcon.TOOLS.create());
                adminSection.addItem(crudSection);

                if (accessChecker.hasAccess(NewCallRecordView.class)) {
                    crudSection.addItem(new SideNavItem("Nuevo registro de llamada", NewCallRecordView.class,
                            VaadinIcon.PHONE.create()));
                }

                if (accessChecker.hasAccess(NewDataRecordView.class)) {
                    crudSection.addItem(new SideNavItem("Nuevo registro de datos", NewDataRecordView.class,
                            VaadinIcon.DATABASE.create()));
                }

                if (accessChecker.hasAccess(NewLineView.class)) {
                    crudSection.addItem(new SideNavItem("Nueva línea a ofertar", NewLineView.class,
                            VaadinIcon.PHONE.create()));
                }

                if (accessChecker.hasAccess(NewCustomerLineView.class)) {
                    crudSection.addItem(new SideNavItem("Nueva línea de cliente", NewCustomerLineView.class,
                            VaadinIcon.USER.create()));
                }

                if (accessChecker.hasAccess(NewTicketView.class)) {
                    crudSection.addItem(new SideNavItem("Nuevo ticket", NewTicketView.class,
                            VaadinIcon.TICKET.create()));
                }

                if (accessChecker.hasAccess(NewTicketMessageView.class)) {
                    crudSection.addItem(new SideNavItem("Nuevo mensaje de ticket", NewTicketMessageView.class,
                            VaadinIcon.ENVELOPE.create()));
                }                

                nav.addItem(adminSection);
            }
            SideNavItem userProfile = new SideNavItem("Perfil del usuario");
                userProfile.setPrefixComponent(VaadinIcon.USER.create());
                if (accessChecker.hasAccess(UserProfileView.class)) {
                    userProfile.addItem(new SideNavItem("Mis datos", UserProfileView.class,
                            VaadinIcon.USER.create()));
            }
            nav.addItem(userProfile);
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
