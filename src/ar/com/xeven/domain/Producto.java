package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by pacevedo on 5/08/17.
 */
public class Producto {
    private StringProperty prodID;
    private StringProperty nombreProducto;
    private StringProperty detallesProducto;
    private MapProperty<String, Double> precioPorTamanio;
    private ObjectProperty<LocalDate> fechaModificacionPrecio;
    private ListProperty<Producto> accesoriosDisponibles;

    public Producto(String nombreProducto, String detallesProducto, Map<String, Double> precioPorTamanio, LocalDate fechaModificacionPrecio, ObservableList<Producto> accesoriosDisponibles) {
        this.prodID = new SimpleStringProperty(XEVEN.generateID("ePN"));
        this.nombreProducto = new SimpleStringProperty(nombreProducto);
        this.detallesProducto = new SimpleStringProperty(detallesProducto);
        this.precioPorTamanio = new SimpleMapProperty<>(FXCollections.observableMap(precioPorTamanio));
        this.fechaModificacionPrecio = new SimpleObjectProperty<>(fechaModificacionPrecio);
        this.accesoriosDisponibles = new SimpleListProperty<>(accesoriosDisponibles);
    }

    public StringProperty getProdID() {
        return prodID;
    }

    public StringProperty getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(StringProperty nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public StringProperty getDetallesProducto() {
        return detallesProducto;
    }

    public void setDetallesProducto(StringProperty detallesProducto) {
        this.detallesProducto = detallesProducto;
    }

    public MapProperty<String, Double> getPrecioPorTamanio() {
        return precioPorTamanio;
    }

    public void setPrecioPorTamanio(MapProperty<String, Double> precioPorTamanio) {
        this.precioPorTamanio = precioPorTamanio;
    }

    public ObjectProperty<LocalDate> getFechaModificacionPrecio() {
        return fechaModificacionPrecio;
    }

    public void setFechaModificacionPrecio(ObjectProperty<LocalDate> fechaModificacionPrecio) {
        this.fechaModificacionPrecio = fechaModificacionPrecio;
    }

    public ListProperty<Producto> getAccesoriosDisponibles() {
        return accesoriosDisponibles;
    }

    public void setAccesoriosDisponibles(ListProperty<Producto> accesoriosDisponibles) {
        this.accesoriosDisponibles = accesoriosDisponibles;
    }
}