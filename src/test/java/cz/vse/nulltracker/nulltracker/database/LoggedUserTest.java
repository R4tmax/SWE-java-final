package cz.vse.nulltracker.nulltracker.database;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggedUserTest {
    public static String LUname;
    public static String LUemail;
    public static ObjectId LUID;
    private LoggedUser uzivatel;

    @BeforeEach
    public void setUp()
    {
        uzivatel = new LoggedUser();
        LUID = new ObjectId();


    }
    @Test
    void saveUserData() {
        LUname = "name";
        LUemail = "mail";
        uzivatel.setLUname(LUname);
        uzivatel.setLUemail(LUemail);
        uzivatel.setLUID(LUID);


        assertEquals(true,LUname.equals("name"));
        assertEquals(true,LUemail.equals("mail"));
        assertEquals(true, uzivatel.getLUID().equals(LUID));
    }
}