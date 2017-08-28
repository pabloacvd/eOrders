package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Clase modelo para Ordenes de compra
 * @author Pablo Acevedo
 */
public class Orden implements Serializable{
    private StringProperty idOrden;
    private StringProperty nombreContacto;
    private StringProperty telefonoContacto;
    private StringProperty detallesAdicionales;
    private ObjectProperty<LocalDate> fechaEntrega;
    private StringProperty detallesEntrega;
    private ListProperty<LineaDetalle> lineasDetalle;
    private DoubleProperty descuento; // valor de descuento aplicado sobre la orden
    private DoubleProperty montoAbonado; // TODO si mayor a cero -> status = confirmado (actualizar automaticamente)
    private StringProperty status;// TODO status = {nuevo, confirmado, preparado, entregado, cancelado}
    //TODO if status=entregado -> montoAbonado = montoTotal

    public Orden(String nombreContacto, String telefonoContacto, String detallesAdicionales, LocalDate fechaEntrega, String detallesEntrega, ObservableList<LineaDetalle> lineasDetalle, Double montoAbonado, Double descuento, String status) {
        this.idOrden = new SimpleStringProperty(XEVEN.generateID("eOR"));
        this.nombreContacto = new SimpleStringProperty(nombreContacto);
        this.telefonoContacto = new SimpleStringProperty(telefonoContacto);
        this.detallesAdicionales = new SimpleStringProperty(detallesAdicionales);
        this.fechaEntrega = new SimpleObjectProperty<>(fechaEntrega);
        this.detallesEntrega = new SimpleStringProperty(detallesEntrega);
        this.lineasDetalle = new SimpleListProperty<>(lineasDetalle);
        this.montoAbonado = new SimpleDoubleProperty(montoAbonado);
        this.descuento = new SimpleDoubleProperty(descuento);
        this.status = new SimpleStringProperty(status);
    }

    public Double getTotal(){
        Double total = 0.00;
        for(LineaDetalle linea: lineasDetalle)
            total += linea.getSubtotal();
        return total;
    }

    public Double getMontoPendiente(){
        return this.getTotal() - this.montoAbonado.getValue();
    }

    public StringProperty getIdOrden() {
        return idOrden;
    }

    public StringProperty getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(StringProperty nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public StringProperty getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(StringProperty telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public StringProperty getDetallesAdicionales() {
        return detallesAdicionales;
    }

    public void setDetallesAdicionales(StringProperty detallesAdicionales) {
        this.detallesAdicionales = detallesAdicionales;
    }

    public ObjectProperty<LocalDate> getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(ObjectProperty<LocalDate> fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public StringProperty getDetallesEntrega() {
        return detallesEntrega;
    }

    public void setDetallesEntrega(StringProperty detallesEntrega) {
        this.detallesEntrega = detallesEntrega;
    }

    public ListProperty<LineaDetalle> getLineasDetalle() { return lineasDetalle; }
    public void setLineasDetalle(ListProperty<LineaDetalle> lineasDetalle) { this.lineasDetalle = lineasDetalle; }

    public Double getMontoAbonado() { return montoAbonado.get(); }
    public void setMontoAbonado(Double montoAbonado) { this.montoAbonado.set(montoAbonado); }
    public DoubleProperty montoAbonadoProperty() { return montoAbonado; }

    public Double getDescuento() { return descuento.get(); }
    public void setDescuento(Double descuento) { this.descuento.set(descuento); }
    public DoubleProperty descuentoProperty() { return descuento; }
    
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }    
    public StringProperty statusProperty(){ return status; }
}