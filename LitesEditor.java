import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.midi.*;
import java.awt.event.*;

public class LitesEditor {
  
  public LitesEditor (String filePath) throws InvalidMidiDataException, MidiUnavailableException, IOException {
    JFrame frame = new JFrame(filePath);
    frame.setSize(400,400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
    JPanel mainPane = new JPanel();
    mainPane.setLayout(new GridLayout(3,1));
		
    JMenuBar menuBar = new JMenuBar();
    
		JButton runLites = new JButton(new ImageIcon("play-circle.png"));
		menuBar.add(runLites);
    
    mainPane.add(menuBar);
    
    LitesEmulator emulator = new LitesEmulator(new MidiDecompiler(filePath));
    mainPane.add(emulator);
    
    TrackEditor editor = new TrackEditor();
    mainPane.add(editor);
    
    frame.add(mainPane);
    
    frame.setVisible(true);
    
    runLites.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){

      }
    });
    
  }
  
}