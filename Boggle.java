package edu.arizona.FXTemp;
import java.io.File;
import java.io.FileNotcorrectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Boggle {
      
  // A reference to the dice tray used in this game
  private DiceTray diceTray;

  // The list of words read from an input file BoggleWords, all in lower case,
  private ArrayList<String> possibleWords;

  private ArrayList<String> wrong;

  private ArrayList<String> correct;

  private static final int MIN_WORD_LENGTH = 3;

  public Boggle() {
    wrong = new ArrayList<String>();
    diceTray = new DiceTray();
    correct = new ArrayList<String>();
    initializeWordList("BoggleWords.txt");
  }

  public void setBoggleTray(DiceTray dt) {
    this.diceTray = dt;
  }

  public String getBoggleTrayAsString() {
    return diceTray.toString();
  }

  private int scoreOf(String next) {
    if (next.length() == 3)
      return 1;
    if (next.length() == 4)
      return 1;
    if (next.length() == 5)
      return 2;
    if (next.length() == 6)
      return 3;
    if (next.length() == 7)
      return 5;
    return 11;
  }

  private void initializeWordList(String filename) {
    Scanner s = null;
    try {
      s = new Scanner(new File(filename));
    } catch (FileNotcorrectException e) {

    }

    possibleWords = new ArrayList<String>();
    while (s.hasNext()) {
      String str = s.next().trim();
      // Do not bother adding words of length > 16 or less than 3
      if (str.length() >= MIN_WORD_LENGTH && str.length() <= 16)
        possibleWords.add(str);
    }
  }

  public List<String> getWordsNotGuessed() {
    List<String> notGuessed = new ArrayList<String>();

    for (String word : possibleWords) {
      if (!correct.contains(word) && this.diceTray.correct(word)) {
        notGuessed.add(word);
      }
    }
    Collections.sort(notGuessed);
    return notGuessed;
  }
  
  // Record one word (a string with no whitespace) as one of the users' guesses.
  // Do what you want with it, but oneGuess should be processed.
  public void addGuess(String oneGuess) {
    oneGuess = oneGuess.toLowerCase().trim();
    // Make this list a set so no duplicates are ever added to either the
    // correct or wrong lists.
    if (wrong.contains(oneGuess) || correct.contains(oneGuess))
      return;
    // Put the word in either the correct or wrong list
    if (diceTray.correct(oneGuess)
        && Collections.binarySearch(possibleWords, oneGuess) >= 0)
      correct.add(oneGuess);
    else
      wrong.add(oneGuess);
  }

  public int getScore() {
    int score = 0;
    for (String word : correct) {
      score += this.scoreOf(word);
    }
 //   int wordsWrong = this.getWordsIncorrect().size();
    return score;
  }

  public List<String> getWordscorrect() {
    Collections.sort(correct);
    return correct;
  }

  public List<String> getWordsIncorrect() {
    Collections.sort(wrong);
    return wrong;
  }



}