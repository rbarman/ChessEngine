
public class OpeningLine {
	String name;
	String line;
	public OpeningLine(String name, String line) {
		this.name = name;
		this.line = line;
	}
	
	public void printLine(){
		System.out.printf("%s \t %s\n", name, line);
	}
}
