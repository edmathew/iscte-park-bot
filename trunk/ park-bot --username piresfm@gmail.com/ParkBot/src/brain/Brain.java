package brain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import learning_methods.LearningMethod;

public class Brain implements Serializable {

	private static final long serialVersionUID = 1L;
	private int current_iteration = 1;
	public static final int NUMBER_OF_STARTER_NETWORKS = 20;
	public static final int NUMBER_OF_CREATIONAL_NETWORKS = 10;
	private static final int NUMBER_OF_NEURONS_IN_HIDDEN_LAYER = 27;
	private List<FeedForward> neuralNetworks = new ArrayList<FeedForward>();

	public static Brain readFromFile(String fileName) {
		ObjectInputStream stream;
		Brain b = null;

		try {
			stream = new ObjectInputStream(new FileInputStream(new File(
					fileName)));
			b = (Brain) stream.readObject();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		}

		return b;
	}

	/**
	 * @return top NumberOfStarterNetworks
	 */
	public List<FeedForward> topN() {
		/*
		 * if (!isOrdered(neuralNetworks)) order(neuralNetworks);
		 */
		return neuralNetworks.subList(0, NUMBER_OF_STARTER_NETWORKS);
	}

	public void concat(List<FeedForward> newList) {
		for(FeedForward ff : newList){
			System.out.println(ff.getFitness());
		}
		System.out.println(neuralNetworks.size());
		neuralNetworks.addAll(newList);
		System.out.println(neuralNetworks.size());
		
		
		//neuralNetworks = topN();
		System.out.println(neuralNetworks.size());
	}

	public void createStarterNetworks(int inputLength, int numberOfOutputs) {
		for (int i = 0; i < NUMBER_OF_STARTER_NETWORKS; i++) {
			FeedForward ff = new FeedForward();
			FeedForwardLayer inputLayer = new FeedForwardLayer(inputLength
					+ numberOfOutputs);
			FeedForwardLayer hiddenLayer = new FeedForwardLayer(
					NUMBER_OF_NEURONS_IN_HIDDEN_LAYER);
			FeedForwardLayer outputLayer = new FeedForwardLayer(numberOfOutputs);

			ff.addLayer(inputLayer);
			ff.addLayer(hiddenLayer);
			ff.addLayer(outputLayer);
			ff.randomize();
			ff.setDescriptor("S" + (i + 1));
			neuralNetworks.add(ff);
		}
	}

	public List<FeedForward> getNeuralNetworks() {
		return neuralNetworks;
	}

	public void learn(LearningMethod lm) {
		lm.learn(this);
	}

	public double groupFitness(List<FeedForward> nnlist) {
		double result = 0;
		for (FeedForward ff : nnlist) {
			result += ff.getFitness();
		}
		return result;
	}

	public boolean isOrdered(List<FeedForward> list) {

		for (int i = 1; i < list.size(); i++) {
			if (list.get(i).getFitness() < list.get(i - 1).getFitness()) {
				return false;
			}
		}
		return true;
	}

	public void order(List<FeedForward> iterationNetworks) {
		//TODO: replace this with a quick-sort
		// I hate sorting algorithms! BubbleSort I choose you!
		for (int i = 1; i < iterationNetworks.size(); i++) {
			if (iterationNetworks.get(i).getFitness() < iterationNetworks.get(
					i - 1).getFitness()) {
				FeedForward temp = iterationNetworks.get(i);
				iterationNetworks.set(i, iterationNetworks.get(i - 1));
				iterationNetworks.set(i - 1, temp);
			}

		}

	}

	public int getCurrent_iteration() {
		return current_iteration;
	}

	public void setCurrent_iteration(int current_iteration) {
		this.current_iteration = current_iteration;
	}

	public void setNeuralNetworks(List<FeedForward> subList) {
		this.neuralNetworks = subList;
	}


}
