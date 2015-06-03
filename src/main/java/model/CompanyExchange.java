package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by odin on 03.06.15.
 */
@Entity
public class CompanyExchange {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private Date date;
    @Column
    private double value;

    @ManyToOne
    @JoinColumn(name = "companyId")
    private Company company;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
