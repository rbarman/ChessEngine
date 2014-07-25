
public class Piece {
	char name;
	int side;
		// 1 = white, 2 = black, 3 = empty
		// 0 is special for the edges.  
	int x;
	int y;
	boolean hasMoved;
	
	public Piece(char name) {
		this.name = name;
		setSide();
		this.hasMoved = false;
	}
	public Piece(String name){
		this.name = name.charAt(0);
		this.side = 0;
	}
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
	
	public void setSide() {
		if(Character.isUpperCase(this.name))
			side = 1;
		else if(Character.isLowerCase(this.name))
			side = 2;
		else if(this.name == '.' || this.name == '-') 
			side = 3;
	}
	
	// only static values ATM
		// pawn's value will change based on how far up it is + if stacked or not + if defended ?? 
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
	
	// returns the algebraic notation of a piece -> a1, etc
	public String getAlgebraic() {
		char algebraX = (char) ('a' + this.x - 1);
		int algebraY = 8 - this.y;
		return "" + algebraX + algebraY;
	}
	
	public void print() {
		System.out.print(this.name);
	}
	
	public void printInfo(String tag) {
		System.out.printf("%s >> %s @ (%d,%d) with side : %d moved : %b\n",tag,this.name,this.x,this.y,this.side,this.hasMoved);
	}
	
	boolean isEmpty() {
		return this.side == 3;
	}
	
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
