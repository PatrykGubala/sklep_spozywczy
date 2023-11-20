package com.example.sklep;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_wyloguj;
    @FXML
    private Label label_zaloguj;
    @FXML
    private Label label_hello_name_surname;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_wyloguj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                DBUtils.changeScene(event,"login.fxml","Zaloguj",null,null);
            }
        });
    }


    public void setNameAndSurname(String name, String surname)
    {
        label_hello_name_surname.setText(name+" "+surname);

    }
}
