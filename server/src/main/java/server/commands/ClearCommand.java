package server.commands;

import general.data.User;
import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import server.connection.DatabaseCollectionManager;

public class ClearCommand implements Commandable{
    CollectionManager collectionManager;
    DatabaseCollectionManager databaseCollectionManager;
    /**
     * Constructor, just set variables for work
     * @param coll Collection Manager
     */
    public ClearCommand(CollectionManager coll, DatabaseCollectionManager db){
        collectionManager = coll;
        databaseCollectionManager = db;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return ": очистить коллекцию";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        databaseCollectionManager.clearCollection(user);
        collectionManager.update();
        ans.AddAnswer("Успешно удалены принадлежащие вам элементы");
        Main.logger.info("Коллекция успешно очищена");
        return true;
    }
}
