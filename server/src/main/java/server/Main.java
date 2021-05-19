package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.collection.CollectionManager;
import server.commands.*;
import server.connection.DatabaseCollectionManager;
import server.connection.DatabaseManager;
import server.connection.DatabaseUserManager;
import server.file.FileManager;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Main class for server
 * @author Konanykhina Antonina
 */
public class Main {
    public static Logger logger = LogManager.getLogger("ServerLogger");
    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int MAX_CONNECTIONS = 3;
    public static final String DATABASE_HOST = "jdbc:postgresql://pg:5432/studs";
    public static final String DATABASE_USER = "s314929";
    public static final String DATABASE_PASS = "3o32";

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "windows-1251"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Code error");
        }

        DatabaseManager databaseManager = new DatabaseManager(DATABASE_HOST, DATABASE_USER, DATABASE_PASS);

        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseManager);

        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseManager, databaseUserManager);

        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);

        CommandManager commandManager = new CommandManager(collectionManager, new Commandable[]{
                new ClearCommand(collectionManager, databaseCollectionManager), new AddCommand(collectionManager, databaseCollectionManager),
                new ShowCommand(collectionManager), new InfoCommand(collectionManager),
                new UpdateIdCommand(collectionManager, databaseCollectionManager), new RemoveByIdCommand(collectionManager, databaseCollectionManager),
                new RemoveByIndexCommand(collectionManager, databaseCollectionManager), new AddIfMaxCommand(collectionManager, databaseCollectionManager),
                new RemoveAllByStudentsCountCommand(collectionManager),
                new FilterStartsWithNameCommand(collectionManager),
                new FilterLessThenExpelledCommand(collectionManager)});
        Server server = new Server(PORT, CONNECTION_TIMEOUT, commandManager, MAX_CONNECTIONS, databaseUserManager);

        server.run();
    }

}
