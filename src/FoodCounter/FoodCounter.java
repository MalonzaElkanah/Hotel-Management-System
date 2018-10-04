package FoodCounter;

import LogIn.GenerateReceipt;
import LogIn.Users;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodCounter {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JPanel panel2;
    private JPanel mainLabelPanel;
    private PreparedStatement pst;
    private JTable foodOrderTable;
    public FoodCounter(Users users){
        //main label
        Font forte = new Font("forte",Font.BOLD,30);
        JLabel foodCounterLabel = new JLabel("FOOD COUNTER PANEL");
        foodCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foodCounterLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2,1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(foodCounterLabel);
        mainLabelPanel.add(mainLabelSeparator);
        //food order table
        JLabel orderedLabel = new JLabel("FOOD ORDERED");
        orderedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        String[] columnName = {"Receipt No","Food Ordered","Price","Time","Date"};
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(columnName);
        foodOrderTable  = new JTable(model);
        foodOrderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(foodOrderTable);
        JButton payOrderButton = new JButton("PAY ORDER");
        JButton addRoomOrder = new JButton("ADD TO ROOM CUSTOMER");
        JPanel southButtonPanel = new JPanel(new GridLayout(1,5));
        southButtonPanel.add(new JLabel());
        southButtonPanel.add(payOrderButton);
        southButtonPanel.add(new JLabel());
        southButtonPanel.add(addRoomOrder);
        southButtonPanel.add(new JLabel());
        long []receiptNo;
        try{
            String state = "NOT_PAID";
            pst = users.con.prepareStatement("SELECT DISTINCT Customer FROM food_orders WHERE State = ?");
            pst.setString(1, state.toLowerCase());
            ResultSet rs = pst.executeQuery();
            int init = 0;
            while (rs.next()){
                init++;
            }
            receiptNo = new long[init+1];
            rs.beforeFirst();
            int init1 = 0;
            while (rs.next()) {
                receiptNo[init1] = rs.getLong("Customer");
                init1++;
            }
            for (int j=0;j<init;j++) {
                String food = "";
                float totalPrice = 0F;
                String time = "";
                String date = "";
                pst = users.con.prepareStatement("SELECT * FROM food_orders WHERE State = ? AND Customer = ?");
                pst.setString(1, state.toLowerCase());
                pst.setLong(2,receiptNo[j]);
                rs = pst.executeQuery();
                while (rs.next()) {
                    food = food+" "+rs.getString("Quantity")+" "+rs.getString("food_name")+",";
                    time = rs.getString("Time");
                    date = rs.getString("Date");
                    totalPrice = totalPrice+(rs.getFloat("Amount_Paid"));
                }
                model.addRow(new Object[]{receiptNo[j], food, totalPrice, time, date});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //Buttons
        JButton orderButton = new JButton("ORDER");
        JButton menuButton = new JButton("MENU");
        JButton paidOrdersButton = new JButton("PAID ORDERS");
        JButton logOutButton = new JButton("REFRESH");
        BorderLayout borderLayout2 = new BorderLayout(25,15);
        GridLayout gridLayout1 = new GridLayout(4,1,0,20);
        panel2 =new JPanel(borderLayout2);
        panel2.add(orderedLabel, BorderLayout.NORTH);
        panel2.add(scrollPane, BorderLayout.CENTER);
        panel2.add(southButtonPanel, BorderLayout.SOUTH);
        JPanel panel3 = new JPanel(gridLayout1);
        panel3.add(orderButton);
        panel3.add(menuButton);
        panel3.add(paidOrdersButton);
        panel3.add(logOutButton);
        // COMBINE BUTTONS AND Available table
        panel2.add(panel3, BorderLayout.EAST);
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
        //outer frame
        BorderLayout borderLayout = new BorderLayout(25,15);
        users.frame.setLayout(borderLayout);
        users.frame.add(mainLabelPanel, BorderLayout.NORTH);
        users.frame.add(panel2,BorderLayout.CENTER);
        users.frame.add(southPanel, BorderLayout.SOUTH);
        users.frame.add(westPanel, BorderLayout.WEST);
        users.frame.add(eastPanel, BorderLayout.EAST);
        users.frame.setVisible(true);
        //Listeners
        payOrderButton.addActionListener(e -> {
            int row = foodOrderTable.getSelectedRow();
            if (row>=0){
                long customer = Long.parseLong(foodOrderTable.getValueAt(row, 0).toString());
                String food = foodOrderTable.getValueAt(row,1).toString();
                float price = Float.parseFloat(foodOrderTable.getValueAt(row,2).toString());
                float balance;
                float cash = 0;
                //JTextField balanceTextField = new JTextField();
                JTextField cashTextField = new JTextField(8);
                JPanel jPanel = new JPanel(new GridLayout(6,2,10,10));
                jPanel.add(new JLabel("CUSTOMER ID: "));jPanel.add(new JLabel(""+customer));
                jPanel.add(new JLabel("FOOD ORDERED: "));jPanel.add(new JLabel(food));
                jPanel.add(new JLabel("TOTAL PRICE: "));jPanel.add(new JLabel(""+price));
                jPanel.add(new JLabel("CASH GIVEN: "));jPanel.add(cashTextField);
                //jPanel.add(new JLabel("BALANCE: "));jPanel.add(balanceTextField);
                JPanel optionPanel = new JPanel(new BorderLayout());
                JLabel label = new JLabel("PAY FOOD");
                optionPanel.add(label,BorderLayout.NORTH);
                optionPanel.add(jPanel,BorderLayout.CENTER);
                Object [] options = {"PAY","CANCEL"};
                int ans =JOptionPane.showOptionDialog(null,optionPanel,"PAY FOOD",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (ans ==0){
                    try {
                        cash = Float.parseFloat(cashTextField.getText());
                    }catch (NumberFormatException Nfe){
                        JOptionPane.showMessageDialog(null,
                                "ENTER THE CORRECT NUMBER FORMAT AT CASH");
                    }
                    balance= cash-price;
                    if (balance>=0){
                        try{
                            String sql = "UPDATE food_customers \n" +
                                    "SET State = ?" +
                                    "WHERE Customer = ?;";
                            pst  = users.con.prepareStatement(sql);
                            pst.setString(1, "PAID");
                            pst.setLong(2,customer);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null,
                                    "FOOD PAID for Customer_ID "+customer+". Give Balance of "+balance);
                            int p = JOptionPane.showConfirmDialog(null,"Click yes to generate Receipt",
                                    "RECEIPT", JOptionPane.YES_NO_OPTION);
                            if(p==0) {
                                try {
                                    Paragraph[] paragraphs = new Paragraph[50];
                                    paragraphs[0]=new Paragraph("_____________________________________ ");
                                    paragraphs[1]=new Paragraph("Qty - Food Name _________ Total Amount ",
                                            FontFactory.getFont(FontFactory.TIMES_BOLD,11, com.lowagie.text.Font.BOLD));
                                    paragraphs[2]=new Paragraph("______________________________________");
                                    float totalAmount = 0;
                                    int k=3;
                                    pst = users.con.prepareStatement(
                                            "SELECT * FROM food_orders WHERE Customer = ?");
                                    pst.setLong(1,customer);
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
                                    paragraphs[k+2]=new Paragraph(" ");
                                    paragraphs[k+3]=new Paragraph("CASH GIVEN: "+cash);
                                    paragraphs[k+4]=new Paragraph("GIVE CHANGE: "+balance);
                                    new GenerateReceipt(users,customer,paragraphs);
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        }catch (SQLException se){
                            se.printStackTrace();
                        }
                    }else {
                        JOptionPane.showMessageDialog(null,
                                "FOOD NOT FULLY PAID for Customer_ID "+customer);
                    }
                    refresh(users);
                }
            }else{
                JOptionPane.showMessageDialog(null, "SELECT A CUSTOMER FIRST!!");
            }
        });
        addRoomOrder.addActionListener(e -> {
            int row = foodOrderTable.getSelectedRow();
            if (row>=0) {
                long customer = Long.parseLong(foodOrderTable.getValueAt(row,0).toString());
                String[] columnName1 = {"Customer_ID","Names","RoomNumber"};
                DefaultTableModel model1 = new DefaultTableModel(){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                model1.setColumnIdentifiers(columnName1);
                try{
                    String state = "OCCUPIED";
                    PreparedStatement pst;
                    pst = users.con.prepareStatement("SELECT Customer_ID, Names, Room_No " +
                            "FROM room_customers WHERE State = ?");
                    pst.setString(1,state);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        model1.addRow(new Object[]{rs.getString("Customer_ID"),
                                rs.getString("Names"),
                                rs.getString("Room_No")});
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                }
                JTable roomCustomerTable = new JTable(model1);
                roomCustomerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                JScrollPane scrollPane1 = new JScrollPane(roomCustomerTable);
                scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                JLabel roomCustomerLabel = new JLabel("Select the Room Customer",SwingConstants.CENTER);
                JPanel panel4 =new JPanel(new BorderLayout(1,21));
                panel4.add(roomCustomerLabel,BorderLayout.NORTH);
                panel4.add(scrollPane1,BorderLayout.CENTER);
                Object [] options = {"SELECT","CANCEL"};
                int ans =JOptionPane.showOptionDialog(null,panel4,"Room Customers",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (ans==0){
                    int f = roomCustomerTable.getSelectedRow();
                    if (f>=0){
                        int roomCustomerID = Integer.parseInt(roomCustomerTable.getValueAt(f, 0).toString());
                        try{
                            String sql = "UPDATE food_customers " +
                                    "SET Customer_Room_ID = ?, State = ? " +
                                    "WHERE Customer = ?";
                            pst  = users.con.prepareStatement(sql);
                            pst.setFloat(1,roomCustomerID);
                            pst.setString(2, "ROOM_PAYMENT");
                            pst.setLong(3,customer);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null,"FOOD ADDED TO ROOM EXPENSES ");
                        }catch(SQLException se){
                            se.printStackTrace();
                        }
                    }else {
                        JOptionPane.showMessageDialog(null,"NO ROOM CUSTOMER WAS SELECTED");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "SELECT A CUSTOMER FIRST!!");
            }
            refresh(users);
        });
        orderButton.addActionListener(e-> order(users));
        menuButton.addActionListener(e->menu(users));
        paidOrdersButton.addActionListener(e->previousOrders(users));
        logOutButton.addActionListener(e->{
            removeComponents(users);
            new FoodCounter(users);
        });
    }
    private void removeComponents(Users users){
        mainLabelPanel.removeAll();
        mainLabelPanel.repaint();
        mainLabelPanel.revalidate();
        eastPanel.removeAll();
        eastPanel.repaint();
        eastPanel.revalidate();
        westPanel.removeAll();
        westPanel.repaint();
        westPanel.revalidate();
        southPanel.removeAll();
        southPanel.repaint();
        southPanel.revalidate();
        panel2.removeAll();
        panel2.repaint();
        panel2.revalidate();
        users.frame.remove(mainLabelPanel);
        users.frame.remove(southPanel);
        users.frame.remove(westPanel);
        users.frame.remove(eastPanel);
        users.frame.remove(panel2);
        users.frame.repaint();
        users.frame.revalidate();
    }
    private void order(Users users){
        removeComponents(users);
        new FoodOrder(users);
    }
    private void  menu(Users users){
        removeComponents(users);
        new Menu(users);
    }
    private void previousOrders(Users users){
        removeComponents(users);
        new PreviousOrders(users);
    }
    private void refresh(Users users ){
        String[] columnName = {"Receipt No","Food Ordered","Price","Time","Date"};
        DefaultTableModel model1 = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model1.setColumnIdentifiers(columnName);
        foodOrderTable.setModel(model1);
        long []receiptNo;
        try{
            String state = "NOT_PAID";
            pst = users.con.prepareStatement("SELECT DISTINCT Customer FROM food_orders WHERE State = ?");
            pst.setString(1, state.toLowerCase());
            ResultSet rs = pst.executeQuery();
            int init = 0;
            while (rs.next()){
                init++;
            }
            receiptNo = new long[init+1];
            rs.beforeFirst();
            int init1 = 0;
            while (rs.next()) {
                receiptNo[init1] = rs.getLong("Customer");
                init1++;
            }
            for (int j=0;j<init;j++) {
                String food = "";
                float totalPrice = 0F;
                String time = "";
                String date = "";
                pst = users.con.prepareStatement("SELECT * FROM food_orders WHERE State = ? AND Customer = ?");
                pst.setString(1, state.toLowerCase());
                pst.setLong(2,receiptNo[j]);
                rs = pst.executeQuery();
                while (rs.next()) {
                    food = food+" "+rs.getString("Quantity")+" "+rs.getString("food_name")+",";
                    time = rs.getString("Time");
                    date = rs.getString("Date");
                    totalPrice = totalPrice+(rs.getFloat("Amount_Paid"));
                }
                model1.addRow(new Object[]{receiptNo[j], food, totalPrice, time, date});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
    }
}