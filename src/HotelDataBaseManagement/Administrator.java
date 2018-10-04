package HotelDataBaseManagement;

import LogIn.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Administrator {
    private JLabel administratorLabel;
    public JPanel panel;
    Administrator(Users users){
        PreparedStatement pst;
        JScrollPane scrollPane;
        JLabel activityLabel;
        JButton activityButton;
        //Title label
        Font forte = new Font("forte",Font.BOLD,30);
        administratorLabel = new JLabel("ADMINISTRATOR PANEL");
        administratorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        administratorLabel.setBackground(Color.darkGray);
        administratorLabel.setFont(forte);
        //Activity tables, labels and buttons
        activityLabel = new JLabel("Recent Database Activities");
        activityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        String[] columnName = {"Event_Time", "User_host", "thread_Id", "Command_Type","argument"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);
        JTable activityTable = new JTable(model);
        scrollPane = new JScrollPane(activityTable);
        try {
            String sql = "SELECT * FROM mysql.general_log order by (event_time) desc";
            pst = users.con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("event_time"), rs.getString("user_host"),
                        rs.getString("thread_id"),
                        rs.getString("command_type"), rs.getString("argument")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }
        JPanel buttonPanel = new JPanel(new GridLayout(2,6));
        JButton cancelButton = new JButton("Back");
        activityButton = new JButton("more details");
        buttonPanel.add(new JLabel());
        buttonPanel.add(cancelButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(new JLabel());
        buttonPanel.add(activityButton);
        buttonPanel.add(new JLabel());
        for (int j=7;j<=12;j++){
            buttonPanel.add(new JLabel());
        }

        panel = new JPanel(new BorderLayout(10,10));
        panel.add(activityLabel, BorderLayout.NORTH);
        panel.add(scrollPane,BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        //main frame
        users.frame.setLayout(new BorderLayout(10,20));
        users.frame.add(administratorLabel,BorderLayout.NORTH);
        users.frame.add(panel,BorderLayout.CENTER);
        //Action Listeners
        cancelButton.addActionListener(e -> {
            removeComponents(users);
            new ManageUsers(users);
        });
    }
    private void removeComponents(Users users){
        panel.removeAll();
        panel.repaint();
        panel.revalidate();
        users.frame.remove(panel);
        users.frame.remove(administratorLabel);
        users.frame.repaint();
        users.frame.revalidate();
    }
}
