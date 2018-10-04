package HotelRoomManagement;

import LogIn.Users;
import LogIn.GenerateReceipt;
import com.lowagie.text.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reception {
    private JButton checkInButton;
    private JScrollPane scrollPane;
    private JPanel viewPanel;
    private JPanel buttonPanel;
    private JPanel centrePanel;
    JPanel panel2;
    JPanel jPanel;
    private JComboBox<String> viewComboBox;
    private JTable availableRoomTable;
    private JTable bookedRoomTable;
    private JTable occupiedRoomTable;
    private JTable previousTransactionTable;
    private PreparedStatement pst;
    String roomType;
    int roomNo;
    Float roomPrice;
    Float alreadyPaid;
    String names;
    int Id;
    int index;
    public Reception(Users users){
        JPanel mainLabelPanel;
        JPanel eastPanel;
        JPanel westPanel;
        JPanel southPanel;
        //main label
        Font forte = new Font("forte",Font.BOLD,30);
        JLabel receptionLabel = new JLabel("RECEPTION PANEL");
        receptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        receptionLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2,1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(receptionLabel);
        mainLabelPanel.add(mainLabelSeparator);
        /*
        * FIRST TAB
        * */
        //View by
        String [] views ={"AVAILABLE ROOMS","ALL ROOMS"};
        viewComboBox = new JComboBox<>(views);
        viewPanel = new JPanel(new GridLayout(1, 5, 10, 1));
        viewPanel.add(new JLabel());
        viewPanel.add(new JLabel("VIEW:", SwingConstants.TRAILING));
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        comboBoxPanel.add(viewComboBox);
        viewPanel.add(comboBoxPanel);
        viewPanel.add(new JLabel());
        viewPanel.add(new JLabel());
        //Available room table
        availableRoomTable = new JTable();
        availableRoomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(availableRoomTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        checkInButton = new JButton("CHECK IN / BOOK");
        BorderLayout borderLayout2 = new BorderLayout(25,15);
        panel2 =new JPanel(borderLayout2);
        panel2.add(viewPanel, BorderLayout.NORTH);
        panel2.add(scrollPane, BorderLayout.CENTER);
        panel2.add(checkInButton, BorderLayout.SOUTH);
        /*
        * SECOND TAB
        * */
        //search system
        JLabel bookedRoomLabel = new JLabel("BOOKED ROOMS",SwingConstants.CENTER);
        Font forte1 = new Font("forte",Font.BOLD,14);
        bookedRoomLabel.setFont(forte1);
        JLabel searchLabel = new JLabel("SEARCH: ",SwingConstants.TRAILING);
        JTextField searchTextField = new JTextField(20);
        JLabel searchCategoryLabel = new JLabel("Search by: ",SwingConstants.TRAILING);
        String [] searchVariables = {"Name","National ID","ROOM"};
        JComboBox<String> searchCategoryComboBox = new JComboBox<>(searchVariables);
        JPanel searchPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JPanel searchPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        searchPanel2.add(searchLabel);
        searchPanel2.add(searchTextField);
        searchPanel1.add(searchCategoryLabel);
        searchPanel1.add(searchCategoryComboBox);
        JPanel searchPanel = new JPanel(new GridLayout(3,1,1,1));
        searchPanel.add(bookedRoomLabel);  searchPanel.add(searchPanel2);searchPanel.add(searchPanel1);
        //Table booked rooms
        bookedRoomTable = new JTable();
        bookedRoomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane bookedScrollPane = new JScrollPane(bookedRoomTable);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(bookedScrollPane,BorderLayout.CENTER);
        //COMBINE
        centrePanel = new JPanel(new BorderLayout(0,15));
        centrePanel.add(searchPanel,BorderLayout.NORTH);
        centrePanel.add(tablePanel,BorderLayout.CENTER);
        //BUTTONS
        GridLayout buttonGridLayout = new GridLayout(2,5,0,0);
        buttonPanel = new JPanel(buttonGridLayout);
        JButton unBookButton = new JButton("REMOVE ");
        JButton checkInButton2 = new JButton("CHECK IN");
        buttonPanel.add(new JLabel());
        buttonPanel.add(unBookButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(checkInButton2);
        buttonPanel.add(new JLabel());
        for (int j=1;j<=5;j++){
            buttonPanel.add(new JLabel());
        }
        jPanel = new JPanel(new BorderLayout(10,10));
        jPanel.add(centrePanel,BorderLayout.CENTER);
        jPanel.add(buttonPanel,BorderLayout.SOUTH);
        /*
        * THIRD TAB
        * */
        //search system
        JLabel occupiedRoomLabel = new JLabel("OCCUPIED ROOMS",SwingConstants.CENTER);
        occupiedRoomLabel.setFont(forte1);
        JLabel searchLabel1 = new JLabel("SEARCH: ",SwingConstants.TRAILING);
        JTextField searchTextField1 = new JTextField(15);
        JLabel searchCategoryLabel1 = new JLabel("Search by: ",SwingConstants.TRAILING);
        String[] searchVariables2 = {"Names","ID NUMBER","ROOM NUMBER"};
        JComboBox<String> searchCategoryComboBox1 = new JComboBox<>(searchVariables2);
        JPanel searchPanel21 = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JPanel searchPanel11 = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        searchPanel21.add(searchLabel1);
        searchPanel21.add(searchTextField1);
        searchPanel11.add(searchCategoryLabel1);
        searchPanel11.add(searchCategoryComboBox1);
        JPanel searchPanel4 = new JPanel(new GridLayout(3,1,1,5));
        searchPanel4.add(occupiedRoomLabel);  searchPanel4.add(searchPanel21);searchPanel4.add(searchPanel11);
        //table
        JPanel occupiedRoomPanel = new JPanel(new BorderLayout(1,10));
        occupiedRoomTable = new JTable();
        occupiedRoomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane occupiedTableScrollPane = new JScrollPane(occupiedRoomTable);
        //BUTTONS
        GridLayout buttonGridLayout1 = new GridLayout(2,5,0,0);
        JPanel buttonPanel1 = new JPanel(buttonGridLayout1);
        JButton flagButton = new JButton("FLAG ");
        JButton checkOutButton = new JButton("CHECK OUT");
        buttonPanel1.add(new JLabel());
        buttonPanel1.add(flagButton);
        buttonPanel1.add(new JLabel());
        buttonPanel1.add(checkOutButton);
        buttonPanel1.add(new JLabel());
        for (int j=1;j<=5;j++){
            buttonPanel1.add(new JLabel());
        }
        occupiedRoomPanel.add(searchPanel4,BorderLayout.NORTH);
        occupiedRoomPanel.add(occupiedTableScrollPane,BorderLayout.CENTER);
        occupiedRoomPanel.add(buttonPanel1,BorderLayout.SOUTH);
        /*
        * 4TH TAB
        * */
        JPanel previousTransactionPanel;
        JLabel previousLabel = new JLabel("PREVIOUS RECORD",SwingConstants.CENTER);
        previousLabel.setFont(forte1);
        previousTransactionTable = new JTable();
        JScrollPane prevTransactionScrollPane = new JScrollPane(previousTransactionTable);
        previousTransactionPanel = new JPanel(new BorderLayout(10,10));
        previousTransactionPanel.add(previousLabel,BorderLayout.NORTH);
        previousTransactionPanel.add(prevTransactionScrollPane,BorderLayout.CENTER);
        //south border
        JSeparator southSeparator = new JSeparator();
        JLabel south = new JLabel();
        southPanel = new JPanel();
        southPanel.add(southSeparator);
        southPanel.add(south);
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
        //TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("AVAILABLE ROOMS",panel2);
        tabbedPane.addTab("BOOKED ROOMS ",jPanel);
        tabbedPane.addTab("OCCUPIED ROOMS ",occupiedRoomPanel);
        tabbedPane.addTab("PREVIOUS ROOM RECORDS ",previousTransactionPanel);
        //outer frame
        BorderLayout borderLayout = new BorderLayout(25,15);
        users.frame.setLayout(borderLayout);
        users.frame.add(mainLabelPanel, BorderLayout.NORTH);
        users.frame.add(tabbedPane,BorderLayout.CENTER);
        users.frame.add(southPanel, BorderLayout.SOUTH);
        users.frame.add(westPanel, BorderLayout.WEST);
        users.frame.add(eastPanel, BorderLayout.EAST);
        refresh(users);
        //Action listeners
        unBookButton.addActionListener(e -> {
            int b = bookedRoomTable.getSelectedRow();
            if (b>=0) {
                index = Integer.parseInt(bookedRoomTable.getValueAt(b, 0).toString());
                names = bookedRoomTable.getValueAt(b,1).toString();
                roomNo = Integer.parseInt(bookedRoomTable.getValueAt(b, 3).toString());
                int option = JOptionPane.showConfirmDialog(null,"DO YOU WANT TO UN-BOOK "+names,
                        "UN-BOOK "+names,JOptionPane.YES_NO_OPTION);
                if (option==0) {
                    try {
                        String sql = "DELETE FROM room_customers WHERE Customer_ID = ? ";
                        pst = users.con.prepareStatement(sql);
                        pst.setInt(1, index);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "" + names + " UN-BOOKED successfully");
                        sql = "UPDATE rooms \n" +
                                "SET State = ? " +
                                "WHERE Room_No = ?;";
                        pst = users.con.prepareStatement(sql);
                        pst.setString(1, "AVAILABLE");
                        pst.setInt(2, roomNo);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "ROOM " + roomNo + " Updated successfully");

                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"NO CUSTOMER WAS SELECTED");
            }
            refresh(users);
        });
        checkInButton2.addActionListener(e->{
            int b = bookedRoomTable.getSelectedRow();
            if (b>=0) {
                index = Integer.parseInt(bookedRoomTable.getValueAt(b, 0).toString());
                names = bookedRoomTable.getValueAt(b,1).toString();
                Id = Integer.parseInt(bookedRoomTable.getValueAt(b, 2).toString());
                roomNo = Integer.parseInt(bookedRoomTable.getValueAt(b, 3).toString());
                try {
                    alreadyPaid = Float.parseFloat(bookedRoomTable.getValueAt(b, 7).toString());
                }catch (NullPointerException npe){
                    alreadyPaid = 0F;
                }
                try{
                    String sql = "SELECT * FROM rooms WHERE Room_No = ? ";
                    pst = users.con.prepareStatement(sql);
                    pst.setInt(1, roomNo);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        roomType = rs.getString("Room_Type");
                        roomPrice = rs.getFloat("Price_Per_Night");
                    }
                }catch(SQLException se){
                    se.printStackTrace();
                }
                jPanel.removeAll();
                jPanel.repaint();
                jPanel.revalidate();
                new BookedRooms(users,this);

            }else {
                JOptionPane.showMessageDialog(null, "SELECT A ROOM TO CHECK IN FIRST!!");
            }
        });
        searchTextField1.getDocument().addDocumentListener(new DocumentListener() {
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
                String[] columnName2 = {"Index","Names","NationID No","RoomNumber","Check-In Date","Amount Paid",
                        "Food Expenses","Room Expenses","Other Expenses","Total Amount","Balance To Pay","Checked In By"};

                String searchResults = searchTextField1.getText();
                DefaultTableModel occupiedTableModel1 = new DefaultTableModel(){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                occupiedTableModel1.setColumnIdentifiers(columnName2);
                occupiedRoomTable.setModel(occupiedTableModel1);
                try {
                    ResultSet rs;
                    if (searchCategoryComboBox1.getSelectedIndex()==0){
                        String Name = "%"+searchResults+"%";
                        String sql = "SELECT * FROM occupied_rooms WHERE Names LIKE ? ";
                        pst = users.con.prepareStatement(sql);
                        pst.setString(1, Name.toLowerCase());
                        rs = pst.executeQuery();
                    }else if (searchCategoryComboBox1.getSelectedIndex()==1) {
                        String ID = "%" + searchResults + "%";
                        String sql = "SELECT * FROM occupied_rooms WHERE CAST(National_ID as CHAR) LIKE ? ";
                        pst = users.con.prepareStatement(sql);
                        pst.setString(1, ID);
                        rs = pst.executeQuery();
                    }else if (searchCategoryComboBox1.getSelectedIndex()==2) {
                        String room = "%" + searchResults + "%";
                        String sql = "SELECT * FROM occupied_rooms WHERE CAST(Room_No as CHAR) LIKE ?";
                        pst = users.con.prepareStatement(sql);
                        pst.setString(1, room);
                        rs = pst.executeQuery();
                    }else{
                        String sql = "SELECT * FROM occupied_rooms";
                        pst = users.con.prepareStatement(sql);
                        rs = pst.executeQuery();
                    }
                    int []customerId;
                    pst = users.con.prepareStatement("SELECT DISTINCT Customer_ID FROM room_food_customers");
                    ResultSet rs1 = pst.executeQuery();
                    int j=0;
                    while(rs1.next()){
                        j++;
                    }
                    customerId = new int[j+1];
                    rs1.beforeFirst();
                    j=0;
                    while(rs1.next()){
                        customerId[j] = rs1.getInt("Customer_ID");
                        j++;
                    }
                    String sql;
                    while (rs.next()) {
                        float foodPrice=0;
                        for (int k=0;k<=j;k++){
                            if (customerId[k]==rs.getInt("Customer_ID")){
                                pst = users.con.prepareStatement("SELECT price, Quantity FROM room_food_customers " +
                                        "WHERE Customer_ID = ?");
                                pst.setInt(1,customerId[k]);
                                ResultSet rs2 = pst.executeQuery();
                                while(rs2.next()){
                                    foodPrice = foodPrice+(rs2.getFloat("Quantity")*
                                            rs2.getFloat("price"));
                                }
                            }
                        }
                        int days=0;
                        sql = "SELECT Check_In_Date FROM occupied_rooms WHERE Room_No = ? ";
                        pst = users.con.prepareStatement(sql);
                        pst.setInt(1, rs.getInt("Room_No"));
                        ResultSet rs3 = pst.executeQuery();
                        if (rs3.next()){
                            sql= "SET @d1 = CURDATE(), @d2 = ?;";
                            pst = users.con.prepareStatement(sql);
                            pst.setDate(1, rs3.getDate(1));
                            pst.executeQuery();
                            sql= "SELECT DATEDIFF(@d1,@d2) AS 'days'";
                            pst = users.con.prepareStatement(sql);
                            ResultSet rs2 = pst.executeQuery();
                            while(rs2.next()){
                                days = rs2.getInt(1);
                            }
                        }
                        float roomExpenses = rs.getFloat("Price_Per_Night")*days;
                        float extraExpenses;
                        try {
                            extraExpenses =rs.getFloat("Extra_Payments");
                        }catch(NumberFormatException nfe){
                            extraExpenses=0;
                        }
                        occupiedTableModel1.addRow(new Object[]{rs.getInt("Customer_ID"),rs.getString("Names"),
                                rs.getString("National_ID"), rs.getString("Room_No"),
                                rs.getString("Check_In_Date"),rs.getString("Room_Total_Price"),
                                foodPrice,roomExpenses, extraExpenses, foodPrice+roomExpenses+extraExpenses,
                                (foodPrice+roomExpenses+extraExpenses)-rs.getFloat("Room_Total_Price"),
                                rs.getString("Checked_In_By")
                        });
                    }
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }
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
                String[] columnName1 = {"Index","Names","NationID No","Room Number","Room Type","Price Per Day",
                        "Expected Check-in Date", "Amount Already Paid","Booked By"};
                String searchResults = searchTextField.getText();
                DefaultTableModel bookedTableModel1 = new DefaultTableModel(){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                bookedTableModel1.setColumnIdentifiers(columnName1);
                bookedRoomTable.setModel(bookedTableModel1);
                try{
                    if (searchCategoryComboBox.getSelectedIndex()==0){
                        String Name = "%"+searchResults+"%";
                        String sql = "SELECT * FROM booked_rooms WHERE Names LIKE ? ";
                        pst = users.con.prepareStatement(sql);
                        pst.setString(1, Name.toLowerCase());

                    }else if (searchCategoryComboBox.getSelectedIndex()==1){
                        String idNum = "%"+searchResults+"%";
                        String sql = "SELECT * FROM booked_rooms WHERE CAST(National_ID as CHAR) LIKE ? ";
                        pst = users.con.prepareStatement(sql);
                        pst.setString(1, idNum);
                    }else if (searchCategoryComboBox.getSelectedIndex()==2) {
                        String room = "%" + searchResults + "%";
                        String sql = "SELECT * FROM booked_rooms WHERE CAST(Room_No as CHAR) LIKE ?";
                        pst = users.con.prepareStatement(sql);
                        pst.setString(1, room);
                    }
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        bookedTableModel1.addRow(new Object[]{rs.getString("Customer_ID"),
                                rs.getString("Names"), rs.getString("National_ID"),
                                rs.getString("Room_No"), rs.getString("Room_Type"),
                                rs.getString("Price_Per_Night"),
                                rs.getString("Check_In_Date"),
                                rs.getString("Room_Total_Price"),
                                rs.getString("Checked_In_By")
                        });
                    }
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }
        });
        flagButton.addActionListener(e ->{
            int row = occupiedRoomTable.getSelectedRow();
            String state = "FLAG";
            if (row>=0){
                int room = Integer.parseInt(occupiedRoomTable.getValueAt(row,3).toString());
                String name = occupiedRoomTable.getValueAt(row,1).toString();
                int option = JOptionPane.showConfirmDialog(null,
                        "DO YOU WANT TO FLAG THIS ROOM CUSTOMER","FLAG "+name,JOptionPane.YES_NO_OPTION);
                if (option==0){
                    try {
                        String sql = "UPDATE room_customers \n" +
                                "SET State = ? \n" +
                                "WHERE Room_No = ?;";
                        pst = users.con.prepareStatement(sql);
                        pst.setString(1, state);
                        pst.setInt(2, room);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null,"CUSTOMER "+name+" FLAGGED");
                    }catch (SQLException se){
                        se.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"Please Select a RoomCustomer");
            }
            refresh(users);
        });
        checkInButton.addActionListener(e->checkIn(users));
        viewComboBox.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String[] columns = {"Type","Room Number","Price","State"};
                DefaultTableModel model1 = new DefaultTableModel(){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                availableRoomTable.setModel(model1);
                try {
                    if (viewComboBox.getSelectedIndex() == 1) {
                        model1.setColumnIdentifiers(columns);
                        pst = users.con.prepareStatement("SELECT * FROM rooms");
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            model1.addRow(new Object[]{rs.getString("Room_Type"),
                                    rs.getString("Room_No"),
                                    rs.getString("Price_Per_Night"), rs.getString("State")});
                        }
                    }else if (viewComboBox.getSelectedIndex() == 0) {
                        model1.setColumnIdentifiers(columns);
                        String state = "AVAILABLE";
                        pst = users.con.prepareStatement("SELECT * FROM rooms " +
                                "WHERE State = ?");
                        pst.setString(1, state.toLowerCase());
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            model1.addRow(new Object[]{rs.getString("Room_Type"),
                                    rs.getString("Room_No"),
                                    rs.getString("Price_Per_Night"), rs.getString("State")});
                        }
                    }else {
                        model1.setColumnIdentifiers(columns);
                        pst = users.con.prepareStatement("SELECT * FROM rooms");
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            model1.addRow(new Object[]{rs.getString("Room_Type"),
                                    rs.getString("Room_No"),
                                    rs.getString("Price_Per_Night"), rs.getString("State")});
                        }
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                }
            }
        });
        JTextField amountTextField = new JTextField(20);
        checkOutButton.addActionListener(e -> {
            int index;
            float totalPrice;
            float balance;
            float paid;
            float roomExpenses;
            float foodAmount ;
            float otherExpenses;
            float amountPaid;
            int f = occupiedRoomTable.getSelectedRow();
            if (f>=0) {
                index = Integer.parseInt(occupiedRoomTable.getValueAt(f,0).toString());
                String name = occupiedRoomTable.getValueAt(f,1).toString();
                String nationalID = occupiedRoomTable.getValueAt(f,2).toString();
                int room = Integer.parseInt(occupiedRoomTable.getValueAt(f, 3).toString());
                String checkInDate = occupiedRoomTable.getValueAt(f,4).toString();
                try {
                    paid = Float.parseFloat(occupiedRoomTable.getValueAt(f, 5).toString());
                }catch (NullPointerException npe){
                    paid = 0f;
                }
                try {
                    foodAmount = Float.parseFloat(occupiedRoomTable.getValueAt(f, 6).toString());
                }catch (NullPointerException npe){
                    foodAmount = 0f;
                }
                try {
                    otherExpenses = Float.parseFloat(occupiedRoomTable.getValueAt(f, 8).toString());
                }catch (NullPointerException npe){
                    otherExpenses = 0f;
                }
                try{
                    roomExpenses = Float.parseFloat(occupiedRoomTable.getValueAt(f,7).toString());
                }catch (NullPointerException npe){
                    roomExpenses=0;
                }
                try{
                    totalPrice = Float.parseFloat(occupiedRoomTable.getValueAt(f,9).toString());
                }catch(NullPointerException npe){
                    totalPrice=roomExpenses+otherExpenses+foodAmount;
                }
                try{
                    balance = Float.parseFloat(occupiedRoomTable.getValueAt(f,10).toString());
                }catch(NullPointerException npe){
                    balance=roomExpenses+otherExpenses+foodAmount-paid;
                }
                String [] paymentOption = {"Cash","M-Pesa","CREDIT CARD"};
                JComboBox<String> paymentMethodComboBox = new JComboBox<>(paymentOption);
                JPanel textFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                textFieldPanel.add(amountTextField);
                comboPanel.add(paymentMethodComboBox);
                JPanel jPanel = new JPanel(new GridLayout(13,2,10,10));
                jPanel.add(new JLabel("NAME: "));jPanel.add(new JLabel(name));
                jPanel.add(new JLabel("ID NO: "));jPanel.add(new JLabel(nationalID));
                jPanel.add(new JLabel("ROOM NO: "));jPanel.add(new JLabel(""+room));
                jPanel.add(new JLabel("CHECK IN DATE:"));jPanel.add(new JLabel(checkInDate));
                jPanel.add(new JLabel("Room Expenses: "));jPanel.add(new JLabel(""+roomExpenses));
                jPanel.add(new JLabel("Food Expenses: "));jPanel.add(new JLabel(""+foodAmount));
                jPanel.add(new JLabel("Other Expenses: "));jPanel.add(new JLabel(""+otherExpenses));
                jPanel.add(new JLabel("Total Amount Spent: "));jPanel.add(new JLabel(""+totalPrice));
                jPanel.add(new JLabel("Amount Previously Paid: "));jPanel.add(new JLabel(""+paid));
                jPanel.add(new JLabel("Balance To Pay: "));jPanel.add(new JLabel(""+balance));
                jPanel.add(new JLabel("Cash Given: "));jPanel.add(textFieldPanel);
                jPanel.add(new JLabel("Method of Payment: "));jPanel.add(comboPanel);
                Object [] options = {"CHECK OUT","CANCEL"};
                int ans =JOptionPane.showOptionDialog(null,jPanel,"Check Out",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (ans ==0){
                    try {
                        amountPaid = Integer.parseInt(amountTextField.getText());
                    }catch (NumberFormatException nfe){
                        amountPaid = 0f;
                    }
                    balance = balance - amountPaid;
                    if (balance<=0){
                        try {
                            String sql = "UPDATE room_customers \n" +
                                    "SET Check_Out_Date = ?, Room_Total_Price = ?, Checked_Out_By = ?, State = ? \n" +
                                    "WHERE Room_No = ?;";
                            PreparedStatement pst = users.con.prepareStatement(sql);
                            long millis = System.currentTimeMillis();
                            java.sql.Date todayDate = new java.sql.Date(millis);
                            pst.setDate(1,todayDate);
                            pst.setFloat(2,totalPrice);
                            pst.setString(3, users.userName);
                            pst.setString(4, "PREVIOUS");
                            pst.setInt(5, room);
                            pst.executeUpdate();
                            sql = "UPDATE rooms \n" +
                                    "SET State = ? " +
                                    "WHERE Room_No = ?;";
                            pst  = users.con.prepareStatement(sql);
                            pst.setString(1, "AVAILABLE");
                            pst.setInt(2,room);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null,
                                    "CHECKED OUT SUCCESSFULLY. "+"ROOM "+room+" Updated successfully");
                            sql = "UPDATE food_customers \n" +
                                    "SET State = ? " +
                                    "WHERE Customer_Room_ID = ?";
                            pst  = users.con.prepareStatement(sql);
                            pst.setString(1, "PAID");
                            pst.setInt(2,index);
                            pst.executeUpdate();
                            int days =1;
                            pst = users.con.prepareStatement("SET @d1 = '"+todayDate.toString()+
                                    "', @d2 = '"+checkInDate+"';");
                            pst.executeQuery();
                            sql= "SELECT DATEDIFF(@d1,@d2) AS 'days'";
                            pst = users.con.prepareStatement(sql);
                            ResultSet rs2 = pst.executeQuery();
                            while(rs2.next()){
                                days = rs2.getInt(1);
                            }
                            int p = JOptionPane.showConfirmDialog(null,"Click yes to generate Receipt",
                                    "RECEIPT", JOptionPane.YES_NO_OPTION);
                            if(p==0){
                                try{
                                    Paragraph [] paragraphs = new Paragraph[50];
                                    paragraphs[0]=new Paragraph("Customer Name: "+name);
                                    paragraphs[1]=new Paragraph("Room Number: "+room);
                                    paragraphs[2]=new Paragraph("Check In Date: "+checkInDate);
                                    paragraphs[3]=new Paragraph("Check Out Date: "+todayDate);
                                    paragraphs[4]=new Paragraph("Days: "+days);
                                    paragraphs[5]=new Paragraph("Room Expenses: "+roomExpenses);
                                    paragraphs[6]=new Paragraph("Food Expenses: " + foodAmount);
                                    paragraphs[7]=new Paragraph("Other Expenses: " + otherExpenses);
                                    paragraphs[8]=new Paragraph("TOTAL AMOUNT SPENT " + totalPrice,
                                            FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, com.lowagie.text.Font.BOLDITALIC));
                                    paragraphs[9]=new Paragraph("Previous Amount Paid: " + paid);
                                    paragraphs[10]=new Paragraph("Amount Paid: " + amountPaid);
                                    paragraphs[11]=new Paragraph("Payment Method: ");
                                    if (foodAmount>0){
                                        paragraphs[12]=new Paragraph("_____________________________________ ");
                                        paragraphs[13]=new Paragraph("Qty - Food Name _________ Total Amount ",
                                                FontFactory.getFont(FontFactory.TIMES_BOLD,11, com.lowagie.text.Font.BOLD));
                                        paragraphs[14]=new Paragraph("______________________________________");
                                        float totalAmount = 0;
                                        int k=15;
                                        try{
                                            pst = users.con.prepareStatement(
                                                    "SELECT * FROM room_food_customers WHERE Customer_ID = ?");
                                            pst.setLong(1,index);
                                            ResultSet rs = pst.executeQuery();
                                            while (rs.next()) {
                                                float totalPrice1  =rs.getFloat("price")*rs.getFloat("Quantity");
                                                paragraphs[k]=new Paragraph(""+rs.getFloat("Quantity")+"--"+
                                                        rs.getString("food_name")+"----------------"+
                                                        totalPrice1);
                                                totalAmount = totalAmount+totalPrice1;
                                                k++;
                                            }
                                            paragraphs[k]=new Paragraph("_________________________________ ");
                                            paragraphs[k+1]=new Paragraph("FOOD SUB-TOTAL-------------"+totalAmount,
                                                    FontFactory.getFont(FontFactory.TIMES_ROMAN,12,
                                                            com.lowagie.text.Font.BOLDITALIC));
                                            paragraphs[k+2]=new Paragraph("_________________________________ ");
                                        }catch (SQLException se){
                                            se.printStackTrace();
                                        }
                                    }
                                    new GenerateReceipt(users,index,paragraphs);
                                }catch(Exception ex ){
                                    ex.printStackTrace();
                                }
                            }
                            refresh(users);
                        }catch (SQLException se){
                            se.printStackTrace();
                        }
                    }else {
                        try {
                            String sql = "UPDATE room_customers \n" +
                                    "SET Room_Total_Price = ? \n" +
                                    "WHERE Room_No = ?;";
                            pst  = users.con.prepareStatement(sql);
                            pst.setFloat(1,(paid+amountPaid));
                            pst.setInt(2, room);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null,
                                    " AMOUNT ALREADY PAID UPDATED SUCCESSFULLY");
                            JOptionPane.showMessageDialog(null,
                                    " BALANCE OF "+(totalPrice-(paid+amountPaid))+
                                            "AVAILABLE!!.. ROOM NOT CHECKED OUT");
                            refresh(users);
                        }catch (SQLException se){
                            se.printStackTrace();
                        }
                    }
                }else if(ans==1){
                    JOptionPane.showMessageDialog(null, " ROOM NOT CHECKED OUT");
                }

                refresh(users);
            }else {
                JOptionPane.showMessageDialog(null,
                        "PLEASE SELECT A CUSTOMER TO CHECK OUT FIRST!!");
            }
        });
    }
    private void checkIn(Users users){
        int e = availableRoomTable.getSelectedRow();
        if (e>=0) {
            roomType = availableRoomTable.getValueAt(e,0).toString();
            roomNo = Integer.parseInt(availableRoomTable.getValueAt(e, 1).toString());
            roomPrice = Float.parseFloat(availableRoomTable.getValueAt(e,2).toString());
            String states = availableRoomTable.getValueAt(e,3).toString().toLowerCase();
            if (states.equals("available")) {
                //System.out.print("SELECTED ROW: " + e + "\n VALUE AT FIRST COLUMN: " + EmployeeNo);
                panel2.removeAll();
                panel2.repaint();
                panel2.revalidate();
                new CheckIn(users, this);
            }else {
                JOptionPane.showMessageDialog(null, "SELECT AN AVAILABLE ROOM TO CHECK IN!!");
            }
        }else {
            JOptionPane.showMessageDialog(null, "SELECT A ROOM TO CHECK IN FIRST!!");
        }
    }
    void refresh(Users users){
        //available
        viewComboBox.setSelectedIndex(0);
        String[] columns = {"Type","Room Number","Price","State"};
        DefaultTableModel model1 = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableRoomTable.setModel(model1);
        model1.setColumnIdentifiers(columns);
        String state = "AVAILABLE";
        try {
            pst = users.con.prepareStatement("SELECT * FROM rooms WHERE State = ?");
            pst.setString(1, state.toLowerCase());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model1.addRow(new Object[]{rs.getString("Room_Type"), rs.getString("Room_No"),
                        rs.getString("Price_Per_Night"), rs.getString("State")});
            }

        }catch (SQLException se){
            se.printStackTrace();
        }
        //booked
        String[] columnName1 = {"Index","Names","NationID No","Room Number","Room Type","Price Per Day",
                "Expected Check-in Date", "Amount Already Paid","Booked By"};
        DefaultTableModel bookedTableModel1 = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookedTableModel1.setColumnIdentifiers(columnName1);
        bookedRoomTable.setModel(bookedTableModel1);
        try {
            String sql = "SELECT * FROM booked_rooms";
            pst = users.con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                bookedTableModel1.addRow(new Object[]{rs.getString("Customer_ID"),
                        rs.getString("Names"), rs.getString("National_ID"),
                        rs.getString("Room_No"), rs.getString("Room_Type"),
                        rs.getString("Price_Per_Night"), rs.getString("Check_In_Date"),
                        rs.getString("Room_Total_Price"), rs.getString("Checked_In_By")
                });
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //occupied table
        String[] columnName2 = {"Index","Names","NationID No","RoomNumber","Check-In Date","Amount Paid",
                "Food Expenses","Room Expenses","Other Expenses","Total Amount","Balance To Pay","Checked In By" };
        DefaultTableModel occupiedTableModel1 = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        occupiedTableModel1.setColumnIdentifiers(columnName2);
        occupiedRoomTable.setModel(occupiedTableModel1);
        try {
            int []customerId;
            pst = users.con.prepareStatement("SELECT DISTINCT Customer_ID FROM room_food_customers");
            ResultSet rs1 = pst.executeQuery();
            int j=0;
            while(rs1.next()){
                j++;
            }
            customerId = new int[j+1];
            rs1.beforeFirst();
            j=0;
            while(rs1.next()){
                customerId[j] = rs1.getInt("Customer_ID");
                System.out.println(customerId[j]);
                j++;
            }
            pst = users.con.prepareStatement("SELECT * FROM occupied_rooms");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                float foodPrice=0;
                for (int k=0;k<=j;k++){
                    System.out.println(""+k);
                    if (customerId[k]==rs.getInt("Customer_ID")){
                        pst = users.con.prepareStatement("SELECT price, Quantity FROM room_food_customers " +
                                "WHERE Customer_ID = ?");
                        pst.setInt(1,customerId[k]);
                        ResultSet rs2 = pst.executeQuery();
                        while(rs2.next()){
                            foodPrice = foodPrice+(rs2.getFloat("Quantity")*rs2.getFloat("price"));
                            System.out.println(customerId[k]+"- food price: "+foodPrice);
                        }
                    }
                }
                int days=0;
                String sql = "SELECT Check_In_Date FROM occupied_rooms WHERE Room_No = ? ";
                pst = users.con.prepareStatement(sql);
                pst.setInt(1, rs.getInt("Room_No"));
                ResultSet rs3 = pst.executeQuery();
                if (rs3.next()){
                    sql= "SET @d1 = CURDATE(), @d2 = ?;";
                    pst = users.con.prepareStatement(sql);
                    pst.setDate(1, rs3.getDate(1));
                    pst.executeQuery();
                    sql= "SELECT DATEDIFF(@d1,@d2) AS 'days'";
                    pst = users.con.prepareStatement(sql);
                    ResultSet rs2 = pst.executeQuery();
                    while(rs2.next()){
                        days = rs2.getInt(1);
                    }
                }
                float roomExpenses = rs.getFloat("Price_Per_Night")*days;
                float extraExpenses;
                try {
                    extraExpenses =rs.getFloat("Extra_Payments");
                }catch(NumberFormatException nfe){
                    extraExpenses=0;
                }
                occupiedTableModel1.addRow(new Object[]{rs.getInt("Customer_ID"),
                        rs.getString("Names"),
                        rs.getString("National_ID"), rs.getString("Room_No"),
                        rs.getString("Check_In_Date"),rs.getString("Room_Total_Price"),
                        foodPrice,roomExpenses, extraExpenses, foodPrice+roomExpenses+extraExpenses,
                        (foodPrice+roomExpenses+extraExpenses)-rs.getFloat("Room_Total_Price"),
                        rs.getString("Checked_In_By")
                });
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //previous room
        String[] columnNamePrev = {"Names","NationID No","RoomNumber","Check-In Date","Check-out Date","Room Amount Paid",
                "Checked In By"};
        DefaultTableModel prevTransactionTableModel1 = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        prevTransactionTableModel1.setColumnIdentifiers(columnNamePrev);
        previousTransactionTable.setModel(prevTransactionTableModel1);
        try{
            state = "PREVIOUS";
            pst = users.con.prepareStatement("SELECT * FROM room_customers WHERE State = ?");
            pst.setString(1, state.toLowerCase());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                prevTransactionTableModel1.addRow(new Object[]{rs.getString("Names"),
                        rs.getString("National_ID"),
                        rs.getString("Room_No"), rs.getString("Check_In_Date"),
                        rs.getString("Check_Out_Date"), rs.getString("Room_Total_Price"),
                        rs.getString("Checked_In_By")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
    }
    void addTab(){
        BorderLayout borderLayout2 = new BorderLayout(25,15);
        panel2.setLayout(borderLayout2);
        panel2.add(viewPanel, BorderLayout.NORTH);
        panel2.add(scrollPane, BorderLayout.CENTER);
        panel2.add(checkInButton, BorderLayout.SOUTH);
    }
    void addTab2(){
        jPanel.setLayout(new BorderLayout(10,10));
        jPanel.add(centrePanel,BorderLayout.CENTER);
        jPanel.add(buttonPanel,BorderLayout.SOUTH);
    }
}