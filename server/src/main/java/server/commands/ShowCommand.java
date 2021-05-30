package server.commands;

import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import general.data.User;

public class ShowCommand implements Commandable{
    CollectionManager collectionManager;
    /**
     * Constructor, just set variables for work
     * @param col Collection Manager
     */
    public ShowCommand(CollectionManager col){
        collectionManager = col;
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return " : \u0432\u044B\u0432\u0435\u0441\u0442\u0438 \u0432 \u0441\u0442\u0430\u043D\u0434\u0430\u0440\u0442\u043D\u044B\u0439 \u043F\u043E\u0442\u043E\u043A \u0432\u044B\u0432\u043E\u0434\u0430 \u0432\u0441\u0435 \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u044B \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u0438 \u0432 \u0441\u0442\u0440\u043E\u043A\u043E\u0432\u043E\u043C \u043F\u0440\u0435\u0434\u0441\u0442\u0430\u0432\u043B\u0435\u043D\u0438\u0438";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        ans.AddAnswer("\u041A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044F:\n");
        ans.AddAnswer(collectionManager.getList());
        Main.logger.info("\u041F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044C \u0437\u0430\u043F\u0440\u043E\u0441\u0438\u043B \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u044E");
        return true;
    }
}
