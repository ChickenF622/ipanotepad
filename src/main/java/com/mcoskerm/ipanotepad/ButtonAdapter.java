package com.mcoskerm.ipanotepad;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
      if (!keyText.equals(""))
      {
        if (keyText.equals("BACKSPACE"))
        {
          ImageButton imgButton = new ImageButton(this.context);
          Drawable icon = this.res.getDrawable(R.drawable.ic_backspace_white);
          imgButton.setImageDrawable(icon);
          imgButton.setOnClickListener(new KeyboardClickListener(false));
          key = (View) imgButton;
        }
        else
        {
          Button textButton = new Button(this.context);
          textButton.setText(keyText);
          textButton.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "fonts/LGSmartGothic-Regular.ttf"));
          textButton.setAllCaps(false);
          textButton.setOnClickListener(new KeyboardClickListener());
          key = (View) textButton;
        }
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
    return key;
  }
}
