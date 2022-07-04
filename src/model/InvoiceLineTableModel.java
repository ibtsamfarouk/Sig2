
package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class InvoiceLineTableModel extends AbstractTableModel {

    private ArrayList<InvoiceLine> data;
    private String[] cols = {"InvoiceID","Item Name", " Price", "Count","Total"};

    public InvoiceLineTableModel(ArrayList<InvoiceLine> data) {
        this.data = data;
    }

    public ArrayList<InvoiceLine> getData() {
        return data;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }
    
    public void addData(InvoiceLine newLine){
       data.add(newLine);
         fireTableDataChanged();
       //fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    
    
    
     public void deleteLine(int rowIndex) {
        if (this.data.remove(this.data.get(rowIndex))) {
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine line = data.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return line.getHeader().getId();
            case 1:
                return line.getItemName();
            case 2:
                return line.getUnitPrice();
            case 3:
                return line.getCount();
            case 4:
                return line.getLineTotal();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return cols[column];
    }
    
    
}
