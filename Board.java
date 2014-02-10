

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Board extends JFrame {
	
	public Board(String title) {
		super(title);
	}
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paint(g);
		for (int i = 0; i < Ruzzle.curWord.size()-1; i++) {
			//constants to account for title bar and side windows on jframe
			int xConst = 8, yConst = 30;
			Point[] line = drawLine(Ruzzle.findCoord(Ruzzle.curWord.get(i)), Ruzzle.findCoord(Ruzzle.curWord.get(i+1)));
			g2.drawLine(line[0].x + xConst, line[0].y + yConst, line[1].x + xConst, line[1].y + yConst);
		}
	}
	//returns Point array with size 2:
	//index 0 = start point of line 
	//index 1 = end point of line
	private Point[] drawLine(Point lastButton, Point curButton) {
		//x,y value of starting pt for line
		int startX = 0, startY = 0;
		int finalX = 0, finalY = 0;
		
		JButton last = Ruzzle.buttons[lastButton.x][lastButton.y];
		JButton cur = Ruzzle.buttons[curButton.x][curButton.y];
		
		//if moving to the left
		if (curButton.x < lastButton.x) {
			startX = last.getLocation().x;
			///up-left
			if (curButton.y < lastButton.y) {
				startY = last.getLocation().y;
				finalX = cur.getLocation().x + cur.getWidth();
				finalY = cur.getLocation().y + cur.getHeight();
			//left
			} else if (curButton.y == lastButton.y) {
				startY = last.getLocation().y + last.getHeight()/2;
				finalX = cur.getLocation().x + cur.getWidth();
				finalY = cur.getLocation().y + cur.getHeight()/2;
			//down-left
			} else {
				startY = last.getLocation().y + last.getHeight();
				finalX = cur.getLocation().x + cur.getWidth();
				finalY = cur.getLocation().y;
			}
		//same column
		} else if (curButton.x == lastButton.x) {
			startX = last.getLocation().x + last.getWidth()/2;
			//up
			if (curButton.y < lastButton.y) {
				startY = last.getLocation().y;
				finalX = cur.getLocation().x + cur.getWidth()/2;
				finalY = cur.getLocation().y + cur.getHeight();
			//down
			} else {
				startY = last.getLocation().y + last.getHeight();
				finalX = cur.getLocation().x + cur.getWidth()/2;
				finalY = cur.getLocation().y;
			}
		//if moving to the right
		} else {
			startX = last.getLocation().x + last.getWidth();
			//up-right
			if (curButton.y < lastButton.y) {
				startY = last.getLocation().y;
				finalX = cur.getLocation().x;
				finalY = cur.getLocation().y + cur.getHeight();
			//right
			} else if (curButton.y == lastButton.y) {
				startY = last.getLocation().y + last.getHeight()/2;
				finalX = cur.getLocation().x;
				finalY = cur.getLocation().y + cur.getHeight()/2;
			//down-right
			} else {
				startY = last.getLocation().y + last.getHeight();
				finalX = cur.getLocation().x;
				finalY = cur.getLocation().y;
			}
		}
		Point[] pts = new Point[2];
		pts[0] = new Point(startX, startY);
		pts[1] = new Point(finalX, finalY);
		return pts;
	}
}
