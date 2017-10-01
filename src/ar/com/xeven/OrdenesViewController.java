/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.xeven;

import ar.com.xeven.domain.LineaDetalle;
import ar.com.xeven.domain.Orden;
import ar.com.xeven.domain.Producto;
import ar.com.xeven.utils.XEVEN;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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
    @FXML private Button menuCerrar;
    @FXML private Button menuEliminar;
    @FXML private Button menuAbout;
    @FXML private Button menuVerProductos;
    @FXML private GridPane detallesOrden;
    @FXML private TableView<Orden> orderTable;
    @FXML private TableColumn<Orden, String> colIDOrden;
    @FXML private TableColumn<Orden, String> colNombreContacto;
    @FXML private TableColumn<Orden, String> colTelefonoContacto;
    @FXML private TableColumn<Orden, String> colFechaEntrega;
    @FXML private TableColumn<Orden,String> colProductos;
    @FXML private TableColumn<Orden, String> colStatus;
    @FXML private TreeTableView<LineaDetalle> rptLineasDetalle;
    @FXML private TreeTableColumn<LineaDetalle, String> colProducto;
    @FXML private TreeTableColumn<LineaDetalle, String> colTamanio;
    @FXML private TreeTableColumn<LineaDetalle, Integer> colCantidad;
    @FXML private TreeTableColumn<LineaDetalle, Double> colPrecioUnitario;
    @FXML private TreeTableColumn<LineaDetalle, Double> colSubtotal;
    @FXML private TreeTableColumn<LineaDetalle, Double> colTotal;
    @FXML private TextField buscador;

    /**
     * Este metodo se llama automaticamente cuando se carga el controller.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // cargo columnas simples (no editables)
        colIDOrden.setCellValueFactory(new PropertyValueFactory<>("idOrden"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<>("telefonoContacto"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));

        // la columna nombre es editable
        colNombreContacto.setCellValueFactory(new PropertyValueFactory<>("nombreContacto"));
        colNombreContacto.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombreContacto.setOnEditCommit(e -> {
            e.getRowValue().setNombreContacto(e.getNewValue());
            e.getRowValue().guardar();
        });
        
        // cargo la columna de status con los valores de estados de la clase XEVEN (internacionalizada)
        statusValues = FXCollections.observableArrayList();
        for(XEVEN.status valor: XEVEN.status.values())
            statusValues.add(valor.getSpanish());
        status.setItems(statusValues);//agrego los valores al comboBox status
        
        colStatus.setEditable(true);
        // agregar a colStatus un filtro por estado
        colStatus.setCellValueFactory(linea -> linea.getValue().statusProperty());
        colStatus.setCellFactory(ComboBoxTableCell.forTableColumn(statusValues));
        colStatus.setOnEditCommit(e -> {
            e.getRowValue().setStatus(e.getNewValue());
            e.getRowValue().guardar();
        });

        //oculto el form de detalles
        detallesOrden.setVisible(false);
        // agrego un listener para la seleccion de un elemento de la tabla
        // esto hace que se pueda a mostrarDetallesOrden() el elemento elegido
        orderTable.getSelectionModel().selectedIndexProperty().addListener(
            (obs,antes,ahora) -> mostrarDetallesOrden(orderTable.getSelectionModel().getSelectedItem()));
        //configuro los botones
        btnGuardar.setDisable(true);
        // agrego graficos a botones
        btnGuardar.setGraphic(new ImageView(new Image("/resources/color/001_06.png", true)));
        btnNuevaOrden.setGraphic(new ImageView(new Image("/resources/color/001_45.png", true)));
        btnAgregarProductos.setGraphic(new ImageView(new Image("/resources/color/001_01.png", true)));
        menuCerrar.setGraphic(new ImageView(new Image("/resources/grey/001_42.png", true)));
        menuEliminar.setGraphic(new ImageView(new Image("/resources/color/001_05.png", true)));
        menuAbout.setGraphic(new ImageView(new Image("/resources/color/001_42.png", true)));
        menuVerProductos.setGraphic(new ImageView(new Image("/resources/color/001_43.png", true)));
        configurarListenersYTablas();
        orderTable.setEditable(true);
        rptLineasDetalle.setEditable(true);
        buscarOrdenes("");
        //selecciono el primer elemento
        if(!orderTable.getItems().isEmpty())
            orderTable.getSelectionModel().select(0);
    }
    private void configurarListenersYTablas(){
        montoAbonado.textProperty().addListener((observable, valorOriginal, valorNuevo) -> {
            Orden ordenSeleccionada = orderTable.getSelectionModel().getSelectedItem();
            if(ordenSeleccionada!=null){
                ordenSeleccionada.setMontoAbonado(Double.valueOf(valorNuevo));
                montoPendiente.setText(Double.toString(ordenSeleccionada.getMontoPendiente()));
            }
        });
        descuento.textProperty().addListener((observable, valorOriginal, valorNuevo) -> {
            Orden ordenSeleccionada = orderTable.getSelectionModel().getSelectedItem();
            if(ordenSeleccionada!=null){
                ordenSeleccionada.setDescuento(Double.valueOf(valorNuevo));
                total.setText(Double.toString(ordenSeleccionada.getTotal()));                
                montoPendiente.setText(Double.toString(ordenSeleccionada.getMontoPendiente()));
            }
        });
        colProducto.setCellValueFactory(param -> param.getValue().getValue().getProducto().getNombreProducto());
        colTamanio.setCellValueFactory(param -> param.getValue().getValue().tamanioElegidoProperty());
        colCantidad.setCellValueFactory(param -> param.getValue().getValue().cantidadProperty().asObject());
        colPrecioUnitario.setCellValueFactory(param -> param.getValue().getValue().precioUnitarioProperty().asObject());
        colTotal.setCellValueFactory(param -> param.getValue().getValue().totalProperty().asObject());
        colSubtotal.setCellValueFactory(param -> param.getValue().getValue().subtotalProperty().asObject());
        
        colProductos.setCellValueFactory(p -> {
            ObservableList<LineaDetalle> lineas = p.getValue().getLineasDetalle();
            if(lineas!=null)
                return new SimpleObjectProperty<>(lineas.stream().map(i->{
                    String valorColumna = i.getCantidad()+" "+
                            i.getProducto().getNombreProducto().get()+
                            " ("+i.getTamanioElegido()+")";
                    return valorColumna;
                        }).collect(Collectors.joining("\n")));
            return new SimpleObjectProperty<>();
        });
        colCantidad.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new IntegerStringConverter()));
        colCantidad.setOnEditCommit(event -> {
            event.getRowValue().getValue().setCantidad(event.getNewValue());
            event.getRowValue().getValue().setSubtotal(event.getRowValue().getValue().getSubtotal());
            event.getRowValue().getValue().setTotal(event.getRowValue().getValue().getTotal());
            this.actualizarMontosManualmente();
            rptLineasDetalle.getRoot().getChildren().forEach(prod -> prod.getValue().setTotal(prod.getValue().getTotal()));
        });
    }
     /**
     * Carga todos los detalles de una orden
     *
     * @param orden
     */
    private void mostrarDetallesOrden(Orden orden) {
        if(orden!=null){
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

            // cargo las lineas de detalle en la tabla
            if(orden.getLineasDetalle()!=null)
                mostrarLineasDetalle(orden.getLineasDetalle());
            // muestro el form con las ordenes
            detallesOrden.setVisible(true);
            // configuro botones
            btnGuardar.setText("Guardar cambios");
            btnGuardar.setDisable(false);
            btnAgregarProductos.setDisable(false);
        }
    }
    @FXML private void nuevaOrden(ActionEvent e){
        idOrden.setText("");
        nombreContacto.setText("");
        nombreContacto.requestFocus();
        telefonoContacto.setText("");
        detallesEntrega.setText("");
        detallesAdicionales.setText("");
        montoAbonado.setText("0");
        descuento.setText("0");
        montoPendiente.setText("0");
        total.setText("0");
        fechaEntrega.setValue(LocalDate.now());
        status.setValue(statusValues.get(0));
        if(rptLineasDetalle.getRoot()!=null && rptLineasDetalle.getRoot().getChildren()!=null)
            rptLineasDetalle.getRoot().getChildren().clear();
        // configurar botones
        btnGuardar.setText("Crear nueva");
        btnAgregarProductos.setDisable(true);
    }
    @FXML private void guardarOrden(ActionEvent e) {
        Orden laOrden;
        if(btnGuardar.getText().equals("Crear nueva")){
            laOrden = new Orden(
                    nombreContacto.getText(),
                    telefonoContacto.getText(),
                    detallesAdicionales.getText(),
                    fechaEntrega.getValue(),
                    detallesEntrega.getText(),                    
                    Double.valueOf(montoAbonado.getText()),
                    Double.valueOf(descuento.getText()),
                    status.getValue()
            );
        }else{
            laOrden = orderTable.getSelectionModel().getSelectedItem();
            laOrden.setIdOrden(Integer.valueOf(idOrden.getText()));
            laOrden.setNombreContacto(nombreContacto.getText());
            laOrden.setMontoAbonado(Double.valueOf(montoAbonado.getText()));
            laOrden.setDescuento(Double.valueOf(descuento.getText()));
            laOrden.setDetallesEntrega(new SimpleStringProperty(detallesEntrega.getText()));
            laOrden.setTelefonoContacto(new SimpleStringProperty(telefonoContacto.getText()));
            laOrden.setDetallesAdicionales(new SimpleStringProperty(detallesAdicionales.getText()));
            laOrden.setFechaEntrega(new SimpleObjectProperty<>(fechaEntrega.getValue()));            
            laOrden.setStatus(status.getValue());
            laOrden.guardar();
        }
        buscarOrdenes("");
        seleccionarPorID(laOrden.getIdOrden());
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
        alert.setHeaderText("eOrders v2.1 - Sistema de gestión de órdenes");
        alert.setContentText("Para mayor información contactarse a ordenes@xeven.com.ar");
        alert.showAndWait();
    }

    private void mostrarLineasDetalle(ObservableList<LineaDetalle> lineas) {
        rptLineasDetalle.setRoot(new TreeItem<>());
        rptLineasDetalle.getRoot().setExpanded(true);
        rptLineasDetalle.setShowRoot(false);
        lineas.stream().forEach((linea) -> {
             TreeItem<LineaDetalle> producto = new TreeItem<>(linea);
             producto.setExpanded(true);
             linea.getAccesorios().forEach(sublinea->producto.getChildren().add(new TreeItem<>(sublinea)));
             rptLineasDetalle.getRoot().getChildren().add(producto);
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
    @FXML public void mostrarDetalleProducto(TreeTableColumn.CellEditEvent<LineaDetalle, String> event) {
        System.out.println("Mostrar producto seleccionado");
    }
    @FXML private void cambioEstado(ActionEvent event) {
        //actualizar el modelo al cambiar el estado
    }

    private void buscarOrdenes(String query) {
        // cargo el listado de ordenes desde el modelo
        orderTable.setItems(Orden.getOrdenes(query));
    }

    private void seleccionarPorID(String idOrden) {
        orderTable.getItems().stream()
                .filter(linea ->linea.getIdOrden().equals(idOrden))                        
                .forEachOrdered(linea -> orderTable.getSelectionModel().select(linea)
        );
    }

    @FXML private void buscar(KeyEvent event) {
        buscarOrdenes(buscador.getText());        
    }

    public void verProductos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductosView.fxml"));
        Parent root = loader.load();
        Stage stage=(Stage) buscador.getScene().getWindow();
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setMaximized(true);
        stage.getIcons().add(new Image("/resources/color/001_56.png"));
        stage.setTitle("eOrders - Productos- XEVEN");
        stage.setScene(scene);
        stage.show();
    }
}
