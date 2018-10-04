package GeneralHotelManagement;

import LogIn.Users;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager {
    private JPanel reportPanel;
    private JPanel mainLabelPanel;
    private JPanel btnPanel3;
    private JPanel sidePanel;
    private JComboBox <String>durationComboBox;
    private JTable foodSaleTable;
    private JTable checkOutTable;
    private JTable checkInTable;
    private JTable bookedTable;
    private JTable productsTable;
    private JTable summaryTable;
    private JLabel roomOccupiedTextArea;
    private JLabel roomSaleTextArea;
    private JLabel roomBookedTextArea;
    private JLabel foodSaleTextArea;
    public Manager(Users users){
        JButton foodMgmtBtn;//east
        JButton EmpBtn;
        JButton roomMgmtBtn;
        JButton logOutBtn;
        //main saleLabel
        Font cambria = new Font("cambria",Font.BOLD,15);
        Font forte = new Font("forte",Font.BOLD,30);
        Font forte1 = new Font("forte",Font.BOLD,20);
        JLabel managersLabel = new JLabel("MANAGERS PANEL");
        managersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        managersLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2,1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(managersLabel);
        mainLabelPanel.add(mainLabelSeparator);
        //Top
        JLabel durationLabel = new JLabel("Report Duration:");
        String [] duration= {"ALL","YEAR","MONTH","WEEK","DAY"};
        durationComboBox = new JComboBox<>(duration);
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        durationPanel.add(durationLabel); durationPanel.add(durationComboBox);
        JPanel timeDurationPanel = new JPanel(new BorderLayout(10,10));
        timeDurationPanel.add(durationPanel,BorderLayout.NORTH);
        JPanel topDetailPanel = new JPanel(new GridLayout(1,3,0,0));
        JLabel saleLabel1 = new JLabel("DETAILED SALES REPORT",SwingConstants.LEADING);
        saleLabel1.setFont(forte1);
        topDetailPanel.add(timeDurationPanel);
        topDetailPanel.add(saleLabel1);topDetailPanel.add(new JLabel());
        //table- food sale
        float totalFoodSale=0;
        JLabel foodSaleLabel = new JLabel("food sale");
        foodSaleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foodSaleTable = new JTable();
        JScrollPane scrollPane2 = new JScrollPane(foodSaleTable);
        //table - room Sale
        float totalRoomSale =0;
        JLabel checkOutsLabel = new JLabel("Check-Outs");
        checkOutsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        checkOutTable = new JTable();
        JScrollPane scrollPane1 = new JScrollPane(checkOutTable);
        //Check-in
        float totalOccupiedRoomPrice = 0;
        JLabel checkInLabel = new JLabel("Rooms Occupied");
        checkInLabel.setHorizontalAlignment(SwingConstants.CENTER);
        checkInTable = new JTable();
        JScrollPane jsp = new JScrollPane(checkInTable);
        //Table - booked
        float totalBookedRoomPrice = 0;
        JLabel bookedLabel = new JLabel("Check-Outs");
        bookedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookedTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookedTable);
        //summary table
        JLabel totalEarningLabel = new JLabel("Summary");
        totalEarningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        summaryTable = new JTable();
        JScrollPane summaryScrollPane = new JScrollPane(summaryTable);

        //food sale report
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(foodSaleLabel,BorderLayout.NORTH);
        panel.add(scrollPane2,BorderLayout.CENTER);
        foodSaleTextArea = new JLabel("Food Total Sale: "+totalFoodSale);
        foodSaleTextArea.setFont(cambria);
        panel.add(foodSaleTextArea,BorderLayout.SOUTH);
        //booked rooms
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(bookedLabel, BorderLayout.NORTH);
        panel1.add(scrollPane, BorderLayout.CENTER);
        String roomsBooked = totalBookedRoomPrice+" kSH";
        roomBookedTextArea = new JLabel( "Room Total Returns: "+roomsBooked);
        roomBookedTextArea.setFont(cambria);
        panel1.add(roomBookedTextArea,BorderLayout.SOUTH);
        //room checked out
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.add(checkOutsLabel,BorderLayout.NORTH);
        panel2.add(scrollPane1, BorderLayout.CENTER);
        String roomSale = totalRoomSale+" kSH";
        roomSaleTextArea = new JLabel( "Room Total Returns: "+roomSale);
        roomSaleTextArea.setFont(cambria);
        panel2.add(roomSaleTextArea,BorderLayout.SOUTH);
        //room occupied
        JPanel panel3 = new JPanel(new BorderLayout(5,5));
        panel3.add(checkInLabel, BorderLayout.NORTH);
        panel3.add(jsp, BorderLayout.CENTER);
        String roomOccupied = totalOccupiedRoomPrice+" kSH";
        roomOccupiedTextArea = new JLabel( "Total Amount Paid: "+roomOccupied);
        roomOccupiedTextArea.setFont(cambria);
        panel3.add(roomOccupiedTextArea,BorderLayout.SOUTH);
        //Store
        productsTable = new JTable();
        JScrollPane productsScrollPane = new JScrollPane(productsTable);
        JPanel productsPanel = new JPanel(new BorderLayout(5,5));
        productsPanel.add(productsScrollPane,BorderLayout.CENTER);
        //summary
        JPanel summaryPanel = new JPanel(new BorderLayout(5,5));
        summaryPanel.add(totalEarningLabel, BorderLayout.NORTH);
        summaryPanel.add(summaryScrollPane, BorderLayout.CENTER);
        //TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("FOOD SALE REPORT",panel);
        tabbedPane.addTab("ROOMS Checked-Out",panel2);
        tabbedPane.addTab("ROOMS Occupied",panel3);
        tabbedPane.addTab("Booked ROOMS",panel1);
        tabbedPane.addTab("Store",productsPanel);
        tabbedPane.addTab("Total Earnings",summaryPanel);
        //SIDE BUTTONS
        GridLayout sideGrid = new GridLayout(4,1,1,40);
        foodMgmtBtn = new JButton("FOOD MANAGEMENT");
        EmpBtn = new JButton("EMPLOYEE MANAGEMENT");
        roomMgmtBtn = new JButton("ROOM MANAGEMENT");
        logOutBtn = new JButton("LOG OUT");
        sidePanel = new JPanel(sideGrid);
        sidePanel.add(foodMgmtBtn);
        sidePanel.add(EmpBtn);
        sidePanel.add(roomMgmtBtn);
        sidePanel.add(logOutBtn);
        //add components to frame
        BorderLayout borderLayout4 = new BorderLayout(1,20);
        btnPanel3 = new JPanel(borderLayout4);
        reportPanel = new JPanel(new BorderLayout(10,1));
        reportPanel.add(topDetailPanel,BorderLayout.NORTH);
        reportPanel.add(tabbedPane,BorderLayout.CENTER);
        reportPanel.revalidate();
        reportPanel.repaint();
        users.frame.setLayout(new BorderLayout(20,10));
        users.frame.add(mainLabelPanel,BorderLayout.NORTH);
        users.frame.add(sidePanel, BorderLayout.EAST);
        users.frame.add(btnPanel3,BorderLayout.WEST);
        users.frame.add(reportPanel, BorderLayout.CENTER);
        tableData(users);
        foodMgmtBtn.addActionListener(e -> {
            removeComponents(users);
            new FoodManagement(users);
        });
        EmpBtn.addActionListener(e-> createEmployee(users));
        roomMgmtBtn.addActionListener(e->{
            removeComponents(users);
            new RoomManagement(users);
        });
        logOutBtn.addActionListener(e->logOut(users));
        durationComboBox.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                tableData(users);
            }
        });

    }
    private void tableData(Users users){
        //table- food sale
        float totalFoodSale1=0;
        String[] columnName = {"Customer_ID", "Food", "Quantity","Time", "Date", "TotalPrice"};
        DefaultTableModel foodSaleModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        foodSaleModel.setColumnIdentifiers(columnName);
        foodSaleTable.setModel(foodSaleModel);
        try{
            PreparedStatement pst2;
            if(durationComboBox.getSelectedIndex() == 1) {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 YEAR)");
            }else if (durationComboBox.getSelectedIndex() == 2){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 MONTH)");
            }else if (durationComboBox.getSelectedIndex() == 3){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 WEEK)");
            } else if (durationComboBox.getSelectedIndex() == 4){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 DAY)");
            }else {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 50 YEAR)");
            }
            ResultSet rs1 = pst2.executeQuery();
            while(rs1.next()){
                String state = "PAID";
                PreparedStatement pst1 = users.con.prepareStatement("SELECT * FROM food_orders " +
                        "WHERE State = ? AND Date >= ? ");
                pst1.setString(1, state.toLowerCase());
                pst1.setDate(2,rs1.getDate(1));
                ResultSet rs = pst1.executeQuery();
                while (rs.next()) {
                    foodSaleModel.addRow(new Object[]{rs.getString("Customer_ID"),
                            rs.getString("food_name"), rs.getFloat("Quantity"),
                            rs.getString("Time"), rs.getString("Date"),
                            rs.getFloat("Amount_Paid")
                    });
                    totalFoodSale1 = totalFoodSale1+rs.getFloat("Amount_Paid");
                }
            }
            foodSaleTextArea.setText("Food Total Sale: "+totalFoodSale1);
        }catch (SQLException se){
            se.printStackTrace();
        }
        //room checked out
        float totalRoomSale1 =0;
        String[] columnName1 = {"Customer_ID","Names","Room No", "Nights", "Total Amount Paid",
              "Checked In Date","Checked Out Date","Check In By","Check Out BY"};
        DefaultTableModel checkOutModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        checkOutModel.setColumnIdentifiers(columnName1);
        checkOutTable.setModel(checkOutModel);
        try{
            PreparedStatement pst2;
            if(durationComboBox.getSelectedIndex() == 1) {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 YEAR)");
            }else if (durationComboBox.getSelectedIndex() == 2){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 MONTH)");
            }else if (durationComboBox.getSelectedIndex() == 3){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 WEEK)");
            } else if (durationComboBox.getSelectedIndex() == 4){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 DAY)");
            }else {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 50 YEAR)");
            }
            ResultSet rs1 = pst2.executeQuery();
            while(rs1.next()) {
                PreparedStatement pst1 = users.con.prepareStatement("SELECT * FROM Room_Customers " +
                        "WHERE State = ? AND Check_Out_Date >= ?");
                pst1.setString(1, "PREVIOUS");
                pst1.setDate(2,rs1.getDate(1));
                ResultSet rs = pst1.executeQuery();
                while (rs.next()) {
                    int days = 0;
                    String sql = "SET @d1 = ?, @d2 = ?;";
                    pst1 = users.con.prepareStatement(sql);
                    pst1.setDate(1, rs.getDate("Check_Out_Date"));
                    pst1.setDate(2, rs.getDate("Check_In_Date"));
                    pst1.executeQuery();
                    sql = "SELECT DATEDIFF(@d1,@d2) AS 'days'";
                    pst1 = users.con.prepareStatement(sql);
                    ResultSet rs2 = pst1.executeQuery();
                    while (rs2.next()) {
                        days = rs2.getInt(1);
                    }
                    checkOutModel.addRow(new Object[]{rs.getInt("Customer_ID"), rs.getString("Names"),
                            rs.getString("Room_No"), days, rs.getString("Room_Total_Price"),
                            rs.getDate("Check_In_Date"), rs.getDate("Check_Out_Date"),
                            rs.getString("Checked_In_By"), rs.getString("Checked_Out_By")
                    });
                    totalRoomSale1 = totalRoomSale1 + rs.getFloat("Room_Total_Price");
                }
                roomSaleTextArea.setText("Room Total Return: " + totalRoomSale1);
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //Check-in
        float totalOccupiedRoomPrice1 = 0;
        String[] columnName2 = {"RoomNumber",
              "Check-In Date","Days Stayed","Amount Paid",
            "Food Expenses","Room Expenses","Other Expenses","Total Amount","Balance To Pay","Checked In By"};
        DefaultTableModel checkInModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        checkInModel.setColumnIdentifiers(columnName2);
        checkInTable.setModel(checkInModel);
        try{
            int []customerId;
            PreparedStatement pst1 = users.con.prepareStatement("SELECT DISTINCT Customer_ID FROM room_food_customers");
            ResultSet rs1 = pst1.executeQuery();
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
            PreparedStatement pst2;
            if(durationComboBox.getSelectedIndex() == 1) {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 YEAR)");
            }else if (durationComboBox.getSelectedIndex() == 2){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 MONTH)");
            }else if (durationComboBox.getSelectedIndex() == 3){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 WEEK)");
            } else if (durationComboBox.getSelectedIndex() == 4){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 DAY)");
            }else {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 50 YEAR)");
            }
            ResultSet results = pst2.executeQuery();
            while(results.next()) {
                pst1 = users.con.prepareStatement("SELECT * FROM occupied_rooms WHERE Check_In_Date >= ?");
                pst1.setDate(1,results.getDate(1));
                ResultSet rs = pst1.executeQuery();
                while (rs.next()) {
                    float foodPrice = 0;
                    for (int k = 0; k <= j; k++) {
                        if (customerId[k] == rs.getInt("Customer_ID")) {
                            pst1 = users.con.prepareStatement("SELECT price, Quantity FROM room_food_customers " +
                                    "WHERE Customer_ID = ?");
                            pst1.setInt(1, customerId[k]);
                            ResultSet rs2 = pst1.executeQuery();
                            while (rs2.next()) {
                                foodPrice = foodPrice + (rs2.getFloat("Quantity") *
                                        rs2.getFloat("price"));
                            }
                        }
                    }
                    int days = 0;
                    String sql = "SELECT Check_In_Date FROM room_customers WHERE Room_No = ? ";
                    pst1 = users.con.prepareStatement(sql);
                    pst1.setInt(1, rs.getInt("Room_No"));
                    ResultSet rs3 = pst1.executeQuery();
                    if (rs3.next()) {
                        sql = "SET @d1 = CURDATE(), @d2 = ?;";
                        pst1 = users.con.prepareStatement(sql);
                        pst1.setDate(1, rs3.getDate(1));
                        pst1.executeQuery();
                        sql = "SELECT DATEDIFF(@d1,@d2) AS 'days'";
                        pst1 = users.con.prepareStatement(sql);
                        ResultSet rs2 = pst1.executeQuery();
                        while (rs2.next()) {
                            days = rs2.getInt(1);
                        }
                    }
                    float roomExpenses = rs.getFloat("Price_Per_Night") * days;
                    float extraExpenses;
                    try {
                        extraExpenses = rs.getFloat("Extra_Payments");
                    } catch (NumberFormatException nfe) {
                        extraExpenses = 0;
                    }
                    checkInModel.addRow(new Object[]{rs.getString("Room_No"),
                            rs.getString("Check_In_Date"), days, rs.getString("Room_Total_Price"),
                            foodPrice, roomExpenses, extraExpenses, foodPrice + roomExpenses + extraExpenses,
                            (foodPrice + roomExpenses + extraExpenses) - rs.getFloat("Room_Total_Price"),
                            rs.getString("Checked_In_By")
                    });
                    totalOccupiedRoomPrice1 = totalOccupiedRoomPrice1 + rs.getFloat("Room_Total_Price");
                }
                roomOccupiedTextArea.setText("Total Amount Paid: " + totalOccupiedRoomPrice1);
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //Table - booked
        float totalBookedRoomPrice1 = 0;
        String[] columnName3 = {"Room Number","Room Type","Price Per Day",
              "Expected Check-in Date", "Amount Already Paid","Booked By"};
        DefaultTableModel bookedModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookedModel.setColumnIdentifiers(columnName3);
        bookedTable.setModel(bookedModel);
        try{
            PreparedStatement pst2;
            if(durationComboBox.getSelectedIndex() == 1) {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 YEAR)");
            }else if (durationComboBox.getSelectedIndex() == 2){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 MONTH)");
            }else if (durationComboBox.getSelectedIndex() == 3){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 WEEK)");
            } else if (durationComboBox.getSelectedIndex() == 4){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 DAY)");
            }else {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 50 YEAR)");
            }
            ResultSet results = pst2.executeQuery();
            while(results.next()) {
                String sql = "SELECT * FROM booked_rooms WHERE Check_In_Date >= ?";
                PreparedStatement pst1 = users.con.prepareStatement(sql);
                pst1.setDate(1,results.getDate(1));
                ResultSet rs = pst1.executeQuery();
                while (rs.next()) {
                    bookedModel.addRow(new Object[]{rs.getString("Room_No"), rs.getString("Room_Type"),
                            rs.getString("Price_Per_Night"), rs.getString("Check_In_Date"),
                            rs.getString("Room_Total_Price"), rs.getString("Checked_In_By")
                    });
                    totalBookedRoomPrice1 = totalBookedRoomPrice1 + rs.getFloat("Room_Total_Price");
                }
                roomBookedTextArea.setText("Room Total Return: "+totalBookedRoomPrice1);
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //store
        float totalStoreExpenses =0;
        String[] storeColumnName = {"name","unit","Category","alert unit","Cost","Supplied Date","Supplier_name"};
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(storeColumnName);
        productsTable.setModel(model);
        try{
            PreparedStatement pst2;
            if(durationComboBox.getSelectedIndex() == 1) {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 YEAR)");
            }else if (durationComboBox.getSelectedIndex() == 2){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 MONTH)");
            }else if (durationComboBox.getSelectedIndex() == 3){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 WEEK)");
            } else if (durationComboBox.getSelectedIndex() == 4){
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 1 DAY)");
            }else {
                pst2 = users.con.prepareStatement("SELECT DATE_SUB(CURDATE(),INTERVAL 50 YEAR)");
            }
            ResultSet results = pst2.executeQuery();
            while(results.next()) {
                PreparedStatement pst = users.con.prepareStatement("SELECT * FROM product_supplier WHERE supplied_date >= ?");
                pst.setDate(1,results.getDate(1));
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString(2), rs.getInt(3),
                            rs.getString(4), rs.getInt(5), rs.getFloat(6),
                            rs.getDate(7), rs.getString(9)});
                    totalStoreExpenses = totalStoreExpenses + rs.getFloat(6);
                }
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //summary
        float totalEarnings;
        totalEarnings = totalRoomSale1+totalFoodSale1;
        String[] columnName4 = {"Activity","Duration","Revenue", "Expenses"};
        DefaultTableModel summaryModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        summaryModel.setColumnIdentifiers(columnName4);
        summaryTable.setModel(summaryModel);
        summaryModel.addRow(new Object[]{"Food Orders",durationComboBox.getSelectedItem(),totalFoodSale1,0.0});
        summaryModel.addRow(new Object[]{"Checked out Rooms",durationComboBox.getSelectedItem(),totalRoomSale1,0.0});
        summaryModel.addRow(new Object[]{"Store Expenses",durationComboBox.getSelectedItem(),0.0,totalStoreExpenses});
        summaryModel.addRow(new Object[]{"TOTAL ",durationComboBox.getSelectedItem(),totalEarnings,totalStoreExpenses});

    }
    private void removeComponents(Users users){
        mainLabelPanel.removeAll();
        mainLabelPanel.repaint();
        mainLabelPanel.revalidate();
        reportPanel.removeAll();
        reportPanel.repaint();
        reportPanel.revalidate();
        btnPanel3.removeAll();
        btnPanel3.repaint();
        btnPanel3.revalidate();
        sidePanel.removeAll();
        sidePanel.repaint();
        sidePanel.revalidate();
        users.frame.remove(reportPanel);
        users.frame.repaint();
        users.frame.revalidate();
        users.frame.remove(btnPanel3);
        users.frame.remove(sidePanel);
        //users.frame.remove(southPanel);
    }
    private void logOut(Users users){
        try{
            removeComponents(users);
            users.addComponents();
            users.con.close();
            new Users();
        }catch (SQLException se){
            JOptionPane.showMessageDialog(null, "LOG OUT failed");
            se.getStackTrace();
        }
    }
    private void createEmployee(Users user){
        removeComponents(user);
        new ViewEmployees(user);
    }
}