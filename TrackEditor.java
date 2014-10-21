import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.lang.*;
import javax.sound.midi.*;

public class TrackEditor extends JPanel {
  
  public static TrackListing model;
  public static JTable table;
  
  public TrackEditor(SongData song) {
    model = new TrackListing(song);
    table = new JTable(model);
    model.setRowCount(0);
    model.addColumn("Tracks");
    model.addColumn("Instruments");
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setDragEnabled(true);
    table.setDropMode(DropMode.USE_SELECTION);
    
    table.setTransferHandler(new TransferHandler(){
      public int originalRow, originalCol;
       public int getSourceActions(JComponent c) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }
    
        public Transferable createTransferable(JComponent comp)
        {
            JTable table=(JTable)comp;
            int row=table.getSelectedRow();
            int col=table.getSelectedColumn();
            originalRow = row;
            originalCol = col;
            String value = (String)table.getModel().getValueAt(row,col);
            StringSelection transferable = new StringSelection(value);
            //table.getModel().setValueAt(null,row,col);
            return transferable;
        }
        public boolean canImport(TransferHandler.TransferSupport info){
            if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)){
                return false;
            }
    
            return true;
        }
    
        public boolean importData(TransferSupport support) {
    
            if (!support.isDrop()) {
                return false;
            }
    
            if (!canImport(support)) {
                return false;
            }
    
            JTable table=(JTable)support.getComponent();
            TrackListing tableModel=(TrackListing)table.getModel();
              
           JTable.DropLocation dl = (JTable.DropLocation)support.getDropLocation();
    
            int row = dl.getRow();
            int col=dl.getColumn();
            Object swapData = tableModel.getValueAt(row,col);
    
            String data;
            try {
                data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (IOException e) {
                return false;
            }
            
            if(col == originalCol && col == 1) {
              tableModel.setValueAt(data, row, col);
              tableModel.setValueAt(swapData, originalRow, originalCol);
              tableModel.reorderEvent(originalRow, row);
              return true;
            }
            System.out.println("Couldn't SWAP " + col + ", " + row + " | " + originalCol + ", " + originalRow);
            return false;
        }
    
    });
    table.setPreferredSize(new Dimension(950, 300));
    this.add(table);
    
    int i = 0;
		for(String instrument : song.trackNames){
		  model.addRow(new Object[]{Integer.toString(i++), instrument});
		}
    // while(i<16) {
      // model.addRow(new Object[]{Integer.toString(i++), ""});
      // decompiler.Song.add(new Container());
    // }
    
  }
  
  public static SongData getEdit() {
    return model.getEdit();
  }
  
}