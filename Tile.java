

import javax.swing.JButton;

public class Tile extends JButton {
	private char letter;
	private boolean dl = false;
	private boolean tl = false;
	private boolean dw = false;
	private boolean tw = false;
	
	public Tile(char letter) {
		this.letter = letter;
	}
	
	public char getLetter() {
		return letter;
	}
	public void setLetter(char letter) {
		this.letter = letter;
	}
	public void setDL() {
		dl = true;
	}
	public void setTL() {
		tl = true;
	}
	public void setDW() {
		dw = true;
	}
	public void setTW() {
		tw = true;
	}
	public boolean isDL() {
		return dl;
	}
	public boolean isTL() {
		return tl;
	}
	public boolean isDW() {
		return dw;
	}
	public boolean isTW() {
		return tw;
	}
	public int getValue() {
		int value = Ruzzle.getValue(getLetter());
		if (dl) value = value*2;
		else if (tl) value = value*3;
		return value;
	}
}
