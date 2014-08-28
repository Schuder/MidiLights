import java.io.*;
import javax.sound.midi.*;

public class MidiLights {

	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	public static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	
	public static void main(String _args[]) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		
		Sequence sequence = MidiSystem.getSequence(new File("journeyMIDI.mid"));
		
		//Get all tracks/instruments in MIDI
		Track tracks[] = sequence.getTracks();
			
		Track track = tracks[2];
		//for(track : Tracks) {
		
			//Loop through every MIDIEvent in track
			for(int i = 0; i < track.size(); i++) {
			
				MidiEvent event = track.get(i);
				System.out.println(event.getTick() + " ");
				MidiMessage message = event.getMessage();
				
				//Short Messages are what notes are, plus some extra effect stuff.
				if(message instanceof ShortMessage) {
				
					ShortMessage sm = (ShortMessage) message;
					
					
					if(sm.getCommand() == NOTE_ON) {
						int key = sm.getData1();
						int octave = (key/12) - 1;
						int note = key % 12;
						String noteName = NOTES[note];
						int velocity = sm.getData2();
						System.out.print("Channel: " + sm.getChannel() + " "+"Note ON:  " + noteName + octave + " key: " + key + " velocity: " + velocity);
						
					}
					else if(sm.getCommand() == NOTE_OFF) {
						int key = sm.getData1();
						int octave = (key/12) - 1;
						int note = key % 12;
						String noteName = NOTES[note];
						int velocity = sm.getData2();
						System.out.print("Channel: " + sm.getChannel() + " "+"Note OFF:  " + noteName + octave + " key: " + key + " velocity: " + velocity);						
					}
					else {
						//System.out.print("Command: " + sm.getCommand());
					}	
				
				}
				else {
					//System.out.print("WTF is that " + message.getClass());
				}
			
			}
		
		//}
		
		
		// Create a sequencer for the sequence
		Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.open();
		sequencer.setSequence(sequence);

		// Start playing
		//sequencer.start();
	}
}
