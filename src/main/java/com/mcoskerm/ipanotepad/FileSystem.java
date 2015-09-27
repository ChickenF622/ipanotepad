package com.mcoskerm.ipanotepad;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileSystem
{
  private static FileSystem instance;
  private static final String TAG = "com.mcoskerm.ipanotepad.FileSystem";
  private FileSystem()
  {
  }

  private boolean isStorageWritable()
  {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state);
  }

  public static FileSystem getInstance()
  {
    if (instance == null)
    {
      instance = new FileSystem();
    }
    return instance;
  }

  public boolean save(String filename, String content)
  {
    boolean success = false;
    if (this.isStorageWritable())
    {
      File documents = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
      filename += ".txt";
      String dictionPath = "IPANotepad/dictions";
      File dictionDir = new File(documents, dictionPath);
      if (!dictionDir.exists())
      {
        dictionDir.mkdirs();
      }
      File diction = new File(dictionDir, filename);
      try
      {
        diction.createNewFile();
        FileOutputStream fout = new FileOutputStream(diction);
        fout.write(content.getBytes());
        fout.close();
        success = true;
      }
      catch (IOException err)
      {
        Log.e(TAG, "Unable to write file");
      }
    }
    return success;
  }

  public boolean exists(String filename)
  {
    return true;
  }
}
