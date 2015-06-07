package main;

import daoImpl.DbCompany;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.NeuralNetwork;
import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.neural.error.OutputErrorFunction;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import others.Parser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    /**
     * The input necessary for XOR.
     */
    public static double XOR_INPUT[][] = {{14.25 / 14.50, 14.30 / 14.50, 14.30 / 14.50, 14.50 / 14.50},
            {16.90 / 16.95, 16.95 / 16.95, 16.65 / 16.95, 16.70 / 16.95},
            {20.62 / 20.90, 20.90 / 20.90, 20.88 / 20.90, 20.73 / 20.90},
            {4.95 / 5.10, 5.00 / 5.10, 5.10 / 5.10, 5.00 / 5.10}};

//    public static double XOR_INPUT[][] = { {14.25, 14.30, 14.30, 14.50},
//            {16.90, 16.95, 16.65, 16.70},
//            {20.62, 20.90, 20.88, 20.73},
//            {4.95, 5.00, 5.10, 5.00 } };

    /**
     * The ideal data necessary for XOR.
     */
    public static double XOR_IDEAL[][] = {{13.60 / 14.50}, {16.83 / 16.95}, {20.40 / 20.90}, {5.05 / 5.10}};
//    public static double XOR_IDEAL[][] = { { 13.60 }, { 16.83 }, { 20.40 }, { 5.05 } };

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
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 4));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 7000));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        network.getStructure().finalizeStructure();
        network.reset();

        // create training data
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
        //double[] d = {14.60 / 14.50, 12.83 / 16.95, 20.40 / 20.90, 10.05 / 5.10};
        //BasicMLData mlData = new BasicMLData(d);


        // train the neural network
        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

        int epoch = 1;
        double epochError = 0.00003;
        OutputErrorFunction outputErrorFunction = new OutputErrorFunction();


        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error: " + train.getError());
            epoch++;
            if (epoch > 15000) {
                break;
            }


        } while (train.getError() > epochError);

        // test the neural network
        System.out.println("Neural Network Results:");
        int z = 0;
        double[] helpArray = {14.50, 16.95, 20.90, 5.10};

        double[] error = new double[4];


        for (MLDataPair pair : trainingSet) {
            final MLData output = network.compute(pair.getInput());
            //final MLData output = network.compute(mlData);
            System.out.printf("Yesterday " + String.format(String.format("%.03f", pair.getInput().getData(3) * helpArray[z]) +
                    " predicted = " + String.format("%.03f", output.getData(0) * helpArray[z]) +
                    " correct = " + String.format("%.03f", pair.getIdeal().getData(0) * helpArray[z]) + " difference = " + String.format("%.03f", (output.getData(0) * helpArray[z] - pair.getIdeal().getData(0) * helpArray[z])) + "\n"));
            z++;
        }

        Encog.getInstance().shutdown();
    }
}
