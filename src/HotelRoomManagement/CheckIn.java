package HotelRoomManagement;

import LogIn.GenerateReceipt;
import LogIn.Users;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

class CheckIn {
    private JComboBox<String> stateComboBox;
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel buttonPanel;
    private JPanel roomPanel;
    CheckIn(Users users,Reception reception){
        //ROOM LABELS
        roomPanel = new JPanel(new GridLayout(6,2,10,0));
        Font haettenschweiler = new Font("SketchFlow Print",Font.BOLD,12);
        JLabel roomNumberLabel = new JLabel("ROOM: ",SwingConstants.TRAILING);roomNumberLabel.setFont(haettenschweiler);
        JTextField roomNoTextField = new JTextField(""+reception.roomNo+"",20);roomNoTextField.setEditable(false);
        JPanel roomNoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        roomNoPanel.add(roomNumberLabel);roomNoPanel.add(roomNoTextField);
        JLabel roomTypeLabel = new JLabel("ROOM TYPE: ",SwingConstants.TRAILING);roomTypeLabel.setFont(haettenschweiler);
        JTextField roomTypeTextField = new JTextField(reception.roomType,20);roomTypeTextField.setEditable(false);
        JPanel roomTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        roomTypePanel.add(roomTypeLabel);roomTypePanel.add(roomTypeTextField);
        JLabel nightPriceLabel = new JLabel("PRICE PER NIGHT:"); nightPriceLabel.setFont(haettenschweiler);
        JTextField nightPriceTextField = new JTextField(""+reception.roomPrice+"",7);nightPriceTextField.setEditable(false);
        JPanel nightPricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        nightPricePanel.add(nightPriceLabel);nightPricePanel.add(nightPriceTextField);
        //ENTER DETAILS
        JLabel fNameLabel = new JLabel("FIRST NAME: ",SwingConstants.TRAILING); fNameLabel.setFont(haettenschweiler);
        JTextField fNameTextField = new JTextField(20);
        JPanel fNamePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,10,10));
        fNamePanel.add(fNameLabel);fNamePanel.add(fNameTextField);
        JLabel sNameLabel = new JLabel("SECOND NAME:",SwingConstants.TRAILING); sNameLabel.setFont(haettenschweiler);
        JTextField sNameTextField = new JTextField(20);
        JPanel sNamePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,10,10));
        sNamePanel.add(sNameLabel);sNamePanel.add(sNameTextField);
        JLabel nationIdLabel = new JLabel("NATION I.D: ",SwingConstants.TRAILING);nationIdLabel.setFont(haettenschweiler);
        JTextField nationalIDTextField = new JTextField(20);
        JPanel nationalIDPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,10,10));
        nationalIDPanel.add(nationIdLabel);
        nationalIDPanel.add(nationalIDTextField);
        JCheckBox prePayment = new JCheckBox("pre-payment");
        prePayment.setFont(haettenschweiler);
        JPanel prePaymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        prePaymentPanel.add(prePayment);
        JLabel nightsLabel = new JLabel("Stay -in days"); nightsLabel.setFont(haettenschweiler);
        JTextField nightsTextField = new JTextField("1",6);
        JPanel nightsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        nightsPanel.add(nightsLabel);nightsPanel.add(nightsTextField);
        float totalPrice=reception.roomPrice*1;
        JLabel priceLabel = new JLabel("Cash Given:"); priceLabel.setFont(haettenschweiler);
        JTextField priceTextField = new JTextField(""+totalPrice+"",7);
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        pricePanel.add(priceLabel); pricePanel.add(priceTextField);
        JLabel paymentMethodLabel = new JLabel("PAYMENT METHOD: ",SwingConstants.TRAILING);
        paymentMethodLabel.setFont(haettenschweiler);
        String [] paymentOption = {"Cash","M-Pesa","CREDIT CARD"};
        JComboBox<String> paymentMethodComboBox = new JComboBox<>(paymentOption);
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        paymentPanel.add(paymentMethodLabel);
        paymentPanel.add(paymentMethodComboBox);
        String [] state1 = {"CHECK IN","BOOK"};
        JLabel stateLabel = new JLabel("State: "); stateLabel.setFont(haettenschweiler);
        stateComboBox = new JComboBox<>(state1);
        JPanel statePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        statePanel.add(stateLabel);statePanel.add(stateComboBox);
        JLabel expectedDateLabel = new JLabel("Check In Date: ");expectedDateLabel.setFont(haettenschweiler);
        JLabel yearLabel = new JLabel("YYYY-");
        JTextField yearTextField = new JTextField(4);
        JLabel monthLabel = new JLabel("MM-");
        JTextField monthTextField = new JTextField(2);
        JLabel dayLabel = new JLabel("DD-");
        JTextField dayTextField = new JTextField(2);
        JPanel expectedDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        expectedDatePanel.add(expectedDateLabel);expectedDatePanel.add(yearLabel);expectedDatePanel.add(yearTextField);
        expectedDatePanel.add(monthLabel);expectedDatePanel.add(monthTextField);
        expectedDatePanel.add(dayLabel);expectedDatePanel.add(dayTextField);
        roomPanel.add(roomNoPanel); roomPanel.add(statePanel);
        roomPanel.add(roomTypePanel); roomPanel.add(expectedDatePanel);
        roomPanel.add(nightPricePanel); roomPanel.add(prePaymentPanel);
        roomPanel.add(fNamePanel); roomPanel.add(nightsPanel);
        roomPanel.add(sNamePanel);roomPanel.add(pricePanel);
        roomPanel.add(nationalIDPanel);roomPanel.add(paymentPanel);
        if (!prePayment.isSelected()){
            paymentMethodLabel.setEnabled(false);
            priceLabel.setEnabled(false);
            nightsLabel.setEnabled(false);
            nightsTextField.setText("xxx");
            nightsTextField.setEnabled(false);
            paymentMethodComboBox.setEnabled(false);
            priceTextField.setText("00.00");
            priceTextField.setEnabled(false);
        }else if (prePayment.isSelected()){
            paymentMethodLabel.setEnabled(true);
            priceLabel.setEnabled(true);
            nightsLabel.setEnabled(true);
            nightsTextField.setEnabled(true);
            nightsTextField.setText("1");
            priceTextField.setEnabled(true);
            priceTextField.setText(""+totalPrice);
            paymentMethodComboBox.setEnabled(true);
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
        if (stateComboBox.getSelectedItem().toString().equals("BOOK")){
            checkInButton.setText("BOOK");
            expectedDateLabel.setEnabled(true);
            yearLabel.setEnabled(true);
            yearTextField.setEnabled(true);
            monthLabel.setEnabled(true);
            monthTextField.setEnabled(true);
            dayLabel.setEnabled(true);
            dayTextField.setEnabled(true);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH);
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            yearTextField.setText(""+year);
            monthTextField.setText(""+month);
            dayTextField.setText(""+day);
        }else if (stateComboBox.getSelectedItem().toString().equals("CHECK IN")){
            checkInButton.setText("CHECK IN");
            expectedDateLabel.setEnabled(false);
            yearLabel.setEnabled(false);
            yearTextField.setEnabled(false);
            monthLabel.setEnabled(false);
            monthTextField.setEnabled(false);
            dayLabel.setEnabled(false);
            dayTextField.setEnabled(false);
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
        /*//Outer frame*/
        reception.panel2.setLayout(new BorderLayout(250,10));
        reception.panel2.add(roomPanel,BorderLayout.CENTER);
        reception.panel2.add(buttonPanel, BorderLayout.SOUTH);
        reception.panel2.add(eastPanel, BorderLayout.EAST);
        reception.panel2.add(westPanel, BorderLayout.WEST);
        //action listeners
        cancelButton.addActionListener(e->cancel(reception));
        stateComboBox.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (stateComboBox.getSelectedItem().toString().equals("BOOK")){
                    checkInButton.setText("BOOK");
                    expectedDateLabel.setEnabled(true);
                    yearLabel.setEnabled(true);
                    yearTextField.setEnabled(true);
                    monthLabel.setEnabled(true);
                    monthTextField.setEnabled(true);
                    dayLabel.setEnabled(true);
                    dayTextField.setEnabled(true);
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM");
                    int month = Integer.parseInt(dateFormat.format(Calendar.getInstance().getTime()));
                    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    yearTextField.setText(""+year);
                    monthTextField.setText(""+month);
                    dayTextField.setText(""+day);
                }else if (stateComboBox.getSelectedItem().toString().equals("CHECK IN")){
                    checkInButton.setText("CHECK IN");
                    expectedDateLabel.setEnabled(false);
                    yearLabel.setEnabled(false);
                    yearTextField.setEnabled(false);
                    monthLabel.setEnabled(false);
                    monthTextField.setEnabled(false);
                    dayLabel.setEnabled(false);
                    dayTextField.setEnabled(false);
                    yearTextField.setText("");
                    monthTextField.setText("");
                    dayTextField.setText("");
                }
            }else if (e.getStateChange() == ItemEvent.DESELECTED) {
                if (stateComboBox.getSelectedItem().toString().equals("BOOK")){
                    checkInButton.setText("BOOK");
                    expectedDateLabel.setEnabled(true);
                    yearLabel.setEnabled(true);
                    yearTextField.setEnabled(true);
                    monthLabel.setEnabled(true);
                    monthTextField.setEnabled(true);
                    dayLabel.setEnabled(true);
                    dayTextField.setEnabled(true);
                }else if (stateComboBox.getSelectedItem().toString().equals("CHECK IN")){
                    checkInButton.setText("CHECK IN");
                    expectedDateLabel.setEnabled(false);
                    yearLabel.setEnabled(false);
                    yearTextField.setEnabled(false);
                    monthLabel.setEnabled(false);
                    monthTextField.setEnabled(false);
                    dayLabel.setEnabled(false);
                    dayTextField.setEnabled(false);
                }
            }
        });
        nightsTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                try {
                    int nights = Integer.parseInt(nightsTextField.getText());
                    float j = nights * reception.roomPrice;
                    priceTextField.setText(""+j);
                }catch (NumberFormatException nfe ){
                    float j=reception.roomPrice*1;
                    priceTextField.setText(""+j);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    int nights = Integer.parseInt(nightsTextField.getText());
                    float j = nights * reception.roomPrice;
                    priceTextField.setText(""+j);
                }catch (NumberFormatException nfe ){
                    JOptionPane.showMessageDialog(null,"Stay-IN Days Fields is Empty or Wrong Number Format");
                    nightsTextField.requestFocus();
                    float j=reception.roomPrice*1;
                    priceTextField.setText(""+j);
                }
            }
        });
        prePayment.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (!prePayment.isSelected()){
                    paymentMethodLabel.setEnabled(false);
                    priceLabel.setEnabled(false);
                    nightsLabel.setEnabled(false);
                    nightsTextField.setText("xxx");
                    nightsTextField.setEnabled(false);
                    paymentMethodComboBox.setEnabled(false);
                    priceTextField.setText("00.00");
                    priceTextField.setEnabled(false);
                }else if (prePayment.isSelected()){
                    paymentMethodLabel.setEnabled(true);
                    priceLabel.setEnabled(true);
                    nightsLabel.setEnabled(true);
                    nightsTextField.setEnabled(true);
                    nightsTextField.setText("1");
                    priceTextField.setEnabled(true);
                    priceTextField.setText(""+totalPrice);
                    paymentMethodComboBox.setEnabled(true);
                }
            }else if (e.getStateChange() == ItemEvent.DESELECTED) {
                if (!prePayment.isSelected()){
                    nightsTextField.setText("xxx");
                    nightsTextField.setEnabled(false);
                    paymentMethodComboBox.setEnabled(false);
                    priceTextField.setText("00.00");
                    priceTextField.setEnabled(false);
                }else if (prePayment.isSelected()){
                    paymentMethodLabel.setEnabled(true);
                    priceLabel.setEnabled(true);
                    nightsLabel.setEnabled(true);
                    nightsTextField.setEnabled(true);
                    nightsTextField.setText("1");
                    priceTextField.setEnabled(true);
                    priceTextField.setText(""+totalPrice);
                    paymentMethodComboBox.setEnabled(true);
                }
            }
        });
        checkInButton.addActionListener(e->{
            String names = fNameTextField.getText()+" "+sNameTextField.getText();
            try {
                String state;
                if (stateComboBox.getSelectedIndex()==0){
                    state = "OCCUPIED";
                }else if (stateComboBox.getSelectedIndex()==1){
                    state = "BOOKED";
                }else {
                    state = "OCCUPIED";
                }
                if (!prePayment.isSelected()){
                    PreparedStatement pst = users.con.prepareStatement("INSERT INTO room_customers(Names, Room_No, " +
                            "Checked_In_By, National_ID, State, Check_In_Date)" +
                            "VALUES(?,?,?,?,?,?)");
                    pst.setString(1,names);
                    pst.setInt(2,Integer.parseInt(roomNoTextField.getText()));
                    pst.setString(3,users.userName);
                    try {
                        pst.setInt(4,Integer.parseInt(nationalIDTextField.getText()));
                    }catch (NumberFormatException nfe ){
                        JOptionPane.showMessageDialog(null,"National ID Fields is Empty or Wrong Number Format");
                    }
                    pst.setString(5,state);
                    if (state.equals("OCCUPIED")) {
                        long millis = System.currentTimeMillis();
                        java.sql.Date todayDate = new java.sql.Date(millis);
                        pst.setDate(6, todayDate);
                    }else if (state.equals("BOOKED")){
                        String dateMonth;
                        String dateDay;
                        int year = Integer.parseInt(yearTextField.getText());
                        int month = Integer.parseInt(monthTextField.getText());
                        if (month<10){
                            dateMonth = "0"+month;
                        }else{
                            dateMonth = ""+month;
                        }
                        int day = Integer.parseInt(dayTextField.getText());
                        if (day<10){
                            dateDay = "0"+day;
                        }else {
                            dateDay = ""+day;
                        }
                        String date = year+"-"+dateMonth+"-"+dateDay;
                        System.out.println(date);
                        pst.setString(6,date);
                    }
                    pst.executeUpdate();
                    if (state.equals("OCCUPY")) {
                        JOptionPane.showMessageDialog(null, "Room: " + roomNoTextField.getText() + " Checked In Successfully");
                    }else if (state.equals("BOOKED")){
                        JOptionPane.showMessageDialog(null, "Room: " + roomNoTextField.getText() + " BOOKED Successfully");
                    }
                }else if (prePayment.isSelected()){
                    String date = "";
                    PreparedStatement pst = users.con.prepareStatement("INSERT INTO room_customers(Names, Room_No, " +
                            "Room_Total_Price, Checked_In_By, National_ID, State, Check_In_Date)" +
                            "VALUES(?,?,?,?,?,?,?)");
                    pst.setString(1,names);
                    pst.setInt(2,Integer.parseInt(roomNoTextField.getText()));
                    pst.setFloat(3,Float.parseFloat(priceTextField.getText()));
                    pst.setString(4,users.userName);
                    try {
                        pst.setInt(5,Integer.parseInt(nationalIDTextField.getText()));
                    }catch (NumberFormatException nfe ){
                        JOptionPane.showMessageDialog(null,"National ID Fields is Empty or Wrong Number Format");
                    }
                    pst.setString(6,state);
                    if (state.equals("OCCUPIED")) {
                        long millis = System.currentTimeMillis();
                        java.sql.Date todayDate = new java.sql.Date(millis);
                        pst.setDate(7, todayDate);
                        date =""+todayDate;
                    }else if (state.equals("BOOKED")){
                        String dateMonth;
                        String dateDay;
                        int year = Integer.parseInt(yearTextField.getText());
                        int month = Integer.parseInt(monthTextField.getText());
                        if (month<10){
                            dateMonth = "0"+month;
                        }else{
                            dateMonth = ""+month;
                        }
                        int day = Integer.parseInt(dayTextField.getText());
                        if (day<10){
                            dateDay = "0"+day;
                        }else {
                            dateDay = ""+day;
                        }
                        date = year+"-"+dateMonth+"-"+dateDay;
                        System.out.println(date);
                        pst.setString(7,date);
                    }
                    pst.executeUpdate();
                    if (state.equals("OCCUPY")) {
                        JOptionPane.showMessageDialog(null, "Room: " + roomNoTextField.getText() + " Checked In Successfully");
                    }else if (state.equals("BOOKED")){
                        JOptionPane.showMessageDialog(null, "Room: " + roomNoTextField.getText() + " BOOKED Successfully");
                    }
                    int p = JOptionPane.showConfirmDialog(null,"Click yes to generate Receipt","RECEIPT", JOptionPane.YES_NO_OPTION);
                    if(p==0) {
                        try {
                            Paragraph[] paragraphs = new Paragraph[50];
                            paragraphs[0] = new Paragraph("Customer Name: " + names);
                            paragraphs[1] = new Paragraph("Room Number: " + roomNoTextField.getText());
                            paragraphs[2] = new Paragraph("Check In Date: " + date);
                            paragraphs[3] = new Paragraph("Total Amount Paid: " + priceTextField.getText());
                            paragraphs[4] = new Paragraph("Payment Method: ");
                            long receiptNo = 0;
                            new GenerateReceipt(users, receiptNo, paragraphs);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
                String sql = "UPDATE rooms \n" +
                        "SET State = ? " +
                        "WHERE Room_No = ?;";
                PreparedStatement pst  = users.con.prepareStatement(sql);
                pst.setString(1, state);
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
        reception.panel2.remove(buttonPanel);
        reception.panel2.remove(roomPanel);
        reception.panel2.repaint();
        reception.panel2.revalidate();
        reception.panel2.remove(westPanel);
        reception.panel2.remove(eastPanel);
        reception.addTab();
    }
}
