package com.mcoskerm.ipanotepad;

class Slashing
{
  private static String pref = "EACH_WORD";//TODO get default value from resource instead of hard code
  private static String diction = "";
  private static final String SLASH_CHAR = "/";

  public static void setPref(String newPref)
  {
    pref = newPref;
  }

  public static String getPref()
  {
    return pref;
  }

  /**
   * Removes all of the slashing characters in the diction and trims the whitespace
   */
  private static void normalize()
  {
    diction = diction.replaceAll(SLASH_CHAR, "").trim();
  }

  /**
   * Adds slashing to the given character if needed
   * @param c The character to add slashing to
   * @return The character with slashing if need
   */
  public static String updateChar(String c, int pos)
  {
    String text = c;
    //If it is the first letter then add a slash in front of it
    if (!pref.equals("NONE"))
    {
      if ((c.equals(" ") || c.equals("\n")) && pref.equals("EACH_WORD"))
      {
        text = SLASH_CHAR + c + SLASH_CHAR;
      }
      else if (pos == 0)
      {
        text = SLASH_CHAR + text;
      }
      else if (pos == diction.length() - 1)//If it is the last letter then add a letter after it
      {
        text += SLASH_CHAR;
      }
    }
    return text;
  }

  /**
   * Updates the slashing of the given text using the EACH_WORD rule, this is an expensive operation
   * and should only be used when the actual preference is changed
   */
  private static String updateEachWord()
  {
    String newText = "";
    for (int i = 0; i < diction.length(); i++)
    {
      char c = diction.charAt(i);
      newText += updateChar(String.valueOf(c), i);
    }
    return newText;
  }

  private static String updateEntirePhrase()
  {
    return SLASH_CHAR + diction + SLASH_CHAR;
  }

  public static String update(String text)
  {
    diction = text;
    normalize();
    if (pref.equals("EACH_WORD"))
    {
      return updateEachWord();
    }
    else if (pref.equals("ENTIRE_PHRASE"))
    {
      return updateEntirePhrase();
    }
    else
    {
      return diction;
    }
  }
}
