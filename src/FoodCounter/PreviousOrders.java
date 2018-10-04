package FoodCounter;

import LogIn.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class PreviousOrders {
    private  JPanel previousOrderPanel;
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel buttonPanel;
    private JPanel mainLabelPanel;
    PreviousOrders(Users users){
        PreparedStatement pst;
        //main label
        Font forte1 = new Font("forte",Font.BOLD,15);
        Font forte = new Font("forte",Font.BOLD,30);
        JLabel foodCounterLabel = new JLabel("FOOD COUNTER PANEL");
        foodCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foodCounterLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2,1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(foodCounterLabel);
        mainLabelPanel.add(mainLabelSeparator);
        //previously paid Table
        JLabel previousLabel = new JLabel("PREVIOUS PAID ORDER",SwingConstants.CENTER);
        previousLabel.setFont(forte1);
        String[] columnName = {"Receipt No","Food Ordered","Price","Time","Date"};
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(columnName);
        JTable previousOrderTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(previousOrderTable);
        previousOrderPanel = new JPanel(new BorderLayout());
        previousOrderPanel.add(previousLabel,BorderLayout.NORTH);
        previousOrderPanel.add(scrollPane,BorderLayout.CENTER);
        long []receiptNo;
        try{
            String state = "PAID";
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

        //buttons
        GridLayout buttonGridLayout = new GridLayout(2,5,30,0);
        buttonPanel = new JPanel(buttonGridLayout);
        JLabel []label = new JLabel[8];
        for (int j=0;j<8;j++){
            label[j] = new JLabel();
        }
        JButton backButton = new JButton("BACK");
        JButton detailButton = new JButton("VIEW MORE DETAILS");
        buttonPanel.add(label[0]);
        buttonPanel.add(backButton);
        buttonPanel.add(label[1]);
        buttonPanel.add(detailButton);
        buttonPanel.add(label[2]);
        for (int j=3;j<8;j++){
            buttonPanel.add(label[j]);
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
        //OUTER FRAME
        BorderLayout borderLayout = new BorderLayout(25,15);
        users.frame.setLayout(borderLayout);
        users.frame.add(mainLabelPanel, BorderLayout.NORTH);
        users.frame.add(previousOrderPanel,BorderLayout.CENTER);
        users.frame.add(buttonPanel, BorderLayout.SOUTH);
        users.frame.add(westPanel, BorderLayout.WEST);
        users.frame.add(eastPanel, BorderLayout.EAST);
        //Action Listeners
        backButton.addActionListener(e->goBack(users));

    }
    private void goBack(Users users){
        users.frame.remove(mainLabelPanel);
        users.frame.remove(buttonPanel);
        users.frame.remove(westPanel);
        users.frame.remove(eastPanel);
        users.frame.remove(previousOrderPanel);
        users.frame.repaint();
        users.frame.revalidate();
        new FoodCounter(users);
    }
}
