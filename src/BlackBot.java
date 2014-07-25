
/**
 * @author Rohan
 * Bot child
 */
public class BlackBot extends Bot {

	public BlackBot(int color, int depth) {
		super(color, depth);
	}
	
	/**
	 * @param b
	 * Makes a move on Board b. 
	 * Either opening or alpha beta based if Board b is in opening move state
	 */
	public void move(Board b){
		if(b.moveCount < openingMoveCount + 1)
			openingMove(b);
		else
			alphaBetaMove(b);
	}
	
	/**
	 * @param b
	 * Play a Black opening move on Board b
	 */
	public void openingMove(Board b){
		
	}
}
