package com.example.sklep.controller;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class OrderController {

    @FXML
    private TextField productNameField;

    @FXML
    private DatePicker expirationDatePicker;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void handleAddProduct() {
        String productName = productNameField.getText();
        LocalDate expirationDate = expirationDatePicker.getValue();
        String category = categoryField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        Product product = new Product(productName, expirationDate, category, quantity);
        DBUtils.addProduct(product);

        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}