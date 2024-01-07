package es.uca.iw.telefonuca.line.domain;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "blockedNumbers", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "blocker", "blocked" })
})
public class BlockedNumber {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotNull
    @Column
    @Min(value = 100000000, message = "Phone number must have at least 9 digits")
    @Max(value = 999999999, message = "Phone number must have at most 9 digits")
    private int blocker;

    @NotNull
    @Column
    @Min(value = 100000000, message = "Phone number must have at least 9 digits")
    @Max(value = 999999999, message = "Phone number must have at most 9 digits")
    private int blocked;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getBlocker() {
        return blocker;
    }

    public void setBlocker(int blocker) {
        this.blocker = blocker;
    }
}
