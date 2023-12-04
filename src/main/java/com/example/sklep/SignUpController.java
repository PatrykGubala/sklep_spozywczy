package com.example.sklep;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button button_sign_up;

    @FXML
    private Button button_go_back;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_surname;
    @FXML
    private TextField tf_login;
    @FXML
    private PasswordField tf_password_first;

    @FXML
    private PasswordField tf_password_second;
    @FXML
    private CheckBox adminCheckBox;

    private PasswordHasher passwordHasher = new PasswordHasher();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tf_password_first.getText().equals(tf_password_second.getText())) {
                    String hashedPassword = passwordHasher.hash(tf_password_first.getText());
                    byte[] salt = passwordHasher.getSalt();
                    DBUtils.signUpUser(event, tf_login.getText(), hashedPassword, salt, tf_name.getText(), tf_surname.getText(), adminCheckBox.isSelected());
                } else {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText(null);
                    alert.setContentText("Hasła są różne");
                    alert.showAndWait();
                }
            }
        });
        button_go_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "Login", null, null);
            }
        });

        }
    }

