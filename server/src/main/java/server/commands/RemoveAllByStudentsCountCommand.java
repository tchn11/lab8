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
            ans.AddErrorMsg("studentsCount \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            Main.logger.error("studentsCount \u0434\u043E\u043B\u0436\u0435\u043D \u0431\u044B\u0442\u044C \u0447\u0438\u0441\u043B\u043E\u043C");
            return true;
        }
        catch (EmptyIOException e)
        {
            ans.AddErrorMsg("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 studentsCount");
            Main.logger.error("\u0414\u043E\u043B\u0436\u0435\u043D \u043F\u0440\u0438\u0441\u0443\u0442\u0441\u0442\u0432\u043E\u0432\u0430\u0442\u044C \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442 studentsCount");
            return true;
        }
        collectionManager.DeleteByStudentsCount(num, user, ans);
        collectionManager.update();
        ans.AddAnswer("\u042D\u043B\u0435\u043C\u0435\u043D\u0442\u044B \u0443\u0441\u043F\u0435\u0448\u043D\u043E \u0443\u0434\u0430\u043B\u0435\u043D\u044B");
        Main.logger.info("\u042D\u043B\u0435\u043C\u0435\u043D\u0442\u044B \u0443\u0441\u043F\u0435\u0448\u043D\u043E \u0434\u043E\u0431\u0430\u0432\u043B\u0435\u043D\u044B");
        return true;
    }

    @Override
    public String getName() {
        return "remove_all_by_students_count";
    }

    @Override
    public String getDescription() {
        return " studentsCount : \u0443\u0434\u0430\u043B\u0438\u0442\u044C \u0438\u0437 \u043A\u043E\u043B\u043B\u0435\u043A\u0446\u0438\u0438 \u0432\u0441\u0435 \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u044B, \u0437\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F studentsCount \u043A\u043E\u0442\u043E\u0440\u043E\u0433\u043E \u044D\u043A\u0432\u0438\u0432\u0430\u043B\u0435\u043D\u0442\u043D\u043E \u0437\u0430\u0434\u0430\u043D\u043D\u043E\u043C\u0443";
    }
}
