package server.commands;

import general.data.StudyGroup;
import general.exeptions.EmptyIOException;
import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import general.data.User;
import server.connection.DatabaseCollectionManager;

public class RemoveByIndexCommand implements Commandable{
    CollectionManager collectionManager;
    DatabaseCollectionManager databaseCollectionManager;
    /**
     * Constructor, just set variables for work
     * @param col Collection Manager
     */
    public RemoveByIndexCommand(CollectionManager col, DatabaseCollectionManager db){
        collectionManager = col;
        databaseCollectionManager = db;
    }

    @Override
    public String getDescription() {
        return " index : \u0443\u0434\u0430\u043B\u0438\u0442\u044C \u044D\u043B\u0435\u043C\u0435\u043D\u0442, \u043D\u0430\u0445\u043E\u0434\u044F\u0449\u0438\u0439\u0441\u044F \u0432 \u0437\u0430\u0434\u0430\u043D\u043D\u043E\u0439 \u043F\u043E\u0437\u0438\u0446\u0438\u0438 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u0438 (index)";
    }

    @Override
    public String getName() {
        return "remove_at";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        int index;
        try{
            if (arg.trim().equals(""))
                throw new EmptyIOException();
            index = Integer.parseInt(arg.trim());
        }
        catch (NumberFormatException e){
            ans.AddErrorMsg("\u0438\u043D\u0434\u0435\u043A\u0441 \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            Main.logger.error("\u0438\u043D\u0434\u0435\u043A\u0441 \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            return false;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 Index");
            Main.logger.error("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 Index");
            return false;
        }
        if (collectionManager.getSize() <= index || index < 0) {
            ans.AddAnswer("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0433\u043E \u0438\u043D\u0434\u0435\u043A\u0441\u0430");
            Main.logger.info("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0433\u043E \u0438\u043D\u0434\u0435\u043A\u0441\u0430");
            return true;
        }
        long i = 0;
        long remId = 0;
        for (StudyGroup sg : collectionManager.getMyCollection()){
            if (i == index){
                remId = sg.getId();
                break;
            }
            i++;
        }
        if (!user.getUsername().equals(collectionManager.getById((int) remId).getUser().getUsername())){
            ans.AddErrorMsg("\u041D\u0435\u0442 \u0440\u0430\u0437\u0440\u0435\u0448\u0435\u043D\u0438\u044F \u043D\u0430 \u0440\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u0435, \u0442\u0430\u043A \u043A\u0430\u043A \u0432\u043B\u0430\u0434\u0435\u043B\u0435\u0446: " + collectionManager.getById((int) remId).getUser().getUsername());
            Main.logger.error("\u0423 \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0435\u043B\u044F " + user.getUsername() + " \u043D\u0435\u0442 \u043F\u0440\u0430\u0432 \u0440\u0435\u0434\u0430\u043A\u0442\u0438\u0440\u043E\u0432\u0430\u0442\u044C \u044D\u0442\u043E\u0442 \u044D\u043B\u0435\u043C\u0435\u043D\u0442.");
            return false;
        }
        databaseCollectionManager.deleteStudyGroupById(remId);
        collectionManager.update();
        Main.logger.info("\u0423\u0441\u043F\u0435\u0448\u043D\u043E \u0443\u0434\u0430\u043B\u0435\u043D \u044D\u043B\u0435\u043C\u0435\u043D\u0442 c \u0438\u043D\u0434\u0435\u043A\u0441\u043E\u043C " + Integer.toString(index));
        ans.AddAnswer("\u0423\u0441\u043F\u0435\u0448\u043D\u043E \u0443\u0434\u0430\u043B\u0435\u043D\u043E");
        return true;
    }
}
