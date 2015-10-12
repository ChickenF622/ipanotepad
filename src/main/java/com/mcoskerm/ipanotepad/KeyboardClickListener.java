package com.mcoskerm.ipanotepad;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

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
    Diction.getInstance().modify(view, this.add);
  }
}
