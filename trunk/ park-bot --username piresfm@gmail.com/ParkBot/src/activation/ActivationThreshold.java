package activation;

@SuppressWarnings("serial")
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
		return 0;
	}

}
