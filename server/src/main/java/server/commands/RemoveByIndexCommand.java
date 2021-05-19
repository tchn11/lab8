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
        return " index : удалить элемент, находящийся в заданной позиции коллекции (index)";
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
            ans.AddErrorMsg("индекс должен быть числом");
            Main.logger.error("индекс должен быть числом");
            return false;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("Должен присутствовать аргумент Index");
            Main.logger.error("Должен присутствовать аргумент Index");
            return false;
        }
        if (collectionManager.getSize() <= index || index < 0) {
            ans.AddAnswer("Нет такого индекса");
            Main.logger.info("Нет такого индекса");
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
            ans.AddErrorMsg("Нет разрешения на редактирование, так как владелец: " + collectionManager.getById((int) remId).getUser().getUsername());
            Main.logger.error("У пользоваеля " + user.getUsername() + " нет прав редактировать этот элемент.");
            return false;
        }
        databaseCollectionManager.deleteStudyGroupById(remId);
        collectionManager.update();
        Main.logger.info("Успешно удален элемент c индексом " + Integer.toString(index));
        ans.AddAnswer("Успешно удалено");
        return true;
    }
}
