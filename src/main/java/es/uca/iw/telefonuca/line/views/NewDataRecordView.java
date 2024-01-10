package es.uca.iw.telefonuca.line.views;

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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.line.domain.DataRecord;
import es.uca.iw.telefonuca.line.services.DataRecordManagementService;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@PageTitle("Nuevo registro de datos")
@Route(value = "/data-records/new", layout = MainLayout.class)
public class NewDataRecordView extends Composite<VerticalLayout> {

    private final DataRecordManagementService dataRecordManagementService;
    IntegerField phoneNumber = new IntegerField();
    DatePicker date = new DatePicker();
    IntegerField megabytes = new IntegerField();

    Button saveButton = new Button("Guardar", event -> saveDataRecord());
    Button resetButton = new Button("Limpiar", event -> clearFields());

    public NewDataRecordView(DataRecordManagementService dataRecordManagementService) {
        this.dataRecordManagementService = dataRecordManagementService;
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
        h3.setText("Nuevo registro de datos");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        phoneNumber.setLabel("Número de teléfono");
        date.setLabel("Fecha");
        megabytes.setLabel("Megabytes consumidos");
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
        formLayout2Col.add(megabytes);
        formLayout2Col.add(date);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(resetButton);

    }

    private void saveDataRecord() {
        DataRecord dataRecord = new DataRecord();
        dataRecord.setPhoneNumber(phoneNumber.getValue());
        dataRecord.setDate(date.getValue());
        dataRecord.setMegabytes(megabytes.getValue());

        dataRecordManagementService.saveDataRecord(dataRecord);
    }

    private void clearFields() {
        phoneNumber.clear();
        date.clear();
        megabytes.clear();
    }

}