package com.mcoskerm.ipanotepad;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Generic click listener class that provides the behavoir for all of the keys in the keyboard
 */
public class KeyboardClickListener implements View.OnClickListener
{
  private boolean add;

  /**
   * Basic contstructor
   */
  public KeyboardClickListener()
  {
    super();
    this.add = true;
  }

  /**
   * Contructor to specify whether or not the key should add or delete a character
   * @param add If true it will add the character, if false it will remove a single character
   */
  public KeyboardClickListener(boolean add)
  {
    super();
    this.add = add;
  }

  /**
   * The onClick listener
   * @param view The view that the click event occured on
   */
  public void onClick(View view)
  {
    View root = view.getRootView();
    EditText notepad = (EditText) root.findViewById(R.id.notepad);
    int start = Math.max(notepad.getSelectionStart(), 0);
    int end = Math.max(notepad.getSelectionEnd(), 0);
    Editable text = notepad.getText();
    //Assume that the space should be the character appended
    String character = " ";
    if (this.add)
    {
      //If it is a button then it is assumed that it is not the spacebar
      if (view instanceof Button)
      {
        Button button = (Button) view;
        character = (String) button.getText();
      }
      else if (view.getId() == R.id.newline)
      {
        character = "\n";
      }
    }
    else //Backspace was clicked so delete a the selection instead of replacing it
    {
      character = "";
      //No selection has been made so delete the character to the left of the cursor
      if (start == end)
      {
        start = Math.max(start - 1, 0);
      }
    }
    text = text.replace(start, end, character);
    notepad.setText(text.toString());
    //Move the cursor back to the posistion it would be relative to the new text
    if (this.add)
    {
      notepad.setSelection(start + 1);
    }
    else
    {
      notepad.setSelection(start);
    }
  }
}
