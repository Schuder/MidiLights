import java.io.*;
import javax.sound.midi.*;
import java.util.*;
import java.lang.*;

public class MidiDecompiler {
	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 224;

	public static SongData decompile(String fileName) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		SongData Song = new SongData(fileName);
		Sequence sequence = MidiSystem.getSequence(new File(fileName));
		int resolution = sequence.getResolution();
		float secondsPerTick = -1;
		boolean tempoFlag = false;
		Track tracks[] = sequence.getTracks();
		for(Track midiTrackData : tracks) {
			int trackIndex = Song.addTrack();
			//Loop through every MIDIEvent in track
			for(int i = 0; i < midiTrackData.size(); i++) {
				MidiEvent event = midiTrackData.get(i);
				long timestamp = event.getTick();
				MidiMessage message = event.getMessage();
				//Short Messages are what notes are, plus some extra effect stuff.
				if(message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;
					int cmd = sm.getCommand();
					short channel = (short)sm.getChannel();
					if(cmd == NOTE_ON) {
						int key = sm.getData1();
						int velocity = sm.getData2();
						if(velocity>10){ // note is turned on
							Song.addNote(channel, trackIndex, new Note(key, timestamp));
						}
						else{ // 10 is arbitrary number within audible level
							 // anything less is too quite to hear
							// so we just assume note is turned off
							Song.endNote(trackIndex, key, timestamp);
						}
					}
					else if(cmd == NOTE_OFF) {
						int key = sm.getData1();
						int velocity = sm.getData2();
						Song.endNote(trackIndex, key, timestamp);
					}
				}
				else if(message instanceof MetaMessage){
					MetaMessage mm = (MetaMessage) message;
					int type = mm.getType();
					byte data[] = mm.getData();
					if(type==3){
						String name = "";
						for(byte b : data){
							name+=(char)b;
						}
					 	Song.trackNames.set(trackIndex, name);
					}
					if(type==81/* &&!tempoFlag */){
						Formatter formatter = new Formatter();
						for (byte b : data) {
							formatter.format("%02x", b);
						}
						String hex = formatter.toString();
						// System.out.println("HEX: " + hex);
						int tempo = Integer.parseInt(hex, 16);
						// System.out.println("TEMPO: " + tempo);

						float bpm = 60000000 / (float)tempo;
						// System.out.println("BPM: " + bpm);

						float ppq = resolution;

						float mspt = 60000 / (bpm*ppq);
						// System.out.println("MS PER TICK: " + mspt);
						// System.out.println("TICKS PER S: " + (1000/mspt));
						System.out.println(MidiDecompiler.cropFileName(fileName)+", "+trackIndex+" Tempo: "+(float)(mspt/1000)+", "+tempoFlag);
						if(!tempoFlag)Song.Tempo = mspt/1000;
						tempoFlag = true;
					}
				}
			}
			if(Song.tracks.get(trackIndex).size()==0){
				Song.trackNames.set(trackIndex, "Empty");
			}
		}
		return Song;
	}
	public static void ExportByTrack(SongData Song, String outputFile) throws IOException {
		PrintWriter file = new PrintWriter(new File(outputFile));
		file.println("0 "+Song.Tempo);
		int i = 0;
		for(ArrayList<Note> track : Song.tracks){
			if(Song.trackNames.get(i++).equals("Empty"))continue;
			String line = "";
			for(Note n : track){
				line += n.on + " " + n.off + " " + n.key + " ";
			}
			file.println(line);
		}
		file.close();
	}
	public static void ExportByChannel(SongData Song, String outputFile) throws IOException {
		PrintWriter file = new PrintWriter(new File(outputFile));
		file.println(Song.Tempo);
		int i = 0;
		for(ArrayList<Note> chan : Song.channels){
			String line = "";
			for(Note n : chan){
				line += n.on + " " + n.off + " " + n.key + " ";
			}
			file.println(line);
		}
		file.close();
	}
	public static String cropFileName(String path){
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
	public static SongData cropTracks(SongData input){
		for(int i=0;i<input.trackNames.size();i++){
			if(input.trackNames.get(i).equals("Empty")){
				input.tracks.remove(i);
				input.trackNames.remove(i--);
			}
		}
		return input;
	}
}