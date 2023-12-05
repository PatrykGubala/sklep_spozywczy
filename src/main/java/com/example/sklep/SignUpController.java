package com.example.sklep;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;
import net.synedra.validatorfx.Validator;

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

    private Validator validator = new Validator();

    private PasswordHasher passwordHasher = new PasswordHasher();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatorCheck(tf_name, "^[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż]+$");
        validatorCheck(tf_surname, "^[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż]+$");
        validatorCheck(tf_login, "^(.{2,})+$");
        validatorCheck(tf_password_first, "^(.*[0-9].*).+$");

        passwordValidator(tf_password_first,tf_password_second,"^(.*[0-9].*).+$");

        button_sign_up.disableProperty().bind(validator.containsErrorsProperty());



        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tf_password_first.getText().equals(tf_password_second.getText())) {
                    String hashedPassword = passwordHasher.hash(tf_password_first.getText());
                    DBUtils.signUpUser(event, tf_login.getText(), hashedPassword, "salt", tf_name.getText(), tf_surname.getText(), adminCheckBox.isSelected());
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



    private void validatorCheck(TextField field, String regex) {
        this.validator.createCheck()
                .dependsOn("field", field.textProperty())
                .withMethod(c -> {
                    String field1 = c.get("field");
                    if (!field1.matches(regex)) {
                        c.error("Podaj polskie znaki");
                    }
                })
                .decorates(field)
                .immediate();


    }
    private void passwordValidator(PasswordField field, PasswordField field2,String regex) {
        this.validator.createCheck()
                .dependsOn("field", field.textProperty()).dependsOn("field2", field2.textProperty())
                .withMethod(c -> {
                    String text1 = c.get("field");
                    String text2 = c.get("field2");
                    if (!(text1.matches(regex) && text1.equals(text2))) {
                        c.error("Hasło ma posiadac przynajmniej jedną cyfrę");
                    }
                })
                .decorates(field2)
                .immediate();


    }
}



