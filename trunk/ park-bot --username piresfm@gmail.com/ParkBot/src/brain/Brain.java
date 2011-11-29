package brain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import support.ScoreItem;

import activation.ActivationSigmoid;
import activation.ActivationStep;
import activation.ActivationThreshold;

public class Brain {
	private static final int NUMBER_OF_STARTER_NETWORKS = 10;
	private static final int NUMBER_OF_CREATIONAL_NETWORKS = 5;
	private List<FeedForward> neuralNetworks = new ArrayList<FeedForward>();
	private ArrayList<ScoreItem> iterationNetworks = new ArrayList<ScoreItem>();


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

	public void record(FeedForward ff, double score) {
		iterationNetworks.add(new ScoreItem(ff, score));
	}

	public void learn() {
		System.out.println("Sorting the networks...");
		//ordenar as redes por score
		while (!isOrdered(iterationNetworks)){
			order(iterationNetworks);
		}
		for (ScoreItem si : iterationNetworks){
			System.out.println(">: " + si.getScore());
		}

		//seleccionar redes de topo
		List<FeedForward> creationalNetworks = selectNetworks(iterationNetworks, NUMBER_OF_CREATIONAL_NETWORKS);

		//criar novas redes com base nas anteriores
		neuralNetworks = new ArrayList<FeedForward>();
		int c = 0;
		while (neuralNetworks.size() < NUMBER_OF_STARTER_NETWORKS){
			neuralNetworks.add(creationalNetworks.get(c).evolve(0.2, 0.1));
			c++;
			if (c == NUMBER_OF_CREATIONAL_NETWORKS)
				c = 0;
		}
		
		iterationNetworks = new ArrayList<ScoreItem>();
	}

	private List<FeedForward> selectNetworks(
			ArrayList<ScoreItem> iterationNetworks,
			int numberOfCreationalNetworks) {
		ArrayList<FeedForward> selectedNetworks = new ArrayList<FeedForward>();
		for (int i = 0; i < numberOfCreationalNetworks; i++)
			selectedNetworks.add(iterationNetworks.get(i).getFf());
			
		return selectedNetworks;
	}

	private boolean isOrdered(ArrayList<ScoreItem> list) {

		for (int i = 1; i < list.size()-1; i++){
			if (list.get(i).getScore() < list.get(i-1).getScore()){
				return false;
			}
		}
		return true;
	}

	private void order(ArrayList<ScoreItem> iterationNetworks) {
		//I hate sorting algorithms! BubbleSort I choose you!
		for (int i = 1; i < iterationNetworks.size()-1; i++){
			if (iterationNetworks.get(i).getScore() < iterationNetworks.get(i-1).getScore()){
				ScoreItem temp = iterationNetworks.get(i);
				iterationNetworks.set(i, iterationNetworks.get(i-1));
				iterationNetworks.set(i-1, temp);
			}

		}

	}
}
