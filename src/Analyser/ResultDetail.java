package Analyser;

public class ResultDetail {
	public String scoreInString;
	public String name;
	public double score;

	public ResultDetail(String name, String score, double scoreInDouble) {
		this.name = name;
		this.scoreInString = score;
		this.score = scoreInDouble;
	}
}
