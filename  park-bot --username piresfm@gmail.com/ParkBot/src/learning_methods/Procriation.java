package learning_methods;

import java.util.LinkedList;
import java.util.List;

import brain.Brain;
import brain.FeedForward;

public class Procriation extends LearningMethod{

	@Override
	public void learn(Brain b) {
		synchronized (b.getNeuralNetworks()){
			while (!b.isOrdered(b.getNeuralNetworks())){
				b.order(b.getNeuralNetworks());
			}

			LinkedList<FeedForward> temp = new LinkedList<FeedForward>();

			//O Rei da tribo vai acasalar com os 9 que lhe seguem
			FeedForward king = b.getNeuralNetworks().get(0);

			List<FeedForward> matingPartners = b.getNeuralNetworks().subList(1, 10);


			//		System.out.println("Gene size: " + king.getGeneticSequence().size());

			LinkedList<Double> kings_genes = king.getGeneticSequence();
			for (FeedForward ff: matingPartners){
				LinkedList<Double> childs_genes = king.createChild(ff);
				FeedForward child = king.newInstance();
				child.loadFromGeneticSequence(childs_genes);
				child.setDescriptor("("+king.getDescriptor() + ")+(" + ff.getDescriptor()+")");
				child.evolve(0.1);
				temp.add(child);
			}
			
			b.setNeuralNetworks(temp);
			b.getNeuralNetworks().addAll(matingPartners);
		}


	}



}
