import java.lang.Thread;
import java.util.concurrent.CyclicBarrier;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

class JPanelUpdater extends Thread {
	private Thread t;
	private String threadName;
	private JPanel panel;
	public boolean kill = false;
	private boolean valid = true;

	JPanelUpdater(){
		valid = false;
	}
	JPanelUpdater(JPanel target, String name){
		panel = target;
		threadName = name;
	}
	public void run(){
		if(!valid)return;
		try{
			while(!kill){
				panel.repaint();
			}
		}catch(Exception e){
			System.out.println("Thread " + threadName+ " interrupted.");
		}
		System.out.println("Thread " + threadName+ " exiting.");
	}
	public void start(){
		if(!valid)return;
		System.out.println("Starting " + threadName);
		if(t==null){
			t = new Thread(this, threadName);
			t.start();
		}
	}
}

class LightOff extends Thread {
	private Thread t;
	private String threadName;
	private JPanel light;
	private ArrayList<Note> track;
	public boolean kill = false;
	private Color changeColor;
	private CyclicBarrier gate;
	private float tempo;

	LightOff(JPanel target, Color c, ArrayList<Note> data, float timing, String name, CyclicBarrier syncher){
		changeColor = c;
		light = target;
		track = data;
		threadName = name;
		gate = syncher;
		tempo = timing;
	}
	public void run(){
		try{
			gate.await();
			int i=0;
			long last = 0;
			while(!kill&&i<track.size()){
				light.setBackground(changeColor);
				if(track.get(i).off-last>0)
					Thread.sleep((int)((track.get(i).off-last)*tempo*1000));
				last = track.get(i++).off;
			}
		}catch(Exception e){
			System.out.println("Thread " + threadName+ " interrupted.");
			System.out.println(e);
		}
		System.out.println("Thread " + threadName+ " exiting.");
		light.setBackground(Color.BLACK);
	}
	public void start(){
		System.out.println("Starting " + threadName);
		if(t==null){
			t = new Thread(this, threadName);
			t.start();
		}
	}
	public void die(){
		if(t==null)return;
		t.interrupt();
	}
}
class LightOn extends Thread {
	private Thread t;
	private String threadName;
	private JPanel light;
	private ArrayList<Note> track;
	public boolean kill = false;
	private Color changeColor;
	private CyclicBarrier gate;
	private float tempo;

	LightOn(JPanel target, Color c, ArrayList<Note> data, float timing, String name, CyclicBarrier syncher){
		changeColor = c;
		light = target;
		track = data;
		threadName = name;
		gate = syncher;
		tempo = timing;
	}
	public Color getColorIntensity(Color c, int change){
		if(change==0)return c;
		Color lastColor = new Color(c.getRGB());
		if(change<0)for(int i=0;i>change;i--){
			lastColor = lastColor.darker();
		}
		else for(int i=0;i<change;i++){
			lastColor = lastColor.brighter();
		}
		return lastColor;
	}
	public void run(){
		try{
			gate.await();
			int i=0;
			long last = 0;
			while(!kill&&i<track.size()){
				light.setBackground(getColorIntensity(changeColor, (track.get(i).key%12)-5));
				if(track.get(i).off-last>0)
					Thread.sleep((int)((track.get(i).on-last)*tempo*1000));
				last = track.get(i++).on;
			}
		}catch(Exception e){
			System.out.println("Thread " + threadName+ " interrupted.");
			System.out.println(e);
		}
		System.out.println("Thread " + threadName+ " exiting.");
		light.setBackground(Color.BLACK);
	}
	public void start(){
		System.out.println("Starting " + threadName);
		if(t==null){
			t = new Thread(this, threadName);
			t.start();
		}
	}
	public void die(){
		if(t==null)return;
		t.interrupt();
	}
}
class LightHandler {
	private LightOn ON;
	private LightOff OFF;

	LightHandler(JPanel target, ArrayList<Note> data, float timing, int index, CyclicBarrier syncher){
		ON = new LightOn(target, new Color(128, 0, 0), data, timing, "Light"+index+"on", syncher);
		OFF = new LightOff(target, target.getBackground(), data, timing, "Light"+index+"off", syncher);
	}
	public void start(){
		if(ON==null||OFF==null)return;
		ON.start();
		OFF.start();
	}
	public void die(){
		if(ON==null||OFF==null)return;
		ON.kill = true;
		OFF.kill = true;
		ON.die();
		OFF.die();
	}
}

class LightBoard {
	private ArrayList<LightHandler> threads = new ArrayList<LightHandler>();
	private CyclicBarrier gate;
	private boolean valid = true;
	private float tempo;

	LightBoard(){
		valid = false;
	}
	LightBoard(ArrayList<JPanel> lights, ArrayList<ArrayList<Note> > data, float timing){
		int size = java.lang.Math.min(data.size(), 16);
		gate = new CyclicBarrier(size*2+1);
		int i=0;
		tempo = timing;
		for(;i<size&&i<16;i++){
			if((i+i/8)%2==0){
				lights.get(i).setBackground(Color.GRAY);
			}else{
				lights.get(i).setBackground(Color.WHITE);
			}
			threads.add(new LightHandler(lights.get(i), data.get(i), timing, i, gate));
		}
		for(;i<16;i++){
			lights.get(i).setBackground(Color.BLACK);
		}
	}
	public void start(){
		if(!valid)return;
		for(LightHandler light : threads){
			light.start();
		}
		try{
			gate.await();
		}catch(Exception e){
			System.out.println("gate issue.");
		}
	}
	public void die(){
		if(!valid)return;
		for(LightHandler light : threads){
			light.die();
		}
	}
}

public class LitesEmulator extends JPanel {
	private LightBoard board = new LightBoard();
	private JPanelUpdater updater = new JPanelUpdater();
	private ArrayList<JPanel> outputs;

	public LitesEmulator(JPanel parent){
		updater = new JPanelUpdater(parent, "JPanelUpdater");
		this.setLayout(new GridLayout(2,8));
		outputs = new ArrayList<JPanel>();
		for(int i=0;i<16;i++){
			JPanel pane = new JPanel();
			JLabel id = new JLabel(""+i);
			id.setFont(new Font("Verdana",1,60));
			pane.add(id);
			this.add(pane);
			outputs.add(pane);
		}
	}
	public void run(SongData midiData){
		updater.start();
		board = new LightBoard(outputs, midiData.tracks, midiData.Tempo);
		board.start();
	}
	public void die(){
		board.die();
		updater.kill = true;
	}
}