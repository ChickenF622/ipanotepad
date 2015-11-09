package com.mcoskerm.ipanotepad;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class FileSelectActivity extends ListActivity
{
  private static final String TAG = "FileSelectActivity";

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    final Intent result = new Intent();
    File dictionDir = Diction.getInstance().getDir();
    final String[] dictions = dictionDir.list(new DictionFilter());
    Arrays.sort(dictions);
    //Generate the list view for all the files
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_2,
        android.R.id.text1,
        dictions);
    this.setListAdapter(adapter);
    this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int pos, long rowId)
          {
            result.putExtra(Intent.EXTRA_TEXT, dictions[pos]);
            FileSelectActivity.this.setResult(Activity.RESULT_OK, result);
            FileSelectActivity.this.finish();
          }
        });
  }
}
