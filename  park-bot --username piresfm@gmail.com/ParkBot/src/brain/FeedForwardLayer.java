package brain;

import java.util.Random;

import activation.ActivationFunction;
import activation.ActivationSigmoid;
import matrix.Matrix;
import matrix.MatrixMath;

public class FeedForwardLayer {
	private int neuronNumber;
	private Matrix matrix;
	private double[] charge;
	private FeedForwardLayer previousLayer;
	private FeedForwardLayer nextLayer;
	private ActivationFunction activationFunction;

	public FeedForwardLayer(int neuronNumber){
		this(neuronNumber, new ActivationSigmoid());
	}

	public FeedForwardLayer(int neuronNumber, ActivationFunction function){
		this.neuronNumber = neuronNumber;
		charge = new double[neuronNumber];
		this.activationFunction = function;
	}

	public void setMatrix(Matrix matrix){
		this.matrix = matrix;
	}

	public void setPreviousLayer(FeedForwardLayer ffl) {
		this.previousLayer = ffl;
	}

	public void setNextLayer(FeedForwardLayer ffl) {
		this.nextLayer = ffl;
	}

	public boolean isInput(){
		return previousLayer == null;
	}

	public boolean isHidden(){
		return previousLayer != null && nextLayer != null;
	}

	public boolean isOutput(){
		return nextLayer == null;
	}

	public void calculate(double[] input) {
		if (input != null){
			//first layer, recebe o input, não tem matriz, a carga dos seus neurónios é o valor de input
			this.charge = input;
		}else{
			//layers restantes, usa a carga das layers anteriores para dar input aos seus neurónios
			//ponderar no novo módulo de matrizes devolver as colunas inteiras para permitir iteração
			for (int i = 0 ; i < matrix.getCols(); i++){
				//				Matrix inputm = formMatrix(previousLayer.getCharge());
				//				Matrix m = formMatrix(matrix.getCol(i).toPackedArray());
				//				System.out.println("Calculating dot product between");
				//				printMatrix(inputm);
				//				System.out.println("and");
				//				printMatrix(m);

				this.setCharge(i, MatrixMath.dotProduct(formMatrix(previousLayer.getCharge()), matrix.getCol(i)));
				//				System.out.println("result = " + MatrixMath.dotProduct(inputm, m));
			}
		}
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void printMatrix(Matrix matrix){
		for (int i = 0; i < matrix.getRows(); i++){
			for (int j = 0; j < matrix.getCols(); j++){
				System.out.print(matrix.get(i, j) + ",");
			}
			System.out.println();
		}

	}

	public Matrix formMatrix(double[] input){
		Matrix inputVector = new Matrix(1, input.length);
		for (int i = 0; i < input.length; i++)
			inputVector.set(0, i, input[i]);
		return inputVector;
	}

	public Matrix formMatrix(Double[] input){
		Matrix inputVector = new Matrix(1, input.length);
		for (int i = 0; i < input.length; i++)
			inputVector.set(0, i, input[i]);
		return inputVector;
	}

	private void setCharge(int pos, double charge) {
		//que carga é que estes neurónios vão passar aos neurónios da camada seguinte?
		//vai depender da função de activação
		if (!isOutput())
			this.charge[pos] = activationFunction.activationFunction(charge);
		else
			this.charge[pos] = charge;
	}

	public double[] getCharge() {
		return charge;
	}

	public void randomize() {
		if (previousLayer != null){
			Random r = new Random();

			double[][] temp = new double[previousLayer.charge.length][neuronNumber];
			for (int i = 0; i < previousLayer.charge.length; i++){
				for (int j = 0 ; j < neuronNumber; j++){
					temp[i][j] = r.nextDouble();
				}
			}
			matrix = new Matrix(temp);
		}
	}

	public void mutate(double mutationProb, double mutationRate) {
		if (!isInput()){
			Random r = new Random();
			int rows = matrix.getRows();
			int cols = matrix.getCols();

			for (int i = 0; i < rows; i++){
				for (int j = 0; j < cols; j++){
					if (r.nextDouble() > mutationProb){
						matrix.set(i, j, matrix.get(i, j)*mutationRate);
					}
				}
			}
		}


	}

	public FeedForwardLayer newInstance() {
		FeedForwardLayer copy = new FeedForwardLayer(neuronNumber, activationFunction);
		if (!isInput()){
			copy.setMatrix(matrix.clone());
		}
		return copy;
	}

}
