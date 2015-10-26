package com.mcoskerm.ipanotepad;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class FileSelectActivity extends ListActivity
{
  private static final String TAG = "FileSelectActivity";
  private File[] dictions;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    final Intent resultIntent = new Intent("com.mcoskerm.ipanotepad.ACTION_RETURN_FILE");
    File dictionDir = Diction.getInstance().getDir();
    this.dictions = dictionDir.listFiles();
    this.setResult(Activity.RESULT_CANCELED, null);
    Arrays.sort(this.dictions);
    ArrayList<String> dictionNames = new ArrayList<>();
    for (File diction: dictions)
    {
      if (!diction.getName().equals(Diction.CACHE_NAME))
      {
        dictionNames.add(diction.getName());
      }
    }
    ArrayAdapter adapter = new ArrayAdapter(this,
        android.R.layout.simple_list_item_2,
        android.R.id.text1,
        dictionNames);
    this.setListAdapter(adapter);
    this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int pos, long rowId)
          {
            File diction = dictions[pos];
            Uri dictionUri = null;
            try
            {
              dictionUri = FileProvider.getUriForFile(FileSelectActivity.this,
                  "com.mcoskerm.ipanotepad.fileprovider",
                  diction);
            }
            catch (IllegalArgumentException e)
            {
              Log.e(TAG, "The selected file cannot be shared: " + diction.getName());
            }
            if (dictionUri != null)
            {
                resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                resultIntent.setDataAndType(dictionUri, getContentResolver().getType(dictionUri));
                FileSelectActivity.this.setResult(Activity.RESULT_CANCELED, resultIntent);
            }
          }
        });
  }
}
