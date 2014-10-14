import javax.swing.*;

public class LitesEditor {
  
  public static final JFrame frame = new JFrame("SWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGE");
  
  public static void main(String _args[]) {
    
    frame.setSize(400,400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
    
    LitesEmulator emulator = new LitesEmulator();
    frame.add(emulator);
    
    TrackEditor editor = new TrackEditor();
    frame.add(editor);
    
  }
  
}