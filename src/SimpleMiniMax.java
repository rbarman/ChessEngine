import java.util.ArrayList;

// example.
public class SimpleMiniMax {
	int color; // color is the color of the bot.
	int depthLimit = 2;
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
	
	public boolean depthLimitReached(int depth) {
		System.out.printf("%d vs %d\n", depth, depthLimit);
		return (depth == depthLimit);
	}
	

	// general pseuduo method of miniMax
//	minimax(player,board)
//    if(game over in current board position)
//        return winner
//    children = all legal moves for player from this board
//    if(max's turn)
//        return maximal score of calling minimax on all the children
//    else (min's turn)
//        return minimal score of calling minimax on all the children
		
	// need to have default board for every bot's first step. 	
	public int miniMax(Board b) {
		return maxMove(b, 0);
	}
	
	public int miniMaxTest(Board b) {
		return maxMoveTest(b, 0);
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
				Board temp = b.getCopy();
				temp.makeMove(mv.source, mv.dest);
				int eval = minMoveTest(temp, depth + 1);
				if(eval > bestMoveVal)
					bestMoveVal = eval;
			}
			return bestMoveVal;
		}
	}
	
// testing purposes. 
	 public void showFirstMoves(Board b) {
		System.out.println("Showing the first moves!");
		ArrayList<MovePair> movePairs = b.getAvailableMoves(color);
 		
 		for(MovePair mv : movePairs)
 			mv.printPair("");
 		System.out.println("----");
 		for(MovePair mv : movePairs) {
 			Board temp = b.getCopy();
 			mv.printPair("firstMove");
 			temp.makeMove(mv.source, mv.dest);
 			System.out.println("score " + evaluateBoard(temp) + " -----\n");
 		}
 	}
	
	// for first move only. 
	public int maxMoveTest(Board b , int depth) {
		System.out.println("maxMove with a depth of " + depth);
		if(depthLimitReached(depth)) {
			System.out.println("is true!");
			return evaluateBoard(b);
		}
		else {
			int bestMoveVal = 0;
//			System.out.println("board info before moving");
//			b.printInfoOnAllPieces("before");
			
			ArrayList<MovePair>mv = b.getAvailableMoves(color);
			Board temp = b.getCopy();
			MovePair firstMove = mv.get(0);
			firstMove.printPair("moving");
			temp.makeMove(firstMove.source, firstMove.dest);
			
//			System.out.println("board info after moving");
//			b.printInfoOnAllPieces("after");
			System.out.println("opponent available moves after moving");
			for(MovePair mvs : b.getAvailableMoves(3 - color))
				mvs.printPair("opp mvs");

			int eval = minMoveTest(temp, depth + 1);
			if(eval > bestMoveVal)
				bestMoveVal = eval;
			
			return bestMoveVal;
		}
	}
	
	// for first move only. 
	public int minMoveTest(Board b , int depth) {

		System.out.println("minMove with a depth of " + depth);
		if(depthLimitReached(depth))
			return evaluateBoard(b);
		else {
			int bestMoveVal = 0;
			
			System.out.println("\t\t board received to minMove");
			b.printBoardContents();
//			b.printInfoOnAllPieces("pls");
						
			ArrayList<MovePair>mv = b.getAvailableMoves(3 - color);
			System.out.println("opponent availabe moves");
			for(MovePair m : mv)
				m.printPair("min's moves");
			
			MovePair firstMove = mv.get(0);
			Board temp = b.getCopy();
			firstMove.printPair("moving");
			temp.makeMove(firstMove.source,firstMove.dest);
			
			int eval = maxMove(temp, depth + 1);
			if(eval > bestMoveVal)
				bestMoveVal = eval;
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
			
			System.out.println("\t\t board received to minMove");
			b.printBoardContents();
			for(MovePair mv : b.getAvailableMoves(3 - color))
				mv.printPair("\t movepairs for this board");
			
			for(MovePair mv : b.getAvailableMoves(3 - color)) {
				Board temp = b.getCopy();
				mv.printPair("moving");
				temp.makeMove(mv.source, mv.dest);
				int eval = maxMove(temp, depth + 1);
				if(eval > bestMoveVal)
					bestMoveVal = eval;
			}
			return bestMoveVal;	
		}
	}	
}