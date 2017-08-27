package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.Map;
import javafx.collections.ObservableList;


/**
 * Model class for a line of detail.
 *
 * @author Pablo Acevedo
 */
public class LineaDetalle {
    private StringProperty idLinea;
    private ObjectProperty<Producto> producto;
    private StringProperty tamanioElegido;
    private DoubleProperty precioUnitario;
    private IntegerProperty cantidad;
    private ListProperty<LineaDetalle> accesorios; // una linea de detalle por accesorio
    private DoubleProperty subtotal; // precioUnitario + (subtotal de cada accesorio)

    public LineaDetalle(Producto producto, String pickedSize, Double priceBySize, List<LineaDetalle> accesorios, Integer quantity) {
        this.idLinea = new SimpleStringProperty(XEVEN.generateID("eDET"));
        this.producto = new SimpleObjectProperty<>(producto);
        this.tamanioElegido = new SimpleStringProperty(pickedSize);
        this.precioUnitario = new SimpleDoubleProperty(priceBySize);
        this.accesorios = new SimpleListProperty<>(FXCollections.observableList(accesorios));
        this.cantidad = new SimpleIntegerProperty(quantity);
        this.subtotal = new SimpleDoubleProperty(getSubtotal());
    }

    public Double getSubtotal(){
        Double total = this.precioUnitario.getValue() * this.cantidad.getValue();
        for(LineaDetalle accesorio: accesorios)
           total += accesorio.getSubtotal();
        return total;
    }
    
    public DoubleProperty subtotalProperty() {
        return subtotal;
    }

    public String getIdLinea() {
        return idLinea.get();
    }

    public StringProperty idLineaProperty() {
        return idLinea;
    }

    public void setIdLinea(String lineID) {
        this.idLinea.set(lineID);
    }

    public Producto getProducto() {
        return producto.get();
    }

    public ObjectProperty<Producto> productoProperty() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto.set(producto);
    }

    public String getTamanioElegido() {
        return tamanioElegido.get();
    }

    public StringProperty tamanioElegidoProperty() {
        return tamanioElegido;
    }

    public void setTamanioElegido(String tamanioElegido) {
        this.tamanioElegido.set(tamanioElegido);
    }

    public double getPrecioUnitario() {
        return precioUnitario.get();
    }

    public DoubleProperty precioUnitarioProperty() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario.set(precioUnitario);
    }

    public ObservableList<LineaDetalle> getAccesorios() {
        return accesorios.get();
    }

    public ListProperty<LineaDetalle> accesoriosProperty() {
        return accesorios;
    }

    public void setAccesorios(ObservableList<LineaDetalle> accesorios) {
        this.accesorios.set(accesorios);
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }

    public void setCantidad(int quantity) {
        this.cantidad.set(quantity);
    }
}