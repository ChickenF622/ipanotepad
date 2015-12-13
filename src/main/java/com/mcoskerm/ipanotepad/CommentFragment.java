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

public class CommentFragment extends DialogFragment
{

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    final Resources res = this.getResources();
    final Diction diction = Diction.getInstance();
    AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
    LayoutInflater inflater = this.getActivity().getLayoutInflater();
    final View view = inflater.inflate(R.layout.dialog_comment, null);
    final EditText commentField = (EditText) view.findViewById(R.id.comment);
    commentField.setText(diction.getComment());
    builder.setView(view)
      .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
              AlertDialog alertDialog = (AlertDialog) dialog;
              diction.setComment(commentField);
            }
          })
      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
              CommentFragment.this.getDialog().cancel();
            }
          });
      return builder.create();
  }
}
