import java.util.ArrayList;
import java.io.*;
import javax.sound.midi.*;
import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.util.List;
import java.util.Arrays;
import java.util.Vector;

public class MidiExporter {
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
	public MidiExporter(ArrayList<String> midiFilePaths, String outputDirectory) throws InvalidMidiDataException, IOException, MidiUnavailableException{
		for(String midi : midiFilePaths){
			String zoom = crop_file_name(midi);
			new File(outputDirectory+"\\MidiLites").mkdir();
			MidiDecompiler data = new MidiDecompiler(midi);
			data.Export(outputDirectory+"\\MidiLites\\"+zoom+".lites");
		}
		JOptionPane.showMessageDialog(null, "Conversion done!!");
	}
}