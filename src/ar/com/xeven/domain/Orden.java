package ar.com.xeven.domain;

import ar.com.xeven.domain.LineaDetalle;
import ar.com.xeven.utils.XEVEN;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Model class for Purchase orders
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
    private DoubleProperty montoAbonado; // TODO si mayor a cero -> status = confirmado (actualizar automaticamente)
    private StringProperty status;// TODO status = {nuevo, confirmado, preparado, entregado, cancelado}
    //TODO if status=entregado -> montoAbonado = montoTotal

    public Orden(String contactName, String contactPhone, String additionalDetails, LocalDate deliveryDate, String deliveryDetails, ObservableList<LineaDetalle> detailsLines, Double paidAmount, String status) {
        this.idOrden = new SimpleStringProperty(XEVEN.generateID("eOR"));
        this.nombreContacto = new SimpleStringProperty(contactName);
        this.telefonoContacto = new SimpleStringProperty(contactPhone);
        this.detallesAdicionales = new SimpleStringProperty(additionalDetails);
        this.fechaEntrega = new SimpleObjectProperty<>(deliveryDate);
        this.detallesEntrega = new SimpleStringProperty(deliveryDetails);
        this.lineasDetalle = new SimpleListProperty<>(detailsLines);
        this.montoAbonado = new SimpleDoubleProperty(paidAmount);
        this.status = new SimpleStringProperty(status);
    }

    public Double getTotal(){
        Double total = 0.00;
        for(LineaDetalle linea: lineasDetalle)
            total += linea.getTotal();
        return total;
    }

    public Double getMontoPendiente(){
        return this.getTotal() - this.montoAbonado.getValue();
    }

    public StringProperty getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(StringProperty idOrden) {
        this.idOrden = idOrden;
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

    public ListProperty<LineaDetalle> getLineasDetalle() {
        return lineasDetalle;
    }

    public void setLineasDetalle(ListProperty<LineaDetalle> lineasDetalle) {
        this.lineasDetalle = lineasDetalle;
    }

    public DoubleProperty getMontoAbonado() {
        return montoAbonado;
    }

    public void setMontoAbonado(DoubleProperty montoAbonado) {
        this.montoAbonado = montoAbonado;
    }

    public StringProperty getStatus() {
        return status;
    }

    public void setStatus(StringProperty status) {
        this.status = status;
    }

}

