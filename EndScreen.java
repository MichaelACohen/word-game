

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class EndScreen {
	private String header = "ROUND OVER!";
	private ArrayList<String> wordList;
	private ArrayList<Integer> scoreList;
	
	public EndScreen(final JFrame frame, ArrayList<String> words, ArrayList<Integer> scores, 
						ArrayList<String> allWords, ArrayList<Integer> allScores) {
		wordList = words;
		scoreList = scores;
		
		sortArrays(wordList, scoreList);
		sortArrays(allWords, allScores);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(null);
		frame.setContentPane(panel);
		
		JTextPane tp = new JTextPane();
		tp.setBackground(Color.yellow);
		tp.setEditable(false);
		String s = "";
		tp.setFont(new Font("", Font.BOLD, frame.getWidth()/30));
		for (int i = 0; i < wordList.size(); i++) {
			s = s + wordList.get(i) + " " + scoreList.get(i) + "\n";
		}
		tp.setText(s);
		centerAlign(tp);
		//because scrollbar started at the bottom for some reason without this
		tp.setCaretPosition(0);
		JScrollPane sp = new JScrollPane(tp);
		sp.setSize(frame.getWidth()/3, frame.getHeight()/2);
		sp.setLocation(frame.getWidth()/9, frame.getHeight()/3);
		sp.setBorder(new LineBorder(Color.black, 2));
		panel.add(sp);
		
		JTextPane title = new JTextPane();
		title.setBackground(Color.yellow);
		title.setEditable(false);
		int width = frame.getWidth();
		int height = frame.getHeight();
		if (width > height) {
			title.setFont(new Font("",0,height/16));
		} else {
			title.setFont(new Font("",0,width/16));
		}
		title.setText(header);
		centerAlign(title);
		title.setSize(frame.getWidth()/2, frame.getHeight()/8);
		title.setLocation(frame.getWidth()/4, frame.getHeight()/15);
		title.setBorder(new LineBorder(Color.black, 2));
		panel.add(title);
		
		JTextPane results = new JTextPane();
		results.setBackground(Color.yellow);
		results.setEditable(false);
		results.setFont(new Font("",0, frame.getWidth()/40));
		results.setText("You found " + wordList.size() + " words for " + sumScoreList(scoreList) + " points.");
		results.setSize(frame.getWidth()/3, frame.getHeight()/14);
		results.setLocation(frame.getWidth()/9, frame.getHeight()/4);
		results.setBorder(new LineBorder(Color.black, 2));
		centerAlign(results);
		panel.add(results);
		
		JTextPane total = new JTextPane();
		total.setBackground(Color.yellow);
		total.setEditable(false);
		total.setFont(new Font("",0, frame.getWidth()/40));
		total.setText("There were " + allWords.size() + " total words for " + sumScoreList(allScores) + " total points.");
		total.setSize(frame.getWidth()/3, frame.getHeight()/14);
		total.setLocation(frame.getWidth()*5/9, frame.getHeight()/4);
		total.setBorder(new LineBorder(Color.black, 2));
		centerAlign(total);
		panel.add(total);
		
		JTextPane all = new JTextPane();
		all.setBackground(Color.yellow);
		all.setEditable(false);
		String w = "";
		all.setFont(new Font("", Font.BOLD, frame.getWidth()/30));
		for (int i = 0; i < allWords.size(); i++) {
			w = w + allWords.get(i).toUpperCase() + " " + allScores.get(i) + "\n";
		}
		all.setText(w);
		centerAlign(all);
		//because scrollbar started at the bottom for some reason without this
		all.setCaretPosition(0);
		JScrollPane sp2 = new JScrollPane(all);
		sp2.setSize(frame.getWidth()/3, frame.getHeight()/2);
		sp2.setLocation(frame.getWidth()*5/9, frame.getHeight()/3);
		sp2.setBorder(new LineBorder(Color.black, 2));
		panel.add(sp2);
	}
	private int sumScoreList(ArrayList<Integer> scoreList) {
    	int sum = 0;
		for (int i : scoreList) {
			sum += i;
		}
		return sum;
	}
	private void sortArrays(ArrayList<String> wordList, ArrayList<Integer> scoreList) {
    	for (int i = 0; i < scoreList.size(); i++) {
    		int max = scoreList.get(i);
    		int maxIndex = i;
    		for (int j = i; j < scoreList.size(); j++) {
    			if (scoreList.get(j) > max) {
    				max = scoreList.get(j);
    				maxIndex = j;
    			}
    		}
    		swap(wordList, scoreList, i, maxIndex);
    	}
    }
    private void swap(ArrayList<String> wordList, ArrayList<Integer> scoreList, int i, int j) {
    	if (j > i) {
    		String temp = wordList.get(i);
    		int temp2 = scoreList.get(i);
    		wordList.set(i, wordList.get(j));
    		wordList.set(j, temp);
    		scoreList.set(i, scoreList.get(j));
    		scoreList.set(j, temp2);
    	}
    }
	private void centerAlign(JTextPane tp) {
		StyledDocument doc = tp.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(),center,false);
	}
}