package server.connection;

import general.data.User;
import server.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A manager of user database.
 */
public class DatabaseUserManager {
    // USER_TABLE
    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseManager.USER_TABLE +
            " WHERE " + DatabaseManager.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseManager.USER_TABLE +
            " WHERE " + DatabaseManager.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            DatabaseManager.USER_TABLE + " (" +
            DatabaseManager.USER_TABLE_USERNAME_COLUMN + ", " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + ") VALUES (?, ?)";

    private DatabaseManager databaseManager;

    public DatabaseUserManager(DatabaseManager database) {
        this.databaseManager = database;
    }


    public User getUserById(long userId){
        User user = null;
        PreparedStatement preparedSelectUserByIdStatement = null;
        try {
            preparedSelectUserByIdStatement =
                    databaseManager.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedSelectUserByIdStatement.setLong(1, userId);
            ResultSet resultSet = preparedSelectUserByIdStatement.executeQuery();
            Main.logger.info("Выполнен запрос SELECT_USER_BY_ID.");
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseManager.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseManager.USER_TABLE_PASSWORD_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запроса SELECT_USER_BY_ID!");
        } finally {
            databaseManager.closePreparedStatement(preparedSelectUserByIdStatement);
        }
        return user;
    }


    public boolean checkUserByUsernameAndPassword(User user) {
        PreparedStatement preparedSelectUserByUsernameAndPasswordStatement = null;
        try {
            preparedSelectUserByUsernameAndPasswordStatement =
                    databaseManager.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserByUsernameAndPasswordStatement.setString(1, user.getUsername());
            preparedSelectUserByUsernameAndPasswordStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedSelectUserByUsernameAndPasswordStatement.executeQuery();
            boolean result = resultSet.next();
            Main.logger.info("Выполнен запрос SELECT_USER_BY_USERNAME_AND_PASSWORD: " + result);
            return result;
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
        } finally {
            databaseManager.closePreparedStatement(preparedSelectUserByUsernameAndPasswordStatement);
        }
        return false;
    }

    public long getUserIdByUsername(User user) {
        long userId;
        PreparedStatement preparedSelectUserByUsernameStatement = null;
        try {
            preparedSelectUserByUsernameStatement =
                    databaseManager.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedSelectUserByUsernameStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedSelectUserByUsernameStatement.executeQuery();
            Main.logger.info("Выполнен запрос SELECT_USER_BY_USERNAME.");
            if (resultSet.next()) {
                userId = resultSet.getLong(DatabaseManager.USER_TABLE_ID_COLUMN);
            } else userId = -1;
            return userId;
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME!");
        } finally {
            databaseManager.closePreparedStatement(preparedSelectUserByUsernameStatement);
        }
        return -1;
    }

    public boolean insertUser(User user) {
        PreparedStatement preparedInsertUserStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;
            preparedInsertUserStatement =
                    databaseManager.getPreparedStatement(INSERT_USER, false);
            preparedInsertUserStatement.setString(1, user.getUsername());
            preparedInsertUserStatement.setString(2, user.getPassword());
            if (preparedInsertUserStatement.executeUpdate() == 0) throw new SQLException();
            Main.logger.info("Выполнен запрос INSERT_USER.");
            return true;
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запроса INSERT_USER!");
        } finally {
            databaseManager.closePreparedStatement(preparedInsertUserStatement);
        }
        return false;
    }
}
