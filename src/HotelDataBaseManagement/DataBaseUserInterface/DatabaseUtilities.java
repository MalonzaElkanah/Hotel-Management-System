package HotelDataBaseManagement.DataBaseUserInterface;

import LogIn.Users;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

/**
 * Created by Malone-Ibra on 5/27/2018.
 *
 */
class DatabaseUtilities {
    private String dbName = "myhotel";
    private ActionListener exceptionListener = null;
    DatabaseUtilities(){

    }
    void setDatabaseName(String dbName, Users users){
        this.dbName=dbName;
        try {
            String sql = "USE "+this.dbName;
            PreparedStatement pst = users.con.prepareStatement(sql);
            pst.executeQuery();
        }catch (SQLException se){
            reportException(se.getMessage());
        }
    }
    public void execute(String SQLCommand,Users users){
        try {
            Statement stmt = users.con.createStatement();
            stmt.execute(SQLCommand);
        }
        catch(SQLException e){
            reportException(e.getMessage());
        }
    }
    public void execute(String[] SQLCommand, Users users){
        try {
            Statement stmt = users.con.createStatement();
            for(int i=0;i<SQLCommand.length;i++){
                stmt.execute(SQLCommand[i]);
            }
        }
        catch(SQLException e){
            reportException(e.getMessage());
        }
    }
    public String[] getColumnNames(String tableName, Users users){
        Vector dataSet = new Vector();
        String[] columnNames = null;
        String SQLCommand = "SELECT * FROM "+tableName+";";
        try {
            Statement stmt = users.con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommand);
            ResultSetMetaData md = rs.getMetaData();
            columnNames = new String[md.getColumnCount()];
            for(int i=0;i<columnNames.length;i++){
                columnNames[i] = md.getColumnLabel(i+1);
            }
        }
        catch(SQLException e){
            reportException(e.getMessage());
        }
        return columnNames;
    }
    public String[] getColumnNamesUsingQuery(String SQLCommand, Users users){
        Vector dataSet = new Vector();
        String[] columnNames = null;
        try {
            Statement stmt = users.con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommand);
            ResultSetMetaData md = rs.getMetaData();
            columnNames = new String[md.getColumnCount()];
            for(int i=0;i<columnNames.length;i++){
                columnNames[i] = md.getColumnLabel(i+1);
            }
        }
        catch(SQLException e){
            reportException(e.getMessage());
        }
        return columnNames;
    }
    public String[] getDataTypes(String tableName, Users users){
        Vector dataSet = new Vector();
        String[] dataTypes = null;
        String SQLCommand = "SELECT * FROM "+tableName+";";
        try {
            Statement stmt = users.con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommand);
            ResultSetMetaData md = rs.getMetaData();
            dataTypes = new String[md.getColumnCount()];
            for(int i=0;i<dataTypes.length;i++){
                dataTypes[i] = md.getColumnTypeName(i+1);
            }
        }
        catch(SQLException e){
            reportException(e.getMessage());
        }
        return dataTypes;
    }
    public Vector executeQuery(String SQLQuery, Users users){
        Vector dataSet = new Vector();
        try {
            Statement stmt = users.con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLQuery);
            ResultSetMetaData md = rs.getMetaData();
            int nColumns = md.getColumnCount();
            while(rs.next()){
                Vector rowData = new Vector();
                for(int i=1;i<=nColumns;i++){
                    rowData.addElement(rs.getObject(i));
                }
                dataSet.addElement(rowData);
            }
        }
        catch(SQLException e){
            reportException(e.getMessage());
        }
        return dataSet;
    }
    void setExceptionListener(ActionListener exceptionListener){
        this.exceptionListener=exceptionListener;
    }
    void reportException(String exception){
        if(exceptionListener!=null){
            ActionEvent evt = new ActionEvent(this,0,exception);
            exceptionListener.actionPerformed(evt);
        }else{
            JOptionPane.showMessageDialog(null,""+exception);
            System.err.println(exception);
        }
    }
}
