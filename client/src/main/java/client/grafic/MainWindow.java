package client.grafic;

import client.Client;
import client.Main;
import client.grafic.locals.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    Local local;

    private JLabel username = new JLabel();

    private JButton ru = new JButton("RU");
    private JButton enAu = new JButton("EN(AU)");
    private JButton tr = new JButton("TR");
    private JButton hu = new JButton("HU");

    private JButton commandMode = new JButton();
    private JButton visualMode = new JButton();
    private JButton tableMode = new JButton();

    Client client;

    public MainWindow(Local loc, Client cli, Rectangle rec){
        super("Main window");

        client = cli;

        local = loc;

        setLocal();

        this.setBounds(rec);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));

        ru.addActionListener(new SetRuListener());
        enAu.addActionListener(new SetEnAuListener());
        tr.addActionListener(new SetTrListener());
        hu.addActionListener(new SetHuListener());

        commandMode.addActionListener(new SetCom());
        visualMode.addActionListener(new SetViz());
        tableMode.addActionListener(new SetTab());

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 3));

        container.add(Box.createGlue());

        container.add(Box.createGlue());

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

        container.add(Box.createGlue());

        Box box = Box.createVerticalBox();
        username.setText(client.getUser().getUsername());
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(username);
        commandMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        commandMode.setSize(new Dimension(500, 40));
        box.add(commandMode);
        visualMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        visualMode.setSize(new Dimension(500, 40));
        box.add(visualMode);
        tableMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        tableMode.setSize(new Dimension(500, 40));
        box.add(tableMode);
        container.add(box);

        container.add(Box.createGlue());
        container.add(Box.createGlue());
        container.add(Box.createGlue());
        container.add(Box.createGlue());

    }

    private void setLocal(){
        commandMode.setText(local.COMMAND_MODE);
        visualMode.setText(local.VISUALIZATION);
        tableMode.setText(local.TABLE_MODE);
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

    class SetViz implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        }
    }

    class SetCom implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CommandExecuteWindow com = new CommandExecuteWindow(client, local, getMe(), getRec());
            hideMe();
        }
    }

    class SetTab implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        }
    }
    private void hideMe(){
        this.setVisible(false);
    }
    public void showMe(Rectangle size){
        this.setBounds(size);
        this.setVisible(true);
    }

    private MainWindow getMe(){
        return this;
    }

    public Rectangle getRec(){
        return this.getBounds();
    }

}
