package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import cz.vse.nulltracker.nulltracker.database.DatabaseHandler;
import org.bson.Document;
import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;

import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationControllerTest {


    DatabaseHandler handler = new DatabaseHandler();
    MongoCollection<Document> coll = database.getCollection("users");

    RegistrationController rgc = new RegistrationController();

    @BeforeEach
    public void setUp() {

    }


    @Test
    public void pwCheck() {

        assertFalse(rgc.isPassSafe("hereispassword")); //does not contain number
        assertFalse(rgc.isPassSafe("123456789")); // does not contain letter
        assertFalse(rgc.isPassSafe("pa1")); //does not contain enough characters
        assertTrue(rgc.isPassSafe("hereisstrongpassword4204520"));
    }


    @Test
    public void validInput(){
        rgc.emailInput.setText("testmail@mail.lol");
        rgc.nameInput.setText("testuser");


        rgc.attemptRegistration();







    }
}