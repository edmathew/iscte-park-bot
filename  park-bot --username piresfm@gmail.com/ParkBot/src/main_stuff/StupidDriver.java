package main_stuff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.Timer;

import org.jbox2d.common.MathUtils;

import activation.ActivationLinear;
import brain.Brain;
import brain.FeedForward;
import brain.FeedForwardLayer;

import feed_fwd.FeedforwardLayer;
import framework.TestbedTest;
import main_stuff.CarTest;

public class StupidDriver extends Thread{

	private CarTest t;
	private int sleep;
	boolean timerRanOut = false;

	public StupidDriver(TestbedTest t, int hp, int sleep) {
		this.t = (CarTest) t;
		this.t.setspeed(hp);
		this.sleep = sleep;
	}

	public void accelerate(){
		t.keyPressed('u', KeyEvent.VK_UP);
	}

	public void release_acceleration(){
		t.keyReleased('u', KeyEvent.VK_UP);
	}

	public void reverse(){
		t.keyPressed('d', KeyEvent.VK_DOWN);
	}

	public void release_reverse(){
		t.keyReleased('d', KeyEvent.VK_DOWN);
	}

	public void brake(){
		t.keyPressed('o', KeyEvent.VK_O);
	}

	public void release_brake(){
		t.keyReleased('o', KeyEvent.VK_O);
	}

	public void turn_left(){
		t.keyPressed('l', KeyEvent.VK_LEFT);
	}

	public void turn_right(){
		t.keyPressed('r', KeyEvent.VK_RIGHT);
	}

	public void reset_steering(){
		t.keyReleased('r', KeyEvent.VK_RIGHT);
	}
	@Override
	public synchronized void start() {
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.start();
		
		FeedForward ff = new FeedForward();
		FeedForwardLayer inputLayer = new FeedForwardLayer(1);
		FeedForwardLayer hiddenLayer = new FeedForwardLayer(1, new ActivationLinear());
		FeedForwardLayer outputLayer = new FeedForwardLayer(1);
		ff.addLayer(inputLayer);
		ff.addLayer(hiddenLayer);
		ff.addLayer(outputLayer);
		
		while(!t.isColliding()){
			
		}
		
		
		System.out.println("no more neural networks to test");

	}

	private void actuate(double[] calculate) {
		double average = calculateAverage(calculate);
		//		System.out.println(calculate[0] + " " + average);

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

		//		if(calculate[4] > average)
		//			brake();

		if(calculate[5] > average){
			release_brake();		
		}
		if(calculate[6] > average){
			turn_left();
		}
		if(calculate[7] > average){
			turn_right();
		}

		//		if(calculate[8] > average)
		//			reset_steering();
	}

	private double calculateAverage(double[] calculate) {
		double temp = 0.0;
		for (int i = 0; i < calculate.length; i++)
			temp += calculate[i];
		return temp/calculate.length;
	}	

}
