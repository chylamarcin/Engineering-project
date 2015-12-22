package model;

import org.encog.Encog;
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

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Marcin on 22/12/2015.
 */
public class Network {

    private Company selectedCompany;
    private Company randomCompany1;
    private Company randomCompany2;
    private Company randomCompany3;
    public double[] dividors;

    public Network(Company selectedCompany, Company randomCompany1, Company randomCompany2, Company randomCompany3) {
        this.selectedCompany = selectedCompany;
        this.randomCompany1 = randomCompany1;
        this.randomCompany2 = randomCompany2;
        this.randomCompany3 = randomCompany3;
    }

    public Network() {
    }

    private double[][] companyNormalize(Company company, Company rndCompany, Company rnd2Company, Company rnd3Company) {

        List<Double> listOfValues = new ArrayList<>();
        List<Double> listOfValues2 = new ArrayList<>();
        List<Double> listOfValues3 = new ArrayList<>();
        List<Double> listOfValues4 = new ArrayList<>();
        List<List<Double>> listOfListOfValues = new ArrayList<List<Double>>(4);
        List<CompanyExchange> listOfExchange = company.getListOfExchanges();
        List<CompanyExchange> listOfExchange2 = rndCompany.getListOfExchanges();
        List<CompanyExchange> listOfExchange3 = rnd2Company.getListOfExchanges();
        List<CompanyExchange> listOfExchange4 = rnd3Company.getListOfExchanges();

        for (int i = 0; i < listOfExchange.size(); i++) {
            listOfValues.add(Double.valueOf(listOfExchange.get(i).getValue()));
            listOfValues2.add(Double.valueOf(listOfExchange2.get(i).getValue()));
            listOfValues3.add(Double.valueOf(listOfExchange3.get(i).getValue()));
            listOfValues4.add(Double.valueOf(listOfExchange4.get(i).getValue()));
        }

        listOfListOfValues.add(listOfValues);
        listOfListOfValues.add(listOfValues2);
        listOfListOfValues.add(listOfValues3);
        listOfListOfValues.add(listOfValues4);
        double dividor;

        int k = 0;
        double[][] tableOfValues = new double[4][listOfValues.size()];
        for (int i = 0; i < listOfValues.size(); i++) {
            dividor = Collections.max(listOfListOfValues.get(i));
            for (int j = 0; j < listOfListOfValues.size(); j++) {
                tableOfValues[j][i] = listOfListOfValues.get(j).get(i) / dividor;
            }
            dividors[k] = dividor;
            k++;
        }

        return tableOfValues;

    }

    public double[] denormalize(double[] outputTable) {
        double[] output = new double[4];
        for (int i = 0; i < outputTable.length; i++) {
            output[i] = outputTable[i] * dividors[i];
        }
        return output;
    }

    private double[] prepareTables(double[] selectedCompany, double[] randomCompany1, double[] randomCompany2,
                                   double[] randomCompany3, double[] randomCompany4, int day) {
        double[] preparedTable = {selectedCompany[selectedCompany.length - day], randomCompany1[randomCompany1.length - day],
                randomCompany3[randomCompany3.length - day], randomCompany4[randomCompany4.length - day]};

        return preparedTable;
    }

    private double prepareIdealOutput(double[] selectedCompany, int day) {
        if (selectedCompany.length - day + 1 > selectedCompany.length) {
            return 0; // NARAZIE ZERO POTEM SIE POMYSLI
        } else {
            return selectedCompany[selectedCompany.length - day + 1];
        }
    }

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

        for (MLDataPair pair : trainingSet) {
            final MLData output = network2.compute(pair.getInput());
            //final MLData output = network.compute(mlData);
            System.out.printf("Yesterday " + String.format(String.format("%.04f", pair.getInput().getData(3) * helpArray[z]) +
                    " predicted = " + String.format("%.04f", output.getData(0) * helpArray[z]) +
                    " correct = " + String.format("%.04f", pair.getIdeal().getData(0) * helpArray[z]) + " difference = " + String.format("%.04f", (output.getData(0) * helpArray[z] - pair.getIdeal().getData(0) * helpArray[z])) + "\n"));
            z++;
        }

        int response = JOptionPane.showConfirmDialog(null, "Do you want to save network?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            System.out.println("No button clicked");
        } else if (response == JOptionPane.YES_OPTION) {
            EncogDirectoryPersistence.saveObject(new File("network.eg"), network2);
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.out.println("JOptionPane closed");
        }
        Encog.getInstance().shutdown();

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
