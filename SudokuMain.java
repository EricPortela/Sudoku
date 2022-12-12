import javax.swing.*;

import java.net.SocketImpl;
import java.util.*;
import java.awt.*;


public class SudokuMain {

    public static void main(String[] args) {

        int[][] matrix = new int[9][9];
        Sudoku s = new Sudoku(matrix);
        SudokuGUI gui = new SudokuGUI(new int[9][9], s);
    }
}