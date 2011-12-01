package brain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



@SuppressWarnings("serial")
public class Brain implements Serializable{
	private int current_iteration = 1;
	private static final int NUMBER_OF_STARTER_NETWORKS = 40;
	private static final int NUMBER_OF_CREATIONAL_NETWORKS = 10;
	private static final int NUMBER_OF_NEURONS_IN_HIDDEN_LAYER = 25;
	private List<FeedForward> neuralNetworks = new ArrayList<FeedForward>();


	public void createStarterNetworks(int inputLength, int numberOfOutputs){
		for (int i = 0; i < NUMBER_OF_STARTER_NETWORKS; i++){
			FeedForward ff = new FeedForward();
			FeedForwardLayer inputLayer = new FeedForwardLayer(inputLength + numberOfOutputs);
			FeedForwardLayer hiddenLayer = new FeedForwardLayer(NUMBER_OF_NEURONS_IN_HIDDEN_LAYER);
			FeedForwardLayer outputLayer = new FeedForwardLayer(numberOfOutputs);

			ff.addLayer(inputLayer);
			ff.addLayer(hiddenLayer);
			ff.addLayer(outputLayer);
			ff.randomize();
			ff.setDescriptor("S"+(i+1)+"#");
			neuralNetworks.add(ff);
		}
	}

	public List<FeedForward> getNeuralNetworks() {
		return neuralNetworks;
	}

	public void learn() {
//		System.out.println("Sorting the networks...");
		//ordenar as redes por score
		while (!isOrdered(neuralNetworks)){
			order(neuralNetworks);
		}
//		for (FeedForward ff : neuralNetworks){
//			System.out.println(ff.getDescriptor() + ">: " + ff.getFitness());
//		}
		
		//Verbose time. I want: Iteration Average; Iteration Best (with identification).
		System.out.println("Iteration " + current_iteration + " results:");
		System.out.println("Average: " + groupFitness(neuralNetworks)/neuralNetworks.size());
		System.out.println("Best: " + neuralNetworks.get(0).getFitness() + " ( " + neuralNetworks.get(0).getDescriptor() + " ).");

		//seleccionar redes de topo
		ArrayList<FeedForward> creationalNetworks = selectNetworks(neuralNetworks, NUMBER_OF_CREATIONAL_NETWORKS);

		//preencher as primeiras posi��es com os top performers
		neuralNetworks = creationalNetworks;
		int c = 0;
		
		//criar novas redes com base nas top anteriores
		while (neuralNetworks.size() < NUMBER_OF_STARTER_NETWORKS){
				FeedForward evolvedNetwork = (FeedForward) creationalNetworks.get(c).newInstance();
				evolvedNetwork.setDescriptor(evolvedNetwork.getDescriptor()+"#E" + current_iteration);
				evolvedNetwork.evolve(0.2);
				neuralNetworks.add(evolvedNetwork);
				c++;
				if (c == NUMBER_OF_CREATIONAL_NETWORKS)
					c = 0;
		}
		
		current_iteration++;
		
		
	}

	private double groupFitness(List<FeedForward> nnlist) {
		double result = 0;
		for (FeedForward ff : nnlist){
			result += ff.getFitness();
		}
		return result;
	}

	private ArrayList<FeedForward> selectNetworks(
			List<FeedForward> iterationNetworks,
			int numberOfCreationalNetworks) {
		ArrayList<FeedForward> selectedNetworks = new ArrayList<FeedForward>();
		for (int i = 0; i < numberOfCreationalNetworks; i++)
			selectedNetworks.add(iterationNetworks.get(i));
			
		return selectedNetworks;
	}

	private boolean isOrdered(List<FeedForward> list) {

		for (int i = 1; i < list.size(); i++){
			if (list.get(i).getFitness() < list.get(i-1).getFitness()){
				return false;
			}
		}
		return true;
	}

	private void order(List<FeedForward> iterationNetworks) {
		//I hate sorting algorithms! BubbleSort I choose you!
		for (int i = 1; i < iterationNetworks.size(); i++){
			if (iterationNetworks.get(i).getFitness() < iterationNetworks.get(i-1).getFitness()){
				FeedForward temp = iterationNetworks.get(i);
				iterationNetworks.set(i, iterationNetworks.get(i-1));
				iterationNetworks.set(i-1, temp);
			}

		}

	}
	
	public int getCurrent_iteration() {
		return current_iteration;
	}
	
	public void setCurrent_iteration(int current_iteration) {
		this.current_iteration = current_iteration;
	}
	
}
