package es.uca.iw.telefonuca.ticket.views;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.ticket.domain.Ticket;
import es.uca.iw.telefonuca.ticket.domain.TicketMessage;
import es.uca.iw.telefonuca.ticket.services.TicketManagementService;
import es.uca.iw.telefonuca.ticket.services.TicketMessageManagementService;
import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;

@PermitAll
@PageTitle("Nuevo mensaje de ticket")
@Route(value = "/ticket-messages/new", layout = MainLayout.class)
public class NewTicketMessageView extends Composite<VerticalLayout> {

    private final TicketMessageManagementService ticketMessageManagementService;
    private final TicketManagementService ticketManagementService;
    ComboBox<Ticket> ticketId = new ComboBox<>();
    ComboBox<UUID> parentMessageId = new ComboBox<>();
    TextArea content = new TextArea();
    DateTimePicker dateAndTime = new DateTimePicker();
    Button saveButton = new Button("Guardar", event -> saveTicket());
    Button resetButton = new Button("Limpiar", event -> clearFields());

    public NewTicketMessageView(TicketMessageManagementService ticketMessageManagementService,
            TicketManagementService ticketManagementService) {
        this.ticketMessageManagementService = ticketMessageManagementService;
        this.ticketManagementService = ticketManagementService;

        List<Ticket> tickets = ticketManagementService.loadAll();
        ticketId.setItems(tickets);
        ticketId.setItemLabelGenerator(ticket -> ticket.getId().toString());

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
        h3.setText("Nuevo mensaje de ticket");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        ticketId.setLabel("Ticket");
        parentMessageId.setLabel("Mensaje al que responde");
        content.setLabel("Contenido del mensaje");
        content.setHeight("300px");
        content.setWidth("100%");
        dateAndTime.setLabel("Fecha y hora");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(ticketId);
        formLayout2Col.add(parentMessageId);
        formLayout2Col.add(content);
        formLayout2Col.add(dateAndTime);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(resetButton);

    }

    private void saveTicket() {
        TicketMessage ticketMessage = new TicketMessage();
        ticketMessage.setTicketId((ticketId.getValue().getId()));
        ticketMessage.setParentMessageId(parentMessageId.getValue());
        ticketMessage.setContent(content.getValue());
        ticketMessage.setDate(dateAndTime.getValue());
        ticketMessageManagementService.saveTicketMessage(ticketMessage);
        clearFields();
    }

    private void clearFields() {
        ticketId.clear();
        parentMessageId.clear();
        content.clear();
        dateAndTime.clear();
    }

}