package brain;

import matrix.Matrix;

public class BrainTester {
	public static void main(String[] args) {
		FeedForward ff = new FeedForward();
		FeedForwardLayer input = new FeedForwardLayer(2);
		FeedForwardLayer output = new FeedForwardLayer(2);

		double[][] outputLayerMatrixDef = {
				{2, 0},
				{1, 2}
		};
		Matrix outputLayerMatrix = new Matrix(outputLayerMatrixDef); 
		output.setMatrix(outputLayerMatrix);
		ff.addLayer(input);
		ff.addLayer(output);

		double[] inputData = {2,2};
		double[] result = ff.calculate(inputData);
		System.out.println(result[0] + " " + result[1]);
	}
}
