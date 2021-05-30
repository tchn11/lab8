package server.commands;

import general.data.RowStudyGroup;
import general.data.StudyGroup;
import general.data.User;
import messages.AnswerMsg;
import server.collection.CollectionManager;
import server.connection.DatabaseCollectionManager;

public class AddIfMaxCommand implements Commandable{
    CollectionManager collectionManager;
    DatabaseCollectionManager databaseCollectionManager;
    /**
     * Constructor, just set variables for work
     * @param coll Collection Manager
     */
    public AddIfMaxCommand(CollectionManager coll, DatabaseCollectionManager db){
        collectionManager = coll;
        databaseCollectionManager = db;
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        RowStudyGroup rowStudyGroup = (RowStudyGroup) obArg;
        StudyGroup sg = databaseCollectionManager.insertStudyGroup(rowStudyGroup, user);
        collectionManager.update();
        return true;
    }

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getDescription() {
        return " {element} : \u0434\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u043D\u043E\u0432\u044B\u0439 \u044D\u043B\u0435\u043C\u0435\u043D\u0442 \u0432 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044E, \u0435\u0441\u043B\u0438 \u0435\u0433\u043E \u0437\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u0440\u0435\u0432\u044B\u0448\u0430\u0435\u0442 \u0437\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043D\u0430\u0438\u0431\u043E\u043B\u044C\u0448\u0435\u0433\u043E \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u0430 \u044D\u0442\u043E\u0439 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u0438";
    }
}
