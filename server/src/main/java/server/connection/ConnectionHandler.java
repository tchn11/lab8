package server.connection;

import messages.AnswerMsg;
import messages.CommandMsg;
import messages.Status;
import server.Main;
import server.commands.CommandManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

public class ConnectionHandler extends Thread{
    private SocketChannel socketChannel;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private CommandManager commandManager;

    public static long WorksNow = 0;

    public ConnectionHandler(SocketChannel channel, CommandManager comm){
        socketChannel = channel;
        commandManager = comm;
        try {
            Main.logger.info("Получаю разреение на чтение и запись");
            objectInputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
            objectOutputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            Main.logger.info("Разрешение на чтение и запись получено");
        } catch (IOException e) {
            Main.logger.error("Ошибка начала передачи");
        }
        WorksNow++;
        //start();
    }

    /**
     * Read object from client
     * @return Object witch was send
     */
    private Object readObj(){
        try{
            Main.logger.info("Начинаю чтение объекта");
            Object obj = objectInputStream.readObject();
            Main.logger.info("Объект получен");
            return obj;
        } catch (IOException exception) {
            Main.logger.error("Разрыв соеденения");
        } catch (ClassNotFoundException exception) {
            Main.logger.error("Ошибка получения объекта");
        }
        return null;
    }



    public void run(){
        boolean working = true;
        Main.logger.info("Начинаю общение с пользователем");
        while (working) {
            Object obj = readObj();
            if (obj == null){
                Main.logger.error("Ошибка чтения, закрываю поток");
                WorksNow--;
                return;
            }
            CommandMsg commandMsg = (CommandMsg) obj;
            AnswerMsg answerMsg = new AnswerMsg();
            RequestHandler task = new RequestHandler(commandManager, commandMsg, answerMsg);
            try {
                task.join();
            } catch (InterruptedException e) {
                Main.logger.error("Ошибка ожидания выполнения запроса");
            }
            AnswerHandler ansTask = new AnswerHandler(objectOutputStream, answerMsg);
            try {
                ansTask.join();
            } catch (InterruptedException e) {
                Main.logger.error("Ошибка ожидания выполнения запроса");
            }
            if (!ansTask.getStatus()){
                Main.logger.error("Ошибка отправки сообщения, закрываю поток");
                WorksNow--;
                return;
            }
            if (answerMsg.getStatus() == Status.EXIT)
                working = false;
        }
        Main.logger.info("Заканчиваю поток, пользователь написал выход");
        WorksNow--;
    }
}
