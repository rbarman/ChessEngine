import java.util.ArrayList;

// example.
public class SimpleMiniMax {

//	Board board; // board is the board.
	int color; // color is the color of the bot.
	int depthLimit = 1;
	public SimpleMiniMax( int color) {
//		this.board = board;
		this.color = color;
	}
	
	// should be recursive
		// need to create a temp board on iteration and pass it in. 
	public int getFutureBoardValue(Board b) {
		int futureBoardValue = 0;
		
		// can we attack opponent pieces?
		for(Piece p : b.getPiecesOfColor(3 - color)) {
			ArrayList<Piece> ourAttackers = b.isAttackedBy(p, color);
			// ourAttackers are our Pieces that can attack opponent's Piece , p.
			if(ourAttackers.size() == 0)
				continue; // nothing is attacking p. 
			for(Piece ap : ourAttackers) {
				b.makeMove(p, ap);
				
			}
		}
		
		
//		for(Piece p : b.getPiecesOfColor(color)) {
//			ArrayList<Piece> attackers  = b.isAttackedBy(p, 3 - color);
//			if(attackers.size() == 0)
//				continue;
//			// validation is already done. 
//			for(Piece ap : attackers) {
//				b.makeMove(p, ap);
////				return getFutureBoardValue(b);
//			}
//		}
		
		return futureBoardValue;
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
//		int futureBoardValue = getFutureBoardValue(b);
		int futureBoardValue = 0;
		return currentBoardValue + futureBoardValue;
	}
	
	// evaluateBoard WILL have other weights to it, futureScore, positionScore, etc.
	public int evaluateBoard(Board b) {
		return getMaterialScore(b);
	}
	
	public boolean depthLimitReached(int depth) {
		System.out.printf("%d vs %d\n", depth, depthLimit);
		return (depth == depthLimit);
	}
	
	// nned to have default board for every bot's first step. 
	public int miniMax(Board b) {
		return maxMove(b, 0);
	}
	
	// perspective of bot 
	public int maxMove(Board b, int depth) { 
		System.out.println("maxMove with a depth of " + depth);
		if(depthLimitReached(depth)) {
			System.out.println("is true!");
			return evaluateBoard(b);
		}
		else {
			int bestMoveVal = 0;
			for(MovePair mv : b.getAvailableMoves(color)) {
				b.makeMove(mv.source, mv.dest);
				int eval = minMove(b, depth + 1);
				if(eval > bestMoveVal)
					bestMoveVal = eval;
			}
			return bestMoveVal;
		}
	}
	
	// perspective of the opponent
	public int minMove(Board b, int depth) {
		System.out.println("minMove with a depth of " + depth);
		if(depthLimitReached(depth))
			return evaluateBoard(b);
		else {
			int bestMoveVal = 0;
			for(MovePair mv : b.getAvailableMoves(3 - color)) {
				b.makeMove(mv.source, mv.dest);
				int eval = maxMove(b, depth + 1);
				if(eval > bestMoveVal)
					bestMoveVal = eval;
			}
			return bestMoveVal;	
		}
	}	
}