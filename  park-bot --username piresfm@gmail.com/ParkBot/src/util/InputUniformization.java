package util;

public class InputUniformization {
	@SuppressWarnings("unused")
	private double lowestValue;
	private double highestValue;
	
	public InputUniformization(double lowestValue, double highestValue) {
		this.lowestValue = lowestValue;
		this.highestValue = highestValue;
	}
	
	public double getUniformValue(double value){
		return ((highestValue - value)/highestValue);
	}
	
	public double[] getUniformValue(double[] array) {
		double temp[] = new double[array.length];
		for (int i=0; i < temp.length; i++){
			temp[i] = getUniformValue(array[i]);
		}
		return temp;
	}
	
}
