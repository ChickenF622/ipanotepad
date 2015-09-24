package com.mcoskerm.ipanotepad;

import java.io.File;

import android.os.Environment;

public class FileSystem
{
  private static FileSystem instance;
  private FileSystem() {}

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
    if (this.isStorageWritable())
    {
      return true;
    }
    return false;
  }

  public boolean exists(String filename)
  {
    return true;
  }
}
