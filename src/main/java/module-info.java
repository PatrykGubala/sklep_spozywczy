module com.example.sklep {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sklep to javafx.fxml;
    exports com.example.sklep;
}