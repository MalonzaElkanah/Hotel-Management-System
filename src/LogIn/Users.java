package LogIn;

import FoodCounter.FoodCounter;
import GeneralHotelManagement.Manager;
import HotelDataBaseManagement.ManageUsers;
import HotelRoomManagement.Reception;
import Store.ProductAndAssets;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Users {
    public JFrame frame = new JFrame("VINTAGE LUX HOTEL MANAGEMENT SYSTEM");
    public String userName;
    private JTextField userNameTextField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JLabel warningLabel = new JLabel();
    public Connection con = null;
    private JMenu userMenu;
    private JMenu settingsMenu;
    private JMenu connection;
    public JMenuBar menuBar;
    private JPanel panel4;
    private JPanel jPanel;
    private JPanel panel8;
    private JPanel panel5;
    private JPanel panel7;
    public Users(){
        Font forte = new Font("forte",Font.BOLD,75);
        Font forte1 = new Font("forte",Font.PLAIN,16);
        //Menu
        JMenuItem database = new JMenuItem("Database");
        settingsMenu = new JMenu("Settings");
        settingsMenu.add(database);
        JMenuItem checkConnection = new JMenuItem("refresh ");
        JMenuItem closeConnection = new JMenuItem("close ");
        connection = new JMenu("Connection");
        connection.add(checkConnection);
        connection.add(closeConnection);
        JMenuItem exit = new JMenuItem("Exit");
        userMenu = new JMenu("Users");
        userMenu.add(exit);
        menuBar = new JMenuBar();
        menuBar.add(connection);
        menuBar.add(settingsMenu);
        menuBar.add(userMenu);
        frame.setJMenuBar(menuBar);
        //Title
        JLabel loginLabel = new JLabel("VINTAGE LUX MOTEL");
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setVerticalAlignment(SwingConstants.TOP);
        loginLabel.setBackground(Color.darkGray);
        loginLabel.setFont(forte);
        jPanel = new JPanel(new GridLayout(3,1,10,0));
        jPanel.add(new JSeparator());
        jPanel.add(loginLabel);
        jPanel.add(new JLabel());
        //Warning Label
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        panel.add(new JLabel());
        panel.add(warningLabel);
        //UserName
        JLabel userNameLabel = new JLabel("USER NAME:");
        userNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        userNameLabel.setFont(forte1);
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        userNameTextField.setFont(new Font("verdana",Font.PLAIN,16));
        panel1.add(userNameLabel);
        panel1.add(userNameTextField);
        //Password
        JLabel passwordLabel = new JLabel("PASS-WORD:");
        passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        passwordLabel.setFont(forte1);
        passwordField.setFont(new Font("verdana",Font.PLAIN,16));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        panel2.add(passwordLabel);
        panel2.add(passwordField);
        //login
        JButton loginButton = new JButton("LOG IN");
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        panel3.add(new JLabel());
        panel3.add(loginButton);
        panel3.add(new JLabel());

        panel4 = new JPanel(new GridLayout(4,1,10,-10));
        panel4.add(panel);
        panel4.add(panel1);
        panel4.add(panel2);
        panel4.add(panel3);

        panel5 = new JPanel();
        panel7 = new JPanel(new GridLayout(4,1,10,40));
        for(int j = 1;j<=4;j++){
            panel7.add(new JLabel());
        }
        panel8 = new JPanel();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1450,730);
        frame.setForeground(Color.DARK_GRAY);
        frame.setLayout(new BorderLayout(100,0));
        frame.add(jPanel,BorderLayout.NORTH);
        frame.add(panel4,BorderLayout.CENTER);
        frame.add(panel5,BorderLayout.EAST);
        frame.add(panel7, BorderLayout.SOUTH);
        frame.add(panel8,BorderLayout.WEST);
        frame.setBackground(Color.darkGray);
        frame.setVisible(true);
        establishConnection();
        loginButton.addActionListener(e-> login());
        database.addActionListener(e->dataBaseSettings());
        exit.addActionListener(e ->  exit());
        checkConnection.addActionListener(e -> establishConnection());
        closeConnection.addActionListener(e -> closeConnection());
    }
    private void establishConnection(){
        try{
            Font verdana = new Font("verdana",Font.BOLD,14);
            warningLabel.setFont(verdana);
            try {
                Class.forName("com.mysql.jdbc.Driver");
            }catch (ClassNotFoundException cnf){
                warningLabel.setForeground(Color.RED);
                warningLabel.setText("ERROR!!: Database Drivers Not found");
                cnf.getStackTrace();
            }
            warningLabel.setForeground(Color.GRAY);
            warningLabel.setText("creating connection...");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "","");
            warningLabel.setForeground(Color.BLUE);
            warningLabel.setText("SERVER STATUS: ONLINE");
        }catch(SQLException ex){
            if (ex.getErrorCode()==0){
                warningLabel.setForeground(Color.RED);
                warningLabel.setText("SERVER STATUS: UNAVAILABLE");
            }else{
                warningLabel.setForeground(Color.RED);
                warningLabel.setText("ERROR "+ex.getErrorCode()+": "+ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    private void closeConnection(){
        try{
            con.close();
        }catch (SQLException se){
            warningLabel.setForeground(Color.RED);
            warningLabel.setText(se.getLocalizedMessage());
        }
    }
    public void login(){
        JMenuItem logOut = new JMenuItem("LogOut");
        userMenu.add(logOut);
        userName= userNameTextField.getText().trim();
        char[] password = passwordField.getPassword();
        closeConnection();
        if (!userName.equals("")) {
            try {
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                }catch (ClassNotFoundException cnf){
                    warningLabel.setForeground(Color.RED);
                    warningLabel.setText("ERROR: Database Drivers Not found");
                    userMenu.remove(logOut);
                }
                warningLabel.setForeground(Color.GRAY);
                warningLabel.setText("authenticating username and password...");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/myhotel",
                        "" + userName + "", "" + String.copyValueOf(password));
                warningLabel.setText("authentication successful...");
                PreparedStatement stmt = con.prepareStatement("SELECT Department FROM employee WHERE UserName = ?");
                stmt.setString(1, userName);
                warningLabel.setText("executing query...");
                ResultSet rs = stmt.executeQuery();
                rs.next();
                if (rs.getString("Department").equalsIgnoreCase("MANAGER")) {
                    warningLabel.setText(" Access Granted...");
                    removeComponents();
                    new Manager(this);
                } else if (rs.getString("Department").equalsIgnoreCase("ADMINISTRATOR")) {
                    warningLabel.setText(" Access Granted...");
                    removeComponents();
                    new ManageUsers(this);
                } else if (rs.getString("Department").equalsIgnoreCase("RECEPTION")) {
                    warningLabel.setText(" Access Granted...");
                    removeComponents();
                    new Reception(this);
                } else if (rs.getString("Department").equalsIgnoreCase("FOOD COUNTER")) {
                    warningLabel.setText(" Access Granted...");
                    removeComponents();
                    new FoodCounter(this);
                }else if (rs.getString("Department").equalsIgnoreCase("STORE")) {
                    warningLabel.setText(" Access Granted...");
                    removeComponents();
                    new ProductAndAssets(this);
                } else {
                    warningLabel.setForeground(Color.RED);
                    warningLabel.setText("ERROR: "+userName+" USER ROLE or DEPARTMENT not set.");
                    userMenu.remove(logOut);
                }
            } catch ( SQLException  ex) {
                if (ex.getErrorCode()==1044){
                    warningLabel.setForeground(Color.RED);
                    warningLabel.setText("Access Denied: Check your username or password");
                }else if (ex.getErrorCode() ==1045){
                    warningLabel.setForeground(Color.RED);
                    warningLabel.setText("ACCESS DENIED"+": Check your password and username");
                }else if (ex.getErrorCode()==0){
                    warningLabel.setForeground(Color.RED);
                    warningLabel.setText("CONNECTION ERROR!!: server unavailable");
                }else {
                    warningLabel.setForeground(Color.RED);
                    warningLabel.setText("ERROR "+ex.getErrorCode()+": "+ex.getMessage());
                }
                userMenu.remove(logOut);
            }
        }else{
            warningLabel.setForeground(Color.RED);
            warningLabel.setText("ERROR: please provide username");
            userMenu.remove(logOut);
        }
        logOut.addActionListener(e -> {
            closeConnection();
            frame.setVisible(false);
            new Users();
        });
    }
    private void dataBaseSettings(){
        Font forte1 = new Font("forte",Font.PLAIN,16);
        JCheckBox firstTime = new JCheckBox("First Time");
        JLabel dbTypeLabel = new JLabel("Database Type");
        JComboBox dbTypeComboBox = new JComboBox();
        JPanel dbTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dbTypePanel.add(dbTypeLabel);
        dbTypePanel.add(dbTypeComboBox);
        JPanel topPanel = new JPanel(new BorderLayout(0,10));
        topPanel.add(firstTime,BorderLayout.NORTH);
        topPanel.add(dbTypePanel,BorderLayout.CENTER);
        JLabel hostLabel = new JLabel("Host Configuration");
        hostLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hostLabel.setFont(forte1);
        JCheckBox localHostCheckBox = new JCheckBox("Local Host");
        JLabel hostNameLabel = new JLabel("Host");
        JTextField hostTextField = new JTextField(10);
        JPanel host = new JPanel(new FlowLayout(FlowLayout.CENTER));
        host.add(hostNameLabel);
        host.add(hostTextField);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(localHostCheckBox);
        panel.add(host);
        JLabel dbNameLabel = new JLabel("Database Name");
        JTextField dbNameTextField = new JTextField(12);
        JPanel panel1 = new JPanel(new FlowLayout());
        panel1.add(dbNameLabel);
        panel1.add(dbNameTextField);
        JPanel hostPanel = new JPanel(new BorderLayout());
        hostPanel.add(hostLabel,BorderLayout.NORTH);
        hostPanel.add(panel,BorderLayout.CENTER);
        hostPanel.add(panel1,BorderLayout.SOUTH);
        JLabel testConnLabel = new JLabel("Test Connection");
        testConnLabel.setFont(forte1);
        testConnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel userLabel = new JLabel("USER: ");
        JTextField userTextField = new JTextField(12);
        JPanel panel2 = new JPanel(new FlowLayout());
        panel2.add(userLabel);
        panel2.add(userTextField);
        JLabel passwordLabel = new JLabel("PASSWORD");
        JPasswordField passwordField = new JPasswordField(12);
        JPanel panel3 = new JPanel(new FlowLayout());
        panel3.add(passwordLabel); panel3.add(passwordField);
        JPanel jPanel = new JPanel(new GridLayout(1,2,10,10));
        jPanel.add(panel2);jPanel.add(panel3);

        JButton testButton = new JButton("TEST");
        JLabel feedBackLabel = new JLabel("FeedBack: ");
        JPanel jPanel1 = new JPanel(new FlowLayout());
        jPanel1.add(testButton);
        jPanel1.add(feedBackLabel);
        JButton runScriptButton = new JButton("RUN SCRIPT");
        JLabel scriptLabel = new JLabel(" Script: " );
        JPanel scriptPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scriptPanel.add(runScriptButton);
        scriptPanel.add(scriptLabel);
        JPanel jPanel2 = new JPanel(new GridLayout(1,2,10,10));
        jPanel2.add(jPanel1);jPanel2.add(scriptPanel);
        JPanel testConnPanel = new JPanel(new BorderLayout(10,10));
        testConnPanel.add(testConnLabel,BorderLayout.NORTH);
        testConnPanel.add(jPanel,BorderLayout.CENTER);
        testConnPanel.add(jPanel2,BorderLayout.SOUTH);
        JPanel panel4 =new JPanel(new BorderLayout(1,21));
        panel4.add(topPanel,BorderLayout.NORTH);
        panel4.add(hostPanel,BorderLayout.CENTER);
        panel4.add(testConnPanel,BorderLayout.SOUTH);
        Object [] options = {"FINISH","NO","CANCEL"};
        int ans =JOptionPane.showOptionDialog(null,panel4,"Database Settings",JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        if (ans==0){
            System.out.println("Change Implemented");
        }else if (ans==1){
            System.out.println("");
        }else{
            System.out.println("");
        }
    }
    private void removeComponents(){
        panel4.removeAll();
        panel4.repaint();
        panel4.revalidate();
        jPanel.removeAll();
        jPanel.repaint();
        jPanel.revalidate();
        panel5.removeAll();
        panel5.repaint();
        panel5.revalidate();
        panel7.removeAll();
        panel7.repaint();
        panel7.revalidate();
        panel8.removeAll();
        panel8.repaint();
        panel8.revalidate();
        frame.remove(panel4);
        frame.repaint();
        frame.revalidate();
        frame.remove(jPanel);
        //frame.revalidate();
        frame.remove(panel5);
        frame.remove(panel7);
        frame.remove(panel8);
        menuBar.remove(connection);
        menuBar.remove(settingsMenu);
        //menuBar.setVisible(false); // malonza.elkanah
    }
    public void addComponents(){
        frame.setLayout(new BorderLayout(100,0));
        frame.add(jPanel,BorderLayout.NORTH);
        frame.add(panel4,BorderLayout.CENTER);
        frame.add(panel5,BorderLayout.EAST);
        frame.add(panel7, BorderLayout.SOUTH);
        frame.add(panel8,BorderLayout.WEST);
    }
    private void exit(){
        closeConnection();
        System.exit(0);
    }
    public static void main(String [] args){
        new Users();
    }
}
