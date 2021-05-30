package client;

import client.console.ConsoleManager;
import client.states.LoginStates;
import general.data.RowStudyGroup;
import general.data.User;
import general.exeptions.ConnectionBrokenException;
import messages.AnswerMsg;
import messages.CommandMsg;
import messages.Status;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import static client.console.ConsoleManager.print;
import static client.console.ConsoleManager.printErr;


/**
 * Class working with connection
 */
public class Client {
    private String serverHost;
    private int serverPort;
    private int connectionAttempts;
    private int connectionTimeout;

    public int attempts = 0;

    private ObjectOutputStream serverWriter;

    private ObjectInputStream serverReader;

    private Socket socket;

    private  ConsoleManager consoleManager;

    private User user;

    public Client(String host, int port, int attempts, int timeout, ConsoleManager cons){
        serverHost = host;
        serverPort = port;
        connectionAttempts = attempts;
        connectionTimeout = timeout;
        consoleManager = cons;
    }

    /**
     * Connect client to server
     * @return is closed successfully or not
     */
    public LoginStates connectToServer(String login, String password, boolean isRegistered){
        try {
            if (attempts > 0)
                print("Попытка переподключиться");
            attempts++;
            socket = new Socket(serverHost, serverPort);
            print("Сервер найден, встаю в очередь");
            serverWriter = new ObjectOutputStream(socket.getOutputStream());
            serverReader = new ObjectInputStream(socket.getInputStream());
            ConsoleManager.print("Вы уже зарегестрированы?");
            user = new User(login, password);
            if(isRegistered){
                CommandMsg msg = new CommandMsg("login", "", null ,user);
                writeMessage(msg);
            }
            else {
                CommandMsg msg = new CommandMsg("register", "", null, user);
                writeMessage(msg);
            }
            AnswerMsg answerMsg = (AnswerMsg) serverReader.readObject();
            if (answerMsg.getStatus().equals(Status.FINE)) {
                print("Вход в аккаунт произведен");
                return LoginStates.LOGINED;
            }else {
                print(answerMsg.getMessage());
                return LoginStates.LOGIN_ERROR;
            }
        } catch (UnknownHostException e) {
            printErr("Неизвестный хост: " + serverHost + "\n");
            return LoginStates.CONNECTION_ERROR;
        } catch (IOException exception) {
            printErr("Ошибка открытия порта " + serverPort + "\n");
            return LoginStates.CONNECTION_ERROR;
        } catch (ClassNotFoundException e) {
            print("Ошибка авторизации");
            return LoginStates.CONNECTION_ERROR;
        } catch (ConnectionBrokenException e) {
            printErr("Произошел разрыв соединения");
        }
        print("Порт успешно открыт.");
        return LoginStates.LOGINED;
    }

    /**
     * Method write message to server in CommandMsg format
     * @param msg message
     * @throws ConnectionBrokenException If connection was broken
     */
    private void writeMessage(CommandMsg msg) throws ConnectionBrokenException {
        try{
            serverWriter.writeObject(msg);
        } catch (IOException exception) {
            printErr("Разрыв соеденения");
            throw new ConnectionBrokenException();
        }
    }

    /**
     * Read message from server in AnswerMsg format
     * @return Message
     * @throws ConnectionBrokenException If connection was broken
     */
    private AnswerMsg readMessage() throws ConnectionBrokenException{
        AnswerMsg retMsg = null;
        try {
            retMsg =  (AnswerMsg) serverReader.readObject();
        } catch (IOException exception) {
            printErr("Разрыв соеденения");
            throw new ConnectionBrokenException();
        } catch (ClassNotFoundException exception) {
            printErr("Пришедшие данные не класс");
        }
        return retMsg;
    }

    /**
     * Close connection when everything end.
     */
    private void closeConnection(){
        try{
            socket.close();
            serverReader.close();
            serverWriter.close();
            print("Соеденение успешно закрыто");
        } catch (IOException exception) {
            printErr("Ошибка закрытия файлов");
        }
    }

    /**
     * Main function witch get command and send.
     */
    public void run(){
        /*boolean work = true;
        print("Подключаюсь к серверу");
        while (!connectToServer()) {
            if(attempts > connectionAttempts){
                printErr("Превышено количество попыток подключиться");
                return;
            }
            try {
                Thread.sleep(connectionTimeout);
            } catch (InterruptedException e) {
                printErr("Произошла ошибка при попытке ожидания подключения!");
                print("Повторное подключение будет произведено немедленно.");
            }

        }
        //print("Подключился, работаю");
        while (work){
            //print("Жду команду");
            consoleManager.waitCommand();
            RowStudyGroup studyGroup = null;
            if (consoleManager.getCommand().equals("add") | consoleManager.getCommand().equals("update")
            | consoleManager.getCommand().equals("add_if_max")){
                studyGroup = consoleManager.askGroup();
            }
            CommandMsg send = new CommandMsg(consoleManager.getCommand(), consoleManager.getArg(), studyGroup, user);

            AnswerMsg answ = null;
            boolean wasSend = false;
            try{
                writeMessage(send);
                wasSend = true;
                answ = readMessage();
            } catch (ConnectionBrokenException e) {
                print("Попытка переподключиться");
                while (!connectToServer()){
                    if(attempts > connectionAttempts){
                        printErr("Превышено количество попыток подключиться");
                        return;
                    }
                    try {
                        Thread.sleep(connectionTimeout);
                    } catch (InterruptedException exception) {
                        printErr("Произошла ошибка при попытке ожидания подключения");
                    }
                }
                if (wasSend){
                    print("Сервер запомнил данные о команде");
                }else {
                    print("Сервер не запомнил данные о команде");
                }

            }
            if (answ != null) {
                if (answ.getStatus() == Status.ERROR) {
                    print("При выполнении программы произошла ошибка");
                    print(answ.getMessage().trim());
                } else {
                    print(answ.getMessage().trim());
                }
                if (answ.getStatus() == Status.EXIT) {
                    work = false;
                }
            }
        }
        closeConnection();*/
    }

}
