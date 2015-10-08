package others;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ViewChanger {

    public static Object object;

    public ViewChanger() {
    }

    public Stage changeView(String pathToView, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    pathToView));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle(title);
            stage.show();

            return stage;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Stage noResiseChangeView(String pathToView, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    pathToView));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle(title);
            stage.show();
            stage.setResizable(false);
            return stage;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Stage changeMaximalizedView(String pathToView, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    pathToView));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle(title);
            stage.show();
            stage.setFullScreen(true);
            return stage;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Stage mainView(String pathToView, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    pathToView));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle(title);
            stage.show();
            return stage;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Stage changeViewModalityWindow(String pathToView, String title,
                                          ActionEvent event, boolean menuBar, Stage... args) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    pathToView));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle(title);
            stage.initModality(Modality.WINDOW_MODAL);
            if (!menuBar) {
                stage.initOwner(((Node) event.getSource()).getScene()
                        .getWindow());
            } else {
                stage.initOwner(args[0].getScene().getWindow());
            }
            stage.showAndWait();
            return stage;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
