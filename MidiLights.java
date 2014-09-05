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
		// PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		
		//Get all tracks/instruments in MIDI
		Track tracks[] = sequence.getTracks();
			
			
		int iterator = 0;
		
		for(Track track : tracks) {
			// writer.println("\n\nChanging to track "+(iterator++)+"\n\n");
			
			Container cur_track = new Container();
			
			//Loop through every MIDIEvent in track
			for(int i = 0; i < track.size(); i++) {
			
				MidiEvent event = track.get(i);
				long timestamp = event.getTick();
				// writer.print(timestamp+"->");
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
						// writer.println("Channel: " + channel + " "+"Note ON:  " + noteName + octave + " key: " + key + " velocity: " + velocity);
						
			// System.out.println("main "+(i));
			
			// output in main output line above current key values
			// then in class functions output also to make sure correct
			
			
						if(velocity>10){ // note is turned on
							System.out.println("start:"+channel+","+key+";"+timestamp);
							cur_track.get(channel).Start(key, timestamp);
						}
						else{ // 10 is arbitrary number within audible level
							 // anything less is too quite to hear
							// so we just assume note is turned off
							System.out.println("stop:"+channel+","+key+";"+timestamp);
							cur_track.get(channel).Stop(key, timestamp);
						}
						
					}
					else if(cmd == NOTE_OFF) {
						int key = sm.getData1();
						int octave = (key/12) - 1;
						int note = key % 12;
						String noteName = NOTES[note];
						int velocity = sm.getData2();
						System.out.println("noteOFF:"+channel+","+key+";"+timestamp);
						cur_track.get(channel).Stop(key, timestamp);
						// writer.println("Channel: " + channel + " "+"Note OFF:  " + noteName + octave + " key: " + key + " velocity: " + velocity);						
					}
					else {
						// System.out.println("loop index = "+i+" Command: " + cmd);
					}
				
				}
				else {
					// writer.println("WTF is that " + message.getClass());
				}
			}
			System.out.println(cur_track.toString());
			new Scanner(System.in).next();
		}
		
		// close file
		// writer.close();
		
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
