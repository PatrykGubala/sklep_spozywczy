package com.example.sklep;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable
{
    @FXML
    private Button button_sign_up;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_surname;
    @FXML
    private TextField tf_login;
    @FXML
    private TextField tf_password_first;
    @FXML
    private TextField tf_password_second;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                DBUtils.signUpUser(event,tf_login.getText(),tf_password_first.getText(),tf_name.getText(),tf_surname.getText());
            }
        });
    }
}
