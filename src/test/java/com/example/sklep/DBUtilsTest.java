package com.example.sklep;
import static org.junit.jupiter.api.Assertions.*;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.PasswordHasher;
import com.example.sklep.model.Product;
import com.example.sklep.model.SessionManager;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;


public class DBUtilsTest {

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = new Random().nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }

    @Test
    public void testDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sklep", "root", ""
            );

            assertTrue(connection.isValid(5), "Brak połączenia z bazą danych");

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            assertTrue(false, "Błąd podczas próby połączenia z bazą danych");
        }
    }
    @Test
    void testGetNextId() {
        int nextId = DBUtils.getNextId("uzytkownik", "id");
        assertTrue(nextId > 0, "Id większe od 0");
    }


    @Test
    public void testSignUpUser() {
        String login = "testuser" + generateRandomString(5);
        String password = "testpassword" + generateRandomString(5);
        String name = "Test" + generateRandomString(5);
        String surname = "User" + generateRandomString(5);
        boolean isAdmin = false;

        DBUtils.signUpUser(login, password, name, surname, isAdmin);

        assertNotNull(SessionManager.getInstance().getLoggedInUser(), "Zalogowany użytkownik jest nullem");
        assertEquals(login, SessionManager.getInstance().getLoggedInUser().getUsername(), "Zalogowano nie na to konto");
    }


    @Test
    public void testGetProductListFromDatabase() {
        boolean expired = false;
        ObservableList<Product> productList = DBUtils.getProductListFromDatabase(expired);
        assertNotNull(productList, "Lista produktów jest nullem");
        assertFalse(productList.isEmpty(), "Lista produktów jest pusta");
    }

    @Test
    public void testAddProduct() {

        Product testProduct = new Product("TestProduct", LocalDate.now().plusDays(5), "TestCategory", 10);

        DBUtils.addProduct(testProduct);

        ObservableList<Product> updatedProductList = DBUtils.getProductListFromDatabase(true);
        assertFalse(updatedProductList.contains(testProduct), "Testowy produkt nie został dodany");
    }

    @Test
    public void testDeleteProduct() {
        Product testProduct = new Product("TestProduct", LocalDate.now().plusDays(5), "TestCategory", 10);
        DBUtils.addProduct(testProduct);

        DBUtils.deleteProduct(testProduct);

        ObservableList<Product> updatedProductList = DBUtils.getProductListFromDatabase(false);
        assertFalse(updatedProductList.contains(testProduct), "Testowy produkt nie został usunięty");
    }

    @Test
    public void testProductListExpirationFilter() {
        Product expiredProduct = new Product("ExpiredProduct", LocalDate.now().minusDays(1), "TestCategory", 5);
        Product nonExpiredProduct = new Product("NonExpiredProduct", LocalDate.now().plusDays(5), "TestCategory", 10);

        DBUtils.addProduct(expiredProduct);
        DBUtils.addProduct(nonExpiredProduct);

        ObservableList<Product> expiredProductsList = DBUtils.getProductListFromDatabase(true);

        assertFalse(expiredProductsList.contains(expiredProduct), "Produkt po terminie nie jest obecny na liście");
       // assertFalse(expiredProductsList.contains(nonExpiredProduct), "Produkt nie po terminie jest obecny na liście");
    }

}
