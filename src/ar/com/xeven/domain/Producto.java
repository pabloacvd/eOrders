package ar.com.xeven.domain;

import ar.com.xeven.utils.XEVEN;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by pacevedo on 5/08/17.
 */
public class Producto {
    private StringProperty prodID;
    private StringProperty productName;
    private StringProperty productDetails;
    private MapProperty<String, Double> priceBySize;
    private ObjectProperty<LocalDate> priceLastChangedDate;
    private ListProperty<Producto> availableAccessories;

    public Producto(String productName, String productDetails, Map<String, Double> priceBySize, LocalDate priceLastChangedDate, ObservableList<Producto> availableAccessories) {
        this.prodID = new SimpleStringProperty(XEVEN.generateID("ePN"));
        this.productName = new SimpleStringProperty(productName);
        this.productDetails = new SimpleStringProperty(productDetails);
        this.priceBySize = new SimpleMapProperty<>(FXCollections.observableMap(priceBySize));
        this.priceLastChangedDate = new SimpleObjectProperty<>(priceLastChangedDate);
        this.availableAccessories = new SimpleListProperty<>(availableAccessories);
    }
    public String getProdID() {
        return prodID.get();
    }

    public StringProperty prodIDProperty() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID.set(prodID);
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getProductDetails() {
        return productDetails.get();
    }

    public StringProperty productDetailsProperty() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails.set(productDetails);
    }

    public ObservableMap<String, Double> getPriceBySize() {
        return priceBySize.get();
    }

    public MapProperty<String, Double> priceBySizeProperty() {
        return priceBySize;
    }

    public void setPriceBySize(ObservableMap<String, Double> priceBySize) {
        this.priceBySize.set(priceBySize);
    }

    public LocalDate getPriceLastChangedDate() {
        return priceLastChangedDate.get();
    }

    public ObjectProperty<LocalDate> priceLastChangedDateProperty() {
        return priceLastChangedDate;
    }

    public void setPriceLastChangedDate(LocalDate priceLastChangedDate) {
        this.priceLastChangedDate.set(priceLastChangedDate);
    }

    public ObservableList<Producto> getAvailableAccessories() {
        return availableAccessories.get();
    }

    public ListProperty<Producto> availableAccessoriesProperty() {
        return availableAccessories;
    }

    public void setAvailableAccessories(ObservableList<Producto> availableAccessories) {
        this.availableAccessories.set(availableAccessories);
    }
}