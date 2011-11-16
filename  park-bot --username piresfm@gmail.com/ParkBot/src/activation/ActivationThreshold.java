package activation;

public class ActivationThreshold implements ActivationFunction{
	double thresholdValue;
	
	public ActivationThreshold(double d) {
		this.thresholdValue = d;
	}

	@Override
	public double activationFunction(double d) {
		if (d > thresholdValue)
			return d;
		return 0;
	}

	@Override
	public double derivativeFunction(double d) {
		// TODO Auto-generated method stub
		return 0;
	}

}
