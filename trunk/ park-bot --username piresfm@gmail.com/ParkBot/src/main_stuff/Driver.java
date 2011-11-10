package main_stuff;

import java.awt.event.KeyEvent;
import java.util.Random;

import brain.Brain;
import brain.FeedForward;

import framework.TestbedTest;
import main_stuff.CarTest;

public class Driver extends Thread{

	private CarTest t;
	private int sleep;

	public Driver(TestbedTest t, int hp, int sleep) {
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
		super.start();
		Brain b = new Brain();
		int numberOfSensors = t.returnSensorStatus().length;
		b.createStarterNetworks(10, 9);
		for(FeedForward ff: b.getNeuralNetworks()){
			while (!t.isColliding()){
				actuate(ff.calculate(t.returnSensorStatus()));
			}
			t.reset();
		}
		System.out.println("no more neural networks to test");

	}

	private void actuate(double[] calculate) {
		if(calculate[0] > 0.5)
			accelerate();
		if(calculate[1] > 0.5)
			release_acceleration();
		if(calculate[2] > 0.5)
			reverse();
		if(calculate[3] > 0.5)
			release_reverse();
		if(calculate[4] > 0.5)
			brake();
		if(calculate[5] > 0.5)
			release_brake();						
		if(calculate[6] > 0.5)
			turn_left();
		if(calculate[7] > 0.5)
			turn_right();
		if(calculate[8] > 0.5)
			reset_steering();
	}	

}
