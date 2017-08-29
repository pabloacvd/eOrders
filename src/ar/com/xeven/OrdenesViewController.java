/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.xeven;

import ar.com.xeven.domain.LineaDetalle;
import ar.com.xeven.domain.Orden;
import ar.com.xeven.utils.XEVEN;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author pacevedo
 */
public class OrdenesViewController implements Initializable {

    private ObservableList<String> statusValues;
    private EOrders application;
    
    @FXML
    private TableView<Orden> orderTable;
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
    private ComboBox<String> status;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextArea detallesAdicionales;
    @FXML
    private TreeTableView<LineaDetalle> rptLineasDetalle;
    @FXML
    private TextField descuento;
    @FXML
    private MenuItem menuNuevaOrden;
    @FXML
    private MenuItem menuCerrar;
    @FXML
    private MenuItem menuEliminar;
    @FXML
    private MenuItem menuAbout;
    @FXML
    private Button btnAgregarProductos;
    @FXML
    private TableColumn<Orden, String> colIDOrden;
    @FXML
    private TableColumn<Orden, String> colNombreContacto;
    @FXML
    private TableColumn<Orden, String> colTelefonoContacto;
    @FXML
    private TableColumn<Orden, String> colFechaEntrega;
    @FXML
    private TableColumn<Orden, String> colStatus;
    @FXML
    private TreeTableColumn<LineaDetalle, String> colProducto;
    @FXML
    private TreeTableColumn<LineaDetalle, String> colTamanio;
    @FXML
    private TreeTableColumn<LineaDetalle, Integer> colCantidad;
    @FXML
    private TreeTableColumn<LineaDetalle, Double> colPrecioUnitario;
    @FXML
    private TreeTableColumn<LineaDetalle, Double> colSubtotal;
    @FXML
    private TreeTableColumn<LineaDetalle, Double> colTotal;
    @FXML
    private GridPane detallesOrden;

     /**
     * Relaciono el main con la informacion del controlador.
     *
     * @param main
     */
    public void setMain(EOrders main) {
        this.application = main;
        // Add observable list data to the table
        orderTable.setEditable(true);
        orderTable.setItems(main.getLstOrdenes());
        rptLineasDetalle.setEditable(true);
        
    }
     /**
     * Carga todos los detalles de una orden
     *
     * @param orden or null
     */
    private void mostrarDetallesOrden(Orden orden) {
        if (orden != null) {
            idOrden.setText(orden.getIdOrden());
            nombreContacto.setText(orden.getNombreContacto().get());
            telefonoContacto.setText(orden.getTelefonoContacto().get());
    
            total.setText(Double.toString(orden.getTotal()));
            montoAbonado.setText(Double.toString(orden.getMontoAbonado()));
            descuento.setText(Double.toString(orden.getDescuento()));
            montoPendiente.setText(Double.toString(orden.getMontoPendiente()));
            
            montoAbonado.textProperty().addListener((observable, oldValue, newValue) -> {
                Orden ordenSeleccionada = orderTable.getSelectionModel().getSelectedItem();
                ordenSeleccionada.setMontoAbonado(Double.valueOf(newValue));
                montoPendiente.setText(Double.toString(ordenSeleccionada.getMontoPendiente()));
            });
            descuento.textProperty().addListener((observable, oldValue, newValue) -> {
                Orden ordenSeleccionada = orderTable.getSelectionModel().getSelectedItem();
                ordenSeleccionada.setDescuento(Double.valueOf(newValue));
                total.setText(Double.toString(ordenSeleccionada.getTotal()));                
                montoPendiente.setText(Double.toString(ordenSeleccionada.getMontoPendiente()));
            });
                        
            fechaEntrega.setValue(orden.getFechaEntrega().get());
            detallesEntrega.setText(orden.getDetallesEntrega().get());
            detallesAdicionales.setText(orden.getDetallesAdicionales().get());
            status.setValue(orden.getStatus());
            mostrarLineasDetalle(orden.getLineasDetalle());
            detallesOrden.setVisible(true);
        } else {
            detallesOrden.setVisible(false);
        }
    }

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colIDOrden.setCellValueFactory(cellData -> cellData.getValue().idOrdenProperty());
        colNombreContacto.setCellValueFactory(cellData -> cellData.getValue().getNombreContacto());
        colNombreContacto.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombreContacto.setOnEditCommit(event -> event.getRowValue().setNombreContacto(event.getNewValue()));
        
        colTelefonoContacto.setCellValueFactory(cellData -> cellData.getValue().getTelefonoContacto());
        colFechaEntrega.setCellValueFactory(cellData -> cellData.getValue().getFechaEntrega().asString());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("DD dd/mm");

        statusValues = FXCollections.observableArrayList();
        for(XEVEN.status valor:XEVEN.status.values())
            statusValues.add(valor.getSpanish());
        status.setItems(statusValues);//prepare the status values
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        colStatus.setCellFactory(ComboBoxTableCell.forTableColumn(statusValues));

        mostrarDetallesOrden(null);
        orderTable.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesOrden(orderTable.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void guardarOrden(ActionEvent event) {
        System.out.println("Guardando...");
    }

    @FXML
    private void cancelarCambios(ActionEvent event) {
        System.out.println("Cancelado!");
    }

    @FXML
    private void nuevaOrden(ActionEvent event) {
    }

    @FXML
    private void cerrar(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void eliminarOrden(ActionEvent event) {
        //eliminar de la base de datos
    }

    @FXML
    private void acercaDe(ActionEvent event) {
        System.out.println("eOrders - Abrir un alert con información del sistema.");
    }

    private void mostrarLineasDetalle(ListProperty<LineaDetalle> lineas) {
        rptLineasDetalle.setRoot(new TreeItem<>(lineas.get(0)));
        rptLineasDetalle.getRoot().setExpanded(true);
        rptLineasDetalle.setShowRoot(false);
        
        lineas.stream().forEach((linea) -> {
             TreeItem<LineaDetalle> producto = new TreeItem<>(linea);
             producto.setExpanded(true);
             producto.addEventHandler(TreeItem.branchCollapsedEvent(),(TreeModificationEvent<String> event) -> {
                 event.getTreeItem().setExpanded(true);
             });
             linea.getAccesorios().forEach((sublinea) -> {
                 producto.getChildren().add(new TreeItem<>(sublinea));
            });
             rptLineasDetalle.getRoot().getChildren().add(producto);
        });
        
        //En el doble click del producto se tendria que abrir una ventana
        //con los detalles del producto.
        colProducto.setCellValueFactory(param -> param.getValue().getValue().getProducto().getNombreProducto());
        colTamanio.setCellValueFactory(param -> param.getValue().getValue().tamanioElegidoProperty());
        colCantidad.setCellValueFactory(param -> param.getValue().getValue().cantidadProperty().asObject());
        colPrecioUnitario.setCellValueFactory(param -> param.getValue().getValue().precioUnitarioProperty().asObject());
        colTotal.setCellValueFactory(param -> param.getValue().getValue().totalProperty().asObject());
        colSubtotal.setCellValueFactory(param -> param.getValue().getValue().subtotalProperty().asObject());
        
        colCantidad.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new IntegerStringConverter()));
        colCantidad.setOnEditCommit(event -> {
            event.getRowValue().getValue().setCantidad(event.getNewValue());
            event.getRowValue().getValue().setSubtotal(event.getRowValue().getValue().getSubtotal());
            event.getRowValue().getValue().setTotal(event.getRowValue().getValue().getTotal());
            this.actualizarMontosManualmente();
            rptLineasDetalle.getRoot().getChildren().forEach((producto) -> {
                producto.getValue().setTotal(producto.getValue().getTotal());                
            });
        });
    }

    @FXML
    private void actualizarMontos(InputMethodEvent event) {
        this.actualizarMontosManualmente();   
    }

    private void actualizarMontosManualmente() {
        int ordenActual = orderTable.getSelectionModel().getSelectedIndex();
        orderTable.getSelectionModel().clearSelection();
        orderTable.getSelectionModel().select(ordenActual);
    }
    
    @FXML
    private void mostrarDetalleProducto(TreeTableColumn.CellEditEvent<LineaDetalle, String> event) {
        System.out.println("Mostrar producto seleccionado");
    }

    @FXML
    private void cambioEstado(ActionEvent event) {
    }
}
