/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.xeven;

import ar.com.xeven.domain.LineaDetalle;
import ar.com.xeven.domain.Orden;
import ar.com.xeven.domain.Producto;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author educacionit
 */
public class EOrders extends Application {

    private static ObservableList<Orden> lstOrdenes;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("OrdenesView.fxml"));
        Parent root = loader.load(); 
                
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("eOrders - XEVEN");
        stage.show();
        
        OrdenesViewController controller = loader.getController();
        controller.setMain(this);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        EOrders.lstOrdenes = FXCollections.observableArrayList();
        ObservableList<LineaDetalle> lstLineasDetalle = FXCollections.observableArrayList();
        ObservableList<LineaDetalle> lstLineasDetalle2 = FXCollections.observableArrayList();
        
        Map<String, Double> precioPorTamanio = new HashMap<String, Double>(){{
            put("Chico", 10.0);
            put("Medio", 22.20);
            put("Grande", 35.50);
            put("Promo", 0.0);
        }};
        Map<String, Double> precioPorTamanio2 = new HashMap<String, Double>(){{
            put("30 personas", 100.0);
            put("50 personas", 200.50);
            put("Promo", 0.0);
        }};
        
        
        // no accessories for this test
        ObservableList<Producto> accesorios = FXCollections.observableArrayList();
        
        Producto accesorio1 = new Producto("Bandeja","Bandeja artesanal",
                    precioPorTamanio, LocalDate.now(),accesorios);
        
        accesorios.add(accesorio1);
        
        Producto producto1 = new Producto("Picada XEVEN","Con jamon y otros fiambres.",
                    precioPorTamanio2, LocalDate.now(),accesorios);
        

        ObservableList<LineaDetalle> accesoriosSeleccionados = FXCollections.observableArrayList();
        LineaDetalle lineaAccesorio = new LineaDetalle(accesorio1, "Grande", precioPorTamanio.get("Grande"), new ArrayList<>(), 2);
        accesoriosSeleccionados.add(lineaAccesorio);
        
        LineaDetalle linea1 = new LineaDetalle(producto1, "50 personas",
                precioPorTamanio2.get("50 personas"), accesoriosSeleccionados, 3);

        lstLineasDetalle.add(linea1);
        
        LineaDetalle linea2 = new LineaDetalle(producto1, "30 personas", precioPorTamanio2.get("30 personas"), new ArrayList<>(), 10);
        ObservableList<LineaDetalle> accesoriosSeleccionados2 = FXCollections.observableArrayList();
        LineaDetalle lineaAccesorio2 = new LineaDetalle(accesorio1, "Chico", precioPorTamanio.get("Chico"), new ArrayList<>(), 2);
        accesoriosSeleccionados2.add(lineaAccesorio);
        accesoriosSeleccionados2.add(lineaAccesorio2);
        lstLineasDetalle2.add(linea1);
        lstLineasDetalle2.add(linea2);

      //   String detallesEntrega, ObservableList<LineaDetalle> lineasDetalle, Double montoAbonado, Double descuento, String status) {

        Orden orden1 = new Orden("Pablo Acevedo Areco","+5491123456789",
                "Profe JavaSE8", LocalDate.now(),"Retira por local 13hs",
                lstLineasDetalle,10.0, 5.0,"Preparado");

        lstOrdenes.add(orden1);
        
        Orden orden2 = new Orden("Linus Torvalds","+358 91911",
                "GIT and Linux dad", LocalDate.of(2017, Month.JULY, 30),"Entregar por DHL a Finlandia",
                lstLineasDetalle2,1.0, 0.0,"Confirmado");        
        lstOrdenes.add(orden2);
        
        launch(args);
    }

    public static ObservableList<Orden> getLstOrdenes() {
        return lstOrdenes;
    }

    public static void setLstOrdenes(ObservableList<Orden> lstOrdenes) {
        EOrders.lstOrdenes = lstOrdenes;
    }
}
