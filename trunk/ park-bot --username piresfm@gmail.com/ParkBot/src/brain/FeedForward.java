package brain;

import java.util.ArrayList;
import java.util.List;


public class FeedForward {
	private FeedForwardLayer firstLayer = null;
	private FeedForwardLayer lastLayer = null;
	private List<FeedForwardLayer> allLayers = new ArrayList<FeedForwardLayer>();
	private float fitness = 0;
	
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
	
	public double[] calculate(double[] input){
		for (FeedForwardLayer ffl: allLayers){
			if (ffl.isInput())
				ffl.calculate(input);
			else if(ffl.isHidden())
				ffl.calculate(null);
			else if(ffl.isOutput() && allLayers.size() == 2)
				ffl.calculate(null);
		}
		return lastLayer.getCharge();
	}
	
	public float getFitness() {
		return fitness;
	}
	
	public void setFitness(float fitness) {
		this.fitness = fitness;
	}

	public void randomize() {
		for (FeedForwardLayer ffl : allLayers)
			ffl.randomize();
	}
}
