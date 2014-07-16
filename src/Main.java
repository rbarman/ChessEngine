import java.util.Scanner;
public class Main {
	static Board b;
	static SimpleMiniMax miniMax;
	
	public static void main(String[] args) {
		b = new Board();
		b.setDefaultBoard();
		b.mapLocations();		
		b.printBoardContents();

		while(true) {
			if(b.isCheckMated()) 
				return;
			else
				parseCommand(new Scanner(System.in).nextLine());
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
			
			// possible moves 1
				// gets all possible moves for white. 
			else if(command.contains("possible")) { 
				for(MovePair mp : b.getAvailableMoves(Integer.parseInt(command.split(" ")[1])))
					mp.printPair("possible");
			}
				
			else if(command.equals("all")) {
				for(int i = 0; i < b.contents.length; i++) 
					for(int j = 0; j < b.contents[i].length; j++)
						b.getPieceAt(i, j).printInfo("pls");
			}
			
			else if(command.equals("material score")) {
				miniMax = new SimpleMiniMax(1);
				System.out.println("score : " + miniMax.getMaterialScore(b));
			}
			else if(command.equals("minimax")) {
				int botColor = 1;
				miniMax = new SimpleMiniMax(botColor);
				Board temp = b.getCopy();
				System.out.println(miniMax.miniMax(temp));
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
		}
	}
}
