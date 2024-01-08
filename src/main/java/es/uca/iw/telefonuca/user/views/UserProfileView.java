package es.uca.iw.telefonuca.user.views;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;

import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import jakarta.annotation.security.PermitAll;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Perfil del Usuario")
@PermitAll
public class UserProfileView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;
    private final UserManagementService userManagementService;
    private TextField emailField;
    private PasswordField passwordField;
    private Button saveEmailButton;
    private Button savePasswordButton;
    private ConfirmDialog emailConfirmDialog;
    private ConfirmDialog passwordConfirmDialog;

    private Div datosUsuarioContent;
    private Div cambiarEmailContent;
    private Div cambiarContrasenaContent;
    private Map<Tab, Component> tabsToPages;


    public UserProfileView(AuthenticatedUser authenticatedUser, UserManagementService userManagementService) {
        this.authenticatedUser = authenticatedUser;
        this.userManagementService = userManagementService;
        buildForm();
        buildTabs();
        loadUserProfile();
        buildConfirmDialogs();
    }

    private void buildTabs() {
        // Crear las pestañas
        Tab tabDatosUsuario = new Tab("Datos del Usuario");
        Tab tabCambiarEmail = new Tab("Cambiar Email");
        Tab tabCambiarContrasena = new Tab("Cambiar Contraseña");

        // Crear los contenidos para cada pestaña
        datosUsuarioContent = new Div();
        cambiarEmailContent = new Div();
        cambiarContrasenaContent = new Div();

        // Mapeo de pestañas con sus contenidos
        tabsToPages = new HashMap<>();
        tabsToPages.put(tabDatosUsuario, datosUsuarioContent);
        tabsToPages.put(tabCambiarEmail, cambiarEmailContent);
        tabsToPages.put(tabCambiarContrasena, cambiarContrasenaContent);

        // Configurar las pestañas
        Tabs tabs = new Tabs(tabDatosUsuario, tabCambiarEmail, tabCambiarContrasena);
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.setSelectedTab(tabDatosUsuario);

        // Contenedor para los contenidos de las pestañas
        Div pages = new Div(datosUsuarioContent);
        
        // Añadir contenido inicial
        datosUsuarioContent.add();
        cambiarEmailContent.add(emailField, saveEmailButton);
        cambiarContrasenaContent.add(passwordField, savePasswordButton);

        // Cambio de contenido al seleccionar una pestaña
        tabs.addSelectedChangeListener(event -> {
            pages.removeAll();
            pages.add(tabsToPages.get(event.getSelectedTab()));
        });

        // Añadir las pestañas y el contenido al layout principal
        getContent().add(tabs, pages);
    }

    private void buildForm() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3("Perfil del Usuario");
        FormLayout formLayout2Col = new FormLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");

        emailField = new TextField("Correo Electrónico");
        saveEmailButton = new Button("Guardar Correo Electrónico", event -> {
            emailConfirmDialog.open();
        });
        VerticalLayout emailLayout = new VerticalLayout(emailField, saveEmailButton);
        emailLayout.setSpacing(false);

        passwordField = new PasswordField("Contraseña");
        savePasswordButton = new Button("Guardar Contraseña", event -> {
            passwordConfirmDialog.open();
        });
        VerticalLayout passwordLayout = new VerticalLayout(passwordField, savePasswordButton);
        passwordLayout.setSpacing(false);

        saveEmailButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        savePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(emailLayout);
        formLayout2Col.add(passwordLayout);
    }

    private void loadUserProfile() {
        User currentUser = authenticatedUser.get().orElseThrow();
        
        // Crear los campos para mostrar la información del usuario
        TextField idField = new TextField("ID");
        idField.setValue(currentUser.getId().toString());
        idField.setReadOnly(true);

        TextField usernameField = new TextField("Nombre de usuario");
        usernameField.setValue(currentUser.getUsername());
        usernameField.setReadOnly(true);

        TextField nameField = new TextField("Nombre");
        nameField.setValue(currentUser.getName());
        nameField.setReadOnly(true);

        TextField surnameField = new TextField("Apellidos");
        surnameField.setValue(currentUser.getSurname());
        surnameField.setReadOnly(true);

        TextField emailField = new TextField("Correo Electrónico");
        emailField.setValue(currentUser.getEmail());
        emailField.setReadOnly(true);

        // Convertir los roles a una cadena de texto separada por comas
        String rolesStr = currentUser.getRoles().stream()
            .map(Enum::name)
            .collect(Collectors.joining(", "));
        TextField rolesField = new TextField("Rol(es)");
        rolesField.setValue(rolesStr);
        rolesField.setReadOnly(true);

        // Añadir los campos al layout de la pestaña de datos del usuario
        datosUsuarioContent.add(idField, usernameField, nameField, surnameField, emailField, rolesField);
}

    private void updateEmail() {
        User currentUser = authenticatedUser.get().orElseThrow();
        currentUser.setEmail(emailField.getValue());
        userManagementService.updateUser(currentUser);
        Notification.show("Correo electrónico actualizado exitosamente!");
    }

    private void updatePassword() {
        User currentUser = authenticatedUser.get().orElseThrow();
        currentUser.setPassword(passwordField.getValue()); // Deberías codificarla antes de guardarla
        userManagementService.updateUser(currentUser);
        Notification.show("Contraseña actualizada exitosamente!");
    }

    private void buildConfirmDialogs() {
        emailConfirmDialog = new ConfirmDialog();
        emailConfirmDialog.setHeader("Confirmar cambio de correo electrónico");
        emailConfirmDialog.setConfirmText("Cambiar");
        emailConfirmDialog.addConfirmListener(event -> updateEmail());
        emailConfirmDialog.setCancelable(true);
        emailConfirmDialog.addCancelListener(event -> emailConfirmDialog.close());
   
        passwordConfirmDialog = new ConfirmDialog();
        passwordConfirmDialog.setHeader("Confirmar cambio de contraseña");
        passwordConfirmDialog.setConfirmText("Cambiar");
        passwordConfirmDialog.setCancelText("Cancelar");
        passwordConfirmDialog.addConfirmListener(event -> updatePassword());
        passwordConfirmDialog.setCancelable(true);
        passwordConfirmDialog.addCancelListener(event -> passwordConfirmDialog.close());
    }
}
