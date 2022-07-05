
package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DELL
 */
public class InvoiceHeaderTableModel extends AbstractTableModel {
    private ArrayList<InvoiceHeader> data=new ArrayList<>();
    private String[] cols = {"ID", "Customer Name", "Date" ,"Total"};

    public InvoiceHeaderTableModel(ArrayList<InvoiceHeader> data) {
        this.data = data;
    }

    public ArrayList<InvoiceLine> getAllLines() {
           ArrayList <InvoiceLine> allLines=new ArrayList<>();

        for(int i=0;i<data.size();i++) 
          for(int j=0;j<data.get(i).getLines().size();j++)
              allLines.add(data.get(i).getLines().get(j));
     
        return allLines;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }
public void addLinedown (InvoiceLine lineData,int index) {
        this.data.get(index).addLine(lineData);
             //  data.get(index).getLines().add(lineData);
           //    for(int i=0;i<data.size();i++)
                   

       // fireTableRowsInserted(data.size()+1, data.size()+1);       
        fireTableDataChanged();
}



       public void addNewInvoiceHeader (InvoiceHeader invoiceData) {
        data.add(invoiceData);
         fireTableDataChanged();
      }

       
public ArrayList<InvoiceLine> getLines (int index) {
       return data.get(index).getLines();

}




    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader header = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return header.getId();
            case 1:
                return header.getCustomerName();
            case 2:
                return header.getDate();
            case 3:
                return header.getInvoiceTotal();

        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return cols[column];
    }
    
   public void addNewHeader (InvoiceHeader invoiceHeader){
          data.add(invoiceHeader);
        } 

    private void fireTableChanged() {
    }
    
    
    public void deleteInvoice(int rowIndex) {
       /* if (this.data.remove(this.data.get(rowIndex))) {
            fireTableRowsDeleted(rowIndex, rowIndex);
        }*/
        if (data.remove(data.get(rowIndex))) {
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }
   
    public void deleteLine(int linerowIndex,int headerRowIndex) {
        
          System.out.println("line rowIndex  "+linerowIndex);
          System.out.println("header rowIndex  "+headerRowIndex);

          System.out.println("The deleted line is "+data.get(headerRowIndex).getLines().get(linerowIndex));

              
                data.get(headerRowIndex).removeLine(data.get(headerRowIndex).getLines().get(linerowIndex));

               // fireTableDataChanged();

            
           fireTableRowsDeleted(headerRowIndex, headerRowIndex);
        }
    
    
}
