package es.uca.iw.telefonuca.line.views;

import java.util.UUID;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.services.CustomerLineManagementService;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Nueva línea de cliente")
@Route(value = "/customer-lines/new", layout = MainLayout.class)
public class NewCustomerLineView extends Composite<VerticalLayout> {

    private final CustomerLineManagementService customerLineManagementService;
    IntegerField phoneNumber = new IntegerField();
    TextField contractId = new TextField();
    TextField lineId = new TextField();
    Checkbox roaming = new Checkbox();
    IntegerField pricePerMinute = new IntegerField();
    IntegerField pricePerMegabyte = new IntegerField();

    Button saveButton = new Button("Guardar", event -> saveCallRecord());
    Button resetButton = new Button("Limpiar", event -> clearFields());

    public NewCustomerLineView(CustomerLineManagementService customerLineManagementService) {
        this.customerLineManagementService = customerLineManagementService;
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
        h3.setText("Nueva línea de cliente");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        phoneNumber.setLabel("Número de teléfono");
        contractId.setLabel("ID de contrato");
        lineId.setLabel("ID de línea ofertada");
        roaming.setLabel("Roaming");
        pricePerMinute.setLabel("Precio por minuto");
        pricePerMegabyte.setLabel("Precio por megabyte");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(phoneNumber);
        formLayout2Col.add(contractId);
        formLayout2Col.add(lineId);
        formLayout2Col.add(roaming);
        formLayout2Col.add(pricePerMinute);
        formLayout2Col.add(pricePerMegabyte);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(resetButton);

    }

    private void saveCallRecord() {
        CustomerLine customerLine = new CustomerLine();
        customerLine.setPhoneNumber(phoneNumber.getValue());
        customerLine.setContractId(UUID.fromString(contractId.getValue()));
        customerLine.setLineId(UUID.fromString(lineId.getValue())); // Convert string to UUID
        customerLine.setRoaming(roaming.getValue());
        customerLine.setPricePerMinute(pricePerMinute.getValue());
        customerLine.setPricePerMegabyte(pricePerMegabyte.getValue());

        customerLineManagementService.saveCustomerLine(customerLine);
    }

    private void clearFields() {
        phoneNumber.clear();
        contractId.clear();
        lineId.clear();
        roaming.clear();
        pricePerMinute.clear();
        pricePerMegabyte.clear();
    }

}