package wwwordz.puzzle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;

/**
 * <b>An organized collection of words, optimized for searching them. 
 * This class is a singleton, meaning that there is, at most, a single instance of this class per application.
 * This dictionary uses a collection of Portuguese words loaded as a resource from a file in this package. 
 * It is backed by a Trie to index words and speedup searches.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Dictionary
extends java.lang.Object {
	
	Trie trie;
	private static final String DIC_FILE = "src/wwwordz/puzzle/pt-PT-AO.dic";
	private static Dictionary instance = null;
	
	public Dictionary() {
		try {
			this.trie = createDictionaryWithTrie();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Obtain the sole instance of this class. Multiple invocations will receive the exact same instace.
	 * @return singleton
	 */
	public static Dictionary getInstance() {
		if(instance==null) instance = new Dictionary();
		return instance;
	}
	
	/**
	 * Start a dictionary search.
	 * @return search
	 */
	public Trie.Search startSearch() {
		return this.trie.startSearch();
	}
	
	/**
	 * Return a large random word from the trie
	 * @return large word
	 */
	public java.lang.String getRandomLargeWord() {
		return this.trie.getRandomLargeWord();
	}
	
	/**
	 * Create a trie for the dictionary given
	 * @return trie
	 * @throws IOException
	 */
	public static Trie createDictionaryWithTrie() throws IOException {
		
		Trie trie = new Trie();		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(DIC_FILE),"UTF-8"));

		String line;
		int firstLine=0;
		while((line = reader.readLine()) != null) {
			if(firstLine==0) {
				firstLine++;
				continue;
			 }
			 String aux = "";
			 for(int i=0; i<line.length(); i++) {
				 if(!Character.isLetter(line.charAt(i))) {
					 break;
				 }
				 aux += line.charAt(i);
			 }
			 aux = Normalizer.normalize(aux.toUpperCase(Locale.ENGLISH),Form.NFD).
	          replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			 if(aux.length()>=3) trie.put(aux);			  
		  }
		  reader.close();
		  return trie;						
	}
}