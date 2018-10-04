package HotelDataBaseManagement.DataBaseUserInterface;

import LogIn.Users;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * TableEditFrame extends JInternalFrame to create a display which builds
 * SQL CREATE statements
 * <p/>
 * TableBuilder, which extends JTable, is a key component.
 */
class TableEditFrame extends JInternalFrame{
    protected JTable table;
    protected JTextArea SQLPane = new JTextArea();
    protected JButton insertButton = new JButton("Insert Data");
    protected DatabaseUtilities dbUtils;
    protected String tableName = null;
    protected String colNames[] = null;
    protected String dataTypes[] = null;
    protected String SQLCommand[] = null;
    protected String SQLCommandRoot = "";
    public TableEditFrame(String tableName, DatabaseUtilities dbUtils, Users users){
        setSize(600,400);
        setLocation(10,10);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        getContentPane().setLayout(new BorderLayout());
        this.tableName=tableName;
        this.dbUtils=dbUtils;
        SQLCommandRoot = "INSERT INTO "+tableName+" VALUES ";
        setTitle(SQLCommandRoot);
        init(users);
        setVisible(true);
    }
    // initialise the JInternalFrame
    private void init(Users users){
        colNames = dbUtils.getColumnNames(tableName,users);
        dataTypes = dbUtils.getDataTypes(tableName,users);
        table = createTable(colNames,15);
        TableChangeListener modelListener = new TableChangeListener();
        table.getModel().addTableModelListener(modelListener);
        JScrollPane sqlScroller = new JScrollPane(SQLPane);
        JScrollPane tableScroller = new JScrollPane(table);
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                sqlScroller,tableScroller);
        splitter.setDividerLocation(100);
        getContentPane().add(splitter,BorderLayout.CENTER);
        getContentPane().add(insertButton,BorderLayout.SOUTH);
        insertButton.addActionListener(e -> dbUtils.execute(SQLCommand,users));
    }
    protected JTable createTable(String[] colNames,int nRows){
        String[][] rowData = new String[nRows][colNames.length];
        for(int i=0;i<nRows;i++){
            for(int j=0;j<colNames.length;j++)rowData[i][j]="";
        }
        JTable table = new JTable(rowData,colNames);
        return table;
    }
    public Vector parseTable(){
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
    class TableChangeListener implements TableModelListener{
        public TableChangeListener(){
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
