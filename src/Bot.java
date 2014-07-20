
public class Bot {
	int color; 
	int depth;  //depth will indicate difficulty of bot. 
	
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
	
	// useful info http://www.cs.ucla.edu/~rosen/161/notes/alphabeta.html
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

	public ScoredMovePair alphabetaMain(Board b, int depth) {
		ScoredMovePair best = new ScoredMovePair();
		best.score = 0;
		int count = 0;
		for(MovePair mv : b.getAvailableMoves(color)) {
			System.out.println("count : " + ++count);
			ScoredMovePair test = alphabetaNew(depth, b, mv, 0, 0, true);
			if(test.score >= best.score) {
				best.score = test.score;
				best.movePair = test.movePair;
			}
		}
		System.out.println("best score : " + best.score);
		System.out.println(best);
		System.out.println(best.movePair);
		best.movePair.printPair("best move pair");
		return best;
	}
	
	public ScoredMovePair alphabetaNew(int depth, Board b,MovePair movePair, int alpha, int beta, boolean maxPlayer) {
		
		ScoredMovePair best = new ScoredMovePair();
		best.movePair = movePair;
		best.movePair.printPair("pair");
		
		if(depth == 0) {
			best.score = evaluateBoard(b);
			return best;
		}
		
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
			best.score = alpha;
			return best;
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
			best.score = beta;
			return best;
		}
	}	

	public void move(Board b){
		
	}
}
