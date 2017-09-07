package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;

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
            total += linea.getTotal();
        total -= this.getDescuento();
        return total;
    }

    public Double getMontoPendiente(){
        return this.getTotal() - this.montoAbonado.getValue();
    }

    public String getIdOrden() {
        return idOrden.get();
    }
    public StringProperty idOrdenProperty(){ return idOrden; }
    

    public String getNombreContacto() {
        return nombreContacto.get();
    }
    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto.set(nombreContacto);
    }
    public StringProperty nombreContactoProperty() {
        return this.nombreContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto.get();
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

    public LocalDate getFechaEntrega() {
        return fechaEntrega.get();
    }

    public void setFechaEntrega(ObjectProperty<LocalDate> fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getDetallesEntrega() {
        return detallesEntrega.get();
    }

    public void setDetallesEntrega(StringProperty detallesEntrega) {
        this.detallesEntrega = detallesEntrega;
    }

    public ObservableList<LineaDetalle> getLineasDetalle() { return lineasDetalle.get(); }
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
    
    public static ObservableList<Orden> getOrdenes(){
        ObservableList<Orden> lstOrdenes = FXCollections.observableArrayList();
        ObservableList<LineaDetalle> lstLineasDetalle = FXCollections.observableArrayList();
        ObservableList<LineaDetalle> lstLineasDetalle2 = FXCollections.observableArrayList();
        
        Map<String, Double> precioPorTamanio = new HashMap<String, Double>(){{
            put("Chico", 10.0);
            put("Medio", 22.20);
            put("Grande", 35.50);
            put("Promo", 0.0);
        }};
        Map<String, Double> precioPorTamanio2 = new HashMap<String, Double>(){{
            put("30 personas", 100.0);
            put("50 personas", 200.50);
            put("Promo", 0.0);
        }};
        // esta es la lista de accesorios
        ObservableList<Producto> accesorios = FXCollections.observableArrayList();
        
        Producto accesorio1 = new Producto("Bandeja","Bandeja artesanal",
                    precioPorTamanio, LocalDate.now(),accesorios);
        
        accesorios.add(accesorio1);
        
        Producto producto1 = new Producto("Picada XEVEN","Con jamon y otros fiambres.",
                    precioPorTamanio2, LocalDate.now(),accesorios);
        
        ObservableList<LineaDetalle> accesoriosSeleccionados = FXCollections.observableArrayList();
        LineaDetalle lineaAccesorio = new LineaDetalle(accesorio1, "Grande", precioPorTamanio.get("Grande"), new ArrayList<>(), 2);
        accesoriosSeleccionados.add(lineaAccesorio);
        
        LineaDetalle linea1 = new LineaDetalle(producto1, "50 personas",
                precioPorTamanio2.get("50 personas"), accesoriosSeleccionados, 3);

        lstLineasDetalle.add(linea1);
        
        LineaDetalle linea2 = new LineaDetalle(producto1, "30 personas", precioPorTamanio2.get("30 personas"), new ArrayList<>(), 10);
        ObservableList<LineaDetalle> accesoriosSeleccionados2 = FXCollections.observableArrayList();
        LineaDetalle lineaAccesorio2 = new LineaDetalle(accesorio1, "Chico", precioPorTamanio.get("Chico"), new ArrayList<>(), 2);
        accesoriosSeleccionados2.add(lineaAccesorio);
        accesoriosSeleccionados2.add(lineaAccesorio2);
        lstLineasDetalle2.add(linea1);
        lstLineasDetalle2.add(linea2);

        Orden orden1 = new Orden("Pablo Acevedo Areco","+5491123456789",
                "Profe JavaSE8", LocalDate.now(),"Retira por local 13hs",
                lstLineasDetalle,10.0, 5.0,"Preparado");

        lstOrdenes.add(orden1);
        
        Orden orden2 = new Orden("Linus Torvalds","+358 91911",
                "GIT and Linux dad", LocalDate.of(2017, Month.JULY, 30),"Entregar por DHL a Finlandia",
                lstLineasDetalle2,1.0, 0.0,"Confirmado");        
        lstOrdenes.add(orden2);
        
        return lstOrdenes;
    }
}