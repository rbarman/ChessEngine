
public class WhiteBot extends Bot
{
	public WhiteBot(int color, int depth) {
		super(color, depth);
	}
	
	public void move(Board b){
		if(b.moveCount < openingMoveCount + 1)
			openingMove(b);
		else
			alphaBetaMove(b);
	}
	
	public void openingMove(Board b){
		
	}
}
