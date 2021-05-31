package client.grafic;

import client.Client;
import client.console.ConsoleManager;
import client.grafic.locals.*;
import general.data.StudyGroup;
import messages.CommandMsg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

public class VisualWindow extends JFrame {
    private JButton ru = new JButton("RU");
    private JButton enAu = new JButton("EN(AU)");
    private JButton tr = new JButton("TR");
    private JButton hu = new JButton("HU");

    private JPanel map= new JPanel();
    private JButton returnButtn = new JButton();
    private Box legnd;
    private JLabel legendLabel = new JLabel();
    private JTextArea InfoEarea = new JTextArea();
    private JLabel msg = new JLabel();

    Local local;
    MainWindow mainWindow;
    Client client;

    Stack<StudyGroup> collection;
    Stack<StudyGroup> oldCollection;

    boolean isWork;
    Thread th;
    Rectangle oldRec;

    public VisualWindow(Client cli, Local loc, MainWindow mn, Rectangle rec){
        super("Command window");

        isWork = true;

        mainWindow = mn;
        client  = cli;
        local = loc;

        setLocal();

        this.setBounds(rec);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));
        this.setVisible(true);

        legnd = Box.createVerticalBox();
        InfoEarea.setEditable(false);
        legnd.setMaximumSize(new Dimension(200, 500));
        InfoEarea.setMaximumSize(new Dimension(200, 500));
        InfoEarea.setLineWrap(true);

        map.addMouseListener(new PanelMous());

        map.setBackground(Color.WHITE);
        map.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        ru.addActionListener(new SetRuListener());
        enAu.addActionListener(new SetEnAuListener());
        tr.addActionListener(new SetTrListener());
        hu.addActionListener(new SetHuListener());

        returnButtn.addActionListener(new SetReturn());

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(1, 3));

        Box infoBox = Box.createVerticalBox();
        infoBox.add(returnButtn);
        infoBox.add(legendLabel);
        infoBox.add(new JScrollPane(legnd));
        infoBox.add(new JScrollPane(InfoEarea));
        infoBox.add(msg);
        container.add(infoBox);

        container.add(map);

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

        th = new Thread(()->{
            while (isWork) {

                CommandMsg commandMsg = new CommandMsg("get_collection", "", null, client.getUser());
                commandMsg.makeSystem();
                collection = (Stack<StudyGroup>) client.sendAndRet(commandMsg).getObjAnswer();
                if (!collection.equals(oldCollection) || !this.getRec().equals(oldRec)) {
                    synchronized (this) {
                        ArrayList<String> users = new ArrayList<>();
                        Graphics graphics = map.getGraphics();
                        graphics.setColor(Color.white);
                        graphics.fillRect(2, 2, map.getWidth() - 3, map.getHeight() - 3);
                        //graphics.clearRect(0, 0, map.getWidth(), map.getHeight());
                        for (StudyGroup studyGroup : collection) {
                            if (!users.contains(studyGroup.getUser().getUsername().trim())) {
                                users.add(studyGroup.getUser().getUsername().trim());
                            }
                            graphics.setColor(colorFromString(studyGroup.getUser().getUsername()));
                            graphics.fillRect((int) studyGroup.getCoordinates().getX(), studyGroup.getCoordinates().getY(), (studyGroup.getStudentsCount() - studyGroup.getExpelledStudents()) / 2, (studyGroup.getStudentsCount() - studyGroup.getExpelledStudents()) / 2);
                        }
                        map.revalidate();
                        legnd.removeAll();
                        for (String user : users) {
                            JLabel jlab = new JLabel();
                            jlab.setText(user);
                            jlab.setForeground(colorFromString(user));
                            legnd.add(jlab);
                        }
                        legnd.revalidate();
                        oldCollection = collection;
                        oldRec = this.getRec();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ConsoleManager.print("error");
                }
            }
        });
        th.start();
    }

    private void setLocal(){
        returnButtn.setText(local.RETURN);
        legendLabel.setText(local.MAP_LEGEND);
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

    class PanelMous extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            synchronized (this) {
                StudyGroup minStudyGroup = null;
                double minRast = 10000000;
                for (StudyGroup sg : collection) {
                    double rast = Math.sqrt(Math.pow(e.getX() - sg.getCoordinates().getX(), 2) + Math.pow(e.getY() - sg.getCoordinates().getY(), 2));
                    if (minRast > rast){
                        minRast = rast;
                        minStudyGroup = sg;
                    }
                }
                if (minStudyGroup != null){
                    InfoEarea.setText(minStudyGroup.toString());
                }
                else{
                    msg.setText("Error, empty collection");
                }
            }
        }
    }

    public Color colorFromString(String str){
        byte[] bytes = str.getBytes();
        long summ = 0;
        for (byte bt:bytes){
            summ += bt*4;
        }
        return new Color((float)((summ%255)/255.0), (float)(((summ/255)%255)/255.0), (float)(((summ/255/255)%255)/255.0));
    }

    class SetReturn implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            mainWindow.showMe(getRec(), local);
            closeMe();
        }
    }

    public Rectangle getRec(){
        return this.getBounds();
    }

    private void closeMe(){
        isWork = false;
        try {
            th.join();
        } catch (InterruptedException e) {
            ConsoleManager.print("Error");
        }
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
