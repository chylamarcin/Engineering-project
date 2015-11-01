package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "company")
    private List<CompanyExchange> listOfExchanges;

    public Company() {
        listOfExchanges = new ArrayList<CompanyExchange>();
    }

    public Company(String companyName, CompanyExchange companyExchange){
        this.companyName = companyName;
        this.listOfExchanges.add(companyExchange);
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

    public ObservableList<CompanyExchange> getObsList() {
        ObservableList<CompanyExchange> obsList = FXCollections.observableArrayList(listOfExchanges);
        return obsList;
    }

    public void setListOfExchanges(List<CompanyExchange> listOfExchanges) {
        this.listOfExchanges = listOfExchanges;
    }

    public void addExchange(CompanyExchange companyExchange){
        listOfExchanges.add(companyExchange);
    }
}
