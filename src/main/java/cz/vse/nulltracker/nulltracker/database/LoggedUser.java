package cz.vse.nulltracker.nulltracker.database;

import org.bson.types.ObjectId;

/**
 * @author Martin Kadlec
 * @version Last refactor on 25.1
 *
 * <p>
 *     Holds the data of the user who has passed the login.
 *     Makes them public to be accessed by the controllers on demand
 * </p>
 */
public class LoggedUser {
    public static String LUname;
    public static String LUemail;
    public static ObjectId LUID;


    /**
     * Copies the logged data to the static variables
     *
     * @param name Name of the User
     * @param mail Mail of the User
     * @param userId UID of the user within the DB
     */
    public static void saveUserData (String name, String mail, ObjectId userId) {
        LUname = name;
        LUemail = mail;
        LUID = userId;
    }

    public static String getLUname() {
        return LUname;
    }

    public static void setLUname(String LUname) {
        LoggedUser.LUname = LUname;
    }

    public static ObjectId getLUID() {
        return LUID;
    }

    public static void setLUID(ObjectId LUID) {
        LoggedUser.LUID = LUID;
    }

    public static String getLUemail() {
        return LUemail;
    }

    public static void setLUemail(String LUemail) {
        LoggedUser.LUemail = LUemail;
    }
}
