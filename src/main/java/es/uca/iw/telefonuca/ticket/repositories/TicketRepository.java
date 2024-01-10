package es.uca.iw.telefonuca.ticket.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.ticket.domain.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findById(UUID id);

    List<Ticket> findByCustomerLineId(UUID id);

    List<Ticket> findBySubject(String subject);

    List<Ticket> findByStatus(String status);

    List<Ticket> findByDate(LocalDate date);
}
