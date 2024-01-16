package com.example.sklep;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.Product;
import com.example.sklep.model.SessionManager;
import javafx.collections.ObservableList;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DBUtilsTest {
    private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    @BeforeEach
    public void setUp() {
        DBUtils.setDatabaseConfiguration(JDBC_URL, "", "");

        try (Connection connection = DBUtils.getConnection();
             InputStreamReader reader = new InputStreamReader(DBUtilsTest.class.getResourceAsStream("/h2database.sql"))) {
            RunScript.execute(connection, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        try (Connection connection = DBUtils.getConnection();
             InputStreamReader reader = new InputStreamReader(DBUtilsTest.class.getResourceAsStream("/h2database.sql"))) {
            RunScript.execute(connection, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DBUtils.setDatabaseConfiguration("jdbc:mysql://localhost:3306/sklep", "root", "");


    }
    @Test
    public void testDatabaseConnection() {
        try {
            Connection connection = DBUtils.getConnection();

            assertNotNull(connection, "Connection is null");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error during database connection");
        }
    }




    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = new Random().nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }
}