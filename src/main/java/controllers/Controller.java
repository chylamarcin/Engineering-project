package controllers;

import daoImpl.DbCompany;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Company;
import model.CompanyExchange;
import model.PredictedExchange;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.joda.time.DateTime;
import others.ExceptionAlert;
import others.Network;
import others.Parser;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private Menu menuAbout;

    @FXML
    private MenuItem menuClose;

    @FXML
    private ComboBox<String> cbFilter;

    @FXML
    public ComboBox<String> cbCompanies;

    @FXML
    public Button btnGenerate;

    @FXML
    private Button btnGetValues;

    @FXML
    private Button buttonLoad;

    @FXML
    private Button btnGetComapnies;

    @FXML
    private Button btnPropagation;

    @FXML
    private TableColumn<CompanyExchange, String> colValue;

    @FXML
    private TableColumn<CompanyExchange, String> colDate;

    @FXML
    private TableView<CompanyExchange> tableView;

    @FXML
    private TableView<PredictedExchange> tableViewPredict;

    @FXML
    private TableColumn<PredictedExchange, String> colPredictValue;

    @FXML
    private TableColumn<PredictedExchange, String> colPredictMean;

    @FXML
    private TableColumn<PredictedExchange, String> colPredictDate;

    private DbCompany dbCompany = new DbCompany();
    private List<Company> listOfCompanies = dbCompany.loadAllCompanies();
    private List<Company> listOfFilteredCompanies;
    public Button getGenerateButton() {
        return btnGenerate;
    }

    public void initialize(URL location, ResourceBundle resources) {

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));

        colPredictDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPredictValue.setCellValueFactory(new PropertyValueFactory<>("predictedValue"));
        colPredictMean.setCellValueFactory(new PropertyValueFactory<>("predictedValueMean"));

        cbCompanies.setDisable(true);
        btnPropagation.setDisable(true);
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

                if (cbCompanies.getItems().isEmpty()) {
                    btnPropagation.setDisable(true);
                } else {
                    if (new File("./" + company.getCompanyName() + " network.eg").exists()) {
                        btnPropagation.setDisable(false);
                    } else {
                        btnPropagation.setDisable(true);
                    }
                }
            }
        }));

        btnGetValues.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                DateTime lastJDate = null;
                DateTime currentJDate = null;

                try {
                    List<Company> companToCheckDate = dbCompany.loadAllCompanies();
                    List<CompanyExchange> exchangeToCheckDate = companToCheckDate.get(
                            companToCheckDate.size() - 1).getListOfExchanges();
                    Date lastDate = exchangeToCheckDate.get(exchangeToCheckDate.size() - 1).getDate();
                    lastJDate = new DateTime(lastDate);
                    currentJDate = new DateTime(new Date());
                    if (currentJDate.toLocalDate().compareTo(lastJDate.toLocalDate()) == 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Checking value");
                        alert.setContentText("Values are up to date!");
                        alert.showAndWait();
                    } else {
                        btnGenerate.setDisable(true);
                        btnPropagation.setDisable(true);
                        btnGetComapnies.setDisable(true);
                        btnGetValues.setDisable(true);
                        Parser.getValues();

                    }
                } catch (Exception ArrayIndexOutOfBoundsException) {
                    ExceptionAlert alert = new ExceptionAlert(ArrayIndexOutOfBoundsException);
                    Parser.getValues();
                }

                btnGenerate.setDisable(false);
                btnGetComapnies.setDisable(false);
                btnGetValues.setDisable(false);
            }
        }));

        buttonLoad.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                if (cbCompanies.getSelectionModel().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error!");
                    alert.setContentText("Please select a company.");
                    alert.showAndWait();
                } else {
                    Company company = dbCompany.findCompany(cbCompanies.getSelectionModel().getSelectedItem());
                    ObservableList<CompanyExchange> c = company.getObsList();
                    tableView.setItems(c);
                    ObservableList<PredictedExchange> pe = FXCollections.observableArrayList(
                            dbCompany.findPredictedExchange(company.getId()));
                    tableViewPredict.setItems(pe);

                }
            }
        }));

        btnGetComapnies.setOnAction((new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                btnGenerate.setDisable(true);
                btnPropagation.setDisable(true);
                btnGetComapnies.setDisable(true);
                btnGetValues.setDisable(true);
                Parser.getCompanies();
                btnGenerate.setDisable(false);
                btnGetComapnies.setDisable(false);
                btnGetValues.setDisable(false);

            }
        }));

        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cbCompanies.getSelectionModel().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error!");
                    alert.setContentText("Please select a company.");
                    alert.showAndWait();
                } else {
                    Company company = dbCompany.findCompany(cbCompanies.getSelectionModel().getSelectedItem());

                    int max = listOfCompanies.size();
                    int[] companiesNumbers = new Random().ints(1, max).distinct().limit(3).toArray();
                    Company company2 = listOfCompanies.get(companiesNumbers[0]);
                    Company company3 = listOfCompanies.get(companiesNumbers[1]);
                    Company company4 = listOfCompanies.get(companiesNumbers[2]);

                    Network network = new Network();
                    network.dividors = new double[4];
                    try {
                        double[][] dataSet = network.companyNormalize(company, company2, company3, company4);
                        double[][] preparedTable = network.prepare2dTable(dataSet);
                        double[][] preparedOutput = network.prepareIdealOutput(dataSet);
                        network.createAndTrainNetwork(preparedTable, preparedOutput, company.getCompanyName());
                    } catch (Exception e) {
                        ExceptionAlert alert = new ExceptionAlert(e);
                    }
                }
            }
        });

        btnPropagation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cbCompanies.getSelectionModel().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error!");
                    alert.setContentText("Please select a company.");
                    alert.showAndWait();
                } else {
                    Company company = dbCompany.findCompany(cbCompanies.getSelectionModel().getSelectedItem());

                    int max = listOfCompanies.size();
                    int[] companiesNumbers = new Random().ints(1, max).distinct().limit(3).toArray();
                    Company company2 = listOfCompanies.get(companiesNumbers[0]);
                    Company company3 = listOfCompanies.get(companiesNumbers[1]);
                    Company company4 = listOfCompanies.get(companiesNumbers[2]);

                    Network network = new Network();
                    network.dividors = new double[4];
                    try {
                        double[][] dataSet = network.companyNormalize(company, company2, company3, company4);
                        double[][] prepared2 = network.prepare2dTbToPrpg(dataSet);
                        MLData prep1dTb = new BasicMLData(network.prepareTables1d(dataSet));
                        network.loadAndCompute(prep1dTb, company);
                    } catch (Exception e) {
                        ExceptionAlert alert = new ExceptionAlert(e);
                    }
                }
            }
        });

        menuClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Information!");
                alert.setContentText("Author - Marcin Chy\u0142a\nUniversity of Rzesz\u00F3w - Information Technology year IV");
                alert.showAndWait();
            }
        });
    }
}