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
import java.util.Vector;
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
		
		JButton emulateMidi = new JButton(new ImageIcon("emulator.png"));
		menuBar.add(emulateMidi);
		
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
        String midiPaths[] = openFileExplorer(frame);
        
        if(midiPaths.length == 0) {
          JOptionPane.showMessageDialog(null, "No MIDIs Selected!");
          return;
        }
        
        for(String path : midiPaths) {
		  if(true){ // check if file aready exists
			model.addRow(new Object[]{path});
		  }
        }
        
      }
    });
    
    removeMidi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
        int selected[] = table.getSelectedRows();
        
        for(int i = selected.length-1; i>=0; i--) {
          model.removeRow(selected[i]);
        }
        
      }
    });
    
    exportMidi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File output = fileChooser.getSelectedFile();
			Object table[] = model.getDataVector().toArray();
			ArrayList<String> paths = new ArrayList<String>();
			for(Object obj : table) {
				paths.add(obj.toString().replace("[","").replace("]",""));
			}
			try{
				MidiExporter exp = new MidiExporter(paths, output.getAbsolutePath());
			}catch(Exception exception){
				JOptionPane.showMessageDialog(null, "Unknown errorrrrrrrrrrrr!");
			}
		}
		else
		{
          JOptionPane.showMessageDialog(null, "Cannot export to there!");
		}
      }
    });
    
    emulateMidi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //LitesEmulator = new LitesEmulator
      }
    });
		
	}
	
	public static String[] openFileExplorer(JFrame frame) {
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
    FileNameExtensionFilter filter = new FileNameExtensionFilter("MIDI", "mid");
    fileChooser.setFileFilter(filter);
		
		System.out.println(System.getProperty("user.dir"));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		String filePaths[] = {};
		
		int result = fileChooser.showOpenDialog(frame);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFiles[] = fileChooser.getSelectedFiles();
			List<String> validFiles = new ArrayList<String>();
			
      for(File f : selectedFiles) {
        String path = f.getAbsolutePath();
        System.out.println(path);
  			if(path.toLowerCase().endsWith(".mid")) {
  			  validFiles.add(path);
  			}
      }
      
      filePaths = new String[validFiles.size()];
      validFiles.toArray(filePaths);
      
		}
		
		return filePaths;
		
	}
	
}
