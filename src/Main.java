import java.util.Scanner;
public class Main {
	static Board b;
	static int botColor;
	static int depth;
	
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		botColor = scan.nextInt();
		depth = scan.nextInt();
		System.out.printf("botColor : %d \t depth : %d", botColor, depth);
		
		b = new Board();
		b.setDefaultBoard();
		b.mapLocations();		
		b.printBoardContents();
		
		// "move bot" OR "move __ to __"
		// bot move vs self move
		
		while(true) {
			if(b.isCheckMated()) 
				return;
			else
				parseCommand(scan.nextLine());
		}
	}
	
	static void parseCommand(String command) {
		
		
		try {	
			// g2
			if(command.length() == 2) 
				b.getPieceAt(command).printInfo("");
			// whose turn?
			else if(command.equals("turn"))
				System.out.println("Turn = " + b.turn);
			else if(command.contains("set turn")) {
				if(b.inDebugMode == false)
					System.out.println("can only set turn in debugmode!");
				else { 
					b.turn = Integer.parseInt(command.split(" ")[2]);
					System.out.println("turn is now " + b.turn);
				}
					
			}
			// debug shit.
			else if(command.contains("debug")) {
				if(command.split(" ")[1].equals("on")) {
					System.out.println("debug mode on");
					b.inDebugMode = true;
				}
				else if(command.split(" ")[1].equals("off")){
					System.out.println("debug mode off");
					b.inDebugMode = false;
				}
				else if(command.split(" ")[1].equals("status"))
					System.out.println(b.inDebugMode);
			}
			
			else if(command.contains("remove")) {
				if(b.inDebugMode == false) {
					System.out.println("can only remove when debug mode is true!");
				}
				else  {
					b.removePiece(command.split(" ")[1]);
				}
			}
			
			// move a1 to a2
			else if(command.contains("move")) {
				String p1Coordinate = command.split(" ")[1];
				Piece p1 = b.getPieceAt(p1Coordinate);
				String p2Coordinate = command.split(" ")[3];
				Piece p2 = b.getPieceAt(p2Coordinate);
				
				if(b.isValidMove(p1, p2)) {
					b.makeMove(p1, p2);
				}
				b.printBoardContents();
			}
			else if(command.contains("valid")) {
				String p1Coordinate = command.split(" ")[1];
				Piece p1 = b.getPieceAt(p1Coordinate);
				String p2Coordinate = command.split(" ")[3];
				Piece p2 = b.getPieceAt(p2Coordinate);
				
				if(b.isValidMove(p1, p2)) 
					System.out.println("valid move");
				else
					System.out.println("invalid move");
			}
			
			else if(command.contains("surround")) {
				Piece king = b.getPieceAt(command.split(" ")[1]);
				king.printInfo("king");
				for(Piece p : b.getKingSurroundingPeces(king)) 
					p.printInfo("");
			}
			
			// available a1
			else if(command.contains("available")) {
				Piece source = b.getPieceAt(command.split(" ")[1]);
				for(Piece p : b.getAvailableMovesFor(source))
					p.printInfo("available for " + command.split(" ")[1]);
			}
			
			// possible 1
				// gets all possible moves for white. 
			else if(command.contains("possible")) { 
				for(MovePair mp : b.getAvailableMoves(Integer.parseInt(command.split(" ")[1])))
					mp.printPair("possible");
			}
				
			else if(command.equals("all")) {
				b.printInfoOnAllPieces("pls");
			}
			
//			else if(command.equals("material score")) {
//				System.out.println("score : " + bot.getMaterialScore(b));
//			}

			else if(command.equals("minimax")) {
				Board temp = b.getCopy();
				int miniMax;
				if(botColor == 1)
					miniMax = new WhiteBot(botColor,depth).miniMax(depth,temp, true);
				else
					miniMax = new BlackBot(botColor,depth).miniMax(depth,temp, true);
				System.out.println(miniMax);	
			}
			
			else if(command.equals("alphabeta")) {
				Board temp = b.getCopy();
				int alphabeta = 0;
				if(botColor == 1)
					alphabeta = new WhiteBot(botColor, depth).alphabeta(depth, temp, 0, 0, true);
				else
					alphabeta = new BlackBot(botColor, depth).alphabeta(depth, temp, 0, 0, true);
				System.out.println(alphabeta);
			}
			
			else if(command.equals("alphabeta test")) {
				Board temp = b.getCopy();
				ScoredMovePair best;
				if(botColor == 1)
					best = new WhiteBot(botColor, depth).alphabetaMain(temp, depth);
				else
					best = new BlackBot(botColor, depth).alphabetaMain(temp, depth);
				best.print("best");
			}
			else if(command.equals("move bot")) {
				Board temp = b.getCopy();
				if(botColor == 1)
					new WhiteBot(botColor, depth).move(temp);
				else
					new BlackBot(botColor, depth).move(temp);
			}
			
			else if(command.equals("quit"))
				System.exit(0);
			else if(command.equals("check status")) {
				b.isChecked();
			}
			else if(command.equals("print board"))
				b.printBoardContents();
			}
		catch(Exception e) {
			System.out.printf("%s caused a %s\n",command, e.getMessage());
			e.printStackTrace();
		}
	}
}
