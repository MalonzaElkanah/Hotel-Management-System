package Store;

import LogIn.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Created by Malone-Ibra on 7/25/2018.
 */
class NewProductAndAssets {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JPanel panel2;
    private JPanel mainLabelPanel;
    private PreparedStatement pst;
    private JLabel locationLabel;
    private JTextField locationTextField;
    private JLabel stutusLabel;
    private JTextField stutusTextField;
    private JLabel alertUnitLabel;
    private JTextField alertUnitTextField;
    private JComboBox<String> categoryComboBox;
    private JLabel supplerLabel;
    private JTextField supplerTextField;
    private JButton supplierButton;
    private JComboBox<String> chooseComboBox;
    private JTextField unitTextField;
    private JTextField nameTextField;
    private JTextField costTextField;
    NewProductAndAssets(Users users){
        //main label
        Font forte = new Font("forte",Font.BOLD,30);
        JLabel foodCounterLabel = new JLabel("HOTEL STORE PANEL");
        foodCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foodCounterLabel.setFont(forte);
        GridLayout mainLabelGrid = new GridLayout(2,1);
        mainLabelPanel = new JPanel(mainLabelGrid);
        JSeparator mainLabelSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        mainLabelPanel.add(foodCounterLabel);
        mainLabelPanel.add(mainLabelSeparator);
        //Combo
        JLabel createLabel = new JLabel("CREATE NEW: ");
        String []choice ={"Product","Asset"};
        chooseComboBox = new JComboBox<>(choice);
        JPanel comboPanel = new JPanel(new FlowLayout());
        comboPanel.add(createLabel);comboPanel.add(chooseComboBox);
        //form
        JPanel [] panels = new JPanel[11];
        for(int j=0;j<10;j++){
            panels [j]= new JPanel(new FlowLayout(FlowLayout.CENTER));
        }
        JLabel costLabel;
        JLabel unitLabel;
        JLabel nameLabel;
        JLabel categoryLabel;
        nameLabel = new JLabel("Name: ");
        nameTextField = new JTextField(20);
        panels[0].add(nameLabel);panels[0].add(nameTextField);
        unitLabel = new JLabel("Unit: ");
        unitTextField = new JTextField(20);
        panels[1].add(unitLabel);panels[1].add(unitTextField);
        categoryLabel = new JLabel("Category: ");
        String []category = {"","",""};
        categoryComboBox = new JComboBox<>(category);
        JButton newCategoryButton = new JButton("NEW");
        newCategoryButton.addActionListener(e -> {
            String newCategory = JOptionPane.showInputDialog(null,"NEW CATEGORY NAME:");
            if (!newCategory.equals("")) {
                categoryComboBox.addItem(newCategory);
            }
        });
        panels[2].add(categoryLabel);panels[2].add(categoryComboBox);panels[2].add(newCategoryButton);
        alertUnitLabel = new JLabel("Alert Unit: ");
        alertUnitTextField = new JTextField(20);
        panels[3].add(alertUnitLabel);panels[3].add(alertUnitTextField);
        costLabel = new JLabel("Cost: ");
        costTextField = new JTextField(20);
        panels[4].add(costLabel);panels[4].add(costTextField);
        supplerLabel = new JLabel("Supplier: ");
        supplerTextField = new JTextField(12);
        supplierButton = new JButton("SELECT");
        supplierButton.addActionListener(e ->{
            String[] columnName = {"supplier id","name"};
            DefaultTableModel model = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setColumnIdentifiers(columnName);
            JTable supplierTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(supplierTable);
            try{
                pst = users.con.prepareStatement("SELECT * FROM suppliers");
                ResultSet rs = pst.executeQuery();
                while (rs.next()){
                    model.addRow(new Object[]{rs.getInt(1),rs.getString(2)});
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
            JPanel panel = new JPanel(new BorderLayout(5,5));
            panel.add(scrollPane);
            supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            Object [] options = {"SELECT ","CANCEL"};
            int ans =JOptionPane.showOptionDialog(null,panel,"SELECT SUPPLIER",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if (ans ==0){
                int row =supplierTable.getSelectedRow();
                if(row>=0){
                    supplerTextField.setText(""+supplierTable.getValueAt(row,0));
                }else{
                    JOptionPane.showMessageDialog(null,"NO SUPPLIER WAS SELECTED");
                }
            }
            });
        panels[5].add(supplerLabel);panels[5].add(supplerTextField);panels[5].add(supplierButton);
        stutusLabel = new JLabel("Status: ");
        stutusTextField = new JTextField(20);
        panels[6].add(stutusLabel);panels[6].add(stutusTextField);
        locationLabel = new JLabel("Location: ");
        locationTextField = new JTextField(20);
        panels[7].add(locationLabel);panels[7].add(locationTextField);
        JPanel formPanel2 =new JPanel(new GridLayout(5,2));
        for (int j=0;j<8;j++){
            formPanel2.add(panels[j]);
        }
        panel2 = new JPanel(new BorderLayout(5,10));
        panel2.add(comboPanel,BorderLayout.NORTH);
        panel2.add(formPanel2,BorderLayout.CENTER);
        //south border
        JButton createButton = new JButton("+ CREATE");
        JButton cancelButton = new JButton("CANCEL");
        southPanel = new JPanel(new GridLayout(2,6));
        southPanel.add(new JLabel());southPanel.add(cancelButton);southPanel.add(new JLabel());
        southPanel.add(new JLabel());southPanel.add(createButton);southPanel.add(new JLabel());
        for (int j =7;j<=12;j++){
            southPanel.add(new JLabel());
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
        BorderLayout borderLayout = new BorderLayout(25,15);
        users.frame.setLayout(borderLayout);
        users.frame.add(mainLabelPanel, BorderLayout.NORTH);
        users.frame.add(panel2,BorderLayout.CENTER);
        users.frame.add(southPanel, BorderLayout.SOUTH);
        users.frame.add(westPanel, BorderLayout.WEST);
        users.frame.add(eastPanel, BorderLayout.EAST);
        users.frame.setVisible(true);
        selectedComboItem(users);
        //listeners
        cancelButton.addActionListener(e -> {
            removeComponents(users);
            new ProductAndAssets(users);
        });
        chooseComboBox.addItemListener((ItemEvent e)-> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectedComboItem(users);
            }else if (e.getStateChange()==ItemEvent.DESELECTED){
                selectedComboItem(users);
            }
        });
        createButton.addActionListener(e -> {
            String name,categorySelected, status,location;
            float cost;
            int unit,alertUnit;
            name = nameTextField.getText();
            categorySelected = categoryComboBox.getSelectedItem().toString();
            try{
                cost = Float.parseFloat(costTextField.getText());
            }catch(NumberFormatException nfe){
                nfe.printStackTrace(); cost =0;
            }
            try{
                unit = Integer.parseInt(unitTextField.getText());
            }catch(NumberFormatException nfe){
                nfe.printStackTrace();
                unit = 1;
            }

            if(chooseComboBox.getSelectedItem().toString().equals("Product")){
                try{
                    alertUnit = Integer.parseInt(alertUnitTextField.getText());
                }catch(NumberFormatException nfe){
                    nfe.printStackTrace();
                    alertUnit =0;
                }
                try {
                    pst = users.con.prepareStatement("INSERT INTO product(product_name,unit,category,alert_unit," +
                            "cost,supplied_date,supplier_id)" +
                            "VALUES(?,?,?,?,?,?,?)");
                    pst.setString(1,name);
                    pst.setInt(2,unit);
                    pst.setString(3,categorySelected);
                    pst.setInt(4,alertUnit);
                    pst.setFloat(5,cost);
                    long millis = System.currentTimeMillis();
                    java.sql.Date todayDate = new java.sql.Date(millis);
                    pst.setDate(6,todayDate);
                    pst.setInt(7,Integer.parseInt(supplerTextField.getText()));
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,
                            chooseComboBox.getSelectedItem().toString()+" CREATED");
                    resetForm();
                }catch (SQLException se){
                    JOptionPane.showMessageDialog(null,""+se.getMessage());
                }
            }else if(chooseComboBox.getSelectedItem().toString().equals("Asset")){
                status = stutusTextField.getText();
                location = locationTextField.getText();
                try {
                    pst = users.con.prepareStatement("INSERT INTO assets(asset_name,unit,location," +
                            "cost,Status,category)" +
                            "VALUES(?,?,?,?,?,?)");
                    pst.setString(1,name);
                    pst.setInt(2,unit);
                    pst.setString(6,categorySelected);
                    pst.setString(3,location);
                    pst.setFloat(4,cost);
                    pst.setString(5,status);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,
                            chooseComboBox.getSelectedItem().toString()+" CREATED");
                    resetForm();
                }catch (SQLException se){
                    JOptionPane.showMessageDialog(null,""+se.getMessage());
                }
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
    private void selectedComboItem(Users users){
        if(chooseComboBox.getSelectedItem().toString().equals("Product")){
            categoryComboBox.removeAllItems();
            try{
                pst = users.con.prepareStatement("SELECT DISTINCT category FROM product");
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    categoryComboBox.addItem(""+rs.getString("category"));
                }
            }catch (SQLException se){
                se.printStackTrace();
            }

            alertUnitLabel.setEnabled(true);
            alertUnitTextField.setEnabled(true);
            supplerLabel.setEnabled(true);
            supplerTextField.setEnabled(true);
            supplierButton.setEnabled(true);
            locationLabel.setEnabled(false);
            locationTextField.setEnabled(false);
            stutusLabel.setEnabled(false);
            stutusTextField.setEnabled(false);
        }else if(chooseComboBox.getSelectedItem().toString().equals("Asset")){
            categoryComboBox.removeAllItems();
            try{
                pst = users.con.prepareStatement("SELECT DISTINCT category FROM assets");
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    categoryComboBox.addItem(""+rs.getString("category"));
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
            locationLabel.setEnabled(true);
            locationTextField.setEnabled(true);
            stutusLabel.setEnabled(true);
            stutusTextField.setEnabled(true);
            alertUnitLabel.setEnabled(false);
            alertUnitTextField.setEnabled(false);
            supplerLabel.setEnabled(false);
            supplerTextField.setEnabled(false);
            supplierButton.setEnabled(false);
        }
    }
    private void resetForm(){
        locationTextField.setText("");
        stutusTextField.setText("");
        alertUnitTextField.setText("");
        supplerTextField.setText("");
        costTextField.setText("");
        nameTextField.setText("");
        unitTextField.setText("");
    }
}
