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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

class UtilityRowFragment extends Fragment
{
  private final String TAG = "UtilityRowFragment";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View fragment = inflater.inflate(R.layout.utility_row_fragment, container, false);
    return fragment;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    final Activity activity = this.getActivity();
    //Handle the other keys that are not included in the GridView
    int[] utilButtons = {R.id.newline, R.id.spacebar, R.id.normal_keyboard};
    for (int id: utilButtons)
    {
      ImageButton button = (ImageButton) activity.findViewById(id);
      if (id == R.id.normal_keyboard)
      {
        button.setOnClickListener(new OnClickListener()
          {
            @Override
            public void onClick(View view)
            {
              EditText notepad = (EditText) activity.findViewById(R.id.notepad);
              InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.showSoftInput(notepad, InputMethodManager.SHOW_FORCED);
            }
          });
      }
      else
      {
        button.setOnClickListener(new KeyboardClickListener());
      }
    }
  }
}

