
public class Bot {
	int color; 
	int depth; //depth will indicate difficulty of bot. 
	
	public Bot(int color, int depth) {
		this.color = color;
		this.depth = depth;
	}
	
	// very simple... only accounts for static piece values
	// and does not consider if pieces are attacked or not... 
	public int getMaterialScore(Board b) {
		int botMaterialValue = 0;
		int oppMaterialValue = 0;
		
		for(Piece p: b.getPiecesOfColor(color))
			botMaterialValue = botMaterialValue + p.getValue();
		for(Piece p: b.getPiecesOfColor(3 - color))
			oppMaterialValue = oppMaterialValue + p.getValue();
		
		int currentBoardValue = botMaterialValue - oppMaterialValue;
		int futureBoardValue = 0;
		return currentBoardValue + futureBoardValue;
	}
	
	// evaluateBoard WILL have other weights to it, futureScore, positionScore, etc.
	public int evaluateBoard(Board b) {
		return getMaterialScore(b);
	}	
	
	public int miniMax(int depth, Board b, boolean maxPlayer) {
		if(depth == 0)
			return evaluateBoard(b);
		if(maxPlayer) {
			int bestMoveVal = 0;  //  - infinity ?
			for(MovePair mv : b.getAvailableMoves(color)) {
				Board temp = b.getCopy();
				mv.printPair("MAX player : moving");
				temp.makeMove(mv.source, mv.dest);
				int eval = miniMax(depth - 1, temp, false);
				bestMoveVal = Math.max(bestMoveVal, eval);
			}
			return bestMoveVal;
		}
		else {
			int bestMoveVal = 0; //  + infinity ? 
			for(MovePair mv : b.getAvailableMoves(3 - color)) {
				Board temp = b.getCopy();
				mv.printPair("MIN player : moving");
				temp.makeMove(mv.source, mv.dest);
				int eval = miniMax(depth - 1, temp, true);
				bestMoveVal = Math.min(bestMoveVal, eval);
			}
			return bestMoveVal;
		}
	}
	
	public int alphabeta(int depth, Board b, int alpha, int beta, boolean maxPlayer) {
		if(depth == 0)
			return evaluateBoard(b);
		if(maxPlayer) {
			for(MovePair mv : b.getAvailableMoves(color)) {
				Board temp = b.getCopy();
				mv.printPair("MAX player : moving");
				temp.makeMove(mv.source, mv.dest);
				int eval = alphabeta(depth - 1, temp, alpha, beta, false);
				alpha = Math.max(alpha, eval);
				if(beta <= alpha) //beta cut off;
					break;
			}
			return alpha;
		}
		else {
			for(MovePair mv : b.getAvailableMoves(3 - color)) {
				Board temp = b.getCopy();
				mv.printPair("MIN player : moving");
				temp.makeMove(mv.source, mv.dest);
				int eval = alphabeta(depth - 1, temp, alpha, beta, true);
				beta = Math.min(beta, eval);
				if(beta <= alpha)
					break; // alpha cut off;
			}
			return beta;
		}
	}

}
