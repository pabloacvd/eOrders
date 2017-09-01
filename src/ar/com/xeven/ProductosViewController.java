/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.xeven;

import ar.com.xeven.domain.Producto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author pacevedo
 */
public class ProductosViewController implements Initializable {

    @FXML
    private MenuItem menuNuevaOrden;
    @FXML
    private MenuItem menuCerrar;
    @FXML
    private MenuItem menuEliminar;
    @FXML
    private MenuItem menuAbout;
    @FXML
    private TreeTableView<Producto> rptProductos;
    @FXML
    private TreeTableColumn<Producto, String> colProducto;
    @FXML
    private TreeTableColumn<Producto, String> colTamanio;
    @FXML
    private TreeTableColumn<Producto, String> colCantidad;
    @FXML
    private TreeTableColumn<Producto, String> colPrecioUnitario;
    @FXML
    private TreeTableColumn<Producto, String> colSubtotal;
    @FXML
    private TreeTableColumn<Producto, String> colTotal;
    @FXML
    private GridPane detallesOrden;
    @FXML
    private Label idOrden;
    @FXML
    private TextField nombreContacto;
    @FXML
    private DatePicker fechaPrecio;
    @FXML
    private ComboBox<?> status;
    @FXML
    private Button btnAgregarProductos;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextArea detallesAdicionales;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    @FXML
    private void nuevaOrden(ActionEvent event) {
    }

    @FXML
    private void cerrar(ActionEvent event) {
    }

    @FXML
    private void eliminarOrden(ActionEvent event) {
    }

    @FXML
    private void acercaDe(ActionEvent event) {
    }


    @FXML
    private void cambioEstado(ActionEvent event) {
    }

    @FXML
    private void cancelarCambios(ActionEvent event) {
    }

    @FXML
    private void guardarOrden(ActionEvent event) {
    }

    @FXML
    private void mostrarDetalleProducto(TreeTableColumn.CellEditEvent<Producto, String> event) {
    }
    
}
