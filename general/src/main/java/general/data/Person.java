package general.data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Person class.
 */
public class Person  implements Serializable {
    private String name; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null, \u0421\u0442\u0440\u043E\u043A\u0430 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C \u043F\u0443\u0441\u0442\u043E\u0439
    private LocalDateTime birthday; //\u041F\u043E\u043B\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null
    private Long weight; //\u041F\u043E\u043B\u0435 \u043D\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null, \u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435 \u043F\u043E\u043B\u044F \u0434\u043E\u043B\u0436\u043D\u043E \u0431\u044B\u0442\u044C \u0431\u043E\u043B\u044C\u0448\u0435 0
    private String passportID; //\u041F\u043E\u043B\u0435 \u043C\u043E\u0436\u0435\u0442 \u0431\u044B\u0442\u044C null

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public Long getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public String getPassportID() {
        return passportID;
    }

    /**
     * Constructor, just set class
     * @param nm Name
     * @param bth Birthday
     * @param wdt Weight
     * @param passId Passport ID
     */
    public Person(String nm, LocalDateTime bth, Long wdt, String passId){
        name = nm;
        birthday = bth;
        weight = wdt;
        passportID = passId;
    }

    @Override
    public String toString() {
        return "{" +
                "\u0438\u043C\u044F = '" + name + '\'' +
                ", \u0414\u0435\u043D\u044C \u0440\u043E\u0436\u0434\u0435\u043D\u0438\u044F = " + birthday.toString() +
                ", \u0412\u0435\u0441 = " + weight.toString() +
                ", \u041D\u043E\u043C\u0435\u0440 \u043F\u0430\u0441\u043F\u043E\u0440\u0442\u0430 = '" + passportID + '\'' +
                '}';
    }
}
