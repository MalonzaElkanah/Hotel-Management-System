package GeneralHotelManagement;

import LogIn.Users;

import javax.swing.*;
import java.awt.*;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.*;

class ViewEmployees {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel buttonPanel;
    private JPanel mainLabelPanel;
    private JPanel centrePanel1;
    private PreparedStatement pst;
    private JComboBox<String> searchCategoryComboBox;
    private JTextField searchTextField;
    private JTable employeeTable;
    int EmployeeNo;
    ViewEmployees(Users users) {
        //main label
        Font forte1 = new Font("forte", Font.BOLD, 20);
        JLabel managerLabel = new JLabel("EMPLOYEES");
        managerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        managerLabel.setFont(forte1);
        GridLayout mainLabelGrid = new GridLayout(1, 1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        mainLabelPanel.add(managerLabel);
        //search system
        JLabel searchLabel = new JLabel("SEARCH: ", SwingConstants.TRAILING);
        searchTextField = new JTextField(15);
        JPanel textFieldPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        textFieldPanel.add(searchLabel);
        textFieldPanel.add(searchTextField);
        JLabel searchCategoryLabel = new JLabel("Search by: ", SwingConstants.TRAILING);
        String [] searchCategory= {"First Name","Employee Number","Surname Name","Other Name","Phone Number"};
        searchCategoryComboBox = new JComboBox<>(searchCategory);
        JPanel comboSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        comboSearchPanel.add(searchCategoryLabel);
        comboSearchPanel.add(searchCategoryComboBox);
        JPanel searchPanel = new JPanel(new GridLayout(1, 4, 1, 1));
        searchPanel.add(new JLabel());
        //searchPanel.add(searchLabel);
        searchPanel.add(textFieldPanel);
        //searchPanel.add(searchCategoryLabel);
        searchPanel.add(comboSearchPanel);
        searchPanel.add(new JLabel());
        JPanel searchPanel2 = new JPanel(new GridLayout(1,1,1,10));
        searchPanel2.add(searchPanel);
        //separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        //View by
        String [] views ={"ALL","MANAGERS","RECEPTIONS","ADMINISTRATORS","FOOD COUNTER",
                "ROOM ATTENDANTS","CHEFS","COOKS","WAITERS"};
        JComboBox<String> viewComboBox = new JComboBox<>(views);
        JPanel comboViewPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        comboViewPanel.add(viewComboBox);
        JPanel viewPanel = new JPanel(new GridLayout(1, 5, 10, 1));
        viewPanel.add(new JLabel());
        viewPanel.add(new JLabel("VIEW: ", SwingConstants.TRAILING));
        viewPanel.add(comboViewPanel);
        viewPanel.add(new JLabel());
        viewPanel.add(new JLabel());
        //employee table
        String[] columnNames = {"EmpNo", "FirstName", "Surname", "Department", "PhoneNo", "YearOfEmployment",
                "HomeAddress", "Email"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        employeeTable = new JTable();
        employeeTable.setModel(model);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        try {
            pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                    " HomeAdress, Email  FROM employee");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String employeeNo = rs.getString("EmployeeNo");
                String firstName = rs.getString("FirstName");
                String secondName = rs.getString("Surname");
                String department = rs.getString("Department");
                String phone = rs.getString("PhoneNo");
                String yOE = rs.getString("employementDate");
                String homeAddress = rs.getString("HomeAdress");
                String email = rs.getString("Email");
                model.addRow(new Object[]{employeeNo, firstName, secondName, department, phone, yOE, homeAddress, email});
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        //COMBINE
        JPanel centrePanel = new JPanel(new BorderLayout(0, 10));
        centrePanel.add(viewPanel, BorderLayout.NORTH);
        centrePanel.add(tablePanel, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.add(separator, BorderLayout.NORTH);
        panel.add(centrePanel, BorderLayout.CENTER);
        centrePanel1 = new JPanel(new BorderLayout(0, 40));
        centrePanel1.add(searchPanel2, BorderLayout.NORTH);
        centrePanel1.add(panel, BorderLayout.CENTER);
        //BUTTONS
        GridLayout buttonGridLayout = new GridLayout(2, 8, 17, 0);
        buttonPanel = new JPanel(buttonGridLayout);
        JButton createEmpButton = new JButton("NEW EMPLOYEE");
        JButton backButton = new JButton("BACK");
        JButton profileButton = new JButton("EDIT / DELETE");
        buttonPanel.add(new JLabel());
        buttonPanel.add(backButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(new JLabel());
        buttonPanel.add(new JLabel());
        buttonPanel.add(profileButton);
        buttonPanel.add(createEmpButton);
        buttonPanel.add(new JLabel());
        for (int j = 1; j <= 8; j++) {
            buttonPanel.add(new JLabel());
        }
        //WEST border
        JSeparator westSeparator = new JSeparator();
        JLabel west = new JLabel();
        westPanel = new JPanel();
        westPanel.add(westSeparator);
        westPanel.add(west);
        //East border

        //JButton dutyRoasterButton = new JButton("DUTY ROASTER");
        eastPanel = new JPanel();
        eastPanel.add(new JPanel());
        //eastPanel.add(dutyRoasterButton);
        //Outer frame
        users.frame.setLayout(new BorderLayout(10, 10));
        users.frame.add(mainLabelPanel, BorderLayout.NORTH);
        users.frame.add(centrePanel1, BorderLayout.CENTER);
        users.frame.add(buttonPanel, BorderLayout.SOUTH);
        users.frame.add(eastPanel, BorderLayout.EAST);
        users.frame.add(westPanel, BorderLayout.WEST);
        //action listeners
        createEmpButton.addActionListener(e ->{
            removeComponents(users);
            new CreateEmployee(users);
        });
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
            private void search(){
                    String[] columnNames = {"EmpNo", "FirstName", "SecondName", "Department", "PhoneNo", "YearOfEmployment", "HomeAddress", "Email"};
                    DefaultTableModel model = new DefaultTableModel();
                    model.setColumnIdentifiers(columnNames);
                    employeeTable.setModel(model);
                    try {
                        String searchVariable = searchTextField.getText();
                        if (searchCategoryComboBox.getSelectedIndex() == 0) {
                            pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                    " HomeAdress, Email  FROM employee WHERE FirstName LIKE ?");
                        } else if (searchCategoryComboBox.getSelectedIndex() == 1) {
                            pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                    " HomeAdress, Email  FROM employee WHERE CAST(EmployeeNo as CHAR) LIKE ?");
                        } else if (searchCategoryComboBox.getSelectedIndex() == 2) {
                            pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                    " HomeAdress, Email  FROM employee WHERE Surname LIKE ?");
                        } else if (searchCategoryComboBox.getSelectedIndex() == 3) {
                            pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                    " HomeAdress, Email  FROM employee WHERE OtherName LIKE ?");
                        } else if (searchCategoryComboBox.getSelectedIndex() == 4) {
                            pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                    " HomeAdress, Email  FROM employee WHERE CAST(PhoneNo as CHAR) LIKE ?");
                        }
                        pst.setString(1,"%"+searchVariable+"%");
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            String employeeNo = rs.getString("EmployeeNo");
                            String firstName = rs.getString("FirstName");
                            String secondName = rs.getString("Surname");
                            String department = rs.getString("Department");
                            String phone = rs.getString("PhoneNo");
                            String yOE = rs.getString("employementDate");
                            String homeAddress = rs.getString("HomeAdress");
                            String email = rs.getString("Email");
                            model.addRow(new Object[]{employeeNo, firstName, secondName, department, phone, yOE, homeAddress, email});
                        }
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }

            }});
        backButton.addActionListener(e -> cancel(users));
        profileButton.addActionListener(e ->fullProfile(users));
        viewComboBox.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                DefaultTableModel model1 = new DefaultTableModel();
                model1.setColumnIdentifiers(columnNames);
                employeeTable.setModel(model1);
                try {
                    if (viewComboBox.getSelectedIndex() == 0) {
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, " +
                                "PhoneNo, employementDate, HomeAdress, Email  FROM employee");
                    }else if (viewComboBox.getSelectedIndex() == 1) {
                        String department = "Manager";
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee WHERE Department = ?");
                        pst.setString(1, department.toLowerCase());
                    }else if (viewComboBox.getSelectedIndex() == 2) {
                        String department = "RECEPTION";
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee WHERE Department = ?");
                        pst.setString(1, department.toLowerCase());
                    }else if (viewComboBox.getSelectedIndex() == 3) {
                        String department = "ADMINISTRATOR";
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee WHERE Department = ?");
                        pst.setString(1, department.toLowerCase());
                    }else if (viewComboBox.getSelectedIndex() == 4) {
                        String department = "FOOD COUNTER";
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee WHERE Department = ?");
                        pst.setString(1, department.toLowerCase());
                    }else if (viewComboBox.getSelectedIndex() == 5) {
                        String department = "ROOM ATTENDANT";
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee WHERE Department = ?");
                        pst.setString(1, department.toLowerCase());
                    }else if (viewComboBox.getSelectedIndex() == 6) {
                        String department = "CHEF";
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee WHERE Department = ?");
                        pst.setString(1, department.toLowerCase());
                    }else if (viewComboBox.getSelectedIndex() == 7) {
                        String department = "COOK";
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee WHERE Department = ?");
                        pst.setString(1, department.toLowerCase());
                    }else if (viewComboBox.getSelectedIndex() == 8) {
                        String department = "WAITER";
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee WHERE Department = ?");
                        pst.setString(1, department.toLowerCase());
                    }else {
                        pst = users.con.prepareStatement("SELECT EmployeeNo, FirstName, Surname, Department, PhoneNo, employementDate," +
                                " HomeAdress, Email  FROM employee");
                    }
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        String employeeNo = rs.getString("EmployeeNo");
                        String firstName = rs.getString("FirstName");
                        String secondName = rs.getString("Surname");
                        String department = rs.getString("Department");
                        String phone = rs.getString("PhoneNo");
                        String yOE = rs.getString("employementDate");
                        String homeAddress = rs.getString("HomeAdress");
                        String email = rs.getString("Email");
                        model1.addRow(new Object[]{employeeNo, firstName, secondName, department, phone, yOE, homeAddress, email});
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                }
            }
        });
    }
    private void fullProfile( Users users){
        int e = employeeTable.getSelectedRow();
        if (e>=0) {

            EmployeeNo = Integer.parseInt(employeeTable.getValueAt(e, 0).toString());
            //System.out.print("SELECTED ROW: " + e + "\n VALUE AT FIRST COLUMN: " + EmployeeNo);
            cancel(users);
            new EditEmployee(users,this);
        }else {
            JOptionPane.showMessageDialog(null, "SELECT AN EMPLOYEE TO EDIT OR DELETE FIRST!!");

        }

    }
    private void cancel(Users users){
        removeComponents(users);
        new Manager(users);
    }
    private void removeComponents(Users users){
        centrePanel1.removeAll();
        centrePanel1.repaint();
        centrePanel1.revalidate();
        mainLabelPanel.removeAll();
        mainLabelPanel.repaint();
        mainLabelPanel.revalidate();
        buttonPanel.removeAll();
        buttonPanel.repaint();
        buttonPanel.revalidate();
        eastPanel.removeAll();
        eastPanel.repaint();
        eastPanel.revalidate();
        westPanel.removeAll();
        westPanel.repaint();
        westPanel.revalidate();
        users.frame.remove(centrePanel1);
        users.frame.repaint();
        users.frame.revalidate();
        users.frame.remove(mainLabelPanel);
        users.frame.remove(buttonPanel);
        users.frame.remove(westPanel);
        users.frame.remove(eastPanel);
    }
}