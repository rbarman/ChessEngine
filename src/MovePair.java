
public class MovePair {
	Piece source;
	Piece dest;
	int score;
	
	public MovePair(Piece source, Piece dest) {
		this.source = source;
		this.dest = dest;
	}
	
	public void printPair(String tag) { 	
		System.out.printf("%s >>  %s @ (%d,%d) PAIRED with %s @ (%d,%d) \n",
				tag, this.source.name, this.source.x, this.source.y,
				this.dest.name, this.dest.x,this.dest.y);
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
}
