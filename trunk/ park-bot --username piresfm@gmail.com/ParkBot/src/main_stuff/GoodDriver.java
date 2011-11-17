package main_stuff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import util.InputUniformization;

import brain.Brain;
import brain.FeedForward;

import framework.TestbedTest;

public class GoodDriver extends Driver{

	protected boolean timerRanOut = false;
	
	public GoodDriver(TestbedTest t) {
		super(t);
	}

	@Override
	public synchronized void run() {
		super.run();

		Brain b = new Brain();
		InputUniformization iu = new InputUniformization(0, CarTest.DEFAULT_PARKING_SENSOR_DISTANCE);
		
		int numberOfSensors = t.getCar().getNumberOfSensors();
		b.createStarterNetworks(numberOfSensors, 9);
		
		int c = 0;
		
		for(FeedForward ff: b.getNeuralNetworks()){
			
			System.out.println("trying network #"+(++c) + "...");
			try { wait(1000); } catch (InterruptedException e) { e.printStackTrace(); }
			System.out.println("GO!");
			timerRanOut = false;
			
			Timer timer = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					timerRanOut = true;
				}
			});
			
			timer.start();
			
			while (!t.isColliding() && timerRanOut == false){
				double[] temp = iu.getUniformValue(t.getCar().getSensorStatusInDouble());
				actuate(ff.calculate(temp));
			}
			
			timer.stop();
			t.reset();
		}
		System.out.println("no more neural networks to test");
	}	

}