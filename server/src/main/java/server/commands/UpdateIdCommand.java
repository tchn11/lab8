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
        return " id {element} : обновить значение элемента коллекции, id которого равен заданному";
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
            ans.AddErrorMsg("ID должен быть числом");
            Main.logger.error("Ошибка ввода: ID должен быть числом");
            return false;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("Должен присутствовать аргумент ID");
            Main.logger.error("Ошибка ввода: Должен присутствовать аргумент ID");
            return false;
        }
        if (!collectionManager.checkID(id)) {
            ans.AddErrorMsg("Нет такого ID");
            Main.logger.error("Ошибка ввода: Нет такого ID");
            return false;
        }
        RowStudyGroup sg = (RowStudyGroup) obArg;
        databaseCollectionManager.updateStudyGroupById(id, sg);
        collectionManager.update();
        Main.logger.info("Успешно изменен элемент коллекции " + sg.toString());
        ans.AddAnswer("Успешно изменено");
        return true;
    }
}
