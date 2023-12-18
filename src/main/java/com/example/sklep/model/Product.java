package com.example.sklep.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;

public class Product {
    private final String productName;
    private final LocalDate expirationDate;
    private final String category;
    private final Integer quantity;

    public Product(String productName, LocalDate expirationDate, String category, int quantity) {
        this.productName = productName;
        this.expirationDate = expirationDate;
        this.category = category;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public LocalDate expirationDateProperty() {
        return expirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer quantityProperty() {
        return quantity;
    }
}
