import java.io.*;
import javax.sound.midi.*;
import java.util.Scanner;

private class Note{
	public int on;
	public int off;
	public int key;
	private static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public Note(int KEY, int ON, int OFF){
		on = ON;
		off = OFF;
		key = KEY;
	}
	public Note(int KEY, int ON){
		on = ON;
		off = -1;
		key = KEY;
	}
	public int length(){
		if(off==-1){
			return -1;
		}
		return off-on;
	}
	public int octave(){
		return (key/12)-1;
	}
	public String name(){
		return NOTES[key%12];
	}
}
/* 
private class Channel{
	public int value;
	private boolean[] notes_playing = new boolean[12];
	public Channel(int _chan){
		value = _chan;
	}
	public void Start(Note n){
		for(Note check:notes_playing){
			if(check.key==n.key){
				// note already being played
				return;
			}
		}
		ArrayList.add(n);
	}
	public void End(Note n){
		int i = 0;
		for(Note check:notes_playing){
			i++;
			if(check.key==n.key){
				notes_playing.remove(i);
				return;
			}
		}
	}
}
 */
 /* 
private class Track{
	
} */

public class MidiLights {

	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 224;
	public static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	
	public static void main(String _args[]) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		
		Sequence sequence = MidiSystem.getSequence(new File("journeyMIDI.mid"));
		// write all midi data to file for fast scanning
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		
		//Get all tracks/instruments in MIDI
		Track tracks[] = sequence.getTracks();
			
			
		int iterator = 0;
		
		for(Track track : tracks) {
			writer.println("\n\nChanging to track "+(iterator++)+"\n\n");
			
			//Loop through every MIDIEvent in track
			for(int i = 0; i < track.size(); i++) {
			
				MidiEvent event = track.get(i);
				int timestamp = event.getTick();
				writer.print(timestamp+"->");
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
						writer.println("Channel: " + channel + " "+"Note ON:  " + noteName + octave + " key: " + key + " velocity: " + velocity);
						
						if(velocity<=10){ // 10 is arbitrary number within audible level
										 // anything less is too quite to hear
										// so we just assume note is turned off
							
						}
						else{
							// note is turned on
							
						}
						
					}
					else if(cmd == NOTE_OFF) {
						int key = sm.getData1();
						int octave = (key/12) - 1;
						int note = key % 12;
						String noteName = NOTES[note];
						int velocity = sm.getData2();
						writer.println("Channel: " + channel + " "+"Note OFF:  " + noteName + octave + " key: " + key + " velocity: " + velocity);						
					}
					else {
						System.out.println("loop index = "+i+" Command: " + cmd);
					}	
				
				}
				else {
					writer.println("WTF is that " + message.getClass());
				}
			}
		}
		
		// close file
		writer.close();
		
		// Create a sequencer for the sequence
		Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.open();
		sequencer.setSequence(sequence);

		// Start playing
		sequencer.start();
		
		new Scanner(System.in).next();
		sequencer.stop();
		sequencer.close();
	}
}
