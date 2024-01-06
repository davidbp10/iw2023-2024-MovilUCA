package es.uca.iw.telefonuca.line.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "CustomerLines")
public class CustomerLine {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(value = 100000000, message = "Phone number must have at least 9 digits")
    @Max(value = 999999999, message = "Phone number must have at most 9 digits")
    @Column(unique = true)
    private int phoneNumber;

    @NotEmpty
    @Column
    private UUID contractId;

    @NotEmpty
    @Column
    private UUID lineId;

    @NotEmpty
    @Column
    private boolean roaming;

    @NotEmpty
    @Column
    private int pricePerMinute;

    @NotEmpty
    @Column
    private int pricePerMegabyte;

    @NotEmpty
    @Column
    private int freeMinutes;

    @NotEmpty
    @Column
    private int freeMegabytes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public UUID getLineId() {
        return lineId;
    }

    public String getLineIdAsString() {
        return lineId.toString();
    }

    public void setLineId(UUID lineId) {
        this.lineId = lineId;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public float getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(int pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    public int getPricePerMegabyte() {
        return pricePerMegabyte;
    }

    public void setPricePerMegabyte(int pricePerMegabyte) {
        this.pricePerMegabyte = pricePerMegabyte;
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
        CustomerLine other = (CustomerLine) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
