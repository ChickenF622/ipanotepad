package com.mcoskerm.ipanotepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
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
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IPANotepadActivity extends Activity
{
  private final static String TAG = "IPANotepadActivity";
  public final static int OPEN_FILE = 0;//Request code for opening a file

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    setContentView(R.layout.main);
    NotepadFragment notepad = new NotepadFragment();
    KeyboardFragment keyboard = new KeyboardFragment();
    UtilityRowFragment utilRow = new UtilityRowFragment();
    this.getFragmentManager().beginTransaction()
      .add(R.id.notepad_container, notepad, "notepad_fragment")
      .add(R.id.keyboard_container_main, keyboard, "keyboard_fragment")
      .add(R.id.keyboard_container_utility_row, utilRow, "utility_row_fragment")
      .commit();
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
      case R.id.open:
        intent = new Intent(this, FileSelectActivity.class);
        this.startActivityForResult(intent, OPEN_FILE);
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
        this.startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (resultCode == Activity.RESULT_OK)
    {
      switch (requestCode)
      {
        case OPEN_FILE:
          String filename = data.getStringExtra(Intent.EXTRA_TEXT);
          Diction.getInstance().load(filename);
          break;
      }
    }
  }

  /**
   * Displays the save as fragment
   */
  private void displaySaveAs()
  {
    DialogFragment saveAsFragment = new SaveAsFragment();
    saveAsFragment.show(getFragmentManager(), "save_as");
    //Ensure the Dialog is not null when it is retrieved
    getFragmentManager().executePendingTransactions();
  }

  /**
   * Adding the CalligraphyContextWrapper to the base context of the app to allow for the font to be modified
   */
  /*@Override
  public void attachBaseContext(Context newBase)
  {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }*/
}
