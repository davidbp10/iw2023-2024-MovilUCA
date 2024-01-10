package es.uca.iw.telefonuca.bill.domain;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "bills", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "contractId", "year", "month" }) // Por cada contrato, año y mes solo puede
                                                                           // haber una factura
})
public class Bill {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotNull
    @Column
    private UUID contractId;

    @NotNull
    @Column
    private int year;

    @NotNull
    @Column
    private int month;

    @NotNull
    @Min(0)
    @Column
    private long totalMinutes;

    @NotNull
    @Min(0)
    @Column
    private long totalMegabytes;

    @NotNull
    @Min(0)
    @Column
    private double total; // En céntimos

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public long getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(long totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public long getTotalMegabytes() {
        return totalMegabytes;
    }

    public void setTotalMegabytes(long totalMegabytes) {
        this.totalMegabytes = totalMegabytes;
    }

}
