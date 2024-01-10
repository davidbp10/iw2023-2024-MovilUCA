package es.uca.iw.telefonuca.contract.views;

import java.time.LocalDate;
import java.util.Optional;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.services.ContractManagementService;
import es.uca.iw.telefonuca.line.domain.Line;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.services.CustomerLineManagementService;
import es.uca.iw.telefonuca.line.services.LineManagementService;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Nuevo contrato de cliente")
@Route(value = "/my-contracts/new", layout = MainLayout.class)
public class NewContractCustomerView extends Composite<VerticalLayout> {

    private NativeLabel status = new NativeLabel();

    private final ContractManagementService contractManagementService;
    private final CustomerLineManagementService customerLineManagementService;

    ComboBox<Line> line = new ComboBox<>("Seleccione la línea a contratar");
    IntegerField monthsAgreed = new IntegerField();

    public NewContractCustomerView(LineManagementService lineManagementService,
            ContractManagementService contractManagementService,
            CustomerLineManagementService customerLineManagementService, AuthenticatedUser authenticatedUser) {
        this.contractManagementService = contractManagementService;
        this.customerLineManagementService = customerLineManagementService;

        Optional<User> maybeUser = authenticatedUser.get();
        User user = maybeUser.get();
        Button saveButton = new Button("Guardar", event -> saveContract(user));
        Button resetButton = new Button("Limpiar", event -> clearFields());

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Nueva contratación de línea");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        line.setItemLabelGenerator(Line::getName);
        line.setItems(lineManagementService.loadAll());
        line.addValueChangeListener(event -> {
            Line selectedLine = event.getValue();
            if (selectedLine != null) {
                monthsAgreed.setMin(selectedLine.getMinimumMonths());
            }
        });

        monthsAgreed.setLabel("Meses de permanencia");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(line);
        formLayout2Col.add(monthsAgreed);

        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(resetButton);
        status.getElement().getStyle().set("margin-left", "1em");
        layoutRow.add(status);

    }

    private void saveContract(User user) {
        CustomerLine customerLine = new CustomerLine();
        Contract contract = new Contract();
        LocalDate startAt = LocalDate.now();
        int monthsAgreed = this.monthsAgreed.getValue();
        contract.setOwnerId(user.getId());
        contract.setMonthsAgreed(monthsAgreed);
        contract.setCarrier("telefonuca");
        contract.setStartAt(startAt);
        contract.setFinishAt(startAt.plusMonths(monthsAgreed));
        contract.setSharedData(false);

        customerLine.setLineId(line.getValue().getId());
        customerLine.setRoaming(false);
        customerLine.setPricePerMinute(line.getValue().getPricePerMinute());
        customerLine.setPricePerMegabyte(line.getValue().getPricePerMegabyte());
        customerLine.setFreeMinutes(line.getValue().getFreeMinutes());
        customerLine.setFreeMegabytes(line.getValue().getFreeMegabytes());
        customerLine.setPhoneNumber(customerLineManagementService.generatePhoneNumber());

        try {
            contractManagementService.saveContractWithCustomerLine(contract, customerLine);

            // Actualiza el estado con un mensaje de éxito
            status.setText("Enhorabuena, tu contrato y tu línea de cliente se han creado con éxito.");
        } catch (Exception e) {
            // Actualiza el estado con un mensaje de error
            status.setText(
                    "Ha ocurrido un error al crear tu contrato y tu línea de cliente. Por favor, inténtalo de nuevo.");
        }
    }

    private void clearFields() {
        line.clear();
        monthsAgreed.clear();
    }

}