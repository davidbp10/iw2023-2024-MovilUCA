package es.uca.iw.telefonuca.ticket.domain;

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

    @Column
    private UUID parentMessageId;

    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String content;

}
