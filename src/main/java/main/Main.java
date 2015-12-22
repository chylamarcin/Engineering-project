package main;

import daoImpl.DbCompany;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import others.Parser;

import javax.swing.*;

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
            JOptionPane.showMessageDialog(null, "Check your internet connection!");
        } else if (dbCompany.checkCompaniesCount()) {
            JOptionPane.showMessageDialog(null, "Companies are up to date.");
            primaryStage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Wait, program need to update companies database.");
            Parser.getCompanies();
            Parser.getValues();
            primaryStage.show();
        }


        //close operation
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });


    }

    public static void main(String[] args) {
        launch(args);


    }

    //Parser.getTitle();


}


