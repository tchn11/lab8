package server.commands;

import general.data.RowStudyGroup;
import messages.AnswerMsg;
import messages.CommandMsg;
import messages.Status;
import server.Main;
import server.collection.CollectionManager;
import server.file.ScriptManager;
import general.data.User;

import java.util.Stack;

/**
 * Operates with commands. Choose what command should be called.
 */
public class CommandManager {
    private Commandable[] commands;
    private CollectionManager collectionManager;
    private String History[];
    private Stack<String> openedScripts;
    /**
     * Constructor, set start values
     * @param col Collection manager
     * @param comm Array of all commands (implements from Commandable)
     */
    public CommandManager(CollectionManager col, Commandable[] comm){
        commands = comm;
        collectionManager = col;
        History = new String[7];
        for (int i = 0; i < 7; i++){
            History[i] = "";
        }
        openedScripts = new Stack<String>();
    }

    /**
     * Execute command
     * @param commandMsg Command witch should be executed
     * @param ans What should return
     */
    public synchronized void executeCommand(CommandMsg commandMsg, AnswerMsg ans){
        Main.logger.info("\u0412\u044B\u043F\u043E\u043B\u043D\u044F\u0435\u0442\u0441\u044F \u043A\u043E\u043C\u0430\u043D\u0434\u0430 " + commandMsg.getCommandName() + " " + commandMsg.getCommandStringArgument());
        if (commandMsg.getCommandName().trim().equals("help")) {
            ans.AddAnswer("help : \u0432\u044B\u0432\u0435\u0441\u0442\u0438 \u0441\u043F\u0440\u0430\u0432\u043A\u0443 \u043F\u043E \u0434\u043E\u0441\u0442\u0443\u043F\u043D\u044B\u043C \u043A\u043E\u043C\u0430\u043D\u0434\u0430\u043C");
            ans.AddAnswer("execute_script file_name : \u0441\u0447\u0438\u0442\u0430\u0442\u044C \u0438 \u0438\u0441\u043F\u043E\u043B\u043D\u0438\u0442\u044C \u0441\u043A\u0440\u0438\u043F\u0442 \u0438\u0437 \u0443\u043A\u0430\u0437\u0430\u043D\u043D\u043E\u0433\u043E \u0444\u0430\u0439\u043B\u0430. \u0412 \u0441\u043A\u0440\u0438\u043F\u0442\u0435 \u0441\u043E\u0434\u0435\u0440\u0436\u0430\u0442\u0441\u044F \u043A\u043E\u043C\u0430\u043D\u0434\u044B \u0432 \u0442\u0430\u043A\u043E\u043C \u0436\u0435 \u0432\u0438\u0434\u0435, \u0432 \u043A\u043E\u0442\u043E\u0440\u043E\u043C \u0438\u0445 \u0432\u0432\u043E\u0434\u0438\u0442 \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044C \u0432 \u0438\u043D\u0442\u0435\u0440\u0430\u043A\u0442\u0438\u0432\u043D\u043E\u043C \u0440\u0435\u0436\u0438\u043C\u0435.");
            ans.AddAnswer("history : \u0432\u044B\u0432\u0435\u0441\u0442\u0438 \u043F\u043E\u0441\u043B\u0435\u0434\u043D\u0438\u0435 7 \u043A\u043E\u043C\u0430\u043D\u0434 (\u0431\u0435\u0437 \u0438\u0445 \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442\u043E\u0432)");
            ans.AddAnswer("exit : \u0437\u0430\u0432\u0435\u0440\u0448\u0438\u0442\u044C \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u0443 (\u0431\u0435\u0437 \u0441\u043E\u0445\u0440\u0430\u043D\u0435\u043D\u0438\u044F \u0432 \u0444\u0430\u0439\u043B)");
            for (Commandable comman : commands) {
                ans.AddAnswer(comman.getName() + comman.getDescription());
            }
            ans.AddStatus(Status.FINE);
        }
        else if(commandMsg.getCommandName().trim().equals("exit")){
            ans.AddStatus(Status.EXIT);
            return;
        }
        else if(commandMsg.getCommandName().trim().equals("history")){
            ans.AddAnswer("\u0438\u0441\u0442\u043E\u0440\u0438\u044F:");
            for (int i = 0; i<7; i++){
                if (History[i]=="") break;
                ans.AddAnswer(History[i]);
            }
            ans.AddStatus(Status.FINE);
        }
        else if(commandMsg.getCommandName().trim().equals("execute_script")){
            if (!commandMsg.getCommandStringArgument().trim().equals("")) {
                ScriptMode(commandMsg.getCommandStringArgument().trim(), ans, commandMsg.getUser());
                openedScripts.clear();
                ans.AddStatus(Status.FINE);
            }
            else{
                ans.AddStatus(Status.ERROR);
                Main.logger.error("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0438\u043C\u044F \u0444\u0430\u0439\u043B\u0430");
            }
        }
        else if (!commandMsg.getCommandName().trim().equals("")){
            boolean isFindCommand = false;
            for (Commandable comman : commands) {
                if (commandMsg.getCommandName().trim().equals(comman.getName())) {
                    boolean stat =  comman.execute(commandMsg.getCommandStringArgument(), commandMsg.getCommandObjectArgument(),
                            ans, commandMsg.getUser());
                    if (stat){
                        ans.AddStatus(Status.FINE);
                    }else{
                        ans.AddStatus(Status.ERROR);
                    }
                    isFindCommand = true;

                    break;
                }
            }
            if (!isFindCommand){
                Main.logger.error("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0439 \u043A\u043E\u043C\u0430\u043D\u0434\u044B");
                ans.AddErrorMsg("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0439 \u043A\u043E\u043C\u0430\u043D\u0434\u044B");
                ans.AddStatus(Status.ERROR);
            }
        }
        if(!commandMsg.getCommandName().trim().equals("")) {
            for (int i = 6; i > 0; i--) {
                History[i] = History[i - 1];
            }
            History[0] = commandMsg.getCommandName();
        }

    }

    /**
     * Script mode. Open script and do it.
     * @param file Script file
     * @param ans Witch should return to user
     */
    private void ScriptMode(String file, AnswerMsg ans, User user) {
        ScriptManager scriptManager = new ScriptManager(file.trim());
        openedScripts.add(file.trim());
        boolean isRuning = true;
        while (isRuning) {
            String nextLine = scriptManager.nextLine();
            if (nextLine == null)
                break;
            String[] cmd = (nextLine.trim() + " ").split(" ",2);
            if (cmd[0].trim().equals("exit")){
                break;
            }
            if (cmd[0].trim().equals("help")) {
                ans.AddAnswer("help : \u0432\u044B\u0432\u0435\u0441\u0442\u0438 \u0441\u043F\u0440\u0430\u0432\u043A\u0443 \u043F\u043E \u0434\u043E\u0441\u0442\u0443\u043F\u043D\u044B\u043C \u043A\u043E\u043C\u0430\u043D\u0434\u0430\u043C");
                ans.AddAnswer("execute_script file_name : \u0441\u0447\u0438\u0442\u0430\u0442\u044C \u0438 \u0438\u0441\u043F\u043E\u043B\u043D\u0438\u0442\u044C \u0441\u043A\u0440\u0438\u043F\u0442 \u0438\u0437 \u0443\u043A\u0430\u0437\u0430\u043D\u043D\u043E\u0433\u043E \u0444\u0430\u0439\u043B\u0430. \u0412 \u0441\u043A\u0440\u0438\u043F\u0442\u0435 \u0441\u043E\u0434\u0435\u0440\u0436\u0430\u0442\u0441\u044F \u043A\u043E\u043C\u0430\u043D\u0434\u044B \u0432 \u0442\u0430\u043A\u043E\u043C \u0436\u0435 \u0432\u0438\u0434\u0435, \u0432 \u043A\u043E\u0442\u043E\u0440\u043E\u043C \u0438\u0445 \u0432\u0432\u043E\u0434\u0438\u0442 \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044C \u0432 \u0438\u043D\u0442\u0435\u0440\u0430\u043A\u0442\u0438\u0432\u043D\u043E\u043C \u0440\u0435\u0436\u0438\u043C\u0435.");
                ans.AddAnswer("history : \u0432\u044B\u0432\u0435\u0441\u0442\u0438 \u043F\u043E\u0441\u043B\u0435\u0434\u043D\u0438\u0435 7 \u043A\u043E\u043C\u0430\u043D\u0434 (\u0431\u0435\u0437 \u0438\u0445 \u0430\u0440\u0433\u0443\u043C\u0435\u043D\u0442\u043E\u0432)");
                ans.AddAnswer("exit : \u0437\u0430\u0432\u0435\u0440\u0448\u0438\u0442\u044C \u043F\u0440\u043E\u0433\u0440\u0430\u043C\u043C\u0443 (\u0431\u0435\u0437 \u0441\u043E\u0445\u0440\u0430\u043D\u0435\u043D\u0438\u044F \u0432 \u0444\u0430\u0439\u043B)");
                for (Commandable comman : commands) {
                    ans.AddAnswer(comman.getName() + comman.getDescription());
                }
            }
            else if(cmd[0].trim().equals("history")){
                ans.AddAnswer("\u0438\u0441\u0442\u043E\u0440\u0438\u044F:");
                for (int i = 0; i<7; i++){
                    if (History[i]=="") break;
                    ans.AddAnswer(History[i]);
                }
            }
            else if(cmd[0].trim().equals("execute_script")){
                if (!cmd[1].trim().equals("")) {
                    if (openedScripts.contains(cmd[1].trim())){
                        ans.AddErrorMsg("\u041F\u043E\u043F\u044B\u0442\u043A\u0430 \u0440\u0435\u043A\u0443\u0440\u0441\u0438\u0432\u043D\u043E \u0432\u044B\u0437\u0432\u0430\u0442\u044C \u0441\u043A\u0440\u0438\u043F\u0442");
                    }
                    else {
                        ScriptMode(cmd[1].trim(), ans, user);
                    }
                }
                else{
                    ans.AddErrorMsg("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0438\u043C\u044F \u0444\u0430\u0439\u043B\u0430");
                }
            }
            else if (!cmd[0].trim().equals("")){
                boolean isFindCommand = false;
                for (Commandable comman : commands) {
                    if (cmd[0].trim().equals(comman.getName())) {
                        RowStudyGroup rowStudyGroup = null;
                        if (cmd[0].trim().equals("add") | cmd[0].trim().equals("update")
                                | cmd[0].trim().equals("add_if_max")){
                            isFindCommand = true;
                            rowStudyGroup = scriptManager.askGroup();
                            if (rowStudyGroup == null){
                                ans.AddErrorMsg("\u041E\u0448\u0438\u0431\u043A\u0430 \u043F\u0430\u0440\u0441\u0438\u043D\u0433\u0430 \u043E\u0431\u044A\u0435\u043A\u0442\u0430 \u0438\u0437 \u0444\u0430\u0439\u043B\u0430");
                                break;
                            }
                        }
                        comman.execute(cmd[1], rowStudyGroup, ans, user);
                        isFindCommand = true;
                    }
                }
                if (!isFindCommand){
                    ans.AddErrorMsg("\u041D\u0435\u0442 \u0442\u0430\u043A\u043E\u0439 \u043A\u043E\u043C\u0430\u043D\u0434\u044B: " + cmd[0]+ " " + cmd[1]);
                }
            }
            if(!cmd[0].trim().equals("")) {
                for (int i = 6; i > 0; i--) {
                    History[i] = History[i - 1];
                }
                History[0] = cmd[0];
            }

        }
        ans.AddAnswer("\u0421\u043A\u0440\u0438\u043F\u0442 \u0432\u044B\u043F\u043E\u043B\u043D\u0435\u043D");
    }

}
