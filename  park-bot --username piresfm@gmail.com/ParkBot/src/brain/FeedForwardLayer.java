package brain;

import matrix.Matrix;
import matrix.MatrixMath;

public class FeedForwardLayer {
	private int neuronNumber;
	private Matrix matrix;
	private double[] charge;
	private FeedForwardLayer previousLayer;
	private FeedForwardLayer nextLayer;

	public FeedForwardLayer(int neuronNumber){
		this.neuronNumber = neuronNumber;
		charge = new double[neuronNumber];

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
				this.setCharge(i, MatrixMath.dotProduct(formMatrix(previousLayer.getCharge()), matrix.getCol(i)));
			}
		}
	}

	public Matrix formMatrix(double[] input){
		Matrix inputVector = new Matrix(1, input.length);
		for (int i = 0; i < input.length; i++)
			inputVector.set(0, i, input[i]);
		return inputVector;
	}

	private void setCharge(int pos, double charge) {
		this.charge[pos] = charge;
	}

	public double[] getCharge() {
		return charge;
	}

}
