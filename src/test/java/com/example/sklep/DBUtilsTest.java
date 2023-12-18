package com.example.sklep;
import static org.junit.jupiter.api.Assertions.*;

import com.example.sklep.model.DBUtils;
import com.example.sklep.model.PasswordHasher;
import org.junit.jupiter.api.Test;
import javafx.event.ActionEvent;


public class DBUtilsTest {
    @Test
    void testGetNextId() {
        int nextId = DBUtils.getNextId("uzytkownik", "id");
        assertTrue(nextId > 0, "Next ID should be greater than 0");
    }
    /*
    @Test
    void testLogInUser() {
        assertDoesNotThrow(() -> DBUtils.logInUser(new ActionEvent(), "test_user", "test_password", new PasswordHasher()));
    }


     */
}
