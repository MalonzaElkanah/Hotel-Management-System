package Store;

import LogIn.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 *
 * Created by Malone-Ibra on 7/25/2018.
 */
public class ProductAndAssets {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JPanel panel2;
    private JPanel mainLabelPanel;
    private PreparedStatement pst;
    private JTable productsTable;
    private JTable assetsTable;
    public ProductAndAssets(Users users){
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
        //product table
        productsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(productsTable);
        JButton giveButton = new JButton("PRODUCT CHECK OUT ");
        JPanel productPanel = new JPanel(new BorderLayout(5,5));
        productPanel.add(scrollPane,BorderLayout.CENTER);
        productPanel.add(giveButton,BorderLayout.SOUTH);
        //assets table
        assetsTable = new JTable();
        JScrollPane scrollPane1 = new JScrollPane(assetsTable);
        JPanel assetsPanel = new JPanel(new BorderLayout(5,5));
        assetsPanel.add(scrollPane1,BorderLayout.CENTER);
        //TABS
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("PRODUCTS IN STORE",productPanel);
        tabbedPane.addTab("HOTEL ASSETS ",assetsPanel);
        //Side buttons
        JButton addButton = new JButton("+ ADD");
        JButton suppliersButton = new JButton("SUPPLIERS");
        JButton usageButton = new JButton("USAGE");
        JPanel buttonsPanel = new JPanel(new GridLayout(3,1,10,10));
        buttonsPanel.add(addButton);
        buttonsPanel.add(suppliersButton);
        buttonsPanel.add(usageButton);
        BorderLayout borderLayout2 = new BorderLayout(25,15);
        panel2 =new JPanel(borderLayout2);
        panel2.add(tabbedPane,BorderLayout.CENTER);
        panel2.add(buttonsPanel,BorderLayout.EAST);
        southPanel = new JPanel();
        /*
        //south border
        JButton deleteButton = new JButton("DELETE");
        JButton editButton = new JButton("EDIT");
        southPanel = new JPanel(new GridLayout(2,6));
        southPanel.add(new JLabel());southPanel.add(deleteButton);southPanel.add(new JLabel());
        southPanel.add(new JLabel());southPanel.add(editButton);southPanel.add(new JLabel());
        for (int j =7;j<=12;j++){
            southPanel.add(new JLabel());
        }*/
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
        refresh(users);
        //Listeners
        addButton.addActionListener(e -> {
            removeComponents(users);
            new NewProductAndAssets(users);
        });
        suppliersButton.addActionListener(e -> {
            removeComponents(users);
            new Suppliers(users);
        });
        usageButton.addActionListener(e -> {
            removeComponents(users);
            new ProductUsage(users);
        });
        JButton pickedByButton = new JButton("SELECT");
        JTextField pickedByTextField = new JTextField(10);
        giveButton.addActionListener(e -> {
            int row = productsTable.getSelectedRow();
            if (row>=0){
                JPanel [] panels = new JPanel[12];
                for (int j=0;j<12;j++){
                    panels[j]= new JPanel(new FlowLayout(FlowLayout.CENTER));
                }
                JLabel idLabel = new JLabel("Product ID: ");
                int id = Integer.parseInt( productsTable.getValueAt(row,0).toString());
                JTextField idTextField = new JTextField(""+id,15); idTextField.setEditable(false);
                panels[0].add(idLabel);panels[1].add(idTextField);
                JLabel nameLabel = new JLabel("Name: ");
                String name = productsTable.getValueAt(row,1).toString();
                JTextField nameTextField = new JTextField(""+name,15);nameTextField.setEditable(false);
                panels[2].add(nameLabel);panels[3].add(nameTextField);
                JLabel availableUnitLabel = new JLabel("Available Unit: ");
                int availableUnit = Integer.parseInt( productsTable.getValueAt(row,2).toString());
                JTextField availableUnitTextField = new JTextField(""+availableUnit,15);
                availableUnitTextField.setEditable(false);
                panels[4].add(availableUnitLabel);panels[5].add(availableUnitTextField);
                JLabel checkOutUnit = new JLabel("Check out Unit:");
                JTextField checkOutTextField = new JTextField(15);
                panels[6].add(checkOutUnit);panels[7].add(checkOutTextField);
                JLabel purposeLabel = new JLabel("Purpose: ");
                JTextField purposeTextField = new JTextField(15);
                panels[8].add(purposeLabel);panels[9].add(purposeTextField);
                JLabel pickedByLabel = new JLabel("Picked By: ");
                pickedByTextField.setEditable(false);
                panels[10].add(pickedByLabel);panels[11].add(pickedByTextField);panels[11].add(pickedByButton);
                JPanel jOptionPanel = new JPanel(new GridLayout(7,2));
                for (int j =0;j<12;j++){
                    jOptionPanel.add(panels[j]);
                }
                Object [] options = {"CHECK OUT ","CANCEL"};
                int ans =JOptionPane.showOptionDialog(null,jOptionPanel,"CREATE SUPPLIER",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (ans ==0){
                    if(availableUnit>Integer.parseInt(checkOutTextField.getText())) {
                        try {
                            pst = users.con.prepareStatement("INSERT INTO picked_product(product_id,unit,purpose," +
                                    "picked_by,given_by,picked_date)" +
                                    "VALUES(?,?,?,?,?,?)");
                            pst.setInt(1, id);
                            pst.setInt(2, Integer.parseInt(checkOutTextField.getText()));
                            pst.setString(3, purposeTextField.getText());
                            pst.setString(4, pickedByTextField.getText());
                            pst.setString(5, users.userName);
                            long millis = System.currentTimeMillis();
                            java.sql.Date todayDate = new java.sql.Date(millis);
                            pst.setDate(6, todayDate);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(null,
                                    checkOutTextField.getText() + " " + name + " CHECKED OUT OF STORE");
                            pst = users.con.prepareStatement("UPDATE product " +
                                    "SET unit = ? " +
                                    "WHERE product_id = ?");
                            pst.setInt(1, (availableUnit - Integer.parseInt(checkOutTextField.getText())));
                            pst.setInt(2, id);
                            pst.executeUpdate();
                        } catch (SQLException se) {
                            JOptionPane.showMessageDialog(null, "" + se.getMessage());
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,
                                "ERROR YOUR CHECK OUT UNIT IS MORE THAN AVAILABLE UNIT" );
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"SELECT A PRODUCT TO CHECK OUT");
            }
            refresh(users);
        });
        pickedByButton.addActionListener(e -> {
            String[] columnName = {"Surname"," FirstName", "OtherName", "EmployeeNo"};
            DefaultTableModel model = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setColumnIdentifiers(columnName);
            JTable supplierTable = new JTable(model);
            JScrollPane scrollPane2 = new JScrollPane(supplierTable);
            try{
                pst = users.con.prepareStatement("SELECT Surname, FirstName, OtherName, EmployeeNo FROM employee");
                ResultSet rs = pst.executeQuery();
                while (rs.next()){
                    model.addRow(new Object[]{rs.getInt(4),rs.getString(1),
                            rs.getString(2),rs.getString(3)});
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
            JPanel panel = new JPanel(new BorderLayout(5,5));
            panel.add(scrollPane2);
            supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            Object [] options = {"SELECT ","CANCEL"};
            int ans =JOptionPane.showOptionDialog(null,panel,"SELECT Employee",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if (ans ==0){
                int row =supplierTable.getSelectedRow();
                if(row>=0){
                    pickedByTextField.setText(""+supplierTable.getValueAt(row,1)+" "+
                            supplierTable.getValueAt(row,2)+" "+supplierTable.getValueAt(row,3));
                }else{
                    JOptionPane.showMessageDialog(null,"NO SUPPLIER WAS SELECTED");
                }
            }
        });
    }
    private void refresh(Users users){
        String[] columnName = {"product id","name","unit","Category","alert unit","Cost","Supplied Date","Supplier_name"};
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(columnName);
        productsTable.setModel(model);
        try{
            pst = users.con.prepareStatement("SELECT * FROM product_supplier");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                model.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getInt(3),
                        rs.getString(4),rs.getInt(5),rs.getFloat(6),
                        rs.getDate(7),rs.getString(9)});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        String[] columnName1 = {"id","name","unit","Location","Cost","Status","Category"};
        DefaultTableModel model1 = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model1.setColumnIdentifiers(columnName1);
        assetsTable.setModel(model1);
        try{
            pst = users.con.prepareStatement("SELECT * FROM assets");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                model1.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getInt(3),
                        rs.getString(4),rs.getFloat(5),
                        rs.getString(6),rs.getString(7)});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
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
}
