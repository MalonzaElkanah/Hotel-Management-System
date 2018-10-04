package HotelRoomManagement;

import LogIn.GenerateReceipt;
import LogIn.Users;
import com.lowagie.text.Paragraph;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class BookedRooms {
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel buttonPanel;
    private JPanel roomPanel;
    BookedRooms(Users users,Reception reception){
        //ROOM LABELS
        roomPanel = new JPanel(new GridLayout(6,2,10,0));
        Font haettenschweiler = new Font("SketchFlow Print",Font.BOLD,12);
        JLabel roomNumberLabel = new JLabel("ROOM: ",SwingConstants.TRAILING);
        roomNumberLabel.setFont(haettenschweiler);
        JTextField roomNoTextField = new JTextField(""+reception.roomNo+"",17);
        roomNoTextField.setEditable(false);
        JButton changeRoomButton = new JButton("change");
        JPanel roomNoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        roomNoPanel.add(roomNumberLabel);roomNoPanel.add(roomNoTextField);roomNoPanel.add(changeRoomButton);
        JLabel roomTypeLabel = new JLabel("ROOM TYPE: ",SwingConstants.TRAILING);
        roomTypeLabel.setFont(haettenschweiler);
        JTextField roomTypeTextField = new JTextField(reception.roomType,20);
        roomTypeTextField.setEditable(false);
        JPanel roomTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        roomTypePanel.add(roomTypeLabel);roomTypePanel.add(roomTypeTextField);
        JLabel nightPriceLabel = new JLabel("PRICE PER NIGHT:");
        nightPriceLabel.setFont(haettenschweiler);
        JTextField nightPriceTextField = new JTextField(""+reception.roomPrice+"",7);
        nightPriceTextField.setEditable(false);
        JPanel nightPricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        nightPricePanel.add(nightPriceLabel);nightPricePanel.add(nightPriceTextField);
        JLabel fNameLabel = new JLabel("NAMES : ",SwingConstants.TRAILING); fNameLabel.setFont(haettenschweiler);
        JTextField fNameTextField = new JTextField(""+reception.names,20);
        fNameTextField.setEditable(false);
        JPanel fNamePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,10,10));
        fNamePanel.add(fNameLabel);fNamePanel.add(fNameTextField);
        //ENTER DETAILS
        float cashGiven = 0;
        JLabel cashGivenLabel = new JLabel("Cash Given:",SwingConstants.TRAILING);
        cashGivenLabel.setFont(haettenschweiler);
        JTextField cashGivenTextField = new JTextField(10);
        JPanel cashGivenPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        cashGivenPanel.add(cashGivenLabel);cashGivenPanel.add(cashGivenTextField);
        JLabel nationIdLabel = new JLabel("NATION I.D: ",SwingConstants.TRAILING);
        nationIdLabel.setFont(haettenschweiler);
        JTextField nationalIDTextField = new JTextField(""+reception.Id,20);
        nationalIDTextField.setEditable(false);
        JPanel nationalIDPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,10,10));
        nationalIDPanel.add(nationIdLabel);
        nationalIDPanel.add(nationalIDTextField);
        JCheckBox prePayment = new JCheckBox("pre-payment"); prePayment.setFont(haettenschweiler);
        JPanel prePaymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        prePaymentPanel.add(prePayment);
        JLabel nightsLabel = new JLabel("Stay -in days");
        JTextField nightsTextField = new JTextField("1",6);
        JPanel nightsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        nightsPanel.add(nightsLabel);nightsPanel.add(nightsTextField);
        float totalPrice=reception.roomPrice*1;
        JLabel priceLabel = new JLabel("Total Price:");
        JTextField priceTextField = new JTextField(""+totalPrice+"",7); priceTextField.setEditable(false);
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        pricePanel.add(priceLabel); pricePanel.add(priceTextField);
        JLabel paymentMethodLabel = new JLabel("PAYMENT METHOD: ",SwingConstants.TRAILING);
        String [] paymentOption = {"Cash","M-Pesa","CREDIT CARD"};
        JComboBox<String> paymentMethodComboBox = new JComboBox<>(paymentOption);
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        paymentPanel.add(paymentMethodLabel);
        paymentPanel.add(paymentMethodComboBox);
        JLabel prevPaidLabel = new JLabel("Already Paid: ");prevPaidLabel.setFont(haettenschweiler);
        JTextField amountPrevPaid = new JTextField(""+reception.alreadyPaid,20);
        amountPrevPaid.setEditable(false);
        JPanel statePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        statePanel.add(prevPaidLabel);statePanel.add(amountPrevPaid);
        float balance = totalPrice-reception.alreadyPaid-cashGiven;
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        JLabel balanceLabel = new JLabel("Amount Balance:");
        JTextField balanceTextField = new JTextField(""+balance,7);balanceTextField.setEditable(false);
        balancePanel.add(balanceLabel);balancePanel.add(balanceTextField);
        roomPanel.add(roomNoPanel); roomPanel.add(prePaymentPanel);
        roomPanel.add(roomTypePanel); roomPanel.add(cashGivenPanel);
        roomPanel.add(nightPricePanel); roomPanel.add(nightsPanel);
        roomPanel.add(fNamePanel); roomPanel.add(pricePanel);
        roomPanel.add(nationalIDPanel);roomPanel.add(balancePanel);
        roomPanel.add(statePanel);roomPanel.add(paymentPanel);
        if (!prePayment.isSelected()){
            cashGivenTextField.setEnabled(false);
            cashGivenLabel.setEnabled(false);
            cashGivenTextField.setText(" ");
            paymentMethodLabel.setEnabled(false);
            priceLabel.setEnabled(false);
            nightsLabel.setEnabled(false);
            nightsTextField.setText(" ");
            nightsTextField.setEnabled(false);
            paymentMethodComboBox.setEnabled(false);
            priceTextField.setText("00.00");
            priceTextField.setEnabled(false);
            balanceLabel.setEnabled(false);
            balanceTextField.setText(" ");
            balanceTextField.setEnabled(false);
        }else if (prePayment.isSelected()){
            cashGivenTextField.setEnabled(true);
            cashGivenLabel.setEnabled(true);
            cashGivenTextField.setText("0");
            paymentMethodLabel.setEnabled(true);
            priceLabel.setEnabled(true);
            nightsLabel.setEnabled(true);
            nightsTextField.setEnabled(true);
            nightsTextField.setText("1");
            priceTextField.setEnabled(true);
            priceTextField.setText(""+totalPrice);
            paymentMethodComboBox.setEnabled(true);
            balanceLabel.setEnabled(true);
            balanceTextField.setText(""+balance);
            balanceTextField.setEnabled(true);
        }
        //BUTTONS
        GridLayout buttonGridLayout = new GridLayout(2,5,30,0);
        buttonPanel = new JPanel(buttonGridLayout);
        JLabel []label = new JLabel[8];
        for (int j=0;j<8;j++){
            label[j] = new JLabel();
        }
        JButton cancelButton = new JButton("CANCEL");
        JButton checkInButton = new JButton("CHECK IN");
        buttonPanel.add(label[0]);
        buttonPanel.add(cancelButton);
        buttonPanel.add(label[1]);
        buttonPanel.add(checkInButton);
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
        //Outer frame
        reception.jPanel.setLayout(new BorderLayout(250,10));
        reception.jPanel.add(roomPanel,BorderLayout.CENTER);
        reception.jPanel.add(buttonPanel, BorderLayout.SOUTH);
        reception.jPanel.add(eastPanel, BorderLayout.EAST);
        reception.jPanel.add(westPanel, BorderLayout.WEST);
        //action listeners
        cancelButton.addActionListener(e->cancel(reception));
        changeRoomButton.addActionListener(e -> {
            String[] columnName = {"Type","Price","Room Number"};
            DefaultTableModel model = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setColumnIdentifiers(columnName);
            try{
                PreparedStatement pst;
                String state = "AVAILABLE";
                pst = users.con.prepareStatement("SELECT Room_Type, Room_No, Price_Per_Night FROM rooms WHERE State = ?");
                pst.setString(1, state.toLowerCase());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString("Room_Type"), rs.getString("Room_No"),
                            rs.getString("Price_Per_Night")});
                }
            }catch (SQLException se){
                se.printStackTrace();
            }
            JTable availableRoomTable = new JTable(model);
            availableRoomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(availableRoomTable);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            JLabel changeLabel = new JLabel("Select the New Room",SwingConstants.CENTER);
            changeLabel.setFont(haettenschweiler);
            JPanel panel4 =new JPanel(new BorderLayout(1,21));
            panel4.add(changeLabel,BorderLayout.NORTH);
            panel4.add(scrollPane,BorderLayout.CENTER);
            Object [] options = {"CHANGE","CANCEL"};
            int ans =JOptionPane.showOptionDialog(null,panel4,"Database Settings",JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if (ans==0){
                int f = availableRoomTable.getSelectedRow();
                if (f>=0){
                    String type = availableRoomTable.getValueAt(f,0).toString();
                    float price = Float.parseFloat(availableRoomTable.getValueAt(f,2).toString());
                    int room = Integer.parseInt(availableRoomTable.getValueAt(f, 1).toString());
                    roomNoTextField.setText(""+room);
                    roomTypeTextField.setText(type);
                    nightPriceTextField.setText(""+price);
                    if (prePayment.isSelected()) {
                        float cash = Float.parseFloat(cashGivenTextField.getText());
                        priceTextField.setText("" + price);
                        nightsTextField.setText("" + 1);
                        balanceTextField.setText("" + (price - reception.alreadyPaid-cash));
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"NO ROOM WAS SELECTED");
                }
            }
        });
        prePayment.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (!prePayment.isSelected()){
                    cashGivenTextField.setEnabled(false);
                    cashGivenLabel.setEnabled(false);
                    paymentMethodLabel.setEnabled(false);
                    priceLabel.setEnabled(false);
                    nightsLabel.setEnabled(false);
                    nightsTextField.setText("xxx");
                    nightsTextField.setEnabled(false);
                    paymentMethodComboBox.setEnabled(false);
                    priceTextField.setText("00.00");
                    priceTextField.setEnabled(false);
                    balanceLabel.setEnabled(false);
                    balanceTextField.setText("xxx");
                    balanceTextField.setEnabled(false);
                }else if (prePayment.isSelected()){
                    cashGivenTextField.setEnabled(true);
                    cashGivenLabel.setEnabled(true);
                    cashGivenTextField.setText("0");
                    paymentMethodLabel.setEnabled(true);
                    priceLabel.setEnabled(true);
                    nightsLabel.setEnabled(true);
                    nightsTextField.setEnabled(true);
                    nightsTextField.setText("1");
                    priceTextField.setEnabled(true);
                    priceTextField.setText(""+nightPriceTextField.getText());
                    paymentMethodComboBox.setEnabled(true);
                    balanceLabel.setEnabled(true);
                    balanceTextField.setText(""+(Float.parseFloat(nightPriceTextField.getText())-reception.alreadyPaid));
                    balanceTextField.setEnabled(true);
                }
            }else if (e.getStateChange() == ItemEvent.DESELECTED) {
                if (!prePayment.isSelected()){
                    cashGivenTextField.setEnabled(false);
                    cashGivenLabel.setEnabled(false);
                    paymentMethodLabel.setEnabled(false);
                    priceLabel.setEnabled(false);
                    nightsLabel.setEnabled(false);
                    nightsTextField.setText("xxx");
                    nightsTextField.setEnabled(false);
                    paymentMethodComboBox.setEnabled(false);
                    priceTextField.setText("00.00");
                    priceTextField.setEnabled(false);
                    balanceLabel.setEnabled(false);
                    balanceTextField.setText("xxx");
                    balanceTextField.setEnabled(false);
                }else if (prePayment.isSelected()){
                    cashGivenTextField.setEnabled(true);
                    cashGivenLabel.setEnabled(true);
                    cashGivenTextField.setText("0");
                    paymentMethodLabel.setEnabled(true);
                    priceLabel.setEnabled(true);
                    nightsLabel.setEnabled(true);
                    nightsTextField.setEnabled(true);
                    nightsTextField.setText("1");
                    priceTextField.setEnabled(true);
                    priceTextField.setText(""+nightPriceTextField.getText());
                    paymentMethodComboBox.setEnabled(true);
                    balanceLabel.setEnabled(true);
                    balanceTextField.setText(""+(Float.parseFloat(nightPriceTextField.getText())-reception.alreadyPaid));
                    balanceTextField.setEnabled(true);
                }
            }
        });
        cashGivenTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeBalance();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeBalance();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeBalance();
            }
            private void changeBalance(){
                float cash;
                try {
                    cash = Float.parseFloat(cashGivenTextField.getText());
                    float totalAmount = Float.parseFloat(priceTextField.getText());
                    float balance1 = totalAmount-reception.alreadyPaid-cash;
                    balanceTextField.setText(""+balance1);
                }catch (NumberFormatException nfe){
                    cash = 0F;
                    float totalAmount = Float.parseFloat(priceTextField.getText());
                    float balance1 = totalAmount-reception.alreadyPaid-cash;
                    balanceTextField.setText(""+balance1);
                }

            }
        });
        nightsTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                nightsChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                nightsChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                nightsChange();
            }
            private void nightsChange(){
                try {
                    int nights = Integer.parseInt(nightsTextField.getText());
                    float j = nights * Float.parseFloat(nightPriceTextField.getText());
                    float cash = Float.parseFloat(cashGivenTextField.getText());
                    priceTextField.setText(""+j);
                    float balance1 = j-reception.alreadyPaid-cash;
                    balanceTextField.setText(""+balance1);
                }catch (NumberFormatException nfe ){
                    float j=Float.parseFloat(nightPriceTextField.getText())*1;
                    float cash = Float.parseFloat(cashGivenTextField.getText());
                    priceTextField.setText(""+j);
                    float balance1 = j-reception.alreadyPaid - cash;
                    balanceTextField.setText(""+balance1);
                }
            }
        });
        checkInButton.addActionListener(e -> {
            try{
                String sql = "UPDATE room_customers \n" +
                        "SET Names = ?, Room_No = ?, Check_In_Date = ?, Room_Total_Price = ?, Checked_In_By = ?, National_ID = ?, State = ? \n" +
                        "WHERE Customer_ID = ?;";
                PreparedStatement pst = users.con.prepareStatement(sql);
                pst.setString(1, fNameTextField.getText());
                pst.setString(5,users.userName);
                pst.setString(7, "OCCUPIED");
                long millis = System.currentTimeMillis();
                java.sql.Date todayDate = new java.sql.Date(millis);
                pst.setDate(3,todayDate);
                pst.setInt(8, reception.index);
                try {
                    int nationalID = Integer.parseInt(nationalIDTextField.getText());
                    pst.setInt(6, nationalID);
                }catch (NumberFormatException nfe ){
                    JOptionPane.showMessageDialog(null,"National ID is Empty or Wrong Number Format");
                }
                try {
                    int roomNo = Integer.parseInt(roomNoTextField.getText());
                    pst.setInt(2, roomNo);
                }catch (NumberFormatException nfe ){
                    JOptionPane.showMessageDialog(null," Room Number is Empty or Wrong Number Format");
                }
                try {
                    if (prePayment.isSelected()){
                        float cash;
                        try{
                            cash =  Float.parseFloat(cashGivenTextField.getText());
                        }catch (NumberFormatException ne){
                            cash=0F;
                        }
                        float paid = Float.parseFloat(amountPrevPaid.getText())+cash;
                        pst.setFloat(4, paid);
                        int p = JOptionPane.showConfirmDialog(null,"Click yes to generate Receipt",
                                "RECEIPT", JOptionPane.YES_NO_OPTION);
                        if(p==0) {
                            try {
                                Paragraph[] paragraphs = new Paragraph[8];
                                paragraphs[0] = new Paragraph("Customer Name: " + fNameTextField.getText());
                                paragraphs[1] = new Paragraph("Room Number: " + roomNoTextField.getText());
                                paragraphs[2] = new Paragraph("Check In Date: " + todayDate);
                                paragraphs[3] = new Paragraph("Amount Paid: " + cash);
                                paragraphs[4] = new Paragraph("Previous Payment: " + amountPrevPaid.getText());
                                paragraphs[5] = new Paragraph("Total Amount Paid: " + paid);
                                paragraphs[6] = new Paragraph("Payment Method: ");
                                new GenerateReceipt(users, reception.index, paragraphs);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }else {
                        float paid = Float.parseFloat(amountPrevPaid.getText());
                        pst.setFloat(4, paid);
                    }
                }catch (NumberFormatException nfe ){
                    JOptionPane.showMessageDialog(null," Already Paid is Empty or Wrong Number Format");
                }
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null,"ROOM NUMBER: "+roomNoTextField.getText()+
                        " IS CHECKED IN successfully");
                sql = "UPDATE rooms \n" +
                        "SET State = ? " +
                        "WHERE Room_No = ?;";
                pst  = users.con.prepareStatement(sql);
                pst.setString(1, "OCCUPIED");
                pst.setInt(2,Integer.parseInt(roomNoTextField.getText()));
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null,"Room: "+roomNoTextField.getText()+" Up-Dated");
                cancel(reception);
                reception.refresh(users);
            }catch (SQLException se){
                se.printStackTrace();
            }
        });

    }
    private void cancel(Reception reception){
        eastPanel.removeAll();
        eastPanel.repaint();
        eastPanel.revalidate();
        westPanel.removeAll();
        westPanel.repaint();
        westPanel.revalidate();
        buttonPanel.removeAll();
        buttonPanel.repaint();
        buttonPanel.revalidate();
        roomPanel.removeAll();
        roomPanel.repaint();
        roomPanel.revalidate();
        reception.jPanel.remove(buttonPanel);
        reception.jPanel.remove(roomPanel);
        reception.jPanel.repaint();
        reception.jPanel.revalidate();
        reception.jPanel.remove(westPanel);
        reception.jPanel.remove(eastPanel);
        reception.addTab2();
    }
}
