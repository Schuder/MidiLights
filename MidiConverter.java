import java.util.*;
import java.io.*;

public class MidiConverter {

	public ArrayList<Container> tracks;

	public MidiConverter(ArrayList<Container> song, String name) throws IOException {
		
		String fileName = name.replace(".mid", ".mid.lites");
		
		int i = 0;
		this.tracks = song;
		PrintWriter file = new PrintWriter(new File(fileName));
		for(Container t : tracks) {
			
			System.out.println("Track " + i++);
			String line = "";
			for(int key : t.channels.keySet()) {

				System.out.println(">>Channel " + key);
				
				ArrayList<Note> notes = t.channels.get(key).notes_playing;
				
				for(Note n : notes) {
				//s+="| On: " + n.on + " Off: " + n.off + " Key: " + n.key;
				line+= n.on + " " + n.off + " " + n.key;
				
				}
				//System.out.println(s);
				
			}
			
			file.println(line);
			
		}
		file.close();
		
		
		
	}
	
	

}