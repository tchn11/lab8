package server;

import server.commands.CommandManager;
import server.connection.ConnectionHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class for server, connect and send
 */
public class Server {
    private int port;
    private int timeout;
    private ServerSocketChannel socketChannel;
    private CommandManager commandManager;
    private int maxUsers;
    private ExecutorService cachedThreadPool;

    public Server(int in_port, int in_timeout, CommandManager com, int MaxUsers){
        port = in_port;
        timeout = in_timeout;
        commandManager = com;
        maxUsers = MaxUsers;

        cachedThreadPool = Executors.newCachedThreadPool();
    }

    /**
     * Open and set server
     * @return All right or not
     */
    private boolean openSocket(){
        try {
            Main.logger.info("Начинаю запуск сервера");
            socketChannel = ServerSocketChannel.open();
            socketChannel.socket().bind(new InetSocketAddress(port));
            socketChannel.socket().setSoTimeout(timeout);
            Main.logger.info("Сервер успешно запущен");
            return true;
        } catch (IllegalArgumentException exception) {
            Main.logger.fatal("Порт '" + port + "' находится за пределами возможных значений");
            return false;
        }catch (IOException exception) {
            Main.logger.fatal("Произошла ошибка при попытке использовать порт");
            return false;
        }
    }

    /**
     * Close server in end of work
     */
    private  void closeSocket(){
        try{
            Main.logger.info("Пытаюсь закрыть сервер");
            socketChannel.close();
            Main.logger.info("Сервер успешно закрыт");
        } catch (IOException exception) {
            Main.logger.error("Ошибка при закрытии сервера");
        }
    }

    /**
     * Wait for client and set connection with him
     */
    private SocketChannel startTransmission(){
        SocketChannel clientChanel;
        try {
            Main.logger.info("Вхожу в ожидание соединения");
            clientChanel = socketChannel.accept();
            Main.logger.info("Уcтановлено соединение с клиентом");
        } catch (IOException exception) {
            Main.logger.error("Ошибка подключения к клиенту");
            return null;
        }
        return clientChanel;
    }


    /**
     * Main run class, read client, execute and answer
     */
    public void run() {
        if (!openSocket())
            return;
        boolean working = true;
        while (working) {
            if (ConnectionHandler.WorksNow >= 10){
                Main.logger.error("Максимум соединений!!");
                while(ConnectionHandler.WorksNow >= 10){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Main.logger.error("Ошибка ожидания!");
                    }
                }
            }
            SocketChannel client = startTransmission();
            if (client == null)
                Main.logger.error("Ошибка");
            else
                cachedThreadPool.submit(new ConnectionHandler(client, commandManager));

            Main.logger.info("Запущен " + ConnectionHandler.WorksNow + " поток");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Main.logger.error("Ошибка ожидания!");
            }
        }
        Main.logger.info("Конец завершение работы");
        closeSocket();
    }
}