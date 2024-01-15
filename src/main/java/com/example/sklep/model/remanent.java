package com.example.sklep.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class remanent {
    private final String productName;
    private final Integer quantity;

    public remanent(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;

    }
}
