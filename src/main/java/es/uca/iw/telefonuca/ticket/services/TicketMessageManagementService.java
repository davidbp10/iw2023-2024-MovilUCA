package es.uca.iw.telefonuca.ticket.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import es.uca.iw.telefonuca.ticket.domain.TicketMessage;
import es.uca.iw.telefonuca.ticket.repositories.TicketMessageRepository;
import jakarta.transaction.Transactional;

@Service
public class TicketMessageManagementService {
    TicketMessageRepository ticketMessageRepository;

    public TicketMessageManagementService(TicketMessageRepository ticketMessageRepository) {
        this.ticketMessageRepository = ticketMessageRepository;
    }

    @Transactional
    public List<TicketMessage> loadAll() {
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

}
