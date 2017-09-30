package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Model class for a line of detail.
 *
 * @author Pablo Acevedo
 */
public class LineaDetalle {
    private StringProperty lineaID;
    private ObjectProperty<Producto> producto;
    private StringProperty tamanioElegido;
    private DoubleProperty precioUnitario;
    private IntegerProperty cantidad;
    private ListProperty<LineaDetalle> accesorios; // una linea de detalle por accesorio
    private DoubleProperty subtotal; // precioUnitario * cantidad
    private DoubleProperty total; // precioUnitario + (subtotal de cada accesorio)

    public LineaDetalle(Producto producto, String tamanioElegido, Double precioUnitario, List<LineaDetalle> accesorios, Integer cantidad) {
        this.producto = new SimpleObjectProperty<>(producto);
        this.tamanioElegido = new SimpleStringProperty(tamanioElegido);
        this.precioUnitario = new SimpleDoubleProperty(precioUnitario);
        this.accesorios = new SimpleListProperty<>(FXCollections.observableArrayList(accesorios));
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.subtotal = new SimpleDoubleProperty(getSubtotal());
        this.total = new SimpleDoubleProperty(getTotal());
        // INSERT INTO lineaDetalle...
    }
    public LineaDetalle(Integer lineaID, Integer prodID, String tamanioElegido, Double precioUnitario, Integer cantidad, Double subtotal, Double total, Integer idOrden){
        this.lineaID = new SimpleStringProperty(String.valueOf(lineaID));
        this.producto = new SimpleObjectProperty<>(new Producto(prodID));
        this.tamanioElegido = new SimpleStringProperty(tamanioElegido);
        this.precioUnitario = new SimpleDoubleProperty(precioUnitario);
        this.accesorios = new SimpleListProperty<>(getLineasDetalle(idOrden, lineaID));
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.subtotal = new SimpleDoubleProperty(subtotal);
        this.total = new SimpleDoubleProperty(getTotal());
    }
    public static ObservableList<LineaDetalle> getLineasDetalle(Integer idOrden, Integer lineaID) {
        ObservableList<LineaDetalle> lstLineasDetalle = FXCollections.observableArrayList();
        String query = "SELECT * FROM lineasDetalle as L WHERE L.idOrden="+idOrden;
        if(lineaID > 0)
            query+=" AND L.lineaID IN (SELECT A.accesorioID FROM accesoriosPorLineaDetalle AS A WHERE A.accesorioID=L.lineaID AND A.lineaID="+lineaID+")";            
        else
            query+=" AND L.lineaID NOT IN (SELECT A.accesorioID FROM accesoriosPorLineaDetalle AS A WHERE A.accesorioID=L.lineaID)";
        Connection c = XEVEN.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next())
                lstLineasDetalle.add(new LineaDetalle(
                        rs.getInt("lineaID"),
                        rs.getInt("prodID"),
                        rs.getString("tamanioElegido"),
                        rs.getDouble("precioUnitario"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal"),
                        rs.getDouble("total"),
                        rs.getInt("idOrden")
                ));
        } catch (SQLException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{    rs.close();     } catch (SQLException e){}
            try{    stmt.close();   } catch (SQLException e){}       
        }
        return lstLineasDetalle;
    }

    public final Double getSubtotal(){
        return this.precioUnitario.getValue() * this.cantidad.getValue();
    }
    public void setSubtotal(Double subtotal){
        this.subtotal.set(subtotal);
    }
    public DoubleProperty subtotalProperty() { return subtotal; }
    
    public final Double getTotal(){
        Double total = this.getSubtotal();
        total = accesorios.stream().map(accesorio -> accesorio.getTotal()).reduce(total, (accumulator, _item) -> accumulator + _item);
        return total;
    }
    public void setTotal(Double total){
        this.total.set(total);
    }
    public DoubleProperty totalProperty() { return total; }
    
    public String getIdLinea() {
        return lineaID.get();
    }

    public StringProperty idLineaProperty() {
        return lineaID;
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
