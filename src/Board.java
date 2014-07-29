import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Rohan
 * Board is the Class that represents the Chess Board that Players will play on. 
 *  The contents of the Board (squares) is represented as a 2 dimensional Piece array.
 */
public class Board {

	Piece[][] contents = new Piece[9][9];
	int turn = 1; 	// 1 -> white, 2 -> black.
	boolean inDebugMode = false; 
	int moveCount = 0;	// number of plies that have been played.
	ArrayList<MovePair> playedMoveList = new ArrayList<MovePair>();
	
	/**
	 * Prints Board.contents
	 */
	void printBoardContents() {
		for (int i = 0; i < contents.length; i++) {
			for (int j = 0; j < contents[i].length; j++) {
				System.out.print("|");
				contents[i][j].print();
				System.out.print("| ");
			}
			System.out.println("\n");
		}
	}
	
	/**
	 * Sets the x and y member fields of all Piece objects currently on the board. 
	 * Objects in an array are not aware of its relative location, so this method is critical.
	 * mapLocations() must be called anytime a Piece object is moved. For example, this is called extensively in Board.mapLocations(); 
	 */
	void mapLocations() {
		for (int i = 0; i < contents.length; i++)
			for (int j = 0; j < contents[i].length; j++) {
				contents[i][j].x = j;
				contents[i][j].y = i;
			}
	}

	/**
	 * sets Board.contents to be the standard board at the start of game. 
	 */
	void setDefaultBoard() {
		Piece[][] bs = {
				{ new Piece(8), new Piece('r'), new Piece('n'), new Piece('b'),
						new Piece('q'), new Piece('k'), new Piece('b'),
						new Piece('n'), new Piece('r') },
				{ new Piece(7), new Piece('p'), new Piece('p'), new Piece('p'),
						new Piece('p'), new Piece('p'), new Piece('p'),
						new Piece('p'), new Piece('p') },
				{ new Piece(6), new Piece('.'), new Piece('-'), new Piece('.'),
						new Piece('-'), new Piece('.'), new Piece('-'),
						new Piece('.'), new Piece('-') },
				{ new Piece(5), new Piece('-'), new Piece('.'), new Piece('-'),
						new Piece('.'), new Piece('-'), new Piece('.'),
						new Piece('-'), new Piece('.') },
				{ new Piece(4), new Piece('.'), new Piece('-'), new Piece('.'),
						new Piece('-'), new Piece('.'), new Piece('-'),
						new Piece('.'), new Piece('-') },
				{ new Piece(3), new Piece('-'), new Piece('.'), new Piece('-'),
						new Piece('.'), new Piece('-'), new Piece('.'),
						new Piece('-'), new Piece('.') },
				{ new Piece(2), new Piece('P'), new Piece('P'), new Piece('P'),
						new Piece('P'), new Piece('P'), new Piece('P'),
						new Piece('P'), new Piece('P') },
				{ new Piece(1), new Piece('R'), new Piece('N'), new Piece('B'),
						new Piece('Q'), new Piece('K'), new Piece('B'),
						new Piece('N'), new Piece('R') },
				{ new Piece("*"), new Piece("a"), new Piece("b"),
						new Piece("c"), new Piece("d"), new Piece("e"),
						new Piece("f"), new Piece("g"), new Piece("h") } };
		contents = bs;
	}

	/**
	 * @param x
	 * @param y
	 * @return Piece at respective location in Board.contents
	 * (0,1) refers to (1,0) of array notation.
	 */
	Piece getPieceAt(int x, int y) {
		return contents[y][x];
	}

	/**
	 * @param str : Algebraic notation of a piece : "c1" , "d8" etc
	 * @return Piece at that location. 
	 */
	Piece getPieceAt(String str) {
		int y = Integer.parseInt(str.substring(1, 2));
		int x = str.charAt(0) - 97 + 1;
		return contents[8 - y][x];
	}

	/**
	 * @return Piece that represents the King based on the turn value. 
	 * If turn is 1 , returns White King.
	 * Could be refactored to get king based on int color paramter.
	 */
	Piece getKing() {
		char flag;
		if(turn == 1)
			flag = 'K';
		else
			flag = 'k';
		
		for(int i = 0; i < contents.length; i++) {
			for(int j = 0; j < contents[i].length; j++) {
				if(contents[i][j].name == flag)
					return contents[i][j];
			}
		}
		System.out.println("unexpeted in getting king! Turn was : " + turn );
		// should not get here....
		return new Piece('.');
	}
	
	/**
	 * @param side : 1 = White , 2 = Black
	 * @return All Pieces of the parameter color that are on Board.contents. 
	 */
	ArrayList<Piece>getPiecesOfColor(int side) {
		
		ArrayList<Piece>pieces = new ArrayList<Piece>();
		for(int i = 0; i < contents.length; i++) {
			for(int j = 0; j < contents[i].length; j++) {
				if(contents[i][j].side == side)
					pieces.add(contents[i][j]);
			}
		}
		return pieces;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return if square at x,y is actually on Board.contents
	 * 	(0,1) refers to (1,0) of array notation.
	 */
	boolean isValidSquare(int x, int y) {
		if (y < 0 || y > 7)
			return false;
		if (x < 1 || x > 8)
			return false;
		return true;
	}
	
	/**
	 * @param p : Piece to check
	 * @param oppColor : Opponent's color
	 * @return True or False if the Piece P is attacked by opponent Pieces
	 */
	boolean isAttacked(Piece p, int oppColor) {
		if(inDebugMode == true)
			return false;
		for(Piece a : getPiecesOfColor(oppColor))
			if(isValidMove(a, p)) {
				System.out.printf("%s @ (%d,%d) is attacked by %s @ (%d,%d)\n", p.name, p.x, p.y,a.name,a.x,a.y);
				return true;
			}
		return false;
	}
	
	/**
	 * @param p : Piece to check
	 * @param oppColor : : Opponent's color
	 * @return All opponent Pieces that attack Piece p.
	 * If returned list is empty, there are no Pieces that currently attack P. 
	 */
	ArrayList<Piece> isAttackedBy(Piece p, int oppColor) {
//		if(inDebugMode == true)
//			return null;
		ArrayList<Piece>attackers = new ArrayList<Piece>();
		for(Piece a : getPiecesOfColor(oppColor))
			if(isValidMove(a, p)) {
//				System.out.printf("%s @ (%d,%d) is attacked by %s @ (%d,%d)\n", p.name, p.x, p.y,a.name,a.x,a.y);
				attackers.add(a);
			}
		return attackers;
	}
	
	ArrayList<Piece> isDefendedBy(Piece p) {
		ArrayList<Piece>defenders = new ArrayList<Piece>();
		p.side = 3 - p.side;
		for(Piece d : getPiecesOfColor(3 - p.side)) {
			if(isValidMove(d, p))
				defenders.add(d);
		}
		p.side = 3 - p.side;
		return defenders;
	}
	
	
	/**
	 * @param king : king Piece
	 * @return list of Pieces where the King COULD move to. 
	 * The only validation that goes on in here is with Board.isValidSquare()
	 */
	ArrayList<Piece> getKingSurroundingPeces(Piece king) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
	
		Piece p;
		if(isValidSquare(king.x, king.y + 1)) {
			 p = getPieceAt(king.x, king.y + 1);
			if(p.side != king.side)
				pieces.add(p);
		}
		if(isValidSquare(king.x, king.y - 1)) {
			p = getPieceAt(king.x, king.y - 1);
			if(p.side != king.side)
				pieces.add(p);
		}
			
		if(isValidSquare(king.x + 1, king.y + 1)) {
			p = getPieceAt(king.x + 1, king.y + 1);
			if(p.side != king.side)
				pieces.add(p);
		}
		if(isValidSquare(king.x + 1, king.y - 1)) {
			p = getPieceAt(king.x + 1, king.y - 1);
			if(p.side != king.side)
				pieces.add(p);
		}
		
		if(isValidSquare(king.x - 1, king.y + 1)) {
			p = getPieceAt(king.x - 1, king.y + 1);
			if(p.side != king.side)
				pieces.add(p);
		}
		if(isValidSquare(king.x - 1, king.y - 1)) {
			p = getPieceAt(king.x - 1, king.y - 1);
			if(p.side != king.side)
				pieces.add(p);
		}
		
		if(isValidSquare(king.x + 1, king.y)) {
			p = getPieceAt(king.x + 1, king.y);
			if(p.side != king.side)
				pieces.add(p);
		}
		if(isValidSquare(king.x - 1, king.y)) {
			p = getPieceAt(king.x - 1, king.y);
			if(p.side != king.side)
				pieces.add(p);
		}
		return pieces;
	}
	
	
	/**
	 * @return if King Piece based on current Board.turn value is CheckMated. 
	 * Could be re-factored to to have int color paramamter to not rely on Board.turn value. 
	 */
	boolean isCheckMated() {
		if(!isChecked())
			return false;
		else {  
			Piece king = getKing();
			int oppColor = 3 - king.side;
			// checks for squares that the king can move to to avoid being in check.
				// if there is a safe square, king is not in checkmat, return false.
			for(Piece p : getKingSurroundingPeces(king)) 
				if(!isAttacked(p, oppColor))
					return false;
			
			// if the king can not move to safety then possibly the same colored pieces can move 
			// in such a way to avoid the king from being in check. 
				// sudo version of makeMove(), check if in check.
			Piece[][] prevBoardState = new Piece[9][9];
			for(int i = 0; i <contents.length; i++)
				  for(int j=0; j<contents[i].length; j++)
					  prevBoardState[i][j]=contents[i][j];
			
			for(Piece source : getPiecesOfColor(king.side)) {
				for(Piece dest : getAvailableMovesFor(source)) {
					contents[dest.y][dest.x] = source;
					contents[source.y][source.x] = new Piece('.');
					if(isChecked()) {
						//reset contents for next iteration.
						for(int i = 0; i <prevBoardState.length; i++)
							  for(int j=0; j<prevBoardState[i].length; j++)
								  contents[i][j]=prevBoardState[i][j];
					}
					else  {
						// there is such a move to not put king in check, return false. 
						for(int i = 0; i <prevBoardState.length; i++)
							  for(int j=0; j<prevBoardState[i].length; j++)
								  contents[i][j]=prevBoardState[i][j];
						return false;
					}
				}
			}
			king.printInfo("IS CHECKMATED!");
			return true;	
		}		
	}
	
	/**
	 * @return if King Piece based on current Board.turn value is Checked. 
	 * Could be re-factored to to have int color paramamter to not rely on Board.turn value. 
	 */
	boolean isChecked() {
		Piece king = getKing();
		int oppColor = 3 - king.side;
		
		if(isAttacked(king,oppColor)) 
			return true;
		else 
//			System.out.println("king is not in check!");
			return false;
	}
	
	/**
	 * @param source : Piece we are moving from
	 * @param dest : Piece we are moving to
	 * @return if it is valid to move from Source to Dest. 
	 * Calls respective isValid helpers based on Source.name
	 */
	boolean isValidMove(Piece source, Piece dest) {
		//dest.printInfo("validating to...");
		
		if (inDebugMode)
			return true;
		// checks if source is not an empty piece.
		if (source.side == 3) {
//			System.out.println("you have selected an empty piece!");
			return false;
		}
		if(source.side == 0) {
//			System.out.println("edge piece");
			return false;
		}
		if(dest.side == 0) {
//			System.out.println("dest is edge piece");
			return false;
		}
		if(dest.isEdge())
			return false;
	
		// checks if right color piece is played given current turn.
//		if (source.side != turn) {
//			System.out.println("turn is " + turn
//					+ "\nyou have selected the wrong color!");
//			return false;
//		}
		// checks if piece does not move anywhere
		if ((getHorizontalDiff(source, dest) == 0)
				&& (getVerticalDiff(source, dest) == 0)) {
//			System.out
//					.println("you must move the piece somewhere different from its starting spot");
			return false;
		}
		// checks if source is not eating dest of same color
		if (source.side == dest.side) {
//			System.out.println("can not attack your own pieces!");
			return false;
		}

		// Now that general validation is passed
		// Check validation for each piece. 
		if (source.isBishop())
			return isValidBishopMove(source, dest);
		else if (source.isRook())
			return isValidRookMove(source, dest);
		else if (source.isQueen()) 
			return isValidQueenMove(source, dest);
		else if (source.isKnight()) 
			return isValidKnightMove(source, dest);
		else if (source.isPawn())
			return isValidPawnMove(source, dest);
		else if (source.isKing()) 
			return isValidKingMove(source, dest);
		else if (source.isEmpty()) {
			System.out.println("you have selected an en empty piece!");
			return false;
		}
		System.out.println("unexpted validateMove");
		return false;
	}

	boolean isValidKingMove(Piece king, Piece dest) {
//		System.out.println("validating king move");
		
		int horizontalDiff = getHorizontalDiff(king, dest);
		int verticalDiff = getVerticalDiff(king, dest);
		if(Math.abs(horizontalDiff) == 1 && verticalDiff == 0)
			return true;
		else if(Math.abs(verticalDiff) == 1 && horizontalDiff == 0)
			return true;
		else if(Math.abs(horizontalDiff) == 2 && verticalDiff == 0)
			return kingCanCastle(king, dest);
//		System.out.printf("invalid king move @ (%d,%d)\n", dest.x,
//				dest.y);
		return false;
	}
	
	boolean isValidKnightMove(Piece knight, Piece dest) {
		
//		System.out.println("validating knight move");
		
		int horizontalDiff = getHorizontalDiff(knight, dest);
		int verticalDiff = getVerticalDiff(knight, dest);
		
		if(horizontalDiff == 2 && verticalDiff == 1)
	        return true;
	    if(horizontalDiff == 2 && verticalDiff == -1)
	        return true;
	    if(horizontalDiff == -2 && verticalDiff == 1)
	        return true;
	    if(horizontalDiff == -2 && verticalDiff == -1)
	        return true;
	    
	    if(horizontalDiff == 1 && verticalDiff == 2)
	        return true;
	    if(horizontalDiff == 1 && verticalDiff == -2)
	        return true;
	    if(horizontalDiff == -1 && verticalDiff == 2)
	        return true;
	    if(horizontalDiff == -1 && verticalDiff == -2)
	        return true;
//		System.out.printf("invalid knight move @ (%d,%d)\n", dest.x,
//				dest.y);
		return false;
	}
	
	boolean isValidWhitePawnMove(Piece source, Piece dest) {
//		System.out.println("\tvalidating white pawn move");
		int horizontalDiff = getHorizontalDiff(source, dest);
		int verticalDiff = getVerticalDiff(source, dest);
		
		if (verticalDiff == -1) {
			if(Math.abs(horizontalDiff) == 1 && dest.side == 3 - source.side)
				return true;
			if(horizontalDiff == 0 && dest.isEmpty())
				return true;
		} else if (verticalDiff == -2 && source.y == 6 && horizontalDiff == 0 && dest.isEmpty())
			return true;

//		System.out.println("invalid white pawn move");
		return false;
	}

	boolean isValidBlackPawnMove(Piece source, Piece dest) {
		int horizontalDiff = getHorizontalDiff(source, dest);
		int verticalDiff = getVerticalDiff(source, dest);
		
		if (verticalDiff == 1) {
			if(Math.abs(horizontalDiff) == 1 && dest.side == 3 - source.side)
				return true;
			if(horizontalDiff == 0 && dest.isEmpty())
				return true;
		} else if (verticalDiff == 2 && source.y == 1 && horizontalDiff == 0 && dest.isEmpty())
			return true;
//		System.out.println("invalid black pawn move");
		return false;
	}

	boolean isValidPawnMove(Piece source, Piece dest) {
//		System.out.println("validating pawn move");
		if (source.side == 1)
			return isValidWhitePawnMove(source, dest);
		else if (source.side == 2)
			return isValidBlackPawnMove(source, dest);
		return false;
	}

	boolean isValidBishopMove(Piece source, Piece dest) {
		int horizontalDiff = getHorizontalDiff(source, dest);
		int verticalDiff = getVerticalDiff(source, dest);
		// bishop can only move diagonally.
		if (Math.abs(horizontalDiff) != Math.abs(verticalDiff)) {
//			System.out.println("bishop can only move diagonally!");
			return false;
		}

		if (horizontalDiff < 0 && verticalDiff < 0) {
			// left up diagonal.
			int idx = source.y - 1;
			for (int i = source.x - 1; i > dest.x; i--) {
				Piece p = getPieceAt(i, idx--);
				// p.printInfo();
				if (!p.isEmpty()) {
//					System.out.printf("invalid bishop move 1 @ (%d,%d)\n", p.x,
//							p.y);
					return false;
				}
			}
		} else if (horizontalDiff < 0 && verticalDiff > 0) {
			// left down diagonal.
			int idx = source.y + 1;
			for (int i = source.x - 1; i > dest.x; i--) {
				Piece p = getPieceAt(i, idx++);
				// p.printInfo();
				if (!p.isEmpty()) {
//					System.out.printf("invalid bishop move 2 @ (%d,%d)\n", p.x,
//							p.y);
					return false;
				}
			}
		} else if (horizontalDiff > 0 && verticalDiff < 0) {
			// right up diagonal.
			int idx = source.y - 1;
			for (int i = source.x + 1; i < dest.x; i++) {
				Piece p = getPieceAt(i, idx--);
				// p.printInfo();
				if (!p.isEmpty()) {
//					System.out.printf("invalid bishop move 3 @ (%d,%d)\n", p.x,
//							p.y);
					return false;
				}
			}
		} else if (horizontalDiff > 0 && verticalDiff > 0) {
			// right down diagonal.
			int idx = source.y + 1;
			for (int i = source.x + 1; i < dest.x; i++) {
				Piece p = getPieceAt(i, idx++);
				// p.printInfo;
				if (!p.isEmpty()) {
//					System.out.printf("invalid bishop move 4 @ (%d,%d)\n", p.x,
//							p.y);
					return false;
				}
			}
		}
		return true;
	}

	boolean isValidQueenMove(Piece source, Piece dest) {
//		System.out.println("validating queen move...");
		if (isValidRookMove(source, dest))
			return true;
		else 
			return isValidBishopMove(source, dest);
	}
	
	boolean isValidRookMove(Piece source, Piece dest) {
//		System.out.println("validating rook move");
		// rook can only move horizontally.

		int horizontalDiff = getHorizontalDiff(source, dest);
		int verticalDiff = getVerticalDiff(source, dest);
//		System.out.printf("horiz : %d\t vert : %d\n", horizontalDiff,
//				verticalDiff);

		if (horizontalDiff != 0 && verticalDiff == 0) {
			// rook is moving vertically
			if (horizontalDiff < 0) {
				// moving left, x is decreasing.
//				System.out.println("moving left");
				for (int i = source.x - 1; i > dest.x; i--) {
					Piece p = getPieceAt(i, source.y);
//					p.printInfo();
					if (!p.isEmpty()) {
//						System.out.printf("invalid rook move 1 @ (%d,%d)\n",
//								p.x, p.y);
						return false;
					}
				}
			} else {
				// moving right, x is increasing
//				System.out.println("moving right");

				for (int i = source.x + 1; i < dest.x; i++) {
					Piece p = getPieceAt(i, source.y);
//					p.printInfo();
					if (!p.isEmpty()) {
//						System.out.printf("invalid rook move 2 @ (%d,%d)\n",
//								p.x, p.y);
						return false;
					}
				}
			}
			return true;
		} else if (horizontalDiff == 0 && verticalDiff != 0) {
			// rook is moving horizontally
			if (verticalDiff < 0) {
				// moving up, y is decreasing.
//				System.out.println("moving up");
				for (int i = source.y - 1; i > dest.y; i--) {
					Piece p = getPieceAt(source.x, i);
//					p.printInfo();
					if (!p.isEmpty()) {
//						System.out.printf("invalid rook move 3 @ (%d,%d)\n",
//								p.x, p.y);
						return false;
					}
				}
			} else {
				// moving down, y is increasing.
//				System.out.println("moving down");
				for (int i = source.y + 1; i < dest.y; i++) {
					Piece p = getPieceAt(source.x, i);
//					p.printInfo();
					if (!p.isEmpty()) {
//						System.out.printf("invalid rook move 4 @ (%d,%d)\n",
//								p.x, p.y);
						return false;
					}
				}
			}
			return true;
		} else {
//			System.out.println("unexpted in validateRookMove with... ");
//			source.printInfo();
			return false;
		}
	}

	/**
	 * @param king : Piece king we want to castle
	 * @param dest : Piece that King will move to
	 * King will castle on Board.contents
	 */
	void castleKing(Piece king, Piece dest) {
		System.out.println("in castleKing");
//		king.printInfo();
//		System.out.printf("dest \t");
//		dest.printInfo();
		int hDiff = getHorizontalDiff(king, dest);
		if(king.side == 1) {
			if(hDiff == 2) {
				// king side castle
				System.out.println("king side castle");
				contents[dest.y][dest.x] = new Piece(king.name);
				contents[king.y][king.x] = new Piece('.');
				contents[7][8] = new Piece('.');
				contents[7][6] = new Piece('R');
			}
			else if(hDiff == -2) {
				// queen side castle
				System.out.println("queen side castle");
				contents[dest.y][dest.x] = new Piece(king.name);
				contents[king.y][king.x] = new Piece('.');
				contents[7][4] = new Piece('R');
				contents[7][1] = new Piece('.');
			}
		}
		else if(king.side == 2) {
			if(hDiff == 2) {
				System.out.println("king side castle");
				contents[dest.y][dest.x] = new Piece(king.name);
				contents[king.y][king.x] = new Piece('.');
				contents[0][6] = new Piece('r');
				contents[0][8] = new Piece('.');
			}
			else if(hDiff == -2) {
				System.out.println("queen side castle");
				contents[dest.y][dest.x] = new Piece(king.name);
				contents[king.y][king.x] = new Piece('.');
				contents[0][4] = new Piece('R');
				contents[0][1] = new Piece('.');
			}
		}
		mapLocations();
	}
	
	/**
	 * @param pawn : Piece pawn we want to promote
	 * @param dest : Piece that Pawn will move to
	 * pawn will be promoted to Queen on Board.contents
	 */
	void promotePawn(Piece pawn, Piece dest) {
//		System.out.println("in promotePawn");
		if(pawn.side == 1) 
			contents[dest.y][dest.x] = new Piece('Q');
		else if(pawn.side == 2) 
			contents[dest.y][dest.x] = new Piece('q');
		contents[pawn.y][pawn.x] = new Piece('.');
		mapLocations();
	}
	
	/**
	 * @param source : Piece that will move
	 * @param dest : Piece that source will move to 
	 * Source is moved to Dest on Board.contents as long as player is not put in check
	 */
	void makeMove(Piece source, Piece dest) {
		
		Piece[][] prevBoardState = new Piece[9][9];
		for(int i = 0; i <contents.length; i++)
			  for(int j=0; j<contents[i].length; j++)
				  prevBoardState[i][j]=contents[i][j];
		
//		if(isCheckMated()){
//			System.out.println("king is checkmated!");
//			System.exit(0);
//		}

		int kingCastledFlag = 0;
		if(source.isKing() && inDebugMode == false) {
				
			if(kingCanCastle(source, dest))	{
				System.out.println("will castle the king....");
				castleKing(source, dest);
				kingCastledFlag = 1;
			}
		}	
		
		else if(source.isPawn() && pawnCanPromote(source)) {
			promotePawn(source, dest);
		}
		else {
			// other general move, not castle or pawn promotion.
			Piece tempSource = new Piece(source.name);
			// tempSource magically works. -> had a 'massive' bug without it.... why?
			contents[dest.y][dest.x] = tempSource;
			contents[source.y][source.x] = new Piece('.');
		}
		
		if(inDebugMode == true) {
//			printBoardContents();
			return;
		}
		
		if(!isChecked()) { 
			contents[dest.y][dest.x].hasMoved = true;
			
			if(kingCastledFlag == 1) {
				System.out.println("kingCastledFlag == 1");
				if(contents[dest.y][dest.x - 1].isRook()) {
					contents[dest.y][dest.x - 1].hasMoved = true;
				}
				else if(contents[dest.y][dest.x + 1].isRook()) {
					contents[dest.y][dest.x + 1].hasMoved = true;
				}
			}
//			printBoardContents();
			turn = 3 - turn;
			moveCount++;
		}
		else { // king is in check, so put contents back to prevBoardState.
			System.out.println("king checked");
			
			for(int i=0; i<prevBoardState.length; i++)
				  for(int j=0; j<prevBoardState[i].length; j++)
					  contents[i][j]=prevBoardState[i][j];
			
			mapLocations();
//			printBoardContents();
		}
		mapLocations();
	}		

	/**
	 * @param str : Algebraic notation. "c1" "c2" etc
	 * removes Piece at str from Board.contents
	 * Should only be called when Board.inDebugMode is true
	 */
	void removePiece(String str) {
		Piece p = getPieceAt(str);
		contents[p.y][p.x] = new Piece('.');
		mapLocations();
		printBoardContents();
	}
	
	/**
	 * @param pawn : Piece that will be checked
	 * @return if pawn can be promoted
	 */
	boolean pawnCanPromote(Piece pawn) {
//		System.out.println("in canPromotePawn");
		if(pawn.side == 1 && pawn.y == 1)  {
//			System.out.println("white pawn");
				return true;
		}
		else if(pawn.side == 2 && pawn.y == 6) {
//			System.out.println("black pawn");
			return true;
		}
//		System.out.println("end of canPromotePawn");
		return false;
	}
	
	/**
	 * @param king : Piece that represents King
	 * @param dest : Where king wants to castle to
	 * @return if King can castle towards dest
	 */
	boolean kingCanCastle(Piece king, Piece dest) {
		if(inDebugMode)
			return true;
//		System.out.println("in kingCanCastle");
		int oppColor = 3 - king.side;
		int horizontalDiff = getHorizontalDiff(king, dest);
		int verticalDiff = getVerticalDiff(king, dest);
		
		if (king.hasMoved)
			return false;
		
		if(Math.abs(horizontalDiff) == 2 && verticalDiff == 0) {
			// king side castle
			if(horizontalDiff == 2 ) {
					if(king.side == 1) {
						Piece p = getPieceAt("h1");
//						p.printInfo("kingCanCastle");
						if(p.isRook() && (p.hasMoved == false)){
							Piece p1 = getPieceAt("f1");
							Piece p2 = getPieceAt("g1");
							if(p1.isEmpty() && p2.isEmpty()) {
								if(!isAttacked(p1, oppColor) && !isAttacked(p2,oppColor))
									return true;
							}
						}
					}
					else if(king.side == 2) {
						Piece p = getPieceAt("h8");
//						p.printInfo("kingCanCastle");
						if(p.isRook() && (p.hasMoved == false)){
							Piece p1 = getPieceAt("f8");
							Piece p2 = getPieceAt("g8");
							if(p1.isEmpty() && p2.isEmpty()) {
								if(!isAttacked(p1, oppColor) && !isAttacked(p2,oppColor))
									return true;
							}
						}	
					}
			}
			// queen side castle. 
			else if(horizontalDiff == -2) {
				if(king.side == 1) {
					Piece p = getPieceAt("a1");
//					p.printInfo("kingCanCastle");
					if(p.isRook() && (p.hasMoved == false)){
						Piece p1 = getPieceAt("b1");
						Piece p2 = getPieceAt("c1");
						Piece p3 = getPieceAt("d1");
						if(p1.isEmpty() && p2.isEmpty() && p3.isEmpty()) 
							if(!isAttacked(p1, oppColor) && !isAttacked(p2,oppColor) && !isAttacked(p3, oppColor))
								return true;
					}
				}
				else if(king.side == 2) {
					Piece p = getPieceAt("a1");
//					p.printInfo("kingCanCastle");
					if(p.isRook() && (p.hasMoved == false)){
						Piece p1 = getPieceAt("b8");
						Piece p2 = getPieceAt("c8");
						Piece p3 = getPieceAt("d8");
						if(p1.isEmpty() && p2.isEmpty() && p3.isEmpty()) 
							if(!isAttacked(p1, oppColor) && !isAttacked(p2,oppColor) && !isAttacked(p3, oppColor))
								return true;
					}
				}
			}
		}
//		System.out.println("at end of kingCanCastle");
		return false;
	}
	
	// returns all legal MovePair (s) for a color.
	/**
	 * @param color : Color we want to look at
	 * @return list of MovePairs that are available for player based on color. 
	 */
	ArrayList<MovePair> getAvailableMoves(int color) {
		ArrayList<MovePair> pairs = new ArrayList<MovePair>();
		for(Piece source : getPiecesOfColor(color)) {
			for(Piece p : getAvailableMovesFor(source))
				pairs.add(new MovePair(source, p));
		}
		return pairs;
	}
	
	/**
	 * @param source : Piece we check
	 * @return all Piece (squares) that source can legally move to
	 */
	ArrayList<Piece> getAvailableMovesFor(Piece source) {
		ArrayList<Piece> availableMoves = new ArrayList<Piece>();
		for(int i = 0; i < contents.length;i++){
			for(int j = 0; j < contents[i].length; j++) {
				Piece dest = getPieceAt(i, j);
				if(isValidMove(source, dest)) 
					availableMoves.add(dest);				
			}	
		}
		
		return availableMoves;
	}
	
	int getHorizontalDiff(Piece source, Piece dest) {
		return dest.x - source.x;
	}
																												
	int getVerticalDiff(Piece source, Piece dest) {
		return dest.y - source.y;
	}
	
	/**
	 * @return Board with it's contents to be a copy of this.contents
	 */
	public Board getCopy() {
		Board copy = new Board();
		for(int i = 0; i < this.contents.length; i++)
			copy.contents[i] = Arrays.copyOf(this.contents[i], this.contents[i].length);
		return copy;	
	}
	
	/**
	 * @param tag : statement passed to Piece.printInfo()
	 * Prints information of all pieces on Board.contents
	 */
	void printInfoOnAllPieces(String tag){
		for(int i = 0; i < this.contents.length; i++) 
			for(int j = 0; j < this.contents[i].length; j++)
				this.getPieceAt(i, j).printInfo(tag);
	}
	
}
