package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import static java.sql.JDBCType.BLOB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Types.BLOB;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public Producto(String nombreProducto, String detallesProducto, Map<String, Double> precioPorTamanio, LocalDate fechaModificacionPrecio, ObservableList<Producto> accesoriosDisponibles) {
        this.prodID = new SimpleStringProperty(XEVEN.generateID("ePN"));
        this.nombreProducto = new SimpleStringProperty(nombreProducto);
        this.detallesProducto = new SimpleStringProperty(detallesProducto);
        this.precioPorTamanio = new SimpleMapProperty<>(FXCollections.observableMap(precioPorTamanio));
        this.fechaModificacionPrecio = new SimpleObjectProperty<>(fechaModificacionPrecio);
        this.accesoriosDisponibles = new SimpleListProperty<>(accesoriosDisponibles);
    }
    
    public static ObservableList<Producto> getProductos(Connection c){
        ObservableList<Producto> lstProductos = FXCollections.observableArrayList();
        String query = "SELECT * FROM productos WHERE soloAccesorio!=1;";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            save(c);
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next() ){
                LinkedHashMap<String,Double> precioPorTamanio = new Gson().fromJson(rs.getString("precioPorTamanio"), new TypeToken<LinkedHashMap<String, Double>>(){}.getType());
                ObservableList<Producto> accesoriosDisponibles = loadAccesoriosDisponibles(c, rs.getInt("prodID"));
                Producto unProducto = new Producto(rs.getString("nombreProducto"), 
                        rs.getString("detallesProducto"), precioPorTamanio, 
                        rs.getDate("fechaModificacionPrecio").toLocalDate(),
                        accesoriosDisponibles);
                unProducto.setProdID(rs.getInt("prodID"));
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
    
    public static void save(Connection c) throws SQLException{
        String WRITE_OBJECT_SQL = "UPDATE `productos` SET `precioPorTamanio` = ?;";
        PreparedStatement pstmt = c.prepareStatement(WRITE_OBJECT_SQL); 
        /*Map<String, Double> map = new HashMap<String, Double>();{{
            put("Promo", 0.0);
            put("Chico", 10.0);
            put("Medio", 22.0);
            put("Grande", 35.50);
        }};       */
        LinkedHashMap<String, Double> map = new LinkedHashMap();
        map.put("Promo",0.0);
        map.put("Chico",10.0);
        map.put("Medio",22.0);
        map.put("Grande",35.4);

        String json = new Gson().toJson(map, new TypeToken<LinkedHashMap<String, Double>>(){}.getType());
        System.out.println("el json ahora es:"+json);
        pstmt.setString(1, json);
        pstmt.executeUpdate();
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
                Producto unProducto = new Producto(rs.getString("nombreProducto"), 
                        rs.getString("detallesProducto"), precioPorTamanioDelAccesorio, 
                        rs.getDate("fechaModificacionPrecio").toLocalDate(),
                        accesoriosDelAccesorio);
                unProducto.setProdID(rs.getInt("prodID"));
                unProducto.setSoloAccesorio(rs.getBoolean("soloAccesorio"));
                lstAccesorios.add(unProducto);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lstAccesorios;
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
    public void eliminar(){
        
    }
}