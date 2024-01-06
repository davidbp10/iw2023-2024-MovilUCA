package es.uca.iw.telefonuca.ticket.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "ticketMessages")
public class TicketMessage {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotEmpty
    @Column
    private UUID ticketId;

    @Column
    private UUID parentMessageId;

    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotEmpty
    @Column
    private LocalDateTime date;

}
