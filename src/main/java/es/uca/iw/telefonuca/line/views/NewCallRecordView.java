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
import es.uca.iw.telefonuca.line.domain.CallRecord;
import es.uca.iw.telefonuca.line.services.CallRecordManagementService;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"CUSTOMER", "ADMIN"})
@PageTitle("Nuevo registro de llamada")
@Route(value = "/call-records/new", layout = MainLayout.class)
public class NewCallRecordView extends Composite<VerticalLayout> {

    private final CallRecordManagementService callRecordManagementService;
    IntegerField sender = new IntegerField();
    IntegerField receiver = new IntegerField();
    DatePicker date = new DatePicker();
    IntegerField duration = new IntegerField();
    Button saveButton = new Button("Guardar", event -> saveCallRecord());
    Button resetButton = new Button("Limpiar", event -> clearFields());

    public NewCallRecordView(CallRecordManagementService callRecordManagementService) {
        this.callRecordManagementService = callRecordManagementService;
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
        h3.setText("Nuevo registro de llamada");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        sender.setLabel("Emisor");
        receiver.setLabel("Receptor");
        date.setLabel("Fecha");
        duration.setLabel("Duración");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(sender);
        formLayout2Col.add(receiver);
        formLayout2Col.add(date);
        formLayout2Col.add(duration);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(resetButton);

    }

    private void saveCallRecord() {
        CallRecord callRecord = new CallRecord();
        callRecord.setSender(sender.getValue());
        callRecord.setReceiver(receiver.getValue());
        callRecord.setDuration(duration.getValue());
        callRecord.setDate(date.getValue());

        callRecordManagementService.saveCallRecord(callRecord);
    }

    private void clearFields() {
        sender.clear();
        receiver.clear();
        date.clear();
        duration.clear();
    }

}