package HotelDataBaseManagement.DataBaseUserInterface;

import LogIn.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * TableQueryFrame extends JInternalFrame to create a display which builds SQL
 *  CREATE statements
 */
class TableQueryFrame extends JInternalFrame{
    protected JTable table;
    protected JScrollPane tableScroller;
    protected JTextArea SQLPane = new JTextArea();
    protected JButton queryButton = new JButton("Execute Query");
    protected DatabaseUtilities dbUtils;
    protected String tableName = null;
    protected String colNames[] = null;
    protected String dataTypes[] = null;
    protected String SQLQuery = null;
    protected String SQLCommandRoot = "";
    public TableQueryFrame(String tableName, DatabaseUtilities dbUtils,Users users){
        System.out.println(tableName+", "+dbUtils);
        setSize(600,400);
        setLocation(10,10);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        getContentPane().setLayout(new BorderLayout());
        this.tableName=tableName;
        this.dbUtils=dbUtils;
        setTitle("Query "+tableName);
        init(users);
        setVisible(true);
    }
    // initialize the JInternalFrame
    private void init(Users users){
        colNames = dbUtils.getColumnNames(tableName,users);
        dataTypes = dbUtils.getDataTypes(tableName,users);
        SQLQuery = "SELECT * FROM "+tableName;
        Vector dataSet = dbUtils.executeQuery(SQLQuery,users);
        table = createTable(colNames,dataSet);
        JScrollPane sqlScroller = new JScrollPane(SQLPane);
        tableScroller = new JScrollPane(table);
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                sqlScroller,tableScroller);
        splitter.setDividerLocation(100);
        getContentPane().add(splitter,BorderLayout.CENTER);
        getContentPane().add(queryButton,BorderLayout.SOUTH);
        queryButton.addActionListener(e -> {
            SQLQuery = SQLPane.getText();
            JViewport viewport = tableScroller.getViewport();
            viewport.remove(table);
            colNames = dbUtils.getColumnNamesUsingQuery(SQLQuery, users);
            Vector dataSet1 = dbUtils.executeQuery(SQLQuery,users);
            table = createTable(colNames,dataSet1);
            viewport.add(table);
        });
    }
    protected JTable createTable(String[] colNames,Vector dataSet){
        int nRows = dataSet.size();
        String[][] rowData = new String[nRows][colNames.length];
        for(int i=0;i<nRows;i++){
            Vector row = (Vector)dataSet.elementAt(i);
            for(int j=0;j<row.size();j++)
                try {
                    rowData[i][j]=(row.elementAt(j)).toString();
                }catch (NullPointerException npe){
                    rowData[i][j]=" ";
                }
        }
        JTable table = new JTable(rowData,colNames);
        return table;
    }
}