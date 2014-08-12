
/**
 * @author Rohan
 * MovePair is Class that represents a move action. 
 * A source Piece with a respective and Piece dest.
 * MovePair object should only be used if Piece source is valid to move to Piece dest
 */
public class MovePair {
	Piece source;
	Piece dest;
	
	/**
	 * @return Algebraic name of each piece
	 * example : e2e2
	 */
	public String simpleName(){
		return source.getAlgebraic() + "" + "" + dest.getAlgebraic();
	}
	
	/**
	 * @param source
	 * @param dest
	 * Creates MovePair with given source and dest
	 */
	public MovePair(Piece source, Piece dest) {
		this.source = source;
		this.dest = dest;
	}
	
	/**
	 * @param tag : debugging statement
	 * Prints information about MovePair's source and dest.
	 */
	public void printPair(String tag) { 	
		//System.out.printf("%s  %s @ (%d,%d) %s PAIRED with %s @ (%d,%d) %s \n",
				//tag, this.source.name, this.source.x, this.source.y, this.source.getAlgebraic(),
				//this.dest.name, this.dest.x,this.dest.y, this.dest.getAlgebraic());
	}
	
	/**
	 * @param tag
	 * @param depth
	 * @param maxDepth
	 * Prints information about MovePair's source and dest with spacing in mind.
	 * This should be called for recursive methods such as Bot.alphabeta or Bot.minimax
	 */
	public void printPair(String tag, int depth, int maxDepth) {
		String tab = "";
		for(int i = 0; i < maxDepth - depth + 1; i++)
			tab = "\t" + tab;
		printPair(tab + tag);
	}
}
