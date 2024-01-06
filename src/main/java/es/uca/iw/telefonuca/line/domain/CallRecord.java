package es.uca.iw.telefonuca.line.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "callRecords")
public class CallRecord {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotNull
    @Column
    @Min(value = 100000000, message = "Phone number must have at least 9 digits")
    @Max(value = 999999999, message = "Phone number must have at most 9 digits")
    private int sender;

    @NotNull
    @Column
    @Min(value = 100000000, message = "Phone number must have at least 9 digits")
    @Max(value = 999999999, message = "Phone number must have at most 9 digits")
    private int receiver;

    @NotNull
    @Column
    private long duration; // duración en segundos

    @NotNull
    @Column
    private LocalDate date;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CallRecord other = (CallRecord) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
