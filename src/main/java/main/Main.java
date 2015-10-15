package main;

import daoImpl.DbCompany;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.error.OutputErrorFunction;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
import others.Parser;

import javax.swing.*;
import java.io.File;

public class Main extends Application {

    /**
     * The input necessary for XOR.
     */
    public static double input[][] = {{14.25 / 14.50, 14.30 / 14.50, 14.30 / 14.50, 14.50 / 14.50},
            {16.90 / 16.95, 16.95 / 16.95, 16.65 / 16.95, 16.70 / 16.95},
            {20.62 / 20.90, 20.90 / 20.90, 20.88 / 20.90, 20.73 / 20.90},
            {4.95 / 5.10, 5.00 / 5.10, 5.10 / 5.10, 5.00 / 5.10}};

    /**
     * The ideal data necessary for XOR.
     */
    public static double ideal[][] = {{13.60 / 14.50}, {16.83 / 16.95}, {20.40 / 20.90}, {5.05 / 5.10}};

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
        } else {
            JOptionPane.showMessageDialog(null, "Wait, program need to update companies database.");
            Parser.getCompanies();
            Parser.getValues();
        }

        primaryStage.show();

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


    public void createAndTrainNetwork(double[][] input, double[][] ideal) {
        BasicNetwork network2 = new BasicNetwork();
        network2.addLayer(new BasicLayer(null, true, 4));
        network2.addLayer(new BasicLayer(new ActivationSigmoid(), true, 8));
        network2.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        network2.getStructure().finalizeStructure();
        network2.reset();

        MLDataSet trainingSet = new BasicMLDataSet(input, ideal);

        // test the neural network
        System.out.println("Neural Network Results:");

        double[] helpArray = {14.50, 16.95, 20.90, 5.10};
        double[] error = new double[4];
        int z = 0;

//        for (MLDataPair pair : trainingSet) {
//            final MLData output = network2.compute(pair.getInput());
//            //final MLData output = network.compute(mlData);
//            System.out.printf("Yesterday " + String.format(String.format("%.04f", pair.getInput().getData(3) * helpArray[z]) +
//                    " predicted = " + String.format("%.04f", output.getData(0) * helpArray[z]) +
//                    " correct = " + String.format("%.04f", pair.getIdeal().getData(0) * helpArray[z]) + " difference = " + String.format("%.04f", (output.getData(0) * helpArray[z] - pair.getIdeal().getData(0) * helpArray[z])) + "\n"));
//            z++;
//        }

//        int response = JOptionPane.showConfirmDialog(null, "Do you want to save network?", "Confirm",
//                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//        if (response == JOptionPane.NO_OPTION) {
//            System.out.println("No button clicked");
//        } else if (response == JOptionPane.YES_OPTION) {
//            EncogDirectoryPersistence.saveObject(new File("network.eg"), network);
//        } else if (response == JOptionPane.CLOSED_OPTION) {
//            System.out.println("JOptionPane closed");
//        }
//        Encog.getInstance().shutdown();

        for (int i = 0; i < 10; i++) {

            network2 = new BasicNetwork();
            network2.addLayer(new BasicLayer(null, true, 4));
            network2.addLayer(new BasicLayer(new ActivationSigmoid(), true, 8));
            network2.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

            network2.getStructure().finalizeStructure();
            network2.reset();

            double[] newNetworkError = trainNewNetwork(network2, trainingSet);
            loadAndEvaluate(trainingSet, helpArray, newNetworkError, network2);
            System.out.println("Jeszcze tylko " + (10000000 - i + 1));
        }
    }


    public static double[] trainNewNetwork(BasicNetwork newNetwork, MLDataSet trainingSet) {

        final ResilientPropagation train = new ResilientPropagation(newNetwork, trainingSet);

        int epoch = 1;
        double epochError = 0.0000009;
        OutputErrorFunction outputErrorFunction = new OutputErrorFunction();

        do {
            train.iteration();
            //System.out.println("Epoch #" + epoch + " Error: " + train.getError());
            epoch++;
            if (epoch > 200000) {
                break;
            }
        } while (train.getError() > epochError);

        double[] helpArray = {14.50, 16.95, 20.90, 5.10};
        double[] error = new double[4];
        int z = 0;
        double[] newNetworkError = new double[4];
        for (MLDataPair pair : trainingSet) {
            final MLData output = newNetwork.compute(pair.getInput());
            //final MLData output = network.compute(mlData);
            System.out.printf("Yesterday " + String.format(String.format("%.04f", pair.getInput().getData(3) * helpArray[z]) +
                    " predicted = " + String.format("%.04f", output.getData(0) * helpArray[z]) +
                    " correct = " + String.format("%.04f", pair.getIdeal().getData(0) * helpArray[z]) + " difference = " + String.format("%.04f", (output.getData(0) * helpArray[z] - pair.getIdeal().getData(0) * helpArray[z])) + "\n"));
            newNetworkError[z] = (output.getData(0) * helpArray[z] - pair.getIdeal().getData(0) * helpArray[z]);
            z++;
        }

        return newNetworkError;
    }

    public static void loadAndEvaluate(MLDataSet trainingSet, double[] helpArray, double[] newNetworkError, BasicNetwork newNetwork) {
        int z = 0;
        int j = 0;
        System.out.println("Loading network");
        BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File("network.eg"));

        double e = network.calculateError(trainingSet);
        System.out.println();
        double[] oldNetworkError = new double[4];
        System.out.println("----------------old one--------------------");
        for (MLDataPair pair : trainingSet) {
            final MLData output = network.compute(pair.getInput());
            //final MLData output = network.compute(mlData);
            System.out.printf("Yesterday " + String.format(String.format("%.04f", pair.getInput().getData(3) * helpArray[z]) +
                    " predicted = " + String.format("%.04f", output.getData(0) * helpArray[z]) +
                    " correct = " + String.format("%.04f", pair.getIdeal().getData(0) * helpArray[z]) + " difference = " + String.format("%.04f", (output.getData(0) * helpArray[z] - pair.getIdeal().getData(0) * helpArray[z])) + "\n"));
            oldNetworkError[z] = (output.getData(0) * helpArray[z] - pair.getIdeal().getData(0) * helpArray[z]);
            z++;

        }


        for (int i = 0; i < newNetworkError.length; i++) {
            if (Math.abs(roundOff(oldNetworkError[i], 3)) >= Math.abs(roundOff(newNetworkError[i], 3))) {
                j++;
            }
        }

        System.err.println("Lepsze wyniki " + j);

        if (j >= 4) {
            EncogDirectoryPersistence.saveObject(new File("network.eg"), newNetwork);
            JOptionPane.showMessageDialog(null, "New network saved");
        } else {
            System.out.println("No better network here!");
        }
    }

    static double roundOff(double x, int position) {
        double a = x;
        double temp = Math.pow(10.0, position);
        a *= temp;
        a = Math.round(a);
        return (a / (temp));
    }
}


