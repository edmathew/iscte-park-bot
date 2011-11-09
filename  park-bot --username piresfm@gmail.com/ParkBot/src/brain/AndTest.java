package brain;

import activation.ActivationFunction;
import activation.ActivationSigmoid;
import feed_fwd.FeedforwardLayer;
import feed_fwd.FeedforwardNetwork;
import feed_fwd.train.Train;
import feed_fwd.train.back_propagation.Backpropagation;
import genetic.GeneticAlgorithm;

public class AndTest {
	public static void main(String[] args) {
		
		double[][] AND_INPUT = {
				{0,0},
				{0,1},
				{1,0},
				{1,1}
		};
		double[][] AND_IDEAL = {
				{0},
				{0},
				{0},
				{1}
		};
		
		FeedforwardNetwork ffn = new FeedforwardNetwork();
		FeedforwardLayer input_layer = new FeedforwardLayer(2);
		FeedforwardLayer output_layer = new FeedforwardLayer(1);
		FeedforwardLayer hidden_layer = new FeedforwardLayer(new ActivationSigmoid(), 2);
		ffn.addLayer(input_layer);
		ffn.addLayer(hidden_layer);
		ffn.addLayer(output_layer);
		
		ffn.reset();
		
		Train train = new Backpropagation(ffn, AND_INPUT, AND_IDEAL, 0.7, 0.9);
		int epoch = 1;
		do{
			train.iteration();
			System.out.println("Epoch #"+ epoch + " Error: " + train.getError());
			epoch++;
		}while((epoch < 5000) && (train.getError() > 0.001));
		
		System.out.println("Results: ");
		for (int i = 0; i < AND_IDEAL.length; i++){
			final double actual[] = ffn.computeOutputs(AND_INPUT[i]);
			System.out.println(AND_INPUT[i][0] + "," + AND_INPUT[i][1] + ", actual=" + actual[0] + ",ideal=" + AND_IDEAL[i][0]);
		}
	}
	
}
