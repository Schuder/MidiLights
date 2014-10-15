import javax.swing.*;
import java.awt.*;

public class LitesEditor {
  
  public static final JFrame frame = new JFrame("SWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGE");
  
  public LitesEditor (String filePath) {
    
    frame.setSize(400,400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
    JPanel mainPane = new JPanel();
    mainPane.setLayout(new GridLayout(3,1));
		
    JMenuBar menuBar = new JMenuBar();
    
		JButton run = new JButton(new ImageIcon("play-circle.png"));
		menuBar.add(run);
    
    mainPane.add(menuBar);
    
    LitesEmulator emulator = new LitesEmulator(new MidiDecompiler(filePath));
    mainPane.add(emulator);
    
    TrackEditor editor = new TrackEditor();
    mainPane.add(editor);
    
    frame.add(mainPane);
    
    frame.setVisible(true);
  }
  
}