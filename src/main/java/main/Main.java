package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
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
    public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
            { 0.0, 1.0 }, { 1.0, 1.0 } };

    /**
     * The ideal data necessary for XOR.
     */
    public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

    /**
     * Main method
     * @param primaryStage
     * @throws Exception
     */




    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Backward Propagation");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //launch(args);
        String test = "";
        //bitbuckettest
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null,true,2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),true,3));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));
        network.getStructure().finalizeStructure();
        network.reset();

        // create training data
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

        // train the neural network
        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

        int epoch = 1;

        do {
            train.iteration();
            //System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while(train.getError() > 0.01);
        train.finishTraining();

        // test the neural network
        //System.out.println("Neural Network Results:");
        for(MLDataPair pair: trainingSet ) {
            final MLData output = network.compute(pair.getInput());
            //System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
            //        + ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
        }


        ///////////////////////////
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL("http://www.bankier.pl/gielda/notowania/akcje");
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder buf = new StringBuilder();

            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                buf.append(line+"\n");
            }

            test = buf.toString();
            //System.out.println(test);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }

        Parser.getTitle(test);
        //////////////////////////
        //System.out.println("test 2");

//        try {
//            URL url = new URL("http://stackoverflow.com/questions/1381617");
//            URLConnection con = url.openConnection();
//            Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
//            Matcher m = p.matcher(con.getContentType());
///* If Content-Type doesn't match this pre-conception, choose default and
// * hope for the best. */
//            String charset = m.matches() ? m.group(1) : "ISO-8859-1";
//            Reader r = new InputStreamReader(con.getInputStream(), charset);
//            StringBuilder buf = new StringBuilder();
//            while (true) {
//                int ch = r.read();
//                if (ch < 0)
//                    break;
//                buf.append((char) ch);
//            }
//            String str = buf.toString();
//            System.out.println(str);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }

        Encog.getInstance().shutdown();
    }
}
