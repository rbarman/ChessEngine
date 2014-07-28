
/**
 * @author Rohan
 * Piece is Class that represents the pieces (rook, pawn, queen ,etc) that players wil play on Board.
 * 
 */
public class Piece {
	char name;
		// r,b,k,q,p, etc
		// Uppercase is White and lowercase is Black
	int side;
		// 1 = white, 2 = black, 3 = empty
		// 0 is special for the edges.  
	int x;
	int y;
	boolean hasMoved;
	
	/**
	 * @param name
	 * Pieces that are made with this constructor will be playable on
	 * Side is possible to be 1,2, or 3
	 */
	public Piece(char name) {
		this.name = name;
		setSide();
		this.hasMoved = false;
	}
	
	/**
	 * @param name
	 * Edge Piece is made with this constructor
	 */
	public Piece(String name){
		this.name = name.charAt(0);
		this.side = 0;
	}
	
	/**
	 * @param name
	 * Edge Piece is made with this constructor
	 */
	public Piece (int name){
		if(name == 8)
			this.name = '8';
		if(name == 7)
			this.name = '7';
		if(name == 6)
			this.name = '6';
		if(name == 5)
			this.name = '5';
		if(name == 4)
			this.name = '4';
		if(name == 3)
			this.name = '3';
		if(name == 2)
			this.name = '2';
		if(name == 1)
			this.name = '1';
		this.side = 0;
	}
	
	/**
	 * sets the side of the Piece based on it's name field
	 * Called in new Piece (Char name). 
	 */
	public void setSide() {
		if(Character.isUpperCase(this.name))
			side = 1;
		else if(Character.isLowerCase(this.name))
			side = 2;
		else if(this.name == '.' || this.name == '-') // no significance for . or - atm
			side = 3;
	}
	 
	/**
	 * @return value of Piece
	 * only static values ATM
	 */
	public int getValue() {
		if(this.isKing()) return 100;
		if(this.isQueen()) return 9;
		if(this.isRook()) return 5;
		if(this.isBishop()) return 3;
		if(this.isKnight()) return 3;
		if(this.isPawn()) return 1;
		this.printInfo("unexpected in Piece.getValue()");
		return 0;
	}
	
	/**
	 * @return algebraic notation of a Piece. "a1", "c3",  etc
	 */
	public String getAlgebraic() {
		char algebraX = (char) ('a' + this.x - 1);
		int algebraY = 8 - this.y;
		return "" + algebraX + algebraY;
	}
	
	/**
	 * print's Piece's name
	 */
	public void print() {
		System.out.print(this.name);
	}
	
	/**
	 * @param tag : Debugging statement before complete print statement
	 * print's Piece's name, x, y, side, and if it has moved
	 * "TAG >> P @ (1,2) with side : 1 moved : false"
	 */
	public void printInfo(String tag) {
		System.out.printf("%s >> %s @ (%d,%d) %s with side : %d moved : %b\n",tag,this.name,this.x,this.y,this.getAlgebraic(),this.side,this.hasMoved);
	}
	
	/**
	 * @return if Piece's side is 3. 
	 * Side of 1 represents white and 2 represents black
	 */
	boolean isEmpty() {
		return this.side == 3;
	}
	
	/**
	 * @return if Piece is on edge
	 */
	boolean isEdge(){
		return this.side == 0;
	}
	
	boolean isKing(){
		return (this.name == 'K') || (this.name == 'k');
	}
	boolean isQueen() {
		return (this.name == 'Q') || (this.name == 'q');
	}
	boolean isPawn() {
		return (this.name == 'P') || (this.name == 'p');
	}
	boolean isRook() {
		return (this.name == 'R') || (this.name == 'r');
	}
	boolean isBishop() {
		return (this.name == 'B') || (this.name == 'b');
	}
	boolean isKnight() {
		return (this.name == 'N') || (this.name == 'n');
	}
}
