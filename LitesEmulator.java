import java.awt.*;
import javax.swing.*;

public class LitesEmulator extends JPanel {
  
  public LitesEmulator(MidiDecompiler decomp) {
    //this.setBackground(Color.BLACK);
    this.setLayout(new GridLayout(2,8));
    
    //array of panels 16 for lights
    for(int i = 0; i < 16; i++) {
      JPanel pane = new JPanel();
      if(i%2 == 0 && i < 8) {
        pane.setBackground(Color.BLACK);
      }
      else if(i%2 != 0 && i < 8) {
       pane.setBackground(Color.WHITE);
      }
      else if(i%2 == 0 && i > 7) {
        pane.setBackground(Color.WHITE);
      }
      else {
        pane.setBackground(Color.BLACK);
      }
      this.add(pane);
    }
  }
  
}