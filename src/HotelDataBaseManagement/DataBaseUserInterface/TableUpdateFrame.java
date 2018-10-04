package HotelDataBaseManagement.DataBaseUserInterface;

import LogIn.Users;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.sql.PreparedStatement;
import java.util.Vector;

/**
 * Created by Malone-Ibra on 6/3/2018.
 *
 */
class TableUpdateFrame extends JInternalFrame {
    protected JTable table;
    protected JTextArea SQLPane = new JTextArea();
    protected JButton updateButton = new JButton("Update Data");
    protected DatabaseUtilities dbUtils = new DatabaseUtilities();
    protected String tableName = null;
    protected String colNames[] = null;
    protected String dataTypes[] = null;
    protected String SQLCommand[] = null;
    protected String SQLCommandRoot = "";
   TableUpdateFrame(String tableName,Users users, DatabaseUtilities dbUtils){
       setSize(600,400);
       setLocation(10,10);
       setClosable(true);
       setMaximizable(true);
       setIconifiable(true);
       setResizable(true);
       getContentPane().setLayout(new BorderLayout());
       this.tableName=tableName;
       this.dbUtils= dbUtils;
       SQLCommandRoot = "UPDATE "+tableName+" VALUES ";
       setTitle(SQLCommandRoot);
       init(users);
       setVisible(true);
   }
    // initialise the JInternalFrame
    private void init(Users users){
        String SQLQuery = "SELECT * FROM "+tableName;
        colNames = dbUtils.getColumnNames(tableName,users);
        dataTypes = dbUtils.getDataTypes(tableName,users);
        Vector dataSet = dbUtils.executeQuery(SQLQuery,users);
        table = createTable(colNames,dataSet);
        TableChangeListener modelListener = new TableChangeListener();
        table.getModel().addTableModelListener(modelListener);
        JScrollPane sqlScroller = new JScrollPane(SQLPane);
        JScrollPane tableScroller = new JScrollPane(table);
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                sqlScroller,tableScroller);
        splitter.setDividerLocation(100);
        getContentPane().add(splitter,BorderLayout.CENTER);
        getContentPane().add(updateButton,BorderLayout.SOUTH);
        updateButton.addActionListener(e -> dbUtils.execute(SQLCommand,users));
    }
    protected JTable createTable(String[] colNames,Vector dataSet){
        int nRows = dataSet.size();
        String[][] rowData = new String[nRows][colNames.length];
        for(int i=0;i<nRows;i++){
            Vector row = (Vector)dataSet.elementAt(i);
            for(int j=0;j<row.size();j++) {
                try {
                    rowData[i][j]=(row.elementAt(j)).toString();
                }catch (NullPointerException npe){
                    rowData[i][j]=" ";
                }
            }
        }
        JTable table = new JTable(rowData,colNames);
        return table;
    }
    private Vector parseTable(){
        int rows = table.getRowCount();
        int cols = table.getColumnCount();
        Vector tableValues = new Vector();
        if(rows>=0&&cols>=0){
            for(int i=0;i<rows;i++){
                String rowData = "";
                for(int j=0;j<cols;j++){
                    String field = (String)table.getValueAt(i,j);
                    if(field.length()>0){
                        field = fixApostrophes(field);
                        if(j>0)rowData += ", ";
                        if(dataTypes[j].equals("CHAR")||
                                dataTypes[j].equals("VARCHAR"))
                            rowData += "'"+field+"'";
                        else
                            rowData += field;
                    }
                }
                if(rowData.length()==0)break;
                tableValues.addElement(" ( " + rowData + " );\n");
            }
        }
        return tableValues;
    }
    private String fixApostrophes(String in){
        int n=0;
        while((n=in.indexOf("'",n))>=0){
            in = in.substring(0,n)+"'"+in.substring(n);
            n+=2;
        }
        return in;
    }
    // Listener for Edit events on the JTable
    class TableChangeListener implements TableModelListener {
        TableChangeListener(){
        }
        public void tableChanged(TableModelEvent event){
            Vector rowData = parseTable();
            SQLCommand = new String[rowData.size()];
            SQLPane.setText("");
            for(int i=0;i<rowData.size();i++){
                if(rowData.elementAt(i)==null)break;
                SQLCommand[i] = SQLCommandRoot+(String)rowData.elementAt(i);
                SQLPane.append(SQLCommand[i]);
            }
        }
    }
}
