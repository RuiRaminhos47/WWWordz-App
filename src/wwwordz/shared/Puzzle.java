package wwwordz.shared;
import java.io.Serializable;
import java.util.List;

/**
 * <b>A puzzle, containing a table and list of solutions. 
 * A table is a square grid of letters and a solution is a word contained in the grid, 
 * where consecutive letters are in neighboring cells on the grid and the letter 
 * in each cell is used only once.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Puzzle extends java.lang.Object implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Table table;
	public List<Puzzle.Solution> solutions;
	
	public Puzzle() {}
	
	public Table getTable() {
		return this.table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	
	public java.util.List<Solution> getSolutions() {
		return this.solutions;
	}
	
	public void setSolutions(java.util.List<Solution> solutions) {
		this.solutions = solutions;
	}
	
	public static class Solution
	extends java.lang.Object
	implements java.io.Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		java.lang.String word;
		java.util.List<Table.Cell> cells;
		
		public Solution() {}
		
		public Solution(java.lang.String word,
                		java.util.List<Table.Cell> cells) {
			this.word = word;
			this.cells = cells;
		}
		
		public java.lang.String getWord() {
			return this.word;
		}
		
		/**
		 * <b>Get points of this word.</b>
		 */
		public int getPoints() {
			int resultado = 1;
			for(int i=3; i<this.word.length(); i++) {
				resultado = resultado * 2 + 1; 
			}
			return resultado;
		}
		
		public java.util.List<Table.Cell> getCells() {
			return this.cells;
		}
		
	}
}
