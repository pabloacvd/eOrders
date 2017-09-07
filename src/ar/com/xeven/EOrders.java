package ar.com.xeven;

import ar.com.xeven.domain.LineaDetalle;
import ar.com.xeven.domain.Orden;
import ar.com.xeven.domain.Producto;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Clase principal para eOrders
 * @author Pablo Acevedo
 */
public class EOrders extends Application {    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("OrdenesView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        /*
        loader.setLocation(getClass().getResource("ProductosView.fxml"));
        Parent productos = loader.load();
        Scene productosScene = new Scene(productos); 
        stage.setScene(productosScene);//para ver los productos
        */
        stage.setMaximized(true);
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
    /*
    // en este metodo uso menu con css
//@Override
public void start3(Stage primaryStage) {
    Pane root = new Pane();
    root.setPrefSize(400, 300);
    Text text = new Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            + " Nullam vehicula justo et sem venenatis mattis non ut quam. "
            + "Aliquam erat volutpat. Etiam maximus magna quis tortor "
            + "pellentesque, in sollicitudin odio ullamcorper. Phasellus "
            + "a quam nisl. Fusce at urna dapibus, elementum quam "
            + "ultricies, posuere ipsum. Etiam varius orci a tortor "
            + "vestibulum fringilla. Sed consectetur nunc rhoncus diam "
            + "volutpat, vitae finibus eros cursus. Praesent quam mauris, "
            + "lacinia nec metus vitae, blandit faucibus tortor.");

    text.setWrappingWidth(385);
    text.setLayoutX(15);
    text.setLayoutY(20);

    VBox menu = new VBox();
    menu.setId("menu");
    menu.prefHeightProperty().bind(root.heightProperty());
    menu.setPrefWidth(200);

    menu.getChildren().addAll(new Button("Something"), new Button("Something else"), new Button("Something different"));

    menu.getStylesheets().add(getClass().getResource("menu.css").toExternalForm());
    menu.setTranslateX(-190);
    TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), menu);

    menuTranslation.setFromX(-190);
    menuTranslation.setToX(0);

    menu.setOnMouseEntered(evt -> {
        menuTranslation.setRate(1);
        menuTranslation.play();
    });
    menu.setOnMouseExited(evt -> {
        menuTranslation.setRate(-1);
        menuTranslation.play();
    });

    root.getChildren().addAll(text, menu);

    Scene scene = new Scene(root);

    primaryStage.setScene(scene);
    primaryStage.show();
}
    */
    