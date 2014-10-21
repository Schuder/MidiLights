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

	public SongData getEdit(){
		return data;
	}
}