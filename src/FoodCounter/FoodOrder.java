package FoodCounter;

import LogIn.GenerateReceipt;
import LogIn.Users;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.awt.Font.BOLD;

class FoodOrder {
    private JPanel panel2;
    private JPanel buttonPanel;
    private JPanel com;
    private JPanel mainLabelPanel;
    FoodOrder(Users users){
        //main label
        Font forte1 = new Font("forte", BOLD,15);
        Font forte = new Font("forte",Font.BOLD,30);
        JLabel foodCounterLabel = new JLabel("FOOD COUNTER PANEL");
        foodCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foodCounterLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2,1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(foodCounterLabel);
        mainLabelPanel.add(mainLabelSeparator);
        //selected Table
        panel2 = new JPanel(new BorderLayout(2,1));
        JLabel tableLabel = new JLabel("FOOD SELECTED");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        String[] columnName = {"FOOD","QUANTITY","PRICE"};
        DefaultTableModel occupiedTableModel = new DefaultTableModel();
        occupiedTableModel.setColumnIdentifiers(columnName);
        JTable table = new JTable(occupiedTableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton payOrderButton = new JButton("PAY FOR SELECTED ORDER");
        panel2.add(tableLabel,BorderLayout.NORTH);
        panel2.add(scrollPane,BorderLayout.CENTER);
        panel2.add(payOrderButton,BorderLayout.SOUTH);
        //food_menu
        int [] column;
        int [] rowNum;
        float [][] prices;
        JPanel [] jPanels;
        JPanel [] borderPanel;
        String [] category ;
        JCheckBox [] [] foodCategory1;
        JTextField [][] quantity;
        JTextField [] []price;
        int [][] foodId;
        JPanel [] jPanels1;
        JPanel [] jPanels2;
        JPanel [] jPanels3;
        int categoriesNum = 0;
        try {
            String sql = "SELECT DISTINCT category FROM food_menu";
            PreparedStatement stmt = users.con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categoriesNum++;
            }
            category = new String[categoriesNum];
            rs.beforeFirst();
            int j = 0;
            while (rs.next()) {
                category[j] = rs.getString("category");
                j++;
            }
        }catch (SQLException se){
            se.printStackTrace();
            category = new String[categoriesNum];
        }
        jPanels = new JPanel[categoriesNum];
        borderPanel = new JPanel[categoriesNum];
        int gridRow = (categoriesNum + 2) / 3;
        com = new JPanel(new GridLayout(gridRow, 3, 1, 1));
        int z = 3;
        for (int k =0;k<category.length;k++){
            int r = 0;
            try {
                System.out.println(category[k]);
                PreparedStatement pStmt = users.con.prepareStatement("SELECT * FROM food_menu WHERE category = ?");
                pStmt.setString(1, category[k]);
                ResultSet rs = pStmt.executeQuery();
                while (rs.next()) {
                    r++;
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
            if (r>z){
                z=r;
            }
        }
        foodCategory1 = new JCheckBox[categoriesNum][z];
        price = new JTextField[categoriesNum][z];
        quantity = new JTextField[categoriesNum][z];
        foodId = new int [categoriesNum][z];
        prices = new float[categoriesNum][z];
        column = new int[z];
        rowNum = new int[categoriesNum];
        for (int k =0;k<category.length;k++){
            int r = 0;
            try {
                System.out.println(category[k]);
                PreparedStatement pStmt = users.con.prepareStatement("SELECT * FROM food_menu WHERE category = ?");
                pStmt.setString(1, category[k]);
                ResultSet rs = pStmt.executeQuery();
                while (rs.next()) {
                    r++;
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
            jPanels[k] = new JPanel(new GridLayout(r + 2, 3, 1, 1));
            jPanels1 = new JPanel[r];
            jPanels2 = new JPanel[r];
            jPanels3 = new JPanel[r];
            jPanels[k].add(new JLabel("food"));
            jPanels[k].add(new JLabel("Quantity"));
            jPanels[k].add(new JLabel("Price"));
            try {
                PreparedStatement pStmt = users.con.prepareStatement("SELECT * FROM food_menu WHERE category = ?");
                pStmt.setString(1, category[k]);
                ResultSet rs = pStmt.executeQuery();
                int j = 0;
                while (rs.next()) {
                    foodId[k][j] = rs.getInt("food_ID");
                    foodCategory1[k][j] = new JCheckBox("" + rs.getString("food_name"));
                    jPanels1[j] = new JPanel(new FlowLayout(FlowLayout.LEADING));
                    jPanels1[j].add(foodCategory1[k][j]);
                    price[k][j] = new JTextField(rs.getString("price"), 5);
                    prices[k][j] = rs.getFloat("price");
                    jPanels2[j] = new JPanel(new FlowLayout(FlowLayout.LEADING));
                    price[k][j].setEditable(false);
                    jPanels2[j].add(price[k][j]);
                    quantity[k][j] = new JTextField("01",5);
                    quantity[k][j].setEditable(false);
                    jPanels3[j] = new JPanel(new FlowLayout(FlowLayout.LEADING));
                    jPanels3[j].add(quantity[k][j]);
                    jPanels[k].add(jPanels1[j]);
                    jPanels[k].add(jPanels3[j]);
                    jPanels[k].add(jPanels2[j]);
                    foodCategory1[k][j].addItemListener((ItemEvent e) -> {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            DefaultTableModel tableModel = new DefaultTableModel();
                            tableModel.setColumnIdentifiers(columnName);
                            table.setModel(tableModel);
                            for(int c= 0;c<rowNum.length;c++){
                                for(int f= 0;f<column.length;f++){
                                    if (foodCategory1[c][f]!=null) {
                                        if (foodCategory1[c][f].isSelected()){
                                            quantity[c][f].setEditable(true);
                                            Object []row = {foodCategory1[c][f].getText(), Float.parseFloat(quantity[c][f].getText()),
                                                    price[c][f].getText()};
                                            tableModel.addRow(row);
                                        }else {
                                            quantity[c][f].setEditable(false);
                                            quantity[c][f].setText("01");
                                        }
                                    }
                                }
                            }
                        }else if (e.getStateChange() == ItemEvent.DESELECTED){
                            DefaultTableModel tableModel = new DefaultTableModel();
                            tableModel.setColumnIdentifiers(columnName);
                            table.setModel(tableModel);
                            for(int c= 0;c<rowNum.length;c++){
                                for(int f= 0;f<column.length;f++){
                                    if (foodCategory1[c][f]!=null) {
                                        if (foodCategory1[c][f].isSelected()){
                                            quantity[c][f].setEditable(true);
                                            Object []row = {foodCategory1[c][f].getText(), Float.parseFloat(quantity[c][f].getText()),
                                                    price[c][f].getText()};
                                            tableModel.addRow(row);
                                        }else {
                                            quantity[c][f].setEditable(false);
                                            quantity[c][f].setText("01");
                                        }
                                    }
                                }
                            }
                        }
                    });
                    quantity[k][j].getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            reCheck();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            reCheck();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            reCheck();
                        }

                        private void reCheck(){
                            float foodQuantity;
                            DefaultTableModel tableModel = new DefaultTableModel();
                            tableModel.setColumnIdentifiers(columnName);
                            table.setModel(tableModel);
                            for(int c= 0;c<rowNum.length;c++){
                                for(int f= 0;f<column.length;f++){
                                    if (foodCategory1[c][f]!=null) {
                                        if (foodCategory1[c][f].isSelected()) {
                                            try {
                                                foodQuantity = Float.parseFloat(quantity[c][f].getText());
                                            } catch (NumberFormatException nfe) {
                                                foodQuantity = 1;
                                            }
                                            float totalPrice = foodQuantity * prices[c][f];
                                            price[c][f].setText("" + totalPrice);
                                            Object[] row = {foodCategory1[c][f].getText(),
                                                    foodQuantity, price[c][f].getText()};
                                            tableModel.addRow(row);

                                        } else {
                                            price[c][f].setText("" + prices[c][f]);

                                        }
                                    }
                                }
                            }
                        }
                    });
                    j++;
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
            borderPanel[k] = new JPanel(new BorderLayout());
            JLabel categoryLabel = new JLabel(category[k]);
            categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
            categoryLabel.setFont(forte1);
            borderPanel[k].add(categoryLabel,BorderLayout.NORTH);
            borderPanel[k].add(jPanels[k],BorderLayout.CENTER);
            borderPanel[k].add(new JSeparator(SwingConstants.VERTICAL),BorderLayout.EAST);
            borderPanel[k].add(new JSeparator(SwingConstants.HORIZONTAL),BorderLayout.SOUTH);
            com.add(borderPanel[k]);
        }
        //buttons
        GridLayout buttonGridLayout = new GridLayout(2,5,30,0);
        buttonPanel = new JPanel(buttonGridLayout);
        JLabel []label = new JLabel[8];
        for (int j=0;j<8;j++){
            label[j] = new JLabel();
        }
        JButton cancelButton = new JButton("CANCEL");
        JButton orderButton = new JButton("ORDER");
        buttonPanel.add(label[0]);
        buttonPanel.add(cancelButton);
        buttonPanel.add(label[1]);
        buttonPanel.add(orderButton);
        buttonPanel.add(label[2]);
        for (int j=3;j<8;j++){
            buttonPanel.add(label[j]);
        }
        //Outer Frame
        BorderLayout borderLayout = new BorderLayout(25,15);
        users.frame.setLayout(borderLayout);
        users.frame.add(mainLabelPanel, BorderLayout.NORTH);
        users.frame.add(com,BorderLayout.CENTER);
        users.frame.add(buttonPanel, BorderLayout.SOUTH);
        users.frame.add(panel2, BorderLayout.EAST);
        //Action Listener
        cancelButton.addActionListener( e->cancelFoodOrder(users));
        orderButton.addActionListener((ActionEvent e) -> {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long customer = timestamp.getTime();
            PreparedStatement pStmt;
            for(int c= 0;c<rowNum.length;c++){
                for(int f= 0;f<column.length;f++){
                    if (foodCategory1[c][f]!=null) {
                        if (foodCategory1[c][f].isSelected()){
                            try{
                                float foodQuantity = Float.parseFloat(quantity[c][f].getText());
                                float totalAmount = Float.parseFloat(price[c][f].getText());
                                pStmt = users.con.prepareStatement("INSERT INTO " +
                                        "food_customers(Time, Date, State, Food_ID, Quantity, Customer, Amount_Paid,Served_By)" +
                                        "VALUES(?,?,?,?,?,?,?,?)");
                                long millis = System.currentTimeMillis();
                                java.sql.Date todayDate = new java.sql.Date(millis);
                                pStmt.setDate(2,todayDate);
                                java.sql.Time todayDate1 = new java.sql.Time(millis);
                                pStmt.setTime(1,todayDate1);
                                pStmt.setString(3,"NOT_PAID");
                                pStmt.setInt(4,foodId[c][f]);
                                pStmt.setFloat(5,foodQuantity);
                                pStmt.setLong(6,customer);
                                pStmt.setFloat(7,totalAmount);
                                pStmt.setString(8,users.userName);
                                pStmt.executeUpdate();
                            }catch (SQLException se){
                                se.printStackTrace();
                            }
                        }
                    }else {break;}
                }
            }
            JOptionPane.showMessageDialog(null,"FOOD ORDERED ");
            for(int c= 0;c<rowNum.length;c++){
                for(int f= 0;f<column.length;f++){
                    if (foodCategory1[c][f]!=null) {
                        if (foodCategory1[c][f].isSelected()){
                            foodCategory1[c][f].setSelected(false);

                        }
                    }else {break;}
                }
            }
        });
        payOrderButton.addActionListener(e -> {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long customer = timestamp.getTime();
            String food = "";
            float totalPrice = 0;
            float balance;
            float cash;
            PreparedStatement pStmt, pst;
            for(int c= 0;c<rowNum.length;c++){
                for(int f= 0;f<column.length;f++){
                    if (foodCategory1[c][f]!=null) {
                        if (foodCategory1[c][f].isSelected()){
                            food = food+", "+foodCategory1[c][f].getText();
                            totalPrice = totalPrice + Float.parseFloat(price[c][f].getText());
                        }
                    }else {break;}
                }
            }
            JTextField cashTextField = new JTextField(8);
            JPanel jPanel = new JPanel(new GridLayout(6,2,10,10));
            jPanel.add(new JLabel("CUSTOMER ID: "));jPanel.add(new JLabel(""+customer));
            jPanel.add(new JLabel("FOOD ORDERED: "));jPanel.add(new JLabel(food));
            jPanel.add(new JLabel("TOTAL PRICE: "));jPanel.add(new JLabel(""+totalPrice));
            jPanel.add(new JLabel("CASH GIVEN: "));jPanel.add(cashTextField);
            JPanel optionPanel = new JPanel(new BorderLayout());
            optionPanel.add(jPanel,BorderLayout.CENTER);
            Object [] options = {"PAY","CANCEL"};
            int ans =JOptionPane.showOptionDialog(null,optionPanel,"PAY FOOD",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if (ans ==0){
                try {
                    cash = Float.parseFloat(cashTextField.getText());
                }catch (NumberFormatException Nfe){
                    cash =0;
                }
                balance= cash-totalPrice;
                if (balance>=0){
                    for(int c= 0;c<rowNum.length;c++){
                        for(int f= 0;f<column.length;f++){
                            if (foodCategory1[c][f]!=null) {
                                if (foodCategory1[c][f].isSelected()){
                                    try{
                                        float foodQuantity = Float.parseFloat(quantity[c][f].getText());
                                        float totalAmount = Float.parseFloat(price[c][f].getText());
                                        pStmt = users.con.prepareStatement("INSERT INTO " +
                                                "food_customers(Time, Date, State, Food_ID, Quantity, Customer, Amount_Paid,Served_By)" +
                                                "VALUES(?,?,?,?,?,?,?,?)");
                                        long millis = System.currentTimeMillis();
                                        java.sql.Date todayDate = new java.sql.Date(millis);
                                        pStmt.setDate(2,todayDate);
                                        java.sql.Time todayDate1 = new java.sql.Time(millis);
                                        pStmt.setTime(1,todayDate1);
                                        pStmt.setString(3,"PAID");
                                        pStmt.setInt(4,foodId[c][f]);
                                        pStmt.setFloat(5,foodQuantity);
                                        pStmt.setLong(6,customer);
                                        pStmt.setFloat(7,totalAmount);
                                        pStmt.setString(8,users.userName);
                                        pStmt.executeUpdate();
                                    }catch (SQLException se){
                                        se.printStackTrace();
                                    }
                                }
                            }else {break;}
                        }
                    }
                    JOptionPane.showMessageDialog(null,
                            "FOOD PAID for Customer_ID "+customer+". Give Balance of: "+balance);
                    int p = JOptionPane.showConfirmDialog(null,"Click yes to generate Receipt",
                            "RECEIPT", JOptionPane.YES_NO_OPTION);
                    if(p==0) {
                        try {
                            Paragraph[] paragraphs = new Paragraph[50];
                            paragraphs[0]=new Paragraph("_____________________________________ ");
                            paragraphs[1]=new Paragraph("Qty - Food Name _________ Total Amount ",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD,11, Font.BOLD));
                            paragraphs[2]=new Paragraph("______________________________________");
                            int k=3;
                            for(int c= 0;c<rowNum.length;c++){
                                for(int f= 0;f<column.length;f++){
                                    if (foodCategory1[c][f]!=null) {
                                        if (foodCategory1[c][f].isSelected()){
                                            paragraphs[k]=new Paragraph(""+quantity[c][f].getText()+"--"+
                                                    food+"----------------"+
                                                    price[c][f].getText());
                                            k++;
                                        }
                                    }else {break;}
                                }
                            }
                            paragraphs[k]=new Paragraph("_________________________________ ");
                            paragraphs[k+1]=new Paragraph("FOOD SUB-TOTAL-------------"+totalPrice,
                                    FontFactory.getFont(FontFactory.TIMES_ROMAN,12,
                                           Font.BOLD));
                            paragraphs[k+2]=new Paragraph("_________________________________ ");
                            paragraphs[k+2]=new Paragraph(" ");
                            paragraphs[k+3]=new Paragraph("CASH GIVEN: "+cash);
                            paragraphs[k+4]=new Paragraph("CHANGE GIVEN: "+balance);
                            new GenerateReceipt(users,customer,paragraphs);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    for(int c= 0;c<rowNum.length;c++){
                        for(int f= 0;f<column.length;f++){
                            if (foodCategory1[c][f]!=null) {
                                if (foodCategory1[c][f].isSelected()){
                                    foodCategory1[c][f].setSelected(false);

                                }
                            }else {break;}
                        }
                    }
                }
            }
        });
    }
    private void cancelFoodOrder(Users users){
        users.frame.remove(mainLabelPanel);
        users.frame.remove(com);
        users.frame.remove(buttonPanel);
        users.frame.remove(panel2);
        users.frame.repaint();
        users.frame.revalidate();
        new FoodCounter(users);
    }
}
