/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.InvoiceHeader;
import model.InvoiceHeaderTableModel;
import model.InvoiceLine;
import model.InvoiceLineTableModel;
import view.AddInvoiceFrame;
import view.AddLineFrame;
import view.Frame;


public class Controller implements ActionListener, ListSelectionListener {

    private Frame frame;
   // private AddLineFrame addFrame;
      AddLineFrame addLinePopupFrame =new AddLineFrame();
      AddInvoiceFrame addInvoicePopupFrame =new AddInvoiceFrame();
boolean dataLoaded=false;
    
     
    public Controller(Frame frame) {
        this.frame = frame;
    }

    
    
    

    public Controller() {
       
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Load Files":
                 System.out.println("Action called!\n Load file. ");
                loadFiles();
                break;
            case "Save Files":
                if (dataLoaded==true)
                { System.out.println("Action called!\n Save file. ");
                  saveFiles();}
                else 
                JOptionPane.showMessageDialog(frame, "Load data first first.");
                
                break;
                
            case "Create New Invoice":
                 if (dataLoaded==true)
                 {
                      System.out.println("Action called!\n Create New Invoice Header. ");
                       System.out.println("Action called!\n Create New Invoice Header. ");

                callNewAddInvoicePopup();
                 }
                 else
                      JOptionPane.showMessageDialog(frame, "Load data first first.");
                break;
            case "Delete Invoice":
                 if (dataLoaded==true)
                 {if (frame.getInvHeaderTable().getSelectedRow()<0)
                     JOptionPane.showMessageDialog(frame, "Select the invoice row that you want to delete it.");
                
                else if( frame.getInvHeaderTable().getSelectedRow()>=0)
                {
                     System.out.println("Action called!\n Delete invoice.");
                    deleteInvoice(frame.getInvHeaderTable().getSelectedRow());
                }
                 }
                  else
                      JOptionPane.showMessageDialog(frame, "Load data first first.");
                break;
            case "Create New Line":
                if (dataLoaded==true)
                    {if (frame.getInvHeaderTable().getSelectedRow()<0)
                     JOptionPane.showMessageDialog(frame, "Select the parent header first.");
                    else if( frame.getInvHeaderTable().getSelectedRow()>=0)
                    {  
                         System.out.println("Action called!\n Create New Line.");
                         callNewAddLinePopup();
                    }
             }
                else
                      JOptionPane.showMessageDialog(frame, "Load data first first.");
                break;
           /* case "Delete Line":
                 if (dataLoaded==true)
                 {if (frame.getInvLineTable().getSelectedRow()<0)
                     JOptionPane.showMessageDialog(frame, "Select the Line row that you want to delete it.");
                
                else if( frame.getInvLineTable().getSelectedRow()>=0)
                {
                     System.out.println("Action called!\n Delete Line ");
                    deleteLine(frame.getInvLineTable().getSelectedRow());
                }
                 }
                  else
                      JOptionPane.showMessageDialog(frame, "Load data first first.");
                break;*/
                
            case "Add" :
                
                 if(addLinePopupFrame.addCounttxt.getText().equals(""))
                 addLinePopupFrame.validationlbl.setText("product count is required");
                 else if(addLinePopupFrame.addItemPricetxt.getText().equals(""))
                 addLinePopupFrame.pricevalidationlbl.setText("product Price is required");
                 else 
                 {
                String name=addLinePopupFrame.addItemNametxt.getText();
                int count =Integer.parseInt(addLinePopupFrame.addCounttxt.getText());
                double price =Integer.parseInt(addLinePopupFrame.addItemPricetxt.getText());
                addLinePopupFrame.dispose();
                InvoiceLine invoiceline =new InvoiceLine(name, price, count,null);
                System.out.println(invoiceline);
                
                addItemline2(invoiceline);
                
                 }
                break;
            case "OKFromAddInvocePopup":
                int Id =1+getLastInvoiceId();
                String date=addInvoicePopupFrame.invoiceDatetxt.getText();
                String customerName= addInvoicePopupFrame.invoiceCustomertxt.getText();
                InvoiceHeader newInvoceHeader=new InvoiceHeader(Id, customerName, date);
                addInvoicePopupFrame.dispose();
                System.out.println(newInvoceHeader);
                addInvoice2(newInvoceHeader);
                break;
                
        }
        
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = frame.getInvHeaderTable().getSelectedRow();
        //System.out.println("selectedRow first is  "+selectedRow);
        if(selectedRow<0)
            selectedRow=0;
        ArrayList<InvoiceLine> lines = frame.getInvoiceHeadersList().get(selectedRow).getLines();
        //frame.getInvHeaderTable().clearSelection();
        frame.getInvLineTable().setModel(new InvoiceLineTableModel(lines));
        
    }

    private void loadFiles() {
       
        
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                String headerStrPath = headerFile.getAbsolutePath();
                Path headerPath = Paths.get(headerStrPath);
                List<String> headerLines = Files.lines(headerPath).collect(Collectors.toList());
                ArrayList<InvoiceHeader> invoiceHeadersList = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] parts = headerLine.split(",");
                    
                    int id = Integer.parseInt(parts[0]);
                    InvoiceHeader invHeader = new InvoiceHeader(id, parts[2], parts[1]);
                    invoiceHeadersList.add(invHeader);
                }
            
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String lineStrPath = fc.getSelectedFile().getAbsolutePath();
                    Path linePath = Paths.get(lineStrPath);
                    List<String> lineLines = Files.lines(linePath).collect(Collectors.toList());
                   
                    for (String lineLine : lineLines) {
                        String[] parts = lineLine.split(",");
                   
                        int invId = Integer.parseInt(parts[0]);
                        double price = Double.parseDouble(parts[2]);
                        int count = Integer.parseInt(parts[3]);
                        InvoiceHeader header = getInvoiceHeaderById(invoiceHeadersList, invId);
                        InvoiceLine invLine = new InvoiceLine(parts[1], price, count, header);
                        header.getLines().add(invLine);
                    }
                    System.out.println("Loaded invoices headers are  \n"+invoiceHeadersList);
                    frame.setInvoiceHeadersList(invoiceHeadersList);
                    
                    dataLoaded=true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private InvoiceHeader getInvoiceHeaderById(ArrayList<InvoiceHeader> invoices, int id) {
        for (InvoiceHeader invoice : invoices) {
            if (invoice.getId() == id) {
                return invoice;
            }
        }
        
        return null;
    }
    
    private void callNewAddInvoicePopup() {
        
         
        addInvoicePopupFrame.setVisible(true);
        addInvoicePopupFrame.pack();
        addInvoicePopupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addInvoicePopupFrame.addInvoiceHeaderbtn.addActionListener(this);

    }

    

    private void saveFiles() {
        
        JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                String headerStrPath = headerFile.getAbsolutePath();
           frame.exportHeadersTocvs(headerStrPath);
           
            result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String linePath = fc.getSelectedFile().getAbsolutePath();  
          ArrayList <InvoiceLine>allLine=new ArrayList<>();
         //  System.out.println(frame.fGetAllLines());
           allLine=frame.fGetAllLines();
          //FileWriter file = null;
          try{
           File file1 = new File(linePath);
        FileWriter fw = new FileWriter(file1);
        BufferedWriter bw = new BufferedWriter(fw);
         bw.write("ID,Product Name,Price,Count");
        bw.newLine();
        for(int i=0;i<allLine.size();i++)
        {
            bw.write(allLine.get(i).getHeader().getId()+","+allLine.get(i).getItemName()+","+allLine.get(i).getUnitPrice()+","+allLine.get(i).getCount());
            bw.newLine();
        }
        
        bw.close();
        fw.close();
          }catch (IOException e) {
        e.printStackTrace();
    }
    }
           
    }}
    private void callNewAddLinePopup() {
        
        addLinePopupFrame.setVisible(true);
        addLinePopupFrame.pack();
        addLinePopupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addLinePopupFrame.addItembtn.addActionListener(this);
     
    }

        public void addItemline2(InvoiceLine line){
     int selectedRow = frame.getInvHeaderTable().getSelectedRow();
     
     frame.adddd(selectedRow,line);
        }
    public void addItemline(InvoiceLine line){
       // frame.addLineList(line);
       int selectedRow = frame.getInvHeaderTable().getSelectedRow();
       /*if(selectedRow==-1)
        selectedRow=0;*/
      // System.out.println("selectedRow in addline is  "+selectedRow);
       
      //  frame.getInvoiceHeadersList().get(selectedRow).addLine(line);
      
          ArrayList<InvoiceLine> cLines= new ArrayList<>();
          cLines=frame.fgetLines(selectedRow);
          cLines.add(line);
          frame.getInvLineTable().setModel(new InvoiceLineTableModel(cLines));
         //ArrayList<InvoiceHeader> invoiceHeadersList= new ArrayList<>();
       // invoiceHeadersList=frame.getInvoiceHeadersList();
        // frame.setInvoiceHeadersList(invoiceHeadersList);
         
    }
    
    public void addInvoice(InvoiceHeader invoiceHeader){
       
         
        ArrayList<InvoiceHeader> invoiceHeadersList= new ArrayList<>();
          invoiceHeadersList=frame.getInvoiceHeadersList();
          invoiceHeadersList.add(invoiceHeader);
          
          frame.setInvoiceHeadersList(invoiceHeadersList);
          //frame.addInvoiceToList(invoiceHeader);
    }
    
    
    public void addInvoice2(InvoiceHeader invoiceHeader){
       
         
        frame.addNewInvoiceHeader(invoiceHeader);
    
    }
    
    public void deleteInvoice(int selectedRow) {
            
          frame.fdeleteInvoiceRow(selectedRow);

    }
    
    /*public void deleteLine(int selectedRow) {
        frame.fdeleteLineRow(selectedRow);
    }*/
    
     public int getLastInvoiceId(){
       ArrayList<InvoiceHeader> invoiceHeadersList= new ArrayList<>();
         invoiceHeadersList=frame.getInvoiceHeadersList();
        // for(int i=0;i<invoiceHeadersList.size();i++)
             int size =invoiceHeadersList.size();
             int lastId=invoiceHeadersList.get(size-1).getId();
             System.out.println("last id is  "+lastId);
           return lastId;
       }

    
     public int getSelectedrow() {
        int selectedRow = frame.getInvHeaderTable().getSelectedRow();
      
        return  selectedRow;
    }

}
