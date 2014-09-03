public class Note{
	public long on;
	public long off;
	public int key;
	private static final String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public Note(int KEY, long ON, long OFF){
		on = ON;
		off = OFF;
		key = KEY;
	}
	public Note(int KEY, long ON){
		on = ON;
		off = -1;
		key = KEY;
	}
	public long length(){
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
	public String toString(){
		return name()+octave()+" starts at "+on+" and ends on "+off;
	}
}