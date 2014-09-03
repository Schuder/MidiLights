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
				if(check.off==-1)return;
					// note already being played
			}
		}
		Note note = new Note(input_key, timestamp);
		notes_playing.add(note);
	}
	public void Stop(int key, long timestamp){
		int i = 0;
		for(Note check:notes_playing){
			if(check.key==key){
				notes_playing.get(i++).off = timestamp;
				return;
			}
		}
	}
	public String toString(){
		String running = "";
		int i=0;
		for(Note note:notes_playing){
			System.out.println("channel doing "+(i++));
			running+=note.toString()+"\n";
		}
		return running;
	}
}