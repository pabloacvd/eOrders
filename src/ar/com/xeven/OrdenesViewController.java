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
import javafx.scene.control.Alert;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    //Datos de aplicacion
    private ObservableList<String> statusValues;
    
    // GUI
    @FXML private Label idOrden;
    @FXML private Label montoPendiente;
    @FXML private Label total;
    @FXML private DatePicker fechaEntrega;
    @FXML private TextField nombreContacto;
    @FXML private TextField telefonoContacto;
    @FXML private TextField montoAbonado;
    @FXML private TextField descuento;
    @FXML private TextArea detallesEntrega;
    @FXML private TextArea detallesAdicionales;
    @FXML private ComboBox<String> status;
    @FXML private Button btnGuardar;
    @FXML private Button btnNuevaOrden;
    @FXML private Button btnAgregarProductos;
    @FXML private MenuItem menuNuevaOrden;
    @FXML private MenuItem menuCerrar;
    @FXML private MenuItem menuEliminar;
    @FXML private MenuItem menuAbout;
    @FXML private GridPane detallesOrden;
    @FXML private TableView<Orden> orderTable;
    @FXML private TableColumn<Orden, String> colIDOrden;
    @FXML private TableColumn<Orden, String> colNombreContacto;
    @FXML private TableColumn<Orden, String> colTelefonoContacto;
    @FXML private TableColumn<Orden, String> colFechaEntrega;
    @FXML private TableColumn<Orden, String> colStatus;
    @FXML private TreeTableView<LineaDetalle> rptLineasDetalle;
    @FXML private TreeTableColumn<LineaDetalle, String> colProducto;
    @FXML private TreeTableColumn<LineaDetalle, String> colTamanio;
    @FXML private TreeTableColumn<LineaDetalle, Integer> colCantidad;
    @FXML private TreeTableColumn<LineaDetalle, Double> colPrecioUnitario;
    @FXML private TreeTableColumn<LineaDetalle, Double> colSubtotal;
    @FXML private TreeTableColumn<LineaDetalle, Double> colTotal;

    /**
     * Este metodo se llama automaticamente cuando se carga el controller.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // cargo el listado de ordenes desde el modelo
        orderTable.setItems(Orden.getOrdenes());
        orderTable.setEditable(true);
        rptLineasDetalle.setEditable(true);
        
        // cargo columnas simples (no editables)
        colIDOrden.setCellValueFactory(new PropertyValueFactory<>("idOrden"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<>("telefonoContacto"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));

        // la columna nombre es editable
        colNombreContacto.setCellValueFactory(new PropertyValueFactory<>("nombreContacto"));
        colNombreContacto.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombreContacto.setOnEditCommit(e -> e.getRowValue().setNombreContacto(e.getNewValue()));
        
        // cargo la columna de status con los valores de estados de la clase XEVEN (internacionalizada)
        statusValues = FXCollections.observableArrayList();
        for(XEVEN.status valor: XEVEN.status.values())
            statusValues.add(valor.getSpanish());
        status.setItems(statusValues);//agrego los valores al comboBox status
        
        colStatus.setCellValueFactory(linea -> linea.getValue().statusProperty());
        colStatus.setCellFactory(ComboBoxTableCell.forTableColumn(statusValues));
        colStatus.setOnEditCommit(e -> e.getRowValue().setStatus(e.getNewValue()));

        //oculto el form de detalles
        detallesOrden.setVisible(false);
        // agrego un listener para la seleccion de un elemento de la tabla
        // esto hace que se pueda a mostrarDetallesOrden() el elemento elegido
        orderTable.getSelectionModel().selectedIndexProperty().addListener(
                (observable, valorOriginal, valorNuevo) -> mostrarDetallesOrden(orderTable.getSelectionModel().getSelectedItem()));
        
        //configuro los botones
        btnGuardar.setDisable(true);
        
        // agrego graficos a botones
        btnGuardar.setGraphic(new ImageView(new Image("/resources/color/001_06.png", true)));
        btnNuevaOrden.setGraphic(new ImageView(new Image("/resources/color/001_45.png", true)));
        btnAgregarProductos.setGraphic(new ImageView(new Image("/resources/color/001_01.png", true)));
    }
     /**
     * Carga todos los detalles de una orden
     *
     * @param orden
     */
    private void mostrarDetallesOrden(Orden orden) {
        idOrden.setText(orden.getIdOrden());
        nombreContacto.setText(orden.getNombreContacto());
        telefonoContacto.setText(orden.getTelefonoContacto());
        detallesEntrega.setText(orden.getDetallesEntrega());
        detallesAdicionales.setText(orden.getDetallesAdicionales().get());

        total.setText(Double.toString(orden.getTotal()));
        montoAbonado.setText(Double.toString(orden.getMontoAbonado()));
        descuento.setText(Double.toString(orden.getDescuento()));
        montoPendiente.setText(Double.toString(orden.getMontoPendiente()));
        
        fechaEntrega.setValue(orden.getFechaEntrega());
        status.setValue(orden.getStatus());

        montoAbonado.textProperty().addListener((observable, valorOriginal, valorNuevo) -> {
            Orden ordenSeleccionada = orderTable.getSelectionModel().getSelectedItem();
            ordenSeleccionada.setMontoAbonado(Double.valueOf(valorNuevo));
            montoPendiente.setText(Double.toString(ordenSeleccionada.getMontoPendiente()));
        });
        descuento.textProperty().addListener((observable, valorOriginal, valorNuevo) -> {
            Orden ordenSeleccionada = orderTable.getSelectionModel().getSelectedItem();
            ordenSeleccionada.setDescuento(Double.valueOf(valorNuevo));
            total.setText(Double.toString(ordenSeleccionada.getTotal()));                
            montoPendiente.setText(Double.toString(ordenSeleccionada.getMontoPendiente()));
        });

        // cargo las lineas de detalle en la tabla
        mostrarLineasDetalle(orden.getLineasDetalle());
        
        // muestro el form con las ordenes
        detallesOrden.setVisible(true);
    }
    @FXML private void nuevaOrden(ActionEvent e){
        idOrden.setText(null);
        nombreContacto.setText(null);
        nombreContacto.requestFocus();
        telefonoContacto.setText(null);
        detallesEntrega.setText(null);
        detallesAdicionales.setText(null);
        total.setText(null);
        montoAbonado.setText(null);
        descuento.setText(null);
        montoPendiente.setText(null);
        fechaEntrega.setValue(null);
        status.setValue(null);
        rptLineasDetalle.getRoot().getChildren().clear();
        
        // configurar botones
    }
    @FXML private void guardarOrden(ActionEvent e) {
        System.out.println("Guardando...");
        //tomar la orden que está viendo el usuario y guardarla
        //el mensaje se envia a la clase del modelo, no se resuelve acá
    }

    @FXML private void cerrar(ActionEvent event) {
        System.exit(0);
    }

    @FXML private void eliminarOrden(ActionEvent event) {
        //eliminar desde el modelo
    }
    @FXML private void agregarProductos(ActionEvent event) {
        // todo -> abrir frame con drag&drop de productos
    }
    @FXML private void acercaDe(ActionEvent event) {
        System.out.println("eOrders - Abrir un alert con información del sistema.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("eOrders - XEVEN");
        alert.setHeaderText("eOrders v1.0 - Sistema de gestión de órdenes");
        alert.setContentText("Para mayor información contactarse a ordenes@xeven.com.ar");
        alert.showAndWait();
    }

    private void mostrarLineasDetalle(ObservableList<LineaDetalle> lineas) {
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
        //con los detalles del producto (DetallesProductoView)
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

    @FXML private void actualizarMontos(InputMethodEvent event) {
        this.actualizarMontosManualmente();   
    }

    private void actualizarMontosManualmente() {
        int ordenActual = orderTable.getSelectionModel().getSelectedIndex();
        orderTable.getSelectionModel().clearSelection();
        orderTable.getSelectionModel().select(ordenActual);
    }
    
    @FXML private void mostrarDetalleProducto(TreeTableColumn.CellEditEvent<LineaDetalle, String> event) {
        System.out.println("Mostrar producto seleccionado");
    }

    @FXML private void cambioEstado(ActionEvent event) {
        //actualizar el modelo al cambiar el estado (esto puede hacerse al guardar)
    }
}
