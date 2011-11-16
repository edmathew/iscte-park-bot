package util;

public class InputUniformization {
	private double lowestValue;
	private double highestValue;
	
	public InputUniformization(double lowestValue, double highestValue) {
		this.lowestValue = lowestValue;
		this.highestValue = highestValue;
	}
	
	public double getUniformValue(double value){
		return ((highestValue - value)/highestValue);
	}
	
	public static void main(String[] args) {
		double current = 3.1;
		double current2[] = {3.1, 3.1, 2, 3};
		
		InputUniformization iu = new InputUniformization(0, 3.1);
		System.out.println(iu.getUniformValue(current2));
	}

	public double[] getUniformValue(double[] array) {
		double temp[] = new double[array.length];
		for (int i=0; i < temp.length; i++){
			temp[i] = getUniformValue(array[i]);
		}
		return temp;
	}
	
}
