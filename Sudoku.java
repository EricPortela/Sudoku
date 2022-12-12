import java.util.*;

public class Sudoku implements SudokuSolver{
    private int[][] matrix;
    private final int SIZE = 9;
    private boolean legalLayout;

    public Sudoku(int[][] matrix){
        this.matrix = matrix;
        legalLayout = true;
    } 

    /**
     * Checks if sudoku is solvable.
     * @return boolean depending on wether the sudoku is solveable or not.
    */  
    public boolean solve(){
        return solve(0,0);
    }
    
    private boolean solve(int row, int col){
        //Gå igenom hela sudokun EN GÅNG o checka ifall något är illegal och isåfall return false direkt
        if(legalLayout){
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if(matrix[r][c] != 0){
                        int digit = matrix[r][c];
                    if(!legal(digit,r,c)){
                        return false;
                    }  
                    }   
                }
            }
        }

        //vi har gått igenom hela sudokun, BASFALL
        if (row == 8 && col == 9){
            return true;
        }
        //kollar om col == 9 isåfall gå vidare till nästa rad
        if (col == 9) {
            row++;
            col = 0;
        }
        //kollar om rutan är fylld
        if (matrix[row][col] != 0){
            //isåfall gå till nästa ruta och gör samma
            return solve(row, col + 1);
        }else{
            legalLayout = false; //tilldelas false för att inte behöva kolla igenom hela sudokut 
            //om rutan är tom testa med olika siffror
        for (int num = 1; num < 10; num++) {
            
            if(legal(num,row,col)){
                set(num, row, col);
                //testa om det går att lösa 
                if(solve(row, col + 1)){
                    return true;
                }else{
                    //annars ta bort o gör om
                   remove(row, col);  
                }
            }
        }
    }
    legalLayout = true;
    return false;
    }

     /**
     * checks if digit is legal in the box, row and column.
     * 
     * @param row   The row
     * @param col   The column
     * @param digit The digit to check in box row, col
     * @return boolean depending on wether the digit is legal or not
     */
    public boolean legal(int digit, int row, int col){
        //check if digit is in row
        for(int i = 0; i<SIZE; i++){
            if(matrix[row][i] == digit){
                if (i != col) {
                    return false;
                }
            }
        }
        //check if digit is in col
        for(int i = 0; i<SIZE; i++){
            if(matrix[i][col] == digit){
                if (i != row) {
                    return false;
                }
            }
        }
        //check if digit is in region
        int r = row - (row % 3);
        int c = col - (col % 3);

        for (int i = r; i < r + 3; i++){
            for (int j = c; j < c + 3; j++){
                if (matrix[i][j] == digit){
                    if (i != row && j != col) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Puts digit in the box row, col.
     * 
     * @param row   The row
     * @param col   The column
     * @param digit The digit to insert in box row, col
     * @throws IllegalArgumentException if row, col or digit is outside the range
     *                                  [0..9]
     */
    public void set(int digit, int row, int col){
        if(row < 0 || row > 8 || col < 0 || col > 8 || digit < 0 || digit > 9){ //checks if outside range [0...9]
            throw new IllegalArgumentException();
        }else if (legal(digit,row,col)){
            matrix[row][col] = digit;
        }
    }

    /**
     * Removes the digit in the box row, col.
     * 
     * @param row   The row
     * @param col   The column
     * @throws IllegalArgumentException if row, col or digit is outside the range
     *                                  [0..9]
     */
    public void remove(int row, int col){
        if(row < 0 || row > 8 || col < 0 || col > 8){ //checks if outside range [0...9]
            throw new IllegalArgumentException();
        }

        matrix[row][col] = 0;
    }

    /**
     * Empties the grid.
    */
    public void clear(){
        for(int i = 0; i<SIZE;i++){
            for(int j = 0; j<SIZE;j++){
                matrix[i][j] = 0;
            }
        }
    }

    /**
     * Fills the grid with the digits in matrix. The digit 0 represents an empty box.
     * 
     * @param matrix the matrix with the digits to insert
     * @throws IllegalArgumentException if m has the wrong dimension or contains
     *                                  values outside the range [0..9]
     */
    public void setMatrix(int[][] matrix){
        if(matrix.length < 0 || matrix.length > SIZE || matrix[0].length < 0 || matrix[0].length > SIZE){ //checks if invalid size
            throw new IllegalArgumentException();
        }
        for(int i = 0; i<SIZE; i++){
            for(int j = 0; j<SIZE; j++){
                if(matrix[i][j] < 0 || matrix[i][j] > 9){ //checks if outside range [0...9]
                    throw new IllegalArgumentException();
                }
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    /**
     * Returns a matrix with the current values.
     * 
     * @return integer matrix with current values
     */
    public int[][] getMatrix() {
        //skapa en kopia av matrisen
        int[][] temp = new int[9][9];
        for(int i = 0; i<SIZE; i++){
            for(int j = 0; j<SIZE; j++){
                temp[i][j] = matrix[i][j];
            }
        }
        return temp;
    }
}
