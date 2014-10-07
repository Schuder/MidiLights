import java.util.*;
import java.io.*;

public class MidiConverter {

	public ArrayList<Container> tracks;

	public MidiConverter(ArrayList<Container> song, float tempo, String inputFile, String outputFile) throws IOException {
		int i = 0;
		this.tracks = song;
		PrintWriter file = new PrintWriter(new File(outputFile));
		file.println(tempo);
		for(Container t : tracks) {

			// System.out.println("Track " + i++);
			String line = "";


			for(int key : t.channels.keySet()) {

				// System.out.println(">>Channel " + key);

				ArrayList<Note> notes = t.channels.get(key).notes_playing;

				for(Note n : notes) {
				//s+="| On: " + n.on + " Off: " + n.off + " Key: " + n.key;
				if(n.off==-1)n.off = n.on+10;
				line+= n.on + " " + n.off + " " + n.key + " ";

				}
				//System.out.println(s);

			}

			file.println(line);

		}
		file.close();



	}



}