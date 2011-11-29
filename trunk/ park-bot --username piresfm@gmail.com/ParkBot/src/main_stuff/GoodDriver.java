package main_stuff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import util.InputUniformization;

import brain.Brain;
import brain.FeedForward;

import framework.TestbedTest;

public class GoodDriver extends Driver{

	public GoodDriver(TestbedTest t) {
		super(t);
	}

	@Override
	protected void actuate(double[] calculate) {
		calculate = uniformize(calculate);
		//		System.out.println("I'm going to do the following actions:");
		//		int c = 0;
		//		for (double d: calculate){
		//			System.out.println(c++ + ": " + d);
		//		}
		//		System.out.println("#####");
		if(calculate[0] == 1){
			accelerate();
		}
		if(calculate[1] == 1){
			release_acceleration();
		}
		if(calculate[2] == 1){
			reverse();
		}
		if(calculate[3] == 1){
			release_reverse();
		}

		if(calculate[4] == 1){
			brake();
		}

		if(calculate[5] == 1){
			release_brake();		
		}
		if(calculate[6] == 1){
			turn_left();
		}
		if(calculate[7] == 1){
			turn_right();
		}

		if(calculate[8] == 1){
			reset_steering();
		}
	}

	private double[] uniformize(double[] calculate) {
		double max = 0;
		double min = 99999;
		double[] temp = new double[calculate.length];
		for (double d: calculate){
			if (d>max)
				max = d;
			if (d < min)
				min = d;
		}
		for (int i = 0; i < temp.length; i++){
			//			System.out.println(i+">"+calculate[i]);
			temp[i] = 1 - ((max - calculate[i])/(max-min));
			//			System.out.println(i+">"+temp[i]);
		}
		for (int i = 0; i < temp.length; i++){
			if (temp[i] > 0.5)
				temp[i] = 1;
			else
				temp[i] = 0;
		}
		return temp;
	}

	@Override
	public synchronized void run() {
		super.run();

		Brain b = new Brain();
		InputUniformization iu = new InputUniformization(0, CarTest.DEFAULT_PARKING_SENSOR_DISTANCE);

		int numberOfSensors = t.getCar().getNumberOfSensors();
		b.createStarterNetworks(numberOfSensors, 9);

		int iteration = 1;
		while(true){
			int c = 0;
			for(FeedForward ff: b.getNeuralNetworks()){
				System.out.println("Iteration: " + iteration + " network: "+(++c) + "...");
				try { wait(1000); } catch (InterruptedException e) { e.printStackTrace(); }
				System.out.println("GO!");
				timerRanOut = false;

				Timer timer = new Timer(20000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						timerRanOut = true;
					}
				});


				timer.start();

				double[] output = {0,0,0,0,0,0,0,0,0};

				while (!t.isColliding() && timerRanOut == false){
					double[] temp = iu.getUniformValue(t.getCar().getSensorStatusInDouble());
					double[] input = concatentate(temp, output); 
					output = ff.calculate(input);
					actuate(output);

				}

				b.record(ff, t.getScore());

				timer.stop();
				t.reset();
			}
			b.learn();
			iteration++;
		}
	}

	private double[] concatentate(double[] input, double[] output) {
		double temp[] = new double[input.length + output.length];
		int i = 0;
		for (int j = 0; j < input.length; j++)
			temp[i++] = input[j];
		for (int j = 0; j < output.length; j++)
			temp[i++] = output[j];

		return temp;
	}	

}