package main;

import daoImpl.DbCompany;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import others.Parser;

public class Main extends Application {


    /**
     * Main method
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Backward Propagation");
        primaryStage.setScene(new Scene(root));
        DbCompany dbCompany = new DbCompany();

        if (Parser.getCountOfSiteCompany() == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Check your internet connection!");
            alert.showAndWait();
            System.exit(0);
        } else if (dbCompany.checkCompaniesCount()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Checking companies");
            alert.setContentText("Companies are up to date!");
            alert.showAndWait();
            primaryStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Checking companies");
            alert.setContentText("Wait, program need to update companies database.");
            primaryStage.showAndWait();
            Parser.getCompanies();
            Parser.getValues();
            primaryStage.show();
        }


        //close operation
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                //Platform.exit();
                System.exit(0);
            }
        });


    }

    public static void main(String[] args) {
        launch(args);


    }

    //Parser.getTitle();


}


