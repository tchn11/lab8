package client.grafic;

import client.Client;
import client.console.ConsoleManager;
import client.grafic.locals.*;
import general.data.StudyGroup;
import messages.CommandMsg;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.PatternSyntaxException;

public class TableWindow extends JFrame {
    private JButton ru = new JButton("RU");
    private JButton enAu = new JButton("EN(AU)");
    private JButton tr = new JButton("TR");
    private JButton hu = new JButton("HU");

    private JButton returnButtn = new JButton();
    private JButton deleteButtn = new JButton();
    private JButton addButtn = new JButton();
    private JTextField filterField = new JTextField();
    private JButton filterButton = new JButton();

    private JTable table;

    Local local;
    MainWindow mainWindow;
    Client client;

    Stack<StudyGroup> collection;
    Stack<StudyGroup> oldCollection;

    boolean isWork;
    Thread th;

    String[] names;

    private TableRowSorter<TablePattern> sorter;

    public TableWindow(Client cli, Local loc, MainWindow mn, Rectangle rec) {
        super("Command window");

        isWork = true;

        mainWindow = mn;
        client = cli;
        local = loc;

        setLocal();

        this.setBounds(rec);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));
        this.setVisible(true);

        ru.addActionListener(new SetRuListener());
        enAu.addActionListener(new SetEnAuListener());
        tr.addActionListener(new SetTrListener());
        hu.addActionListener(new SetHuListener());

        CommandMsg commandMsg2 = new CommandMsg("get_collection", "", null, client.getUser());
        commandMsg2.makeSystem();
        collection = (Stack<StudyGroup>) client.sendAndRet(commandMsg2).getObjAnswer();

        addButtn.addActionListener(new AddListener());
        returnButtn.addActionListener(new SetReturn());
        deleteButtn.addActionListener(new RemoveListener());
        filterButton.addActionListener(new FilterListener());

        TablePattern tablePattern2 = new TablePattern();
        tablePattern2.setTableData(collection);
        tablePattern2.setNames(names);
        table = new JTable(tablePattern2);
        sorter = new TableRowSorter<TablePattern>(tablePattern2);
        table.setRowSorter(sorter);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1));


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

        container.add(new JScrollPane(table));

        Box box = Box.createHorizontalBox();
        box.add(Box.createGlue());
        Box inBOx = Box.createVerticalBox();
        Box topBox = Box.createHorizontalBox();
        topBox.add(returnButtn);
        topBox.add(deleteButtn);
        topBox.add(addButtn);
        inBOx.add(topBox);
        Box downBox = Box.createHorizontalBox();
        downBox.add(filterButton);
        filterField.setMinimumSize(new Dimension(200, 35));
        filterField.setMaximumSize(new Dimension(300, 35));
        downBox.add(filterField);
        inBOx.add(downBox);
        box.add(inBOx);
        box.add(Box.createGlue());
        container.add(box);


        th = new Thread(()->{
            while (isWork) {

                CommandMsg commandMsg = new CommandMsg("get_collection", "", null, client.getUser());
                commandMsg.makeSystem();
                collection = (Stack<StudyGroup>) client.sendAndRet(commandMsg).getObjAnswer();
                if (!collection.equals(oldCollection)) {
                    synchronized (this) {
                        TablePattern tablePattern = new TablePattern();
                        tablePattern.setTableData(collection);
                        tablePattern.setNames(names);
                        table.setModel(tablePattern);
                        sorter = new TableRowSorter<TablePattern>(tablePattern);
                        table.setRowSorter(sorter);
                        table.revalidate();

                        oldCollection = collection;
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    ConsoleManager.print("error");
                }
            }
        });
        th.start();

    }
    private void setLocal(){
        names = new String[]{
                "id",
                local.NAME_COLUMN,
                local.COORDINATES_COLUMN,
                local.ADD_DATE_COLUMN,
                local.COUNT_COLUMN,
                local.EXPELLED_COLUMN,
                local.AVEREGE_COLUMN,
                local.SEMESTER_COLUMN,
                local.ADMIN_NAME_COLUMN,
                local.ADMIN_BIRTHDAY_COLUMN,
                local.ADMIN_WEIGHT_COLUMN,
                local.ADMIN_PASSPORT_COLUMN,
                local.OWNER_COLUMN
        };

        returnButtn.setText(local.RETURN);

        addButtn.setText(local.ADD_BUTTON);

        deleteButtn.setText(local.REMOVE_BUTTON);

        filterButton.setText(local.FILTER_BUTTON);
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

    class SetRuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            local = new Russian();
            setLocal();
            synchronized (this) {
                TablePattern tablePattern = new TablePattern();
                tablePattern.setTableData(collection);
                tablePattern.setNames(names);
                table.setModel(tablePattern);
                sorter = new TableRowSorter<TablePattern>(tablePattern);
                table.setRowSorter(sorter);
                table.revalidate();
            }
        }
    }

    class SetEnAuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            local = new English();
            setLocal();
            synchronized (this) {
                TablePattern tablePattern = new TablePattern();
                tablePattern.setTableData(collection);
                tablePattern.setNames(names);
                table.setModel(tablePattern);
                sorter = new TableRowSorter<TablePattern>(tablePattern);
                table.setRowSorter(sorter);
                table.revalidate();
              }
        }
    }

    class SetTrListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            local = new Turkish();
            setLocal();
            synchronized (this) {
                TablePattern tablePattern = new TablePattern();
                tablePattern.setTableData(collection);
                tablePattern.setNames(names);
                table.setModel(tablePattern);
                sorter = new TableRowSorter<TablePattern>(tablePattern);
                table.setRowSorter(sorter);
                table.revalidate();
             }
        }
    }

    class SetHuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            local = new Hungarian();
            setLocal();
            synchronized (this) {
                TablePattern tablePattern = new TablePattern();
                tablePattern.setTableData(collection);
                tablePattern.setNames(names);
                table.setModel(tablePattern);
                RowSorter<TablePattern> sorter = new TableRowSorter<TablePattern>(tablePattern);
                table.setRowSorter(sorter);
                table.revalidate();
             }
        }
    }

    class RemoveListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            String own = (String)table.getModel().getValueAt(row, 11);
            if (own.trim().equals(client.getUser().getUsername())) {
                int id = (int) table.getModel().getValueAt(row, 0);
                CommandMsg commandMsg = new CommandMsg("remove_by_id", Integer.toString(id), null, client.getUser());
                client.sendAndRet(commandMsg);
            }
        }
    }

    class AddListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            RowGroupAsker rowGroupAsker = new RowGroupAsker(local, new CommandMsg("add", "", null, client.getUser()), client,null, false);
            rowGroupAsker.setVisible(true);
        }
    }

    class FilterListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String text = filterField.getText();
            if (text.length() == 0) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter(text));
                    sorter.setRowFilter(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, Integer.parseInt(text)));
                } catch (PatternSyntaxException|NumberFormatException pse) {
                    System.err.println("Bad regex pattern");
                }
            }
        }
    }

}
