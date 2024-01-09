package es.uca.iw.telefonuca.line.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Lines")
public class Line {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotEmpty
    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column
    private int pricePerMinute; // Céntimos

    @NotNull
    @Column
    private int pricePerMegabyte; // Céntimos

    @NotNull
    @Column
    private int minimumMonths;

    @NotNull
    @Column
    private int freeMinutes;

    @NotNull
    @Column
    private int freeMegabytes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPricePerMinute() {
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

    public int getMinimumMonths() {
        return minimumMonths;
    }

    public void setMinimumMonths(int minimumMonths) {
        this.minimumMonths = minimumMonths;
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
        Line other = (Line) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
