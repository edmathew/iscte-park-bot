package brain;

import java.util.ArrayList;
import java.util.List;

import activation.ActivationSigmoid;
import activation.ActivationStep;
import activation.ActivationThreshold;

public class Brain {
	private static final int NUMBER_OF_STARTER_NETWORKS = 100;
	private List<FeedForward> neuralNetworks = new ArrayList<FeedForward>();
	
	
	public void createStarterNetworks(int inputLength, int numberOfOutputs){
		for (int i = 0; i < NUMBER_OF_STARTER_NETWORKS; i++){
			FeedForward ff = new FeedForward();
			FeedForwardLayer inputLayer = new FeedForwardLayer(inputLength + numberOfOutputs);
			FeedForwardLayer hiddenLayer = new FeedForwardLayer((numberOfOutputs+inputLength)/2);
			FeedForwardLayer outputLayer = new FeedForwardLayer(numberOfOutputs);
			
			ff.addLayer(inputLayer);
			ff.addLayer(hiddenLayer);
			ff.addLayer(outputLayer);
			ff.randomize();
			neuralNetworks.add(ff);
		}
	}
	
	public List<FeedForward> getNeuralNetworks() {
		return neuralNetworks;
	}
}
