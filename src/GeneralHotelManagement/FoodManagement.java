package GeneralHotelManagement;

import LogIn.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class FoodManagement {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JPanel menu;
    private JPanel mainLabelPanel;
    private JTable table;
    FoodManagement(Users users){
        PreparedStatement pst;
        Font forte1 = new Font("forte",Font.BOLD,20);
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
        //table
        JLabel beverageLabel = new JLabel("ALL FOODS IN MENU");
        beverageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        beverageLabel.setFont(forte1);
        String[] columnName = {"FOOD_ID","FOOD","CATEGORY","PRICE"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        try{
            pst = users.con.prepareStatement("SELECT * FROM food_menu");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("food_ID"), rs.getString("food_name"),
                        rs.getString("category"), rs.getString("price")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        //button
        JButton cancelButton = new JButton("Cancel");
        JButton removeButton = new JButton("- Remove Food");
        //JButton createRoomButton = new JButton("+ Add Food ");
        JPanel jPanel = new JPanel(new GridLayout(1,5,20,0));
        jPanel.add(new JLabel());jPanel.add(cancelButton); jPanel.add(new JLabel());
        jPanel.add(removeButton);  jPanel.add(new JLabel());
        JPanel buttonPanel = new JPanel(new GridLayout(3,1,10,10));
        JButton addButton = new JButton("+ NEW FOOD");
        JButton newPricesButton = new JButton("REVIEW PRICES");
        JButton button2 = new JButton("REMOVED FOOD");
        buttonPanel.add(addButton);
        buttonPanel.add(newPricesButton);
        buttonPanel.add(button2);
        menu = new JPanel(new BorderLayout(25,15));
        menu.add(beverageLabel, BorderLayout.NORTH);
        menu.add(scrollPane, BorderLayout.CENTER);
        menu.add(buttonPanel,BorderLayout.EAST);
        menu.add(jPanel, BorderLayout.SOUTH);
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
        users.frame.add(menu,BorderLayout.CENTER);
        users.frame.add(southPanel, BorderLayout.SOUTH);
        users.frame.add(westPanel, BorderLayout.WEST);
        users.frame.add(eastPanel, BorderLayout.EAST);
        //action listeners
        JComboBox<String> category1ComboBox  = new JComboBox<>();
        JPanel optionPanel = new JPanel(new BorderLayout());
        newPricesButton.addActionListener(e -> {
            Font forte2 = new Font("forte",Font.BOLD,14);
            String [] food;
            JTextField [] priceTextFields;
            String []category ;
            try {
                String sql = "SELECT DISTINCT category FROM food_menu";
                PreparedStatement stmt = users.con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                int categoriesNum = 0;
                while (rs.next()) {
                    categoriesNum++;
                }
                category  = new String[categoriesNum];
                int j = 0;
                rs.beforeFirst();
                while (rs.next()){
                    category[j] = rs.getString("category");
                    category1ComboBox.addItem(category[j]);
                    j++;
                }
                String selectedCategory = category1ComboBox.getSelectedItem().toString();
                sql = "SELECT * FROM food_menu WHERE category = ?";
                stmt = users.con.prepareStatement(sql);
                stmt.setString(1,selectedCategory);
                rs = stmt.executeQuery();
                int init = 0;
                while (rs.next()) {
                    init++;
                }
                JPanel reviewPricesPanel = new JPanel(new GridLayout(init*2,2,1,10));
                priceTextFields = new JTextField[init+1];
                food = new String[init+1];
                rs.beforeFirst();
                j=0;
                while (rs.next()) {
                    food[j] = rs.getString("food_name");
                    priceTextFields[j] = new JTextField(""+rs.getString("price"));
                    j++;
                }
                reviewPricesPanel.add(new JLabel("Category"));
                reviewPricesPanel.add(category1ComboBox);
                for (j=0;j<init;j++){
                    reviewPricesPanel.add(new JLabel(food[j]+""));
                    reviewPricesPanel.add(priceTextFields[j]);
                }
                JLabel optionLabel = new JLabel("Enter New "+category1ComboBox.getSelectedItem().toString()+" Prices");
                optionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                optionLabel.setFont(forte2);
                optionPanel.add(optionLabel,BorderLayout.NORTH);
                optionPanel.add(reviewPricesPanel,BorderLayout.CENTER);
                Object [] options = {"UP-DATE","CANCEL"};
                int ans =JOptionPane.showOptionDialog(null,optionPanel,"New Prices",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (ans==0) {
                    for (j=0;j<init;j++) {
                        float price = Float.parseFloat(priceTextFields[j].getText()) ;
                        sql = "UPDATE food_menu SET price = ? WHERE food_name = ?";
                        PreparedStatement stmt1 = users.con.prepareStatement(sql);
                        stmt1.setFloat(1, price);
                        stmt1.setString(2, food[j]);
                        stmt1.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null, "PRICE UPDATED successfully");
                    refresh(users);
                    optionPanel.remove(optionLabel);
                    optionPanel.remove(reviewPricesPanel);
                    optionPanel.repaint();
                    category1ComboBox.removeAllItems();
                }else{
                    optionPanel.remove(optionLabel);
                    optionPanel.remove(reviewPricesPanel);
                    optionPanel.repaint();
                    category1ComboBox.removeAllItems();
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
        });
        category1ComboBox.addItemListener((ItemEvent e)->{
            if (e.getStateChange() == ItemEvent.SELECTED) {
                optionPanel.removeAll();
                optionPanel.revalidate();
                optionPanel.repaint();
                Font forte2 = new Font("forte",Font.BOLD,14);
                String [] food;
                JTextField [] priceTextFields;
                try {
                    String selectedCategory = category1ComboBox.getSelectedItem().toString();
                    String sql = "SELECT * FROM food_menu WHERE category = ?";
                    PreparedStatement stmt = users.con.prepareStatement(sql);
                    stmt.setString(1,selectedCategory);
                    ResultSet rs = stmt.executeQuery();
                    int init = 0;
                    while (rs.next()) {
                        init++;
                    }
                    JPanel reviewPricesPanel = new JPanel(new GridLayout(init+2,2,1,10));
                    priceTextFields = new JTextField[init+1];
                    food = new String[init+1];
                    int j=0;
                    rs.beforeFirst();
                    while (rs.next()) {
                        food[j] = rs.getString("food_name");
                        priceTextFields[j] = new JTextField(""+rs.getString("price"));
                        j++;
                    }
                    reviewPricesPanel.add(new JLabel("Category"));
                    reviewPricesPanel.add(category1ComboBox);
                    for (j=0;j<init;j++){
                        reviewPricesPanel.add(new JLabel(food[j]+""));
                        reviewPricesPanel.add(priceTextFields[j]);
                    }
                    JLabel optionLabel = new JLabel("Enter New "+category1ComboBox.getSelectedItem().toString()+" Prices");
                    optionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    optionLabel.setFont(forte2);
                    optionPanel.add(optionLabel,BorderLayout.NORTH);
                    optionPanel.add(reviewPricesPanel,BorderLayout.CENTER);

                }catch (SQLException se){
                    se.printStackTrace();
                }
            }
        });
        JTextField priceTextArea = new JTextField(15);
        JButton newCategoryButton = new JButton("new ");
        JComboBox<String> categoryComboBox  = new JComboBox<>();
        addButton.addActionListener(e -> {
            float foodPrice;
            int foodId;
            String foodName;
            String foodCategory;
            String []category ;
            try {
                String sql = "SELECT DISTINCT category FROM food_menu";
                PreparedStatement stmt = users.con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                int categoriesNum = 0;
                while (rs.next()) {
                    categoriesNum++;
                }
                category  = new String[categoriesNum];
                int j = 0;
                rs.beforeFirst();
                while (rs.next()){
                    category[j] = rs.getString("category");
                    categoryComboBox.addItem(category[j]);
                    j++;
                }
                JTextField foodIDTextField = new JTextField(15);
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
                panel.add(foodIDTextField);
                JTextField foodNameTextField = new JTextField(15);
                JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.TRAILING));
                panel3.add(foodNameTextField);
                JPanel panel1 = new JPanel(new FlowLayout());
                panel1.add(categoryComboBox);panel1.add(newCategoryButton);
                JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
                panel2.add(priceTextArea);//panel2.add(reviewPrices);
                JPanel newRoomPanel = new JPanel(new GridLayout(5,2,10,10));
                newRoomPanel.add(new JLabel("Food I_D:"));newRoomPanel.add(panel);
                newRoomPanel.add(new JLabel("Food Name:"));newRoomPanel.add(panel3);
                newRoomPanel.add(new JLabel("Food Category:"));newRoomPanel.add(panel1);
                newRoomPanel.add(new JLabel("Food Price:"));newRoomPanel.add(panel2);
                Object [] options = {"ADD FOOD","CANCEL"};
                int ans =JOptionPane.showOptionDialog(null,newRoomPanel,"New Food",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (ans==0){
                    foodId =Integer.parseInt(foodIDTextField.getText());
                    foodCategory = categoryComboBox.getSelectedItem().toString();
                    foodPrice = Float.parseFloat(priceTextArea.getText());
                    foodName = foodNameTextField.getText();
                    sql ="INSERT INTO food_menu(food_ID, food_name, category, price) " +
                            "VALUES(?,?,?,?)";
                    stmt = users.con.prepareStatement(sql);
                    stmt.setInt(1,foodId);
                    stmt.setString(2,foodName);
                    stmt.setFloat(4,foodPrice);
                    stmt.setString(3,foodCategory);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, ""+foodName+" FOOD CREATED");
                    refresh(users);
                    categoryComboBox.removeAllItems();
                }else {
                    categoryComboBox.removeAllItems();
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
        });
        newCategoryButton.addActionListener(e -> {
            JTextField newCategoryTextField = new JTextField(10);
            JPanel roomTypePanel = new JPanel(new GridLayout(2,2,1,10));
            roomTypePanel.add(new JLabel("New Category Name:")); roomTypePanel.add(newCategoryTextField);
            Object [] options = {"CREATE CATEGORY","CANCEL"};
            int ans =JOptionPane.showOptionDialog(null,roomTypePanel,"New Rooms",JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if (ans==0){
                String newType = newCategoryTextField.getText();
                categoryComboBox.addItem(newType);
                categoryComboBox.setSelectedItem(newType);
            }
        });
        removeButton.addActionListener(e -> {
            PreparedStatement pst1;
            int row = table.getSelectedRow();
            int foodId;
            if (row>=0) {
                foodId = Integer.parseInt(table.getValueAt(row, 0).toString());
                String food = table.getValueAt(row,1).toString().toUpperCase();
                int jOption = JOptionPane.showConfirmDialog(null,"DO YOU WANT DELETE FOOD "+food,
                        "DELETE",JOptionPane.YES_NO_OPTION);
                if (jOption==0){
                    try {
                        pst1 = users.con.prepareStatement("DELETE FROM food_menu WHERE food_ID = ?");
                        pst1.setInt(1,foodId);
                        pst1.execute();
                        JOptionPane.showMessageDialog(null,"FOOD DELETED");
                        refresh(users);
                    }catch (SQLException se){
                        se.printStackTrace();
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null, "SELECT A FOOD TO DELETE FIRST!!");

            }
        });
        cancelButton.addActionListener(e -> {
            removeComponents(users);
            new Manager(users);
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
        menu.removeAll();
        menu.repaint();
        menu.revalidate();
        users.frame.remove(mainLabelPanel);
        users.frame.remove(southPanel);
        users.frame.remove(westPanel);
        users.frame.remove(eastPanel);
        users.frame.remove(menu);
        users.frame.repaint();
        users.frame.revalidate();
    }
    private void refresh(Users users){
        PreparedStatement pst;
        String[] columnName = {"FOOD_ID","FOOD","CATEGORY","PRICE"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);
        table.setModel(model);
        try{
            pst = users.con.prepareStatement("SELECT * FROM food_menu");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("food_ID"), rs.getString("food_name"),
                        rs.getString("category"), rs.getString("price")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
    }
}
