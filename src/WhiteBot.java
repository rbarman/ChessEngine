
/**
 * @author Rohan
 * Bot child
 */
public class WhiteBot extends Bot {

	public WhiteBot(int color, int depth) {
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
	 * Play a White opening move on Board b
	 */
	public void openingMove(Board b){

	}
}