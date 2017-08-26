/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.xeven;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author educacionit
 */
public class OrdenesViewController implements Initializable {
    
    @FXML
    private TableView<?> orderTable;
    @FXML
    private TableColumn<?, ?> orderIDCol;
    @FXML
    private TableColumn<?, ?> contactNameCol;
    @FXML
    private TableColumn<?, ?> contactPhoneCol;
    @FXML
    private TableColumn<?, ?> deliveryDateCol;
    @FXML
    private TableColumn<?, ?> statusCol;
    @FXML
    private Label orderID;
    @FXML
    private TextField contactName;
    @FXML
    private DatePicker deliveryDate;
    @FXML
    private TextField contactPhone;
    @FXML
    private Label totalAmount;
    @FXML
    private TextField paidAmount;
    @FXML
    private Label pendingAmount;
    @FXML
    private TextArea additionalDetails;
    @FXML
    private ComboBox<?> status;
    @FXML
    private Button btn_saveChanges;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btn_saveChanges_pressed(ActionEvent event) {
        System.out.println("Guardando...");
    }
    
}
