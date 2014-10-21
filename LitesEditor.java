import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.midi.*;
import java.awt.event.*;

public class LitesEditor {
  public static JFrame frame;

  public LitesEditor (String filePath) throws InvalidMidiDataException, MidiUnavailableException, IOException {
    frame = new JFrame(filePath);
    frame.setSize(968,625);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		
    JPanel mainPane = new JPanel();
    mainPane.setLayout(new GridLayout(2,1));
		
    JMenuBar menuBar = new JMenuBar();
    
		JButton saveLites = new JButton(new ImageIcon("export.png"));
		menuBar.add(saveLites);
    
		JButton runLites = new JButton(new ImageIcon("play-circle.png"));
		menuBar.add(runLites);
    
    SongData song = MidiDecompiler.cropTracks(MidiDecompiler.decompile(filePath));
    final LitesEmulator emulator = new LitesEmulator(mainPane);
    mainPane.add(emulator);
	emulator.run(song);
    
    TrackEditor editor = new TrackEditor(song);
    mainPane.add(editor);
    
    frame.setJMenuBar(menuBar);
    frame.add(mainPane);
    
    frame.setVisible(true);
    
    runLites.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        SongData edit = TrackEditor.getEdit();
		emulator.run(edit);
      }
    });
    
    saveLites.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			emulator.die();
    		JFileChooser fileChooser = new JFileChooser();
    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    		int result = fileChooser.showOpenDialog(frame);
    		if (result == JFileChooser.APPROVE_OPTION) {
				File output = fileChooser.getSelectedFile();
				String outputDirectory = output.getAbsolutePath();
				SongData data = TrackEditor.getEdit();

				String name = MidiDecompiler.cropFileName(data.origin);
				new File(outputDirectory+"\\MidiLites").mkdir();

				try{
					MidiDecompiler.ExportByTrack(data, outputDirectory+"\\MidiLites\\"+name+".lites");
				}catch(IOException ex){
					System.out.println(ex);
				}
				JOptionPane.showMessageDialog(null, "Conversion done!!");
    		}else{
				JOptionPane.showMessageDialog(null, "Cannot export to there!");
    		}
			frame.dispose();
		}
    });
  }
 
 public static void main (String args[]) throws InvalidMidiDataException, MidiUnavailableException, IOException {
	LitesEditor temp = new LitesEditor(args[0]);
  }
}