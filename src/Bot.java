import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Rohan
 * Bot parent Class, WhiteBot and BlackBot are children
 *
 */
public class Bot {
	int color; // color of bot, 1 = white, 2 = black
	int depth;  // depth is how many plies / moves ahead Bot will think. Higher depth results in higher difficulty 
	ArrayList<OpeningLine>openingLines;
	String currentLine = null;
	boolean inOpening = true;

	/**
	 * @param color
	 * @param depth
	 * Creates Bot based on color and depth
	 */
	public Bot(int color, int depth, ArrayList<OpeningLine> openingLines) {
		this.color = color;
		this.depth = depth;
		this.openingLines = openingLines;
	}	
	
	public void move(Board b){
		if(inOpening)
			openingMove(b);
		else
			alphaBetaMove(b);
	}
		
	public void openingMove(Board b){
		if(b.moveCount == 0 && color == 1) {
			// make first white move.
				// later make randomization for e2e4, d2d4, etc
			MovePair e2e4 = new MovePair(b.getPieceAt("e2"), b.getPieceAt("e4"));
			b.makeMove(e2e4.source, e2e4.dest);
			e2e4.printPair("BOT will move");
		}
		else if(b.moveCount == 1 && color == 2) {
			// make first black move. 
				// later make randomization for all supported openings. 
			MovePair e7e5 = new MovePair(b.getPieceAt("e7"), b.getPieceAt("e5"));
			b.makeMove(e7e5.source, e7e5.dest);
			e7e5.printPair("BOT will move");
		}
		else  {
			// boolean bs is a ugly way to exit inner for loop to get to outer for loop. 
			for(OpeningLine ol : openingLines) {
				boolean bs = false;
				String[] olSplit = ol.line.split(" ");
								
				for(int i = 0; i < b.playedMoveList.size(); i++) {
//					System.out.printf("o1 split vs playedMove : %s vs %s\n", olSplit[i], b.playedMoveList.get(i).simpleName());
					if(!olSplit[i].equalsIgnoreCase(b.playedMoveList.get(i).simpleName())) 
						bs = true;
				}
				// if we can get past this for loop this means that there are matches with the current o1 and b.playedMoveList.
				// if o1Split is smaller than b.playedMoveList, then we can not use this opening line. 
				if(!bs && olSplit.length > b.playedMoveList.size()) {
					System.out.println("recognized : " + ol.name);
					System.out.println("will play... " + olSplit[b.playedMoveList.size()]);
					return;
				}
			}
			System.out.println("No Opening matches found 2!");
			inOpening = false;
//			alphaBetaMove(b);
		}
	}
	
	public int getAttackScore(Board b) {
		// Attack score of us is really the same as positive defense score of opponent ? 
		return -1 * new Bot(3 - color, depth, openingLines).getDefenseScore(b);
	}

	public int getDefenseScore(Board b) {
		int defenseScore = 0;
		for(Piece p : b.getPiecesOfColor(color)) {
			ArrayList<Piece> attackers = b.isAttackedBy(p, 3 - color);
			ArrayList<Piece> defenders = b.isDefendedBy(p);


			if(defenders.size() == 0 && attackers.size() == 0) {
				//opp is not attacking P and we are not defending P
//				System.out.println("\tdef and att == 0 \t" + defenseScore);
			}
			else if(defenders.size() == 0) {
				// opp has atleast one attack on P and we are not defending P
//				System.out.println("\tdef == 0 \t" + defenseScore);
				p.printInfo("has 0 defenders, def -- by " + p.getValue());				
				defenseScore -= p.getValue();
			}
			else if(attackers.size() == 0) {
//				System.out.println("\tatt == 0 \t" + defenseScore);
			} // enemy is not attacking P
			else { 
				System.out.println("in the else block");
				p.printInfo("\t checking...");
				ArrayList<Integer> attackerValues = new ArrayList<Integer>();
				ArrayList<Integer> defenderValues = new ArrayList<Integer>();

				for(Piece attacker : attackers) { 
					attacker.printInfo("\t\t ATTACKER");
					attackerValues.add(attacker.getValue());
				}

				for(Piece defender : defenders) { 
					defender.printInfo("\t\t DEFENDER");
					defenderValues.add(defender.getValue());
				}

				Collections.sort(defenderValues);  
				Collections.sort(attackerValues);

				while(!attackerValues.isEmpty()) {

					if((attackerValues.size() == 1 && defenderValues.size() == 1)) {
						if(attackerValues.get(0) >= p.getValue()){
							// neutral. 
							// opponent has one attacking piece and I have one defending piece.
							// if the value of attacker and piece attacked is same, 
							// 		the value of my defender does not matter since opponent has one attack
							// 		and would attack first to trade.
							// ex) Opp pawn attacking my pawn and my queen is defended pawn,
								// neutral -> opp can take and i will retake with queen ,etc
							System.out.println("\t\tneutral");
							break;
						}
						if(p.getValue() > attackerValues.get(0)) {
							defenseScore -= p.getValue();
							System.out.println("\t\tp.getValue > attackerValues.get(0)");
							System.out.println("\t\tdef -- by " + p.getValue());
							break;
						}
					}

					if(defenderValues.isEmpty()) {
						// opp has extra attacks after we have traded away our defenders.
						for(int i = 0; i < attackerValues.size(); i++) { 
							defenseScore--;
							System.out.println("\t\tdef--");
						}
						break;
					}

					if(defenderValues.get(0) > attackerValues.get(0)){
						// value of our lowest defender is greater than value of lowest opp attacker, not good
						System.out.println("\t\tdef val > att val, reducing def by " + p.getValue());
						defenseScore -= p.getValue();
						break;
					}
					defenderValues.remove(0);
					attackerValues.remove(0);
				}
				// check when attackerValues is empty if we still have any defenders. 
				if(attackerValues.isEmpty() && !defenderValues.isEmpty()) {
					System.out.println(" still have defenders, def++");
					for(int i = 0; i < defenderValues.size(); i++) 
						defenseScore++;
				}	
			}
		}
		return defenseScore;
	}

	/**
	 * @param b
	 * @return material score of Board b
	 * only accounts for static piece values and does not consider if pieces are attacked or not 
	 */
	public int getMaterialScore(Board b) {
		int botMaterialValue = 0;
		int oppMaterialValue = 0;

		for(Piece p: b.getPiecesOfColor(this.color))
			botMaterialValue = botMaterialValue + p.getValue();
		for(Piece p: b.getPiecesOfColor(3 - this.color)) 
			oppMaterialValue = oppMaterialValue + p.getValue();

		return botMaterialValue - oppMaterialValue;
	}

	/**
	 * @param b
	 * @return Score value of Board b.
	 * Used in Bot.alphabeta and Bot.minimax
	 * evaluateBoard WILL have other weights to it, futureScore, positionScore, etc. 
	 */
	public int evaluateBoard(Board b) {
		int materialScore = getMaterialScore(b);
		int defenseScore = getDefenseScore(b);
		int attackScore = getAttackScore(b);
		String tab = "";
		for(int i = 0; i < depth + 2; i++)
			tab = "\t" + tab;

		int boardScore = materialScore + defenseScore + attackScore;
		System.out.printf("%sfinal : %d \tcbv :%d\tdS : %d \taS : %d\n",tab,boardScore,materialScore, defenseScore, attackScore);
		return boardScore;
	}	

	/**
	 * @param b 
	 * Bot moves the best MovePair returned from Bot.alphabetaMain on Board b. 
	 */
	public void alphaBetaMove(Board b) { 
		Board temp = b.getCopy();
		ScoredMovePair botMove = alphabetaMain(temp, depth);
		botMove.movePair.printPair("BOT will move" );
//		b.makeMove(botMove.movePair.source, botMove.movePair.dest);
	}

	/**
	 * @param b
	 * @param depth 
	 * @return best ScoredMovePair on Board b returned from miniMax algorithm.
	 * MiniMax can be very time consuming for large depths, consider using AlphaBeta for large depths
	 * Uses Bot.miniMax for helper. 
	 * Could be re factored to not have a helper function 
	 */
	public ScoredMovePair miniMaxMain(Board b, int depth) {
		ScoredMovePair best = new ScoredMovePair();
		best.score = -100; // score preset to -100 because -100 means lost game, ideally best move has score > -100

		ArrayList<ScoredMovePair> allPairs = new ArrayList<ScoredMovePair>();

		for(MovePair mv : b.getAvailableMoves(color))
			mv.printPair("\t possible move pairs");
		// Bot.miniMax is called on all of Bot's available moves
			// this is equivalent to the second to top level of Game Tree. 
		for(MovePair mv : b.getAvailableMoves(color)) {
				mv.printPair("MAX");
			Board temp = b.getCopy();
			temp.makeMove(mv.source, mv.dest); 
			ScoredMovePair test = miniMax(depth, temp , mv,  false);
			allPairs.add(test);
			System.out.println("score = " + test.score);
			// resets best if test has has a better score
			System.out.printf("test.score %d vs best.score %d\n",test.score, best.score);
			if(test.score >= best.score) {
				best.score = test.score;
				best.movePair = test.movePair;
			}
		}
		for(ScoredMovePair smp : allPairs)
			smp.print("all");
		return best;
	}

	/**
	 * @param depth
	 * @param b
	 * @param movePair
	 * @param maxPlayer
	 * @return best ScoredMovePair
	 * recursive helper to miniMaxMain
	 * General Idea of MiniMax : Iterate through the tree of all moves. 
	 * 		Tree : Our moves, opponent's move from our moves, our moves from opponent's move from our moves, etc.
	 * When we reach leaf node of Tree, assign the Board that is pass a score from Bot.evaluateBoard()
	 * Moving upwards to the top level of the Tree, Minimizing player (opponent) moves in such a way that will minimize our score 
	 * while Maximizing Player (us) wants to play in such a way to maximize our score. 
	 * To compromise this, we will play in such a way to maximize what our opponent minimzes. 
	 * The logic of minimax iterates through the Tree of moves accordingly, however everything is upto Bot.evaluateBoard to give real 
	 * values of Board states for the leaf node's Board state.
	 */
	public ScoredMovePair miniMax(int depth, Board b,MovePair movePair, boolean maxPlayer) {

		ScoredMovePair best = new ScoredMovePair();
		best.movePair = movePair;

		// depth is 0 when we have reached a leaf node. 
		if(depth == 0) {
			Board temp = b.getCopy();
			temp.makeMove(movePair.source, movePair.dest);
			best.score = evaluateBoard(temp);
			return best;			
		}

		// This is for the Maximizing Player (Bot)
			// returns maximizing ScoredMovePair
		if (maxPlayer) {
			int bestMoveVal = -100;  //  - infinity ?
			for(MovePair mv : b.getAvailableMoves(color)) {
				Board temp = b.getCopy();
				mv.printPair("MAX ",depth, this.depth);
				temp.makeMove(mv.source, mv.dest);
				ScoredMovePair eval = miniMax(depth - 1, temp, mv, false);
				bestMoveVal = Math.max(bestMoveVal, eval.score); // Math.max because we want to MAXIMIZE what opponent MINIMIZES
				best.score = bestMoveVal;

				String tab = "";
				for(int i = 0; i < this.depth - depth + 2; i++)
					tab = "\t" + tab;
				System.out.println(tab + "for Max, best score is " + best.score);
			}
			return best;
		}
		// This is for Minimizing Player (opponent)
			// returns minimizing ScoredMovePair
		else { 
			int bestMoveVal = 100; //  + infinity ?
			for(MovePair mv : b.getAvailableMoves(3 - color)) {
				Board temp = b.getCopy();
				temp.makeMove(mv.source, mv.dest);
				mv.printPair("MIN ",depth, this.depth);
				ScoredMovePair eval = miniMax(depth - 1, temp, mv, true);
				bestMoveVal = Math.min(bestMoveVal, eval.score); // Math.min because opponent wants to MINIMIZE what we MAXIMIZE
				best.score = bestMoveVal;

				String tab = "";
				for(int i = 0; i < this.depth - depth + 2; i++)
					tab = "\t" + tab;
				System.out.println(tab + "for Min, best score is " + best.score);
			}
			return best;
		}
	}

	/**
	 * @param b
	 * @param depth
	 * @return best ScoredMovePair on Board b based on minimax algorithm with alphabeta pruning
	 * The alphabeta pruning makes this more efficient than minimax for larger depths
	 * Uses Bot.alphabeta as helper
	 * Could be re factored to not have a helper function
	 */
	public ScoredMovePair alphabetaMain(Board b, int depth) {
		ScoredMovePair best = new ScoredMovePair();
		best.movePair = null;
		best.score = -100;

		ArrayList<ScoredMovePair> allPairs = new ArrayList<ScoredMovePair>();
		ArrayList<MovePair> availableMovePairs = b.getAvailableMoves(color);

//		for(MovePair mv : availableMovePairs)
//			mv.printPair("possible move pairs");

		// Bot.alphabeta is called on all of Bot's available moves
			//this is equivalent to the second to top level of Game Tree. 
		for(MovePair mv : availableMovePairs) {
			mv.printPair("MAX");
			Board temp = b.getCopy();
			temp.makeMove(mv.source, mv.dest);
			ScoredMovePair test = alphabeta(depth, temp, mv, -100, 100, false); //  alpha = - inf ?, beta = + inf ?
			allPairs.add(test);
			System.out.println("score = " + test.score);
			// resets best if test has has a better score
			if(test.score > best.score) { 
				best.score = test.score;   
				best.movePair = test.movePair;
			}
		}
		for(ScoredMovePair smp : allPairs)
			smp.print("all"); 
		return best;
	}

	/**
	 * @param depth
	 * @param b
	 * @param movePair
	 * @param alpha
	 * @param beta
	 * @param maxPlayer
	 * @return best ScoredMovePair
	 * helper to Bot.alphabetaMain
	 * More useful information to understand alphabeta pruning at : http://web.stanford.edu/~msirota/soco/alphabeta.html
	 * The general idea of Alphabeta is exactly the same as Minimax however we traverse up the Tree more efficiently
	 * with the alpha and beta values. For example Alphabeta will detect if there are certain branches that it is unnecessary
	 * to traverse. Thus alphabeta is useful if Bot.depth is very high
	 */
	public ScoredMovePair alphabeta(int depth, Board b,MovePair movePair, int alpha, int beta, boolean maxPlayer) {

		ScoredMovePair best = new ScoredMovePair();
		best.movePair = movePair;

		if(depth == 0) {
			best.score = evaluateBoard(b);
			return best;
		}
		// This is for the Maximizing Player (Bot)
					// returns maximizing ScoredMovePair
		if(maxPlayer) {
			for(MovePair mv : b.getAvailableMoves(color)) {
				mv.printPair("MAX ",depth, this.depth);
				Board temp = b.getCopy();
				temp.makeMove(mv.source, mv.dest);
				ScoredMovePair eval = alphabeta(depth -1, temp, mv, alpha, beta, false);
				alpha = Math.max(alpha, eval.score); // Math.max because we want to MAXIMIZE what opponent MINIMIZES
				if(beta <= alpha) //beta cut off;
					break;
			}
			best.score = alpha;
			return best;
		}
		// This is for Minimizing Player (opponent)
					// returns minimizing ScoredMovePair
		else {
			for(MovePair mv : b.getAvailableMoves(3 - color)) {
				mv.printPair("MIN ", depth, this.depth);
				Board temp = b.getCopy();
				temp.makeMove(mv.source, mv.dest);
				ScoredMovePair eval = alphabeta(depth -1, temp, mv, alpha, beta, true);
				beta = Math.min(beta, eval.score); // Math.min because opponent wants to MINIMIZE what we MAXIMIZE
				if(beta <= alpha) // alpha cut off;
					break; 
			}
			best.score = beta;
			return best;
		}
	}	
}