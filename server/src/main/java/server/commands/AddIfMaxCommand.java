package server.commands;

import com.sun.rowset.internal.Row;
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
        return " {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }
}
