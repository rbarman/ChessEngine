import java.util.ArrayList;

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
		
		for(Piece p: b.getPiecesOfColor(this.color))
			botMaterialValue = botMaterialValue + p.getValue();
		for(Piece p: b.getPiecesOfColor(3 - this.color)) 
			oppMaterialValue = oppMaterialValue + p.getValue();
		System.out.printf("\t\tbotMaterial : %d \t oppMaterialValue : %d\n",botMaterialValue, oppMaterialValue);
		b.printBoardContents();
		
		int currentBoardValue = botMaterialValue - oppMaterialValue;
		int futureBoardValue = 0;
		return currentBoardValue + futureBoardValue;
	}
	
	// evaluateBoard WILL have other weights to it, futureScore, positionScore, etc.
	public int evaluateBoard(Board b) {
//		System.out.println("board sent to evaluateBoard");
//		b.printBoardContents();
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
		Board temp = b.getCopy();
		
		if(depth == 0) {
			temp.makeMove(movePair.source, movePair.dest);
			best.score = evaluateBoard(temp);
			return best;			
		}
		
		if(maxPlayer) {
			int bestMoveVal = 0;  //  - infinity ?
			for(MovePair mv : b.getAvailableMoves(color)) {
				mv.printPair("MAX ",depth, this.depth);
				temp.makeMove(mv.source, mv.dest);
				ScoredMovePair eval = miniMax(depth - 1, temp, mv, false);
				bestMoveVal = Math.max(bestMoveVal, eval.score);
			}
			return best;
		}
		else {
			int bestMoveVal = 0; //  + infinity ? 
			for(MovePair mv : b.getAvailableMoves(3 - color)) {
				temp.makeMove(mv.source, mv.dest);
				mv.printPair("MIN ",depth, this.depth);
				ScoredMovePair eval = miniMax(depth - 1, temp, mv, true);
				bestMoveVal = Math.min(bestMoveVal, eval.score);
			}
			return best;
		}
	}

	public ScoredMovePair miniMaxMain(Board b, int depth) {
		ScoredMovePair best = new ScoredMovePair();
		best.score = -100;
		
		ArrayList<ScoredMovePair> allPairs = new ArrayList<ScoredMovePair>();
		
		for(MovePair mv : b.getAvailableMoves(color))
			mv.printPair("\t poassible move pairs");
		
		for(MovePair mv : b.getAvailableMoves(color)) {
			ScoredMovePair test = miniMax(depth, b , mv,  false);
			allPairs.add(test);
			System.out.println("score = " + test.score);
			if(test.score >= best.score) {
				best.score = test.score;
				best.movePair = test.movePair;
			}
		}
		for(ScoredMovePair smp : allPairs)
			smp.print("all");
		return best;
	}
	
	public ScoredMovePair alphabetaMain(Board b, int depth) {
		ScoredMovePair best = new ScoredMovePair();
		best.movePair = null;
		best.score = -100;
		
		ArrayList<ScoredMovePair> allPairs = new ArrayList<ScoredMovePair>();
		ArrayList<MovePair> availableMovePairs = b.getAvailableMoves(color);
		
		for(MovePair mv : availableMovePairs)
			mv.printPair("possible move pairs");
		
		for(MovePair mv : availableMovePairs) {
			mv.printPair("MAX");
			Board temp = b.getCopy();
			temp.makeMove(mv.source, mv.dest);
			ScoredMovePair test = alphabeta(depth, temp, mv, -1, 1, false); // wiki says alpha = - inf, beta = + inf
			allPairs.add(test);
			System.out.println("score = " + test.score);
			if(test.score > best.score) {
				best.score = test.score;
				best.movePair = test.movePair;
			}
		}
		for(ScoredMovePair smp : allPairs)
			smp.print("all"); 
		return best;
	}
	
	//useful info http://www.cs.ucla.edu/~rosen/161/notes/alphabeta.html
	public ScoredMovePair alphabeta(int depth, Board b,MovePair movePair, int alpha, int beta, boolean maxPlayer) {
		
		ScoredMovePair best = new ScoredMovePair();
		best.movePair = movePair;
		
		if(depth == 0) {
			best.score = evaluateBoard(b);
			return best;
		}
		
		if(maxPlayer) {
			for(MovePair mv : b.getAvailableMoves(color)) {
				mv.printPair("MAX ",depth, this.depth);
				Board temp = b.getCopy();
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
//				for(MovePair m : b.getAvailableMoves(3 - color))
//					m.printPair("\t opp can move");
				mv.printPair("MIN ", depth, this.depth);
				Board temp = b.getCopy();
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
