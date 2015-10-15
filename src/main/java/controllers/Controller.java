package controllers;

import daoImpl.DbCompany;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Company;
import model.CompanyExchange;
import org.joda.time.DateTime;
import others.Parser;

import javax.swing.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ComboBox<String> cbFilter;

    @FXML
    private ComboBox<String> cbCompanies;

    @FXML
    private Button btnGetValues;

    @FXML
    private Button buttonLoad;

    @FXML
    private Button btnGetComapnies;

    @FXML
    public static ProgressBar progressBar;

    @FXML
    private TableColumn<CompanyExchange, String> colValue;

    @FXML
    private TableColumn<CompanyExchange, String> colDate;

    @FXML
    private TableView<CompanyExchange> tableView; //to jest ta? nie mam innej :v ok

    private DbCompany dbCompany = new DbCompany();

    private List<Company> listOfCompanies;// = dbCompany.loadAllCompanies();
    private List<Company> listOfFilteredCompanies;

    public void initialize(URL location, ResourceBundle resources) {


        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));

        progressBar = new ProgressBar();

        cbCompanies.setDisable(true);
        btnGetComapnies.setVisible(true);

        for (char i = '0'; i <= '9'; i++) {
            cbFilter.getItems().add(String.valueOf(i));
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            cbFilter.getItems().add(String.valueOf(c));
        }

        cbFilter.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                cbCompanies.getItems().clear();
                listOfFilteredCompanies = dbCompany.loadAllCompaniesOnLetter(cbFilter.getSelectionModel().getSelectedItem());

                for (int i = 0; i < listOfFilteredCompanies.size(); i++) {

                    cbCompanies.getItems().add(listOfFilteredCompanies.get(i).getCompanyName());
                }

                cbCompanies.setDisable(false);
            }
        }));

        cbCompanies.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Company company = dbCompany.findCompany(cbCompanies.getSelectionModel().getSelectedItem());
                //System.out.println(company.getCompanyName());
            }
        }));

        btnGetValues.setOnAction((new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                //progressBar.setProgress(0);
                //progressBar.setProgress(0.50);

                DateTime lastJDate = null;
                DateTime currentJDate = null;

                try {
                    List<Company> companToCheckDate = dbCompany.loadAllCompanies();
                    List<CompanyExchange> exchangeToCheckDate = companToCheckDate.get(companToCheckDate.size() - 1).getListOfExchanges();
                    Date lastDate = exchangeToCheckDate.get(exchangeToCheckDate.size() - 1).getDate();
                    lastJDate = new DateTime(lastDate);
                    currentJDate = new DateTime(new Date());
                    if (currentJDate.toLocalDate().compareTo(lastJDate.toLocalDate()) == 0) {
                        JOptionPane.showMessageDialog(null, "Values are up to date!");
                    } else {
                        Parser.getValues();
                    }
                } catch (Exception ArrayIndexOutOfBoundsException) {
                    System.out.println("date problem");
                    Parser.getValues();
                }



            }
        }));

        btnGetComapnies.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                Parser.getCompanies();
                //btnGetValues.setDisable(false);

            }
        }));

        buttonLoad.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                if (cbCompanies.getSelectionModel().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select a company.");
                } else {
                    Company company = dbCompany.findCompany(cbCompanies.getSelectionModel().getSelectedItem());
                    ObservableList<CompanyExchange> c = company.getObsList();
                    tableView.setItems(c);
                }


            }
        }));

        btnGetComapnies.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                Parser.getCompanies();


            }
        }));


    }

}
