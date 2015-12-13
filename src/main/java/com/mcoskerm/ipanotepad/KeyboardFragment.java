package com.mcoskerm.ipanotepad;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageButton;

public class KeyboardFragment extends Fragment
{
  private final String TAG = "KeyboardFragment";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View fragment = inflater.inflate(R.layout.keyboard_fragment, container, false);
    return fragment;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    final Activity activity = this.getActivity();
    GridView keyboard = (GridView) activity.findViewById(R.id.keyboard_main);
    //Adapater handles the OnClick event since buttons are being used in this GridView
    keyboard.setAdapter(new ButtonAdapter(activity));
    //Handle the other keys that are not included in the GridView
  }
}

