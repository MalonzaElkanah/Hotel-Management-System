package HotelDataBaseManagement.DataBaseUserInterface;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Malone-Ibra on 5/27/2018.
 * *
 */
class TableBuilderFrame extends JInternalFrame {
    protected int nRows = 15;
    protected int nColumns = 6;
    protected JTable table;
    protected JTextArea SQLPane = new JTextArea();
    protected JButton createButton = new JButton("Create Table");
    protected ActionListener commandListener = null;
    protected String tableName = null;
    protected String SQLCommand = "";
    protected String SQLCommandRoot = "";
    TableBuilderFrame(String tableName){
        setSize(600,400);
        setLocation(10,10);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        getContentPane().setLayout(new BorderLayout());
        this.tableName=tableName;
        SQLCommandRoot = "CREATE TABLE "+tableName;
        setTitle(SQLCommandRoot);
        init();
        setVisible(true);
    }
    // initialise the JInternalFrame
    private void init(){
        table = createTable(nRows);
        TableChangeListener modelListener = new TableChangeListener ();
        table.getModel().addTableModelListener(modelListener);
        JScrollPane sqlScroller = new JScrollPane(SQLPane);
        JScrollPane tableScroller = new JScrollPane(table);
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,sqlScroller,tableScroller);
        splitter.setDividerLocation(100);
        getContentPane().add(splitter,BorderLayout.CENTER);
        getContentPane().add(createButton,BorderLayout.SOUTH);
        createButton.addActionListener(new ButtonListener());
    }
    private JTable createTable(int nRows){
        String[] dataTypes = {"CHAR","VARCHAR","INT","FLOAT","DATE"};
        String[] defNull = {"","NULL","NOT NULL"};
        String[] defUnique = {"","UNIQUE"};
        String[] defPriKey = {"","PRIMARY KEY"};
        String[] colNames = {"Name","DataType","SIZE","NULL","UNIQUE","PRIMARY KEY"};
        String[][] rowData = new String[nRows][colNames.length];
        for(int i=0;i<nRows;i++){
            for(int j=0;j<colNames.length;j++)rowData[i][j]="";
        }
        JComboBox dTypes = new JComboBox(dataTypes);
        JComboBox nullDefs = new JComboBox(defNull);
        JComboBox uniqueDefs = new JComboBox(defUnique);
        JComboBox primaryKDefs = new JComboBox(defPriKey);
        JTable table = new JTable(rowData,colNames);
        table.getColumnModel().getColumn(1).
                setCellEditor(new DefaultCellEditor(dTypes));
        table.getColumnModel().getColumn(3).
                setCellEditor(new DefaultCellEditor(nullDefs));
        table.getColumnModel().getColumn(4).
                setCellEditor(new DefaultCellEditor(uniqueDefs));
        table.getColumnModel().getColumn(5).
                setCellEditor(new DefaultCellEditor(primaryKDefs));
        return table;
    }
    public String parseTable(){
        String tableValues = "";
        int rows = table.getRowCount();
        int cols = table.getColumnCount();
        if(rows>=0&&cols>=0){
            tableValues += "\n( ";
            for(int i=0;i<rows;i++){
                String rowData = "";
                for(int j=0;j<cols;j++){
                    String field = (String)table.getValueAt(i,j);
                    if(field!=null){
                        if(field.length()==0)break;
                        if(j==2)rowData+="(";
                        else if(i>0||j>0)rowData += " ";
                        rowData += field;
                        if(j==2)rowData+=")";
                    }
                }
                if(rowData.length()==0)break;
                tableValues += rowData+",\n";
            }
        }
        if(tableValues.endsWith(",\n")){
            int tvLen = tableValues.length()-2;
            if(tvLen>0)tableValues = tableValues.substring(0,tvLen);
        }
        tableValues += " );";
        return tableValues;
    }
    // CommandListener is set by the MVC Controller module as a call back to
// return the SQL command
    public void setCommandListener(ActionListener commandListener){
        this.commandListener=commandListener;
    }
    // Listener for the CreateButton
    class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            String action = event.getActionCommand();
            if(commandListener!=null){
                ActionEvent evt = new ActionEvent(this,0,SQLCommand);
                commandListener.actionPerformed(evt);
            }
        }
    }
    // Listener for Edit events on the JTable
    class TableChangeListener implements TableModelListener {
        public TableChangeListener (){
        }
        public void tableChanged(TableModelEvent event){
            SQLCommand = SQLCommandRoot+parseTable();
            SQLPane.setText(SQLCommand);
        }
    }
}
