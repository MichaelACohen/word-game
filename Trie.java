

import java.util.ArrayList;

//data structure to efficiently search for words on board

public class Trie {
	
    private static final int ALPHABETSIZE = 26;
    private Trie[] children;
    private boolean word;
    
    private String curWord = "";
    
    public static ArrayList<String> allWords = new ArrayList<String>();
    public static ArrayList<Integer> allScores = new ArrayList<Integer>();
    
 
    //initialize trie
    public Trie() {
    	children = new Trie[ALPHABETSIZE];
    	word = false;
    }

    //add string to trie
    public void addString(String s) {
    	Trie t = this;
    	int k;
    	int len = s.length();
    	for(k=0; k < len; k++) {
    		int index = s.charAt(k) - 'a';
    		if (t.children[index] == null) {
    			t.children[index] = new Trie();
    		}
    		t = t.children[index];
    	}
    	t.word = true;
    }
    
    //returns true if s (1) is a prefix OR (2) is a word but not prefix
    public boolean isValidPrefix(String s) {
    	Trie t = this;
    	for (int i = 0; i < s.length(); i++) {
    		int index = s.charAt(i) - 'a';
    		if (t.children[index] == null) return false;
    		t = t.children[index];
    	}
    	return true;
    }

    public boolean isEmpty() {
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null) {
				return false;
			}
		}
		return true;
	}

    
     // determine if a word (s) is in the trie (here or below)
     // return true if s is in trie (rooted here)
     
    public boolean isWord(String s) {
    	Trie t = this;
    	int len = s.length();
    	for(int i = 0; i < len; i++) {
    		int index = s.charAt(i) - 'a';
    		if (t.children[index] == null) return false;
    		t = t.children[index];
    	}
    	return t.isWord();
    }

   //return true if this node marks the end of a word
    public boolean isWord() {
    	return word;
    }

    //returns child
    
    Trie childAt(char ch) {
    	return children[ch-'a'];
    }
    
    //chars is array of chars on board going from left to right and top to bottom
    public void findWords(char[] chars, int[] bonuses) {
    	int curIndex;
    	ArrayList<Integer> visited = new ArrayList<Integer>();
    	for (curIndex=0; curIndex < chars.length; curIndex++) {
    		findWordsRec(chars, bonuses, visited, curIndex);
    		curWord = "";
    		visited.clear();
    	}
	}
    
    //recursive method for finding all words on board
    //depth first search of tiles on board - recursion stops if we hit a prefix
    //with no possible words stemming from it
    
    private void findWordsRec(char[] chars, int[] bonuses, ArrayList<Integer> visited, int curIndex) {
    	
    	if (!visited.contains(curIndex)) {
    		curWord = curWord + String.valueOf(chars[curIndex]).toLowerCase();
    		visited.add(curIndex);
    	
    		//recursion stops if there are no possible words that can be created from curWord
    		if (isValidPrefix(curWord)) {
    			if (isWord(curWord) && curWord.length() > 1) {
    				
    				int value, dw, tw;
    				value = dw = tw = 0;
    				for (int i = 0; i < visited.size(); i++) {
    					int v = Ruzzle.getValue(chars[visited.get(i)]);
						if (visited.get(i) == bonuses[0]) {
							v = v*2;
						} else if (visited.get(i) == bonuses[1]) {
							v = v*3;
						} else if (visited.get(i) == bonuses[2]) {
							dw++;
						} else if (visited.get(i) == bonuses[3]) {
							tw++;
						}
						value += v;
					}
					if (curWord.length() > 4) value += (curWord.length()-4)*5;
					int mult = 1;
					if (dw!=0) mult = mult*2*dw;
					if (tw!=0) mult = mult*3*tw;
					value = value*mult;
					//value contains the point value of the word
					
					int index = allWords.indexOf(curWord);
					if (index == -1) {
						allWords.add(curWord);
						allScores.add(value);
    				} else {
    					if (value > allScores.get(index)) {
    						allScores.set(index, value);
    					}
    				}
    			}
    			
    			ArrayList<Integer> adj = Ruzzle.findAdjacent(curIndex);
    			for (int i = 0; i < adj.size(); i++) {
    				findWordsRec(chars, bonuses, visited, adj.get(i));
    			}
    			curWord = curWord.substring(0, curWord.length()-1);
    			visited.remove(visited.size()-1);
    		} else {
    			curWord = curWord.substring(0, curWord.length()-1);
    			visited.remove(visited.size()-1);
    		} 		
    	}
    }
}


