package com.mcoskerm.ipanotepad;

import java.io.File;

import android.app.Activity;
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
import android.widget.GridView;
import android.widget.ImageButton;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.mcoskerm.ipanotepad.ButtonAdapter;
import com.mcoskerm.ipanotepad.KeyboardClickListener;
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
      GridView keyboard = (GridView) this.findViewById(R.id.keyboard);
      //Adapater handles the OnClick event since buttons are being used in this GridView
      keyboard.setAdapter(new ButtonAdapter(this));
      //Handle the other keys that are not included in the GridView
      ImageButton backspace = (ImageButton) this.findViewById(R.id.backspace);
      Button emphasis = (Button) this.findViewById(R.id.emphasis);
      ImageButton spacebar = (ImageButton) this.findViewById(R.id.spacebar);
      emphasis.setOnClickListener(new KeyboardClickListener());
      spacebar.setOnClickListener(new KeyboardClickListener());
      backspace.setOnClickListener(new KeyboardClickListener(false));
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
        return true;
      case R.id.save_as:
        return true;
      /*case R.id.settings:
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
        return true;*/
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void attachBaseContext(Context newBase)
  {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }
}
