package activation;

@SuppressWarnings("serial")
public class ActivationStep implements ActivationFunction{
	double thresholdValue;
	
	public ActivationStep(double d) {
		this.thresholdValue = d;
	}

	@Override
	public double activationFunction(double d) {
		if (d > thresholdValue)
			return 1;
		return 0;
	}

	@Override
	public double derivativeFunction(double d) {
		return 0;
	}

}