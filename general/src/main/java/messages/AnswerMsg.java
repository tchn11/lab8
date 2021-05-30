package messages;

import java.io.Serializable;

/**
 * Message witch server send to client
 */
public class AnswerMsg implements Serializable {
    private String Msg;
    private Status status;
    private Serializable ObjAnswer;
    public AnswerMsg(){
        clearMessage();
    }

    /**
     * Clear message
     */
    public void clearMessage(){
        Msg = "";
        ObjAnswer = null;
    }

    /**
     * Add answer that you want to user see
     * @param str String that will bw added to answer
     */
    public void AddAnswer(String str){
        Msg += str + "\n";
    }

    /**
     * Add error message
     * @param str Message what happened
     */
    public void AddErrorMsg(String str){
        Msg += "err: " + str + "\n";
    }

    /**
     * Add status of message, if it is error or end
     * @param st Status of message
     */
    public void AddStatus(Status st){
        status = st;
    }

    public Serializable getObjAnswer() {
        return ObjAnswer;
    }

    public void setObjAnswer(Serializable objAnswer) {
        ObjAnswer = objAnswer;
    }

    /**
     * Get message
     * @return Message
     */
    public String getMessage(){
        return Msg;
    }

    /**
     * Get status of message
     * @return Status
     */
    public Status getStatus(){
        return status;
    }



}
