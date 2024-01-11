package es.uca.iw.telefonuca.ticket.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import es.uca.iw.telefonuca.ticket.repositories.TicketMessageRepository;
import es.uca.iw.telefonuca.ticket.repositories.TicketRepository;
import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.repositories.ContractRepository;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.repositories.CustomerLineRepository;
import es.uca.iw.telefonuca.ticket.domain.Ticket;
import es.uca.iw.telefonuca.ticket.domain.TicketMessage;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class TicketManagementService {
    private final TicketRepository ticketRepository;
    private final TicketMessageRepository ticketMessageRepository;
    private final ContractRepository contractRepository;
    private final CustomerLineRepository customerLineRepository;

    public TicketManagementService(TicketRepository ticketRepository, TicketMessageRepository ticketMessageRepository,
            ContractRepository contractRepository, CustomerLineRepository customerLineRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketMessageRepository = ticketMessageRepository;
        this.contractRepository = contractRepository;
        this.customerLineRepository = customerLineRepository;
    }

    @Transactional
    public List<Ticket> loadAll() {
        return ticketRepository.findAll();
    }

    @Transactional
    public Ticket loadTicketById(UUID id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isEmpty()) {
            throw new RuntimeException("No ticket present with id: " + id);
        } else {
            return ticket.get();
        }
    }

    @Transactional
    public List<Ticket> loadTicketByCustomerLineId(UUID id) {
        List<Ticket> tickets = ticketRepository.findByCustomerLineId(id);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets present with customerLineId: " + id);
        } else {
            return tickets;
        }
    }

    @Transactional
    public List<Ticket> loadTicketBySubject(String subject) {
        List<Ticket> tickets = ticketRepository.findBySubject(subject);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets present with subject: " + subject);
        } else {
            return tickets;
        }
    }

    @Transactional
    public List<Ticket> loadTicketByStatus(String status) {
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets present with status: " + status);
        } else {
            return tickets;
        }
    }

    @Transactional
    public List<Ticket> loadTicketByDate(LocalDate date) {
        List<Ticket> tickets = ticketRepository.findByDate(date);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets present with date: " + date);
        } else {
            return tickets;
        }
    }

    @Transactional
    public List<Ticket> loadTicketByUserId(UUID userId) {
        // First, load all contracts associated with the user
        List<Contract> contracts = contractRepository.findByOwnerId(userId);

        // Get all contract IDs
        List<UUID> contractIds = contracts.stream()
                .map(Contract::getId)
                .collect(Collectors.toList());

        // Load all customer lines associated with these contract IDs
        List<CustomerLine> customerLines = customerLineRepository.findByContractIdIn(contractIds);

        // Finally, load all tickets associated with these customer lines
        List<Ticket> tickets = customerLines.stream()
                .flatMap(customerLine -> ticketRepository.findByCustomerLineId(customerLine.getId()).stream())
                .distinct()
                .collect(Collectors.toList());

        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets present with userId: " + userId);
        } else {
            return tickets;
        }
    }

    @Transactional
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Transactional
    public List<TicketMessage> loadAllTicketMessages() {
        return ticketMessageRepository.findAll();
    }

    @Transactional
    public TicketMessage loadTicketMessageById(UUID id) {
        Optional<TicketMessage> ticketMessage = ticketMessageRepository.findById(id);
        if (ticketMessage.isEmpty()) {
            throw new RuntimeException("No ticketMessage present with id: " + id);
        } else {
            return ticketMessage.get();
        }
    }

    @Transactional
    public List<TicketMessage> loadTicketMessageByTicketId(UUID id) {
        List<TicketMessage> ticketMessages = ticketMessageRepository.findByTicketId(id);
        if (ticketMessages.isEmpty()) {
            throw new RuntimeException("No ticketMessages present with ticketId: " + id);
        } else {
            return ticketMessages;
        }
    }

    @Transactional
    public List<TicketMessage> loadTicketMessageByParentMessageId(UUID id) {
        List<TicketMessage> ticketMessages = ticketMessageRepository.findByParentMessageId(id);
        if (ticketMessages.isEmpty()) {
            throw new RuntimeException("No ticketMessages present with parentMessageId: " + id);
        } else {
            return ticketMessages;
        }
    }

    @Transactional
    public List<TicketMessage> loadTicketMessageByContent(String content) {
        List<TicketMessage> ticketMessages = ticketMessageRepository.findByContent(content);
        if (ticketMessages.isEmpty()) {
            throw new RuntimeException("No ticketMessages present with content: " + content);
        } else {
            return ticketMessages;
        }
    }

    @Transactional
    public List<TicketMessage> loadTicketMessageByDate(LocalDateTime date) {
        List<TicketMessage> ticketMessages = ticketMessageRepository.findByDate(date);
        if (ticketMessages.isEmpty()) {
            throw new RuntimeException("No ticketMessages present with date: " + date);
        } else {
            return ticketMessages;
        }
    }

    @Transactional
    public void saveTicketMessage(TicketMessage ticketMessage) {
        ticketMessageRepository.save(ticketMessage);
    }

    @Transactional
    public Ticket saveTicketWithTicketMessage(Ticket ticket, TicketMessage ticketMessage) {
        // Guardar el contrato sin asignarle un customerLine
        Ticket savedTicket = ticketRepository.save(ticket);

        // Obtener el ID del contrato recién creado
        UUID ticketId = savedTicket.getId();

        // Asignar el ID del contrato al customerLine
        ticketMessage.setTicketId(ticketId);

        // Guardar el customerLine
        ticketMessageRepository.save(ticketMessage);

        return savedTicket;
    }

    public int countTicket() {
        return (int) ticketRepository.count();
    }
    public int countTicketMessage() {
        return (int) ticketMessageRepository.count();
    }
}
