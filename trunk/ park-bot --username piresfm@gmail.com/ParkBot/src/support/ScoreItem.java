package support;

import brain.FeedForward;

public class ScoreItem {
	private FeedForward ff;
	private double score;
	
	public ScoreItem(FeedForward ff, double score) {
		this.ff = ff;
		this.score = score;
	}
	
	public FeedForward getFf() {
		return ff;
	}
	
	public double getScore() {
		return score;
	}
	
}
