package com.mcoskerm.ipanotepad;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mcoskerm.ipanotepad.FileSystem;

public class SaveAsFragment extends DialogFragment
{

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final FileSystem fs = FileSystem.getInstance(null);
    final Resources res = this.getResources();
    AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
    LayoutInflater inflater = this.getActivity().getLayoutInflater();
    final View view = inflater.inflate(R.layout.dialog_save_as, null);
    builder.setView(view)
      .setPositiveButton(R.string.save, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
              AlertDialog alertDialog = (AlertDialog) dialog;
              EditText filenameField = (EditText) view.findViewById(R.id.filename);
              EditText notepad = (EditText) alertDialog.getOwnerActivity().findViewById(R.id.notepad);
              String filename = filenameField.getText().toString();
              String content = notepad.getText().toString();
              fs.save(filename, content);
              //Include the name of the file that was written in the action bar so the user knows what file
              //they are working on
              ActionBar actionBar = alertDialog.getOwnerActivity().getActionBar();
              actionBar.setTitle(res.getString(R.string.app_name) + " - " + filename);
            }
          })
      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
              SaveAsFragment.this.getDialog().cancel();
            }
          });
      return builder.create();
  }
}
