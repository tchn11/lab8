package server.commands;

import general.exeptions.EmptyIOException;
import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import general.data.User;

public class FilterStartsWithNameCommand implements Commandable{
    CollectionManager collectionManager;

    /**
     * Constructor, just set variables for work
     * @param col Collection Manager
     */
    public FilterStartsWithNameCommand(CollectionManager col)
    {
        collectionManager = col;
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        try{
            if (arg.trim().equals(""))
                throw new EmptyIOException();
        }catch (EmptyIOException e){
            ans.AddErrorMsg("\u041D\u0435\u043E\u0431\u0445\u043E\u0434\u0438\u043C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 name");
            Main.logger.error("\u041D\u0435\u043E\u0431\u0445\u043E\u0434\u0438\u043C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 name");
            return false;
        }
        String list = collectionManager.StartsWithName(arg);
        if (list.equals("")){
            ans.AddAnswer("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0433\u043E \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u0430");
        }
        else{
            ans.AddAnswer(list);
        }
        Main.logger.info("\u041F\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044C \u0443\u0434\u0430\u043B\u0438\u043B \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u044B");
        return true;
    }

    @Override
    public String getName() {
        return "filter_starts_with_name";
    }

    @Override
    public String getDescription() {
        return " name : \u0432\u044B\u0432\u0435\u0441\u0442\u0438 \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u044B, \u0437\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F name \u043A\u043E\u0442\u043E\u0440\u044B\u0445 \u043D\u0430\u0447\u0438\u043D\u0430\u0435\u0442\u0441\u044F \u0441 \u0437\u0430\u0434\u0430\u043D\u043D\u043E\u0439 \u043F\u043E\u0434\u0441\u0442\u0440\u043E\u043A\u0438";
    }
}
