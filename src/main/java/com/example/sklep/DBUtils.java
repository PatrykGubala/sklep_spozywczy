package com.example.sklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String name, String surname)
    {
        Parent root = null;

        if(name!=null && surname!= null)
            try
            {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setNameAndSurname(name, surname);
            }
            catch(IOException e)
            {
                e.printStackTrace();

            }
        else
        {
            try
            {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }
            catch(IOException e)
            {
                e.printStackTrace();

            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,600,400));
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



    public static void signUpUser(ActionEvent event, String login, String password, String name, String surname) {
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

                int newId = lastId + 1;

                psInsert = connection.prepareStatement("INSERT INTO uzytkownik (id, login, haslo, imie, nazwisko, admin) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setInt(1, newId);
                psInsert.setString(2, login);
                psInsert.setString(3, password);
                psInsert.setString(4, name);
                psInsert.setString(5, surname);
                psInsert.setBoolean(6, true);
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

    public static void logInUser(ActionEvent event, String login, String password)
    {
        Connection connection = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;


        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );
            psSelect = connection.prepareStatement("select haslo from uzytkownik where login = ?");
            psSelect.setString(1,login);
            resultSet = psSelect.executeQuery();

            if (!resultSet.isBeforeFirst())
            {
                System.out.println("ERROR USER NOT EXIST");
            }
            else
            {
                while(resultSet.next())
                {
                    String retrivedPassword = resultSet.getString("haslo");
                    if(retrivedPassword.equals(password))
                    {
                        changeScene(event,"logged-in.fxml","Zalogowano",login,retrivedPassword);
                    }
                    else
                    {
                        System.out.println("WRONG PASSWORD");
                    }
                }

            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            if (psSelect != null) {
                try {
                    psSelect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
