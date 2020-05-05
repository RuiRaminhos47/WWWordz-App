package wwwordz.shared;
import java.io.Serializable;
import java.util.*;

/**
 * <b>A table composed of a collection of cells indexed 
 * by row and column positions.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Table
extends java.lang.Object
implements java.lang.Iterable<Table.Cell>, java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TABLE_SIZE = 4;
	Cell[][] table = new Cell[TABLE_SIZE+2][TABLE_SIZE+2];
	
	/**
	 * <b>Create a table with empty cells.</b><br>
	 */
	public Table() {
		for(int i=0; i<=TABLE_SIZE+1; i++) {
			for(int j=0; j<=TABLE_SIZE+1; j++) {
				table[i][j] = new Cell(i, j);
			}
		}
	}
	
	/**
	 * <b>Create a table with given data.</b><br>
	 */
	public Table(String[] data) {
		int rows = 1;
		int cols = 1;
		for(String word : data) {
			for(int i=0; i<word.length(); i++) {
				char let = word.charAt(i);
				table[rows][cols] = new Cell(rows, cols, let);
				cols++;
			}
			rows++;
			cols = 1;
		}
	}
	
	@Override
	public Iterator<Table.Cell> iterator() {
		return new CellIterator();
	}
	
	/**
	 * Set a letter at a given row and column
	 * @param row - to set
	 * @param column - to set
	 * @return letter in given position
	 */
	public char getLetter(int row,
                          int column) {
		return table[row][column].getLetter();
	}
	
	/**
	 * Set a letter at a given row and column
	 * @param row - to set
	 * @param column - to set
	 * @param letter - to set
	 */
	public void setLetter(int row,
            			  int column,
            			  char letter) {
		table[row][column].setLetter(letter);
	}
	
	/**
	 * Return a list with the empty cells in this table
	 * @return list of cells
	 */
	public List<Table.Cell> getEmptyCells() {
		List<Table.Cell> list = new ArrayList<Table.Cell> (); 
		for(int i=1; i<=TABLE_SIZE; i++) {
			for(int j=1; j<=TABLE_SIZE; j++) {
				if(table[i][j].isEmpty()) list.add(table[i][j]);
			}
		}
		return list;
	}
	
	/**
	 * The 8 neighboring cells of the given cell.
	 * @param cell - defining the neighborhood
	 * @return list of Table.Cell
	 */
	public List<Table.Cell> getNeighbors(Table.Cell cell) {
		List<Table.Cell> list = new ArrayList<Table.Cell> (); 		
		for(int i=cell.row-1; i<=cell.row+1; i++) {
			for(int j=cell.column-1; j<=cell.column+1; j++) {
				if(i==cell.row && j==cell.column) continue;
				if(i!=0 && j!=0 && i!=TABLE_SIZE+1 && j!=TABLE_SIZE+1) {
					list.add(this.getCell(i, j));
				}
			}
		}
		return list;
	}
	
	/**
	 * Get cell of given row and column
	 * @param row - of cell
	 * @param column - of cell
	 * @return cell in given position
	 */
	public Table.Cell getCell(int row,
            				  int column) {
		return this.table[row][column];
	}
	
	@Override
	public String toString() {
		String aux = "";
		for(int i=1; i<=TABLE_SIZE; i++) {
			for(int j=1; j<=TABLE_SIZE; j++) {
				aux += table[i][j].toString();
			}
			aux += "\n";
		}
		return aux;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(table);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (!Arrays.deepEquals(table, other.table))
			return false;
		return true;
	}
	
	/**
	 * <b>A cell in the enclosing table</b>
	 */
	public static class Cell
	extends java.lang.Object
	implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int row;
		private int column;
		private char letter;
		
		Cell() {}
		
		/**
		 * Create a cell with letter at given row and column
		 * @param row - to set
		 * @param column - to set
		 * @param letter - to set
		 */
		Cell(int row,
			 int column,
			 char letter) {
			this.row = row;
			this.column = column;
			this.letter = letter;
		}
		
		/**
		 * Create an empty cell at the given row an column
		 * @param row - of cell
		 * @param column - of cell
		 */
		Cell(int row,
			 int column) {
			this.row = row;
			this.column = column;
			this.letter = '\0';
		}
		
		/**
		 * Check if cell is empty
		 * @return true if empty; false otherwise
		 */
		public boolean isEmpty() {
			if(this.letter=='\0') {
				return true;
			}
			return false;
		}
		
		@Override
		public String toString() {
			return(Character.toString(this.letter));
		}
		
		/**
		 * Change letter in this cell
		 * @param letter - to set
		 */
		public void setLetter(char letter) {
			this.letter = letter;
		}
		
		/**
		 * Letter in this cell
		 * @return letter in cell
		 */
		public char getLetter() {
			return this.letter;
		}
		
		/**
		 * Cell's row
		 * @return cell row
		 */
		public int getRow() {
			return this.row;
		}
		
		/**
		 * Cell's column
		 * @return cell column
		 */
		public int getColumn() {
			return this.column;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + column;
			result = prime * result + letter;
			result = prime * result + row;
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (column != other.column)
				return false;
			if (letter != other.letter)
				return false;
			if (row != other.row)
				return false;
			return true;
		}
	}
	
	/**
	 * <b>An iterator over cells in this table.</b>
	 */	
	private class CellIterator
	extends java.lang.Object
	implements java.util.Iterator<Table.Cell> {
		
		private int row;
		private int column;
		
		CellIterator() {
			this.row = 1;
			this.column = 0;
		}
		
		@Override
		public boolean hasNext() {
			if(this.row==TABLE_SIZE && this.column==TABLE_SIZE) return false;
			return true;
		}
		
		@Override
		public Table.Cell next() {
			
			if(this.column!=TABLE_SIZE) {
				Cell aux = getCell(this.row, this.column+1);
				this.column++;
				return aux;
			}
			
			Cell aux = getCell(this.row+1, 1);
			this.row++;
			this.column = 1;
			return aux;
			
		}
		
		@Override
		public void remove() {
			table[this.row][this.column] = new Cell(this.row, this.column);
		}
	}
	
}
