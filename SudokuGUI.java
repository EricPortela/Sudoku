import javax.swing.*;
import javax.swing.text.AttributeSet.ColorAttribute;
import java.io.File;
import java.io.FileNotFoundException;

import org.w3c.dom.css.RGBColor;

import java.util.*;
import java.util.zip.Checksum;
import java.awt.*;
import javax.swing.JOptionPane;


public class SudokuGUI {

    private int[][] gameMatrix;
    private JTextField[][] gridMatrix;
    private Sudoku s;
    private Scanner scanner;

    private final int SIZE = 9;
    private final Font FONT = new Font("Helvetica-Neue", Font.BOLD, 18);

    /**
     * Constructor that sets up and displays a GUI for Sudoku.
     * @param gameMatrix Matrix containing integers, which initially represents the board (at start).
     * @param s Pass an instance of the implementated design of Sudoku interface. 
     */
    public SudokuGUI(int[][] gameMatrix, Sudoku s) {
        gridMatrix = new JTextField[SIZE][SIZE];

        this.gameMatrix = gameMatrix;
        this.s = s;
        s.setMatrix(gameMatrix);
        
        SwingUtilities.invokeLater(() -> createWindow("Sudoku"));
    }

    /**
     * Sets up the board (i.e. the upper part of the board, a.k.a GridLayout containing 9x9 JTextFields)
     * @return Returns the JPanel (a.k.a gridView) containing the correct layout of the Board itself. 
     */
    private JPanel setUpBoard(){

        JPanel gridView = new JPanel();
        gridView.setLayout(new GridLayout(SIZE, SIZE));

        Color paint1 = Color.white;
        Color paint2 = new Color(36, 32, 56);

        for (int r = 0; r < SIZE; r++) {
            
            //Switch colors
            if (r > 2 && r < 6) {
                paint1 = new Color(36, 32, 56);
                paint2 = Color.white;
            } else {
                paint1 = Color.white;
                paint2 = new Color(36, 32, 56);
            }
            
            for (int c = 0; c < SIZE; c++) {
                JTextField t = new JTextField();
                t.setHorizontalAlignment(JTextField.CENTER);
                t.setFont(FONT);
                t.setForeground(new Color(141, 134, 201));

                //Set colors
                if (c > 2 && c < 6 ) {
                    t.setBackground(paint1);
                } else {
                    t.setBackground(paint2);
                }

                if (gameMatrix[r][c] == 0) {
                    t.setText("");
                } else {
                    t.setText(Integer.toString(gameMatrix[r][c])); //Sets the value in JTextField
                }

                
                gridMatrix[r][c] = t;
                gridView.add(gridMatrix[r][c]);
            }
        }

        return gridView;
    }

    /**
     * Sets up the bottom ButtonBar.
     * @return Returns the JPanel (a.k.a ButtonBar) containing the correct layout of buttons.
     */
    private JPanel setUpButtonBar() {
        //Bottom menu
        JPanel bottomPanel = new JPanel();
        JButton btn1 = new JButton("Clear"); 
        JButton btn2 = new JButton("Solve");
        JButton btn3 = new JButton("Check solution");
        JButton btn4 = new JButton("Read game from file");

        btn1.addActionListener(e -> {
            clearGUI();
        });

        btn2.addActionListener(e -> {
            solve();
        });

        btn3.addActionListener(e -> {
            checkSolution();
        });

        btn4.addActionListener(e -> {
            readGameFromFile();
        });

        bottomPanel.add(btn1);
        bottomPanel.add(btn2);
        bottomPanel.add(btn3);       
        bottomPanel.add(btn4);

        bottomPanel.setSize(800, 50);

        return bottomPanel;
    }
        

    /**
     * Creates the final GUI, using the two private methods (setUpBoard() and setUpButtonBar()) in this class.
     * @param title The title of the window.
     */
    private void createWindow(String title) {

        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(600, 700)); //Ändra size på frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container pane = frame.getContentPane();

        JPanel gridView = setUpBoard();
        pane.add(gridView);

        JPanel bottomBar = setUpButtonBar();
        pane.add(bottomBar, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        frame.setLocationRelativeTo(null);
    }


    /**
     * Reads the local file in this project directory named sudokufile.txt. The file contains a few solvable sudoku problems. 
     * The method will continue to iterate over the file from the beginning once it has read the last game.
     */
    private void readGameFromFile() {
        
    	// Om det finns en scanner uppe som har lästs klart fullständigt, så stängs den.
    	if (scanner != null && !scanner.hasNextLine()) {
    		scanner.close();
    		scanner = null;
    	}
    	
    	// Om det inte finns en scanner, skapas det en ny.
    	if (scanner == null) {
        	try {
        		scanner = new Scanner(new File("SudokuSolver/sudokufile.txt"));
        	} catch(FileNotFoundException e ) {
        		System.out.println("Couldn’t open file: sudokufile");
        		return;
        	}
    	}
    	
    	// Läser in ett sudoku.
    	int currentNum = 0;
        for (int r= 0; r<gameMatrix.length; r++) {
            for (int c = 0; c<gameMatrix.length; c++) {
            	
            	currentNum = scanner.nextInt();
            	
            	if (currentNum >= 0 && currentNum <= SIZE) {
            		gameMatrix[r][c] = currentNum;
            		if (currentNum != 0) {
            			gridMatrix[r][c].setText(Integer.toString(currentNum));
            			gridMatrix[r][c].setEditable(false);
            		} else {
            			gridMatrix[r][c].setText("");
            			gridMatrix[r][c].setEditable(true);
            		}
            	}
            	
            }
        }
    }


    /**
     * Solves the sudoku.
     */
    private void solve() {
        if (updateGUI(false)) {

            if (!s.solve()) {
                //!INTE LÖSBART
                JOptionPane.showMessageDialog(null, "EJ lösbart!");;
            } else {
                //!Displaya lösningen på GUI:n, dvs uppdatera TextField
                gameMatrix = s.getMatrix();
    
                for (int r = 0; r<gameMatrix.length; r++) {
                    for (int c = 0; c<gameMatrix.length; c++) {
                        gridMatrix[r][c].setText(Integer.toString(gameMatrix[r][c]));
                    }
                }    
            }
        }
    }


    /**
     * Controls the solution presented in the GridView.
     */
    private void checkSolution() {
    	
        if (updateGUI(true)) {
            for (int r= 0; r<gameMatrix.length; r++) {
                for (int c = 0; c<gameMatrix.length; c++) {
                    if (!s.legal(gameMatrix[r][c], r, c) || gameMatrix[r][c] == 0) {
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Korrekt lösning!");
        }
    }


    /**
     * Parses the current values inside the GridView and updates the GUI accordingly. 
     * @return boolean depending on whether or not the current values in the JTextField (inside the GridView) could be parsed correctly.
     * If the parsing was unsuccessful, the JTextFields containign the incorrect data will be marked red and a popup message (JOptionPane)
     * will be displayed.
     */
    private boolean updateGUI(boolean checkSolution) {

        int invalidInput = 0;
        int totalErrors = 0;

        for (int r = 0; r<gridMatrix.length; r++) {
            for (int c = 0; c<gridMatrix.length; c++) {

                gridMatrix[r][c].setForeground(new Color(141, 134, 201)); //sets text color

                try {
                    String valStr = gridMatrix[r][c].getText();
                    int val = 0;

                    if (!valStr.equals("")){
                        val = Integer.parseInt(valStr); //try to parse each text input to integer and save it to local variable

                        if (!(val >= 1 && val <= SIZE)) { //INVALID: Values outside range (1-9)

                            gridMatrix[r][c].setForeground(Color.red);
                            invalidInput ++;
                            totalErrors ++;
                        }

                    }  else {
                        totalErrors ++; //INVALID: Values that are ""
                    }
                    
                    gameMatrix[r][c] = val; //VALID VALUE --> INSERT in matrix

                } catch (NumberFormatException ex) {

                    gameMatrix[r][c] = 0; //Update the gameMatrix with users' input:ed value
                    gridMatrix[r][c].setForeground(Color.red);
                    invalidInput ++;
                    totalErrors ++;
                }
            }
        }

        return checkForErrors(checkSolution, invalidInput, totalErrors);
    }


    /**
     * Checks for errors. If we want to "check solution", then check for errors in incorrectly parsed characters (invalidInput),
     * otherwise (if we want to solve) check for incorrectly parsed characters and blank spaces (totalErrors).
     * @param checkSolution boolean, if true, we want to check solution, otherwise we want to solve.
     * @param invalidInput invalidInput in the sense that characters were incorrectly parsed
     * @param totalErrors total errors includes incorrectly parsed values and blank spaces
     * @return boolean
     */
    private boolean checkForErrors(boolean checkSolution, int invalidInput, int totalErrors) {

        if (!checkSolution) {
            return showErrorMessage(invalidInput);
        }

        return showErrorMessage(totalErrors);
    }


    /**
     * Displays error messge depending on the amount of errors detected
     * @param errors in terms of incorrectly parsed input and blank fields
     * @return boolean depending on amount of errors
     */
    private boolean showErrorMessage(int errors) {
        if (errors == 1) {
            JOptionPane.showMessageDialog(null, "Du har skrivit in ett ogiltigt värde/karaktär. \nSudokut är inte lösbart. Se rutan med rödmarkerad text. \nÄndra och försök igen!");
            return false;
        }  else if (errors > 1) {
            JOptionPane.showMessageDialog(null, "Du har skrivit in " + errors +" ogiltiga värden/karaktärer. \nSudokut är inte lösbart. Se rutor med rödmarkerad text. \nÄndra och försök igen!");
            return false;
        }

        s.setMatrix(gameMatrix);

        return true;
    }


    /**
     * Clears the GUI from the entered numbers in the GridView.
     */
    private void clearGUI() {
        for (int r = 0; r<SIZE; r++) {
            for (int c = 0; c<SIZE; c++) {
                gridMatrix[r][c].setText("");
                gameMatrix[r][c] = 0;
                gridMatrix[r][c].setForeground(new Color(141, 134, 201));
                gridMatrix[r][c].setEditable(true);
            }
        }
        s.setMatrix(gameMatrix);
    }
}
