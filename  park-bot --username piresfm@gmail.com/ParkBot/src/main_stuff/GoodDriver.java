package main_stuff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import javax.swing.Timer;

import learning_methods.LearningMethod;

import util.InputUniformization;

import brain.Brain;
import brain.FeedForward;

import framework.TestbedTest;

public class GoodDriver extends Driver {

	private Brain brain;
	private LearningMethod lm;
	private double mutationRate;

	public GoodDriver(TestbedTest t) {
		super(t);
	}

	public Brain getBrain() {
		return brain;
	}

	@Override
	protected void actuate(double[] calculate) {
		calculate = uniformize(calculate);
		// System.out.println("I'm going to do the following actions:");
		// int c = 0;
		// for (double d: calculate){
		// System.out.println(c++ + ": " + d);
		// }
		// System.out.println("#####");
		if (calculate[0] == 1) {
			accelerate();
		}
		if (calculate[1] == 1) {
			release_acceleration();
		}
		if (calculate[2] == 1) {
			reverse();
		}
		if (calculate[3] == 1) {
			release_reverse();
		}

		if (calculate[4] == 1) {
			brake();
		}

		if (calculate[5] == 1) {
			release_brake();
		}
		if (calculate[6] == 1) {
			turn_left();
		}
		if (calculate[7] == 1) {
			turn_right();
		}

		if (calculate[8] == 1) {
			reset_steering();
		}
	}

	private double[] uniformize(double[] calculate) {
		double max = 0;
		double min = 99999;
		double[] temp = new double[calculate.length];
		for (double d : calculate) {
			if (d > max)
				max = d;
			if (d < min)
				min = d;
		}
		for (int i = 0; i < temp.length; i++) {
			// System.out.println(i+">"+calculate[i]);
			temp[i] = 1 - ((max - calculate[i]) / (max - min));
			// System.out.println(i+">"+temp[i]);
		}
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] > 0.5)
				temp[i] = 1;
			else
				temp[i] = 0;
		}
		return temp;
	}

	@Override
	public synchronized void run() {

		PerformanceGUI pgui = new PerformanceGUI();
		// super.run();
		// System.out.println("here i am");
		Brain b;

		if (brain == null) {
			b = new Brain();
			brain = b;
			int numberOfSensors = t.getCar().getNumberOfSensors();
			b.createStarterNetworks(numberOfSensors, 9);
		} else
			b = brain;

		InputUniformization iu = new InputUniformization(0,
				CarTest.DEFAULT_PARKING_SENSOR_DISTANCE);

		while (true) {
			pgui.clear();
			try {
				wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int c = 0;
			for (FeedForward ff : b.getNeuralNetworks()) {
				LoadGUI.updateRunningLabel("Iteration: "
						+ b.getCurrent_iteration() + " network: " + (++c)
						+ "...");
				try {
					wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timerRanOut = false;

				Timer timer = new Timer(20000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						timerRanOut = true;
					}
				});

				timer.start();

				double[] output = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };

				while (!t.isColliding() && timerRanOut == false) {
					double[] temp = iu.getUniformValue(t.getCar()
							.getSensorStatusInDouble());
					double[] input = concatentate(temp, output);
					output = ff.calculate(input);
					actuate(output);

				}
				ff.setFitness(t.getScore());
				pgui.addData(ff.getDescriptor() + ": " + t.getScore());

				timer.stop();
				t.reset();
			}
			b.learn(lm, mutationRate);
			String[] reportData = {"Iteration " + (b.getCurrent_iteration()-1), "Best: " + b.getNeuralNetworks().get(0).getFitness(), "Average: " + b.groupFitness(b.getNeuralNetworks())/b.getNeuralNetworks().size()};
			writeReport(LoadGUI.getReportFile(), reportData);
			
		}
	}
	
	public void setLearningMethod(LearningMethod lm, double mutationRate){
		this.lm = lm;
		this.mutationRate = mutationRate;
	}

//	private double[] addFitnessAsInput(double[] temp, double score) {
//		double[] temp2 = new double[temp.length + 1];
//		int c = 0;
//		for (double s: temp){
//			temp2[c++] = s;
//		}
//		temp2[c] = score;
//		return temp2;
//	}

	public void writeReport(String filename, String[] data){
		try {
			FileWriter fw = new FileWriter(filename);
			for (String s : data){
				fw.append(s+"\n");
			}
			fw.append("###");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void saveBrain(String fileName) {
		ObjectOutputStream stream;

		try {
			stream = new ObjectOutputStream(new FileOutputStream(new File(
					fileName)));
			stream.writeObject(brain);
			stream.close();
		} catch (IOException e) {
		}
	}

	public void loadBrain(String fileName) {
		ObjectInputStream stream;

		try {
			stream = new ObjectInputStream(new FileInputStream(new File(
					fileName)));
			brain = (Brain) stream.readObject();
			System.out.println("Load: " + brain);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		}
	}

	public void loadMultipleFiles(LinkedList<String> filesToLoad) {
		Brain finalBrain = null;
		for (String file : filesToLoad) {
			Brain b = Brain.readFromFile(file);
			if (finalBrain != null)
				finalBrain.concat(b.getNeuralNetworks());
			else
				finalBrain = b;
		}
		//now... order
		while (!finalBrain.isOrdered(finalBrain.getNeuralNetworks())){
			finalBrain.order(finalBrain.getNeuralNetworks());
		}
		
		for (FeedForward ff: finalBrain.getNeuralNetworks()){
			System.out.println(ff.getDescriptor() + ">: " + ff.getFitness());
		}


		if (finalBrain != null)
			brain = finalBrain;
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