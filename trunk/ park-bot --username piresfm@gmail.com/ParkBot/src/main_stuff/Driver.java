package main_stuff;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

import framework.TestbedTest;
import main_stuff.CarTest;

public abstract class Driver implements Runnable{

	protected CarTest t;
	protected boolean timerRanOut = false;

	public Driver(TestbedTest t) {
		this.t = (CarTest) t;
	}

	protected void accelerate(){
		t.keyPressed('u', KeyEvent.VK_UP);
	}

	protected void release_acceleration(){
		t.keyReleased('u', KeyEvent.VK_UP);
	}

	protected void reverse(){
		t.keyPressed('d', KeyEvent.VK_DOWN);
	}

	protected void release_reverse(){
		t.keyReleased('d', KeyEvent.VK_DOWN);
	}

	protected void brake(){
		t.keyPressed('o', KeyEvent.VK_O);
	}

	protected void release_brake(){
		t.keyReleased('o', KeyEvent.VK_O);
	}

	protected void turn_left(){
		t.keyPressed('l', KeyEvent.VK_LEFT);
	}

	protected void turn_right(){
		t.keyPressed('r', KeyEvent.VK_RIGHT);
	}

	protected void reset_steering(){
		t.keyReleased('r', KeyEvent.VK_RIGHT);
	}

	protected void actuate(double[] calculate) {
		double average = calculateAverage(calculate);

		if(calculate[0] > average){
			accelerate();
		}
		if(calculate[1] > average){
			release_acceleration();
		}
		if(calculate[2] > average){
			reverse();
		}
		if(calculate[3] > average){
			release_reverse();
		}

		if(calculate[4] > average){
			brake();
		}

		if(calculate[5] > average){
			release_brake();		
		}
		if(calculate[6] > average){
			turn_left();
		}
		if(calculate[7] > average){
			turn_right();
		}

		if(calculate[8] > average){
			reset_steering();
		}
	}

	protected double calculateAverage(double[] calculate) {
		double temp = 0.0;
		for (int i = 0; i < calculate.length; i++)
			temp += calculate[i];
		return temp/calculate.length;
	}	

	public synchronized void run(){
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void discard() {
		timerRanOut = true;
	}

	public void loadMultipleFiles(LinkedList<String> filesToLoad) {
		// TODO Auto-generated method stub
		
	}

	
}