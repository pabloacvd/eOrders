package ar.com.xeven;

import ar.com.xeven.domain.Producto;
import ar.com.xeven.utils.XEVEN;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;

/**
 * FXML Controller class
 *
 * @author pacevedo
 */
public class ProductosViewController implements Initializable {
    @FXML private DatePicker fechaPrecio;
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
    @FXML private TableView<Map.Entry<String, Double>> tablaPrecios;
    @FXML private TableColumn<Map.Entry<String, Double>, String> colTamanioPrecio1;
    @FXML private TableColumn<Map.Entry<String, Double>, Double> colTamanioPrecio2;
    @FXML private TableView<Producto> accesorioTabla;
    @FXML private TableColumn<Producto, String> accesorioNombre;
    @FXML private TableColumn<Producto, String> accesorioDetalles;
    @FXML private TableColumn<Producto, String> accesorioTamanio;
    @FXML private TableColumn<Producto, String> accesorioPrecio;
    @FXML private TextField txtTamanio;
    @FXML private TextField txtPrecio;
    @FXML private Button menuCerrar;
    @FXML private Button menuAbout;
    @FXML private Button btnGuardar;
    @FXML private Button btnNuevo;
    @FXML private Button menuVerOrdenes;
    @FXML private Button botonAgregarPrecio;
    @FXML private TextField buscador;  
    private double scrollDirection = 0;
    private Timeline scrolltimeline = new Timeline();
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    public void cargarProductos(String query){
        Connection c = XEVEN.getConnection();
        ObservableList<Producto> productos = Producto.getProductos(query);
        tablaProductos.getRoot().getChildren().clear();
        productos.stream().forEach((prod) -> {
            TreeItem<Producto> producto = new TreeItem<>(prod);
            producto.setExpanded(true);
            producto.setGraphic(new ImageView(new Image("/resources/color/001_43.png", true)));
            prod.getAccesoriosDisponibles().forEach((sublinea) ->
                producto.getChildren().add(new TreeItem<>(sublinea))
            );
            tablaProductos.getRoot().getChildren().add(producto);
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // cargo el listado de productos desde el modelo
        tablaProductos.setRoot(new TreeItem<>());
        tablaProductos.getRoot().setExpanded(true);
        tablaProductos.setShowRoot(false);
        cargarProductos("");
        configurarTablas();
        fechaPrecio.setDisable(true);
        //configuro las columnas
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
            if(mapa!=null)
                return new SimpleObjectProperty<>(mapa.keySet().stream().collect(Collectors.joining("\n")));
            return new SimpleObjectProperty<>();
        });
        colPrecio.setCellValueFactory((CellDataFeatures<Producto, String> p) -> {
            ObservableMap<String, Double> mapa = p.getValue().getValue().getPrecioPorTamanio().get();
            if(mapa!=null)
                return new SimpleObjectProperty<>(mapa.values().stream().map(i->i.toString()).collect(Collectors.joining("\n")));
            return new SimpleObjectProperty<>();
        });
        tablaProductos.setRowFactory(this::rowFactory);
        // agrego un listener para la seleccion de un elemento de la tabla
        // esto hace que se pueda a mostrarDetallesOrden() el elemento elegido
        tablaProductos.getSelectionModel().selectedIndexProperty().addListener((observable, valorOriginal, valorNuevo) -> {
            if(tablaProductos.getSelectionModel().getSelectedItem()!=null)
                mostrarDetallesProducto(tablaProductos.getSelectionModel().getSelectedItem().getValue());
            });
        //configuro los botones
        btnGuardar.setDisable(true);      
        btnGuardar.setGraphic(new ImageView(new Image("/resources/color/001_06.png", true)));
        botonAgregarPrecio.setGraphic(new ImageView(new Image("/resources/color/001_01.png", true)));
        btnNuevo.setGraphic(new ImageView(new Image("/resources/color/001_45.png", true)));
        botonAgregarPrecio.setGraphic(new ImageView(new Image("/resources/color/001_01.png", true)));
        menuCerrar.setGraphic(new ImageView(new Image("/resources/grey/001_42.png", true)));
        menuAbout.setGraphic(new ImageView(new Image("/resources/color/001_42.png", true)));
        menuVerOrdenes.setGraphic(new ImageView(new Image("/resources/color/001_43.png", true)));
        
        //configuro el scroll para el drag&drop
        setupScrolling();
        //cargo el menu contextual
        loadContextMenus();
        //selecciono el primer elemento
        if(tablaProductos.getRoot().getChildren().size()>0)
            tablaProductos.getSelectionModel().select(0);
    }
    private void loadContextMenus(){
        MenuItem mi1 = new MenuItem("Eliminar");
        mi1.setOnAction(e -> {
            TreeItem<Producto> seleccionado = tablaProductos.getSelectionModel().getSelectedItem();            
            if(seleccionado.getParent().getParent()== null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("eOrders - XEVEN");
                alert.setHeaderText("Eliminar "+seleccionado.getValue().getNombreProducto().get());
                alert.setContentText("¿Confirma que desea eliminar este producto del catálogo?");
                alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> seleccionado.getValue().eliminar());
            }else{
                int padre = Integer.valueOf(seleccionado.getParent().getValue().getProdID().get());
                seleccionado.getValue().eliminarAccesorio(padre);
            }
            cargarProductos("");
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tablaProductos.setContextMenu(menu);
        
        MenuItem mi2 = new MenuItem("Eliminar precio");
        mi2.setOnAction(e -> {
            Map.Entry<String, Double> seleccionado = tablaPrecios.getSelectionModel().getSelectedItem();            
            Producto padre = tablaProductos.getSelectionModel().getSelectedItem().getValue();
            padre.eliminarPrecio(seleccionado.getKey());
            tablaPrecios.getItems().remove(seleccionado);
        });
        ContextMenu menu2 = new ContextMenu();
        menu2.getItems().add(mi2);
        tablaPrecios.setContextMenu(menu2);
    }
    private void configurarTablas(){   
        //tabla accesorios
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
        
        //tabla precios
        colTamanioPrecio1.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, Double>, String> param)
                -> new SimpleObjectProperty(param.getValue().getKey()));
        colTamanioPrecio2.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, Double>, Double> param)
                -> new SimpleObjectProperty(param.getValue().getValue()));    
        colTamanioPrecio1.setCellFactory(TextFieldTableCell.forTableColumn());
        colTamanioPrecio2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colTamanioPrecio1.setOnEditCommit(e -> actualizarPrecio(e.getNewValue(), e.getRowValue().getValue()));
        colTamanioPrecio2.setOnEditCommit(e -> actualizarPrecio(e.getRowValue().getKey(), e.getNewValue()));
        
    }
    private void mostrarDetallesProducto(Producto producto) {
        prodID.setText(producto.getProdID().get());
        nombreProducto.setText(producto.getNombreProducto().get());
        detallesProducto.setText(producto.getDetallesProducto().get());
        soloAccesorio.setSelected(producto.isSoloAccesorio());
        fechaPrecio.setValue(producto.getFechaModificacionPrecio().get());
        fechaPrecio.setVisible(true);
        ObservableList<Map.Entry<String, Double>> precios = FXCollections.observableArrayList(producto.getPrecioPorTamanio().entrySet());
        tablaPrecios.setItems(precios);
        accesorioTabla.setItems(producto.getAccesoriosDisponibles());
        btnGuardar.setDisable(false);
        btnGuardar.setText("Guardar");
        botonAgregarPrecio.setDisable(false);
    }
    private void limpiarDetallesProducto() {
        prodID.setText("Nuevo");
        nombreProducto.setText("");
        detallesProducto.setText("");
        soloAccesorio.setSelected(false);
        fechaPrecio.setVisible(false);
        tablaPrecios.getItems().clear();
        accesorioTabla.getItems().clear();
        btnGuardar.setDisable(true);
        botonAgregarPrecio.setDisable(true);
    }
    @FXML private void cerrar(ActionEvent event) {
        System.exit(0);
    }
    @FXML private void acercaDe(ActionEvent event) {
        System.out.println("eOrders - Abrir un alert con información del sistema.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("eOrders - XEVEN");
        alert.setHeaderText("eOrders v2.1 - Sistema de gestión de órdenes");
        alert.setContentText("Para mayor información contactarse a ordenes@xeven.com.ar");
        alert.showAndWait();
    }
    @FXML
    private void guardar(ActionEvent event) {
        Producto nuevoProd;
        if(btnGuardar.getText().equals("Crear")){
            nuevoProd = new Producto(
                nombreProducto.getText(),
                detallesProducto.getText()
            );
        }else{
            nuevoProd = tablaProductos.getSelectionModel().getSelectedItem().getValue();
            nuevoProd.setNombreProducto(nombreProducto.getText());
            nuevoProd.setDetallesProducto(detallesProducto.getText());
            nuevoProd.setSoloAccesorio(soloAccesorio.isSelected());
            nuevoProd.guardar();
        }
        cargarProductos("");
        seleccionarPorID(nuevoProd.getProdID().get());
    }
    private void seleccionarPorID(String prodID) {
        tablaProductos.getRoot().getChildren().stream()
                .filter(linea ->linea.getValue().getProdID().get().equals(prodID))
                .forEachOrdered(linea -> tablaProductos.getSelectionModel().select(linea)
        );
    }
    @FXML
    private void nuevo(ActionEvent event) {
        limpiarDetallesProducto();
        tablaProductos.getSelectionModel().clearSelection();
        btnGuardar.setDisable(false);
        btnGuardar.setText("Crear");
    }

    private void setupScrolling() {
        scrolltimeline.setCycleCount(Timeline.INDEFINITE);
        scrolltimeline.getKeyFrames().add(new KeyFrame(Duration.millis(20), "Scroll", (ActionEvent) -> dragScroll()));
        tablaProductos.setOnDragExited(event -> {
            scrollDirection = (event.getY() > 0)?1/tablaProductos.getExpandedItemCount():-1/tablaProductos.getExpandedItemCount(); 
            scrolltimeline.play();
        });
        tablaProductos.setOnDragEntered(e -> scrolltimeline.stop());
        tablaProductos.setOnDragDone(e -> scrolltimeline.stop());
    }

    private void dragScroll() {
        ScrollBar sb = null;
        for(Node bar : tablaProductos.lookupAll(".scroll-bar"))
            if(bar instanceof ScrollBar)
                if (((ScrollBar) bar).getOrientation().equals(Orientation.VERTICAL))
                    sb = (ScrollBar) bar;
        if (sb != null) {
            double nuevoValor = sb.getValue()+scrollDirection;
            nuevoValor = Math.min(nuevoValor, 1.0);
            nuevoValor = Math.max(nuevoValor, 0.0);
            sb.setValue(nuevoValor);
        }
    }

    private TreeTableRow<Producto> rowFactory(TreeTableView<Producto> view) {
        TreeTableRow<Producto> linea = new TreeTableRow<>();
        linea.setOnDragDetected(event -> {
            if (!linea.isEmpty()) {
                Dragboard db = linea.startDragAndDrop(TransferMode.COPY);
                db.setDragView(linea.snapshot(null, null));
                ClipboardContent cc = new ClipboardContent();
                cc.put(SERIALIZED_MIME_TYPE, linea.getIndex());
                db.setContent(cc);
                event.consume();
            }
        });

        linea.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (acceptable(db, linea)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        });

        linea.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (acceptable(db, linea)) {
                int index = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                TreeItem itemArrastrado = tablaProductos.getTreeItem(index);
                //itemArrastrado.getParent().getChildren().remove(itemArrastrado);
                getTarget(linea).getChildren().add(itemArrastrado);
                Producto accesorio = (Producto) itemArrastrado.getValue();
                linea.getTreeItem().getValue().agregarAccesorio(accesorio);
                event.setDropCompleted(true);
                cargarProductos("");
                tablaProductos.getSelectionModel().select(itemArrastrado);
                event.consume();
            }            
        });

        return linea;
    }

    private boolean acceptable(Dragboard db, TreeTableRow<Producto> row) {
        boolean resultado = false;
        if (db.hasContent(SERIALIZED_MIME_TYPE)) {
            int index = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
            if (row.getIndex() != index) {
                TreeItem target = getTarget(row);
                TreeItem itemArrastrado = tablaProductos.getTreeItem(index);
                resultado = !esPadre(itemArrastrado, target);
            }
        }
        return resultado;
    }

    private TreeItem getTarget(TreeTableRow<Producto> row) {
        TreeItem target = tablaProductos.getRoot();
        if (!row.isEmpty()) {
            target = row.getTreeItem();
        }
        return target;
    }

    // evita un bucle en el arbol al soltar sobre su padre
    private boolean esPadre(TreeItem padre, TreeItem hijo) {
        boolean resultado = false;
        while (!resultado && hijo != null) {
            resultado = hijo.getParent() == padre;
            hijo = hijo.getParent();
        }
        return resultado;
    }

    @FXML private void agregarPrecio(ActionEvent event) {
        String nuevoTamanio = txtTamanio.getText();
        Double nuevoPrecio = Double.valueOf(txtPrecio.getText());
        actualizarPrecio(nuevoTamanio, nuevoPrecio);
    }
    private void actualizarPrecio(String nuevoTamanio, Double nuevoPrecio){
        Producto prod = tablaProductos.getSelectionModel().getSelectedItem().getValue();
        prod.agregarPrecio(nuevoTamanio, nuevoPrecio);
        ObservableList<Map.Entry<String, Double>> precios = FXCollections.observableArrayList(prod.getPrecioPorTamanio().entrySet());
        tablaPrecios.setItems(precios);
        txtPrecio.setText("");
        txtTamanio.setText("");
    }
    @FXML private void buscar(KeyEvent event) {
        cargarProductos(buscador.getText());
    }

    @FXML private void verOrdenes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("OrdenesView.fxml"));
        Parent root = loader.load();
        Stage stage=(Stage) buscador.getScene().getWindow();
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.getIcons().add(new Image("/resources/color/001_56.png"));
        stage.setTitle("eOrders - Órdenes - XEVEN");
        stage.show();
    }
}
