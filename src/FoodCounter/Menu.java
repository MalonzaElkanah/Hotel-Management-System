package FoodCounter;

import LogIn.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Menu {
    private JPanel menu;
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel buttonPanel;
    private JPanel mainLabelPanel;
    private JTable table;
    Menu(Users users){
        PreparedStatement pst;
        //main label
        Font forte1 = new Font("forte",Font.BOLD,20);
        Font forte = new Font("forte",Font.BOLD,30);
        JLabel foodCounterLabel = new JLabel("FOOD COUNTER MENU PANEL");
        foodCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foodCounterLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2,1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(foodCounterLabel);
        mainLabelPanel.add(mainLabelSeparator);
        //Beverage
        JLabel beverageLabel = new JLabel("MENU");
        beverageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        beverageLabel.setFont(forte1);
        String[] columnName = {"FOOD_ID","FOOD","CATEGORY","PRICE"};
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
        menu = new JPanel(new BorderLayout());
        menu.add(beverageLabel, BorderLayout.NORTH);
        menu.add(scrollPane, BorderLayout.CENTER);
        //BUTTONS
        buttonPanel = new JPanel(new GridLayout(2,5,30,0));
        JLabel []label = new JLabel[8];
        for (int j=0;j<8;j++){
            label[j] = new JLabel();
        }
        JButton backButton = new JButton("BACK");
        JButton newFoodButton = new JButton("+ NEW MEAL");
        buttonPanel.add(label[0]);
        buttonPanel.add(backButton);
        buttonPanel.add(label[1]);
        buttonPanel.add(newFoodButton);
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
        users.frame.add(this.menu,BorderLayout.CENTER);
        users.frame.add(buttonPanel, BorderLayout.SOUTH);
        users.frame.add(westPanel, BorderLayout.WEST);
        users.frame.add(eastPanel, BorderLayout.EAST);
        //ACTION LISTENERS
        backButton.addActionListener(e->goBack(users));
        JTextField priceTextArea = new JTextField(15);
        JButton newCategoryButton = new JButton("new ");
        JComboBox<String> categoryComboBox  = new JComboBox<>();
        newFoodButton.addActionListener(e -> {
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
    }
    private void refresh(Users users){
        String[] columnName = {"FOOD_ID","FOOD","CATEGORY","PRICE"};
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(columnName);
        table.setModel(model);
        try{
            PreparedStatement pst = users.con.prepareStatement("SELECT * FROM food_menu");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("food_ID"), rs.getString("food_name"),
                        rs.getString("category"), rs.getString("price")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
    }
    private void goBack(Users users){
        users.frame.remove(mainLabelPanel);
        users.frame.remove(buttonPanel);
        users.frame.remove(westPanel);
        users.frame.remove(eastPanel);
        users.frame.remove(menu);
        users.frame.repaint();
        users.frame.revalidate();
        new FoodCounter(users);
    }
}
/* Created by Malonza-Ibrahim. elkanahmalonza@gmail.com*/