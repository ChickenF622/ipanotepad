package com.mcoskerm.ipanotepad;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.mcoskerm.ipanotepad.ButtonAdapter;
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
      case R.id.settings:
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
