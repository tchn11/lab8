package server.connection;

import messages.AnswerMsg;
import messages.CommandMsg;
import server.Main;
import server.commands.CommandManager;

public class RequestHandler extends Thread{
    CommandManager commandManager;
    CommandMsg commandMsg;
    AnswerMsg answerMsg;

    public RequestHandler(CommandManager comm, CommandMsg msg, AnswerMsg ans){
        commandManager = comm;
        commandMsg = msg;
        answerMsg = ans;
        Main.logger.info("Создан новый запрос");

        start();
    }

    public void run(){
        Main.logger.info("Начинаю выполение команды");
        commandManager.executeCommand(commandMsg, answerMsg);
        Main.logger.info("Команда выполнена");
    }
}
