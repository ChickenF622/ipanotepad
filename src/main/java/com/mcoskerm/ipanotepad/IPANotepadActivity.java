package com.mcoskerm.ipanotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IPANotepadActivity extends Activity
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    setContentView(R.layout.main);
    NotepadFragment notepad = new NotepadFragment();
    this.getFragmentManager().beginTransaction()
      .add(R.id.notepad_container, notepad, "notepad_fragment")
      .commit();
    GridView keyboard = (GridView) this.findViewById(R.id.keyboard_main);
    //Adapater handles the OnClick event since buttons are being used in this GridView
    keyboard.setAdapter(new ButtonAdapter(this));
    //Handle the other keys that are not included in the GridView
    ImageButton backspace = (ImageButton) this.findViewById(R.id.newline);
    Button emphasis = (Button) this.findViewById(R.id.emphasis);
    ImageButton spacebar = (ImageButton) this.findViewById(R.id.spacebar);
    emphasis.setOnClickListener(new KeyboardClickListener());
    spacebar.setOnClickListener(new KeyboardClickListener());
    backspace.setOnClickListener(new KeyboardClickListener());
  }

  @Override
  public void onPause()
  {
    super.onPause();
    Diction diction = Diction.getInstance();
    diction.save();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = this.getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    Intent intent;
    Diction diction = Diction.getInstance();
    switch (item.getItemId())
    {
      case R.id.save:
        //If this file has already been written then write the content directly
        if (diction.wasSaved())
        {
          diction.resave();
        }
        else
        {
          this.displaySaveAs();
        }
        return true;
      case R.id.save_as:
        this.displaySaveAs();
        return true;
      case R.id.settings:
        intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
        return true;
      case R.id.share:
        intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, diction.getText());
        intent.setType("text/plain");
        //intent = new Intent(this, FileSelectActivity.class);
        this.startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  /**
   * Displays the save as fragment
   */
  private void displaySaveAs()
  {
    DialogFragment saveAsFragment = new SaveAsFragment();
    saveAsFragment.show(getFragmentManager(), "save_as");
  }

  /**
   * Adding the CalligraphyContextWrapper to the base context of the app to allow for the font to be modified
   */
  @Override
  public void attachBaseContext(Context newBase)
  {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }
}
