package Inlämningsuppgift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TestSudoku {
	private Sudoku sudoku;

	@BeforeEach
	void setUp() {
		int[][] matrix = new int[9][9];
		sudoku = new Sudoku(matrix);
	}

	@AfterEach
	void tearDown(){
		sudoku.clear();
	}

	/**
	 * Test if a newly created sudoku is solvable.
	 */
	@Test
	void testNewSudoku() {
		assertTrue(sudoku.solve());
	}

	@Test
	void testSolve() {
		//Lösbart sudoku
		int[][] board = {
			{ 8, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 3, 6, 0, 0, 0, 0, 0 },
			{ 0, 7, 0, 0, 9, 0, 2, 0, 0 },
			{ 0, 5, 0, 0, 0, 7, 0, 0, 0 },
			{ 0, 0, 0, 0, 4, 5, 7, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 3, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 6, 8 },
			{ 0, 0, 8, 5, 0, 0, 0, 1, 0 },
			{ 0, 9, 0, 0, 0, 0, 4, 0, 0 } 
		  };
		  //lösbart sudoku från figur1 i handledning
		  sudoku.setMatrix(board);
		  assertTrue(sudoku.solve());
		  int[][] board2 = {
			{ 0, 0, 8, 0, 0, 9, 0, 6, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 5 },
			{ 1, 0, 2, 5, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 2, 1, 0, 0, 9, 0 },
			{ 0, 5, 0, 0, 0, 0, 6, 0, 0 },
			{ 6, 0, 0, 0, 0, 0, 0, 2, 8 },
			{ 4, 1, 0, 6, 0, 8, 0, 0, 0 },
			{ 8, 6, 0, 0, 3, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 4, 0, 0 } 
		  };
		  sudoku.setMatrix(board2);
		  assertTrue(sudoku.solve());

		  //olösbart sudoku
		  int[][] board3 = {
			{ 8, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 3, 6, 0, 0, 0, 0, 0 },
			{ 0, 7, 0, 0, 9, 0, 2, 0, 0 },
			{ 0, 5, 0, 6, 0, 7, 0, 0, 0 },
			{ 0, 0, 0, 0, 4, 5, 7, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 3, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 6, 8 },
			{ 0, 0, 8, 5, 0, 0, 0, 1, 0 },
			{ 0, 9, 0, 0, 0, 0, 4, 0, 0 } 
		  };

		  sudoku.setMatrix(board3);
		  assertFalse(sudoku.solve());
	}

	/** Test Legal */
	@Test
	void testLegal() {
	int[][] board = {
			{ 8, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 3, 6, 0, 0, 0, 0, 0 },
			{ 0, 7, 0, 0, 9, 0, 2, 0, 0 },
			{ 0, 5, 0, 0, 0, 7, 0, 0, 0 },
			{ 0, 0, 0, 0, 4, 5, 7, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 3, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 6, 8 },
			{ 0, 0, 8, 5, 0, 0, 0, 1, 0 },
			{ 0, 9, 0, 0, 0, 0, 4, 0, 0 } 
		  };

		sudoku.setMatrix(board);

		//*Testar med 8 i samma rad
		assertFalse(sudoku.legal(8,0,5), "Wrong legal in row");
		//*Testar med 8 i samma col
		assertFalse(sudoku.legal(8,5,0), "Wrong legal in col");
		//*Testar med 8 i samma region
		assertFalse(sudoku.legal(8,2,1), "Wrong legal in region");

		//Testa clear
		sudoku.clear();
		assertTrue(sudoku.legal(8,0,5), "Wrong legal after clear in row");

		for(int i = 0; i<9; i++){
			for(int j = 0; j<9; j++){
				assertEquals(0, sudoku.getMatrix()[i][j], "Wrong elements in matrix after clear");
			}
		  }
	}

	/**
	 * Test set()
	 */
	@Test
	void testSet() {
		//test if digit < 0
		assertThrows(IllegalArgumentException.class, () -> sudoku.set(-1,0,0));
		//test if digit > 8
		assertThrows(IllegalArgumentException.class, () -> sudoku.set(15,0,0));
		//test sätta in ett värde
		sudoku.set(5,0,3);
		int[][] board = sudoku.getMatrix();
		assertEquals(5, board[0][3], "Wrong in set method");
		
		//test remove method
		sudoku.remove(0,3);
		board = sudoku.getMatrix();
		assertEquals(0, board[0][3], "Wrong in remove method");
	}

	/**
	 * Test remove()
	 */
	@Test
	void testRemove() {
		//test if outside range [0...9]
		assertThrows(IllegalArgumentException.class, () -> sudoku.remove(15,0));
		assertThrows(IllegalArgumentException.class, () -> sudoku.set(0,-5,4));
	}

	/**
	 * Test setMatrix with wrong matrix and legal matrix
	 */
	@Test
	void testSetMatrix() {
		int[][] matrix1 = new int[10][10];
		//test matrix outside range [0...9]
		assertThrows(IllegalArgumentException.class, () -> sudoku.setMatrix(matrix1));

		int[][] board = {
			{ 8, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 3, 6, 0, 0, 0, 0, 0 },
			{ 0, 7, 0, 15, 9, 0, 2, 0, 0 },
			{ 0, 5, 0, 0, 0, 7, 0, 0, 0 },
			{ 0, 0, 0, 0, 4, 5, 7, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 3, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 6, 8 },
			{ 0, 0, 8, 5, 0, 0, 0, 1, 0 },
			{ 0, 9, 0, 0, 0, 0, 4, 0, 0 } 
		  };

		  //test matrix[2][3] outside range [0...9]
		  assertThrows(IllegalArgumentException.class, () -> sudoku.setMatrix(board));

		  int[][] board2 = {
			{ 8, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 3, 6, 0, 0, 0, 0, 0 },
			{ 0, 7, 0, 5, 9, 0, 2, 0, 0 },
			{ 0, 5, 0, 0, 0, 7, 0, 0, 0 },
			{ 0, 0, 0, 0, 4, 5, 7, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 3, 0 },
			{ 0, 0, 1, 0, 0, 0, 0, 6, 8 },
			{ 0, 0, 8, 5, 0, 0, 0, 1, 0 },
			{ 0, 9, 0, 0, 0, 0, 4, 0, 0 } 
		  }; 

		  sudoku.setMatrix(board2);
		  for(int i = 0; i<9; i++){
			for(int j = 0; j<9; j++){
				assertEquals(board2[i][j], sudoku.getMatrix()[i][j], "Wrong matrix after set and get");
			}
		  }
		  
	}
}
