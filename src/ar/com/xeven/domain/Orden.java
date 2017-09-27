package ar.com.xeven.domain;

import static ar.com.xeven.domain.Producto.loadAccesoriosDisponibles;
import ar.com.xeven.utils.XEVEN;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public Orden(String idOrden, String nombreContacto, String telefonoContacto, String detallesAdicionales, LocalDate fechaEntrega, String detallesEntrega, ObservableList<LineaDetalle> lineasDetalle, Double montoAbonado, Double descuento, String status) {
        this.idOrden = new SimpleStringProperty(idOrden);
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
    public Orden(String idOrden, String nombreContacto){
        this.idOrden = new SimpleStringProperty(idOrden);
        this.nombreContacto = new SimpleStringProperty(nombreContacto);
    }
    public Orden(String nombreContacto, String telefonoContacto, String detallesAdicionales, LocalDate fechaEntrega
            , String detallesEntrega, Double montoAbonado, Double descuento, String status) {
        String sql = "INSERT INTO ordenes VALUES (NULL,?,?,?,?,?,NULL,?,?,?)";
        Connection c = XEVEN.getConnection();
        ResultSet rs = null;
        Integer idOrden = 0;
        try {
            PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, nombreContacto);
            pstmt.setString(2, telefonoContacto);
            pstmt.setString(3, detallesAdicionales);
            pstmt.setDate(4, Date.valueOf(fechaEntrega));
            pstmt.setString(5, detallesEntrega);
            pstmt.setDouble(6, montoAbonado);
            pstmt.setDouble(7, descuento);
            pstmt.setString(8, status);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            while(rs.next())
                idOrden = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.idOrden = new SimpleStringProperty(idOrden.toString());
        this.nombreContacto = new SimpleStringProperty(nombreContacto);
        this.telefonoContacto = new SimpleStringProperty(telefonoContacto);
        this.detallesAdicionales = new SimpleStringProperty(detallesAdicionales);
        this.fechaEntrega = new SimpleObjectProperty<>(fechaEntrega);
        this.detallesEntrega = new SimpleStringProperty(detallesEntrega);
        this.lineasDetalle = new SimpleListProperty<>();
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
    public void setIdOrden(int ordenID) {
        idOrden.set(String.valueOf(ordenID));
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
    
    private static ObservableList<LineaDetalle> loadLineasDetalle(Connection c, String lineasDetalle) {
        // lineasDetalle es un array de IDs o un stream
        ObservableList<LineaDetalle> lstLineasDetalle = FXCollections.observableArrayList();
        
        // query db usando la connection "c" -> no cerrarla!
        

        LinkedHashMap<String, Double> precioPorTamanio = new LinkedHashMap<String, Double>(){{
            put("Chico", 10.0);
            put("Medio", 22.20);
            put("Grande", 35.50);
            put("Promo", 0.0);
        }};
        ObservableList<Producto> accesorios = FXCollections.observableArrayList();
        
        Producto accesorio1 = new Producto(1, "Bandeja","Bandeja artesanal",
                    precioPorTamanio, LocalDate.now(),accesorios);
        
        accesorios.add(accesorio1);
        
        Producto producto1 = new Producto(2, "Picada XEVEN","Con jamon y otros fiambres.",
                    precioPorTamanio, LocalDate.now(),accesorios);
        
        ObservableList<LineaDetalle> accesoriosSeleccionados = FXCollections.observableArrayList();
        LineaDetalle lineaAccesorio = new LineaDetalle(accesorio1, "Grande", precioPorTamanio.get("Grande"), new ArrayList<>(), 2);
        accesoriosSeleccionados.add(lineaAccesorio);
        
        LineaDetalle linea1 = new LineaDetalle(producto1, "50 personas",
                precioPorTamanio.get("Chico"), accesoriosSeleccionados, 3);

        lstLineasDetalle.add(linea1);
        return lstLineasDetalle;
    }
    public static ObservableList<Orden> getOrdenes(String xql){
        ObservableList<Orden> lstOrdenes = FXCollections.observableArrayList();
        String query = "SELECT * FROM ordenes"; 
        if(!xql.isEmpty())
            query += " WHERE (nombreProducto LIKE '%"+xql+"%' OR detallesAdicionales LIKE '%"+xql+"%');"; 
        Connection c = XEVEN.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next() ){
                ObservableList<LineaDetalle> lineasDetalle = loadLineasDetalle(c, rs.getString("lineasDetalle"));
                Orden unaOrden = new Orden(String.valueOf(rs.getInt("idOrden")),rs.getString("nombreContacto"));
                unaOrden.detallesAdicionales = new SimpleStringProperty(rs.getString("detallesAdicionales"));
                unaOrden.detallesEntrega = new SimpleStringProperty(rs.getString("detallesEntrega"));
                unaOrden.lineasDetalle = new SimpleListProperty<>(lineasDetalle);
                unaOrden.montoAbonado = new SimpleDoubleProperty(rs.getDouble("montoAbonado"));
                unaOrden.descuento = new SimpleDoubleProperty(rs.getDouble("descuento"));
                unaOrden.status = new SimpleStringProperty(rs.getString("status"));
                unaOrden.telefonoContacto = new SimpleStringProperty(rs.getString("telefonoContacto"));
                if(rs.getDate("fechaEntrega") != null)
                    unaOrden.fechaEntrega = new SimpleObjectProperty<>(rs.getDate("fechaEntrega").toLocalDate());
                else
                    unaOrden.fechaEntrega = new SimpleObjectProperty<>();                    
                lstOrdenes.add(unaOrden);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{    rs.close();     } catch (SQLException e){}
            try{    stmt.close();   } catch (SQLException e){}       
        }
        return lstOrdenes;
    }

    public void guardar() {
        String sql = "UPDATE ordenes SET nombreContacto=?,telefonoContacto=?,detallesAdicionales=?,fechaEntrega=?,detallesEntrega=?,montoAbonado=?,descuento=?,status=? WHERE idOrden=?;";
        Connection c = XEVEN.getConnection();
        try {
            PreparedStatement pstmt = c.prepareStatement(sql); 
            pstmt.setString(1, this.nombreContacto.get());
            pstmt.setString(2, this.telefonoContacto.get());
            pstmt.setString(3, this.detallesAdicionales.get());
            if(this.fechaEntrega!=null)
                pstmt.setDate(4, Date.valueOf(this.fechaEntrega.get()));
            else
                pstmt.setDate(4, null);
            pstmt.setString(5, this.detallesEntrega.get());
            pstmt.setDouble(6, this.montoAbonado.get());
            pstmt.setDouble(7, this.descuento.get());
            pstmt.setString(8, this.status.get());
            pstmt.setInt(9, Integer.valueOf(this.idOrden.get()));
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}