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
import javax.sound.midi.*;

public class MidiGUI {

  public static final DefaultTableModel model = new DefaultTableModel() {
    @Override
    public boolean isCellEditable(int row, int col) {
      return false;
    }
  };
  public static final JFrame frame = new JFrame("MIDI Lites");
  public static final JTable table = new JTable(model);
  public static ArrayList<String> paths = new ArrayList<String>();

	public static void main(String _args[]) throws IOException {

		frame.setSize(400,400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem, menuItem2;

		menuBar = new JMenuBar();

		JButton addMidi = new JButton(new ImageIcon("plus-circle.png"));
		menuBar.add(addMidi);

		JButton removeMidi = new JButton(new ImageIcon("minus-circle.png"));
		menuBar.add(removeMidi);

		JButton editMidi = new JButton(new ImageIcon("emulator.png"));
		menuBar.add(editMidi);

		JButton exportMidi = new JButton(new ImageIcon("export.png"));
		menuBar.add(exportMidi);

		frame.setJMenuBar(menuBar);

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
			paths.add(path);
			model.addRow(new Object[]{MidiDecompiler.cropFileName(path)});
		  }
        }

      }
    });

    removeMidi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        int selected[] = table.getSelectedRows();

        for(int i = selected.length-1; i>=0; i--) {
			paths.remove(selected[i]);
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
			try{
				new File(output.getAbsolutePath()+"\\MidiLites").mkdir();
			}catch(Exception exception){
				JOptionPane.showMessageDialog(null, "Cannot create MidiLites folder!");
				return;
			}
			for(String midi : paths){
				String name = MidiDecompiler.cropFileName(midi);
				try{
					MidiDecompiler.ExportByTrack(MidiDecompiler.decompile(midi), output.getAbsolutePath()+"\\MidiLites\\"+name+".lites");
				}catch(MidiUnavailableException ex){
					System.out.println(ex);
				}
				catch(InvalidMidiDataException ex){
					System.out.println(ex);
				}
				catch(IOException ex){
					System.out.println(ex);
				}
			}
			JOptionPane.showMessageDialog(null, "Conversion done!!");
		}
		else
		{
          JOptionPane.showMessageDialog(null, "Cannot export to there!");
		}
      }
    });

    editMidi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        try {
          String editDat;
          if(table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "No MIDI selected");
            return;
          }
          editDat = paths.get(table.getSelectedRow());
          LitesEditor editor = new LitesEditor(editDat);
        }
        catch (IOException ex) {
          System.out.println(ex);
          JOptionPane.showMessageDialog(null, "No MIDI selected");
        }
        catch (InvalidMidiDataException ex) {
          System.out.println(ex);
          JOptionPane.showMessageDialog(null, "Error 404 See Google!");
        }
        catch (MidiUnavailableException ex) {
          System.out.println(ex);
          JOptionPane.showMessageDialog(null, "Error 404 See Google!");
        }
      }
    });

	}

	public static String[] openFileExplorer(JFrame frame) {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
    FileNameExtensionFilter filter = new FileNameExtensionFilter("MIDI", "mid");
    fileChooser.setFileFilter(filter);

		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

		String filePaths[] = {};

		int result = fileChooser.showOpenDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFiles[] = fileChooser.getSelectedFiles();
			List<String> validFiles = new ArrayList<String>();

      for(File f : selectedFiles) {
        String path = f.getAbsolutePath();
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
