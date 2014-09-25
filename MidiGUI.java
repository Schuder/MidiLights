import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import java.util.*;
import java.lang.*;
public class MidiGUI {

	public static void main(String _args[]) throws IOException {
		
		JFrame frame = new JFrame("FrameDemo");
		
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		
		menuBar = new JMenuBar();
		menu = new JMenu("A Menu");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
		
		menu.add(menuItem);
		
		menuBar.add(menu);
		
		frame.setJMenuBar(menuBar);

		frame.setVisible(true);
		
		openFileExplorer(frame);
		
	}
	
	public static void openFileExplorer(JFrame frame) {
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(frame);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		}
	}
}
