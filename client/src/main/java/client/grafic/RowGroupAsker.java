package client.grafic;

import client.Client;
import client.console.ConsoleManager;
import client.grafic.locals.*;
import general.data.Coordinates;
import general.data.Person;
import general.data.RowStudyGroup;
import general.data.Semester;
import messages.AnswerMsg;
import messages.CommandMsg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RowGroupAsker extends JFrame {

    private JLabel nameLabel = new JLabel();
    private JTextField nameTextField = new JTextField();

    private JLabel xLabel = new JLabel();
    private JTextField xTextField = new JTextField();

    private JLabel yLabel = new JLabel();
    private JTextField yTextField = new JTextField();

    private JLabel studentsCountLabel = new JLabel();
    private JTextField studentsCountTextField = new JTextField();

    private JLabel expelledStudentsCountLabel = new JLabel();
    private JTextField expelledStudentsTextField = new JTextField();

    private JLabel averageMarkCountLabel = new JLabel();
    private JTextField averageMarkTextField = new JTextField();

    private JLabel semesterLabel = new JLabel();
    private JTextField semesterTextField = new JTextField();

    private JLabel adminNameLabel = new JLabel();
    private JTextField adminNameTextField = new JTextField();

    private JLabel adminBirthdayLabel = new JLabel();
    private JTextField adminBirthdayTextField = new JTextField();

    private JLabel adminWeightLabel = new JLabel();
    private JTextField adminWeightTextField = new JTextField();

    private JLabel adminPassportIDLabel = new JLabel();
    private JTextField adminPassportIDTextField = new JTextField();

    private JButton sendButton = new JButton();

    private JButton ru = new JButton("RU");
    private JButton enAu = new JButton("EN(AU)");
    private JButton tr = new JButton("TR");
    private JButton hu = new JButton("HU");

    Local local;
    CommandMsg commandMsg;
    Client client;
    CommandExecuteWindow commandExecuteWindow;
    boolean sendNotFull;

    public RowGroupAsker(Local loc, CommandMsg msg, Client cli, CommandExecuteWindow com, boolean sendNF){
        super("Row group asker");

        sendNotFull = sendNF;
        commandMsg = msg;
        local = loc;
        client = cli;
        commandExecuteWindow = com;

        setLocal();

        this.setBounds(10, 10, 650, 650);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));

        ru.addActionListener(new SetRuListener());
        enAu.addActionListener(new SetEnAuListener());
        tr.addActionListener(new SetTrListener());
        hu.addActionListener(new SetHuListener());

        nameTextField.setMaximumSize(new Dimension(800,30));
        xTextField.setMaximumSize(new Dimension(800,30));
        yTextField.setMaximumSize(new Dimension(800,30));
        studentsCountTextField.setMaximumSize(new Dimension(800,30));
        expelledStudentsTextField.setMaximumSize(new Dimension(800,30));
        averageMarkTextField.setMaximumSize(new Dimension(800,30));
        semesterTextField.setMaximumSize(new Dimension(800,30));
        adminNameTextField.setMaximumSize(new Dimension(800,30));
        adminBirthdayTextField.setMaximumSize(new Dimension(800,30));
        adminWeightTextField.setMaximumSize(new Dimension(800,30));
        adminPassportIDTextField.setMaximumSize(new Dimension(800,30));

        sendButton.addActionListener(new SetSendListener());

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(1, 3));

        container.add(Box.createGlue());

        Box box = Box.createVerticalBox();
        box.add(Box.createGlue());

        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(nameLabel);
        box.add(nameTextField);

        box.add(xLabel);
        box.add(xTextField);

        box.add(yLabel);
        box.add(yTextField);

        box.add(studentsCountLabel);
        box.add(studentsCountTextField);

        box.add(expelledStudentsCountLabel);
        box.add(expelledStudentsTextField);

        box.add(averageMarkCountLabel);
        box.add(averageMarkTextField);

        box.add(semesterLabel);
        box.add(semesterTextField);

        box.add(adminNameLabel);
        box.add(adminNameTextField);

        box.add(adminBirthdayLabel);
        box.add(adminBirthdayTextField);

        box.add(adminWeightLabel);
        box.add(adminWeightTextField);

        box.add(adminPassportIDLabel);
        box.add(adminPassportIDTextField);

        box.add(sendButton);

        box.add(Box.createGlue());

        container.add(box);
        Box langBox = Box.createVerticalBox();
        ru.setAlignmentX(Component.RIGHT_ALIGNMENT);
        enAu.setAlignmentX(Component.RIGHT_ALIGNMENT);
        tr.setAlignmentX(Component.RIGHT_ALIGNMENT);
        hu.setAlignmentX(Component.RIGHT_ALIGNMENT);
        langBox.setAlignmentX(Component.RIGHT_ALIGNMENT);

        langBox.add(ru);
        langBox.add(enAu);
        langBox.add(tr);
        langBox.add(hu);
        container.add(langBox);

    }

    private void setLocal(){

        nameLabel.setText(local.ENTER_NAME);

        xLabel.setText(local.ENTER_X);

        yLabel.setText(local.ENTER_Y);

        studentsCountLabel.setText(local.ENTER_STUDENTS_COUNT);

        expelledStudentsCountLabel.setText(local.ENTER_EXPELLED_STUDENTS);

        averageMarkCountLabel.setText(local.ENTER_AVEREGE_MARK);

        semesterLabel.setText(local.ENTER_SEMESTER + " " + Semester.nameList());

        adminNameLabel.setText(local.ENTER_ADMIN_NAME);

        adminBirthdayLabel.setText(local.ENTER_ADMIN_BIRHDAY + " " + local.TIME_DATE_FORMAT);

        adminWeightLabel.setText(local.ENTER_ADMIN_WEIGTHT);

        adminPassportIDLabel.setText(local.ENTER_PASSPORT_ID);

        sendButton.setText(local.SEND_BUTTON);
    }

    class SetRuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            local = new Russian();
            setLocal();
        }
    }

    class SetEnAuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            local = new English();
            setLocal();
        }
    }

    class SetTrListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            local = new Turkish();
            setLocal();
        }
    }

    class SetHuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            local = new Hungarian();
            setLocal();
        }
    }

    class SetSendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean canSend = true;

            Color dark_green = new Color(0, 120, 0);

            String name = nameTextField.getText().trim();//Поле не может быть null, Строка не может быть пустой
            if (name.equals("")){
                canSend = false;
                nameLabel.setForeground(Color.RED);
                name = null;
            }else{
                nameLabel.setForeground(dark_green);
            }

            long x = -1; //Максимальное значение поля: 752

            try {
                x = Long.parseLong(xTextField.getText().trim());
                if (x > 752){
                    canSend = false;
                    xLabel.setForeground(Color.RED);
                } else {
                    xLabel.setForeground(dark_green);
                }
            } catch (Exception exc){
                canSend = false;
                xLabel.setForeground(Color.RED);
            }
            int y = -1;
            try{
                y = Integer.parseInt(yTextField.getText().trim());
                yLabel.setForeground(dark_green);
            } catch (Exception exc){
                canSend = false;
                yLabel.setForeground(Color.RED);
            }

            int studentsCount = -1; //Значение поля должно быть больше 0, Поле может быть null
            try{
                studentsCount = Integer.parseInt(studentsCountTextField.getText().trim());
                if (studentsCount < 0){
                    studentsCountLabel.setForeground(Color.RED);
                    canSend = false;
                }
                else{
                    studentsCountLabel.setForeground(dark_green);
                }
            } catch (Exception exc){
                canSend = false;
                studentsCountLabel.setForeground(Color.RED);
            }

            int expelledStudents = -1; //Значение поля должно быть больше 0, Поле может быть null

            try{
                expelledStudents = Integer.parseInt(expelledStudentsTextField.getText().trim());
                if (expelledStudents < 0){
                    expelledStudentsCountLabel.setForeground(Color.RED);
                    canSend = false;
                }
                else{
                    expelledStudentsCountLabel.setForeground(dark_green);
                }
            } catch (Exception exc){
                canSend = false;
                expelledStudentsCountLabel.setForeground(Color.RED);
            }

            long averageMark = -1; //Значение поля должно быть больше 0, Поле может быть null

            try{
                averageMark = Long.parseLong(averageMarkTextField.getText().trim());
                if (averageMark < 0){
                    averageMarkCountLabel.setForeground(Color.RED);
                    canSend = false;
                }
                else{
                    averageMarkCountLabel.setForeground(dark_green);
                }
            } catch (Exception exc){
                canSend = false;
                averageMarkCountLabel.setForeground(Color.RED);
            }

            Semester semesterEnum = null; //Поле не может быть null
            try{
                semesterEnum = Semester.valueOf(semesterTextField.getText().trim());
                semesterLabel.setForeground(dark_green);
            }catch (Exception exc){
                semesterLabel.setForeground(Color.RED);
                canSend = false;
            }

            String admName = adminNameTextField.getText().trim();
            if (admName.trim().equals("")){
                canSend = false;
                adminNameLabel.setForeground(Color.RED);
                admName = null;
            }else{
                adminNameLabel.setForeground(dark_green);
            }

            LocalDateTime dateTime = null;

            try{
                dateTime = LocalDateTime.parse(adminBirthdayTextField.getText().trim(), DateTimeFormatter.ofPattern(local.TIME_DATE_FORMAT));
                if (dateTime == null)
                    throw new Exception();
                adminBirthdayLabel.setForeground(dark_green);
            }catch (Exception exc){
                dateTime = null;
                adminBirthdayLabel.setForeground(Color.RED);
                canSend = false;
            }

            long adminWeight = -1;
            try{
                adminWeight = Long.parseLong(adminWeightTextField.getText().trim());
                if (adminWeight < 0){
                    adminWeightLabel.setForeground(Color.RED);
                    canSend = false;
                }
                else{
                    adminWeightLabel.setForeground(dark_green);
                }
            }catch (Exception exc){
                adminWeightLabel.setForeground(Color.RED);
                canSend = false;
            }


            String adminPassport = adminPassportIDTextField.getText();
            if (adminPassport.equals("")){
                canSend = false;
                adminPassport = null;
                adminPassportIDLabel.setForeground(Color.RED);
            }else{
                adminPassportIDLabel.setForeground(dark_green);
            }

            if (canSend || sendNotFull){
                RowStudyGroup rowStudyGroup = new RowStudyGroup(name, new Coordinates(x, y),
                        studentsCount, expelledStudents, averageMark, semesterEnum,
                        new Person(admName, dateTime, adminWeight, adminPassport));
                commandMsg.setCommandObjectArgument(rowStudyGroup);
                AnswerMsg answerMsg = client.sendAndRet(commandMsg);
                if (commandExecuteWindow != null){
                    commandExecuteWindow.addText(answerMsg.getMessage());
                }
                closeMe();
            }
        }
    }

    private void closeMe(){
        super.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
