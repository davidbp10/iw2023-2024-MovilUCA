package es.uca.iw.telefonuca.ticket.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.ticket.domain.Ticket;
import es.uca.iw.telefonuca.ticket.domain.TicketMessage;

public interface TicketMessageRepository extends JpaRepository<TicketMessage, UUID> {
    Optional<TicketMessage> findById(UUID id);

    List<TicketMessage> findByTicketId(UUID id);

    List<TicketMessage> findByParentMessageId(UUID id);

    List<Ticket> findByContent(String content);

    List<TicketMessage> findByDate(LocalDateTime date);

}
