package brain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FeedForward implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String descriptor;
	private FeedForwardLayer firstLayer = null;
	private FeedForwardLayer lastLayer = null;
	private List<FeedForwardLayer> allLayers = new ArrayList<FeedForwardLayer>();
	private double fitness = 0;

	public FeedForward(){

	}

	public void addLayer(FeedForwardLayer ffl){
		if (lastLayer != null){
			ffl.setPreviousLayer(lastLayer);
			lastLayer.setNextLayer(ffl);
		}

		if (allLayers.size() == 0){
			firstLayer = lastLayer = ffl;
		}else{
			lastLayer = ffl;
		}
		allLayers.add(ffl);


	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public double[] calculate(double[] input){
		for (FeedForwardLayer ffl: allLayers){
			if (ffl.isInput())
				ffl.calculate(input);
			else if(ffl.isHidden() || ffl.isOutput())
				ffl.calculate(null);
			//			else if(ffl.isOutput() && allLayers.size() == 2)
			//				ffl.calculate(null);
			//			System.out.println("this layer has charge: " + printDoubleArray(ffl.getCharge()));
			//			if (ffl.getMatrix() != null)
			//				ffl.printMatrix(ffl.getMatrix());
		}
		return lastLayer.getCharge();
	}

	public String printDoubleArray(double[] array){
		String s = "";
		for (int i = 0; i < array.length; i++){
			s+= " " + array[i];
		}

		return s;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public void randomize() {
		for (FeedForwardLayer ffl : allLayers)
			ffl.randomize();
	}

	public void evolve(double probOfMutation){

		for (FeedForwardLayer ffl: allLayers){
			ffl.mutate(probOfMutation);
		}

	}

	public void setFirstLayer(FeedForwardLayer firstLayer) {
		this.firstLayer = firstLayer;
	}

	public void setLastLayer(FeedForwardLayer lastLayer) {
		this.lastLayer = lastLayer;
	}

	public void setAllLayers(List<FeedForwardLayer> allLayers) {
		this.allLayers = allLayers;
	}


	public FeedForward newInstance() {
		FeedForward copy = new FeedForward();
		for (FeedForwardLayer ffl: allLayers){
			copy.addLayer(ffl.newInstance());
		}
		copy.setDescriptor(descriptor);
		return copy;
	}
	
	public LinkedList<Double> getGeneticSequence(){
		LinkedList<Double> geneticSequence =  new LinkedList<Double>(); 
		for (FeedForwardLayer ffl: allLayers){
			if (!ffl.isInput()){
				geneticSequence.addAll(ffl.getGeneticSequence());
			}
		}
		
		return geneticSequence;
	}
	
	public void loadFromGeneticSequence(LinkedList<Double> geneticSequence){
		for (FeedForwardLayer ffl: allLayers){
			if (!ffl.isInput()){
				ffl.loadFromGeneticSequence(geneticSequence);
			}
		}
	}

	public LinkedList<Double> createChild(FeedForward mother) {
		//TODO: cut-size shouldn't be so hardcoded;
		
		LinkedList<Double> myGenes = getGeneticSequence();
		LinkedList<Double> mothersGenes = mother.getGeneticSequence();
		LinkedList<Double> childsGenes = new LinkedList<Double>(); 
		
		int cutSize = myGenes.size()/3;
		int cuttingPoint1 = (int) (Math.random()*(myGenes.size() - cutSize));
		int cuttingPoint2 = (int) cuttingPoint1 + cutSize;
		
		//Child will have Father - Mother - Father Genes
		
		for (int i = 0; i < myGenes.size(); i++){
			if (i < cuttingPoint1 || i > cuttingPoint2){
				childsGenes.add(myGenes.get(i));
			}else{
				childsGenes.add(mothersGenes.get(i));
			}
		}
		
		return childsGenes;
	}
	
	
}
