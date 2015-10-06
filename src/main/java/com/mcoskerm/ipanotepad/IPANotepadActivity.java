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

import com.mcoskerm.ipanotepad.ButtonAdapter;
import com.mcoskerm.ipanotepad.FileSystem;
import com.mcoskerm.ipanotepad.KeyboardClickListener;
import com.mcoskerm.ipanotepad.SaveAsFragment;
import com.mcoskerm.ipanotepad.SettingsActivity;

public class IPANotepadActivity extends Activity
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
      super.onCreate(savedInstanceState);
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      setContentView(R.layout.main);
      FileSystem fs = FileSystem.getInstance(this);
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
      //Read any data from previous instance of hte notepad into the new instance
      EditText notepad = (EditText) this.findViewById(R.id.notepad);
      String prevDiction = fs.readPrivate("current_diction");
      notepad.setText(prevDiction);
  }

  @Override
  public void onPause()
  {
    FileSystem fs = FileSystem.getInstance(this);
    fs.savePrivate("current_diction", this.getNotepadContent());
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
    switch (item.getItemId())
    {
      case R.id.save:
        FileSystem fs = FileSystem.getInstance(null);
        //If this file has already been written then write the content directly
        if (fs.wasWritten())
        {
          fs.save(this.getNotepadContent());
        }
        else
        {
          this.displaySaveAs();
        }
        return true;
      case R.id.save_as:
        this.displaySaveAs();
        return true;
      /*case R.id.settings:
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
        return true;*/
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

  private String getNotepadContent()
  {
    EditText notepad = (EditText) this.findViewById(R.id.notepad);
    return notepad.getText().toString();
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
