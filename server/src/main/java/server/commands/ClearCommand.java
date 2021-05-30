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
        return ": \u043E\u0447\u0438\u0441\u0442\u0438\u0442\u044C \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044E";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        databaseCollectionManager.clearCollection(user);
        collectionManager.update();
        ans.AddAnswer("\u0423\u0441\u043F\u0435\u0448\u043D\u043E \u0443\u0434\u0430\u043B\u0435\u043D\u044B \u043F\u0440\u0438\u043D\u0430\u0434\u043B\u0435\u0436\u0430\u0449\u0438\u0435 \u0432\u0430\u043C \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u044B");
        Main.logger.info("\u041A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044F \u0443\u0441\u043F\u0435\u0448\u043D\u043E \u043E\u0447\u0438\u0449\u0435\u043D\u0430");
        return true;
    }
}
