package server.commands;

import general.data.User;
import general.exeptions.EmptyIOException;
import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;

public class FilterLessThenExpelledCommand implements Commandable{
    CollectionManager collectionManager;
    /**
     * Constructor, just set variables for work
     * @param coll Collection Manager
     */
    public FilterLessThenExpelledCommand(CollectionManager coll)
    {
        collectionManager = coll;
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        int max;
        try{
            if (arg.trim().equals(""))
                throw new EmptyIOException();
            max = Integer.parseInt(arg.trim());
        }
        catch (NumberFormatException e){
            ans.AddErrorMsg("expelledStudents \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            Main.logger.error("expelledStudents \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            return false;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 expelledStudents");
            Main.logger.error("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 expelledStudents");
            return false;
        }
        String list = collectionManager.LessExpelled(max);
        if(list.equals("")){
            ans.AddAnswer("\u041D\u0435\u0442 \u0442\u0430\u043A\u0438\u0445 \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u043E\u0432.");
        }
        else {
            ans.AddAnswer(list);
        }
        Main.logger.info("\u041F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044C \u043F\u043E\u043B\u0443\u0447\u0438\u043B \u0441\u043F\u0438\u0441\u043E\u043A \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u043E\u0432");
        return true;
    }

    @Override
    public String getName() {
        return "filter_less_than_expelled_students";
    }

    @Override
    public String getDescription() {
        return " expelledStudents : \u0432\u044B\u0432\u0435\u0441\u0442\u0438 \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u044B, \u0437\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F expelledStudents \u043A\u043E\u0442\u043E\u0440\u044B\u0445 \u043C\u0435\u043D\u044C\u0448\u0435 \u0437\u0430\u0434\u0430\u043D\u043D\u043E\u0433\u043E";
    }
}
