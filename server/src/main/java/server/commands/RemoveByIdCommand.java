package server.commands;

import general.exeptions.EmptyIOException;
import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import general.data.User;
import server.connection.DatabaseCollectionManager;

public class RemoveByIdCommand implements Commandable{
    CollectionManager collectionManager;
    DatabaseCollectionManager databaseCollectionManager;
    /**
     * Constructor, just set variables for work
     * @param col Collection Manager
     */
    public RemoveByIdCommand(CollectionManager col, DatabaseCollectionManager db){
        collectionManager = col;
        databaseCollectionManager = db;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return " id : \u0443\u0434\u0430\u043B\u0438\u0442\u044C \u044D\u043B\u0435\u043C\u0435\u043D\u0442 \u0438\u0437 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u0438 \u043F\u043E \u0435\u0433\u043E id";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        int id;
        try{
            if (arg.trim().equals(""))
                throw new EmptyIOException();
            id = Integer.parseInt(arg.trim());
        }
        catch (NumberFormatException e){
            ans.AddErrorMsg("ID \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            Main.logger.error("ID \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            return false;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 ID");
            Main.logger.error("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 ID");
            return false;
        }
        if (!collectionManager.checkID(id)) {
            ans.AddAnswer("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0433\u043E ID");
            Main.logger.info("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0433\u043E ID");
            return true;
        }
        if (!user.getUsername().equals(collectionManager.getById(id).getUser().getUsername())){
            ans.AddErrorMsg("\u041D\u0435\u0442 \u0440\u0430\u0437\u0440\u0435\u0448\u0435\u043D\u0438\u044F \u043D\u0430 \u0443\u0434\u0430\u043B\u0435\u043D\u0438\u0435, \u0442\u0430\u043A \u043A\u0430\u043A \u0432\u043B\u0430\u0434\u0435\u043B\u0435\u0446: " + collectionManager.getById(id).getUser().getUsername());
            Main.logger.error("\u0423 \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0435\u043B\u044F " + user.getUsername() + " \u043D\u0435\u0442 \u043F\u0440\u0430\u0432 \u0440\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u0442\u044C \u044D\u0442\u043E\u0442 \u044D\u043B\u0435\u043C\u0435\u043D\u0442.");
            return false;
        }
        databaseCollectionManager.deleteStudyGroupById(id);
        collectionManager.update();
        Main.logger.info("\u0423\u0441\u043F\u0435\u0448\u043D\u043E \u0443\u0434\u0430\u043B\u0435\u043D \u044D\u043B\u0435\u043C\u0435\u043D\u0442 \u0441 ID: " + Integer.toString(id));
        ans.AddAnswer("\u0423\u0441\u043F\u0435\u0448\u043D\u043E \u0443\u0434\u0430\u043B\u0435\u043D\u043E");
        return true;
    }
}