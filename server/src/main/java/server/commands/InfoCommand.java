package server.commands;

import messages.AnswerMsg;
import server.Main;
import server.collection.CollectionManager;
import general.data.User;

public class InfoCommand implements Commandable{
    CollectionManager collectionManager;

    /**
     * Constructor, just set variables for work
     * @param col Collection Manager
     */
    public InfoCommand(CollectionManager col){
        collectionManager = col;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return ": \u0432\u044B\u0432\u0435\u0441\u0442\u0438 \u0432 \u0441\u0442\u0430\u043D\u0434\u0430\u0440\u0442\u043D\u044B\u0439 \u043F\u043E\u0442\u043E\u043A \u0432\u044B\u0432\u043E\u0434\u0430 \u0438\u043D\u0444\u043E\u0440\u043C\u0430\u0446\u0438\u044E \u043E \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u0438 (\u0442\u0438\u043F, \u0434\u0430\u0442\u0430 \u0438\u043D\u0438\u0446\u0438\u0430\u043B\u0438\u0437\u0430\u0446\u0438\u0438, \u043A\u043E\u043B\u0438\u0447\u0435\u0441\u0442\u0432\u043E \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u043E\u0432 \u0438 \u0442.\u0434.)";
    }

    @Override
    public boolean execute(String arg, Object obArg, AnswerMsg ans, User user) {
        ans.AddAnswer(collectionManager.getInfo());
        Main.logger.info("\u041F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044C \u043F\u043E\u043B\u0443\u0447\u0438\u043B \u0438\u043D\u0444\u043E\u0440\u043C\u0430\u0446\u0438\u044E \u043E \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u0438");
        return true;
    }
}
