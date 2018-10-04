package HotelDataBaseManagement;

import HotelDataBaseManagement.DataBaseUserInterface.DBManager;
import LogIn.Users;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageUsers {
    private JLabel administratorLabel;
    private JPanel tablePanel;
    private PreparedStatement pst;
    public ManageUsers(Users users){
        JButton DbUIButton;
        JButton createButton;
        JButton rolesButton;
        JButton policyButton;
        //Title label
        Font forte = new Font("forte",Font.BOLD,30);
        administratorLabel = new JLabel("ADMINISTRATOR PANEL");
        administratorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        administratorLabel.setBackground(Color.darkGray);
        administratorLabel.setFont(forte);
        //Title
        JLabel usersLabel = new JLabel("SYSTEM USERS",SwingConstants.CENTER);
        JPanel topPanel = new JPanel(new GridLayout(1,1));
        topPanel.add(usersLabel);
        //Users Table
        String[] columnName = {"Host", "User","Password(encrypted)", "Super Priv", "max connections","Password expired","is role"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);
        JTable userTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(userTable);
        try {
            String sql = "SELECT * FROM mysql.user WHERE user <> 'root'";
            pst = users.con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("host"), rs.getString("user"),
                        rs.getString("password"),
                        rs.getString("super_priv"), rs.getString("max_user_connections"),
                        rs.getString("password_expired"), rs.getString("is_role")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //side button
        JPanel panel1;
        DbUIButton = new JButton("DataBase UI");
        createButton = new JButton("Logs");
        rolesButton = new JButton("User Role");
        policyButton = new JButton("Policy");
        panel1 = new JPanel(new GridLayout(4,1,0,20));
        panel1.add(DbUIButton);
        panel1.add(createButton);
        panel1.add(rolesButton);
        panel1.add(policyButton);
        //Buttons
        JButton createUser = new JButton("+ CREATE NEW USER");
        JButton deleteUser = new JButton("- DROP USER");
        JPanel buttonPanel = new JPanel(new GridLayout(2,6,2,1));
        buttonPanel.add(new JLabel());
        buttonPanel.add(deleteUser);
        buttonPanel.add(new JLabel());
        buttonPanel.add(new JLabel());
        buttonPanel.add(createUser);
        buttonPanel.add(new JLabel());
        for (int j=7;j<=12;j++){
            buttonPanel.add(new JLabel());
        }
        tablePanel = new JPanel(new BorderLayout(10,10));
        tablePanel.add(topPanel,BorderLayout.NORTH);
        tablePanel.add(scrollPane,BorderLayout.CENTER);
        tablePanel.add(panel1,BorderLayout.EAST);
        tablePanel.add(buttonPanel,BorderLayout.SOUTH);
        //east
        JPanel eastPanel = new JPanel();
        eastPanel.add(new JLabel());
        users.frame.setLayout(new BorderLayout(10,1));
        users.frame.add(tablePanel,BorderLayout.CENTER);
        users.frame.add(administratorLabel,BorderLayout.NORTH);
        users.frame.add(eastPanel,BorderLayout.EAST);
        //actionListeners
        createButton.addActionListener(e -> cancel(users));
        DbUIButton.addActionListener(e -> {
            removeComponents(users);
            new DBManager(users);
        });
        createUser.addActionListener(e -> createUser(users));
        deleteUser.addActionListener(e -> {
            if (userTable.getSelectedRow()>=0){
                int row = userTable.getSelectedRow();
                String userName = userTable.getValueAt(row,1).toString();
                String host = userTable.getValueAt(row,0).toString();
                if (!userName.equals(users.userName)){
                    try{
                        String sql = "DROP USER '"+userName+"'@'"+host+"'";
                        pst = users.con.prepareStatement(sql);
                        int option = JOptionPane.showConfirmDialog(null,"DO YOU WANT TO " +
                                "PERMANENTLY DELETE USER "+userName,"DROP USER",JOptionPane.YES_NO_OPTION);
                        if (option==0){
                            pst.execute();
                            JOptionPane.showMessageDialog(null,"USER "+userName+" DROPPED");
                            sql = "UPDATE employee " +
                                    "SET UserName = ?" +
                                    "WHERE UserName = ?";
                            pst= users.con.prepareStatement(sql);
                            pst.setString(1," ");
                            pst.setString(2,userName);
                            pst.execute();
                            JOptionPane.showMessageDialog(null,"Employee Updated");
                            DefaultTableModel model1 = new DefaultTableModel();
                            model1.setColumnIdentifiers(columnName);
                            userTable.setModel(model1);
                            try {
                                sql = "SELECT * FROM mysql.user WHERE user <> 'root'";
                                pst = users.con.prepareStatement(sql);
                                ResultSet rs = pst.executeQuery();
                                while (rs.next()) {
                                    model1.addRow(new Object[]{rs.getString("host"), rs.getString("user"),
                                            rs.getString("password"),
                                            rs.getString("super_priv"), rs.getString("max_user_connections"),
                                            rs.getString("password_expired"), rs.getString("is_role")});
                                }
                            }catch (SQLException se){
                                se.printStackTrace();
                            }

                        }else {
                            JOptionPane.showMessageDialog(null,"COMMAND ABORTED");
                        }
                    }catch (SQLException se){
                        JOptionPane.showMessageDialog(null,se.getMessage());
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"A USER ACCOUNT CANNOT DELETE ITS SELF");
                }
            }else{
                JOptionPane.showMessageDialog(null,"SELECT A USER FIRST");
            }
        });
    }
    private void cancel(Users users){
        removeComponents(users);
        new Administrator(users);
    }
    private void removeComponents(Users users){
        tablePanel.removeAll();
        tablePanel.repaint();
        tablePanel.revalidate();
        users.frame.remove(administratorLabel);
        users.frame.remove(tablePanel);
        users.frame.repaint();
        users.frame.revalidate();
    }
    private void createUser(Users users){
        removeComponents(users);
        Font forte = new Font("forte",Font.BOLD,30);
        administratorLabel = new JLabel("ADMINISTRATOR PANEL");
        administratorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        administratorLabel.setBackground(Color.darkGray);
        administratorLabel.setFont(forte);
        JLabel createUserLabel = new JLabel("SELECT EMPLOYEE FOR THE  NEW USER ACCOUNT");
        createUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        String[] columnName = {"Employee No", "Surname Name", "First Name", "Other Name","Department","Phone No","Email", "UserName",
                "employement Date"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);
        JTable employeeTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        try {
            String sql = "SELECT * FROM myhotel.employee ";
            pst = users.con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("EmployeeNo"), rs.getString("Surname"),
                        rs.getString("FirstName"), rs.getString("OtherName"),
                        rs.getString("Department"),rs.getString("PhoneNo"), rs.getString("Email"),
                        rs.getString("UserName"), rs.getString("employementDate")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createUserLabel,BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        //Buttons
        JButton cancelButton = new JButton("CANCEL");
        JButton finishButton = new JButton("+ CREATE USER");
        JPanel buttonPanel = new JPanel(new GridLayout(2,5));
        buttonPanel.add(new JLabel());
        buttonPanel.add(cancelButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(finishButton);
        buttonPanel.add(new JLabel());
        for (int j=6;j<=10;j++){
            buttonPanel.add(new JLabel());
        }
        users.frame.add(administratorLabel,BorderLayout.NORTH);
        users.frame.add(panel, BorderLayout.CENTER);
        users.frame.add(buttonPanel,BorderLayout.SOUTH);
        finishButton.addActionListener(e -> {
            if (employeeTable.getSelectedRow()>=0){
                int row = employeeTable.getSelectedRow();
                int employeeNum = Integer.parseInt( employeeTable.getValueAt(row,0).toString());
                String userName = employeeTable.getValueAt(row,1)+"."+employeeTable.getValueAt(row,2);
                String []oldUserNames;
                String checkUserName = "";
                try{
                    pst = users.con.prepareStatement("SELECT UserName FROM myhotel.employee " +
                            "WHERE EmployeeNo = ?");
                    pst.setInt(1,employeeNum);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        checkUserName=rs.getString("UserName");
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                }
                try{
                    if (checkUserName.equals(userName))
                        JOptionPane.showMessageDialog(null, "THE USER ALREADY HAS A USER NAME");
                    else
                        JOptionPane.showMessageDialog(null, "THE USER ALREADY HAS A USER NAME");
                }catch (NullPointerException npe) {
                    int j = 0;
                    try {
                        pst = users.con.prepareStatement("SELECT UserName FROM myhotel.employee ");
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            j++;
                        }
                        oldUserNames = new String[j];
                        rs.beforeFirst();
                        j = 0;
                        while (rs.next()) {
                            oldUserNames[j] = "" + rs.getString("UserName");
                            j++;
                        }
                    } catch (SQLException se) {
                        se.printStackTrace();
                        oldUserNames = new String[j];
                    }
                    for (int i = 0; i < j; i++) {
                        if (userName.equals(oldUserNames[i])) {
                            userName = employeeTable.getValueAt(row, 1) + "." + employeeTable.getValueAt(row, 3)
                                    .toString().toLowerCase();
                        }
                    }
                    String oneSessionPWord = "pass123";
                    String role = employeeTable.getValueAt(row, 4).toString();
                    JLabel userNameLabel = new JLabel("Proposed UserName: ", SwingConstants.TRAILING);
                    JTextField userNameTextField = new JTextField(userName, 20);
                    JPanel cu1 = new JPanel(new FlowLayout());
                    cu1.add(userNameLabel);
                    cu1.add(userNameTextField);
                    JLabel passwordLabel = new JLabel("One-Session Password:", SwingConstants.TRAILING);
                    JPasswordField passwordField = new JPasswordField(oneSessionPWord, 18);
                    JPanel cu2 = new JPanel(new FlowLayout());
                    cu2.add(passwordLabel);
                    cu2.add(passwordField);
                    JLabel hostLabel = new JLabel("HOST: ", SwingConstants.TRAILING);
                    JTextField hostTextField = new JTextField("localhost", 10);
                    JPanel cu4 = new JPanel(new FlowLayout());
                    cu4.add(hostLabel);
                    cu4.add(hostTextField);
                    JLabel userRoleLabel = new JLabel("USER ROLE: ", SwingConstants.TRAILING);
                    String[] roles;
                    j = 0;
                    try {
                        pst = users.con.prepareStatement("SELECT Department FROM myhotel.employee ");
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            j++;
                        }
                        roles = new String[j];
                        rs.beforeFirst();
                        j = 0;
                        while (rs.next()) {
                            roles[j] = "" + rs.getString("Department");
                            j++;
                        }
                    } catch (SQLException se) {
                        se.printStackTrace();
                        roles = new String[j];
                    }
                    JComboBox<String> comboBox = new JComboBox<>(roles);
                    comboBox.setSelectedItem(role);
                    JPanel cu3 = new JPanel(new FlowLayout());
                    cu3.add(userRoleLabel);
                    cu3.add(comboBox);
                    JPanel panel1 = new JPanel(new GridLayout(4, 1, 10, 10));
                    panel1.add(cu1);
                    panel1.add(cu2);
                    panel1.add(cu4);
                    panel1.add(cu3);
                    Object[] options = {"CREATE USER", "CANCEL"};
                    int ans = JOptionPane.showOptionDialog(null, panel1, "New Rooms", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (ans == 0) {
                        String host = hostTextField.getText();
                        char[] password = passwordField.getPassword();
                        userName = userNameTextField.getText();
                        role = comboBox.getSelectedItem().toString();
                        try {
                            String sql = "CREATE USER '" + userName + "'@" + host + " " +
                                    "IDENTIFIED BY '" + String.copyValueOf(password) + "'";
                            pst = users.con.prepareStatement(sql);
                            pst.execute();
                            JOptionPane.showMessageDialog(null, "USER " + userNameTextField.getText()
                                    + " CREATED. One Session Password: " + String.copyValueOf(password));
                            sql = "GRANT ALL PRIVILEGES ON myhotel.* TO '" + userName + "'@" + host;
                            pst = users.con.prepareStatement(sql);
                            pst.execute();
                            JOptionPane.showMessageDialog(null, "GIVEN Database myhotel Privileges");
                            sql = "UPDATE employee " +
                                    "SET UserName = ?, Department = ?" +
                                    "WHERE EmployeeNo = ?";
                            pst = users.con.prepareStatement(sql);
                            pst.setString(1, userName);
                            pst.setString(2, role);
                            pst.setInt(3, employeeNum);
                            pst.execute();
                            DefaultTableModel model1 = new DefaultTableModel();
                            model1.setColumnIdentifiers(columnName);
                            employeeTable.setModel(model1);
                            sql = "SELECT * FROM myhotel.employee ";
                            pst = users.con.prepareStatement(sql);
                            ResultSet rs = pst.executeQuery();
                            while (rs.next()) {
                                model1.addRow(new Object[]{rs.getString("EmployeeNo"),
                                        rs.getString("Surname"),
                                        rs.getString("FirstName"), rs.getString("OtherName"),
                                        rs.getString("Department"), rs.getString("PhoneNo"),
                                        rs.getString("Email"),
                                        rs.getString("UserName"), rs.getString("employementDate")});
                            }
                            if (role.equals("ADMINISTRATOR")) {
                                int option = JOptionPane.showConfirmDialog(null, "USER IS " +
                                        "SELECTED AS AN ADMIN. DO YOU WANT TO ISSUE AN ADMINISTRATIVE PRIVILEGES?",
                                        "DROP USER", JOptionPane.YES_NO_OPTION);
                                if (option == 0) {
                                    sql = "GRANT ALL PRIVILEGES ON *.* TO '" + userName + "'@" + host
                                            +" WITH GRANT OPTION" ;
                                    pst = users.con.prepareStatement(sql);
                                    pst.execute();
                                    JOptionPane.showMessageDialog(null, "ADMIN Privileges ISSUED");
                                }else {
                                    JOptionPane.showMessageDialog(null, "ADMIN Privileges DENIED");
                                }
                            }
                        } catch (SQLException se) {
                            se.printStackTrace();
                            JOptionPane.showMessageDialog(null, se.getMessage());
                        }
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null,"SELECT AN Employee First");
            }
        });
        cancelButton.addActionListener(e -> {
            panel.removeAll();
            panel.repaint();
            panel.revalidate();
            buttonPanel.removeAll();
            buttonPanel.repaint();
            buttonPanel.revalidate();
            users.frame.remove(panel);
            users.frame.remove(administratorLabel);
            users.frame.remove(buttonPanel);
            users.frame.repaint();
            users.frame.revalidate();
            new ManageUsers(users);
        });
    }
}