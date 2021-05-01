package server.connection;

import messages.AnswerMsg;
import server.Main;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AnswerHandler extends Thread{
    private ObjectOutputStream objectOutputStream;
    private AnswerMsg answerMsg;
    private boolean status;
    public AnswerHandler(ObjectOutputStream obj, AnswerMsg ans){
        objectOutputStream = obj;
        answerMsg = ans;

        start();
    }

    /**
     * Send answer to user
     * @param answerMsg Message
     * @return All right or not
     */
    private boolean sendAnswer(AnswerMsg answerMsg){
        try{
            Main.logger.info("Отправляю ответ: " + answerMsg.getMessage());
            objectOutputStream.writeObject(answerMsg);
            objectOutputStream.flush();
            Main.logger.info("Ответ отправлен");
            return true;
        } catch (IOException exception) {
            Main.logger.error("Разрыв соеденения");
        }
        return false;
    }

    public void run(){
        status = sendAnswer(answerMsg);
    }

    public boolean getStatus(){
        return status;
    }
}
