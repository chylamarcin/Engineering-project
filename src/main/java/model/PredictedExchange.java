package model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Marcin on 12/10/2015.
 */
@Entity
public class PredictedExchange {

    @Id
    @GeneratedValue
    private long id;
    @Column
    @Type(type = "date")
    private Date date;

    @Column
    private String predictedValue;

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

    public String getPredictedValue() {
        return predictedValue;
    }

    public void setPredictedValue(String value) {
        this.predictedValue = value;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
