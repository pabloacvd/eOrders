package ar.com.xeven;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main para eOrders
 * @author Pablo Acevedo
 */
public class EOrders extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        /**/
        loader.setLocation(getClass().getResource("OrdenesView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        /**/
        /*
        loader.setLocation(getClass().getResource("ProductosView.fxml"));
        Parent productos = loader.load();
        Scene productosScene = new Scene(productos); 
        stage.setScene(productosScene);
        /**/
        stage.setMaximized(true);
        stage.getIcons().add(new Image("/resources/color/001_56.png"));
        stage.setTitle("eOrders - XEVEN");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}