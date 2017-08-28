/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.xeven;

import ar.com.xeven.domain.LineaDetalle;
import ar.com.xeven.domain.Orden;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author pacevedo
 */
public class OrdenesViewController implements Initializable {

    @FXML
    private TableView<Orden> orderTable;
    @FXML
    private TableColumn<Orden, Integer> orderIDCol;
    @FXML
    private TableColumn<?, ?> contactNameCol;
    @FXML
    private TableColumn<?, ?> contactPhoneCol;
    @FXML
    private TableColumn<?, ?> deliveryDateCol;
    @FXML
    private TableColumn<?, ?> statusCol;
    @FXML
    private Label idOrden;
    @FXML
    private TextField nombreContacto;
    @FXML
    private TextField telefonoContacto;
    @FXML
    private DatePicker fechaEntrega;
    @FXML
    private Label total;
    @FXML
    private TextField montoAbonado;
    @FXML
    private Label montoPendiente;
    @FXML
    private TextArea detallesEntrega;
    @FXML
    private ComboBox<?> status;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextArea detallesAdicionales;
    @FXML
    private TableView<LineaDetalle> rptLineasDetalle;
    @FXML
    private TextField descuento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void guardarOrden(ActionEvent event) {
        System.out.println("Guardando...");
    }

    @FXML
    private void cancelarCambios(ActionEvent event) {
        System.out.println("Cancelado!");
    }
    
}
