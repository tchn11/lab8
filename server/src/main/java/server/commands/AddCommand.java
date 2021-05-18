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
        return " {element} : добавить новый элемент в коллекцию";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        RowStudyGroup sg = (RowStudyGroup) obArg;
        StudyGroup studyGroup = databaseCollectionManager.insertStudyGroup(sg, user);
        collectionManager.update();
        Main.logger.info("Добавлен новый элемент в коллекцию: " + studyGroup.toString());
        ans.AddAnswer("Успешно добавлен элемент в коллекцию:\n" + studyGroup.toString());
        return true;
    }
}
