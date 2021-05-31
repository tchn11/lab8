package general.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Main collection element, study group
 */
public class StudyGroup implements Serializable {
    private Integer id; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null, \u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 0, \u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u044D\u0442\u043E\u0433\u043E \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0443\u043D\u0438\u043A\u0430\u043B\u044C\u043D\u044B\u043C, \u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u044D\u0442\u043E\u0433\u043E \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0433\u0435\u043D\u0435\u0440\u0438\u0440\u043E\u0432\u0430\u0442\u044C\u0441\u044F \u0430\u0432\u0442\u043E\u043C\u0430\u0442\u0438\u0447\u0435\u0441\u043A\u0438
    private String name; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null, \u0421\u0442\u0440\u043E\u043A\u0430 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C \u043F\u0443\u0441\u0442\u043E\u0439
    private Coordinates coordinates; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Date creationDate; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null, \u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u044D\u0442\u043E\u0433\u043E \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0433\u0435\u043D\u0435\u0440\u0438\u0440\u043E\u0432\u0430\u0442\u044C\u0441\u044F \u0430\u0432\u0442\u043E\u043C\u0430\u0442\u0438\u0447\u0435\u0441\u043A\u0438
    private Integer studentsCount; //\u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 0, \u041F\u043E\u043B\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Integer expelledStudents; //\u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 0, \u041F\u043E\u043B\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Long averageMark; //\u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 0, \u041F\u043E\u043B\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Semester semesterEnum; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Person groupAdmin; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private User user;

    /**
     * Constructor, just set class
     * @param Id ID
     * @param nm Name
     * @param coord Coordinates
     * @param crDt Creation date
     * @param studCo Students count
     * @param expSt Expelled students
     * @param avrMa Average mark
     * @param semEn Semester
     * @param gradm Group admin
     */
    public StudyGroup(Integer Id, String nm, Coordinates coord, Date crDt, Integer studCo, Integer expSt, Long avrMa, Semester semEn, Person gradm, User us)
    {
        id = Id;
        name = nm;
        coordinates = coord;
        creationDate = crDt;
        studentsCount = studCo;
        expelledStudents = expSt;
        averageMark = avrMa;
        semesterEnum = semEn;
        groupAdmin = gradm;
        user = us;
    }

    public StudyGroup(Integer Id,RowStudyGroup sg, User us){
        id = Id;
        name = sg.getName();
        coordinates = sg.getCoordinates();
        creationDate = new Date();
        studentsCount = sg.getStudentsCount();
        expelledStudents = sg.getExpelledStudents();
        averageMark = sg.getAverageMark();
        semesterEnum = sg.getSemesterEnum();
        groupAdmin = sg.getGroupAdmin();
        user = us;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Integer getStudentsCount() {
        return studentsCount;
    }

    public Integer getExpelledStudents() {
        return expelledStudents;
    }

    public Long getAverageMark() {
        return averageMark;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    public User getUser() {
        return user;
    }

    public long getNum(){
        return (id + studentsCount + expelledStudents + averageMark);
    }

    @Override
    public String toString() {
        return "\u0423\u0447\u0435\u0431\u043D\u0430\u044F \u0433\u0440\u0443\u043F\u043F\u0430 {" +
                "ID = " + id.toString() + "\n" +
                "\u0438\u043C\u044F = '" + name + '\'' + "\n"+
                "\u041A\u043E\u043E\u0440\u0434\u0438\u043D\u0430\u0442\u044B = " + coordinates.toString() + "\n"+
                "\u0414\u0430\u0442\u0430 \u0441\u043E\u0437\u0434\u0430\u043D\u0438\u044F = " + creationDate.toString() + "\n"+
                "\u041A\u043E\u043B\u0438\u0447\u0435\u0441\u0442\u0432\u043E \u0441\u0442\u0443\u0434\u0435\u043D\u0442\u043E\u0432 = " + studentsCount.toString() + "\n"+
                "\u041A\u043E\u043B\u0438\u0447\u0435\u0441\u0442\u0432\u043E \u043E\u0442\u0447\u0438\u0441\u043B\u0435\u043D\u043D\u044B\u0445 \u0441\u0442\u0443\u0434\u0435\u043D\u0442\u043E\u0432 = " + expelledStudents.toString() + "\n"+
                "\u0421\u0440\u0435\u0434\u043D\u044F\u044F \u043E\u0446\u0435\u043D\u043A\u0430 = " + averageMark.toString() + "\n"+
                "\u0421\u0435\u043C\u0435\u0441\u0442\u0440 = " + semesterEnum.toString() + "\n"+
                "\u0410\u0434\u043C\u0438\u043D = " + groupAdmin.toString() + "\n"+
                "\u0414\u043E\u0431\u0430\u0432\u0438\u043B \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044C = " + user.getUsername() + "\n"+
                '}';
    }
}
