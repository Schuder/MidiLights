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

class LightHandler extends Thread {
	private Thread t;
	private String threadName;
	private JPanel light;
	private ArrayList<Note> track;
	public boolean kill = false;
	private Color defaultColor;
	private CyclicBarrier gate;

	LightHandler(JPanel target, ArrayList<Note> data, String name, CyclicBarrier syncher){
		light = target;
		track = data;
		threadName = name;
		gate = syncher;
	}
	public void run(){
		try{
			gate.await();
			boolean on = true;
			while(!kill){
				if(on){
					light.setBackground(defaultColor);
					on = false;
				}else{
					light.setBackground(Color.RED);
					on = true;
				}
				Thread.sleep(1000);
			}
		}catch(Exception e){
			System.out.println("Thread " + threadName+ " interrupted.");
		}
		System.out.println("Thread " + threadName+ " exiting.");
	}
	public void start(){
		defaultColor = light.getBackground();
		System.out.println("Starting " + threadName);
		if(t==null){
			t = new Thread(this, threadName);
			t.start();
		}
	}
}

class LightBoard {
	private ArrayList<LightHandler> threads = new ArrayList<LightHandler>();
	private CyclicBarrier gate;
	private boolean valid = true;

	LightBoard(){
		valid = false;
	}
	LightBoard(ArrayList<JPanel> lights, ArrayList<ArrayList<Note> > data){
		gate = new CyclicBarrier(data.size()+1);
		int i=0;
		for(;i<data.size();i++){
			threads.add(new LightHandler(lights.get(i), data.get(i), "LightThread"+i, gate));
		}
		for(;i<16;i++){
			lights.get(i).setBackground(Color.BLACK);
		}
	}
	public void start(){
		if(!valid)return;
		for(LightHandler light : threads){
			light.kill = false;
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
			light.kill = true;
		}
	}
}

public class LitesEmulator extends JPanel {
	private LightBoard board = new LightBoard();
	private JPanelUpdater updater = new JPanelUpdater();

	public LitesEmulator(JPanel parent){
		updater = new JPanelUpdater(parent, "JPanelUpdater");
	}
	public void run(SongData midiData){
		// board.die(); // these is to stop the last emulation
		// updater.kill = true;
		this.setLayout(new GridLayout(2,8));
		ArrayList<JPanel> outputs = new ArrayList<JPanel>();
		for(int i=0;i<16;i++){
			JPanel pane = new JPanel();
			if((i+i/8)%2==0){
				pane.setBackground(Color.GRAY);
			}else{
				pane.setBackground(Color.WHITE);
			}
			this.add(pane);
			outputs.add(pane);
		}
		updater.start();
		board = new LightBoard(outputs, midiData.tracks);
		board.start();
	}
	public void die(){
		board.die();
		updater.kill = true;
	}
}