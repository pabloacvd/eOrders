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
import java.sql.Date;
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
                ObservableList<Producto> accesorios = loadAccesoriosDisponibles(c, rs.getInt("prodID"));
                Producto unProducto = new Producto(
                        rs.getInt("prodID"),
                        rs.getString("nombreProducto"), 
                        rs.getString("detallesProducto")
                );
                if(rs.getDate("fechaModificacionPrecio") != null)
                    unProducto.fechaModificacionPrecio = new SimpleObjectProperty<>(rs.getDate("fechaModificacionPrecio").toLocalDate());
                if(rs.getString("precioPorTamanio") !=null){
                    LinkedHashMap<String,Double> precioPorTamanio = new Gson().fromJson(rs.getString("precioPorTamanio"), new TypeToken<LinkedHashMap<String, Double>>(){}.getType());
                    unProducto.precioPorTamanio = new SimpleMapProperty<>(FXCollections.observableMap(precioPorTamanio));
                }
                unProducto.accesoriosDisponibles = new SimpleListProperty<>(accesorios);
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
    public Producto(Integer prodID){
        this.prodID = new SimpleStringProperty(prodID.toString());
        String query= "SELECT * FROM productos WHERE prodID='"+prodID+"'";
        Connection c = XEVEN.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next() ){
                ObservableList<Producto> accesorios = loadAccesoriosDisponibles(c, rs.getInt("prodID"));
                this.nombreProducto = new SimpleStringProperty(rs.getString("nombreProducto"));
                this.detallesProducto = new SimpleStringProperty(rs.getString("detallesProducto"));
                this.accesoriosDisponibles = new SimpleListProperty<>(accesoriosDisponibles);
                if(rs.getDate("fechaModificacionPrecio") != null)
                    this.fechaModificacionPrecio = new SimpleObjectProperty<>(rs.getDate("fechaModificacionPrecio").toLocalDate());
                else
                    this.fechaModificacionPrecio = new SimpleObjectProperty<>();
                if(rs.getString("precioPorTamanio") !=null){
                    LinkedHashMap<String,Double> precioPorTamanio = new Gson().fromJson(rs.getString("precioPorTamanio"), new TypeToken<LinkedHashMap<String, Double>>(){}.getType());
                    this.precioPorTamanio = new SimpleMapProperty<>(FXCollections.observableMap(precioPorTamanio));
                }else
                    this.precioPorTamanio = new SimpleMapProperty<>();
                this.accesoriosDisponibles = new SimpleListProperty<>(accesorios);
                this.setSoloAccesorio(rs.getBoolean("soloAccesorio"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{    rs.close();     } catch (SQLException e){}
            try{    stmt.close();   } catch (SQLException e){}
        }
    }
    public Producto(Integer prodID, String nombreProducto, String detallesProducto) {
        this.prodID = new SimpleStringProperty(prodID.toString());
        this.nombreProducto = new SimpleStringProperty(nombreProducto);
        this.detallesProducto = new SimpleStringProperty(detallesProducto);
        this.precioPorTamanio = new SimpleMapProperty<>();
        this.fechaModificacionPrecio = new SimpleObjectProperty<>();
        this.soloAccesorio = false;
    }
    public Producto(String nombreProducto, String detallesProducto) {
        String sql = "INSERT INTO productos (prodID, nombreProducto, detallesProducto, precioPorTamanio, fechaModificacionPrecio, soloAccesorio) VALUES (NULL, ?, ?, NULL, NULL, '0');";
        Connection c = XEVEN.getConnection();
        ResultSet rs = null;
        Integer prodID = 0;
        try {
            PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, nombreProducto);
            pstmt.setString(2, detallesProducto);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            while(rs.next())
                prodID = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.prodID = new SimpleStringProperty(prodID.toString());
        this.nombreProducto = new SimpleStringProperty(nombreProducto);
        this.detallesProducto = new SimpleStringProperty(detallesProducto);
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
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto.set(nombreProducto);
    }

    public StringProperty getDetallesProducto() {
        return detallesProducto;
    }
    public void setDetallesProducto(StringProperty detallesProducto) {
        this.detallesProducto = detallesProducto;
    }
    public void setDetallesProducto(String detallesProducto) {
        this.detallesProducto.set(detallesProducto);
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
                ObservableList<Producto> accesoriosDelAccesorio = loadAccesoriosDisponibles(c, rs.getInt("prodID"));
                Producto unProducto = new Producto(
                        rs.getInt("prodID"),
                        rs.getString("nombreProducto"), 
                        rs.getString("detallesProducto"));
                if(rs.getDate("fechaModificacionPrecio") != null)
                    unProducto.fechaModificacionPrecio = new SimpleObjectProperty<>(rs.getDate("fechaModificacionPrecio").toLocalDate());
                if(rs.getString("precioPorTamanio") !=null){
                    LinkedHashMap<String,Double> precioPorTamanio = new Gson().fromJson(rs.getString("precioPorTamanio"), new TypeToken<LinkedHashMap<String, Double>>(){}.getType());
                    unProducto.precioPorTamanio = new SimpleMapProperty<>(FXCollections.observableMap(precioPorTamanio));
                }
                unProducto.accesoriosDisponibles = new SimpleListProperty<>(accesoriosDelAccesorio);
                unProducto.setSoloAccesorio(rs.getBoolean("soloAccesorio"));
                lstAccesorios.add(unProducto);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lstAccesorios;
    }
    //elimina un producto de todas las entradas donde es accesorio
    //esto sirve para cuando se elimina un producto principal, para evitar dejar accesorios huerfanos
    public boolean eliminarProductoComoAccesorio(){
        boolean resultado = true;
        Connection laConexion = XEVEN.getConnection();
        String sql= "DELETE FROM accesoriosPorProducto WHERE accesorio=?";
        PreparedStatement stmt = null;
        try{
           stmt = laConexion.prepareStatement(sql);
           stmt.setInt(1, Integer.valueOf(this.prodID.get()));
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
    public boolean eliminarAccesorio(int parentID){
        boolean resultado = true;
        Connection laConexion = XEVEN.getConnection();
        String sql= "DELETE FROM accesoriosPorProducto WHERE prodID=? AND accesorio=?";
        PreparedStatement stmt = null;
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
        String sql = "UPDATE productos SET nombreProducto=?, detallesProducto=?, soloAccesorio=? WHERE prodID=?;";
        Connection c = XEVEN.getConnection();
        try {
            PreparedStatement pstmt = c.prepareStatement(sql); 
            pstmt.setString(1, this.getNombreProducto().get());
            pstmt.setString(2, this.getDetallesProducto().get());
            pstmt.setBoolean(3, this.soloAccesorio);
            pstmt.setString(4, this.getProdID().get());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void eliminar(){
        eliminarProductoComoAccesorio();
        String sql = "DELETE FROM productos WHERE prodID=?;";
        Connection c = XEVEN.getConnection();
        try {
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, this.getProdID().get());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public String toString() {
        return "Producto{" + "prodID=" + prodID.get() + ", nombreProducto=" + nombreProducto.get() + '}';
    }   
    public void agregarPrecio(String nuevoTamanio, Double nuevoPrecio) {
        ObservableMap<String,Double> mapa = precioPorTamanio.get();
        if(mapa==null)
            mapa = FXCollections.observableMap(new LinkedHashMap<String,Double>());
        mapa.put(nuevoTamanio, nuevoPrecio);
        actualizarPrecios(mapa);
    }

    public void eliminarPrecio(String key) {
        ObservableMap<String,Double> mapa = precioPorTamanio.get();
        mapa.remove(key);
        actualizarPrecios(mapa);       
    }
}