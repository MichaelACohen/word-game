

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;

public class Ruzzle {
	
	//width and height of board
	private static final int WIDTH = 4;
	private static final int HEIGHT = 4;
	
	private static final int BOARD_WIDTH = 600;
	
	private static final int GAP_SIZE = 15;
	private static final int TOP_SIZE = 100;
	
	public static Tile[][] buttons = new Tile[WIDTH][HEIGHT];
	
	private static JButton scoreBut = new JButton();
	private static JButton wordBut = new JButton();
	private static JButton timer = new JButton();
	private final static Board frame = new Board("Ruzzle");
	
	//number of minutes game lasts
	private static final int MINUTES = 1; 
	private static int time;
	private static String word = " ";
	
	public static ArrayList<JButton> curWord = new ArrayList<JButton>();
	public static ArrayList<String> wordList = new ArrayList<String>();
	public static ArrayList<Integer> scoreList = new ArrayList<Integer>();
	
	private static boolean gameOver = false;
	
	public static void setUpJComponents(char[] chars, int[] bonuses) {
		frame.setSize(BOARD_WIDTH,BOARD_WIDTH+TOP_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		JPanel panel = new JPanel(new GridLayout(HEIGHT,WIDTH));
		
		panel.setLayout(null);
		frame.add(panel);
		
		int index;
		//for each tile on board
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				index = WIDTH*j+i;
				
				buttons[i][j] = new Tile(chars[index]);
				buttons[i][j].setSize((frame.getContentPane().getWidth() - (WIDTH-1)*GAP_SIZE)/WIDTH, ((frame.getContentPane().getHeight()-TOP_SIZE) - (HEIGHT-1)*GAP_SIZE)/HEIGHT);
				buttons[i][j].setLocation(i*(buttons[i][j].getWidth()+GAP_SIZE), TOP_SIZE + j*(buttons[i][j].getHeight()+GAP_SIZE));
				buttons[i][j].setLayout(new BorderLayout());
						
				//write the value of each character on the button
				JLabel label = new JLabel(String.valueOf(buttons[i][j].getValue()));
				buttons[i][j].add(BorderLayout.SOUTH,label);
				buttons[i][j].setFont(new Font("",0,buttons[i][j].getHeight()/3));
				
				//write letter in center of button
				buttons[i][j].setText(String.valueOf(buttons[i][j].getLetter()));
						
				buttons[i][j].setBorder(new LineBorder(Color.BLACK, 2));
				
				if (bonuses[0] == index) {
					makeBonus(index, "DL");
				} else if (bonuses[1] == index) {
					makeBonus(index, "TL");
				} else if (bonuses[2] == index) {
					makeBonus(index, "DW");
				} else if (bonuses[3] == index) {
					makeBonus(index, "TW");
				}
				panel.add(buttons[i][j]);
			}
		}
		
		//make timer button
		timer = new JButton();
		timer.setSize((frame.getContentPane().getWidth()-2*GAP_SIZE)/4, TOP_SIZE/2);
		timer.setLocation(0, TOP_SIZE/4);
		timer.setText(drawTime());
		panel.add(timer);
		
		//make word button
		wordBut.setSize((frame.getContentPane().getWidth()-2*GAP_SIZE)/2,TOP_SIZE/2);
		wordBut.setLocation(timer.getWidth()+GAP_SIZE,TOP_SIZE/4);
		wordBut.setFont(new Font("",0,wordBut.getWidth()/16));
		wordBut.setText(word);	
		panel.add(wordBut);
		
		//make score button
		scoreBut.setSize((frame.getContentPane().getWidth()-2*GAP_SIZE)/4, TOP_SIZE/2);
		scoreBut.setLocation(timer.getWidth() + wordBut.getWidth() + 2*GAP_SIZE, TOP_SIZE/4);
		scoreBut.setText(String.valueOf(0));
		panel.add(scoreBut);
	}

	//randomly decide which tiles are going to have bonuses (1 DL, 1 TL, 1 DW, 1 TW)
	private static int[] makeBonuses() {
		Random rand = new Random();
		int[] bonuses = new int[4];
		
		int dl = rand.nextInt(WIDTH*HEIGHT);
		bonuses[0] = dl;
		
		int tl = dl;
		while (tl == dl) {
			tl = rand.nextInt(WIDTH*HEIGHT);
		}
		bonuses[1] = tl;
		
		int dw = tl;
		while (dw == tl || dw == dl) {
			dw = rand.nextInt(WIDTH*HEIGHT);
		}
		bonuses[2] = dw;
		
		int tw = dw;
		while (tw == dw || tw == tl || tw == dl) {
			tw = rand.nextInt(WIDTH*HEIGHT);
		}
		bonuses[3] = tw;
		
		return bonuses;
	}
	private static void makeBonus(int index, String bonus) {
		switch (bonus) {
			case "DL" :
				buttons[index%WIDTH][index/WIDTH].setDL();
				buttons[index%WIDTH][index/WIDTH].setBorder(new LineBorder(Color.GREEN,2));
				break;
			case "TL" :
				buttons[index%WIDTH][index/WIDTH].setTL();
				buttons[index%WIDTH][index/WIDTH].setBorder(new LineBorder(Color.BLUE,2));
				break;
			case "DW" :
				buttons[index%WIDTH][index/WIDTH].setDW();
				buttons[index%WIDTH][index/WIDTH].setBorder(new LineBorder(Color.ORANGE,2));
				break;
			case "TW" :
				buttons[index%WIDTH][index/WIDTH].setTW();
				buttons[index%WIDTH][index/WIDTH].setBorder(new LineBorder(Color.RED,2));
				break;
		}
		JLabel label = new JLabel(bonus);
		buttons[index%WIDTH][index/WIDTH].add(BorderLayout.NORTH, label);
	}

	private static String drawTime() {
		int min = 0, sec = 0;
		String s = "";
		if (time == 0) {
			min = MINUTES;
		} else {
			int count = 0;
			for (int i = 60; i <= MINUTES*60; i = i+60) {
				if (time <= i) {
					min = MINUTES-count-1; sec = i-time; 
					break;
				}
				count++;
			}
		}
		if (sec < 10) {
			s = min + ":0" + sec;
		}
		else {
			s = min + ":" + sec;
		}
		return s;
	}

	//list of characters for board
	public static char[] makeChars() {
		char[] s = new char[WIDTH*HEIGHT];
		Random rand = new Random();
		for (int i = 0; i < s.length; i++) {
			int r = rand.nextInt(100);
			if (r < 8) s[i] = 'A';
			else if (r < 16) s[i] = 'E';
			else if (r < 24) s[i] = 'I';
			else if (r < 32) s[i] = 'O';
			else if (r < 38) s[i] = 'U';
			else if (r < 46) s[i] = 'R';
			else if (r < 54) s[i] = 'S';
			else if (r < 62) s[i] = 'T';
			else if (r < 68) s[i] = 'N';
			else if (r < 71) s[i] = 'B';
			else if (r < 74) s[i] = 'C';
			else if (r < 77) s[i] = 'D';
			else if (r < 80) s[i] = 'F';
			else if (r < 83) s[i] = 'G';
			else if (r < 86) s[i] = 'H';
			else if (r < 87) s[i] = 'K';
			else if (r < 91) s[i] = 'L';
			else if (r < 94) s[i] = 'M';
			else if (r < 97) s[i] = 'P';
			else if (r < 98) s[i] = 'V';
			else if (r < 99) s[i] = 'W';
			else if (r < 100) s[i] = 'Y';
		}
		return s;
	}
	public static int getValue(char letter) {
		switch (letter) {
			case 'A' : 
				return 1;
			case 'B' : 
				return 4;
			case 'C' : 
				return 4;
			case 'D' :
				return 2;
			case 'E' :
				return 1;
			case 'F' :
				return 4;
			case 'G' : 
				return 3;
			case 'H' :
				return 4;
			case 'I' :
				return 1;
			case 'J' :
				return 8;		
			case 'K' :
				return 5;
			case 'L' : 
				return 1;
			case 'M' : 
				return 3;
			case 'N' : 
				return 1;
			case 'O' :
				return 1;
			case 'P' : 
				return 4;
			case 'Q' : 
				return 8;
			case 'R' : 
				return 1;
			case 'S' : 
				return 1;
			case 'T' : 
				return 1;
			case 'U' : 
				return 2;
			case 'V' :
				return 4;
			case 'W' : 
				return 4;
			case 'X' :
				return 8;
			case 'Y' :
				return 4;
			case 'Z' :
				return 10;
			default :
				return -1;
		}
	}
	
	public static Trie getDictionary(String filename) {
		Trie trie = new Trie();
		try {
			URL url = new URL("http://www.puzzlers.org/pub/wordlists/pocket.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = br.readLine()) != null) {
				trie.addString(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return trie;
	}
	
	//return coordinates on board of button that was pressed
	public static Point findCoord(JButton pressedButton) {
		Point p = null;
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (pressedButton == buttons[i][j]) {
					p = new Point(i,j);
				}
			}
		}
		return p;
	}

	//returns ArrayList of adjacent indices to "index"
	public static ArrayList<Integer> findAdjacent(int index) {
		ArrayList<Integer> adj = new ArrayList<Integer>();
		
		if (index < WIDTH*HEIGHT) {
		
			int x = index%WIDTH;
			int y = index/WIDTH;
		
			int newX;
			int newY;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (!(i == 0 && j == 0)) {
						newX = x+i;
						newY = y+j;
					
						if (!(newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT)) {
							adj.add(newX + newY*WIDTH);
						}
					}
				}
			}
		}
		return adj;
	}
	public static void main(String[] args) {
		//http://www.cs.middlebury.edu/~huang/MLExAI/words
		final Trie trie = getDictionary("C:\\Users\\Michael\\Documents\\dictionary.txt");
		final char[] chars = makeChars();
		final int[] bonuses = makeBonuses();
		setUpJComponents(chars, bonuses);
		
		//taskPerformer calls actionPerformed method every second
		int delay = 1000; //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!gameOver) {
					time++;
					timer.setText(drawTime());
					timer.repaint();
					//when two minutes have passed, game is over
					if (time == 60*MINUTES) {
						gameOver = true;
						for (int i = 0; i < WIDTH; i++) {
							for (int j = 0; j < HEIGHT; j++) {
								buttons[i][j].setBackground(null);
							}
						}
						wordBut.setText("");
						//remove lines from board if a word was in progress
						curWord.clear();
						frame.repaint();
						
						trie.findWords(chars, bonuses);

						ArrayList<String> allWords = Trie.allWords;
						ArrayList<Integer> allScores = Trie.allScores;
											
						new EndScreen(frame,wordList,scoreList, allWords, allScores);
						frame.validate();
					}
				}
			}
		};
		new Timer(delay, taskPerformer).start();
		
		class RuzzleListener extends MouseInputAdapter {
			//x and y represents location of last button pressed in buttons 2D array
			private Point lastButton;
			private String word = "";
			private int wordScore;
			private int dw;
			private int tw;
			
			public void mousePressed(MouseEvent e) {
				if (!gameOver) {
					JButton pressedButton = (JButton) e.getSource();
					//p is coordinate of button on board (0 <= p.x < WIDTH && 0 <= p.y < HEIGHT)
					Point p = findCoord(pressedButton);
					if (buttons[p.x][p.y].isDW()) dw++;
					if (buttons[p.x][p.y].isTW()) tw++;
					//change color of background
					buttons[p.x][p.y].setBackground(Color.yellow);
					//remember position of button
					lastButton = p;
					//set word equal to letter of clicked button
					word = pressedButton.getText();
					//update word button
					wordBut.setText(word);
					//update wordScore to take value of letter
					wordScore = buttons[p.x][p.y].getValue();
					curWord.add(pressedButton);
				}
			}
			public void mouseDragged(MouseEvent e) {
				if (!gameOver) {
					JButton pressedButton = (JButton) e.getSource();
					Point source = findCoord(pressedButton);
					Point cur = e.getPoint();
					Point curButton = isOnButton(source,cur);
					if (curButton != null && !curWord.contains(buttons[curButton.x][curButton.y])) {
						if (buttons[curButton.x][curButton.y].isDW()) dw++;
						if (buttons[curButton.x][curButton.y].isTW()) tw++;
						//change background color
						buttons[curButton.x][curButton.y].setBackground(Color.yellow);
						lastButton = curButton;
						String letter = buttons[curButton.x][curButton.y].getText();
						word = word + letter;
						wordBut.setText(word);
						wordScore = wordScore + buttons[curButton.x][curButton.y].getValue();
						curWord.add(buttons[curButton.x][curButton.y]);
						frame.repaint();
					}	
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (!gameOver) {
					//check if in dict
					boolean isWord = trie.isWord(word.toLowerCase());
					//if in dict and hasn't already been used
					if (isWord && !wordList.contains(word)) {
						if (curWord.size() > 4) {
							wordScore += (curWord.size()-4)*5;
						}
						int mult = 1;
						if (dw != 0) mult = mult*2*dw;
						if (tw != 0) mult = mult*3*tw;
						scoreBut.setText(String.valueOf(Integer.parseInt(scoreBut.getText()) + mult*wordScore));
						wordList.add(word);
						scoreList.add(mult*wordScore);
						wordBut.setText("+" + mult*wordScore + " points!");
					//if it isn't a word
					} else if (!isWord) {
						wordBut.setText("Invalid word!");
					//if already used the word
					} else {
						wordBut.setText("Already used that word!");
					}
					for (JButton button : curWord) {
						button.setBackground(null);
					}
					wordScore = 0;
					dw = tw = 0;
					curWord.clear();
					word = "";
					frame.repaint();
				}
			}
			public void mouseMoved(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			
			private Point isOnButton(Point source, Point cur) {
				int buttonWidth = buttons[lastButton.x][lastButton.y].getWidth();
				int buttonHeight = buttons[lastButton.x][lastButton.y].getHeight();

				int xDiff = lastButton.x - source.x;
				int yDiff = lastButton.y - source.y;
				//(pixel) coordinates of lastButton relative to source
				Point lastButtonCoords = new Point(xDiff*(buttonWidth+GAP_SIZE), yDiff*(buttonHeight+GAP_SIZE));
				
				int xCoord = 0;
				int yCoord = 0;
				//if cursor isn't within either same or adj column to lastButton
				if (cur.x - lastButtonCoords.x < -(buttonWidth+GAP_SIZE) || cur.x - lastButtonCoords.x > 2*buttonWidth+GAP_SIZE ||
					cur.y - lastButtonCoords.y < -(buttonHeight+GAP_SIZE) || (cur.y - lastButtonCoords.y > 2*buttonHeight+GAP_SIZE)) {
					return null;
				} else {
					//if cursor is in a gap
					if ((cur.x - lastButtonCoords.x < 0 && cur.x - lastButtonCoords.x > -GAP_SIZE) || 
						(cur.x - lastButtonCoords.x < buttonWidth+GAP_SIZE && cur.x - lastButtonCoords.x > buttonWidth) ||
						(cur.y - lastButtonCoords.y < 0 && cur.y - lastButtonCoords.y > -GAP_SIZE) ||
						(cur.y - lastButtonCoords.y < buttonHeight+GAP_SIZE && cur.y - lastButtonCoords.y > buttonHeight)) {
						return null;
					} else {
						//record xCoord of button
						if (cur.x - lastButtonCoords.x < 0) xCoord = lastButton.x - 1;
						else if (cur.x - lastButtonCoords.x <= buttonWidth) xCoord = lastButton.x;
						else xCoord = lastButton.x + 1;
						if (cur.y - lastButtonCoords.y < 0) yCoord = lastButton.y - 1;
						else if (cur.y - lastButtonCoords.y <= buttonHeight) yCoord = lastButton.y;
						else yCoord = lastButton.y + 1;
					}
				}
				//avoid index out of bounds exception
				if (xCoord < 0 || xCoord >= WIDTH || yCoord < 0 || yCoord >= HEIGHT) return null;
				return new Point(xCoord, yCoord);
			}
		}
		RuzzleListener listener = new RuzzleListener();
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				buttons[i][j].addMouseListener(listener);
				buttons[i][j].addMouseMotionListener(listener);
			}
		}
	}
}
