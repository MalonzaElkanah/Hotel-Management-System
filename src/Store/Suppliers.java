package Store;

import LogIn.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Malone-Ibra on 7/25/2018.
 *
 */
class Suppliers {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JPanel panel2;
    private JPanel mainLabelPanel;
    private PreparedStatement pst;
    private JTable suppliersTable;
    Suppliers(Users users){
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
        //supplier TABLE
        suppliersTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(suppliersTable);
        panel2 = new JPanel(new BorderLayout(5,5));
        panel2.add(scrollPane,BorderLayout.CENTER);
        //south border
        JButton createButton = new JButton("+ NEW SUPPLIER");
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
        refresh(users);
        //listeners
        cancelButton.addActionListener(e -> {
            removeComponents(users);
            new ProductAndAssets(users);
        });
        createButton.addActionListener(e -> {
            JPanel [] panels = new JPanel[3];
            for (int j =0;j<3;j++){
                panels[j] = new JPanel(new FlowLayout(FlowLayout.CENTER));
            }
            JLabel supplierName = new JLabel("Supplier Name: ");
            JTextField nameTextField = new JTextField(20);
            panels[0].add(supplierName);panels[0].add(nameTextField);
            JLabel supplierPhone = new JLabel("Supplier Phone: ");
            JTextField phoneTextField = new JTextField(20);
            panels[2].add(supplierPhone);panels[2].add(phoneTextField);
            JLabel supplierEmail = new JLabel("Supplier Email: ");
            JTextField emailTextField = new JTextField(20);
            panels[1].add(supplierEmail);panels[1].add(emailTextField);
            JPanel joptionPanel = new JPanel(new GridLayout(4,1));
            for (int j = 0;j<3;j++){
                joptionPanel.add(panels[j]);
            }
            Object [] options = {"CREATE ","CANCEL"};
            int ans =JOptionPane.showOptionDialog(null,joptionPanel,"CREATE SUPPLIER",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if (ans ==0){
                try{
                    pst = users.con.prepareStatement("INSERT INTO suppliers(supplier_name,email,phone_no)" +
                            "VALUES(?,?,?)");
                    pst.setString(1,nameTextField.getText());
                    pst.setString(2,emailTextField.getText());
                    pst.setString(3,phoneTextField.getText());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"SUPPLIER "+nameTextField.getText()+" CREATED");
                    refresh(users);
                }catch (SQLException se){
                    se.printStackTrace();
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
    private void refresh(Users users){
        String[] columnName = {"supplier id","name","email","Phone Number"};
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(columnName);
        suppliersTable.setModel(model);
        try{
            pst = users.con.prepareStatement("SELECT * FROM suppliers");
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                model.addRow(new Object[]{rs.getInt(1),rs.getString(2),
                        rs.getString(3),rs.getString(4)});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
    }
}
