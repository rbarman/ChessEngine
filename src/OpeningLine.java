
/**
 * @author Rohan
 * OpeningLine is a simple Abstraction to hold a line's variation and it's name together. 
 * All OpeningLines come from Openings.txt 
 * Uses : Board.openingMove() and Main.fillBook(); 
 */
public class OpeningLine {
	String name; // name of Opening
	String line; // move line based on the specific variation. 
	public OpeningLine(String name, String line) {
		this.name = name;
		this.line = line;
	}
	
	public void printLine(){
		System.out.printf("%s \t %s\n", name, line);
	}
}
