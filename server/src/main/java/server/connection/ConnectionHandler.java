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
    private DatabaseUserManager databaseUserManager;

    public static long WorksNow = 0;

    public ConnectionHandler(SocketChannel channel, CommandManager comm, DatabaseUserManager userDatatbase){
        databaseUserManager = userDatatbase;
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
        Main.logger.info("Получаю логин и пароль");
        CommandMsg login = (CommandMsg) readObj();
        Hasher hs2 = new Hasher();
        if (login.getCommandName().equals("login")){
            if (databaseUserManager.checkUserByUsernameAndPassword(hs2.hashUser(login.getUser())))
            {
                AnswerMsg answerMsg = new AnswerMsg();
                answerMsg.AddStatus(Status.FINE);

                AnswerHandler ansTask = new AnswerHandler(objectOutputStream, answerMsg);
                try {
                    ansTask.join();
                } catch (InterruptedException e) {
                    Main.logger.error("Ошибка ожидания выполнения запроса");
                }
            }
            else {
                AnswerMsg answerMsg = new AnswerMsg();
                answerMsg.AddStatus(Status.ERROR);
                answerMsg.AddErrorMsg("Неверное имя пользователя или пароль");
                AnswerHandler ansTask = new AnswerHandler(objectOutputStream, answerMsg);
                try {
                    ansTask.join();
                } catch (InterruptedException e) {
                    Main.logger.error("Ошибка ожидания выполнения запроса");
                }
                closeChannel();
                return;
            }
        }
        else if (login.getCommandName().equals("register")){
            if(databaseUserManager.insertUser(hs2.hashUser(login.getUser())))
            {
                AnswerMsg answerMsg = new AnswerMsg();
                answerMsg.AddStatus(Status.FINE);

                AnswerHandler ansTask = new AnswerHandler(objectOutputStream, answerMsg);
                try {
                    ansTask.join();
                } catch (InterruptedException e) {
                    Main.logger.error("Ошибка ожидания выполнения запроса");
                }
            }
            else {
                AnswerMsg answerMsg = new AnswerMsg();
                answerMsg.AddStatus(Status.ERROR);
                answerMsg.AddErrorMsg("Ошибка регистрации");
                AnswerHandler ansTask = new AnswerHandler(objectOutputStream, answerMsg);
                try {
                    ansTask.join();
                } catch (InterruptedException e) {
                    Main.logger.error("Ошибка ожидания выполнения запроса");
                }
                closeChannel();
                return;
            }
        }
        else {
            closeChannel();
            return;
        }
        boolean working = true;
        Main.logger.info("Начинаю общение с пользователем");
        while (working) {
            Object obj = readObj();
            if (obj == null){
                Main.logger.error("Ошибка чтения, закрываю поток");
                closeChannel();
                return;
            }
            CommandMsg commandMsg = (CommandMsg) obj;
            if (!databaseUserManager.checkUserByUsernameAndPassword(hs2.hashUser(commandMsg.getUser())))
            {
                Main.logger.error("Ошибка авторизации по ходу работы");
                closeChannel();
                return;
            }
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
                closeChannel();
                return;
            }
            if (answerMsg.getStatus() == Status.EXIT)
                working = false;
        }
        Main.logger.info("Заканчиваю поток, пользователь написал выход");
        closeChannel();
    }

    private void closeChannel(){
        WorksNow--;
        try {
            socketChannel.close();
            objectOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            Main.logger.error("Ошибка закрытия портов");
        }
    }
}
