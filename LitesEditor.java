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
    
    MidiDecompiler decompiler = new MidiDecompiler(filePath);
    
    LitesEmulator emulator = new LitesEmulator(decompiler);
    mainPane.add(emulator);
    
    TrackEditor editor = new TrackEditor(decompiler);
    mainPane.add(editor);
    
    frame.setJMenuBar(menuBar);
    frame.add(mainPane);
    
    frame.setVisible(true);
    
    runLites.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        MidiDecompiler edit = TrackEditor.getEdit();
      }
    });
    
    saveLites.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
    		JFileChooser fileChooser = new JFileChooser();
    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    		int result = fileChooser.showOpenDialog(frame);
    		if (result == JFileChooser.APPROVE_OPTION) {
    		  
    			File output = fileChooser.getSelectedFile();
          String outputDirectory = output.getAbsolutePath();
          MidiDecompiler data = TrackEditor.getEdit();

    			String zoom = crop_file_name(data.origin);
    			new File(outputDirectory+"\\MidiLites").mkdir();
          
          try {
      			data.Export(outputDirectory+"\\MidiLites\\"+zoom+".lites");
          }catch (IOException ex) {
            System.out.println(ex);
          }
          JOptionPane.showMessageDialog(null, "Conversion done!!");
    		}
    		else
    		{
              JOptionPane.showMessageDialog(null, "Cannot export to there!");
    		}
    		frame.dispose();
      }
    });
    
  }
  
	private String crop_file_name(String path){
		int endLoc=-1,startLoc=-1;
		for(int i=path.length()-1;i>0;i--)
		{
			if(path.charAt(i)=='.')
			{
				endLoc = i;
			}
			else if(path.charAt(i)=='\\')
			{
				startLoc = i+1;
				break;
			}
		}
		return path.substring(startLoc,endLoc);
	}
  
}