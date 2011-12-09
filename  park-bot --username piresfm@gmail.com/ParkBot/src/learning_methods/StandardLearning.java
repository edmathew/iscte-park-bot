package learning_methods;

import main_stuff.LoadGUI;
import brain.Brain;
import brain.FeedForward;

public class StandardLearning extends LearningMethod{

	@Override
	public void learn(Brain b, double mutationRate) {
				while (!b.isOrdered(b.getNeuralNetworks())) {
					b.order(b.getNeuralNetworks());
				}

				String s = "Average: " + b.groupFitness(b.getNeuralNetworks()) / b.getNeuralNetworks().size();
				s  += "Best: " + b.getNeuralNetworks().get(0).getFitness();
				LoadGUI.setPreviousPerformance(s);
				b.setNeuralNetworks(b.getNeuralNetworks().subList(0, Brain.NUMBER_OF_CREATIONAL_NETWORKS));
				int c = 0;

				while (b.getNeuralNetworks().size() < Brain.NUMBER_OF_STARTER_NETWORKS) {
					FeedForward evolvedNetwork = (FeedForward) b.getNeuralNetworks()
							.get(c).newInstance();
					evolvedNetwork.setDescriptor(evolvedNetwork.getDescriptor() + "#E"
							+ b.getCurrent_iteration());
					evolvedNetwork.evolve(mutationRate);
					b.getNeuralNetworks().add(evolvedNetwork);
					c++;
					if (c == Brain.NUMBER_OF_CREATIONAL_NETWORKS)
						c = 0;
				}

				b.setCurrent_iteration(b.getCurrent_iteration()+1);
	}

}
