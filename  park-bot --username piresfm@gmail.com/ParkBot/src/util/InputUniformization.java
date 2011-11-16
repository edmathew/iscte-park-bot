package util;

public class InputUniformization {
	private float lowestValue;
	private float highestValue;
	
	public InputUniformization(float lowestValue, float highestValue) {
		this.lowestValue = lowestValue;
		this.highestValue = highestValue;
	}
	
	public float getUniformValue(float value){
		return 1- ((highestValue - value)/highestValue);
	}
	
	public static void main(String[] args) {
		float current = 1.5f;
		
		InputUniformization iu = new InputUniformization(0, 3f);
		System.out.println(iu.getUniformValue(current));
	}
	
}
