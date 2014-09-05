import java.util.ArrayList;

public class Channel{
	public int value;
	public boolean VALID;
	private ArrayList<Note> notes_playing = new ArrayList<Note>();
	public Channel(int _chan){
		value = _chan;
		VALID = true;
	}
	public Channel(){
		VALID = false;
	}
	public void Start(int input_key, long timestamp){
		for(Note check:notes_playing){
			if(check.key==input_key){
				if(check.off==-1){ // note already being played
					// assume this is a drum beat
					check.sustain = false;
					break;
				}
			}
		}
		Note note = new Note(input_key, timestamp);
		notes_playing.add(note);
	}
	public void Stop(int key, long timestamp){
		int i = 0;
		for(Note check:notes_playing){
			if(check.key==key){
				if(check.sustain)continue;
				if(check.off==-1)
				{
					notes_playing.get(i).off = timestamp;
					return;
				}
			}
			i++;
		}
	}
	public String toString(){
		String running = "Channel "+value+": ";
		int i=0;
		for(Note note:notes_playing){
			running+=note.toString()+"\n";
		}
		return running;
	}
}