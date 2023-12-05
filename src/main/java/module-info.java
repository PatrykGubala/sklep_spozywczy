module com.example.sklep {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.mkammerer.argon2.nolibs;
    requires net.synedra.validatorfx;


    opens com.example.sklep to javafx.fxml;
    exports com.example.sklep;
}