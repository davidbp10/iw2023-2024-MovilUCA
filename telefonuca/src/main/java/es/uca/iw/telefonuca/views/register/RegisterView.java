package es.uca.iw.telefonuca.views.register;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import es.uca.iw.telefonuca.views.login.LoginView;

@Route("register")
@PageTitle("Register | TelefonUCA")
@AnonymousAllowed
public class RegisterView extends Composite<VerticalLayout> {

    public RegisterView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField textField3 = new TextField();
        EmailField emailField = new EmailField();
        TextField textField4 = new TextField();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button registerButton = new Button();
        Button loginButton = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Register");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        textField.setLabel("Name");
        textField2.setLabel("Surname");
        datePicker.setLabel("birthday");
        textField3.setLabel("Telephone");
        emailField.setLabel("Email");
        textField4.setLabel("Username");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        registerButton.setText("Register");
        registerButton.setWidth("min-content");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setText("Go to Login");
        loginButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textField);
        formLayout2Col.add(textField2);
        formLayout2Col.add(textField4);
        formLayout2Col.add(textField3);
        formLayout2Col.add(datePicker);
        formLayout2Col.add(emailField);
        layoutColumn2.add(layoutRow);
        layoutRow.add(registerButton);
        layoutRow.add(loginButton);

        registerButton.addClickListener(event -> {
            // Aquí puedes agregar la lógica para registrar al usuario
            // por ejemplo, puedes usar un servicio para guardar el usuario en la base de datos
        });

        loginButton.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });
    }
}
