package com.mcoskerm.ipanotepad;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import com.mcoskerm.ipanotepad.IPANotepadActivity;

public class ButtonAdapter extends BaseAdapter
{
  private Context context;
  private String[] buttonIds;// = getStringArray(R.array.keyboard_keys);

  public ButtonAdapter(Context context)
  {
    this.context = context;
    Resources res = this.context.getResources();
    this.buttonIds = res.getStringArray(R.array.keyboard_keys);
  }

  public int getCount()
  {
    return buttonIds.length;
  }

  public Object getItem(int pos)
  {
    return null;
  }

  public long getItemId(int pos)
  {
    return 0;
  }

  public View getView(int position, View convertView, ViewGroup parent)
  {
    View button;
    if (convertView == null)
    {
      String keyText = this.buttonIds[position];
      if (keyText.equals("BACKSPACE"))
      {
        ImageButton imgButton = new ImageButton(this.context);
        imgButton.setImageResource(R.drawable.ic_backspace_white_24dp);
        button = imgButton;
      }
      else
      {
        Button txtButton = new Button(this.context);
        txtButton.setText(keyText);
        txtButton.setAllCaps(false);
        button = txtButton;
      }
      button.setLayoutParams(new GridView.LayoutParams(150, 150));
      button.setPadding(10, 10, 10, 10);
    }
    else
    {
      button = (Button) convertView;
    }
    //Get the character the button corresponds to and add it to the notepad
    button.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        View root = view.getRootView();
        Button button = (Button) view;
        EditText notepad = (EditText) root.findViewById(R.id.notepad);
        notepad.append(button.getText());
      }
    });
    return button;
  }
}
