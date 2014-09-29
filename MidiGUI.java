import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.*;
public class MidiGUI {

  public static final DefaultTableModel model = new DefaultTableModel() {
    @Override
    public boolean isCellEditable(int row, int col) {
      return false;
    }
  };
  public static final JFrame frame = new JFrame("MIDI Lites");
  public static final JTable table = new JTable(model);

	public static void main(String _args[]) throws IOException {
	
		frame.setSize(400,400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem, menuItem2;
		
		menuBar = new JMenuBar();
		
		JButton addMidi = new JButton(new ImageIcon("plus-circle.png"));
		menuBar.add(addMidi);
		
		JButton removeMidi = new JButton(new ImageIcon("minus-circle.png"));
		menuBar.add(removeMidi);
		
		JButton exportMidi = new JButton(new ImageIcon("export.png"));
		menuBar.add(exportMidi);
		
		frame.setJMenuBar(menuBar);
    
    // JButton button = new JButton("button");
    
    // button.addActionListener(new ActionListener() {
    //   public void actionPerformed(ActionEvent e) {
    //       System.out.println("button");
    //   }
    // });
    
    // frame.add(button);
    
    // Create a couple of columns
    model.addColumn("Midi Files");
    
    frame.add(table);

		frame.setVisible(true);
		//openFileExplorer(frame);
		
    addMidi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String midiPath = openFileExplorer(frame);
        if(!midiPath.equals("lul nope")) {
          model.addRow(new Object[]{midiPath});
        }
        else {
          JOptionPane.showMessageDialog(null, "My Goodness, this is so not a MIDI");
        }
      }
    });
    
    removeMidi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int selected[] = table.getSelectedRows();
        System.out.println(Arrays.toString(table.getSelectedRows()));
        for(int r : selected) {
          model.removeRow(r);
        }
      }
    });
    
    exportMidi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

      }
    });
		
	}
	
	public static String openFileExplorer(JFrame frame) {
	
		JFileChooser fileChooser = new JFileChooser();
		
    FileNameExtensionFilter filter = new FileNameExtensionFilter("MIDI", "mid");
    fileChooser.setFileFilter(filter);
		
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(frame);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			if(selectedFile.getAbsolutePath().endsWith(".mid")) {
			  return selectedFile.getAbsolutePath();
			}
		}
		return "lul nope";
	}
	
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_DELETE) {
      System.out.println("delete");
    }
  }
}
