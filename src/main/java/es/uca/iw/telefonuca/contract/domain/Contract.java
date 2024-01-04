package es.uca.iw.telefonuca.contract.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotEmpty
    @Column
    private UUID ownerId;

    @NotEmpty
    @Column
    private int monthsAgreed;

    @NotEmpty
    @Column
    private String carrier;

    @Column
    private float bill;

    @NotEmpty
    @Column
    private LocalDate startAt;

    @NotEmpty
    @Column
    private LocalDate finishAt;

    @NotEmpty
    @Column
    private boolean sharedData;

    @NotEmpty
    @Column
    private Set<UUID> contractLines = new HashSet<>();

    public Set<UUID> getContractLines() {
        return contractLines;
    }

    public void setContractLines(Set<UUID> contractLines) {
        this.contractLines = contractLines;
    }

    public boolean isSharedData() {
        return sharedData;
    }

    public void setSharedData(boolean sharedData) {
        this.sharedData = sharedData;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public int getMonthsAgreed() {
        return monthsAgreed;
    }

    public void setMonthsAgreed(int monthsAgreed) {
        this.monthsAgreed = monthsAgreed;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public float getBill() {
        return bill;
    }

    public void setBill(float bill) {
        this.bill = bill;
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDate startAt) {
        this.startAt = startAt;
    }

    public LocalDate getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(LocalDate finishAt) {
        this.finishAt = finishAt;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Contract other)) {
            return false; // null or other class
        }

        if (id != null) {
            return id.equals(other.id);
        }
        return super.equals(other);
    }
}
