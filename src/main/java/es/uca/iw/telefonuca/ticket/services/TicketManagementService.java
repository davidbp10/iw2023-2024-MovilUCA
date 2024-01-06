package es.uca.iw.telefonuca.ticket.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import es.uca.iw.telefonuca.ticket.repositories.TicketRepository;
import es.uca.iw.telefonuca.ticket.domain.Ticket;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class TicketManagementService {
    private final TicketRepository ticketRepository;

    public TicketManagementService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

}
