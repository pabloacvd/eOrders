package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableMap;

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
    private boolean soloAccesorio;

    public Producto(Integer prodID, String nombreProducto, String detallesProducto, LinkedHashMap<String, Double> precioPorTamanio, LocalDate fechaModificacionPrecio, ObservableList<Producto> accesoriosDisponibles) {
        this.prodID = new SimpleStringProperty(prodID.toString());
        this.nombreProducto = new SimpleStringProperty(nombreProducto);
        this.detallesProducto = new SimpleStringProperty(detallesProducto);
        this.precioPorTamanio = new SimpleMapProperty<>(FXCollections.observableMap(precioPorTamanio));
        this.fechaModificacionPrecio = new SimpleObjectProperty<>(fechaModificacionPrecio);
        this.accesoriosDisponibles = new SimpleListProperty<>(accesoriosDisponibles);
    }
    
    public static ObservableList<Producto> getProductos(String xql) {
        ObservableList<Producto> lstProductos = FXCollections.observableArrayList();
        String query = "SELECT * FROM productos WHERE soloAccesorio!=1"; 
        if(!xql.isEmpty())
            query += " AND (nombreProducto LIKE '%"+xql+"%' OR detallesProducto LIKE '%"+xql+"%');"; 
        Connection c = XEVEN.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next() ){
                LinkedHashMap<String,Double> precioPorTamanio = new Gson().fromJson(rs.getString("precioPorTamanio"), new TypeToken<LinkedHashMap<String, Double>>(){}.getType());
                ObservableList<Producto> accesorios = loadAccesoriosDisponibles(c, rs.getInt("prodID"));
                Producto unProducto = new Producto(
                        rs.getInt("prodID"),
                        rs.getString("nombreProducto"), 
                        rs.getString("detallesProducto"), precioPorTamanio, 
                        rs.getDate("fechaModificacionPrecio").toLocalDate(),
                        accesorios);
                unProducto.setSoloAccesorio(rs.getBoolean("soloAccesorio"));
                lstProductos.add(unProducto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{    rs.close();     } catch (SQLException e){}
            try{    stmt.close();   } catch (SQLException e){}       
        }
        return lstProductos;       
    }
    
    public void actualizarPrecios(ObservableMap<String, Double> precios){
        String sql = "UPDATE productos SET fechaModificacionPrecio=?, precioPorTamanio=? WHERE prodID=?;";
        Connection c = XEVEN.getConnection();
        try {
            PreparedStatement pstmt = c.prepareStatement(sql); 
            String mapaJson = new Gson().toJson(precios, new TypeToken<LinkedHashMap<String, Double>>(){}.getType());
            pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setString(2, mapaJson);
            pstmt.setInt(3, Integer.valueOf(this.prodID.get()));
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.precioPorTamanio = new SimpleMapProperty<>(FXCollections.observableMap(precios));
    }

    public StringProperty getProdID() {
        return prodID;
    }
    public void setProdID(int prodID) {
        this.prodID.set(String.valueOf(prodID));
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
        return this.accesoriosDisponibles;
    }
    
    public void agregarAccesorio(Producto accesorio){
        String sql = "INSERT INTO accesoriosPorProducto (prodID, accesorio) VALUES (?, ?);";
        System.out.println("SQL:"+sql);
        PreparedStatement stmt;
        Connection c = XEVEN.getConnection();
        Integer id = Integer.valueOf(this.prodID.get());
        try{
            stmt = c.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setInt(2, Integer.valueOf(accesorio.getProdID().get()));
            if(stmt.executeUpdate()<0)
                throw new SQLException();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.accesoriosDisponibles = new SimpleListProperty<>(loadAccesoriosDisponibles(c, id));
    }
    
    public static ObservableList<Producto> loadAccesoriosDisponibles(Connection c, int elID){
        ObservableList<Producto> lstAccesorios = FXCollections.observableArrayList();
        // este query es el join que devuelve los accesorios de un producto dado
        String query = "SELECT O.* FROM `productos` AS P INNER JOIN accesoriosPorProducto as A ON A.prodID = P.prodID INNER JOIN productos AS O ON A.accesorio = O.prodID WHERE P.prodID = ?";
        PreparedStatement stmtConsulta;
        ResultSet rs = null;
        try {
            stmtConsulta = c.prepareStatement(query);
            stmtConsulta.setInt(1, elID);
            rs = stmtConsulta.executeQuery();
            while(rs.next()){
                LinkedHashMap<String,Double> precioPorTamanioDelAccesorio = new Gson().fromJson(rs.getString("precioPorTamanio"), new TypeToken<LinkedHashMap<String, Double>>(){}.getType());
                ObservableList<Producto> accesoriosDelAccesorio = loadAccesoriosDisponibles(c, rs.getInt("prodID"));
                Producto unProducto = new Producto(
                        rs.getInt("prodID"),
                        rs.getString("nombreProducto"), 
                        rs.getString("detallesProducto"), precioPorTamanioDelAccesorio, 
                        rs.getDate("fechaModificacionPrecio").toLocalDate(),
                        accesoriosDelAccesorio);
                unProducto.setSoloAccesorio(rs.getBoolean("soloAccesorio"));
                lstAccesorios.add(unProducto);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lstAccesorios;
    }
    public boolean eliminarAccesorio(int parentID){
        boolean resultado = true;
        Connection laConexion = XEVEN.getConnection();
        String sql= "DELETE FROM accesoriosPorProducto WHERE prodID=? AND accesorio=?";
        PreparedStatement stmt = null;
        System.out.println("El sql:"+sql+"_"+parentID);
        try{
           stmt = laConexion.prepareStatement(sql);
           stmt.setInt(1, parentID);
           stmt.setInt(2, Integer.valueOf(this.prodID.get()));
           if(stmt.executeUpdate() < 0)
               resultado = false;
        } catch (SQLException ex) {
            resultado = false;
        } finally {
            try{    stmt.close();   } catch (SQLException e){}       
            try{    laConexion.close();   } catch (SQLException e){}       
        }
        return resultado;
    }
    public void setAccesoriosDisponibles(ListProperty<Producto> accesoriosDisponibles) {
        this.accesoriosDisponibles = accesoriosDisponibles;
    }
    public boolean isSoloAccesorio() {
        return soloAccesorio;
    }
    public void setSoloAccesorio(boolean soloAccesorio) {
        this.soloAccesorio = soloAccesorio;
    }
    public void guardar(){
        
    }
    @Override
    public String toString() {
        return "Producto{" + "prodID=" + prodID.get() + ", nombreProducto=" + nombreProducto.get() + '}';
    }   
    public void agregarPrecio(String nuevoTamanio, Double nuevoPrecio) {
        ObservableMap<String,Double> mapa = precioPorTamanio.get();
        mapa.put(nuevoTamanio, nuevoPrecio);
        actualizarPrecios(mapa);
    }

    public void eliminarPrecio(String key) {
        ObservableMap<String,Double> mapa = precioPorTamanio.get();
        mapa.remove(key);
        actualizarPrecios(mapa);       
    }
}