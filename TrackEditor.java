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
  public int splitTrackId;
  public TrackEditor(SongData song) {
    model = new TrackListing(song);
    table = new JTable(model);
    model.setRowCount(0);
    model.addColumn("Tracks");
    model.addColumn("Instruments");
    model.addColumn("PitchStart");
    model.addColumn("PitchEnd");
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setDragEnabled(true);
    table.setDropMode(DropMode.USE_SELECTION);
    
    final JPopupMenu pm = new JPopupMenu();
    JButton deleteTrack = new JButton("Delete Track.");
    JButton splitTrack = new JButton("Split Track.");
    pm.add(splitTrack);
    pm.add(deleteTrack);

    splitTrack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        if(model.getRowCount() < 16 ) {
        System.out.println("Splitting" + model.getValueAt(splitTrackId, 1));
        int minPitch, maxPitch, splitMinPitch, splitMaxPitch;
        minPitch = Integer.parseInt(model.getValueAt(splitTrackId, 2).toString());
        maxPitch = Integer.parseInt(model.getValueAt(splitTrackId, 3).toString());
          System.out.println(minPitch + " " + maxPitch);
          if(!(minPitch == maxPitch)) {
          
          if((maxPitch - minPitch) == 1) {
            splitMinPitch = maxPitch;
            splitMaxPitch = maxPitch;
            maxPitch = minPitch;
            System.out.println("First: " + minPitch + " " + maxPitch);
            System.out.println("First: " + splitMinPitch + " " + splitMaxPitch);
          }
          else {
            int midPitch = minPitch + (maxPitch-minPitch)/2;
            System.out.println("Mid: " + midPitch);
            splitMaxPitch = maxPitch;
            maxPitch = midPitch;
            splitMinPitch = midPitch + 1;
            System.out.println("First: " + minPitch + " " + maxPitch);
            System.out.println("First: " + splitMinPitch + " " + splitMaxPitch);
          }
          
          model.setValueAt(maxPitch, splitTrackId, 3);
          
          model.addRow(new Object[]{model.getRowCount(), model.getValueAt(splitTrackId, 1), splitMinPitch, splitMaxPitch});
          model.splitEvent(model.getValueAt(splitTrackId,1).toString(), splitTrackId, (short)minPitch, (short)maxPitch, (short)splitMinPitch, (short)splitMaxPitch);
          }
          else {
            JOptionPane.showMessageDialog(null, "Can't Split!!");
          }
        }
        else {
          JOptionPane.showMessageDialog(null, "Too Many Tracks!!!");
        }
        }

    });
    
    deleteTrack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        model.removeRow(splitTrackId);
        model.removeEvent(splitTrackId);
      }
    });

    table.addMouseListener(new MouseAdapter() {
    
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.isPopupTrigger()) {
                highlightRow(e);
                doPopup(e);
            }
        }
    
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                highlightRow(e);
                doPopup(e);
            }
        }
        
       protected void doPopup(MouseEvent e) {
            pm.show(e.getComponent(), e.getX(), e.getY());
        }

        protected void highlightRow(MouseEvent e) {
            JTable table = (JTable) e.getSource();
            Point point = e.getPoint();
            int row = table.rowAtPoint(point);
            int col = table.columnAtPoint(point);
            splitTrackId = row;
            table.setRowSelectionInterval(row, row);
            table.setColumnSelectionInterval(0, 3);
        }
    
    });
    
    
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
            Object swapData1 = tableModel.getValueAt(row, col+1);
            Object swapData2 = tableModel.getValueAt(row, col+2);
            
            Object originalData = tableModel.getValueAt(originalRow,originalCol);
            Object originalData1 = tableModel.getValueAt(originalRow,originalCol+1);
            Object originalData2 = tableModel.getValueAt(originalRow,originalCol+2);
            
            String data;
            try {
                data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (IOException e) {
                return false;
            }
            
            if(col == originalCol && col == 1) {
              tableModel.setValueAt(originalData, row, col);
              tableModel.setValueAt(swapData, originalRow, originalCol);
              
              tableModel.setValueAt(originalData1, row, col+1);
              tableModel.setValueAt(swapData1, originalRow, originalCol+1);
              
              tableModel.setValueAt(originalData2, row, col+2);
              tableModel.setValueAt(swapData2, originalRow, originalCol+2);
              
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
		  model.addRow(new Object[]{Integer.toString(i++), instrument,0,11});
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