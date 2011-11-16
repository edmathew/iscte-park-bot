package main_stuff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.Timer;

import org.jbox2d.common.MathUtils;

import util.InputUniformization;

import brain.Brain;
import brain.FeedForward;

import framework.TestbedTest;
import main_stuff.CarTest;

public class Driver extends Thread{

	private CarTest t;
	private int sleep;
	boolean timerRanOut = false;

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
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.start();
		Brain b = new Brain();
		int numberOfSensors = t.returnSensorStatus().length;
		b.createStarterNetworks(numberOfSensors, 9);
		int c = 0;
		InputUniformization iu = new InputUniformization(0, 3.1);
		for(FeedForward ff: b.getNeuralNetworks()){
			System.out.println("trying network #"+(++c) + "...");
			try {
				wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("GO!");
			timerRanOut = false;
			Timer timer = new Timer(20000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					timerRanOut = true;
				}
			});
			timer.start();
			while (!t.isColliding() && timerRanOut == false){
				double[] temp = iu.getUniformValue(t.returnSensorStatus());
				for (double d: temp){
					if (d!=0)
						System.out.println(d);
				}
				actuate(ff.calculate(temp));
			}
			timer.stop();
			t.reset();
			CarTest ct = (CarTest)t;
			ct.carReset();
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
