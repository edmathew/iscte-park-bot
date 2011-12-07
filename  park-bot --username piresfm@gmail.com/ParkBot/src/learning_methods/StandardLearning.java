package learning_methods;

import brain.Brain;
import brain.FeedForward;

public class StandardLearning extends LearningMethod{

	@Override
	public void learn(Brain b) {
				while (!b.isOrdered(b.getNeuralNetworks())) {
					b.order(b.getNeuralNetworks());
				}
				System.out.println("Iteration " + b.getCurrent_iteration() + " results:");
				System.out.println("Average: " + b.groupFitness(b.getNeuralNetworks()) / b.getNeuralNetworks().size());
				System.out.println("Best: " + b.getNeuralNetworks().get(0).getFitness()
						+ " ( " + b.getNeuralNetworks().get(0).getDescriptor() + " ).");

				b.setNeuralNetworks(b.getNeuralNetworks().subList(0, Brain.NUMBER_OF_CREATIONAL_NETWORKS));
				int c = 0;

				while (b.getNeuralNetworks().size() < Brain.NUMBER_OF_STARTER_NETWORKS) {
					FeedForward evolvedNetwork = (FeedForward) b.getNeuralNetworks()
							.get(c).newInstance();
					evolvedNetwork.setDescriptor(evolvedNetwork.getDescriptor() + "#E"
							+ b.getCurrent_iteration());
					evolvedNetwork.evolve(0.2);
					b.getNeuralNetworks().add(evolvedNetwork);
					c++;
					if (c == Brain.NUMBER_OF_CREATIONAL_NETWORKS)
						c = 0;
				}

				b.setCurrent_iteration(b.getCurrent_iteration()+1);
	}

}
