package wwwordz.puzzle;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <b>A trie is data structure to store words efficiently using a tree. 
 * Each tree node represents prefix that may be a a complete word. 
 * The descendants of a node are indexed by a letter, 
 * representing a possible continuation of that prefix followed by that letter.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Trie
extends java.lang.Object
implements java.lang.Iterable<java.lang.String> {

	public Node root;
	
	public Trie() {
		this.root = new Node();
	}
	
	/**
	 * Insert a word in the structure, starting from the root, calling a recursive method. 
	 * @param word - to be inserted
	 */
	public void put(String word) {
		this.root.put(word, 0);
	}
	
	/**
	 * Start a word search from the root.
	 * @return Trie.Search instance
	 */ 
	public Trie.Search startSearch() {
		return new Search(this.root);
	}
	
	/**
	 * Performs a random walk in the data structure, randomly selecting a path in each node, 
	 * until reaching a leaf (a node with no descendants).
	 * @return word as a String
	 */
	public java.lang.String getRandomLargeWord() {
		return this.root.getRandomLargeWord();
	}
	
	/**
	 * Returns an iterator over the strings stored in the trie
	 */
	public java.util.Iterator<java.lang.String> iterator() {
		return new NodeIterator();
	}
	
	/**
	 * <b>Represents a node of the trie.</b><br>
	 */
	public static class Node
	extends java.lang.Object {
	    
		private HashMap<Character, Node> childrens;
	    private boolean completedWord;
	    
	    /**
	     * Creates a new node
	     */
	    public Node() {
	    	this.childrens =  new HashMap<Character,Node>();
	    	this.completedWord = false;
	    }
	    
	    /**
	     * Get the children of a node
	     * @return Hashmap<Character,Node> - children
	     */
	    public HashMap<Character, Node> getChildrens() {
	    	return this.childrens;
	    }
	    
	    /**
	     * Check if a node has a completed word
	     * @return true if it is a completed word; false otherwise
	     */
	    public boolean getCompletedWord() {
	    	return this.completedWord;
	    }
	    
	    /**
	     * Set the boolean if the word is completed
	     */
	    public void setCompletedWordTrue() {
	    	this.completedWord = true;
	    }
	    
	    /**
	     * Check if a child already exists
	     * @param c - char
	     * @return true if child exists; false otherwise
	     */
	    public boolean childrenAlreadyExists(char c) {
	    	if(this.childrens.containsKey(c)) return true;
	    	return false;
	    }
	    
	    /**
	     * Recursive method of put
	     * @param word - to insert
	     * @param level - of the trie
	     */
	    public void put(String word, int level) {
	    	if(level>=word.length()) {
	    		this.setCompletedWordTrue();
	    		return;
	    	}	    	
	    	HashMap<Character, Node> nodeCurrentChildrens = this.getChildrens();   
	        if(this.childrenAlreadyExists(word.charAt(level))) {
	        	Node aux = nodeCurrentChildrens.get(word.charAt(level));
	        	aux.put(word, level+1);
	        }
	        else {
	        	Node children = new Node();
	        	this.childrens.put(word.charAt(level), children);
	        	children.put(word, level+1);
	        }
		}
	    
	    /**
	     * Returns a large word from trie
	     * @return a large word
	     */
	    public String getRandomLargeWord() {	    	
	    	int sizeMap = this.childrens.size();
	    	int low = 1;
	    	int high = sizeMap;
	    	int result = ThreadLocalRandom.current().nextInt(low, high + 1);
	    	int i=1;
	    	Set<Character> keys = this.childrens.keySet();
	    	for (Character key : keys) {
	    		if(i==result) return this.childrens.get(key).getRandomLargeWord(Character.toString(key));
	    	    i++;
	    	}    	
	    	return "";
	    }
	    
	    /**
	     * Recursive method of getRandomLargeWord()
	     * @param aux - string in that point of the recursion 
	     * @return a large word
	     */
	    public String getRandomLargeWord(String aux) {    		    	
	    	if(this.completedWord==true && this.childrens.size()==0) {
	    		return aux;
	    	}	    	
	    	int sizeMap = this.childrens.size();
	    	int low = 1;
	    	int high = sizeMap;
	    	int result = ThreadLocalRandom.current().nextInt(low, high + 1);
	    	int i=1;
	    	Set<Character> keys = this.childrens.keySet();
	    	for (Character key : keys) {
	    		if(i==result) return this.childrens.get(key).getRandomLargeWord(aux + Character.toString(key));
	    	    i++;
	    	}
	    	return "";
	    }

	}
	
	/**
	 * <b>Iterator over strings stored in the internal node structure It traverses the node tree depth first,
	 *  using coroutine with threads, and collects all possible words in no particular order. 
	 *  An instance of this class is returned by iterator()</b><br>
	 */
	public class NodeIterator
	extends java.lang.Object
	implements java.util.Iterator<java.lang.String>, java.lang.Runnable {
		
		private java.lang.Thread thread;
		private boolean terminated;
		private java.lang.String nextWord;
		
		NodeIterator() {
			thread = new Thread(this,"Node iterator");
            thread.start();
		}
		
		public void run() {      
            terminated = false;
            Set<Character> keys = root.childrens.keySet();
            for (Character key : keys) {
	    		visitValues(root.childrens.get(key), Character.toString(key));
	    	}   
            synchronized (this) {
                terminated = true;
                handshake();
            }
		}
		
		public boolean hasNext() {
			synchronized (this) {
                if(! terminated)
                    handshake();
            }
            return nextWord != null;
		}
		
		public java.lang.String next() {
			var word = nextWord;	
            synchronized (this) {
                nextWord = null;
            }         
            return word;
		}
		
		private void visitValues(Node node, String aux) {
			if(node.completedWord==true) {
	            synchronized (this) {
	                if(nextWord != null)
	                    handshake();
	                nextWord = aux;
	                handshake();
	            }
			}           
            Set<Character> keys = node.childrens.keySet();
            for (Character key : keys) {
	    		visitValues(node.childrens.get(key), aux + Character.toString(key));
	    	}   
        }
		
		private void handshake() {
            notify();
            try {
                wait();
            } catch (InterruptedException cause) {
                throw new RuntimeException("Unexpected interruption while waiting",cause);
            }
        }
	}
	
	public static class Search
	extends java.lang.Object {
		
		public Node node;
		
		/**
		 * Create a search starting in given node
		 * @param node - prefix already searched
		 */
		public Search(Trie.Node node) {
			this.node = node;
		}
		
		/**
		 * Create a clone of the given search, with the same fields.
		 * @param search - to be cloned
		 */
		public Search(Trie.Search search) {
			this.node = search.node;
		}
		
		/**
		 * Check if the search can continue with the given letter. Internal node is updated if the search is valid.
		 * @param letter - to continue search
		 * @return true if letter found; false otherwise
		 */
		boolean continueWith(char letter) {
			if(this.node.childrenAlreadyExists(letter)) {
				Node aux = this.node.getChildrens().get(letter);
				this.node = aux;
				return true;
			}
			return false;
		}
		
		/**
		 * Check if characters searched so far correspond to a word
		 * @return true if node is a complete word; false otherwise
		 */
		boolean isWord() {
			if(this.node.completedWord==true) return true;
			return false;
		}

	}
}
