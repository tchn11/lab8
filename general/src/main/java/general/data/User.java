package general.data;

import java.io.Serializable;

/**
 * Class for get username and password.
 */
public class User implements Serializable {
    private String username;
    private String password;

    public User(String name, String pass) {
        username = name.trim();
        password = pass.trim();
    }

    /**
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return Password.
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username + ":" + password;
    }

    public void update(User usr){
        username = usr.getUsername();
        password = usr.getPassword();
    }

    @Override
    public int hashCode() {
        return username.hashCode() + password.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof User) {
            User userObj = (User) obj;
            return username.equals(userObj.getUsername()) && password.equals(userObj.getPassword());
        }
        return false;
    }
}
