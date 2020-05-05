package wwwordz.puzzle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import wwwordz.puzzle.Trie.Search;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Table;
import wwwordz.shared.Table.Cell;

/**
 * <b>A puzzle generator. Creates a puzzle with many scrambled words contained in a dictionary.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Generator
extends java.lang.Object {
	
	Dictionary dic = null;
	
	public Generator() {}
	
	/**
	 * Generate a high quality puzzle with many words in it
	 * @return table
	 */
	public Puzzle generate() {
		Puzzle puzzle = new Puzzle();
		Table table = new Table();
		List<Cell> emptyCells = table.getEmptyCells();
		Random r = new Random();
		dic = Dictionary.getInstance();
		while(emptyCells.size() != 0) {			
			Cell firstCell = emptyCells.get(r.nextInt(emptyCells.size()));
			String bigWord = dic.getRandomLargeWord();
			putWordInTable(bigWord, firstCell, 0, table);
			emptyCells = table.getEmptyCells();
		}
		puzzle.setTable(table);
		puzzle.setSolutions(getSolutions(table));
		return puzzle;
	}
	
	private void putWordInTable(String word, Cell cell, int positionInWord, Table table) {
		cell.setLetter(word.charAt(positionInWord));	
		if(word.length() > positionInWord+1) {
			List<Cell> neighbors = table.getNeighbors(cell);
			Collections.shuffle(neighbors);
			for(Cell element: neighbors) {
				if(element.isEmpty() ) {
					putWordInTable(word, element, positionInWord+1, table);
					break;
				}		
			}
		}	
	}
	
	public Puzzle random() {
		Puzzle gamePuzzle = new Puzzle();
		String[] data = new String[4];
		for(int i=0; i<4; i++) {
			data[i] = "";
		}		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				Random letterRandom = new Random();
				char c = (char) (letterRandom.nextInt(26) + 'A');
				data[i] += Character.toString(c);
			}
		}
		Table aux = new Table(data);
		gamePuzzle.setTable(aux);
		List<Puzzle.Solution> list = getSolutions(gamePuzzle.table);
		gamePuzzle.setSolutions(list);
		return gamePuzzle;
	}
	
	/**
	 * Return a list of solutions for this table. Solutions have at least 2 letters. Different solutions for the same word are discarded.
	 * @param table - containing solutions
	 * @return list of solutions
	 */
	public List<Puzzle.Solution> getSolutions(Table table) {
		
		if(table.getEmptyCells().size()!=0) {
			return Collections.emptyList();
		}
		
		dic = Dictionary.getInstance();
		java.util.List<Puzzle.Solution> list = new ArrayList<Puzzle.Solution>();
		
		// loop the table
		for(int i=1; i<=Table.TABLE_SIZE; i++) {
			for(int j=1; j<=Table.TABLE_SIZE; j++) {
				char letter = table.getLetter(i,j); // first letter of the word
				String aux = Character.toString(letter);
				boolean[][] visited = new boolean[Table.TABLE_SIZE + 1][Table.TABLE_SIZE+1];
				for(int z=1; z<=Table.TABLE_SIZE; z++) {
					for(int k=1; k<=Table.TABLE_SIZE; k++) {
						visited[z][k] = false;
					}
				}
				visited[i][j] = true;
				List<Table.Cell> neighbors = table.getNeighbors(table.getCell(i,j));
				Search search = new Search(dic.trie.root);
				search.continueWith(letter); 
				Search searchAux = new Search(search);
				for (Cell neighbor : neighbors) {
					search = new Search(searchAux);
					if(search.continueWith(neighbor.getLetter())) {
						List<Puzzle.Solution> listAux = getRecursiveSolutions(table, visited, neighbor, aux, search);
						if(listAux!=null) list.addAll(listAux);
					}
			    }
			}
		}	
		
		List<Puzzle.Solution> finalList = new ArrayList<Puzzle.Solution>();
		for (Puzzle.Solution element : list) { 		      
	        String word = element.getWord();  
	        boolean itAppears = false;
	        for(Puzzle.Solution element2 : finalList) {
	        	if(element2.getWord().equals(word)) itAppears = true;
	        }
	        if(!itAppears) finalList.add(element);
	    }

		return list;
	}
	
	public java.util.List<Puzzle.Solution> getRecursiveSolutions(Table table, boolean[][] visited, Cell actual, String aux, Search search) {
		
		List<Puzzle.Solution> returnList = new ArrayList<Puzzle.Solution>();
		
		aux += Character.toString(actual.getLetter()); 
		visited[actual.getRow()][actual.getColumn()] = true;
		
		List<Table.Cell> listCells = new ArrayList<Table.Cell>();
		for(int i=1; i<=Table.TABLE_SIZE; i++) {
			for(int j=1; j<=Table.TABLE_SIZE; j++) {
				if(visited[i][j]==true) listCells.add(table.getCell(i, j));
			}
		}
		Puzzle.Solution actualSolution = new Puzzle.Solution(aux, listCells);
		if(search.isWord()==true) {
			returnList.add(actualSolution);
		}
	
		Search auxSearch = new Search(search);
		
		List<Table.Cell> neighbors = table.getNeighbors(table.getCell(actual.getRow(),actual.getColumn()));
		for (Cell neighbor : neighbors) {
			search = new Search(auxSearch);
			if(search.continueWith(neighbor.getLetter())) {
				returnList.addAll(getRecursiveSolutions(table, visited, neighbor, aux, search));
			}
	    }
		
		return returnList;
	}
}
