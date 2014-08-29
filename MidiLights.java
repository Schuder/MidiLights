import java.io.*;
import javax.sound.midi.*;
import java.util.Scanner;

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
				writer.println(event.getTick() + " ");
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
