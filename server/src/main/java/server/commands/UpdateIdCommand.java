package server.commands;

import general.data.RowStudyGroup;
import general.data.StudyGroup;
import general.exeptions.EmptyIOException;
import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import general.data.User;
import server.connection.DatabaseCollectionManager;

public class UpdateIdCommand implements Commandable{
    CollectionManager collectionManager;
    DatabaseCollectionManager databaseCollectionManager;
    /**
     * Constructor, just set variables for work
     * @param col Collection Manager
     */
    public UpdateIdCommand(CollectionManager col, DatabaseCollectionManager db){
        collectionManager = col;
        databaseCollectionManager = db;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return " id {element} : \u043E\u0431\u043D\u043E\u0432\u0438\u0442\u044C \u0437\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u0430 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u0438, id \u043A\u043E\u0442\u043E\u0440\u043E\u0433\u043E \u0440\u0430\u0432\u0435\u043D \u0437\u0430\u0434\u0430\u043D\u043D\u043E\u043C\u0443";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {

        Integer id;
        try{
            if (arg.trim().equals(""))
                throw new EmptyIOException();
            id = Integer.parseInt(arg.trim());
        }
        catch (NumberFormatException e){
            ans.AddErrorMsg("ID \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            Main.logger.error("\u041E\u0448\u0438\u0431\u043A\u0430 \u0432\u0432\u043E\u0434\u0430: ID \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            return false;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 ID");
            Main.logger.error("\u041E\u0448\u0438\u0431\u043A\u0430 \u0432\u0432\u043E\u0434\u0430: \u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 ID");
            return false;
        }
        if (!collectionManager.checkID(id)) {
            ans.AddErrorMsg("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0433\u043E ID");
            Main.logger.error("\u041E\u0448\u0438\u0431\u043A\u0430 \u0432\u0432\u043E\u0434\u0430: \u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0433\u043E ID");
            return false;
        }
        RowStudyGroup sg = (RowStudyGroup) obArg;
        if (!user.getUsername().equals(collectionManager.getById(id).getUser().getUsername())){
            ans.AddErrorMsg("\u041D\u0435\u0442 \u0440\u0430\u0437\u0440\u0435\u0448\u0435\u043D\u0438\u044F \u043D\u0430 \u0440\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u0435, \u0442\u0430\u043A \u043A\u0430\u043A \u0432\u043B\u0430\u0434\u0435\u043B\u0435\u0446: " + collectionManager.getById(id).getUser().getUsername());
            Main.logger.error("\u0423 \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0435\u043B\u044F " + user.getUsername() + " \u043D\u0435\u0442 \u043F\u0440\u0430\u0432 \u0440\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u0442\u044C \u044D\u0442\u043E\u0442 \u044D\u043B\u0435\u043C\u0435\u043D\u0442.");
            return false;
        }
        databaseCollectionManager.updateStudyGroupById(id, sg);
        ans.AddAnswer("\u0423\u0441\u043F\u0435\u0448\u043D\u043E \u0438\u0437\u043C\u0435\u043D\u0435\u043D\u043E");
        collectionManager.update();
        return true;
    }
}
