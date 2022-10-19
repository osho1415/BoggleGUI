package edu.arizona.FXTemp;

/* A model of the dice tray as used in the real game. 
 * 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DiceTray {

  private char[][] path;
  private char[][] theBoard;
  public static final char TRIED = '@';
  public static final char PART_OF_WORD = '!';
  private String attempt;
  private int index;
  public static final int SIZE = 4;
  public static final int NUMBER_SIDES = 6;

  // 4x4 Boggle includes 16 dice with the character indicated here (except Q
  // would be Qu). Each string element has 6 letters for the 6 sides of the die.
  String[] dice = { "LRYTTE", "VTHRWE", "EGHWNE", "SEOTIS", "ANAEEG", "IDSYTT", "OATTOW", "MTOICU", "AFPKFS", "XLDERI",
      "HCPOAS", "ENSIEU", "YLDEVR", "ZNRNHL", "NMIQHU", "OBBAOJ" };

  private ArrayList<String> diceRandom;

  private static Random generator;

  /**
   * Simulate the shaking of the Boggle tray with the 16 Boggle dice
   */
  public DiceTray() {
    diceRandom = new ArrayList<String>();
    for (int die = 0; die < SIZE * SIZE; die++)
      diceRandom.add(dice[die]);

    generator = new Random();
    theBoard = getRandomizedDiceArray();
  }

  /**
   * Construct a tray of dice using a hard coded 2D array of chars. Use this for
   * testing
   */
  public DiceTray(char[][] newBoard) {
    theBoard = newBoard;
  }

  /**
   * Provide a textual version of this BoggleTray
   */
  @Override
  public String toString() {
    String result = "";
    for (int r = 0; r < SIZE; r++) {
      result += "\n";
      for (int c = 0; c < SIZE; c++) {
        if (theBoard[r][c] == 'Q')
          result += " Qu";
        else
          result += "  " + theBoard[r][c];
      }
      if (r < SIZE - 1)
        result += " \n";
    }
    return result;
  }

  /**
   * Return true if search is word that can found on the board following the rules
   * of Boggle
   */
  public boolean found(String str) {
    if (str.length() < 3) 
      return false;
    attempt = str.toUpperCase().trim();
    boolean found = false;
    for (int r = 0; r < SIZE; r++) {
      for (int c = 0; c < SIZE; c++)
        if (theBoard[r][c] == attempt.charAt(0)) {
          init();
          found = recursiveLook(r, c);
          if (found) {
            return true;
          }
        }
    }
    return found;
  }

  // Keep a 2nd 2D array to remember the characters that have been tried
  private void init() {
    path = new char[SIZE][SIZE];
    for (int r = 0; r < SIZE; r++)
      for (int c = 0; c < SIZE; c++)
        path[r][c] = '.';
    index = 0;
  }

  // Shuffle the 16 dice and select one of the 6 letters.
  // This method simulate shaking the BoggleTray.
  private char[][] getRandomizedDiceArray() {
    // Get the SIZE*SIZE dice in a different random order
    char[][] randomBoard = new char[4][4];
    Collections.shuffle(diceRandom);
    int dieNumber = 0;
    for (int r = 0; r < SIZE; r++)
      for (int c = 0; c < SIZE; c++) {
        String s = diceRandom.get(dieNumber);
        char letterToPlace = s.charAt(generator.nextInt(NUMBER_SIDES));
        randomBoard[r][c] = letterToPlace;
        dieNumber++;
      }
    return randomBoard;
  }

  // This is like the Obstacle course escape algorithm.
  // Now we are checking 8 possible directions (no wraparound)
  private boolean recursiveLook(int r, int c) {
    boolean found = false;

    if (valid(r, c)) { // valid returns true if in range AND one letter was found
      path[r][c] = TRIED;
      if (theBoard[r][c] == 'Q')
        index += 2;
      else
        index++;

      // Look in 8 directions for the next character
      if (index >= attempt.length())
        found = true;
      else {
        found = recursiveLook(r - 1, c - 1);
        if (!found)
          found = recursiveLook(r - 1, c);
        if (!found)
          found = recursiveLook(r - 1, c + 1);
        if (!found)
          found = recursiveLook(r, c - 1);
        if (!found)
          found = recursiveLook(r, c + 1);
        if (!found)
          found = recursiveLook(r + 1, c - 1);
        if (!found)
          found = recursiveLook(r + 1, c);
        if (!found)
          found = recursiveLook(r + 1, c + 1);
        // If still not found, allow backtracking to use the same letter in a
        // different location later as in looking for "BATTLING" in this board
        //
        // L T T X // Mark leftmost T as untried after it finds a 2nd T but not the L.
        // I X A X
        // N X X B
        // G X X X
        //
        if (!found) {
          path[r][c] = '.'; // Rick used . to mark the 2nd 2D array as TRIED
          index--; // 1 less letter was found. Let algorithm find the right first (col 2)
        }
      } // End recursive case

      if (found) {
        // Mark where the letter was found. Not required, but could be used to
        // show the actual path of the word that was found.
        path[r][c] = theBoard[r][c];
      }
    }
    return found;
  }

  // Determine if a current value of row and columns can or should be tried
  private boolean valid(int r, int c) {
    return r >= 0 && r < SIZE && c >= 0 && c < SIZE && path[r][c] != TRIED && theBoard[r][c] == attempt.charAt(index);
  }


}