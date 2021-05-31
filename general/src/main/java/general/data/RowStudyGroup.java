package general.data;

import java.io.Serializable;

/**
 * Class like person, but can be sent and without any data witch will be generated
 */
public class RowStudyGroup  implements Serializable {
    private String name; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null, \u0421\u0442\u0440\u043E\u043A\u0430 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C \u043F\u0443\u0441\u0442\u043E\u0439
    private Coordinates coordinates; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Integer studentsCount; //\u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 0, \u041F\u043E\u043B\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Integer expelledStudents; //\u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 0, \u041F\u043E\u043B\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Long averageMark; //\u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 0, \u041F\u043E\u043B\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Semester semesterEnum; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Person groupAdmin; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null

    /**
     * Constructor, just set class
     * @param nm Name
     * @param coord Coordinates
     * @param studCo Students count
     * @param expSt Expelled students
     * @param avrMa Average mark
     * @param semEn Semester
     * @param gradm Group admin
     */
    public RowStudyGroup(String nm, Coordinates coord, Integer studCo, Integer expSt, Long avrMa, Semester semEn, Person gradm)
    {
        name = nm;
        coordinates = coord;
        studentsCount = studCo;
        expelledStudents = expSt;
        averageMark = avrMa;
        semesterEnum = semEn;
        groupAdmin = gradm;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
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

    @Override
    public String toString() {
        return "\u0423\u0447\u0435\u0431\u043D\u0430\u044F \u0433\u0440\u0443\u043F\u043F\u0430 {" +
                ", \u0438\u043C\u044F = '" + name + '\'' +
                ", \u041A\u043E\u043E\u0440\u0434\u0438\u043D\u0430\u0442\u044B = " + coordinates.toString() +
                ", \u041A\u043E\u043B\u0438\u0447\u0435\u0441\u0442\u0432\u043E \u0441\u0442\u0443\u0434\u0435\u043D\u0442\u043E\u0432 = " + studentsCount.toString() +
                ", \u041A\u043E\u043B\u0438\u0447\u0435\u0441\u0442\u0432\u043E \u043E\u0442\u0447\u0438\u0441\u043B\u0435\u043D\u043D\u044B\u0445 \u0441\u0442\u0443\u0434\u0435\u043D\u0442\u043E\u0432 = " + expelledStudents.toString() +
                ", \u0421\u0440\u0435\u0434\u043D\u044F\u044F \u043E\u0446\u0435\u043D\u043A\u0430 = " + averageMark.toString() +
                ", \u0421\u0435\u043C\u0435\u0441\u0442\u0440 = " + semesterEnum.toString() +
                ", \u0410\u0434\u043C\u0438\u043D = " + groupAdmin.toString() +
                '}';
    }
}
