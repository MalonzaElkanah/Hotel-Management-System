package HotelDataBaseManagement.DataBaseUserInterface;

import HotelDataBaseManagement.ManageUsers;
import LogIn.Users;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Malone-Ibra on 5/27/2018.
 *
 */
public class DBManager {
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JPanel eastPanel;
    private PreparedStatement pst;
    private String database = null;
    private String tableName = null;
    private JComboBox<String> databaseComboBox;
    private JPanel headerPanel;
    private TableBuilderFrame tableMaker = null;
    private TableQueryFrame tableQuery = null; // added for chapter 7
    private TableEditFrame tableEditor = null; // added for Chapter 6
    private DatabaseUtilities dbUtils = new DatabaseUtilities();
    private DBQueryInterface dbQueryInterface = new DBQueryInterface();
    private JMenu tableMenu;
    private JMenu databaseMenu;

    public DBManager(Users users){
        //menu
        databaseMenu =  new JMenu("Database");
        databaseMenu.setMnemonic((int)'D');
        JMenuItem newDataBase = new JMenuItem("New DataBase",'N');
        JMenuItem selectDbItem = new JMenuItem("Select DataBase",'S');
        JMenuItem dropDbItem = new JMenuItem("Drop DataBase",'D');
        databaseMenu.add(newDataBase);
        databaseMenu.add(selectDbItem);
        databaseMenu.add(dropDbItem);
        users.menuBar.add(databaseMenu);
        tableMenu = new JMenu("Table");
        JMenuItem resultSetItem;
        JMenuItem insertItem;
        JMenuItem updateItem;
        JMenuItem deleteItem;
        JMenuItem newItem;
        JMenuItem openItem;
        tableMenu.setMnemonic((int)'T');
        resultSetItem = new JMenuItem("View Table",'V');
        insertItem = new JMenuItem("Insert Into Table",'I');
        updateItem = new JMenuItem("Update VALUES",'U');
        deleteItem = new JMenuItem("Delete VALUES",'D');
        newItem = new JMenuItem("New Table",'T');
        openItem = new JMenuItem("Drop Table",'D');
        tableMenu.add(resultSetItem);
        tableMenu.addSeparator();
        tableMenu.add(insertItem);
        tableMenu.add(updateItem);
        tableMenu.add(deleteItem);
        tableMenu.addSeparator();
        tableMenu.add(newItem);
        tableMenu.add(openItem);
        users.menuBar.add(tableMenu);

        users.frame.setFont(new Font("Dialog",Font.PLAIN,18));
        Font font = users.frame.getGraphics().getFont();
        System.out.println(font);
        //Title label
        JLabel administratorLabel;
        Font forte = new Font("forte",Font.BOLD,30);
        administratorLabel = new JLabel("DATABASE USER INTERFACE");
        administratorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        administratorLabel.setBackground(Color.darkGray);
        administratorLabel.setFont(forte);
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(administratorLabel,BorderLayout.CENTER);
        //inner frame
        System.out.println(users.con.toString());
        tablePanel = new JPanel(new BorderLayout(10,10));
        tableName = "myhotel.food_customers";
        dbUtils = new DatabaseUtilities();
        dbUtils.setDatabaseName("myhotel",users);

        dbQueryInterface = new DBQueryInterface(tableName,dbUtils,users);
        tablePanel.add(dbQueryInterface,BorderLayout.CENTER);
        dbQueryInterface.setVisible(true);
        //Database TREE
        String [] databasesCombo;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Databases");
        DefaultMutableTreeNode [] databaseNodes;
        DefaultMutableTreeNode [][] tableNodes;
        int dbNum = 0;
        int tableNum;
        try{
            String sql = "SHOW DATABASES";
            pst = users.con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dbNum++;
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        databasesCombo= new String[dbNum];
        databaseNodes = new DefaultMutableTreeNode[dbNum];
        try{
            String sql = "SHOW DATABASES";
            pst = users.con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int dbNum2=0;
            while (rs.next()) {
                databasesCombo[dbNum2] = ""+rs.getString(1);
                databaseNodes[dbNum2] = new DefaultMutableTreeNode(rs.getString(1));
                sql = "USE " + rs.getString(1);
                pst = users.con.prepareStatement(sql);
                pst.executeQuery();
                sql = "SHOW TABLES";
                pst = users.con.prepareStatement(sql);
                ResultSet rs1 = pst.executeQuery();
                tableNum = 0;
                while (rs1.next()) {
                    tableNum++;
                }
                tableNodes = new DefaultMutableTreeNode[dbNum] [tableNum];
                rs1.beforeFirst();
                tableNum = 0;
                while (rs1.next()){
                    tableNodes[dbNum2] [tableNum] = new DefaultMutableTreeNode(rs1.getString(1));
                    databaseNodes[dbNum2].add(tableNodes[dbNum2] [tableNum]);
                    tableNum++;
                }
                root.add(databaseNodes[dbNum2]);
                dbNum2++;
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        JScrollPane dbTreeScrollPane;
        JTree databaseTree = new JTree(root);
        dbTreeScrollPane = new JScrollPane(databaseTree);
        TreeSelectionModel selectionModel = databaseTree.getSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JButton dropButton = new JButton("DROP TABLE");
        eastPanel = new JPanel(new BorderLayout());
        eastPanel.add( dbTreeScrollPane,BorderLayout.CENTER);
        eastPanel.add(new JLabel("SELECT TABLE TO VIEW"),BorderLayout.NORTH);
        eastPanel.add(dropButton, BorderLayout.SOUTH);
        //select database
        JPanel comboPanel = new JPanel(new GridLayout(1,4));
        JLabel comboLabel = new JLabel("USE DATABASE: ");
        comboLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        databaseComboBox = new JComboBox<>(databasesCombo);
        databaseComboBox.setSelectedItem("myhotel");
        panel.add(databaseComboBox);
        comboPanel.add(new JLabel());
        comboPanel.add(comboLabel);
        comboPanel.add(panel);
        comboPanel.add(new JLabel());
        headerPanel.add(comboPanel,BorderLayout.SOUTH);
        //south buttons
        JButton backButton = new JButton("BACK");
        JButton createUser = new JButton("REFRESH");
        buttonPanel = new JPanel(new GridLayout(2,5));
        buttonPanel.add(new JLabel());
        buttonPanel.add(backButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(createUser);
        buttonPanel.add(new JLabel());
        for (int j=6;j<=10;j++){
            buttonPanel.add(new JLabel());
        }
        //outer frame
        users.frame.setLayout(new BorderLayout(1,10));
        users.frame.add(tablePanel,BorderLayout.CENTER);
        users.frame.add(headerPanel,BorderLayout.NORTH);
        users.frame.add(eastPanel,BorderLayout.WEST);
        users.frame.add(buttonPanel,BorderLayout.SOUTH);
        //Listeners;
        newDataBase.addActionListener(e -> createDatabase(users));
        dropDbItem.addActionListener(e -> dropDatabase(users));
        selectDbItem.addActionListener(e -> selectDatabase(users));
        newItem.addActionListener(e -> displayTableBuilderFrame(users) );
        openItem.addActionListener(e -> {
            tableName = JOptionPane.showInputDialog(null,"Table:",
                    "Select table",JOptionPane.QUESTION_MESSAGE);
            dropTable(users);
        });
        insertItem.addActionListener(e -> displayTableEditFrame(users));
        updateItem.addActionListener(e -> displayTableUpdateFrame(users) );
        deleteItem.addActionListener(e ->{} );
        resultSetItem.addActionListener(e -> displayTableQueryFrame(users));
        backButton.addActionListener(e -> {
            removeComponents(users);
            new ManageUsers(users);
        });
        databaseComboBox.addItemListener((ItemEvent e) ->{
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String dbName = databaseComboBox.getSelectedItem().toString();
                dbUtils.setDatabaseName(dbName,users);
            }
        });
        dropButton.addActionListener(e -> {
            int option = JOptionPane.showOptionDialog(null,"DELETING THIS MAY AFFECT THE SYSTEM OPERATION",
                    "",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,null,0);
            if (option==0){
                try {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)databaseTree.getSelectionPath().getLastPathComponent();
                    String [] dbName;
                    String [][] tableName;
                    int dbNum1 = 0;
                    int tableNum1;
                    try{
                        String sql = "SHOW DATABASES";
                        pst = users.con.prepareStatement(sql);
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            dbNum1++;
                        }
                        dbName = new String[dbNum1];
                        rs.beforeFirst();
                        int dbNum12=0;
                        while (rs.next()) {
                            dbName[dbNum12] = ""+rs.getString(1);
                            sql = "USE " + rs.getString(1);
                            pst = users.con.prepareStatement(sql);
                            pst.executeQuery();
                            sql = "SHOW TABLES";
                            pst = users.con.prepareStatement(sql);
                            ResultSet rs1 = pst.executeQuery();
                            tableNum1 = 0;
                            while (rs1.next()) {
                                tableNum1++;
                            }
                            tableName = new String[dbNum1] [tableNum1];
                            rs1.beforeFirst();
                            tableNum1 = 0;
                            while (rs1.next()){
                                tableName[dbNum12] [tableNum1] = ""+rs1.getString(1);
                                if (node.toString().equals(tableName[dbNum12] [tableNum1])){
                                    this.tableName=tableName[dbNum12] [tableNum1];
                                    dropTable(users);
                                    databaseNodes[dbNum12].remove((DefaultMutableTreeNode)databaseTree.getSelectionPath().getLastPathComponent());
                                    ((DefaultMutableTreeNode)databaseTree.getSelectionPath().getLastPathComponent()).removeFromParent();
                                }
                                tableNum1++;
                            }
                            dbNum12++;
                        }
                    }catch (SQLException se){
                        se.printStackTrace();
                    }
                }catch (NullPointerException npe){
                    JOptionPane.showMessageDialog(null,"PLEASE SELECT A TABLE");
                }

            }
        });
        databaseTree.addTreeSelectionListener((TreeSelectionEvent event) -> {
            TreePath[] paths = event.getPaths();
            for (TreePath path : paths) {
                Object node = path.getLastPathComponent();
                if (selectionModel.isPathSelected(path)) {
                    String [] dbName;
                    String [][] tableName;
                    int dbNum1 = 0;
                    int tableNum1;
                    try{
                        String sql = "SHOW DATABASES";
                        pst = users.con.prepareStatement(sql);
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            dbNum1++;
                        }
                        dbName = new String[dbNum1];
                        rs.beforeFirst();
                        int dbNum12=0;
                        while (rs.next()) {
                            dbName[dbNum12] = ""+rs.getString(1);
                            sql = "USE " + rs.getString(1);
                            pst = users.con.prepareStatement(sql);
                            pst.executeQuery();
                            sql = "SHOW TABLES";
                            pst = users.con.prepareStatement(sql);
                            ResultSet rs1 = pst.executeQuery();
                            tableNum1 = 0;
                            while (rs1.next()) {
                                tableNum1++;
                            }
                            tableName = new String[dbNum1] [tableNum1];
                            rs1.beforeFirst();
                            tableNum1 = 0;
                            while (rs1.next()){
                                tableName[dbNum12] [tableNum1] = ""+rs1.getString(1);
                                if (node.toString().equals(tableName[dbNum12] [tableNum1])){
                                    tablePanel.removeAll();
                                    tablePanel.repaint();
                                    tablePanel.revalidate();
                                    sql = "USE "+dbName[dbNum12];
                                    databaseComboBox.setSelectedItem(""+dbName[dbNum12]);
                                    pst = users.con.prepareStatement(sql);
                                    pst.executeQuery();
                                    dbQueryInterface = new DBQueryInterface(dbName[dbNum12]+"."+node.toString(),dbUtils,users);
                                    tablePanel.add(dbQueryInterface,BorderLayout.CENTER);
                                    dbQueryInterface.setVisible(true);
                                    dbQueryInterface.tabbedPane.setSelectedIndex(0);
                                }
                                tableNum1++;
                            }
                            dbNum12++;
                        }
                    }catch (SQLException se){
                        se.printStackTrace();
                    }
                }
            }
        });
        int j =databaseComboBox.getItemCount();
        databaseComboBox.setSelectedIndex(j-1);
    }
    private void displayTableBuilderFrame(Users users){
        tableName = JOptionPane.showInputDialog(null,"Table:",
                "Select table",JOptionPane.QUESTION_MESSAGE);
        if(tableName==null){
            JOptionPane.showMessageDialog(null,"NO NULL TABLE");
        }else{
            tableMaker = new TableBuilderFrame(tableName);
            tableMaker.setCommandListener((ActionEvent e) -> {
                String SQLCommand = e.getActionCommand();
                executeSQLCommand(SQLCommand, users);
            });/*new CommandListener()*/
            tablePanel.removeAll();
            tablePanel.repaint();
            tablePanel.revalidate();
            tablePanel.add(tableMaker,BorderLayout.CENTER);
            tableMaker.setVisible(true);
        }
    }
    private void displayTableEditFrame(Users users){
        tableName = JOptionPane.showInputDialog(null,"Table:",
                "Select table",JOptionPane.QUESTION_MESSAGE);
        if(tableName==null){
            JOptionPane.showMessageDialog(null,"NO NULL TABLE");
        }else {
            tableEditor = new TableEditFrame(tableName, dbUtils, users);
            tablePanel.removeAll();
            tablePanel.repaint();
            tablePanel.revalidate();
            tablePanel.add(tableEditor, BorderLayout.CENTER);
            tableEditor.setVisible(true);
        }
    }
    private void displayTableQueryFrame(Users users){
        tableName = JOptionPane.showInputDialog(null,"Table:",
                "Select table",JOptionPane.QUESTION_MESSAGE);
        if(tableName==null){
            JOptionPane.showMessageDialog(null,"NO NULL TABLE");
        }else {
            tableQuery = new TableQueryFrame(tableName, dbUtils, users);
            tablePanel.removeAll();
            tablePanel.repaint();
            tablePanel.revalidate();
            tablePanel.add(tableQuery, BorderLayout.CENTER);
            tableQuery.setVisible(true);
        }
    }
    private void displayTableUpdateFrame(Users users){
        tableName = JOptionPane.showInputDialog(null,"Table:",
                "Select table",JOptionPane.QUESTION_MESSAGE);
        if(tableName==null){
            JOptionPane.showMessageDialog(null,"NO NULL TABLE");
        }else{
            TableUpdateFrame tableUpdate = new TableUpdateFrame(tableName,users,dbUtils);
            tablePanel.removeAll();
            tablePanel.repaint();
            tablePanel.revalidate();
            tablePanel.add(tableUpdate,BorderLayout.CENTER);
            tableQuery.setVisible(true);
        }
    }
    private void selectDatabase(Users users){
        database = JOptionPane.showInputDialog(null,"Database:",
                "Select database",JOptionPane.QUESTION_MESSAGE);
        if(database==null){
            JOptionPane.showMessageDialog(null,"NO NULL DATABASE");
        }else {
            dbUtils = new DatabaseUtilities();
            dbUtils.setDatabaseName(database, users);
            dbUtils.setExceptionListener(new ExceptionListener());
        }
    }
    private void createDatabase(Users users){
        String newDbName = JOptionPane.showInputDialog(null,"Database:",
                "Select database",JOptionPane.QUESTION_MESSAGE);
        if(newDbName==null){
            JOptionPane.showMessageDialog(null,"NO CREATING NULL DATABASE");
        }else {
            try {
                String sql = "CREATE DATABASE " + newDbName;
                pst = users.con.prepareStatement(sql);
                pst.executeQuery();
                JOptionPane.showMessageDialog(null, "DATABASE " + newDbName + " CREATED");
            } catch (SQLException se) {
                dbUtils.reportException(se.getMessage());
            }
        }
    }
    private void dropDatabase(Users users){
        String deleteDbName = JOptionPane.showInputDialog(null,"Database:",
                "Select database",JOptionPane.QUESTION_MESSAGE);
        if(deleteDbName==null){
            JOptionPane.showMessageDialog(null,"NO NULL TABLE");
        }else {
            if (deleteDbName.equals("myhotel") || deleteDbName.equals("mysql") || deleteDbName.equals("information_schema") ||
                    deleteDbName.equals("performance_schema") || deleteDbName.equals("phpmyadmin")) {
                JOptionPane.showMessageDialog(null, "DATABASE " + deleteDbName + " CANNOT BE DROPPED!!");
            } else {
                try {
                    String sql = "DROP DATABASE " + deleteDbName;
                    pst = users.con.prepareStatement(sql);
                    pst.executeQuery();
                    JOptionPane.showMessageDialog(null, "DATABASE " + deleteDbName + " DROPPED");
                } catch (SQLException se) {
                    dbUtils.reportException(se.getMessage());
                }
            }
        }
    }
    private void executeSQLCommand(String SQLCommand, Users users){
        dbUtils.execute(SQLCommand,users);
    }
    private void dropTable( Users users){
        int option = JOptionPane.showConfirmDialog(null,
                "Dropping table "+tableName,
                "Database "+database,
                JOptionPane.OK_CANCEL_OPTION);
        if(option==0){
            executeSQLCommand("DROP TABLE "+tableName,users);
        }
    }
    private void removeComponents(Users users){
        tablePanel.removeAll();
        tablePanel.repaint();
        tablePanel.revalidate();
        eastPanel.removeAll();
        eastPanel.repaint();
        eastPanel.revalidate();
        buttonPanel.removeAll();
        buttonPanel.repaint();
        buttonPanel.revalidate();
        headerPanel.removeAll();
        headerPanel.repaint();
        headerPanel.revalidate();
        users.frame.remove(tablePanel);
        users.frame.remove(eastPanel);
        users.frame.remove(buttonPanel);
        users.frame.remove(headerPanel);
        users.frame.repaint();
        users.frame.revalidate();
        users.menuBar.remove(databaseMenu);
        users.menuBar.remove(tableMenu);
    }
    class ExceptionListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            String exception = event.getActionCommand();
            JOptionPane.showMessageDialog(null, exception,
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
