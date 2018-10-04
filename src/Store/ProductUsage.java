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
class ProductUsage {

    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JPanel panel2;
    private JPanel mainLabelPanel;
    private PreparedStatement pst;
    ProductUsage(Users users){
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
        String[] columnName = {"id","product name","unit","Category","Purpose","Picked By","Given By","Picked Date"};
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(columnName);
        JTable usageTable = new JTable(model);
        try{
            pst = users.con.prepareStatement("SELECT id, product_name, picked_product.unit, category, purpose, " +
                    "picked_by, given_by, picked_date\n" +
                    "    FROM product\n" +
                    "    INNER JOIN picked_product\n" +
                    "    ON product.product_id = picked_product.product_id");
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                model.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getInt(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(6), rs.getDate(8)});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(usageTable);
        panel2 = new JPanel(new BorderLayout(5,5));
        panel2.add(scrollPane,BorderLayout.CENTER);

        //south border
        JButton createButton = new JButton(" ");
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
        //listeners
        cancelButton.addActionListener(e -> {
            removeComponents(users);
            new ProductAndAssets(users);
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
}
