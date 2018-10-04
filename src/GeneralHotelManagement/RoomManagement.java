package GeneralHotelManagement;

import LogIn.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class RoomManagement {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JPanel panel2;
    private JPanel mainLabelPanel;
    private JTable roomsTable;
    RoomManagement(Users users){
        PreparedStatement pst;
        //main label
        Font forte = new Font("forte",Font.BOLD,30);
        JLabel foodCounterLabel = new JLabel("MANAGERS PANEL");
        foodCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foodCounterLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2,1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(foodCounterLabel);
        mainLabelPanel.add(mainLabelSeparator);
        //room table
        JLabel roomLabel = new JLabel("ALL ROOMS");
        roomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        String[] columnName = {"Type","Price","Room Number","State"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);
        roomsTable = new JTable();
        roomsTable.setModel(model);
        try{
            pst = users.con.prepareStatement("SELECT Room_Type, Room_No, Price_Per_Night, State FROM rooms");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Room_Type"), rs.getString("Price_Per_Night"),
                        rs.getString("Room_No"), rs.getString("State")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JButton cancelButton = new JButton("Cancel");
        JButton removeButton = new JButton("Remove Room");
        JButton createRoomButton = new JButton("+ Add Room ");
        JPanel jPanel = new JPanel(new GridLayout(1,7,20,0));
        jPanel.add(new JLabel());jPanel.add(cancelButton); jPanel.add(new JLabel());jPanel.add(new JLabel());
        jPanel.add(removeButton); jPanel.add(createRoomButton); jPanel.add(new JLabel());
        BorderLayout borderLayout2 = new BorderLayout(25,15);
        panel2 =new JPanel(borderLayout2);
        panel2.add(roomLabel, BorderLayout.NORTH);
        panel2.add(scrollPane, BorderLayout.CENTER);
        panel2.add(jPanel, BorderLayout.SOUTH);
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
        //action listeners
        cancelButton.addActionListener(e -> {
            removeComponents(users);
            new Manager(users);
        });
        removeButton.addActionListener(e -> {
            int r = roomsTable.getSelectedRow();
            int roomNo;
            if (r>=0) {
                roomNo = Integer.parseInt(roomsTable.getValueAt(r, 2).toString());
                String state = roomsTable.getValueAt(r,3).toString();
                int j = JOptionPane.showConfirmDialog(null,"DO YOU WANT TO DELETE ROOM NUMBER "+roomNo+"?",
                        "DELETE ROOM", JOptionPane.OK_CANCEL_OPTION);
                if (j==0){
                    if (state.equals("OCCUPIED")){
                        JOptionPane.showMessageDialog(null, "ERROR!!!: THE ROOM IS OCCUPIED!!");
                    }else {
                        try {
                            String sql = "DELETE FROM rooms WHERE Room_No = ? ";
                            PreparedStatement pstmt = users.con.prepareStatement(sql);
                            pstmt.setInt(1, roomNo);
                            pstmt.execute();
                            JOptionPane.showMessageDialog(null, "ROOM DELETED");
                            refresh(users);
                        } catch (SQLException se) {
                            se.printStackTrace();
                        }
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null, "SELECT A ROOM TO DELETE FIRST!!");
            }
        });
        float [] prices = new float[20];
        JTextField priceTextArea = new JTextField(5);
        priceTextArea.setEditable(false);
        JButton newTypeButton = new JButton("new Type");
        JButton reviewPrices = new JButton("Review ");
        JComboBox<String> roomTypeComboBox  = new JComboBox<>();
        createRoomButton.addActionListener(e -> {
            float roomPrice;
            int roomNo;
            String roomType;
            String []type ;
            try {
                String sql = "SELECT DISTINCT Room_Type, Price_Per_Night FROM rooms";
                PreparedStatement stmt = users.con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                int categoriesNum = 0;
                while (rs.next()) {
                    categoriesNum++;
                }
                type  = new String[categoriesNum];
                int j = 0;
                rs.beforeFirst();
                while (rs.next()){
                    type[j] = rs.getString("Room_Type");
                    prices[j] = rs.getFloat("Price_Per_Night");
                    roomTypeComboBox.addItem(type[j]);
                    j++;
                }
                JTextField roomNoTextArea = new JTextField(15);
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
                panel.add(roomNoTextArea);
                JPanel panel1 = new JPanel(new FlowLayout());
                panel1.add(roomTypeComboBox);panel1.add(newTypeButton);
                JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
                panel2.add(priceTextArea);panel2.add(reviewPrices);
                JPanel newRoomPanel = new JPanel(new GridLayout(4,2,10,10));
                newRoomPanel.add(new JLabel("Room Number:"));newRoomPanel.add(panel);
                newRoomPanel.add(new JLabel("Room Type:"));newRoomPanel.add(panel1);
                newRoomPanel.add(new JLabel("Room Price:"));newRoomPanel.add(panel2);
                Object [] options = {"CREATE ROOM","CANCEL"};
                int ans =JOptionPane.showOptionDialog(null,newRoomPanel,"New Rooms",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (ans==0){
                    roomNo =Integer.parseInt(roomNoTextArea.getText());
                    roomType = roomTypeComboBox.getSelectedItem().toString();
                    roomPrice = Float.parseFloat(priceTextArea.getText());
                    sql ="INSERT INTO rooms(Room_No, Room_Type, Price_Per_Night, State) " +
                            "VALUES(?,?,?,?)";
                    stmt = users.con.prepareStatement(sql);
                    stmt.setInt(1,roomNo);
                    stmt.setString(2,roomType);
                    stmt.setFloat(3,roomPrice);
                    stmt.setString(4,"AVAILABLE");
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "ROOM NUMBER "+roomNo+" CREATED");
                    refresh(users);
                    roomTypeComboBox.removeAllItems();
                }else {
                    roomTypeComboBox.removeAllItems();
                }
            }catch (SQLException se){
                se.printStackTrace();
            }

        });

        JTextField priceTextField = new JTextField(10);
        newTypeButton.addActionListener(e -> {
            float price;
            JTextField newTypeTextField = new JTextField(10);
            JPanel roomTypePanel = new JPanel(new GridLayout(3,2,1,10));
            roomTypePanel.add(new JLabel("Room Type Name:")); roomTypePanel.add(newTypeTextField);
            roomTypePanel.add(new JLabel("PRICE")); roomTypePanel.add(priceTextField);
            Object [] options = {"CREATE ROOM TYPE","CANCEL"};
            int ans =JOptionPane.showOptionDialog(null,roomTypePanel,"New Rooms",JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if (ans==0){
                String newType = newTypeTextField.getText();
                price = Float.parseFloat(priceTextField.getText());
                roomTypeComboBox.addItem(newType);
                roomTypeComboBox.setSelectedItem(newType);
                priceTextField.setText(""+price);
                priceTextArea.setText(""+price);
            }
        });
        roomTypeComboBox.addItemListener((ItemEvent e)->{
            try {
                String sql = "SELECT DISTINCT Room_Type, Price_Per_Night FROM rooms";
                PreparedStatement stmt = users.con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                int categoriesNum = 0;
                while (rs.next()) {
                    categoriesNum++;
                }
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    for (int q =0;q<=categoriesNum;q++){
                        if (roomTypeComboBox.getSelectedIndex() == q) {
                            priceTextArea.setText(""+prices[q]);
                        }else if (roomTypeComboBox.getSelectedIndex()>categoriesNum){
                            priceTextArea.setText(""+priceTextField.getText());
                        }
                    }
                }
            }catch (SQLException se){
                se.printStackTrace();
            }

        });

        reviewPrices.addActionListener(e -> {
            Font forte1 = new Font("forte",Font.BOLD,14);
            String [] type;
            JTextField [] priceTextFields;

            try{
                String sql = "SELECT DISTINCT Room_Type, Price_Per_Night FROM rooms";
                PreparedStatement stmt = users.con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                int categoriesNum = 0;
                while (rs.next()) {
                    categoriesNum++;
                }
                JPanel reviewPricesPanel = new JPanel(new GridLayout(categoriesNum+2,2,1,10));
                type = new String[categoriesNum+1];
                priceTextFields = new JTextField[categoriesNum+1];
                int num =0;
                rs.beforeFirst();
                while (rs.next()){
                    priceTextFields[num] = new JTextField(""+rs.getFloat("Price_Per_Night"));
                    type[num] = rs.getString("Room_Type");
                    num++;
                }
                System.out.println("Category Num: "+categoriesNum);
                for (int j=0;j<categoriesNum;j++){
                    reviewPricesPanel.add(new JLabel(type[j]+" Type"));
                    reviewPricesPanel.add(priceTextFields[j]);
                }
                JLabel optionLabel = new JLabel("Enter New Prices");
                optionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                optionLabel.setFont(forte1);
                JPanel optionPanel = new JPanel(new BorderLayout());
                optionPanel.add(optionLabel,BorderLayout.NORTH);
                optionPanel.add(reviewPricesPanel,BorderLayout.CENTER);
                Object [] options = {"UP-DATE","CANCEL"};
                int ans =JOptionPane.showOptionDialog(null,optionPanel,"New Prices",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (ans==0) {
                    for (int j=0;j<categoriesNum;j++) {
                        float price = Float.parseFloat(priceTextFields[j].getText()) ;

                        sql = "UPDATE rooms SET Price_Per_Night = ? WHERE Room_Type = ?";
                        PreparedStatement stmt1 = users.con.prepareStatement(sql);
                        stmt1.setFloat(1, price);
                        stmt1.setString(2, type[j]);
                        stmt1.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null, "PRICE UPDATED successfully");
                    refresh(users);
                }
            }catch (SQLException se){
                se.printStackTrace();
            }

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
    private void refresh(Users users){
        String[] columnName1 = {"Type","Price","Room Number","State"};
        DefaultTableModel model1 = new DefaultTableModel();
        model1.setColumnIdentifiers(columnName1);
        //roomsTable = new JTable();
        roomsTable.setModel(model1);
        try{
            PreparedStatement pst = users.con.prepareStatement("SELECT Room_Type, Room_No, Price_Per_Night, State FROM rooms");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model1.addRow(new Object[]{rs.getString("Room_Type"), rs.getString("Price_Per_Night"),
                        rs.getString("Room_No"), rs.getString("State")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
    }
}
