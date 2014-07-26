
/**
 * @author Rohan
 * ScoredMovePair is MovePair with a score attached to it. 
 * Primary use for ScoredMovePair is in Bot.minimax or Bot.alphabeta
 * This Class could be re factored out by adding a score field to MovePair
 */
public class ScoredMovePair {
	public MovePair movePair;
	public int score;
	
	public void print(String tag) {
		System.out.printf("%s >>  %s @ (%d,%d) %s PAIRED with %s @ (%d,%d) %s has score of %d\n",
				tag, this.movePair.source.name, this.movePair.source.x, this.movePair.source.y,
				this.movePair.source.getAlgebraic(),
				this.movePair.dest.name, this.movePair.dest.x,this.movePair.dest.y,
				this.movePair.dest.getAlgebraic(),this.score);	
		}
}
