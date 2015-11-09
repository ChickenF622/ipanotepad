package com.mcoskerm.ipanotepad;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

class NotepadFragment extends Fragment
{
  private final String TAG = "NotepadFragment";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View fragment = inflater.inflate(R.layout.notepad_fragment, container, false);
    return fragment;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);
    final Activity activity = this.getActivity();
    EditText notepad = (EditText) this.getView().findViewById(R.id.notepad);
    notepad.setOnEditorActionListener(new OnEditorActionListener()
        {
          @Override
          public boolean onEditorAction(TextView view, int actionCode, KeyEvent event)
          {
            if (actionCode == EditorInfo.IME_ACTION_DONE)
            {
              InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
              view.setSingleLine(false);
              imm.toggleSoftInput(0, 0);
            }
            return false;
          }
        });
    Diction diction = Diction.getInstance();
    diction.setNotepad(notepad);
    //If no text is in the current working diction reload the auto-saved diction
    if (diction.isEmpty())
    {
      diction.reload();
    }
  }

  @Override
  public void onResume()
  {
    super.onResume();
    EditText notepad = (EditText) this.getView().findViewById(R.id.notepad);
    Diction diction = Diction.getInstance();
    notepad.setText(diction.getText());
  }

}

