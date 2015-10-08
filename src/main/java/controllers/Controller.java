package controllers;

import daoImpl.DbCompany;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import model.Company;
import others.Parser;

import java.net.URL;
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
    private Button btnGetComapnies;

    @FXML
    public static ProgressBar progressBar;

    private DbCompany dbCompany = new DbCompany();

    private List<Company> listOfCompanies = dbCompany.loadAllCompanies();
    private List<Company> listOfFilteredCompanies;

    public void initialize(URL location, ResourceBundle resources) {
        progressBar = new ProgressBar();

        cbCompanies.setDisable(true);
        //btnGetValues.setDisable(true);
        btnGetComapnies.setVisible(false);

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

                progressBar.setProgress(0);
                progressBar.setProgress(0.50);

                Parser.getValues();


            }
        }));

        btnGetComapnies.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                Parser.getCompanies();
                //btnGetValues.setDisable(false);

            }
        }));

    }

}
