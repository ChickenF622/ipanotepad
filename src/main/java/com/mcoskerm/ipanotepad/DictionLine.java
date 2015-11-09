package com.mcoskerm.ipanotepad;

/**
 * Class that handles all of the slashing for a single line within a diction
 */
public class DictionLine
{
  private static final String SLASH_CHAR = "/";
  private String diction;
  private String comment;
  private int pos;

  public DictionLine()
  {
    this("", "");
  }

  public DictionLine(String diciton, String comment)
  {
    this.diction = diction;
    this.comment = comment;
  }

  /**
   * Adds slashing to the given character if needed
   * @param c The character to add slashing to
   * @return The character with slashing if need
   */
  private String addSlashing(String c, int pos)
  {
    String text = c;
    //If it is the first letter then add a slash in front of it
    if (pos == 0)
    {
      text = SLASH_CHAR + text;
    }
    else if (pos == this.diction.length() - 1)//If it is the last letter then add a letter after it
    {
      text += SLASH_CHAR;
    }
    else if (c.equals(" ") && DictionConfig.slashingPref.equals("EACH_WORD"))
    {
      text = SLASH_CHAR + " " + SLASH_CHAR;
    }
    return text;
  }

  /**
   * Removes all of the slashing characters in the diction and trims the whitespace
   */
  private void normalize()
  {
    this.diction = this.diction.replaceAll(SLASH_CHAR, "").trim();
  }

  /**
   * Updates the slashing of the given text using the EACH_WORD rule, this is an expensive operation
   * and should only be used when the actual preference is changed
   */
  private void updateEachWord()
  {
    String newText = "";
    for (int i = 0; i < this.diction.length(); i++)
    {
      char c = this.diction.charAt(i);
      newText += this.addSlashing(String.valueOf(c), i);
    }
    this.diction = newText;
  }

  private void updateEntirePhrase()
  {
    this.diction = SLASH_CHAR + this.diction + SLASH_CHAR;
  }

  public void update()
  {
    this.normalize();
    if (DictionConfig.slashingPref.equals("EACH_WORD"))
    {
      this.updateEachWord();
    }
    else if (DictionConfig.slashingPref.equals("ENTIRE_PHRASE"))
    {
      this.updateEntirePhrase();
    }
  }

  public void add(String character, int pos)
  {
  }

  public int length()
  {
    return (this.diction + " - " + this.comment).length();
  }
}