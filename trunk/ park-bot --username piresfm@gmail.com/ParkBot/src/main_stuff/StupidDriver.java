package main_stuff;

import jb2d_framework.TestbedTest;
import util.InputUniformization;
import activation.ActivationThreshold;
import brain.FeedForward;
import brain.FeedForwardLayer;

import matrix.Matrix;

public class StupidDriver extends Driver{

	public StupidDriver(TestbedTest t) {
		super(t);
	}

	@Override
	protected void actuate(double[] calculate) {
		if(calculate[0] < 0.5){
			accelerate();
			System.out.println("");
		}
		if(calculate[1] < 0.5){
			reverse();
			System.out.println("");
		}
	}

	@Override
	public synchronized void run() {
		super.run();

		double matrix_1[][] = {
				{1,0},
				{0,1}
		};

		double matrix_2[][] = {
				{1,0},
				{0,1}
		};

		FeedForward ff = new FeedForward();
		FeedForwardLayer inputLayer = new FeedForwardLayer(2);
		FeedForwardLayer hiddenLayer = new FeedForwardLayer(2, new ActivationThreshold(0.8));
		hiddenLayer.setMatrix(new Matrix(matrix_1));
		FeedForwardLayer outputLayer = new FeedForwardLayer(2);
		outputLayer.setMatrix(new Matrix(matrix_2));
		ff.addLayer(inputLayer);
		ff.addLayer(hiddenLayer);
		ff.addLayer(outputLayer);
		ff.randomize();

		InputUniformization iu = new InputUniformization(0, CarTest.DEFAULT_PARKING_SENSOR_DISTANCE);

		while(!t.isColliding()){
			double[] sensors = iu.getUniformValue(t.getCar().getFrontAndBackSensorStatusInDouble());
			actuate(sensors);
		}


		System.out.println("done");

	}
}