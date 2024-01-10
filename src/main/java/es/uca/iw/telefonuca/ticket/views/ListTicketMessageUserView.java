package es.uca.iw.telefonuca.ticket.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.ticket.domain.Ticket;
import es.uca.iw.telefonuca.ticket.domain.TicketMessage;
import es.uca.iw.telefonuca.ticket.services.TicketManagementService;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Optional;

@PermitAll
@PageTitle("Mis tickets")
@Route(value = "my-tickets", layout = MainLayout.class)
public class ListTicketMessageUserView extends VerticalLayout {

    private final TicketManagementService ticketManagementService;
    private final AuthenticatedUser authenticatedUser;

    private ComboBox<Ticket> ticketComboBox;
    private Grid<TicketMessage> ticketMessageGrid;

    public ListTicketMessageUserView(TicketManagementService ticketManagementService,
            AuthenticatedUser authenticatedUser) {
        this.ticketManagementService = ticketManagementService;
        this.authenticatedUser = authenticatedUser;

        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        ticketComboBox = new ComboBox<>("Tickets");
        ticketMessageGrid = new Grid<>(TicketMessage.class);

        ticketComboBox.setItemLabelGenerator(Ticket::getIdAsString);
        ticketComboBox.setClearButtonVisible(true);

        ticketMessageGrid.setSizeFull();
        ticketMessageGrid.setColumns("id", "content", "date");

        add(ticketComboBox, ticketMessageGrid);

        ticketComboBox.addValueChangeListener(event -> {
            Ticket maybeTicket = event.getValue();
            if (maybeTicket != null) {
                Ticket ticket = event.getValue();
                List<TicketMessage> ticketMessages = ticketManagementService
                        .loadTicketMessageByTicketId(ticket.getId());
                ticketMessageGrid.setItems(ticketMessages);
            }
        });

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            List<Ticket> tickets = ticketManagementService.loadTicketByUserId(user.getId());
            ticketComboBox.setItems(tickets);
        }
    }
}
