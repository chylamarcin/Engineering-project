package controllers;

import daoImpl.DbCompany;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Company;
import model.CompanyExchange;
import model.Network;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.joda.time.DateTime;
import others.Parser;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ComboBox<String> cbFilter;

    @FXML
    private ComboBox<String> cbCompanies;

    @FXML
    private Button btnGenerate;

    @FXML
    private Button btnGetValues;

    @FXML
    private Button buttonLoad;

    @FXML
    private Button btnGetComapnies;

    @FXML
    private Button btnPropagation;

    @FXML
    private Label testLabel;

    @FXML
    public static ProgressBar progressBar;

    @FXML
    private TableColumn<CompanyExchange, String> colValue;

    @FXML
    private TableColumn<CompanyExchange, String> colDate;

    @FXML
    private TableView<CompanyExchange> tableView; //to jest ta? nie mam innej :v ok

    private DbCompany dbCompany = new DbCompany();

    private List<Company> listOfCompanies = dbCompany.loadAllCompanies();
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

                btnGetComapnies.setDisable(true);
                btnGetValues.setDisable(true);

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

                btnGetComapnies.setDisable(false);
                btnGetValues.setDisable(false);


            }
        }));


        //TODO check if it work in other thread so i can display values in table at the same time when getting new values
        btnGetComapnies.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            Parser.getCompanies();
                            btnGetComapnies.setDisable(true);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                        }
                    }
                });

                btnGetComapnies.setDisable(false);
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

        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cbCompanies.getSelectionModel().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select a company.");
                } else {
                    Company company = dbCompany.findCompany(cbCompanies.getSelectionModel().getSelectedItem());

                    int max = listOfCompanies.size();
                    int[] companiesNumbers = new Random().ints(1, max).distinct().limit(3).toArray();
                    Company company2 = listOfCompanies.get(companiesNumbers[0]);
                    Company company3 = listOfCompanies.get(companiesNumbers[1]);
                    Company company4 = listOfCompanies.get(companiesNumbers[2]);

                    Network network = new Network();
                    network.dividors = new double[4];
                    double[][] dataSet = network.companyNormalize(company, company2, company3, company4);
                    double[][] preparedTable = network.prepare2dTable(dataSet);
                    double[][] preparedOutput = network.prepareIdealOutput(dataSet);


                    network.createAndTrainNetwork(preparedTable, preparedOutput, company.getCompanyName());

//                    for (int i = 0; i < preparedTable.length; i++) {
//                        for (int j = 0; j < preparedTable[i].length; j++) {
//                            System.out.printf("%.04f", preparedTable[i][j]*network.dividors[i]);
//                            System.out.print(" | ");
//                        }
//                        System.out.println();
//                    }
                }

            }
        });

        btnPropagation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cbCompanies.getSelectionModel().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select a company.");
                } else {
                    Company company = dbCompany.findCompany(cbCompanies.getSelectionModel().getSelectedItem());

                    int max = listOfCompanies.size();
                    int[] companiesNumbers = new Random().ints(1, max).distinct().limit(3).toArray();
                    Company company2 = listOfCompanies.get(companiesNumbers[0]);
                    Company company3 = listOfCompanies.get(companiesNumbers[1]);
                    Company company4 = listOfCompanies.get(companiesNumbers[2]);

                    Network network = new Network();
                    network.dividors = new double[4];
                    double[][] dataSet = network.companyNormalize(company, company2, company3, company4);
                    double[][] prepared2 = network.prepare2dTbToPrpg(dataSet);
                    MLData prep1dTb = new BasicMLData(network.prepareTables1d(dataSet));
                    network.loadAndEvaluate(prep1dTb, company.getCompanyName());


                }
            }
        });
    }
}