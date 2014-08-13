import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.UIManager;
public class Main {
	
	static Board b;
	static Bot bot;
	static int botColor;
	static int depth;
	public static ArrayList<OpeningLine> openingLines;
	static BoardGUI bGUI;
	public static boolean isRohan;
	
	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);

		System.out.println("Rohan?");
		if(scan.next().equalsIgnoreCase("y"))
			isRohan = true;
		else
			isRohan = false;
		
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
		
		// Special condition to get the GUI properly working if on a MAC
		String osName = System.getProperty("os.name").toLowerCase();
		System.out.println(osName);
		if (osName.contains("mac")) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e) {
				System.out.println("Error setting Java LAF: " + e);
			}
		}
		
		bGUI = new BoardGUI(b);
		bGUI.start();
		
		bot = new Bot(botColor, depth, bGUI);
		
		if(bot.color == 1 && bGUI.playingWithBot)
			bot.move(b);
		
		// Enter in commands. 
		while(true) {
			parseCommand(scan.nextLine());
			if(b.isCheckMated()) 
				break;
		}
	}

	/**
	 * Fills openingLines from Openings.txt
	 */
	static void fillBook(){
		openingLines = new ArrayList<>();
		BufferedReader reader = null;
		try {
			 //dynamically finds openings.txt
			if(isRohan)
				reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/Openings.txt")); 
			else
				reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/Openings.txt"));
			
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
	
	/**
	 * @param command
	 * Runs commands on the Board. 
	 * For each possible command, the generic call is shown in the left most comment 
	 * while a description of the command follows below it. 
	 * 
	 * P = Piece distinguished by notation ("a2")
	 * n = any number
	 * s = number that represents a side / player color (1 or 2)
	 */
	static void parseCommand(String command) {

		try {
			
			// P 
				// print information on P. "a2" 
			if(command.length() == 2) 
				b.getPieceAt(command).printInfo("");
			
			// turn 
				// print whose turn it is. 
			else if(command.equals("turn"))
				System.out.println("Turn = " + b.turn);
			
			// set turn s
				// sets Board.turn value. 
			else if(command.contains("set turn")) {
				if(b.inDebugMode == false)
					System.out.println("can only set turn in debugmode!");
				else { 
					b.turn = Integer.parseInt(command.split(" ")[2]);
					System.out.println("turn is now " + b.turn);
				}	
			}
			else if(command.contains("debug")) {
				// debug on 
					//sets debug mode on
				if(command.split(" ")[1].equals("on")) {
					System.out.println("debug mode on");
					b.inDebugMode = true;
				}
				// debug off  
					// sets debug mode off
				else if(command.split(" ")[1].equals("off")){
					System.out.println("debug mode off");
					b.inDebugMode = false;
				}
				// debug status 
					// prints if in debug mode. 
				else if(command.split(" ")[1].equals("status"))
					System.out.println(b.inDebugMode);
			}
			// remove P. 
				// Removes P => "remove a2"  
			else if(command.contains("remove")) {
				if(b.inDebugMode == false) {
					System.out.println("can only remove when debug mode is true!");
				}
				else  {
					b.removePiece(command.split(" ")[1]);
				}
			}

			// move P1 P2
				// moves P1 to P2 if it is valid to move P1 to P2 
			else if(command.contains("move")) {
				String p1Coordinate = command.split(" ")[1];
				Piece source = b.getPieceAt(p1Coordinate);
				String p2Coordinate = command.split(" ")[2];
				Piece dest = b.getPieceAt(p2Coordinate);

				if(b.isValidMove(source, dest) && !b.doesMoveLeadToCheck(source, dest)) 
					b.makeMove(source, dest);
				
				b.printBoardContents();
			}
			// valid P1 P2
				// prints if it is valid to move P1 to P2 , "valid a2 a3"
			else if(command.contains("valid")) {
				String p1Coordinate = command.split(" ")[1];
				Piece p1 = b.getPieceAt(p1Coordinate);
				String p2Coordinate = command.split(" ")[2];
				Piece p2 = b.getPieceAt(p2Coordinate);

				if(b.isValidMove(p1, p2)) 
					System.out.println("valid move");
				else
					System.out.println("invalid move");
			}

			// available P
				// prints all available moves for P. "available a2"
			else if(command.contains("available")) {
				Piece source = b.getPieceAt(command.split(" ")[1]);
				for(Piece p : b.getAvailableMovesFor(source))
					p.printInfo("available for " + command.split(" ")[1]);
			}

			// possible s
				// prints all possible moves for side s. 
			else if(command.contains("possible")) { 
				for(MovePair mp : b.getAvailableMoves(Integer.parseInt(command.split(" ")[1])))
					mp.printPair("possible");
			}
			// all
				// prints information on ALL pieces on board.
			else if(command.equals("all")) {
				b.printInfoOnAllPieces("pls");
			}
			// pieces s
				// prints information that belong to side s. 
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

			// attacked P
				// prints if P is attacked, "attacked a2"
			else if(command.contains("attacked")) {
				Piece p = b.getPieceAt(command.split(" ")[1]);
				System.out.println(b.isAttacked(p, 3 - p.side));
			}

			// material s
				// prints material score of s, "material 1"
			else if(command.contains("material")) {
				int ccolor = Integer.parseInt(command.split(" ")[1]);
				if(ccolor == 1)
					System.out.printf("material score : %d\n", new Bot(ccolor,depth, bGUI).getMaterialScore(b));
				else
					System.out.printf("material score : %d\n", new Bot(ccolor,depth, bGUI).getMaterialScore(b));
			}
			// attack s
				// prints attack score of s, "attack 1"
			else if(command.contains("attack")) {
				int ccolor = Integer.parseInt(command.split(" ")[1]);
				if(ccolor == 1)
					System.out.printf("attack score : %d\n", new Bot(ccolor,depth, bGUI).getAttackScore(b));
				else
					System.out.printf("attack score : %d\n", new Bot(ccolor,depth, bGUI).getAttackScore(b));
			}
			// defense s
				// prints defense score of s, "defense 1"
			else if(command.contains("defense")) {
				int ccolor = Integer.parseInt(command.split(" ")[1]);
				if(ccolor == 1)
					System.out.printf("defense score : %d\n", new Bot(ccolor,depth, bGUI).getDefenseScore(b));
				else
					System.out.printf("defense score : %d\n", new Bot(ccolor,depth, bGUI).getDefenseScore(b));
			}
			
			// minimax
				// prints best move for Bot given by minimax algorithm. Does not make the move for bot. 
			else if(command.equals("minimax")) {
				Board temp = b.getCopy();
				ScoredMovePair best;
				best = bot.miniMaxMain(temp, depth);
				best.print("BEST");
			}
			// alphabeta
				// prints best move for Bot given by alphabeta algorithm. Does not make the move for bot. 
			else if(command.equals("alphabeta")) {
				Board temp = b.getCopy();
				ScoredMovePair best;
				best = bot.alphabetaMain(temp, depth);
				best.print("best");
			}
			// bot go
				// makes Bot's move based on openinglines or alphabeta.
			else if(command.equals("bot go")) {
				bot.move(b);
			}
			// set depth n
				// sets depth of Bot
			else if(command.contains("set depth")) {
				depth = Integer.parseInt(command.split(" ")[2]);
			}
			// set color n
				// sets color of Bot
			else if(command.contains("set color")){
				botColor = Integer.parseInt(command.split(" ")[2]);
			}
			// random
				// makes a random move on Board based on current Board.turn value. 
			else if(command.equals("random")) {
				ArrayList<MovePair> mvs = b.getAvailableMoves(b.turn);
				int randInt = new Random().nextInt(mvs.size());
				MovePair randomMove = mvs.get(randInt);
				b.makeMove(randomMove.source, randomMove.dest);
				b.printBoardContents();
//				b.playedMoveList.add(new MovePair(randomMove.source,randomMove.dest));
			}
			// P defended by
				// prints all Pieces that defend P. 
			else if(command.contains("defended by")) {
				for(Piece defender : b.getDefenders(b.getPieceAt(command.split(" ")[0])))
					defender.printInfo("defending!");
			}
			// quit
				// exits program
			else if(command.equals("quit"))
				System.exit(0);
			// check status
				// prints if King of current Board.turn is in check.
			else if(command.equals("check status")) {
				b.isChecked();
			}
			// list
				// prints all valid moves that have been played on the Board. 
			else if(command.equals("list")){
				for(MovePair mp : b.playedMoveList)
					System.out.printf("%s %s\n", mp.source.getAlgebraic(), mp.dest.getAlgebraic());
			}
			// aline P1 P2
				// shows all Pieces that are in the path of P1's attack to P2 
			else if(command.contains("aline")) {
				Piece attacker = b.getPieceAt(command.split(" ")[1]);
				Piece victim = b.getPieceAt(command.split(" ")[2]);
				for(Piece p : b.getAttackLinePieces(attacker, victim))
					p.printInfo("in attack line!");
			}
			// print board
			else if(command.equals("print board"))
				b.printBoardContents();
			}
		catch(Exception e) {
			System.out.printf("%s caused a %s\n",command, e.getMessage());
			e.printStackTrace();
		}
	}
}