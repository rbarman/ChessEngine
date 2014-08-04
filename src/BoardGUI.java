import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Adam Ran simultaneously with the bot/user input. A basic chess board
 *         with inspiration taken from an online swing tutorial.
 */

public class BoardGUI extends Thread implements MouseListener  {
	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
	private JButton[][] chessBoardSquares = new JButton[8][8];
	private JPanel chessBoard;
	private static final String COLS = "ABCDEFGH";

	private String[][] BoardPosition = new String[8][8];

	BoardGUI() {
		initializeGui();
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	@SuppressWarnings("unused")
	protected ImageIcon createImageIcon(String piece, String description) {
		String imgURL = System.getProperty("user.dir") + "/src/Openings.txt";
		if (imgURL != null) {
			return new ImageIcon(
					"E:\\eclipse\\chess\\ChessEngine\\src\\assets\\" + piece,
					description);
		} else {
			System.err.println("Couldn't find file: " + piece);
			return null;
		}
	}

	public final void initializeGui() {
		// set up the main GUI
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		gui.add(tools, BorderLayout.PAGE_START);

		chessBoard = new JPanel(new GridLayout(0, 9));
		chessBoard.setBorder(new LineBorder(Color.BLACK));
		gui.add(chessBoard);
		
		// create the chess board squares
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i = chessBoardSquares.length - 1; i >= 0; i--) {
			for (int j = 0; j < chessBoardSquares[i].length; j++) {
				System.out.println("i: " + i + "j: " + j);
				JButton b = new JButton();
				b.addMouseListener(this);
				b.setMargin(buttonMargin);
				// our chess pieces are 64x64 px in size, so we'll
				// 'fill this in' using a transparent icon..
				ImageIcon icon = new ImageIcon(new BufferedImage(64, 64,
						BufferedImage.TYPE_INT_ARGB));

				String temp = "";

				if (j == 1) {
					icon = createImageIcon("0_pawn.png", temp = "White Pawn");
				} else if (j == 6) {
					icon = createImageIcon("1_pawn.png", temp = "Black Pawn");
				} else if (j == 0) {
					if (i == 0 || i == 7) {
						icon = createImageIcon("0_rook.png",
								temp = "White Rook");
					} else if (i == 1 || i == 6) {
						icon = createImageIcon("0_knight.png",
								temp = "White Knight");
					} else if (i == 2 || i == 5) {
						icon = createImageIcon("0_bishop.png",
								temp = "White Bishop");
					} else if (i == 4) {
						icon = createImageIcon("0_queen.png",
								temp = "White Queen");
					} else if (i == 3) {
						icon = createImageIcon("0_king.png",
								temp = "White King");
					}
				} else if (j == 7) {
					if (i == 0 || i == 7) {
						icon = createImageIcon("1_rook.png",
								temp = "Black Rook");
					} else if (i == 1 || i == 6) {
						icon = createImageIcon("1_knight.png",
								temp = "Black Knight");
					} else if (i == 2 || i == 5) {
						icon = createImageIcon("1_bishop.png",
								temp = "Black Bishop");
					} else if (i == 4) {
						icon = createImageIcon("1_queen.png",
								temp = "Black Queen");
					} else if (i == 3) {
						icon = createImageIcon("1_king.png",
								temp = "Black King");
					}
				}

				b.setIcon(icon);
				if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
					b.setBackground(Color.WHITE);
				} else {
					b.setBackground(Color.GRAY);
				}
				chessBoardSquares[i][7 - j] = b;
				BoardPosition[i][j] = temp;
			}
		}

		// fill the chess board
		chessBoard.add(new JLabel(""));
		// fill top
		for (int i = 0; i < 8; i++) {
			chessBoard.add(new JLabel(COLS.substring(i, i + 1),
					SwingConstants.CENTER));
		}
		// fill the black non-pawn piece row
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				switch (j) {
				case 0:
					chessBoard.add(new JLabel("" + (8 - i),
							SwingConstants.CENTER));
				default:
					chessBoard.add(chessBoardSquares[j][i]);
				}
			}
		}
	}

	public final JComponent getChessBoard() {
		return chessBoard;
	}

	public final JComponent getGui() {
		return gui;
	}

	public void run() {
		BoardGUI cb = this;
		JFrame f = new JFrame("Chess");
		f.add(cb.getGui());
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLocationByPlatform(true);
		// ensures the frame is the minimum size it needs to be
		// in order display the components within it
		f.pack();
		// ensures the minimum size is enforced.
		f.setMinimumSize(f.getSize());
		f.setVisible(true);
		System.out.println("[0][0]: " + BoardPosition[4][0]);
	}

	 public void mousePressed(MouseEvent e) {
		 //System.out.println("Mouse pressed; # of clicks: "
	                   // + e.getClickCount());
	    }

	    public void mouseReleased(MouseEvent e) {
	    	System.out.println("Mouse released; # of clicks: "
	                    + e.getClickCount());
	    	String button = e.getComponent().toString();
	    	System.out.println("Button: " + button);
	    }

	    public void mouseEntered(MouseEvent e) {
	    	//System.out.println("Mouse entered");
	    }

	    public void mouseExited(MouseEvent e) {
	    	//System.out.println("Mouse exited");
	    }

	    public void mouseClicked(MouseEvent e) {
	       //System.out.println("Mouse clicked (# of clicks: "
	                   // + e.getClickCount());
	    }

}
