package com.mcoskerm.ipanotepad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsActivity extends Activity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    SettingsFragment settings = new SettingsFragment();
    this.getFragmentManager().beginTransaction()
      .replace(android.R.id.content, settings)
      .commit();
  }

  public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
  {
    private static final String KEY_PREF_SLASHING = "pref_slashing";
    private final String TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      this.addPreferencesFromResource(R.xml.preferences);
      this.getPreferenceManager().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
    {
      Log.d(TAG, "Setting changed");
      switch(key)
      {
        case KEY_PREF_SLASHING:
          String slashingPref = prefs.getString(key, "");
          Log.d(TAG, slashingPref);
          Diction.getInstance().setSlashing(slashingPref);
      }
    }

    @Override
    public void onResume()
    {
      super.onResume();
      this.getPreferenceManager().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause()
    {
      super.onPause();
      this.getPreferenceManager().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
    }
  }
}
