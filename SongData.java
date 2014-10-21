import java.util.ArrayList;

public class SongData {
	public String origin;
	public float Tempo = 0;
	public ArrayList<ArrayList<Note> > channels = new ArrayList<ArrayList<Note> >(16);
	public ArrayList<ArrayList<Note> > tracks = new ArrayList<ArrayList<Note> >();
	public ArrayList<String> trackNames = new ArrayList<String>();
	public ArrayList<ArrayList<Note> > pitchClasses = new ArrayList<ArrayList<Note> >(12);
	public SongData(String input){
		origin = input;
		for(int i=0;i<16;i++)
			channels.add(new ArrayList<Note>());
		for(int i=0;i<12;i++)
			pitchClasses.add(new ArrayList<Note>());
	}
	public int addTrack(){
		trackNames.add("Unnamed");
		tracks.add(new ArrayList<Note>());
		return tracks.size()-1;
	}
	public boolean addNote(short chan, int trackIndex, Note n){
		if(chan>=16)return false;
		if(trackIndex>=tracks.size())return false;
		ArrayList<Note> track = tracks.get(trackIndex);
		for(int i=track.size()-1;i>=0;i--){
			Note check = track.get(i);
			if(check.key==n.key){
				if(check.off==-1) // if last note is not turned off, turn it off in middle
					check.off = (check.on+n.on)/2;
				break;
			}
		}
		channels.get(chan).add(n);
		track.add(n);
		pitchClasses.get(n.key%12).add(n);
		return true;
	}
	public boolean endNote(int track, int key, long time){
		if(track>=tracks.size())return false;
		for(Note check : tracks.get(track)){
			if(check.key==key){
				if(check.off==-1){
					check.off = time;
					return true;
				}
			}
		}
		return false;
	}
	public ArrayList<Note> pitchFromTrack(short pitch, int track){
		ArrayList<Note> run = new ArrayList<Note>();
		if(pitch>=12)return run;
		if(track>=tracks.size())return run;
		for(Note i : tracks.get(track))
		for(Note j : pitchClasses.get(pitch)){
			if(i==j){
				run.add(i);
				break;
			}
		}
		return run;
	}
}
