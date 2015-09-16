package com.mcoskerm.ipanotepad;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcoskerm.ipanotepad.IPANotepadActivity;
import com.mcoskerm.ipanotepad.KeyboardClickListener;

public class ButtonAdapter extends BaseAdapter
{
  private static final String TAG = "ButtonAdapter";
  private Context context;
  private Resources res;
  private String[] buttonIds;

  public ButtonAdapter(Context context)
  {
    this.context = context;
    this.res = this.context.getResources();
    this.buttonIds = this.res.getStringArray(R.array.keyboard_keys);
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
    View key = new View(this.context);
    if (convertView == null)
    {
      String keyText = this.buttonIds[position];
      Log.d(TAG, "KEY: " + keyText);
      if (!keyText.equals(""))
      {
        Button button = new Button(this.context);
        button.setText(keyText);
        button.setAllCaps(false);
        key = (View) button;
      }
      int dim = (int) this.res.getDimension(R.dimen.keyboard_button_dim);
      key.setLayoutParams(new GridView.LayoutParams(dim, dim));
      key.setPadding(10, 10, 10, 10);
    }
    else
    {
      key = (Button) convertView;
    }
    //Get the character the button corresponds to and add it to the notepad
    key.setOnClickListener(new KeyboardClickListener());
    return key;
  }
}
