package cz.vse.nulltracker.nulltracker.core;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationTest {


    private Document registrationAbstraction (String name, String login, String pass, String secondPass) {
        try {
            MongoCollection<Document> userCollection = database.getCollection("users");

            Bson filter = Filters.eq("login", login);

            Document entry = userCollection.find(filter).first();

            if (entry != null) {
                System.out.println("Existujici zaznam");
                return null;
            }

            if (!login.matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                System.out.println("Chyba v loginu");
                return null;
            }

            if (!Objects.equals(pass, secondPass)) {
                System.out.println("hesla se neshoduji");
                return null;
            }

            if (!isPassSafe(pass)) {
                System.out.println("Slabe heslo");
                return null;
            }


            Document document = new Document("login", login)
                    .append("password", pass).append("name", name);

            userCollection.insertOne(document);
            return document;

        } catch (Exception e) {
            System.out.println("DB error" + e);
            return null;
        }
    }


    protected boolean isPassSafe (String passToCheck) {

        boolean hasDigit = false;
        boolean hasLetter = false;

        if (passToCheck.length() <= 4) return false;

        for (char c : passToCheck.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
        }

        if (!hasDigit) return false;

        for (char c : passToCheck.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
        }

        return hasLetter;
    }

    @Test
    public void pwCheck() {
        RegistrationController rgc = new RegistrationController();
        assertFalse(rgc.isPassSafe("hereispassword")); //does not contain number
        assertFalse(rgc.isPassSafe("123456789")); // does not contain letter
        assertFalse(rgc.isPassSafe("pa1")); //does not contain enough characters
        assertTrue(rgc.isPassSafe("hereisstrongpassword4204520"));
    }

    @Test
    public void validInputA(){

        boolean exists = false;
        MongoCollection<Document> userCollection = database.getCollection("users");
        Document entry;

        String name;
        String login;
        String pass;
        String secondPass;

        //test set A
        //Correct format
        name = "Tomio Okamura";
        login = "iluvbabis@spd.cz";
        pass = "morava123";
        secondPass = "morava123";

        entry = registrationAbstraction(name,login,pass,secondPass);

        if (entry != null) exists = true;

        assertTrue(exists);
        userCollection.deleteOne(Filters.eq("_id", entry.getObjectId("_id")));
    }

    @Test
    public void validInputB(){

        boolean exists = false;
        MongoCollection<Document> userCollection = database.getCollection("users");
        Document entry;

        String name;
        String login;
        String pass;
        String secondPass;

        //test set B
        //Correct format
        name = "Jarda Bašta";
        login = "iluvdemocracykekw@spd.cz";
        pass = "morava123";
        secondPass = "morava123";

        entry = registrationAbstraction(name,login,pass,secondPass);

        if (entry != null) exists = true;

        assertTrue(exists);
        userCollection.deleteOne(Filters.eq("_id", entry.getObjectId("_id")));
    }

    @Test
    public void incorrectInputExistingA () {

        boolean exists = false;
        MongoCollection<Document> userCollection = database.getCollection("users");
        Document entry;

        String name;
        String login;
        String pass;
        String secondPass;


        //test set C
        //Correct format
        name = "test";
        login = "test";
        pass = "5555";
        secondPass = "5555";

        entry = registrationAbstraction(name,login,pass,secondPass);
        if (entry != null) {
            exists = true;
            userCollection.deleteOne(Filters.eq("_id", entry.getObjectId("_id")));
        }
        assertFalse(exists);

    }

}