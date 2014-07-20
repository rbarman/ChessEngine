
public class Bot {
	int color; 
	int depth;  //depth will indicate difficulty of bot. 
	int openingMoveCount = 4; // number of moves considered for a game to be in opening game stage. 
	
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
	
	public void alphaBetaMove(Board b) { 
		Board temp = b.getCopy();
		ScoredMovePair botMove = alphabetaMain(temp, depth);
		b.makeMove(botMove.movePair.source, botMove.movePair.dest);
	}
	
	public ScoredMovePair miniMax(int depth, Board b,MovePair movePair, boolean maxPlayer) {
		
		ScoredMovePair best = new ScoredMovePair();
		best.movePair = movePair;
		best.movePair.printPair("pair");
		
		if(depth == 0) {
			best.score = evaluateBoard(b);
			return best;			
		}
		
		if(maxPlayer) {
			int bestMoveVal = 0;  //  - infinity ?
			for(MovePair mv : b.getAvailableMoves(color)) {
				Board temp = b.getCopy();
				mv.printPair("MAX player : moving");
				temp.makeMove(mv.source, mv.dest);
				ScoredMovePair eval = miniMax(depth - 1, temp, mv, false);
				bestMoveVal = Math.max(bestMoveVal, eval.score);
			}
			return best;
		}
		else {
			int bestMoveVal = 0; //  + infinity ? 
			for(MovePair mv : b.getAvailableMoves(3 - color)) {
				Board temp = b.getCopy();
				mv.printPair("MIN player : moving");
				temp.makeMove(mv.source, mv.dest);
				ScoredMovePair eval = miniMax(depth - 1, temp, mv, true);
				bestMoveVal = Math.min(bestMoveVal, eval.score);
			}
			return best;
		}
	}

	public ScoredMovePair miniMaxMain(Board b, int depth) {
		ScoredMovePair best = new ScoredMovePair();
		best.score = 0;
		for(MovePair mv : b.getAvailableMoves(color))
			mv.printPair("\t poassible move pairs");
		
		for(MovePair mv : b.getAvailableMoves(color)) {
			ScoredMovePair test = miniMax(depth, b , mv,  true);
			if(test.score >= best.score) {
				best.score = test.score;
				best.movePair = test.movePair;
				System.out.println("test.score : " + test.score);
			}
		}
		System.out.println("best score : " + best.score);
		best.movePair.printPair("best move pair");
		return best;
	}
	
	public ScoredMovePair alphabetaMain(Board b, int depth) {
		ScoredMovePair best = new ScoredMovePair();
		best.score = 0;
		System.out.println("the board received on alphabetaMai()");
		b.printBoardContents();
		for(MovePair mv : b.getAvailableMoves(color))
			mv.printPair("\t poassible move pairs");
		
		for(MovePair mv : b.getAvailableMoves(color)) {
			ScoredMovePair test = alphabeta(depth, b, mv, 0, 0, true);
			if(test.score >= best.score) {
				best.score = test.score;
				best.movePair = test.movePair;
				System.out.println("test.score : " + test.score);
			}
		}
		System.out.println("best score : " + best.score);
		best.movePair.printPair("best move pair");
		return best;
	}
	
	//useful info http://www.cs.ucla.edu/~rosen/161/notes/alphabeta.html
	public ScoredMovePair alphabeta(int depth, Board b,MovePair movePair, int alpha, int beta, boolean maxPlayer) {
		
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
				ScoredMovePair eval = alphabeta(depth -1, temp, mv, alpha, beta, false);
				alpha = Math.max(alpha, eval.score);
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
				ScoredMovePair eval = alphabeta(depth -1, temp, mv, alpha, beta, true);
				beta = Math.min(beta, eval.score);
				if(beta <= alpha)
					break; // alpha cut off;
			}
			best.score = beta;
			return best;
		}
	}	
}
