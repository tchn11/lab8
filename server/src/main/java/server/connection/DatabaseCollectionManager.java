package server.connection;

import general.data.*;
import server.Main;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Stack;
import java.util.Date;

public class DatabaseCollectionManager {
    // STUDY_GROUP_TABLE
    private final String SELECT_ALL_STUDY_GROUPS = "SELECT * FROM " + DatabaseManager.STUDY_GROUP_TABLE;
    private final String SELECT_STUDY_GROUPS_BY_ID = SELECT_ALL_STUDY_GROUPS + " WHERE " +
            DatabaseManager.STUDY_GROUP_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_STUDY_GROUP_BY_ID_AND_USER_ID = SELECT_STUDY_GROUPS_BY_ID + " AND " +
            DatabaseManager.STUDY_GROUP_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_STUDY_GROUP = "INSERT INTO " +
            DatabaseManager.STUDY_GROUP_TABLE + " (" +
            DatabaseManager.STUDY_GROUP_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.STUDY_GROUP_TABLE_COORDINATES_ID + ", " +
            DatabaseManager.STUDY_GROUP_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseManager.STUDY_GROUP_TABLE_STUDENTS_COUNT_COLUMN + ", " +
            DatabaseManager.STUDY_GROUP_TABLE_EXPELLED_STUDENTS_COLUMN + ", " +
            DatabaseManager.STUDY_GROUP_TABLE_AVERAGE_MARK_COLUMN + ", " +
            DatabaseManager.STUDY_GROUP_TABLE_SEMESTER_COLUMN + ", " +
            DatabaseManager.STUDY_GROUP_TABLE_GROUP_ADMIN_ID_COLUMN + ", " +
            DatabaseManager.STUDY_GROUP_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, " +
            "?, ?, ?, ?)";
    private final String DELETE_STUDY_GROUP_BY_ID = "DELETE FROM " + DatabaseManager.STUDY_GROUP_TABLE +
            " WHERE " + DatabaseManager.STUDY_GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_STUDY_GROUP_NAME_BY_ID = "UPDATE " + DatabaseManager.STUDY_GROUP_TABLE + " SET " +
            DatabaseManager.STUDY_GROUP_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.STUDY_GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_STUDY_GROUP_STUDENTS_COUNT_BY_ID = "UPDATE " + DatabaseManager.STUDY_GROUP_TABLE + " SET " +
            DatabaseManager.STUDY_GROUP_TABLE_STUDENTS_COUNT_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.STUDY_GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_STUDY_GROUP_EXPELLED_STUDENTS_BY_ID = "UPDATE " + DatabaseManager.STUDY_GROUP_TABLE + " SET " +
            DatabaseManager.STUDY_GROUP_TABLE_EXPELLED_STUDENTS_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.STUDY_GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_STUDY_GROUP_AVERAGE_MARK_BY_ID = "UPDATE " + DatabaseManager.STUDY_GROUP_TABLE + " SET " +
            DatabaseManager.STUDY_GROUP_TABLE_AVERAGE_MARK_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.STUDY_GROUP_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_STUDY_GROUP_SEMESTER_BY_ID = "UPDATE " + DatabaseManager.STUDY_GROUP_TABLE + " SET " +
            DatabaseManager.STUDY_GROUP_TABLE_SEMESTER_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.STUDY_GROUP_TABLE_ID_COLUMN + " = ?";
   // COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseManager.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseManager.COORDINATES_TABLE + " (" +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_COORDINATES_BY_ID = "UPDATE " + DatabaseManager.COORDINATES_TABLE + " SET " +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseManager.COORDINATES_TABLE +
            " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    // PERSON_TABLE
    private final String SELECT_ALL_PERSON = "SELECT * FROM " + DatabaseManager.PERSON_TABLE;
    private final String SELECT_PERSON_BY_ID = SELECT_ALL_PERSON +
            " WHERE " + DatabaseManager.PERSON_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_PERSON = "INSERT INTO " +
            DatabaseManager.PERSON_TABLE + " (" +
            DatabaseManager.PERSON_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.PERSON_TABLE_BIRTHDAY_COLUMN + ", " +
            DatabaseManager.PERSON_TABLE_WEIGHT_COLUMN + ", " +
            DatabaseManager.PERSON_TABLE_PASSPORT_ID_COLUMN + ") VALUES (?, ?, ?, ?)";
    private final String UPDATE_PERSON_BY_ID = "UPDATE " + DatabaseManager.PERSON_TABLE + " SET " +
            DatabaseManager.PERSON_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseManager.PERSON_TABLE_BIRTHDAY_COLUMN + " = ?, " +
            DatabaseManager.PERSON_TABLE_WEIGHT_COLUMN + " = ?, " +
            DatabaseManager.PERSON_TABLE_PASSPORT_ID_COLUMN + " = ?" +" WHERE " +
            DatabaseManager.PERSON_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_PERSON_BY_ID = "DELETE FROM " + DatabaseManager.PERSON_TABLE +
            " WHERE " + DatabaseManager.PERSON_TABLE_ID_COLUMN + " = ?";
    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseManager manager, DatabaseUserManager userManager) {
        databaseManager = manager;
        databaseUserManager = userManager;
    }

    public StudyGroup insertStudyGroup(RowStudyGroup groupRaw, User user){
        StudyGroup stGroup;
        PreparedStatement preparedInsertStudyGroupStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertPersonStatement = null;
        try {
            databaseManager.setCommitMode();
            databaseManager.setSavepoint();

            LocalDateTime creationTime = LocalDateTime.now();

            preparedInsertStudyGroupStatement = databaseManager.getPreparedStatement(INSERT_STUDY_GROUP, true);
            preparedInsertCoordinatesStatement = databaseManager.getPreparedStatement(INSERT_COORDINATES, true);
            preparedInsertPersonStatement = databaseManager.getPreparedStatement(INSERT_PERSON, true);

            preparedInsertPersonStatement.setString(1, groupRaw.getGroupAdmin().getName());
            preparedInsertPersonStatement.setTimestamp(2,
                    Timestamp.valueOf(groupRaw.getGroupAdmin().getBirthday()));
            preparedInsertPersonStatement.setLong(3, groupRaw.getGroupAdmin().getWeight());
            preparedInsertPersonStatement.setString(4, groupRaw.getGroupAdmin().getPassportID());

            if (preparedInsertPersonStatement.executeUpdate() == 0)
                throw new SQLException();

            ResultSet generatedPersonKeys = preparedInsertPersonStatement.getGeneratedKeys();
            long userId;
            if (generatedPersonKeys.next()) {
                userId = generatedPersonKeys.getLong(1);
            } else throw new SQLException();
            Main.logger.info("Выполнен запрос INSERT_PERSON.");


            preparedInsertCoordinatesStatement.setDouble(1, groupRaw.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setFloat(2, groupRaw.getCoordinates().getY());

            if (preparedInsertCoordinatesStatement.executeUpdate() == 0)
                throw new SQLException();

            ResultSet generatedCoordinatesKeys = preparedInsertCoordinatesStatement.getGeneratedKeys();
            long coordinatesId = 0;
            if (generatedCoordinatesKeys.next()){
                coordinatesId = generatedCoordinatesKeys.getLong(1);
                //Main.logger.info("Get coordinates key: " + coordinatesId);
            }
            Main.logger.info("Выполнен запрос INSERT_COORDINATES.");


            preparedInsertStudyGroupStatement.setString(1, groupRaw.getName());
            preparedInsertStudyGroupStatement.setLong(2, coordinatesId);
            preparedInsertStudyGroupStatement.setTimestamp(3, Timestamp.valueOf(creationTime));
            preparedInsertStudyGroupStatement.setInt(4, groupRaw.getStudentsCount());
            preparedInsertStudyGroupStatement.setInt(5, groupRaw.getExpelledStudents());
            preparedInsertStudyGroupStatement.setLong(6, groupRaw.getAverageMark());
            preparedInsertStudyGroupStatement.setString(7, groupRaw.getSemesterEnum().toString());
            preparedInsertStudyGroupStatement.setLong(8, userId);
            preparedInsertStudyGroupStatement.setLong(9, databaseUserManager.getUserIdByUsername(user));
            if (preparedInsertStudyGroupStatement.executeUpdate() == 0)
                throw new SQLException();
            ResultSet generatedSGKeys = preparedInsertStudyGroupStatement.getGeneratedKeys();
            int studyGroupId;
            if (generatedSGKeys.next()) {
                studyGroupId = generatedSGKeys.getInt(1);
            } else throw new SQLException();
            Main.logger.info("Выполнен запрос INSERT_STUDY_GROUP.");

            stGroup = new StudyGroup(
                    studyGroupId,
                    groupRaw,
                    user);

            databaseManager.commit();
            return stGroup;
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении группы запросов на добавление нового объекта!");
            databaseManager.rollback();
            return null;
        } finally {
            databaseManager.closePreparedStatement(preparedInsertPersonStatement);
            databaseManager.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseManager.closePreparedStatement(preparedInsertStudyGroupStatement);
            databaseManager.setNormalMode();
        }
    }

    public void updateStudyGroupById(long sgId, RowStudyGroup sgRaw) {
        PreparedStatement preparedUpdateSGNameByIdStatement = null;
        PreparedStatement preparedUpdateSGStudentsCountByIdStatement = null;
        PreparedStatement preparedUpdateSGExpelledStudentsByIdStatement = null;
        PreparedStatement preparedUpdateSGAverageMarkByIdStatement = null;
        PreparedStatement preparedUpdateSGSemesterByIdStatement = null;
        PreparedStatement preparedUpdateSGCoordinatesByIdStatement = null;
        PreparedStatement preparedUpdateSGAdminByIdStatement = null;
        try {
            databaseManager.setCommitMode();
            databaseManager.setSavepoint();

            preparedUpdateSGNameByIdStatement = databaseManager.getPreparedStatement(UPDATE_STUDY_GROUP_NAME_BY_ID, false);
            preparedUpdateSGStudentsCountByIdStatement = databaseManager.getPreparedStatement(UPDATE_STUDY_GROUP_STUDENTS_COUNT_BY_ID, false);
            preparedUpdateSGExpelledStudentsByIdStatement = databaseManager.getPreparedStatement(UPDATE_STUDY_GROUP_EXPELLED_STUDENTS_BY_ID, false);
            preparedUpdateSGAverageMarkByIdStatement = databaseManager.getPreparedStatement(UPDATE_STUDY_GROUP_AVERAGE_MARK_BY_ID, false);
            preparedUpdateSGSemesterByIdStatement = databaseManager.getPreparedStatement(UPDATE_STUDY_GROUP_SEMESTER_BY_ID, false);
            preparedUpdateSGCoordinatesByIdStatement = databaseManager.getPreparedStatement(UPDATE_COORDINATES_BY_ID, false);
            preparedUpdateSGAdminByIdStatement = databaseManager.getPreparedStatement(UPDATE_PERSON_BY_ID, false);

            if (sgRaw.getName() != null) {
                preparedUpdateSGNameByIdStatement.setString(1, sgRaw.getName());
                preparedUpdateSGNameByIdStatement.setLong(2, sgId);
                if (preparedUpdateSGNameByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Main.logger.info("Выполнен запрос UPDATE_STUDY_GROUP_NAME_BY_ID.");
            }
            if (sgRaw.getCoordinates().getX() != -1 || sgRaw.getCoordinates().getY() != -1) {
                Coordinates prevCoord = null;
                if (sgRaw.getCoordinates().getX() == -1 || sgRaw.getCoordinates().getY() == -1)
                    prevCoord = getCoordinatesById(getCoordinatesIdByStudyGroupId(sgId));
                if (sgRaw.getCoordinates().getX() == -1)
                    preparedUpdateSGCoordinatesByIdStatement.setLong(1, prevCoord.getX());
                else
                    preparedUpdateSGCoordinatesByIdStatement.setLong(1, sgRaw.getCoordinates().getX());
                if (sgRaw.getCoordinates().getY() == -1)
                    preparedUpdateSGCoordinatesByIdStatement.setInt(2, prevCoord.getY());
                else
                    preparedUpdateSGCoordinatesByIdStatement.setInt(2, sgRaw.getCoordinates().getY());
                preparedUpdateSGCoordinatesByIdStatement.setLong(3, getCoordinatesIdByStudyGroupId(sgId));
                if (preparedUpdateSGCoordinatesByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Main.logger.info("Выполнен запрос UPDATE_COORDINATES_GROUP_NAME_BY_ID.");
            }
            if (sgRaw.getStudentsCount() != -1) {
                preparedUpdateSGStudentsCountByIdStatement.setInt(1, sgRaw.getStudentsCount());
                preparedUpdateSGStudentsCountByIdStatement.setLong(2, sgId);
                if (preparedUpdateSGStudentsCountByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Main.logger.info("Выполнен запрос UPDATE_STUDENTS_COUNT_BY_ID.");
            }
            if (sgRaw.getExpelledStudents() != -1) {
                preparedUpdateSGExpelledStudentsByIdStatement.setInt(1, sgRaw.getStudentsCount());
                preparedUpdateSGExpelledStudentsByIdStatement.setLong(2, sgId);
                if (preparedUpdateSGExpelledStudentsByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Main.logger.info("Выполнен запрос UPDATE_EXPELLED_STUDENTS.");
            }
            if (sgRaw.getAverageMark() != -1) {
                preparedUpdateSGAverageMarkByIdStatement.setLong(1, sgRaw.getAverageMark());
                preparedUpdateSGAverageMarkByIdStatement.setLong(2, sgId);
                if (preparedUpdateSGAverageMarkByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Main.logger.info("Выполнен запрос UPDATE_AVERAGE_MARK.");
            }
            if (sgRaw.getSemesterEnum() != null) {
                preparedUpdateSGSemesterByIdStatement.setString(1, sgRaw.getSemesterEnum().toString());
                preparedUpdateSGSemesterByIdStatement.setLong(2, sgId);
                if (preparedUpdateSGSemesterByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Main.logger.info("Выполнен запрос UPDATE_SEMESTER.");
            }
            if (sgRaw.getGroupAdmin().getName() != null || sgRaw.getGroupAdmin().getBirthday() != null ||
                    sgRaw.getGroupAdmin().getWeight() != -1 || sgRaw.getGroupAdmin().getPassportID() != null) {
                Person oldAdm = null;
                if (sgRaw.getGroupAdmin().getName() == null || sgRaw.getGroupAdmin().getBirthday() == null ||
                        sgRaw.getGroupAdmin().getWeight() == -1 || sgRaw.getGroupAdmin().getPassportID() == null)
                    oldAdm = getPersonById(getAdminIdByStudyGroupId(sgId));

                if (sgRaw.getGroupAdmin().getName() == null)
                    preparedUpdateSGAdminByIdStatement.setString(1, oldAdm.getName());
                else
                    preparedUpdateSGAdminByIdStatement.setString(1, sgRaw.getGroupAdmin().getName());
                if (sgRaw.getGroupAdmin().getBirthday() == null)
                    preparedUpdateSGAdminByIdStatement.setTimestamp(2, Timestamp.valueOf(oldAdm.getBirthday()));
                else
                    preparedUpdateSGAdminByIdStatement.setTimestamp(2, Timestamp.valueOf(sgRaw.getGroupAdmin().getBirthday()));
                if (sgRaw.getGroupAdmin().getWeight() == -1)
                    preparedUpdateSGAdminByIdStatement.setLong(3, oldAdm.getWeight());
                else
                    preparedUpdateSGAdminByIdStatement.setLong(3, sgRaw.getGroupAdmin().getWeight());
                if (sgRaw.getGroupAdmin().getPassportID() == null)
                    preparedUpdateSGAdminByIdStatement.setString(4, oldAdm.getPassportID());
                else
                    preparedUpdateSGAdminByIdStatement.setString(4, sgRaw.getGroupAdmin().getPassportID());
                preparedUpdateSGAdminByIdStatement.setLong(5, getAdminIdByStudyGroupId(sgId));
                if (preparedUpdateSGAdminByIdStatement.executeUpdate() == 0)
                    throw new SQLException();
                Main.logger.info("Выполнен запрос UPDATE_ADMIN.");
            }
            databaseManager.commit();
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            databaseManager.rollback();
        } finally {
            databaseManager.closePreparedStatement(preparedUpdateSGNameByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateSGStudentsCountByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateSGExpelledStudentsByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateSGAverageMarkByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateSGSemesterByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateSGCoordinatesByIdStatement);
            databaseManager.closePreparedStatement(preparedUpdateSGAdminByIdStatement);
            databaseManager.setNormalMode();
        }
    }

    private long getCoordinatesIdByStudyGroupId(long SGId){
        long CoordinatesId = 0;
        PreparedStatement preparedSelectSGByIdStatement = null;
        try {
            preparedSelectSGByIdStatement = databaseManager.getPreparedStatement(SELECT_STUDY_GROUPS_BY_ID, false);
            preparedSelectSGByIdStatement.setLong(1, SGId);
            ResultSet resultSet = preparedSelectSGByIdStatement.executeQuery();
            Main.logger.info("Выполнен запрос SELECT_STUDY_GROUP_BY_ID.");
            if (resultSet.next()) {
                CoordinatesId = resultSet.getLong(DatabaseManager.STUDY_GROUP_TABLE_COORDINATES_ID);
            } else throw new SQLException();
            //Main.logger.info("Get ID " + CoordinatesId);
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запроса!");
        } finally {
            databaseManager.closePreparedStatement(preparedSelectSGByIdStatement);
        }
        return CoordinatesId;
    }

    private long getAdminIdByStudyGroupId(long sgId){
        long AdminId = 0;
        PreparedStatement preparedSelectSGByIdStatement = null;
        try {
            preparedSelectSGByIdStatement = databaseManager.getPreparedStatement(SELECT_STUDY_GROUPS_BY_ID, false);
            preparedSelectSGByIdStatement.setLong(1, sgId);
            ResultSet resultSet = preparedSelectSGByIdStatement.executeQuery();
            Main.logger.info("Выполнен запрос SELECT_STUDY_GROUP_BY_ID.");
            if (resultSet.next()) {
                AdminId = resultSet.getLong(DatabaseManager.STUDY_GROUP_TABLE_GROUP_ADMIN_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запроса!");
        } finally {
            databaseManager.closePreparedStatement(preparedSelectSGByIdStatement);
        }
        return AdminId;
    }


    public void deleteStudyGroupById(long sgId){
        PreparedStatement preparedDeleteSGByIdStatement = null;
        PreparedStatement preparedDeletePersonById = null;
        PreparedStatement preparedDeleteCoordinatesById = null;
        try {
            preparedDeletePersonById = databaseManager.getPreparedStatement(DELETE_PERSON_BY_ID, false);
            preparedDeletePersonById.setLong(1, getAdminIdByStudyGroupId(sgId));
            if (preparedDeletePersonById.executeUpdate() == 0)
                throw new SQLException();
            Main.logger.info("Выполнен запрос DELETE_PERSON_BY_ID.");

            preparedDeleteCoordinatesById = databaseManager.getPreparedStatement(DELETE_COORDINATES_BY_ID, false);
            preparedDeleteCoordinatesById.setLong(1, getCoordinatesIdByStudyGroupId(sgId));
            if (preparedDeleteCoordinatesById.executeUpdate() == 0)
                throw new SQLException();
            Main.logger.info("Выполнен запрос DELETE_COORDINATES_BY_ID.");

            preparedDeleteSGByIdStatement = databaseManager.getPreparedStatement(DELETE_STUDY_GROUP_BY_ID, false);
            preparedDeleteSGByIdStatement.setLong(1, sgId);
            if (preparedDeleteSGByIdStatement.executeUpdate() == 0)
                throw new SQLException();
            Main.logger.info("Выполнен запрос DELETE_STUDY_GROUP_BY_ID.");

        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запросов!");
        } finally {
            databaseManager.closePreparedStatement(preparedDeleteSGByIdStatement);
        }
    }

    public Stack<StudyGroup> getCollection(){
        Stack<StudyGroup> buffCollection = new Stack<StudyGroup>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = databaseManager.getPreparedStatement(SELECT_ALL_STUDY_GROUPS, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while (resultSet.next()) {
                buffCollection.add(createStudyGroup(resultSet));
            }
        } catch (SQLException exception) {
            Main.logger.error("Ошибка создания коллекции");
        } finally {
            databaseManager.closePreparedStatement(preparedSelectAllStatement);
        }
        return buffCollection;
    }

    private StudyGroup createStudyGroup(ResultSet resultSet){
        try {
            long id = resultSet.getLong(DatabaseManager.STUDY_GROUP_TABLE_ID_COLUMN);
            String name = resultSet.getString(DatabaseManager.STUDY_GROUP_TABLE_NAME_COLUMN);
            Date creationDate = Date.from(resultSet.getTimestamp(DatabaseManager.STUDY_GROUP_TABLE_CREATION_DATE_COLUMN).toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
            int StudentsCount = resultSet.getInt(DatabaseManager.STUDY_GROUP_TABLE_STUDENTS_COUNT_COLUMN);
            int ExpeldStudents = resultSet.getInt(DatabaseManager.STUDY_GROUP_TABLE_EXPELLED_STUDENTS_COLUMN);
            long avMark = resultSet.getLong(DatabaseManager.STUDY_GROUP_TABLE_AVERAGE_MARK_COLUMN);
            Main.logger.info("ищу элемент по имени " + resultSet.getString(DatabaseManager.STUDY_GROUP_TABLE_SEMESTER_COLUMN));
            Semester sem = Semester.valueOf(resultSet.getString(DatabaseManager.STUDY_GROUP_TABLE_SEMESTER_COLUMN).trim());
            Coordinates cor = getCoordinatesById(getCoordinatesIdByStudyGroupId(id));
            Person per = getPersonById(getAdminIdByStudyGroupId(id));
            User us = databaseUserManager.getUserById(resultSet.getLong(DatabaseManager.STUDY_GROUP_TABLE_USER_ID_COLUMN));
            return new StudyGroup((int)id, name.trim(),
                    cor, creationDate, StudentsCount,
                    ExpeldStudents, avMark, sem,
                    per, us);
        } catch (SQLException throwables) {
            Main.logger.error("Ошибка парсинга таблицы");
        }
        return null;
    }

    private Coordinates getCoordinatesById(long Id) {
        Coordinates coordinates = null;
        PreparedStatement preparedSelectCoordinatesByIdStatement = null;
        try {
            preparedSelectCoordinatesByIdStatement =
                    databaseManager.getPreparedStatement(SELECT_COORDINATES_BY_ID, false);
            preparedSelectCoordinatesByIdStatement.setLong(1, Id);
            ResultSet resultSet = preparedSelectCoordinatesByIdStatement.executeQuery();
            Main.logger.info("Выполнен запрос SELECT_COORDINATES_BY_ID.");
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getLong(DatabaseManager.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getInt(DatabaseManager.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_ID!");
        } finally {
            databaseManager.closePreparedStatement(preparedSelectCoordinatesByIdStatement);
        }
        return coordinates;
    }


    private Person getPersonById(long Id) {
        Person person = null;
        PreparedStatement preparedSelectPersonByIdStatement = null;
        try {
            preparedSelectPersonByIdStatement =
                    databaseManager.getPreparedStatement(SELECT_PERSON_BY_ID, false);
            preparedSelectPersonByIdStatement.setLong(1, Id);
            ResultSet resultSet = preparedSelectPersonByIdStatement.executeQuery();
            Main.logger.info("Выполнен запрос SELECT_PERSON_BY_ID.");
            if (resultSet.next()) {
                person = new Person(
                        resultSet.getString(DatabaseManager.PERSON_TABLE_NAME_COLUMN).trim(),
                        resultSet.getTimestamp(DatabaseManager.PERSON_TABLE_BIRTHDAY_COLUMN).toLocalDateTime(),
                        resultSet.getLong(DatabaseManager.PERSON_TABLE_WEIGHT_COLUMN),
                        resultSet.getString(DatabaseManager.PERSON_TABLE_PASSPORT_ID_COLUMN).trim());
            } else throw new SQLException();
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_ID!");
        } finally {
            databaseManager.closePreparedStatement(preparedSelectPersonByIdStatement);
        }
        return person;
    }

    public void clearCollection(User chager) {
        Stack<StudyGroup> list = getCollection();
        for (StudyGroup sg : list) {
            if (chager.getUsername().equals(sg.getUser().getUsername()))
                deleteStudyGroupById(sg.getId());
        }
    }
}
