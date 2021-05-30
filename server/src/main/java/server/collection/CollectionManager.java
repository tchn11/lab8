package server.collection;

import general.data.StudyGroup;
import general.data.User;
import messages.AnswerMsg;
import server.Main;
import server.connection.DatabaseCollectionManager;

import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Operates collection.
 */
public class CollectionManager {
    private Stack<StudyGroup> myCollection;
    private java.time.LocalDateTime initDate;

    DatabaseCollectionManager databaseCollectionManager;

    /**
     * Constructor, set start values
     */
    public CollectionManager(DatabaseCollectionManager db)
    {
        initDate = java.time.LocalDateTime.now();
        myCollection = db.getCollection();
        databaseCollectionManager = db;
    }


    public void update(){
        myCollection = databaseCollectionManager.getCollection();
    }

    /**
     * Return collection
     * @return Collection
     */
    public Stack<StudyGroup> getMyCollection()
    {
        return myCollection;
    }

    /**
     * Add element to collection
     * @param sg Element of collection
     */
    public void add(StudyGroup sg){
        myCollection.add(sg);
        Main.logger.info("\u0414\u043E\u0431\u0430\u0432\u043B\u0435\u043D \u044D\u043B\u0435\u043C\u0435\u043D\u0442:");
        Main.logger.info(sg.toString());
    }

    /**
     * Generate next ID for new elements
     * @return ID
     */
    public int generateNextId(){
        if (myCollection.isEmpty())
            return 1;
        else
            return myCollection.lastElement().getId() + 1;
    }

    /**
     * Get all elements by index
     * @return List of elements
     */
    public String getList(){
        if (myCollection.empty())
            return "\u041A\u043E\u043B\u0435\u043A\u0446\u0438\u044F \u043F\u0443\u0441\u0442\u0430";
        return myCollection.stream()
                .reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

    /**
     * Get information about collection
     * @return Information
     */
    public String getInfo(){
        return "Stack \u0438\u0437 \u044D\u043B\u0435\u043C\u0435\u043D\u0442\u043E\u0432 StudyGroup, \u0440\u0430\u0437\u043C\u0435\u0440: " + Integer.toString(myCollection.size()) + ", \u0434\u0430\u0442\u0430 \u0438\u043D\u0438\u0446\u0430\u043B\u0438\u0437\u0430\u0446\u0438\u0438: " + initDate.toString();
    }

    /**
     * Update element
     * @param sg Element
     */
    public void updateElement(StudyGroup sg){
        Stream str = myCollection.stream()
                .map(studyGroup -> studyGroup.getId().equals(sg.getId()) ? sg : studyGroup);

        str.collect(Collectors.toCollection(Stack::new));
    }

    /**
     * Give info about is this ID used
     * @param ID ID
     * @return is it used or not
     */
    public boolean checkID(Integer ID){
        for (StudyGroup sg : myCollection)
        {
            if (sg.getId() == ID)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Delete element by ID
     * @param id ID
     */
    public void removeID(Integer id){
        myCollection.removeIf(studyGroup -> studyGroup.getId().equals(id));
    }

    /**
     * Get size of collection
     * @return Size of collection
     */
    public int getSize(){
        return myCollection.size();
    }

    /**
     * Remove element by Index
     * @param index Index
     */
    public void removeByIndex(int index){
        myCollection.remove(index);

    }
    /**
     * Add if ID of element bigger then max in collection
     * @param sg Element
     */
    public void AddIfMax(StudyGroup sg) {
        if (myCollection.stream().map(studyGroup -> studyGroup.getNum()).max(Long::compareTo).get() < sg.getNum())
            myCollection.add(sg);
    }

    /**
     * Return list of element that have ExpelledStudents less then argument
     * @param max Max ExpelledStudents
     * @return List of elements
     */
    public String LessExpelled(int max){
        return myCollection.stream()
                .filter(studyGroup -> studyGroup.getExpelledStudents() < max)
                .reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2)
                .trim();
    }

    /**
     * Return list of elements Name of that starts with argument
     * @param start Start of name
     * @return List of elements
     */
    public String StartsWithName(String start){
        return myCollection.stream()
                .filter(studyGroup -> studyGroup.getName().startsWith(start))
                .reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2)
                .trim();
    }

    /**
     * Delete elements StudentsCount is equals argument
     * @param num StudentsCount that should be deleted
     */
    public void DeleteByStudentsCount(int num, User changer, AnswerMsg answerMsg){
        for (StudyGroup sg : myCollection){
            if (sg.getStudentsCount() == num){
                if (changer.getUsername().equals(sg.getUser().getUsername())) {
                    databaseCollectionManager.deleteStudyGroupById(sg.getId());
                    answerMsg.AddAnswer("\u0423\u0434\u0430\u043B\u0435\u043D\u0430 \u0433\u0440\u0443\u043F\u043F\u0430: " + sg.getName());
                }else {
                    answerMsg.AddAnswer("\u0413\u0440\u0443\u043F\u043F\u043E\u0439 " + sg.getName() + " \u0432\u043B\u0430\u0434\u0435\u0435\u0442 " + sg.getUser().getUsername() + " \u043F\u043E\u044D\u0442\u043E\u043C\u0443 \u043E\u043D\u0430 \u043D\u0435 \u0431\u044B\u043B\u0430 \u0443\u0434\u0430\u043B\u0435\u043D\u0430!");
                }
            }
        }
    }

    public StudyGroup getById(int Id){
        for (StudyGroup sg : myCollection){
            if (sg.getId() == Id){
                return sg;
            }
        }
        return null;
    }
}
