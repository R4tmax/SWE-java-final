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
    String mainErrorMessage = null; //can save error message here


    private Document registrationAbstraction (String name, String login, String pass, String secondPass) {
        try {
            MongoCollection<Document> userCollection = database.getCollection("users");

            Bson filter = Filters.eq("login", login);

            Document entry = userCollection.find(filter).first();

            if (entry != null) {
                System.out.println("Existujici záznam"); //probably not needed
                mainErrorMessage = "Existujici záznam";
                return null;
            }

            if (!login.matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                System.out.println("nesprávný formát emailu");
                mainErrorMessage = "nesprávný formát emailu";
                return null;
            }

            if (!Objects.equals(pass, secondPass)) {
                System.out.println("hesla se neshodují");
                mainErrorMessage = "hesla se neshodují";
                return null;
            }

            if (!isPassSafe(pass)) {
                System.out.println("Slabé heslo");
                mainErrorMessage = "Slabé heslo";
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
        name = "Bloody Mary";
        login = "MaryBlod@blood.bl";
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
        name = "Ardnold schwarzenegger";
        login = "schwarzenegger@comewithmeifyouwanttolive.cz";
        pass = "terminatornumber1";
        secondPass = "terminatornumber1";

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
        assertEquals("Existujici záznam", mainErrorMessage);

    }

    @Test
    public void incorrectInputWrongEmailFormat () {

        boolean exists = false;
        MongoCollection<Document> userCollection = database.getCollection("users");
        Document entry;

        String name;
        String login;
        String pass;
        String secondPass;


        //test set C
        //Correct format
        name = "James Bond";
        login = "MyNamesIsBond";
        pass = "5555";
        secondPass = "5555";

        entry = registrationAbstraction(name,login,pass,secondPass);
        if (entry != null) {
            exists = true;
            userCollection.deleteOne(Filters.eq("_id", entry.getObjectId("_id")));
        }
        assertFalse(exists);
        assertEquals("nesprávný formát emailu", mainErrorMessage);

    }


    @Test
    public void incorrectInputErorrMatchingPassword () {

        boolean exists = false;
        MongoCollection<Document> userCollection = database.getCollection("users");
        Document entry;

        String name;
        String login;
        String pass;
        String secondPass;


        //test set C
        //Correct format
        name = "Jake Sully";
        login = "sully@Pandora.galaxy";
        pass = "smurf123";
        secondPass = "5555";

        entry = registrationAbstraction(name,login,pass,secondPass);
        if (entry != null) {
            exists = true;
            userCollection.deleteOne(Filters.eq("_id", entry.getObjectId("_id")));
        }
        assertFalse(exists);
        assertEquals("hesla se neshodují", mainErrorMessage);

    }


    @Test
    public void incorrectInputWeakPassword () {

        boolean exists = false;
        MongoCollection<Document> userCollection = database.getCollection("users");
        Document entry;

        String name;
        String login;
        String pass;
        String secondPass;


        //test set C
        //Correct format
        name = "Puss in Boots";
        login = "whereisShrek6@shrek.com";
        pass = "whatareyoudoinginmyswamp";
        secondPass = "whatareyoudoinginmyswamp";

        entry = registrationAbstraction(name,login,pass,secondPass);
        if (entry != null) {
            exists = true;
            userCollection.deleteOne(Filters.eq("_id", entry.getObjectId("_id")));
        }
        assertFalse(exists);
        assertEquals("Slabé heslo", mainErrorMessage);

    }




}