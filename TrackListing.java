import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.lang.*;

public class TrackListing extends DefaultTableModel {
    private SongData data;
	public TrackListing(SongData input){
		data = input;
	}

    @Override
    public boolean isCellEditable(int row, int col) {
      return false;
    }

    public void reorderEvent(int x, int y) {
      ArrayList<Note> list = data.tracks.get(x);
      String name = data.trackNames.get(x);
      data.tracks.set(x, data.tracks.get(y));
      data.tracks.set(y, list);
      data.trackNames.set(x, data.trackNames.get(y));
      data.trackNames.set(y, name);
    }
    
    public void splitEvent(String trackName, int track, short minPitch, short maxPitch, short splitMinPitch, short splitMaxPitch) {
      System.out.println("ffffffffffffffuuuuuuck");
      int splitTrack = data.addTrack();
      data.trackNames.set(splitTrack,trackName);
      
      short pitches[] = new short[(maxPitch-minPitch)+1];
      
      for(short i = minPitch; i <= maxPitch; i++) {
        System.out.println(i-minPitch);
        pitches[i - minPitch] = i;
      }
      
      ArrayList<Note> track1 = data.pitchesFromTrack(pitches, track);
      
      pitches = new short[(splitMaxPitch-splitMinPitch)+1];
      
      for(short i = splitMinPitch; i <= splitMaxPitch; i++) {
        System.out.println(i-splitMinPitch);
        pitches[i - splitMinPitch] = i;
      }
      
      ArrayList<Note> track2 = data.pitchesFromTrack(pitches, track);
      
      data.tracks.set(track, track1);
      data.tracks.set(splitTrack, track2);
      
    }
    
    public void removeEvent(int row) {
      data.tracks.remove(row);
    }

	public SongData getEdit(){
		return data;
	}
}