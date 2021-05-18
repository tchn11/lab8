package server.commands;

import general.exeptions.EmptyIOException;
import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import general.data.User;

public class RemoveAllByStudentsCountCommand implements Commandable{
    CollectionManager collectionManager;
    /**
     * Constructor, just set variables for work
     * @param col Collection Manager
     */
    public RemoveAllByStudentsCountCommand(CollectionManager col){
        collectionManager = col;
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        int num;
        try{
            if (arg.trim().equals(""))
                throw new EmptyIOException();
            num = Integer.parseInt(arg.trim());
        }
        catch (NumberFormatException e){
            ans.AddErrorMsg("studentsCount должен быть числом");
            Main.logger.error("studentsCount должен быть числом");
            return true;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("Должен присутствовать аргумент studentsCount");
            Main.logger.error("Должен присутствовать аргумент studentsCount");
            return true;
        }
        collectionManager.DeleteByStudentsCount(num);
        collectionManager.update();
        ans.AddAnswer("Элементы успешно удалены");
        Main.logger.info("Элементы успешно добавлены");
        return true;
    }

    @Override
    public String getName() {
        return "remove_all_by_students_count";
    }

    @Override
    public String getDescription() {
        return " studentsCount : удалить из коллекции все элементы, значение поля studentsCount которого эквивалентно заданному";
    }
}
