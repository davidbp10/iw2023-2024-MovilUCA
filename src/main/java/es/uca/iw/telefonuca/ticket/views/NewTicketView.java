package es.uca.iw.telefonuca.ticket.views;

import java.util.Arrays;
import java.util.UUID;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.ticket.domain.Ticket;
import es.uca.iw.telefonuca.ticket.domain.TicketStatus;
import es.uca.iw.telefonuca.ticket.services.TicketManagementService;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@PageTitle("Nuevo ticket")
@Route(value = "/tickets/new", layout = MainLayout.class)
public class NewTicketView extends Composite<VerticalLayout> {

    private final TicketManagementService ticketManagementService;
    TextField customerLineId = new TextField();
    TextField subject = new TextField();
    ComboBox<TicketStatus> status = new ComboBox<>("Estado", Arrays.asList(TicketStatus.values()));
    DatePicker date = new DatePicker();

    Button saveButton = new Button("Guardar", event -> saveTicket());
    Button resetButton = new Button("Limpiar", event -> clearFields());

    public NewTicketView(TicketManagementService ticketManagementService) {
        this.ticketManagementService = ticketManagementService;
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
        h3.setText("Nuevo ticket");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        customerLineId.setLabel("Línea de cliente");
        subject.setLabel("Asunto");
        date.setLabel("Fecha");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(customerLineId);
        formLayout2Col.add(subject);
        formLayout2Col.add(status);
        formLayout2Col.add(date);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(resetButton);

    }

    private void saveTicket() {
        Ticket ticket = new Ticket();
        ticket.setCustomerLineId(UUID.fromString(customerLineId.getValue()));
        ticket.setSubject(subject.getValue());
        ticket.setStatus(status.getValue());
        ticket.setDate(date.getValue());
        ticketManagementService.saveTicket(ticket);
        clearFields();
    }

    private void clearFields() {
        customerLineId.clear();
        subject.clear();
        status.clear();
        date.clear();
    }

}