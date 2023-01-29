package cz.vse.nulltracker.nulltracker.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static cz.vse.nulltracker.nulltracker.database.DatabaseHandler.database;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggedUserTest {

    private void loginFramework (String login, String pass) {

        MongoCollection<Document> collection = database.getCollection("users");

        Bson filter = Filters.eq("login", login);


        Document entry = collection.find(filter).first();

        if (entry == null) {
        } else {
            if (!Objects.equals(entry.getString("password"), pass)) {
            } else {
                LoggedUser.saveUserData(entry.getString("name"), entry.getString("login"), entry.getObjectId("_id"));
            }

        }
    }

    @Test
    protected void unsuccessfulLoginTest () {
        //Incorrect login
        String login = "obviouslyIncorrectLogin";
        String pass = "787878";

        loginFramework(login,pass);

        assertEquals(null,LoggedUser.LUname);
        assertEquals(null,LoggedUser.LUemail);
        assertEquals(null,LoggedUser.LUID);

        //correctLogin
        //incorrectPassword
        login = "test";
        pass = "787878";

        loginFramework(login,pass);

        assertEquals(null,LoggedUser.LUname);
        assertEquals(null,LoggedUser.LUemail);
        assertEquals(null,LoggedUser.LUID);


        //Incorrect Login
        //Correct password
        login = "testa";
        pass = "1234";

        loginFramework(login,pass);

        assertEquals(null,LoggedUser.LUname);
        assertEquals(null,LoggedUser.LUemail);
        assertEquals(null,LoggedUser.LUID);

        //typo in login name2
        //correct pass
        login = "michal@vse.cy";
        pass = "Heslo1";

        loginFramework(login,pass);

        assertEquals(null,LoggedUser.LUname);
        assertEquals(null,LoggedUser.LUemail);
        assertEquals(null,LoggedUser.LUID);

    }


}