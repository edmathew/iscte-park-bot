package brain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import activation.ActivationFunction;
import activation.ActivationSigmoid;
import matrix.Matrix;
import matrix.MatrixMath;

@SuppressWarnings("serial")
public class FeedForwardLayer implements Serializable{
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
			for (int i = 0 ; i < matrix.getCols(); i++){
				this.setCharge(i, MatrixMath.dotProduct(formMatrix(previousLayer.getCharge()), matrix.getCol(i)));
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

	private void setCharge(int pos, double charge) {
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

	public void mutate(double mutationProb) {
		if (!isInput()){
			Random r = new Random();
			int rows = matrix.getRows();
			int cols = matrix.getCols();

			for (int i = 0; i < rows; i++){
				for (int j = 0; j < cols; j++){
					if (r.nextDouble() > mutationProb){
						matrix.set(i, j, matrix.get(i, j)+(r.nextDouble()-0.6));
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
	
	public LinkedList<Double> getGeneticSequence(){
		LinkedList<Double> geneticSequence = new LinkedList<Double>();
		int rows = matrix.getRows();
		int cols = matrix.getCols();
		
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				geneticSequence.add(matrix.get(i, j));
			}
		}
		
		return geneticSequence;
	}
	
	public int getNeuronNumber() {
		return neuronNumber;
	}

	public void loadFromGeneticSequence(LinkedList<Double> geneticSequence) {
		double[][] temp = new double[previousLayer.charge.length][neuronNumber];
		for (int i= 0; i < previousLayer.charge.length; i++){
			for (int j = 0; j < neuronNumber; j++){
				temp[i][j] = geneticSequence.pop();
			}
		}
		this.matrix = new Matrix(temp);
	}

}
