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

public class MyTableModel extends DefaultTableModel {
    
    private MidiDecompiler decompiler;
  
    @Override
    public boolean isCellEditable(int row, int col) {
      return false;
    }
    
    public void reorderEvent(int x, int y) {
      
      Container _tempX = decompiler.Song.get(x);
      Container _tempY = decompiler.Song.get(y);
      
      decompiler.Song.set(x, _tempY);
      decompiler.Song.set(y, _tempX);
      System.out.println(x + " to " + y);
    }
    
    public void setDecompiler(MidiDecompiler decomp) {
      this.decompiler = decomp;
    }
    
    public MidiDecompiler getDecompiler () {
      return this.decompiler;
    }
}