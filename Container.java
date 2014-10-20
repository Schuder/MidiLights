import java.util.Map;
import java.util.HashMap;
import java.awt.*;
import javax.swing.*;

public class Container{
	public Map<Integer, Channel> channels = new HashMap<Integer, Channel>();
	public String instrument;
	public Channel get(int input){
		Integer chan_key = new Integer(input);
		if(channels.containsKey(chan_key)){
			return channels.get(chan_key);
		}else{
			Channel result = new Channel(input);
			channels.put(chan_key, result);
			return result;
		}
	}
	public String toString(){
		String running = "";
		int i= 0;
		System.out.println(channels);
		for(Integer key : channels.keySet()){
			// System.out.println("doing "+(i++));
			running+=channels.get(key).toString()+"\n\n------\n\n";
		}
		return running;
	}
	public void draw(Graphics g, int x, int y, int w, int h){
		
		g.fillRect(x, y, w, h);
	}
}