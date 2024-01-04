package es.uca.iw.telefonuca.line.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "CustomerLines")
public class CustomerLine {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotEmpty
    @Column(unique = true)
    private int phoneNumber;

    @NotEmpty
    @Column
    private UUID customerId;

    @NotEmpty
    @Column
    private String lineTypeName;

    @NotEmpty
    @Column
    private boolean roaming;

    @NotEmpty
    @Column
    private Set<Integer> blockedNumbers = new HashSet<>();

    @NotEmpty
    @Column
    private float pricePerMinute;

    @NotEmpty
    @Column
    private float pricePerMegabyte;

    @NotEmpty
    @Column
    private int freeMinutes;

    @NotEmpty
    @Column
    private int freeMegabytes;

    @ElementCollection(fetch = FetchType.LAZY) // Conjunto de CallRecord con CallRecord.lineId = this.id
    private Set<CallRecord> callRecords = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY) // Conjunto de DataRecord con DataRecord.lineId = this.id
    private Set<DataRecord> dataRecords = new HashSet<>();

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

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getLineTypeName() {
        return lineTypeName;
    }

    public void setLineTypeName(String lineTypeName) {
        this.lineTypeName = lineTypeName;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public Set<Integer> getBlockedNumbers() {
        return blockedNumbers;
    }

    public void setBlockedNumbers(Set<Integer> blockedNumbers) {
        this.blockedNumbers = blockedNumbers;
    }

    public float getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(float pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    public float getPricePerMegabyte() {
        return pricePerMegabyte;
    }

    public void setPricePerMegabyte(float pricePerMegabyte) {
        this.pricePerMegabyte = pricePerMegabyte;
    }

    public Set<CallRecord> getCallRecords() {
        return callRecords;
    }

    public void setCallRecords(Set<CallRecord> callRecords) {
        this.callRecords = callRecords;
    }

    public Set<DataRecord> getDataRecords() {
        return dataRecords;
    }

    public void setDataRecords(Set<DataRecord> dataRecords) {
        this.dataRecords = dataRecords;
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
