package client.grafic;

import general.data.StudyGroup;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Stack;

public class TablePattern extends AbstractTableModel {
    Stack<StudyGroup> tableData;
    String[] names;
    boolean[] isSelected;

    @Override
    public int getRowCount() {
        return tableData.size();
    }

    @Override
    public String getColumnName(int col) {
        return names[col];
    }

    @Override
    public int getColumnCount() {
        return names.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        StudyGroup studyGroup = tableData.get(rowIndex);
        switch (columnIndex){
            case 0:
                return studyGroup.getId();
            case 1:
                return studyGroup.getName();
            case 2:
                return studyGroup.getCoordinates().toString();
            case 3:
                return studyGroup.getCreationDate();
            case 4:
                return studyGroup.getStudentsCount();
            case 5:
                return studyGroup.getExpelledStudents();
            case 6:
                return studyGroup.getAverageMark();
            case 7:
                return studyGroup.getSemesterEnum().toString();
            case 8:
                return studyGroup.getGroupAdmin().getName();
            case 9:
                return studyGroup.getGroupAdmin().getBirthday();
            case 10:
                return studyGroup.getGroupAdmin().getWeight();
            case 11:
                return studyGroup.getGroupAdmin().getPassportID();
            case 12:
                return studyGroup.getUser().getUsername();
        }
        return null;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Date.class;
            case 4:
                return Integer.class;
            case 5:
                return Integer.class;
            case 6:
                return Integer.class;
            case 7:
                return String.class;
            case 8:
                return String.class;
            case 9:
                return LocalDateTime.class;
            case 10:
                return Integer.class;
            case 11:
                return String.class;
            case 12:
                return String.class;
        }
        return Object.class;
    }

    public void setTableData(Stack<StudyGroup> tableData) {
        this.tableData = tableData;
    }
}
