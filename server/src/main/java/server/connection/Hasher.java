package server.connection;

import general.data.User;
import server.Main;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public User hashUser(User usr){
        try {
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] bytes = md.digest(usr.getPassword().getBytes());
            BigInteger integers = new BigInteger(1, bytes);
            String newPassword = integers.toString(16);
            while (newPassword.length() < 32) {
                newPassword = "0" + newPassword;
            }
            return new User(usr.getUsername(), newPassword);
        } catch (NoSuchAlgorithmException exception) {
            Main.logger.error("Не найден алгоритм хэширования пароля!");
            return null;
        }
    }
}
