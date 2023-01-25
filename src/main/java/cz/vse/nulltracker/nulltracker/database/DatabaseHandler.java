package cz.vse.nulltracker.nulltracker.database;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * @author Martin Kadlec
 * @version Last refactor on 25.1
 *
 * <p>Facilitator of the MongoDB communication.
 * Predefines and declares DB connection variables as static field acessible from anywhere in the code.
 * The structure is:
 * ConenctionString - holds the URI of the underlying DB, it holds params with name of the connection and necessary password
 * and as such basically functions as a API key
 * MongoClient - opens the communication tunnel between the app and the DB
 * MongoDatabse - configures the connection to only interact with the required tablespace
 * <p>
 * Take note that while the connection is universal the targeted collection might not be.
 * Be sure to specify required collection prior to preforming CRUD operations.
 * <p>
 * Further references are on the project wiki.
 * </p>
 *
 */
public class DatabaseHandler {
    public static final ConnectionString connectionString = new ConnectionString("mongodb+srv://martin_dev:wahB7g4jjP2CCJ7@nulltrackerdev.nxwgnwc.mongodb.net/?retryWrites=true&w=majority");
    public static final MongoClient mongoClient = MongoClients.create(connectionString);
    public static final MongoDatabase database = mongoClient.getDatabase("NullTracerkerDevDB");



}
