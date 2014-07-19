import java.util.ArrayList;

// example.
public class SimpleMiniMax {
	int color; // color is the color of the bot.
	int depthLimit = 4;
	public SimpleMiniMax( int color) {
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
//				temp.printBoardContents();
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
//				temp.printBoardContents();
				int eval = miniMax(depth - 1, temp, true);
				bestMoveVal = Math.max(bestMoveVal, eval);
			}
			return bestMoveVal;
		}
	}
		 
}