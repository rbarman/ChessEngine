
public class ScoredMovePair {
	public MovePair movePair;
	public int score;
	
	public void print(String tag) {
		System.out.printf("%s >>  %s @ (%d,%d) PAIRED with %s @ (%d,%d) has score of %d\n",
				tag, this.movePair.source.name, this.movePair.source.x, this.movePair.source.y,
				this.movePair.dest.name, this.movePair.dest.x,this.movePair.dest.y, this.score);	
		}
}
