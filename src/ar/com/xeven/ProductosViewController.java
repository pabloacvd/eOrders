package ar.com.xeven;

import ar.com.xeven.domain.LineaDetalle;
import ar.com.xeven.domain.Producto;
import ar.com.xeven.utils.XEVEN;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author pacevedo
 */
public class ProductosViewController implements Initializable {
    @FXML private MenuItem menuNuevaOrden;
    @FXML private MenuItem menuCerrar;
    @FXML private MenuItem menuEliminar;
    @FXML private MenuItem menuAbout;
    @FXML private DatePicker fechaPrecio;
    @FXML private Button btnGuardar;
    @FXML private TreeTableView<Producto> tablaProductos;
    @FXML private TreeTableColumn<Producto, String> colNombre;
    @FXML private TreeTableColumn<Producto, String> colDetalles;
    @FXML private TreeTableColumn<Producto, String> colPrecio;
    @FXML private TreeTableColumn<Producto, String> colTamanio;
    @FXML private GridPane detallesProd;
    @FXML private Label prodID;
    @FXML private TextField nombreProducto;
    @FXML private TextArea detallesProducto;
    @FXML private CheckBox soloAccesorio;
    @FXML private Button btnAgregarAccesorios;
    @FXML private TableView<Map.Entry<String, Double>> tablaPrecios;
    @FXML private TableColumn<Map.Entry<String, Double>, String> colTamanioPrecio1;
    @FXML private TableColumn<Map.Entry<String, Double>, Double> colTamanioPrecio2;
    @FXML private TableView<Producto> accesorioTabla;
    @FXML private TableColumn<Producto, String> accesorioNombre;
    @FXML private TableColumn<Producto, String> accesorioDetalles;
    @FXML private TableColumn<Producto, String> accesorioTamanio;
    @FXML private TableColumn<Producto, String> accesorioPrecio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // cargo el listado de productos desde el modelo
        Connection c = XEVEN.getConnection();
        ObservableList<Producto> productos = Producto.getProductos(c);
        tablaProductos.setRoot(new TreeItem<>(productos.get(0)));
        tablaProductos.getRoot().setExpanded(true);
        tablaProductos.setShowRoot(false);
        
        productos.stream().forEach((prod) -> {
             TreeItem<Producto> producto = new TreeItem<>(prod);
             producto.setExpanded(false);
             prod.getAccesoriosDisponibles().forEach((sublinea) -> {
                 producto.getChildren().add(new TreeItem<>(sublinea));
            });
            tablaProductos.getRoot().getChildren().add(producto);
        });
        
        colNombre.setCellValueFactory(p -> p.getValue().getValue().getNombreProducto());
        colDetalles.setCellValueFactory(p ->p.getValue().getValue().getDetallesProducto());
        colDetalles.setCellFactory((TreeTableColumn<Producto, String> p) -> {
            TreeTableCell<Producto, String> cell = new TreeTableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(colDetalles.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        colTamanio.setCellValueFactory((CellDataFeatures<Producto, String> p) -> {
            ObservableMap<String, Double> mapa = p.getValue().getValue().getPrecioPorTamanio().get();
            String valor = mapa.keySet().stream().collect(Collectors.joining("\n"));
            return new SimpleObjectProperty<>(valor);
            });
        colPrecio.setCellValueFactory((CellDataFeatures<Producto, String> p) -> {
            ObservableMap<String, Double> mapa = p.getValue().getValue().getPrecioPorTamanio().get();
            String valor = mapa.values().stream().map(i->i.toString()).collect(Collectors.joining("\n"));
            return new SimpleObjectProperty<>(valor);
            });
        
        // agrego un listener para la seleccion de un elemento de la tabla
        // esto hace que se pueda a mostrarDetallesOrden() el elemento elegido
        tablaProductos.getSelectionModel().selectedIndexProperty().addListener(
                (observable, valorOriginal, valorNuevo) -> 
                mostrarDetallesProducto(tablaProductos.getSelectionModel().getSelectedItem().getValue()));
        //configuro los botones
        btnGuardar.setDisable(true);
        
        // agrego graficos a botones
        btnGuardar.setGraphic(new ImageView(new Image("/resources/color/001_06.png", true)));
        btnAgregarAccesorios.setGraphic(new ImageView(new Image("/resources/color/001_01.png", true)));
    }
    private void mostrarDetallesProducto(Producto producto) {
        prodID.setText(producto.getProdID().get());
        nombreProducto.setText(producto.getNombreProducto().get());
        detallesProducto.setText(producto.getDetallesProducto().get());
        soloAccesorio.setSelected(producto.isSoloAccesorio());
        fechaPrecio.setValue(producto.getFechaModificacionPrecio().get());
        
        colTamanioPrecio1.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, Double>, String> param)
                -> new SimpleObjectProperty(param.getValue().getKey()));
        colTamanioPrecio2.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, Double>, Double> param)
                -> new SimpleObjectProperty(param.getValue().getValue()));    
        ObservableList<Map.Entry<String, Double>> precios = FXCollections.observableArrayList(producto.getPrecioPorTamanio().entrySet());
        tablaPrecios.setItems(precios);

        accesorioTabla.setItems(producto.getAccesoriosDisponibles());
        accesorioNombre.setCellValueFactory(p -> p.getValue().getNombreProducto());
        accesorioNombre.setCellFactory((TableColumn<Producto, String> p) -> {
            TableCell<Producto, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(accesorioNombre.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        accesorioDetalles.setCellValueFactory(p -> p.getValue().getDetallesProducto());
        accesorioDetalles.setCellFactory((TableColumn<Producto, String> p) -> {
            TableCell<Producto, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(accesorioDetalles.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        accesorioTamanio.setCellValueFactory((TableColumn.CellDataFeatures<Producto, String> p) -> {
            ObservableMap<String, Double> mapa = p.getValue().getPrecioPorTamanio().get();
            String valor = mapa.keySet().stream().collect(Collectors.joining("\n"));
            return new SimpleObjectProperty<>(valor);
            });
        accesorioPrecio.setCellValueFactory((TableColumn.CellDataFeatures<Producto, String> p) -> {
            ObservableMap<String, Double> mapa = p.getValue().getPrecioPorTamanio().get();
            String valor = mapa.values().stream().map(i->i.toString()).collect(Collectors.joining("\n"));
            return new SimpleObjectProperty<>(valor);
            });
        
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
    private void cancelarCambios(ActionEvent event) {
    }

    @FXML
    private void guardarOrden(ActionEvent event) {
    }

    
}
