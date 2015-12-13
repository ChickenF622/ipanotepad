package com.mcoskerm.ipanotepad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Diction
{
  public static final String CACHE_NAME = "__current_diction__";
  private final String TAG = "Diction";
  private static Diction instance;
  private Context context;
  private String comment;
  private EditText notepad;

  private Diction(EditText notepad)
  {
    this.notepad = notepad;
  }

  public static Diction getInstance()
  {
    if (instance == null)
    {
      instance = new Diction(null);
    }
    return instance;
  }

  /**
   * Gets the character from the given key
   * @param key The key to retrieve the character from
   * @return The character
   */
  private String getCharFromKey(View key)
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

  public String getComment()
  {
    return this.comment;
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
    return this.notepad.getText().toString();
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public void setComment(EditText commentField)
  {
    this.setComment(commentField.getText().toString());
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
   * Sets the notepad EditText that the class will modify
   * @param notepad The EditText that the class will modify
   */
  public void setNotepad(EditText notepad)
  {
    this.notepad = notepad;
    this.context = notepad.getContext();
  }

  /**
   * Changes the current slashing preference and updates the text accordingly
   * @param slashingPref The slasing preference to change to
   */
  public void setSlashing(String slashingPref)
  {
    Slashing.setPref(slashingPref);
    this.setText(Slashing.update(this.getText()));
  }

  /**
   * Sets the text of the diction, ie the notepad EditText
   * @param text The text for the diction to be set to
   */
  public void setText(String text)
  {
    this.notepad.setText(text);
  }

  /**
   * Determines whether or not the current working diciton is empty
   */
  public boolean isEmpty()
  {
    return this.getText().isEmpty();
  }

  public int length()
  {
    return this.getText().length();
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
   * Gives the position of the text that needs to be removed
   * @param text The text that is having characters removed from it
   * @param start The initial start of the selection
   * @param end The initial end of the selection
   * @return The new start position that will remove the characters that are needed depending on the slashing rules
   */
  private int remove(String text, int start, int end)
  {
    //No selection has been made so delete the character to the left of the cursor
    if (start == 0 && end == 0)
    {
      return -1;
    }
    if (start == end)
    {
      //Check to see if we are deleting slashing
      int removeCount = 1;
      String delChar = String.valueOf(text.charAt(start - 1));
      if (delChar.equals("/"))
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
    JSONParser parser = new JSONParser();
    try
    {
      JSONObject json = (JSONObject) parser.parse(fs.read(filename));
      this.setText((String) json.get("diction"));
      this.comment = (String) json.get("comment");
    }
    catch (IOException|ParseException err)
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
    System.out.println("H");
    if (add)
    {
      character = this.getCharFromKey(key);
      character = Slashing.updateChar(character, start);
    }
    else //Backspace was clicked so delete a the selection instead of replacing it
    {
      character = "";
      start = this.remove(text.toString(), start, end);
      if (start == -1)//Nothing to delete
      {
        return;
      }
    }
    //Check to see if any slashing needs to be done
    int cursorPos = start;
    //If any characters were selected then they will be replaced otherwise this will simply
    //add the character where the cursor is
    text = text.replace(start, end, character);
    String newDiction = text.toString().replaceAll("//", "/");
    Log.d(TAG, newDiction);
    this.setText(newDiction);
    //Move the cursor back to the posistion it would be relative to the new text
    if (add)
    {
      cursorPos += character.length();
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
      fs.save(this.getText());
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
   * Saves the current working diction under the given filename, also formats the data
   * to follow whatever the current slashing rules are
   * @param filename The name of the file to save the content to
   */
  public void save(String filename)
  {
    try
    {
      FileSystem fs = FileSystem.getInstance(this.context);
      JSONObject json = new JSONObject();
      String diction = Slashing.update(this.getText());
      json.put("diction", diction);
      json.put("comment", this.comment);
      fs.save(filename, json.toString());
      this.setText(diction);
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
