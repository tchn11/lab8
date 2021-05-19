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
        return " id : удалить элемент из коллекции по его id";
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
            ans.AddErrorMsg("ID должен быть числом");
            Main.logger.error("ID должен быть числом");
            return false;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("Должен присутствовать аргумент ID");
            Main.logger.error("Должен присутствовать аргумент ID");
            return false;
        }
        if (!collectionManager.checkID(id)) {
            ans.AddAnswer("Нет такого ID");
            Main.logger.info("Нет такого ID");
            return true;
        }
        if (!user.getUsername().equals(collectionManager.getById(id).getUser().getUsername())){
            ans.AddErrorMsg("Нет разрешения на удаление, так как владелец: " + collectionManager.getById(id).getUser().getUsername());
            Main.logger.error("У пользоваеля " + user.getUsername() + " нет прав редактировать этот элемент.");
            return false;
        }
        databaseCollectionManager.deleteStudyGroupById(id);
        collectionManager.update();
        Main.logger.info("Успешно удален элемент с ID: " + Integer.toString(id));
        ans.AddAnswer("Успешно удалено");
        return true;
    }
}