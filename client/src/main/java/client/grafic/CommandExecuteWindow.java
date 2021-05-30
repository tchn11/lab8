package client.grafic;

import client.Client;
import client.grafic.locals.*;
import messages.AnswerMsg;
import messages.CommandMsg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CommandExecuteWindow extends JFrame {
    private JButton ru = new JButton("RU");
    private JButton enAu = new JButton("EN(AU)");
    private JButton tr = new JButton("TR");
    private JButton hu = new JButton("HU");

    private JLabel infoLabel = new JLabel();
    private JButton returnButton = new JButton();
    private JButton sendButton = new JButton();
    private JTextArea answerEarea = new JTextArea(15, 10);
    private JTextField commandField = new JTextField("", 10);

    Client client;
    Local local;

    private MainWindow mainWindow;

    public CommandExecuteWindow(Client cli, Local loc, MainWindow mn, Rectangle rec){
        super("Command window");

        mainWindow = mn;
        client  = cli;
        local = loc;

        setLocal();

        this.setBounds(rec);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));
        this.setVisible(true);

        ru.addActionListener(new SetRuListener());
        enAu.addActionListener(new SetEnAuListener());
        tr.addActionListener(new SetTrListener());
        hu.addActionListener(new SetHuListener());

        returnButton.addActionListener(new SetReturn());
        sendButton.addActionListener(new SetSend());

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
        box.add(infoLabel);
        box.add(commandField);
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(sendButton);
        answerEarea.setEditable(false);
        box.add(new JScrollPane(answerEarea));
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(returnButton);
        container.add(box);
        container.add(Box.createGlue());
        container.add(Box.createGlue());
        container.add(Box.createGlue());
        container.add(Box.createGlue());

    }

    private void setLocal(){
        returnButton.setText(local.RETURN);
        sendButton.setText(local.SEND);
        infoLabel.setText(local.ENTER_COMMANDS);
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

    private void closeMe(){
        super.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    class SetReturn implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            mainWindow.showMe(getRec());
            closeMe();
        }
    }

    class SetSend implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = commandField.getText();
            if (command.equals(""))
                return;
            String[] sendComm = (command.trim()+ " ").split(" ", 2);
            AnswerMsg answerMsg = client.sendAndRet(new CommandMsg(sendComm[0], sendComm[1], null, client.getUser()));

            answerEarea.setText(answerEarea.getText() +  answerMsg.getMessage());
        }
    }

    public Rectangle getRec(){
        return this.getBounds();
    }
}
