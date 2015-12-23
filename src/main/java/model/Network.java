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
    public double[] dividors = new double[4];


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

    public Network(Company selectedCompany, Company randomCompany1, Company randomCompany2, Company randomCompany3) {
        this.selectedCompany = selectedCompany;
        this.randomCompany1 = randomCompany1;
        this.randomCompany2 = randomCompany2;
        this.randomCompany3 = randomCompany3;
    }

    public Network() {
    }

    public double[][] companyNormalize(Company company, Company rndCompany, Company rnd2Company, Company rnd3Company) {

        List<Double> listOfValues = new ArrayList<>();
        List<Double> listOfValues2 = new ArrayList<>();
        List<Double> listOfValues3 = new ArrayList<>();
        List<Double> listOfValues4 = new ArrayList<>();
        List<List<Double>> listOfListOfValues = new ArrayList<List<Double>>(4);

        List<CompanyExchange> listOfExchange = company.getListOfExchanges();
        List<CompanyExchange> listOfExchange2 = rndCompany.getListOfExchanges();
        List<CompanyExchange> listOfExchange3 = rnd2Company.getListOfExchanges();
        List<CompanyExchange> listOfExchange4 = rnd3Company.getListOfExchanges();

        double[][] tableOfValues = new double[4][];

        String helpV2 = "";
        try {
            for (int i = 0; i < listOfExchange.size(); i++) {
                helpV2 = listOfExchange.get(i).getValue().replace(",", ".");
                if (helpV2.contains(" ")) {
                    helpV2 = helpV2.replace(" ", "");
                }
                listOfValues.add(Double.parseDouble(helpV2));
            }

            for (int i = 0; i < listOfExchange2.size(); i++) {
                helpV2 = listOfExchange2.get(i).getValue().replace(",", ".");
                if (helpV2.contains(" ")) {
                    helpV2 = helpV2.replace(" ", "");
                }
                listOfValues2.add(Double.parseDouble(helpV2));
            }

            for (int i = 0; i < listOfExchange3.size(); i++) {
                helpV2 = listOfExchange3.get(i).getValue().replace(",", ".");
                if (helpV2.contains(" ")) {
                    helpV2 = helpV2.replace(" ", "");
                }
                listOfValues3.add(Double.parseDouble(helpV2));
            }

            for (int i = 0; i < listOfExchange4.size(); i++) {
                helpV2 = listOfExchange4.get(i).getValue().replace(",", ".");
                if (helpV2.contains(" ")) {
                    helpV2 = helpV2.replace(" ", "");
                }
                listOfValues4.add(Double.parseDouble(helpV2));
            }

            listOfListOfValues.add(listOfValues);
            listOfListOfValues.add(listOfValues2);
            listOfListOfValues.add(listOfValues3);
            listOfListOfValues.add(listOfValues4);

            dividors[0] = Collections.max(listOfValues);
            dividors[1] = Collections.max(listOfValues2);
            dividors[2] = Collections.max(listOfValues3);
            dividors[3] = Collections.max(listOfValues4);

            tableOfValues = new double[4][listOfValues.size()];
            for (int i = 0; i < listOfValues.size(); i++) {
                for (int j = 0; j < listOfListOfValues.size(); j++) {
                    tableOfValues[j][i] = listOfListOfValues.get(j).get(i) / dividors[j];
                    //System.out.println(i);
                }
            }

        } catch (Exception e) {
            //System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.print("ERROR: <-----------<<" + e.getMessage());
            System.out.println();
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

    //TODO set it up to change result table[4][X] from companyNormalize to [4][5]
    public double[] prepareTables1d(double[][] tbToPrp1d) {
        double[] preparedTable = {tbToPrp1d[0][tbToPrp1d[0].length - 1], tbToPrp1d[1][tbToPrp1d[1].length - 1],
                tbToPrp1d[2][tbToPrp1d[2].length - 1], tbToPrp1d[3][tbToPrp1d[3].length - 1]};

        return preparedTable;
    }

    public double[][] prepare2dTable(double[][] tbToPrp) {
        double preparedTable[][] = {{tbToPrp[0][tbToPrp[0].length - 5], tbToPrp[0][tbToPrp[0].length - 4], tbToPrp[0][tbToPrp[0].length - 3], tbToPrp[0][tbToPrp[0].length - 2]},
                {tbToPrp[1][tbToPrp[1].length - 5], tbToPrp[1][tbToPrp[1].length - 4], tbToPrp[1][tbToPrp[1].length - 3], tbToPrp[1][tbToPrp[1].length - 2]},
                {tbToPrp[2][tbToPrp[2].length - 5], tbToPrp[2][tbToPrp[2].length - 4], tbToPrp[2][tbToPrp[2].length - 3], tbToPrp[2][tbToPrp[2].length - 2]},
                {tbToPrp[3][tbToPrp[3].length - 5], tbToPrp[3][tbToPrp[3].length - 4], tbToPrp[3][tbToPrp[3].length - 3], tbToPrp[3][tbToPrp[3].length - 2]}};

        return preparedTable;
    }

    public double[][] prepare2dTbToPrpg(double[][] tbToPrp) {
        double preparedTable[][] = {{tbToPrp[0][tbToPrp[0].length - 4], tbToPrp[0][tbToPrp[0].length - 3], tbToPrp[0][tbToPrp[0].length - 2], tbToPrp[0][tbToPrp[0].length - 1]},
                {tbToPrp[1][tbToPrp[1].length - 4], tbToPrp[1][tbToPrp[1].length - 3], tbToPrp[1][tbToPrp[1].length - 2], tbToPrp[1][tbToPrp[1].length - 1]},
                {tbToPrp[2][tbToPrp[2].length - 4], tbToPrp[2][tbToPrp[2].length - 3], tbToPrp[2][tbToPrp[2].length - 2], tbToPrp[2][tbToPrp[2].length - 1]},
                {tbToPrp[3][tbToPrp[3].length - 4], tbToPrp[3][tbToPrp[3].length - 3], tbToPrp[3][tbToPrp[3].length - 2], tbToPrp[3][tbToPrp[3].length - 1]}};

        return preparedTable;
    }

//    public double prepareIdealOutput(double[] selectedCompany, int day) {
//        if (selectedCompany.length - day + 1 > selectedCompany.length) {
//            return 0; // NARAZIE ZERO POTEM SIE POMYSLI
//        } else {
//            return selectedCompany[selectedCompany.length - day + 1];
//        }
//    }

    public double[][] prepareIdealOutput(double[][] tbToPrp) {
        System.out.println(tbToPrp[0][tbToPrp.length]);
        double preparedTable[][] = {{tbToPrp[0][tbToPrp[0].length - 1]},
                {tbToPrp[0][tbToPrp[0].length - 1]},
                {tbToPrp[0][tbToPrp[0].length - 1]},
                {tbToPrp[0][tbToPrp[0].length - 1]}};
        return preparedTable;
    }

    public void createAndTrainNetwork(double[][] input, double[][] ideal, String companyName) {
        BasicNetwork network2 = new BasicNetwork();
        network2.addLayer(new BasicLayer(null, true, 4));
        network2.addLayer(new BasicLayer(new ActivationSigmoid(), true, 8));
        network2.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        network2.getStructure().finalizeStructure();
        network2.reset();


        MLDataSet trainingSet = new BasicMLDataSet(input, ideal);

        // test the neural network
        System.out.println("Neural Network Results:");

        double[] helpArray = dividors;
        double[] error = new double[4];
        int z = 0;

        double[] newNetworkError = trainNewNetwork(network2, trainingSet);

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
            EncogDirectoryPersistence.saveObject(new File(companyName + " network.eg"), network2);
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.out.println("JOptionPane closed");
        }
        Encog.getInstance().shutdown();


        //loadAndEvaluate(trainingSet, helpArray, newNetworkError, network2);

    }

    public static double[] trainNewNetwork(BasicNetwork newNetwork, MLDataSet trainingSet) {

        final ResilientPropagation train = new ResilientPropagation(newNetwork, trainingSet);

        int epoch = 1;
        OutputErrorFunction outputErrorFunction = new OutputErrorFunction();

        do {
            train.iteration();
            System.out.println("Epoch: " + epoch + " Error: "
                    + train.getError());
            epoch++;
        } while (train.getError() > 0.0001);

        double[] error = new double[4];
        int z = 0;
        double[] newNetworkError = new double[4];
        return newNetworkError;
    }

    public BasicNetwork loadEgsistingNetwork(String companyName) {
        BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(companyName + " network.eg"));
        return network;
    }

    public void loadAndEvaluate(MLData trainingSet, String companyName) {
        int z = 0;
        int j = 0;

        BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(companyName + " network.eg"));
        // double e = network.calculateError(trainingSet);
//        double[] oldNetworkError = new double[4];
//
//        for (int i = 0; i < newNetworkError.length; i++) {
//            if (Math.abs(roundOff(oldNetworkError[i], 3)) >= Math.abs(roundOff(newNetworkError[i], 3))) {
//                j++;
//            }
//        }

        MLData output = network.compute(trainingSet);
        double value = output.getData(0);
        System.out.println(value * dividors[0] + " to ta wartosc test test test");
//        double[] helpArray = dividors;
//        for (MLDataPair pair : trainingSet) {
//            final MLData output = network.compute(pair.getInput());
//            //final MLData output = network.compute(mlData);
//            System.out.printf("Yesterday " + String.format(String.format("%.04f", pair.getInput().getData(3) * helpArray[z]) +
//                    " predicted = " + String.format("%.04f", output.getData(0) * helpArray[z]) +
//                    " correct = " + String.format("%.04f", pair.getIdeal().getData(0) * helpArray[z]) + " difference = " + String.format("%.04f", (output.getData(0) * helpArray[z] - pair.getIdeal().getData(0) * helpArray[z])) + "\n"));
//            z++;
//        }

    }

    static double roundOff(double x, int position) {
        double a = x;
        double temp = Math.pow(10.0, position);
        a *= temp;
        a = Math.round(a);
        return (a / (temp));
    }
}