package brain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FeedForward implements Cloneable, Serializable{
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

	public void evolve(double probOfMutation, double mutationRate){

		for (FeedForwardLayer ffl: allLayers){
			ffl.mutate(probOfMutation, mutationRate);
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

	@Override
	protected Object clone() throws CloneNotSupportedException {
		FeedForward ff = new FeedForward();
		ff.setDescriptor(descriptor);
		ff.setFirstLayer(firstLayer);
		ff.setLastLayer(lastLayer);
		ff.setAllLayers(allLayers);
		return ff;
	}
	
	
}
