package brain;

import java.util.ArrayList;
import java.util.List;



public class Brain {
	private static final int NUMBER_OF_STARTER_NETWORKS = 10;
	private static final int NUMBER_OF_CREATIONAL_NETWORKS = 2;
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
			ff.setDescriptor("S#"+(i+1));
			neuralNetworks.add(ff);
		}
	}

	public List<FeedForward> getNeuralNetworks() {
		return neuralNetworks;
	}

	public void learn(int iteration) {
		System.out.println("Sorting the networks...");
		//ordenar as redes por score
		while (!isOrdered(neuralNetworks)){
			order(neuralNetworks);
		}
		for (FeedForward ff : neuralNetworks){
			System.out.println(ff.getDescriptor() + ">: " + ff.getFitness());
		}
		System.out.println("Group fitness: " + groupFitness(neuralNetworks));
		System.out.println("#######");

		//seleccionar redes de topo
		ArrayList<FeedForward> creationalNetworks = selectNetworks(neuralNetworks, NUMBER_OF_CREATIONAL_NETWORKS);

		//preencher as primeiras posições com os top performers
		neuralNetworks = creationalNetworks;
		int c = 0;
		
		//criar novas redes com base nas top anteriores
		while (neuralNetworks.size() < NUMBER_OF_STARTER_NETWORKS){
			try {
				FeedForward evolvedNetwork = (FeedForward) creationalNetworks.get(c).clone();
				evolvedNetwork.setDescriptor(evolvedNetwork.getDescriptor()+"#E" + iteration);
				evolvedNetwork.evolve(0.2, 0.1);
				neuralNetworks.add(evolvedNetwork);
				c++;
				if (c == NUMBER_OF_CREATIONAL_NETWORKS)
					c = 0;
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		
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
	
	private void writeToFile(){
		
	}
}
