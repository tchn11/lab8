package server.commands;

import general.data.User;
import messages.AnswerMsg;

public interface Commandable {
    /**
     * Execute command
     * @param arg Argument
     * @param obArg Object argument
     * @param ans Answer object
     * @param user
     * @return End or not to end
     */
    boolean execute(String arg, Object obArg, AnswerMsg ans, User user);
    /**
     * Get name of the command
     * @return Name
     */
    String getName();
    /**
     * Get description for 'help' command
     * @return Description
     */
    String getDescription();
}
