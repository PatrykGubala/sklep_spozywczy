package com.example.sklep.model;

import com.example.sklep.utilities.AlertHelper;
import com.example.sklep.view.CurrentWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class DBUtils {
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
            e.printStackTrace();
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

    public static void signUpUser( String login, String password, String name, String surname, Boolean isAdmin) {

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

                psInsert = connection.prepareStatement("INSERT INTO uzytkownik (id, login, haslo, imie, nazwisko, admin) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setInt(1, newId);
                psInsert.setString(2, login);
                psInsert.setString(3, password);
                psInsert.setString(4, name);
                psInsert.setString(5, surname);
                psInsert.setBoolean(6, isAdmin);
                psInsert.executeUpdate();

                SessionManager.getInstance().setLoggedInUser(new User(login, name,surname,password,isAdmin));
                if(isAdmin) {
                    SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.ADMIN);
                }
                else {
                    SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.SELLER);
                }

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

    public static void logInUser(String login, String password) {
        PasswordHasher passwordHasher = new PasswordHasher();
        Connection connection = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );
            psSelect = connection.prepareStatement("select haslo,imie,nazwisko, admin from uzytkownik where login =?");
            psSelect.setString(1, login);
            resultSet = psSelect.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "User doesn't exist", "");

            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("haslo");
                    String name = resultSet.getString("imie");
                    String surname = resultSet.getString("nazwisko");
                    boolean isAdmin = resultSet.getBoolean("admin");

                    if (passwordHasher.matches(password, retrievedPassword)) {

                        SessionManager.getInstance().setLoggedInUser(new User(login, name,surname,password,isAdmin));
                        if(isAdmin) {
                            SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.ADMIN);
                        }
                        else {
                            SessionManager.getInstance().getViewFactory().getCurrentWindowProperty().set(CurrentWindow.SELLER);
                        }

                    }
                    else{
                        AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "Wrong password", "");
                    }

                }
            }
        } catch (ClassNotFoundException e ) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "ClassNotFound", "");
        } catch ( SQLException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error", "SQL Exception", "");
        }
        finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psSelect != null) psSelect.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ObservableList<Product> getProductListFromDatabase(boolean expired) {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );

            statement = connection.createStatement();
            String query;
            if (expired) {
                query = "SELECT * FROM produkty WHERE data_ważności <= CURDATE()";
            } else {
                query = "SELECT * FROM produkty WHERE data_ważności > CURDATE()";
            }
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String productName = resultSet.getString("nazwa_produktu");
                LocalDate expirationDate = resultSet.getDate("data_ważności").toLocalDate();
                String category = resultSet.getString("kategoria");
                int quantity = resultSet.getInt("ilość");

                Product product = new Product(productName, expirationDate, category, quantity);
                productList.add(product);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
        }

        return productList;
    }

    public static void deleteProduct(Product product) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );

            String query = "DELETE FROM produkty WHERE nazwa_produktu = ? AND data_ważności = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setDate(2, java.sql.Date.valueOf(product.getExpirationDate()));
            preparedStatement.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addProduct(Product product) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );

            psUpdate = connection.prepareStatement("UPDATE produkty SET ilość = ilość + ? WHERE nazwa_produktu = ? AND data_ważności = ?");
            psUpdate.setInt(1, product.getQuantity());
            psUpdate.setString(2, product.getProductName());
            psUpdate.setDate(3, Date.valueOf(product.getExpirationDate()));

            int updatedRows = psUpdate.executeUpdate();

            if (updatedRows == 0) {
                int nextProductId = getNextId("produkty", "id_produktu");
                psInsert = connection.prepareStatement("INSERT INTO produkty (id_produktu, nazwa_produktu, data_ważności, kategoria, ilość) VALUES (?, ?, ?, ?, ?)");
                psInsert.setInt(1, nextProductId);
                psInsert.setString(2, product.getProductName());
                psInsert.setDate(3, Date.valueOf(product.getExpirationDate()));
                psInsert.setString(4, product.getCategory());
                psInsert.setInt(5, product.getQuantity());
                psInsert.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Handle exceptions more gracefully in a real application
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psInsert != null) psInsert.close();
                if (psUpdate != null) psUpdate.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
