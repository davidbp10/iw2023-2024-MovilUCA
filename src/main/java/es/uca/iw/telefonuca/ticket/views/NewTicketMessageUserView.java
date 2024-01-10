package es.uca.iw.telefonuca.ticket.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.services.CustomerLineManagementService;
import es.uca.iw.telefonuca.ticket.domain.Ticket;
import es.uca.iw.telefonuca.ticket.domain.TicketMessage;
import es.uca.iw.telefonuca.ticket.domain.TicketStatus;
import es.uca.iw.telefonuca.ticket.services.TicketManagementService;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@PermitAll
@PageTitle("Nuevo ticket")
@Route(value = "/my-tickets/new", layout = MainLayout.class)
public class NewTicketMessageUserView extends Composite<VerticalLayout> {

    private final TicketManagementService ticketManagementService;

    ComboBox<CustomerLine> customerLine = new ComboBox<>("Customer Line");
    TextField subject = new TextField("Subject");
    TextArea content = new TextArea("Contenido del mensaje");
    Button saveButton = new Button("Guardar", event -> saveTicket());
    Button resetButton = new Button("Limpiar", event -> clearFields());

    public NewTicketMessageUserView(TicketManagementService ticketManagementService,
            CustomerLineManagementService customerLineManagementService, AuthenticatedUser authenticatedUser) {
        this.ticketManagementService = ticketManagementService;

        Optional<User> maybeUser = authenticatedUser.get();
        User user = maybeUser.get();

        List<CustomerLine> customerLines = customerLineManagementService
                .loadCustomerLinesByUserId(user.getId());
        customerLine.setItems(customerLines);
        customerLine.setItemLabelGenerator(CustomerLine::getPhoneNumberAsString);

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
        customerLine.setLabel("Número de teléfono");
        subject.setLabel("Asunto");
        content.setLabel("Contenido del mensaje");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(customerLine);
        formLayout2Col.add(subject);
        formLayout2Col.add(content);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(resetButton);

    }

    private void saveTicket() {
        Ticket ticket = new Ticket();
        TicketStatus status = TicketStatus.PENDING_ANSWER_BY_STAFF;
        ticket.setCustomerLineId(customerLine.getValue().getId());
        ticket.setSubject(subject.getValue());
        ticket.setStatus(status);
        ticket.setDate(LocalDate.now());

        TicketMessage ticketMessage = new TicketMessage();
        ticketMessage.setContent(content.getValue());
        ticketMessage.setDate(LocalDateTime.now());

        try {
            ticketManagementService.saveTicketWithTicketMessage(ticket, ticketMessage);
            Notification.show("Ticket saved successfully!");
        } catch (Exception e) {
            Notification.show("Failed to save ticket: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void clearFields() {
        customerLine.clear();
        subject.clear();
        content.clear();
    }

}
