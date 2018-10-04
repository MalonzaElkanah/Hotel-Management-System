package GeneralHotelManagement;

import LogIn.Users;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class EditEmployee {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel buttonPanel;
    private JPanel panel2;
    private JPanel mainLabelPanel;
    private JTextField surnameTextField;
    private JTextField firstNameTextField;
    private JTextField otherNameTextField;
    private JTextField dOBTextField;
    private JTextField iDTextField;
    private JTextField addressTextField;
    private JTextField phoneNOTextField;
    private JTextField emergencyPhoneTextField;
    private JTextField emailTextField;
    private JComboBox<String> eduLevelComboBox;
    private JComboBox<String> departmentTextField;
    private JTextField photoTextField;
    private JTextField cvTextField;
    private JTextField scanIDTextField;
    private PreparedStatement pst;
    private int employeeNo;

    EditEmployee(Users users, ViewEmployees viewEmployees){
        employeeNo = viewEmployees.EmployeeNo;
        Font forte = new Font("forte", Font.BOLD, 30);
        //Font forte1 = new Font("forte",Font.BOLD,20);
        JLabel managerLabel = new JLabel("EDIT EMPLOYEE");
        managerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        managerLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2, 1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(managerLabel);
        mainLabelPanel.add(mainLabelSeparator);
        //new employee detail
        JPanel[] panels = new JPanel[15];
        for (int j = 0; j < 15; j++) {
            panels[j] = new JPanel(new FlowLayout(FlowLayout.CENTER));
        }
        JLabel surnameLabel = new JLabel("Surname:");
        surnameTextField = new JTextField(20);
        panels[0].add(surnameLabel);
        panels[0].add(surnameTextField);
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameTextField = new JTextField(20);
        panels[1].add(firstNameLabel);
        panels[1].add(firstNameTextField);
        JLabel otherNameLabel = new JLabel("Other Name:");
        otherNameTextField = new JTextField(20);
        panels[2].add(otherNameLabel);
        panels[2].add(otherNameTextField);
        JLabel dOBLabel = new JLabel("Date Of Birth:");
        dOBTextField = new JTextField(20);
        panels[3].add(dOBLabel);
        panels[3].add(dOBTextField);
        JLabel iDLabel = new JLabel("National ID:");
        iDTextField = new JTextField(20);
        panels[4].add(iDLabel);
        panels[4].add(iDTextField);
        JLabel addressLabel = new JLabel("Home Address:");
        addressTextField = new JTextField(20);
        panels[5].add(addressLabel);
        panels[5].add(addressTextField);
        JLabel phoneNOLabel = new JLabel("Phone Number:");
        phoneNOTextField = new JTextField(20);
        panels[6].add(phoneNOLabel);
        panels[6].add(phoneNOTextField);
        JLabel emergencyPhoneLabel = new JLabel("Emergency Phone No.:");
        emergencyPhoneTextField = new JTextField(20);
        panels[7].add(emergencyPhoneLabel);
        panels[7].add(emergencyPhoneTextField);
        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(20);
        panels[8].add(emailLabel);
        panels[8].add(emailTextField);
        String[] educationDetail = {"PHD", "MASTERS", "DEGREE", "DIPLOMA", "CERTIFICATE", "KCSE", "KCPE"};
        JLabel eduLevelLabel = new JLabel("Education Level:");
        eduLevelComboBox = new JComboBox<>(educationDetail);
        panels[9].add(eduLevelLabel);
        panels[9].add(eduLevelComboBox);
        String[] department = {"RECEPTION", "FOOD COUNTER", "ADMINISTRATOR", "MANAGER", "CHEF", "WAITER", "ROOM ATTENDANT", "COOK", "GENETOR", "OTHERS"};
        JLabel departmentLabel = new JLabel("Department:");
        departmentTextField = new JComboBox<>(department);
        panels[10].add(departmentLabel);
        panels[10].add(departmentTextField);
        JLabel photoLabel = new JLabel("Photo Path:");
        photoTextField = new JTextField(20);
        JButton photoButton = new JButton("...");
        panels[11].add(photoLabel);
        panels[11].add(photoTextField);
        panels[11].add(photoButton);
        JLabel cvLabel = new JLabel("CV path:");
        cvTextField = new JTextField(20);
        JButton cvButton = new JButton("...");
        panels[12].add(cvLabel);
        panels[12].add(cvTextField);
        panels[12].add(cvButton);
        JLabel scanIdLabel = new JLabel("National ID path:");
        scanIDTextField = new JTextField(20);
        JButton scanIDButton = new JButton("...");
        panels[13].add(scanIdLabel);
        panels[13].add(scanIDTextField);
        panels[13].add(scanIDButton);
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        for (int j = 0; j < 14; j = j + 2) {
            panel.add(panels[j]);
        }
        JPanel panel1 = new JPanel(new GridLayout(8, 1, 10, 10));
        for (int j = 1; j < 14; j = j + 2) {
            panel1.add(panels[j]);
        }
        panel2 = new JPanel(new GridLayout(1, 2, 0, 10));
        panel2.add(panel);
        panel2.add(panel1);

        try {
            pst = users.con.prepareStatement("SELECT * FROM employee WHERE EmployeeNo = ?");
            pst.setInt(1,employeeNo);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                otherNameTextField.setText(rs.getString("OtherName"));
                firstNameTextField.setText(rs.getString("FirstName"));
                surnameTextField.setText(rs.getString("Surname"));
                iDTextField.setText(rs.getString("NationalIndentityNo"));
                emergencyPhoneTextField.setText(rs.getString("EmergencyPhoneNo"));
                phoneNOTextField.setText(rs.getString("PhoneNo"));
                photoTextField.setText(rs.getString("PhotoPath"));
                addressTextField.setText(rs.getString("HomeAdress"));
                emailTextField.setText(rs.getString("Email"));
                cvTextField.setText(rs.getString("CVPath"));
                scanIDTextField.setText(rs.getString("ScanNationIDPath"));
                String eduLevel1 = rs.getString("EducationLevel");
                String department1 = rs.getString("Department");
                switch (department1){
                    case "RECEPTION":departmentTextField.setSelectedIndex(0);
                    case "FOOD COUNTER":departmentTextField.setSelectedIndex(1);
                    case "ADMINISTRATOR":departmentTextField.setSelectedIndex(2);
                    case "MANAGER":departmentTextField.setSelectedIndex(3);
                    case "CHEF":departmentTextField.setSelectedIndex(4);
                    case "WAITER": departmentTextField.setSelectedIndex(5);
                    case "ROOM ATTENDANT":departmentTextField.setSelectedIndex(6);
                    case "COOK":departmentTextField.setSelectedIndex(7);
                    case "GENETOR":departmentTextField.setSelectedIndex(8);
                    case "OTHERS": departmentTextField.setSelectedIndex(9);
                }
                switch (eduLevel1){
                    case "PHD":eduLevelComboBox.setSelectedIndex(0);
                    case "MASTERS":eduLevelComboBox.setSelectedIndex(1);
                    case "DEGREE":eduLevelComboBox.setSelectedIndex(2);
                    case "DIPLOMA":eduLevelComboBox.setSelectedIndex(3);
                    case "CERTIFICATE":eduLevelComboBox.setSelectedIndex(4);
                    case "KCSE":eduLevelComboBox.setSelectedIndex(5);
                    case "KCPE":eduLevelComboBox.setSelectedIndex(6);
                }
                dOBTextField.setText(rs.getDate("DateOfBirth").toString());
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        //BUTTONS
        GridLayout buttonGridLayout = new GridLayout(2, 6, 15, 0);
        buttonPanel = new JPanel(buttonGridLayout);
        JButton cancelButton = new JButton("CANCEL");
        JButton deleteButton = new JButton("DELETE");
        JButton addButton = new JButton("UPDATE EMPLOYEE");
        buttonPanel.add(new JLabel());
        buttonPanel.add(cancelButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(new JLabel());
        for (int j = 1; j <= 6; j++) {
            buttonPanel.add(new JLabel());
        }
        //WEST border
        JSeparator westSeparator = new JSeparator();
        JLabel west = new JLabel();
        westPanel = new JPanel();
        westPanel.add(westSeparator);
        westPanel.add(west);
        //East border
        JSeparator eastSeparator = new JSeparator();
        JLabel east = new JLabel();
        eastPanel = new JPanel();
        eastPanel.add(eastSeparator);
        eastPanel.add(east);
        //outer frame
        BorderLayout borderLayout = new BorderLayout(50, 15);
        users.frame.setLayout(borderLayout);
        users.frame.add(mainLabelPanel, BorderLayout.NORTH);
        users.frame.add(panel2, BorderLayout.CENTER);
        users.frame.add(westPanel, BorderLayout.WEST);
        users.frame.add(eastPanel, BorderLayout.EAST);
        users.frame.add(buttonPanel, BorderLayout.SOUTH);
        //ACTION LISTENERS
        photoButton.addActionListener(e -> photoPath(panels[11]));
        cvButton.addActionListener(e -> cvPath(panels[12]));
        scanIDButton.addActionListener(e -> scanIDPath(panels[13]));
        addButton.addActionListener(e-> update(users));
        deleteButton.addActionListener(e -> delete(users));
        cancelButton.addActionListener(e->cancel(users));
    }
    private void update(Users users){
        String surname;
        String fName;
        String oName;
        int nationalID;
        String homeAddress;
        int phoneNo;
        int emergencyPhoneNo;
        String email;
        String eduLevel = "DEGREE";
        String department1 = "OTHERS";
        String photoPath;
        String cvPath;
        String scanNationID;
        surname= surnameTextField.getText();
        fName = firstNameTextField.getText();
        oName = otherNameTextField.getText();
        email = emailTextField.getText();
        homeAddress = addressTextField.getText();
        if (eduLevelComboBox.getSelectedIndex()==0){
            eduLevel="PHD";
        }else if (eduLevelComboBox.getSelectedIndex()==1){
            eduLevel="MASTERS";
        }else if (eduLevelComboBox.getSelectedIndex()==2){
            eduLevel="DEGREE";
        }else if (eduLevelComboBox.getSelectedIndex()==3){
            eduLevel="DIPLOMA";
        }else if (eduLevelComboBox.getSelectedIndex()==4){
            eduLevel="CERTIFICATE";
        }else if (eduLevelComboBox.getSelectedIndex()==5){
            eduLevel="KCSE";
        }else if (eduLevelComboBox.getSelectedIndex()==6){
            eduLevel="KCPE";
        }
        if (departmentTextField.getSelectedIndex()==0){
            department1="RECEPTION";
        }else if (departmentTextField.getSelectedIndex()==1){
            department1="FOOD COUNTER";
        }else if (departmentTextField.getSelectedIndex()==2){
            department1="ADMINISTRATOR";
        }else if (departmentTextField.getSelectedIndex()==3){
            department1="MANAGER";
        }else if (departmentTextField.getSelectedIndex()==4){
            department1="CHEF";
        }else if (departmentTextField.getSelectedIndex()==5){
            department1="WAITER";
        }else if (departmentTextField.getSelectedIndex()==6){
            department1="ROOM ATTENDANT";
        }else if (departmentTextField.getSelectedIndex()==7){
            department1="COOK";
        }else if (departmentTextField.getSelectedIndex()==8){
            department1="GENETOR";
        }else if (departmentTextField.getSelectedIndex()==9){
            department1="OTHERS";
        }
        photoPath = photoTextField.getText();
        cvPath = cvTextField.getText();
        scanNationID = scanIDTextField.getText();
        if (!surname.equals("")||!oName.equals("")||!fName.equals("")||email.equals("")) {
            try {
                String sql = "UPDATE employee \n" +
                        "SET FirstName = ?, Surname = ?, OtherName = ?, NationalIndentityNo = ?, DateOfBirth = ?, HomeAdress = ?, PhoneNo = ?, \n" +
                        "EmergencyPhoneNo = ?, Email = ?, EducationLevel = ?, Department = ?, PhotoPath = ?, CVPath = ?, ScanNationIDPath = ?" +
                        "WHERE EmployeeNo = ?;";
                pst = users.con.prepareStatement(sql);
                pst.setString(2, surname);
                pst.setString(1, fName);
                pst.setString(3, oName);
                try {
                    Date DOB = new Date(Integer.parseInt(dOBTextField.getText()));
                    nationalID = Integer.parseInt(iDTextField.getText());
                    emergencyPhoneNo = Integer.parseInt(emergencyPhoneTextField.getText());
                    phoneNo = Integer.parseInt(phoneNOTextField.getText());
                    pst.setInt(4, nationalID);
                    pst.setInt(7, phoneNo);
                    pst.setInt(8, emergencyPhoneNo);
                    pst.setDate(5, DOB);
                }catch (NumberFormatException nfe ){
                    JOptionPane.showMessageDialog(null,"National ID/ Phone Number/" +
                            " emergency phone No/ Date of Birth Fields is Empty or Wrong Number Format");
                }
                pst.setString(6, homeAddress);
                pst.setString(9, email);
                pst.setString(10, eduLevel);
                pst.setString(11, department1);
                pst.setString(12, photoPath);
                pst.setString(13, cvPath);
                pst.setString(14, scanNationID);
                pst.setInt(15,employeeNo);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null,"Employee: "+fName+" "+surname+" UPDATED successfully");
                cancel(users);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,ex.getMessage());
            }
        }else {
            JOptionPane.showMessageDialog(null,"Fields can never be null");
        }
    }
    private void delete(Users users){
        int j = JOptionPane.showConfirmDialog(null,"Do you want to DELETE this Employee?","",
                JOptionPane.YES_NO_OPTION);
        if (j==0){
            try {
                pst = users.con.prepareStatement("DELETE FROM employee WHERE EmployeeNo = ?");
                pst.setInt(1,employeeNo);
                pst.execute();
                JOptionPane.showMessageDialog(null,"Employee DELETED");
                cancel(users);
            }catch (SQLException se){
                se.printStackTrace();
            }
        }
    }
    private void cancel(Users users){
        mainLabelPanel.removeAll();
        mainLabelPanel.repaint();
        mainLabelPanel.revalidate();
        panel2.removeAll();
        panel2.repaint();
        panel2.revalidate();
        buttonPanel.removeAll();
        buttonPanel.repaint();
        buttonPanel.revalidate();
        eastPanel.removeAll();
        eastPanel.repaint();
        eastPanel.revalidate();
        westPanel.removeAll();
        westPanel.repaint();
        westPanel.revalidate();
        users.frame.remove(panel2);
        users.frame.repaint();
        users.frame.revalidate();
        users.frame.remove(mainLabelPanel);
        users.frame.remove(buttonPanel);
        users.frame.remove(westPanel);
        users.frame.remove(eastPanel);
        new ViewEmployees(users);
    }
    private String photoPath(Component parent){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            String string = fileChooser.getSelectedFile().getAbsolutePath();
            photoTextField.setText(string);
            return fileChooser.getSelectedFile().getAbsolutePath();
        }else{
            photoTextField.setText(null);
            return null;
        }
    }
    private String cvPath(Component parent){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            String string = fileChooser.getSelectedFile().getAbsolutePath();
            cvTextField.setText(string);
            return fileChooser.getSelectedFile().getAbsolutePath();
        }else{
            cvTextField.setText(null);
            return null;
        }
    }
    private String scanIDPath(Component parent){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            String string = fileChooser.getSelectedFile().getAbsolutePath();
            scanIDTextField.setText(string);
            return fileChooser.getSelectedFile().getAbsolutePath();
        }else{
            scanIDTextField.setText(null);
            return null;
        }
    }
}
