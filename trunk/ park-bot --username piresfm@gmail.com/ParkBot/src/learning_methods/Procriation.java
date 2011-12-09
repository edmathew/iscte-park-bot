package learning_methods;

import java.util.LinkedList;
import java.util.List;

import main_stuff.LoadGUI;

import brain.Brain;
import brain.FeedForward;

public class Procriation extends LearningMethod{

	@Override
	public void learn(Brain b, double mutationRate) {
		synchronized (b.getNeuralNetworks()){
			while (!b.isOrdered(b.getNeuralNetworks())){
				b.order(b.getNeuralNetworks());
			}
			
			String s = "Average: " + b.groupFitness(b.getNeuralNetworks()) / b.getNeuralNetworks().size();
			s  += "Best: " + b.getNeuralNetworks().get(0).getFitness();
			LoadGUI.setPreviousPerformance(s);

			LinkedList<FeedForward> temp = new LinkedList<FeedForward>();

			//O Rei da tribo vai acasalar com os 9 que lhe seguem
			FeedForward king = b.getNeuralNetworks().get(0);

			List<FeedForward> matingPartners = b.getNeuralNetworks().subList(1, 10);


			//		System.out.println("Gene size: " + king.getGeneticSequence().size());

			for (FeedForward ff: matingPartners){
				LinkedList<Double> childs_genes = king.createChild(ff);
				FeedForward child = king.newInstance();
				child.loadFromGeneticSequence(childs_genes);
				child.setDescriptor("E"+b.getCurrent_iteration());
				child.evolve(mutationRate);
				temp.add(child);
			}
			
			matingPartners.add(king);
			
			while (!b.isOrdered(matingPartners))
				b.order(matingPartners);
			
			b.setNeuralNetworks(matingPartners);
			b.getNeuralNetworks().addAll(temp);
			b.setCurrent_iteration(b.getCurrent_iteration()+1);
		}


	}



}
