
public class MovePair {
	Piece source;
	Piece dest;
	int score;
	
	public MovePair(Piece source, Piece dest) {
		this.source = source;
		this.dest = dest;
	}
	
	public void printPair(String tag) { 	
		System.out.printf("%s  %s @ (%d,%d) %s PAIRED with %s @ (%d,%d) %s \n",
				tag, this.source.name, this.source.x, this.source.y, this.source.getAlgebraic(),
				this.dest.name, this.dest.x,this.dest.y, this.dest.getAlgebraic());
	}
	
	public void printPair(String tag, int depth, int maxDepth) {
		String tab = "";
		for(int i = 0; i < maxDepth - depth + 1; i++)
			tab = "\t" + tab;
		printPair(tab + tag);
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void copy(MovePair copy) {
		this.source.x = copy.source.x;
		this.source.y = copy.source.y;
		this.dest.x = copy.dest.x;
		this.dest.y = copy.dest.y;
	}
}
