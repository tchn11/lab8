package client.grafic;

import client.Client;
import client.states.LoginStates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import client.grafic.locals.*;

public class Interface extends JFrame {
    private Local local;
    private JButton button = new JButton();

    private JButton ru = new JButton("RU");
    private JButton enAu = new JButton("EN(AU)");
    private JButton tr = new JButton("TR");
    private JButton hu = new JButton("HU");

    private JTextField inputLogin = new JTextField("", 1);
    private JLabel login = new JLabel();
    private JTextField inputPassword = new JTextField("", 1);
    private JLabel password = new JLabel();
    private JCheckBox isRegistered = new JCheckBox();
    private JLabel info = new JLabel("");
    private Client client;

    public Interface(Client cl){
        super("Login window");

        local = new Russian();

        this.setBounds(100,100,600,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));

        client = cl;

        setLocal();

        isRegistered.setSelected(false);

        inputLogin.setMaximumSize(new Dimension(400, 25));
        inputPassword.setMaximumSize(new Dimension(400, 25));
        button.setMinimumSize(new Dimension(300, 30));
        login.setMinimumSize(new Dimension(300, 30));
        password.setMinimumSize(new Dimension(300, 30));

        button.addActionListener(new ButtonLoginPressedListener());

        ru.addActionListener(new SetRuListener());
        enAu.addActionListener(new SetEnAuListener());
        tr.addActionListener(new SetTrListener());
        hu.addActionListener(new SetHuListener());

        info.setForeground(Color.RED);

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
        box.add(login);
        box.add(inputLogin);
        box.add(password);
        box.add(inputPassword);
        box.add(isRegistered);
        box.add(button);
        box.add(info);
        container.add(box);
        container.add(Box.createGlue());
        container.add(Box.createGlue());
        container.add(Box.createGlue());
        container.add(Box.createGlue());
    }

    private void setLocal(){
        button.setText(local.BUTTON);
        login.setText(local.LOGIN);
        password.setText(local.PASSWORD);
        isRegistered.setText(local.ARE_REGISTERED);
        info.setText("");
    }

    class ButtonLoginPressedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            LoginStates ans = client.connectToServer(inputLogin.getText(), inputLogin.getText(), isRegistered.isSelected());
            if (ans.equals(LoginStates.CONNECTION_ERROR))
                info.setText(local.CONNECTION_ERROR);
            if (ans.equals(LoginStates.LOGINED))
                info.setText(local.SUCCESSFUL_LOGIN);
            if (ans.equals(LoginStates.LOGIN_ERROR))
                info.setText(local.LOGIN_ERROR);

        }
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

}
