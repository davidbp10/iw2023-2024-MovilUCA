package es.uca.iw.telefonuca.line.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 100000000, message = "Phone number must have at least 9 digits")
    @Max(value = 999999999, message = "Phone number must have at most 9 digits")
    @Column(unique = true)
    private int phoneNumber;

    @NotNull
    @Column
    private UUID contractId;

    @NotNull
    @Column
    private UUID lineId;

    @NotNull
    @Column
    private boolean roaming;

    @NotNull
    @Column
    private int pricePerMinute;

    @NotNull
    @Column
    private int pricePerMegabyte;

    @NotNull
    @Column
    private int freeMinutes;

    @NotNull
    @Column
    private int freeMegabytes;

    public UUID getId() {
        return id;
    }

    public String getIdAsString() {
        return id.toString();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumberAsString() {
        return String.valueOf(phoneNumber);
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

    public int getFreeMinutes() {
        return freeMinutes;
    }

    public void setFreeMinutes(int freeMinutes) {
        this.freeMinutes = freeMinutes;
    }

    public int getFreeMegabytes() {
        return freeMegabytes;
    }

    public void setFreeMegabytes(int freeMegabytes) {
        this.freeMegabytes = freeMegabytes;
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
