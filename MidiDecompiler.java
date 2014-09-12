import java.io.*;
import javax.sound.midi.*;
import java.util.*;
import java.lang.*;
public class MidiDecompiler {
	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 224;
	public static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public static void main(String _args[]) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		String fileName = _args[0];
		Sequence sequence = MidiSystem.getSequence(new File(fileName));
		// write all midi data to file for fast scanning
		//System.out.println(60000/(24*126.4));
		//Get all tracks/instruments in MIDI
		System.out.println("PPQ: " + sequence.PPQ);
		System.out.println("RESOLUTION: " + sequence.getResolution());
		int resolution = sequence.getResolution();
		float secondsPerTick = -1;
		boolean tempoFlag = false;
		Track tracks[] = sequence.getTracks();		
		int iterator = 0;	
		ArrayList<Container> Song = new ArrayList<Container>();
		for(Track track : tracks) {
			Container cur_track = new Container();
			//Loop through every MIDIEvent in track
			for(int i = 0; i < track.size(); i++) {
				MidiEvent event = track.get(i);
				long timestamp = event.getTick();
				MidiMessage message = event.getMessage();
				//Short Messages are what notes are, plus some extra effect stuff.
				if(message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;
					int cmd = sm.getCommand();
					int channel = sm.getChannel();
					if(cmd == NOTE_ON) {
						int key = sm.getData1();
						int octave = (key/12) - 1;
						int note = key % 12;
						String noteName = NOTES[note];
						int velocity = sm.getData2();
			// output in main output line above current key values
			// then in class functions output also to make sure correct
						if(velocity>10){ // note is turned on
							// System.out.println("start:"+channel+","+key+";"+timestamp);
							cur_track.get(channel).Start(key, timestamp);
						}
						else{ // 10 is arbitrary number within audible level
							 // anything less is too quite to hear
							// so we just assume note is turned off
							// System.out.println("stop:"+channel+","+key+";"+timestamp);
							cur_track.get(channel).Stop(key, timestamp);
						}
					}
					else if(cmd == NOTE_OFF) {
						int key = sm.getData1();
						int octave = (key/12) - 1;
						int note = key % 12;
						String noteName = NOTES[note];
						int velocity = sm.getData2();
						// System.out.println("noteOFF:"+channel+","+key+";"+timestamp);
						cur_track.get(channel).Stop(key, timestamp);
					}
				
				}
				else if(message instanceof MetaMessage){
					MetaMessage mm = (MetaMessage) message;
					
					int type = mm.getType();
					byte data[] = mm.getData();
					
					String output = Arrays.toString(data);
					
					if(type == 3) {
					
						String str = "";
						for(byte b : data) {
						
							str+=(char)b;
						
						}
						
						output = str;
						
					
					}
					if(type == 81 && !tempoFlag) {
					
						Formatter formatter = new Formatter();
						for (byte b : data) {
							formatter.format("%02x", b);
						}
						String hex = formatter.toString();
						System.out.println("HEX: " + hex);
						int tempo = Integer.parseInt(hex, 16);
						System.out.println("TEMPO: " + tempo);
						
						float bpm = 60000000 / (float)tempo;
						System.out.println("BPM: " + bpm);
						
						float ppq = resolution;
						
						float mspt = 60000 / (bpm*ppq);
						System.out.println("MS PER TICK: " + mspt);
						System.out.println("TICKS PER S: " + (1000/mspt));
						secondsPerTick = mspt/1000;
						tempoFlag = true;
					
					}
					
					
					System.out.println(mm.getType()+" "+output);
				}
			}
			// System.out.println(cur_track.toString());
			Song.add(cur_track);
			// new Scanner(System.in).next();
		}
		
		
		// kerry write here
		MidiConverter convertDatShit = new MidiConverter(Song, fileName, secondsPerTick);
		// write data to file
		// return Song;
	}
}
