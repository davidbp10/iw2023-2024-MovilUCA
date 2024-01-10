package es.uca.iw.telefonuca.line.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.line.domain.BlockedNumber;
import es.uca.iw.telefonuca.line.services.BlockedNumberManagementService;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.services.CustomerLineManagementService;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Optional;

@RolesAllowed({"CUSTOMER"})
@PageTitle("Nuevo bloqueo de número")
@Route(value = "/blocked-numbers/new", layout = MainLayout.class)
public class NewBlockedNumberUserView extends VerticalLayout {

    private final BlockedNumberManagementService blockedNumberManagementService;
    private final CustomerLineManagementService customerLineManagementService;
    private final AuthenticatedUser authenticatedUser;

    private ComboBox<CustomerLine> customerLineComboBox;
    private IntegerField blockedNumberField;
    private Button saveButton;

    public NewBlockedNumberUserView(BlockedNumberManagementService blockedNumberManagementService,
            CustomerLineManagementService customerLineManagementService, AuthenticatedUser authenticatedUser) {
        this.blockedNumberManagementService = blockedNumberManagementService;
        this.customerLineManagementService = customerLineManagementService;
        this.authenticatedUser = authenticatedUser;

        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        setAlignItems(Alignment.STRETCH);
        setJustifyContentMode(JustifyContentMode.CENTER);

        customerLineComboBox = new ComboBox<>("Número de teléfono");
        blockedNumberField = new IntegerField("Número de teléfono a bloquear");
        saveButton = new Button("Guardar", event -> saveBlockedNumber());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        customerLineComboBox.setItemLabelGenerator(CustomerLine::getPhoneNumberAsString);
        customerLineComboBox.setClearButtonVisible(true);

        blockedNumberField.setRequiredIndicatorVisible(true);

        VerticalLayout layout1 = new VerticalLayout();
        layout1.setSizeFull();
        layout1.setAlignItems(Alignment.CENTER);
        layout1.setJustifyContentMode(JustifyContentMode.START);
        HorizontalLayout layout = new HorizontalLayout(customerLineComboBox, blockedNumberField, saveButton);
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout1.add(layout);
        add(layout1);

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            List<CustomerLine> customerLines = customerLineManagementService.loadCustomerLinesByUserId(user.getId());
            customerLineComboBox.setItems(customerLines);
        }
    }

    private void saveBlockedNumber() {
        CustomerLine customerLine = customerLineComboBox.getValue();
        int phoneNumber = customerLine.getPhoneNumber();

        int blockedPhoneNumber = blockedNumberField.getValue();
        BlockedNumber blockedNumber = new BlockedNumber();
        blockedNumber.setBlocker(phoneNumber);
        blockedNumber.setBlocked(blockedPhoneNumber);

        try {
            blockedNumberManagementService.saveBlockedNumber(blockedNumber);
            Notification.show("Bloqueo guardado con éxito");
        } catch (Exception e) {
            Notification.show("Error al guardar el bloqueo", 3000, Notification.Position.MIDDLE);
        }
    }

}
