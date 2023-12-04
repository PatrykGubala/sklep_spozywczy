package com.example.sklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Base64;

public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String name, String surname) {
        Parent root = null;

        if (name != null && surname != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setNameAndSurname(name, surname);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static int getNextId(String tableName, String idColumnName) {
        int nextId = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );

            statement = connection.createStatement();
            String query = "SELECT MAX(" + idColumnName + ") FROM " + tableName;
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int maxId = resultSet.getInt(1);
                nextId = maxId + 1;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Handle exceptions more gracefully in a real application
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nextId;
    }

    public static void signUpUser(ActionEvent event, String login, String password, byte[] salt, String name, String surname, Boolean isAdmin) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psIsTaken = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );

            int lastId = getNextId("uzytkownik", "id");

            psIsTaken = connection.prepareStatement("SELECT * FROM uzytkownik WHERE login = ?");
            psIsTaken.setString(1, login);
            resultSet = psIsTaken.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("ERROR USER EXISTS");
            } else {
                int newId = lastId;

                psInsert = connection.prepareStatement("INSERT INTO uzytkownik (id, login, haslo, sol, imie, nazwisko, admin) VALUES (?, ?, ?, ?, ?, ?, ?)");
                psInsert.setInt(1, newId);
                psInsert.setString(2, login);
                psInsert.setString(3, password);
                psInsert.setString(4, Base64.getEncoder().encodeToString(salt));
                psInsert.setString(5, name);
                psInsert.setString(6, surname);
                psInsert.setBoolean(7, isAdmin);
                psInsert.executeUpdate();

                changeScene(event, "logged-in.fxml", "Zalogowano", name, surname);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psInsert != null) psInsert.close();
                if (psIsTaken != null) psIsTaken.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logInUser(ActionEvent event, String login, String password, PasswordHasher passwordHasher) {
        Connection connection = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );
            psSelect = connection.prepareStatement("select haslo,sol,imie,nazwisko, admin from uzytkownik where login =?");
            psSelect.setString(1, login);
            resultSet = psSelect.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("ERROR USER NOT EXIST");
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("haslo");
                    byte[] retrievedSalt = Base64.getDecoder().decode(resultSet.getString("sol"));
                    String name = resultSet.getString("imie");
                    String surname = resultSet.getString("nazwisko");
                    boolean admin = resultSet.getBoolean("admin");
                    if (passwordHasher.matches(password, retrievedPassword, retrievedSalt)) {

                        if(admin) {
                            changeScene(event, "logged-in-admin.fxml", "Zalogowano", name, surname);
                        }
                        else changeScene(event, "logged-in.fxml", "Zalogowano", name, surname);
                    } else {
                        System.out.println("WRONG PASSWORD");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psSelect != null) psSelect.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
