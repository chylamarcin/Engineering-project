package model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by odin on 03.06.15.
 */

@Entity
public class Company {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String companyName;

    @OneToMany(mappedBy = "company")
    private List<CompanyExchange> listOfExchanges;

    public Company() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<CompanyExchange> getListOfExchanges() {
        return listOfExchanges;
    }

    public void setListOfExchanges(List<CompanyExchange> listOfExchanges) {
        this.listOfExchanges = listOfExchanges;
    }

    public void addExchange(CompanyExchange companyExchange){
        listOfExchanges.add(companyExchange);
    }
}
