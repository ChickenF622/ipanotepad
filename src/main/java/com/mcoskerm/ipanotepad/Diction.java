package com.mcoskerm.ipanotepad;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Diction
{
  public static final String CACHE_NAME = "__current_diction__";
  private final String SLASH_CHAR = "/";
  private final String TAG = "Diction";
  private static Diction instance;
  private Context context;
  private EditText notepad;
  private String slashingPref;
  private String text;

  private Diction(EditText notepad)
  {
    this.notepad = notepad;
    this.text = "";
    this.slashingPref = "EACH_WORD";//TODO get default value from resource instead of hard code
  }

  public static Diction getInstance()
  {
    if (instance == null)
    {
      instance = new Diction(null);
      Log.d("Diction", "New Instance");
    }
    return instance;
  }

  /**
   * Moves the cursor of the EditText to the given position
   * @param pos The position to move the cursor to
   */
  private void setCursor(int pos)
  {
    this.notepad.setSelection(pos);
  }

  /**
   * Logs an error the occurred within the Diction class
   * @param msg The message to display with the error
   * @param err The error that occured
   */
  private void logError(String msg, Throwable err)
  {
    Log.e(TAG, msg, err);
    Toast toast = Toast.makeText(this.context, msg, Toast.LENGTH_SHORT);
    toast.show();
  }

  /**
   * Gets the directory that holds all the dicitons
   * @return The directory that holds all the dictions
   */
  public File getDir()
  {
    File dir = null;
    try
    {
      dir = FileSystem.getInstance(this.context).getDictionDir();
    }
    catch (IOException err)
    {
      this.logError("Error trying to access dictions files", err);
    }
    return dir;
  }

  /**
   * Gets the text of the diction
   * @return The text of the diction
   */
  public String getText()
  {
    return this.text;
  }

  public void setText(String text)
  {
    this.text = text;
    this.notepad.setText(text);
  }

  /**
   * Gets the text from the notepad Edittext directly
   * @return The text of the notepad EditText
   */
  private String getNotepadText()
  {
    return this.notepad.getText().toString();
  }

  /**
   * Sets the notepad EditText that the class will modify
   * @param notepad The EditText that the class will modify
   */
  public void setNotepad(EditText notepad)
  {
    this.notepad = notepad;
    this.text = this.getNotepadText();
    this.context = notepad.getContext();
  }

  /**
   * Determines whether or not the current working diciton is empty
   */
  public boolean isEmpty()
  {
    return this.getNotepadText().isEmpty();
  }

  /**
   * Changes the current slashing preference and updates the text accordingly
   * @param slashingPref The slasing preference to change to
   */
  public void setSlashing(String slashingPref)
  {
    this.slashingPref = slashingPref;
    this.updateSlashing();
  }

  /**
   * Removes all of the slashing characters in the diction and trims the whitespace
   * @param text The text to normalize
   * @return The normalized text
   */
  private String normalize(String text)
  {
    return text.replaceAll(SLASH_CHAR, "").trim();
  }


  /**
   * Adds the given key to the diction
   * @param key The key to add to the diction
   * @return The diction with the key added
   */
  private String add(View key)
  {
    String character = " ";
    //If it is a button then it is assumed that it is not the spacebar
    if (key instanceof Button)
    {
      Button button = (Button) key;
      character = (String) button.getText();
    }
    else if (key.getId() == R.id.newline)
    {
      character = "\n";
    }
    return character;
  }

  /**
   * Adds slashing to the given character if needed
   * @param c The character to add slashing to
   * @return The character with slashing if need
   */
  private String addSlashing(String c)
  {
    String text = c;
    if (c.equals(" ") && this.slashingPref.equals("EACH_WORD"))
    {
      text = SLASH_CHAR + " " + SLASH_CHAR;
    }
    return text;
  }

  /**
   * Updates the slashing of the diciton based on the slashing preference
   */
  private void updateSlashing()
  {
    String text = this.getNotepadText();
    //TODO have case values derived from resource instead of hard code
    switch(this.slashingPref)
    {
      case "EACH_WORD":
        text = this.updateSlashing(text);
        break;
      case "ENTIRE_PHRASE":
        //Remove all slash characters and add them to the beginning and ending of the diction
        text = SLASH_CHAR + this.normalize(text) + SLASH_CHAR;
        break;
      case "NONE":
        text = this.normalize(text);
        break;
    }
    this.setText(text);
  }

  /**
   * Updates the slashing of the given text using the EACH_WORD rule, this is an expensive operation
   * and should only be used when the actual preference is changed
   * @param text The text to change the slashing for
   * @return The text with its slashing changed
   */
  private String updateSlashing(String text)
  {
    text = this.normalize(text);
    String newText = SLASH_CHAR.toString();
    for (char c: text.toCharArray())
    {
      newText += this.addSlashing(String.valueOf(c));
    }
    newText += SLASH_CHAR;
    return newText;
  }

  /**
   * Gives the position of the text that needs to be removed
   * @param text The text that is having characters removed from it
   * @param start The initial start of the selection
   * @param end The initial end of the selection
   * @return The new start position that will remove the characters that are needed depending on the slashing rules
   */
  private int remove(String text, int start, int end)
  {
    //No selection has been made so delete the character to the left of the cursor
    if (start == end)
    {
      //Check to see if we are deleting slashing
      int removeCount = 1;
      String delChar = String.valueOf(text.charAt(start - 1));
      if (delChar.equals(SLASH_CHAR))
      {
        removeCount += 2;
      }
      start = Math.max(start - removeCount, 0);
    }
    return start;
  }

  /**
   * Loads the saved diction of the given filename
   * @param filename The name of the file that holds the diction to load
   */
  public void load(String filename)
  {
    FileSystem fs = FileSystem.getInstance(this.context);
    try
    {
      String diction = fs.read(filename);
      this.setText(diction);
    }
    catch (IOException err)
    {
      this.logError("Unable to read diciton", err);
    }
  }

  /**
   * Modifies the current working diction based on the given key
   * @param key The key that caused the modification
   * @param add Whether or not the key is adding or deleting from the diction
   */
  public void modify(View key, boolean add)
  {
    int start = Math.max(this.notepad.getSelectionStart(), 0);
    int end = Math.max(this.notepad.getSelectionEnd(), 0);
    Editable text = this.notepad.getText();
    //Assume that the space should be the character appended
    String character = "";
    if (add)
    {
      character = this.add(key);
    }
    else //Backspace was clicked so delete a the selection instead of replacing it
    {
      character = "";
      start = this.remove(text.toString(), start, end);
    }
    //Check to see if any slashing needs to be done
    int cursorPos = start;
    character = this.addSlashing(character);
    //If slashing was added
    if (this.slashingPref.equals("EACH_WORD") && character.contains(" "))
    {
      cursorPos += 2;
    }
    //If any characters were selected then they will be replaced otherwise this will simply
    //add the character where the cursor is
    text = text.replace(start, end, character);
    this.setText(text.toString());
    //Move the cursor back to the posistion it would be relative to the new text
    if (add)
    {
      cursorPos++;
    }
    this.notepad.setSelection(cursorPos);
  }

  /**
   * Reloads the previously auto-saved diction back into the notepad
   * @param context The context of the app
   */
  public void reload()
  {
    this.load(CACHE_NAME);
  }

  /**
   * Resaves the current working diction using the previously used filename
   */
  public void resave()
  {
    FileSystem fs = FileSystem.getInstance(this.context);
    try
    {
      fs.save(this.getNotepadText());
    }
    catch(IOException err)
    {
      this.logError("An error occured when resaving the current diction", err);
    }
  }

  /**
   * Saves the current working diction in the app's private files
   * @param context The context of the app used to access its private files
   */
  public void save()
  {
    this.save(CACHE_NAME);
  }

  /**
   * Saves the current working diction under the given filename
   */
  public void save(String filename)
  {
    try
    {
      FileSystem fs = FileSystem.getInstance(this.context);
      fs.save(filename, this.getNotepadText());
    }
    catch(IOException err)
    {
      this.logError("An error occured when saving the current diction", err);
    }
  }

  /**
   * Determines if this diction has been saved
   * @return Whether or not this diciton has been saved
   */
  public boolean wasSaved()
  {
    return FileSystem.getInstance(null).wasWritten();
  }
}
