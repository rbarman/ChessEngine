import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class Main {
	static Board b;
	static Bot bot;
	static int botColor;
	static int depth;
//	static ArrayList<MovePair> moveList;
	static ArrayList<OpeningLine> openingLines;

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
//		moveList = new ArrayList<MovePair>();

		System.out.println("BotColor: "); 
		botColor = scan.nextInt();
		System.out.println("Depth: ");
		depth = scan.nextInt();
		System.out.printf("botColor : %d \t depth : %d\n", botColor, depth);
		fillBook();

		b = new Board();
		b.setDefaultBoard();
		b.mapLocations();		
		b.printBoardContents();

		bot = new Bot(botColor, depth, openingLines);
		// "move bot" OR "move __ to __"
		// bot move vs self move

		while(true) {
			if(b.isCheckMated()) 
				return;
			else
				parseCommand(scan.nextLine());
		}
	}

	static void fillBook(){
		openingLines = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/Users/Rohan/Development/Chess/src/Openings.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] lineSplit = line.split("-");
            	openingLines.add(new OpeningLine(lineSplit[0].trim(), lineSplit[1].trim()));
			}
		} catch (IOException e) {
			e.printStackTrace();
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

			// move a1 a2
			else if(command.contains("move")) {
				String p1Coordinate = command.split(" ")[1];
				Piece p1 = b.getPieceAt(p1Coordinate);
				String p2Coordinate = command.split(" ")[2];
				Piece p2 = b.getPieceAt(p2Coordinate);

				if(b.isValidMove(p1, p2)) {
					b.makeMove(p1, p2);
					b.playedMoveList.add(new MovePair(p1,p2));
				}
				b.printBoardContents();
			}
			// valid a1 to a2
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
			// pieces 1
			else if (command.contains("pieces")) {
				int color = Integer.parseInt(command.split(" ")[1]);
				for(int i = 0; i < b.contents.length; i++){
					for(int j = 0; j < b.contents[i].length; j++) {
						Piece p = b.getPieceAt(i, j);
						if(p.side == color)
							p.printInfo("piece");
					}
				}
			}

			// attacked a1
			else if(command.contains("attacked")) {
				Piece p = b.getPieceAt(command.split(" ")[1]);
				System.out.println(b.isAttacked(p, 3 - p.side));
			}

			// material 1
			else if(command.contains("material")) {
				int ccolor = Integer.parseInt(command.split(" ")[1]);
				if(ccolor == 1)
					System.out.printf("material score : %d\n", new Bot(ccolor,depth, openingLines).getMaterialScore(b));
				else
					System.out.printf("material score : %d\n", new Bot(ccolor,depth, openingLines).getMaterialScore(b));
			}
			// attack 1
			else if(command.contains("attack")) {
				int ccolor = Integer.parseInt(command.split(" ")[1]);
				if(ccolor == 1)
					System.out.printf("attack score : %d\n", new Bot(ccolor,depth, openingLines).getAttackScore(b));
				else
					System.out.printf("attack score : %d\n", new Bot(ccolor,depth, openingLines).getAttackScore(b));
			}
			// defense 1
			else if(command.contains("defense")) {
				int ccolor = Integer.parseInt(command.split(" ")[1]);
				if(ccolor == 1)
					System.out.printf("defense score : %d\n", new Bot(ccolor,depth, openingLines).getDefenseScore(b));
				else
					System.out.printf("defense score : %d\n", new Bot(ccolor,depth, openingLines).getDefenseScore(b));
			}

			else if(command.equals("minimax")) {
				Board temp = b.getCopy();
				ScoredMovePair best;
				best = bot.miniMaxMain(temp, depth);
				best.print("BEST");
			}

			else if(command.equals("alphabeta")) {
				Board temp = b.getCopy();
				ScoredMovePair best;
				best = bot.alphabetaMain(temp, depth);
				best.print("best");
			}
			else if(command.equals("bot go")) {
				bot.move(b);
			}
			else if(command.contains("set depth")) {
				depth = Integer.parseInt(command.split(" ")[2]);
			}
			else if(command.contains("set color")){
				botColor = Integer.parseInt(command.split(" ")[2]);
			}

			else if(command.equals("random")) {
				ArrayList<MovePair> mvs = b.getAvailableMoves(b.turn);
				int randInt = new Random().nextInt(mvs.size());
				MovePair randomMove = mvs.get(randInt);
				b.makeMove(randomMove.source, randomMove.dest);
				b.printBoardContents();
				b.playedMoveList.add(new MovePair(randomMove.source,randomMove.dest));
			}
			// a1 defended by
			else if(command.contains("defended by")) {
				for(Piece defender : b.isDefendedBy(b.getPieceAt(command.split(" ")[0])))
					defender.printInfo("defending!");
			}
			else if(command.equals("quit"))
				System.exit(0);
			else if(command.equals("check status")) {
				b.isChecked();
			}
			else if(command.equals("list")){
				for(MovePair mp : b.playedMoveList)
					System.out.printf("%s %s\n", mp.source.getAlgebraic(), mp.dest.getAlgebraic());
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