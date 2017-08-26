package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.Map;


/**
 * Model class for a line of detail.
 *
 * @author Pablo Acevedo
 */
public class LineaDetalle {
    private StringProperty lineID;
    private ObjectProperty<Producto> producto;
    private StringProperty pickedSize;
    private DoubleProperty priceBySize;
    private MapProperty<LineaDetalle,Double> accessories; // a line of detail for each accessory + its subtotal
    private DoubleProperty subtotal; // priceBySize + (sum of subtotal of accessories)
    private IntegerProperty quantity;

    public LineaDetalle(Producto producto, String pickedSize, Double priceBySize, Map<LineaDetalle, Double> accessories, Integer quantity) {
        this.lineID = new SimpleStringProperty(XEVEN.generateID("eDET"));
        this.producto = new SimpleObjectProperty<>(producto);
        this.pickedSize = new SimpleStringProperty(pickedSize);
        this.priceBySize = new SimpleDoubleProperty(priceBySize);
        this.accessories = new SimpleMapProperty<>(FXCollections.observableMap(accessories));
        this.quantity = new SimpleIntegerProperty(quantity);
        this.subtotal = new SimpleDoubleProperty(getTotal());
    }

    public Double getTotal(){
        Double total = this.priceBySize.getValue() * this.quantity.getValue();
        for(Double subtotal: accessories.values())
           total += subtotal;
        return total;
    }
    public String getLineID() {
        return lineID.get();
    }

    public StringProperty lineIDProperty() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID.set(lineID);
    }

    public Producto getProducto() {
        return producto.get();
    }

    public ObjectProperty<Producto> productProperty() {
        return producto;
    }

    public void setProduct(Producto producto) {
        this.producto.set(producto);
    }

    public String getPickedSize() {
        return pickedSize.get();
    }

    public StringProperty pickedSizeProperty() {
        return pickedSize;
    }

    public void setPickedSize(String pickedSize) {
        this.pickedSize.set(pickedSize);
    }

    public double getPriceBySize() {
        return priceBySize.get();
    }

    public DoubleProperty priceBySizeProperty() {
        return priceBySize;
    }

    public void setPriceBySize(double priceBySize) {
        this.priceBySize.set(priceBySize);
    }

    public ObservableMap<LineaDetalle, Double> getAccessories() {
        return accessories.get();
    }

    public MapProperty<LineaDetalle, Double> accessoriesProperty() {
        return accessories;
    }

    public void setAccessories(ObservableMap<LineaDetalle, Double> accessories) {
        this.accessories.set(accessories);
    }

    public double getSubtotal() {
        return subtotal.get();
    }

    public DoubleProperty subtotalProperty() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal.set(subtotal);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }
}
