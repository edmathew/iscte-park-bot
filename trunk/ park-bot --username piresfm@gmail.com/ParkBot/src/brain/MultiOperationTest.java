package brain;

import java.io.IOException;

import util.ReadCSV;
import feed_fwd.FeedforwardLayer;
import feed_fwd.FeedforwardNetwork;
import feed_fwd.train.Train;
import feed_fwd.train.back_propagation.Backpropagation;

public class MultiOperationTest {
	static final int COLUMNS = 6;
	static final int OPP_AND = 0;
	static final int OPP_OR = 1;
	static final int OPP_XOR = 2;
	
	private FeedforwardNetwork network;
	private double[][] input;
	private double[][] ideal;
	
	public void createNetwork(){
		this.network = new FeedforwardNetwork();
		network.addLayer(new FeedforwardLayer(MultiOperationTest.COLUMNS));
		network.addLayer(new FeedforwardLayer(3));
		network.addLayer(new FeedforwardLayer(1));
		network.reset();
	}
	
	public void train(){
		final Train train = new Backpropagation(network, input, ideal, 0.7, 0.8);
		int epoch = 1;
		
		do {
			train.iteration();
			System.out.println("Epoch: " + epoch + " Error: " + train.getError());
			epoch++;
		} while ((epoch < 5000) && (train.getError() > 0.001));
	}
	
	public double evaluate (double op1, double op2, int operation){
		double[] input = new double[MultiOperationTest.COLUMNS];
		input[0] = op1;
		input[1] = op2;
		switch(operation){
		case MultiOperationTest.OPP_AND:
			input[2] = 1;
			break;
		case MultiOperationTest.OPP_OR:
			input[3] = 1;
			break;
		case MultiOperationTest.OPP_XOR:
			input[4] = 1;
			break;
		}
		
		double[] output = this.network.computeOutputs(input);
		return output[0];
	}
	
	public void load(String filename) throws IOException{
		int size = 0;
		
		ReadCSV rcsv = new ReadCSV(filename);
		while (rcsv.next()){
			size++;
		}
		rcsv.close();
		
		this.input = new double[size][MultiOperationTest.COLUMNS];
		this.ideal = new double[size][1];
		
		int index = 0;
		rcsv = new ReadCSV(filename);
		while (rcsv.next()) {
			this.input[index][0] = Double.parseDouble(rcsv.get(0));
			this.input[index][1] = Double.parseDouble(rcsv.get(1));
			this.input[index][2] = Double.parseDouble(rcsv.get(3));
			this.input[index][3] = Double.parseDouble(rcsv.get(4));
			this.input[index][4] = Double.parseDouble(rcsv.get(5));
			this.ideal[index][0] = Double.parseDouble(rcsv.get(2));
			index++;
		}
		
		rcsv.close();
		
	}
	
	public static void main(String[] args) {
		
		try {
			MultiOperationTest mot = new MultiOperationTest();
			mot.load("SavedNetworks/network2.net");
			mot.createNetwork();
			mot.train();
			System.out.println("1 and 1 = " + mot.evaluate(1, 1, MultiOperationTest.OPP_AND));
			System.out.println("1 xor 1 = " + mot.evaluate(1, 1, MultiOperationTest.OPP_XOR));
			System.out.println("1 and 0 = " + mot.evaluate(1, 0, MultiOperationTest.OPP_AND));
			System.out.println("1 or 1 = " + mot.evaluate(1, 1, MultiOperationTest.OPP_OR));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
