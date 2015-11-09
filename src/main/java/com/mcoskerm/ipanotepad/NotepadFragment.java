package com.mcoskerm.ipanotepad;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
    EditText notepad = (EditText) this.getView().findViewById(R.id.notepad);
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

