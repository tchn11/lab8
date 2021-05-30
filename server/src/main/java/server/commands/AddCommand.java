package server.commands;

import general.data.RowStudyGroup;
import general.data.StudyGroup;
import general.data.User;
import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import server.connection.DatabaseCollectionManager;

/**
 * Command 'add'. Adds new element to collection
 */
public class AddCommand implements Commandable{
    CollectionManager collectionManager;
    DatabaseCollectionManager databaseCollectionManager;

    /**
     * Constructor, just set variables for work
     * @param col Collection Manager
     */
    public AddCommand(CollectionManager col, DatabaseCollectionManager db){
        collectionManager = col;
        databaseCollectionManager = db;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return " {element} : \u0434\u043E\u0431\u0430\u0432\u0438\u0442\u044C \u043D\u043E\u0432\u044B\u0439 \u044D\u043B\u0435\u043C\u0435\u043D\u0442 \u0432 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044E";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        RowStudyGroup sg = (RowStudyGroup) obArg;
        StudyGroup studyGroup = databaseCollectionManager.insertStudyGroup(sg, user);
        collectionManager.update();
        Main.logger.info("\u0414\u043E\u0431\u0430\u0432\u043B\u0435\u043D \u043D\u043E\u0432\u044B\u0439 \u044D\u043B\u0435\u043C\u0435\u043D\u0442 \u0432 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044E: " + studyGroup.toString());
        ans.AddAnswer("\u0423\u0441\u043F\u0435\u0448\u043D\u043E \u0434\u043E\u0431\u0430\u0432\u043B\u0435\u043D \u044D\u043B\u0435\u043C\u0435\u043D\u0442 \u0432 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044E:\n" + studyGroup.toString());
        return true;
    }
}
