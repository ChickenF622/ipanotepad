package com.mcoskerm.ipanotepad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.StringBuffer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Used to interface with Android's file system for saving the dictions a user creates
 */
public class FileSystem
{
  private static FileSystem instance;
  private static final String TAG = "com.mcoskerm.ipanotepad.FileSystem";
  private String lastSavedFile;
  private Context context;

  private FileSystem()
  {
    this.lastSavedFile = "";
  }

  /**
   * Gets the singleton instance of the class
   * @return The singleton instance of the class
   */
  public static FileSystem getInstance(Context context)
  {
    if (instance == null)
    {
      instance = new FileSystem();
    }
    instance.context = context;
    return instance;
  }

  /**
   * Determines whether or not the storage is currently writable
   * @return Whether or not the storage is currently writable
   */
  private boolean isStorageWritable()
  {
    String state = Environment.getExternalStorageState();
    return state.equals(Environment.MEDIA_MOUNTED);
  }

  /**
   * Determines if there has been any files saved in this instance
   * @return If there has been any files saved in this instance
   */
  public boolean wasWritten()
  {
    return !this.lastSavedFile.isEmpty();
  }

  /**
   * Writes a file to the app's private storage
   */
  public void savePrivate(String filename, String content)
  {
    if (this.context == null)
    {
      Log.e(TAG, "Cannot access private files without context");
    }
    try
    {
      FileOutputStream fout = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
      fout.write(content.getBytes());
      fout.close();
    }
    catch (IOException err)
    {
      Log.e(TAG, "Unable to write private file");
    }
  }

  public String readPrivate(String filename)
  {
    if (this.context == null)
    {
      Log.e(TAG, "Cannot access private files without context");
    }
    try
    {
      FileInputStream fin = this.context.openFileInput(filename);
      byte[] content = new byte[(int) fin.getChannel().size()];
      fin.read(content);
      fin.close();
      return new String(content, "UTF-8");
    }
    catch (IOException err)
    {
      Log.e(TAG, "Unable to read private file");
    }
    return "";
  }

  /**
   * Saves the current content to the most recently saved document
   * @param content The content to save
   * @return Whether or not the save was successful
   */
  public boolean save(String content)
  {
    return this.save(this.lastSavedFile, content);
  }

  /**
   * Saves the current content to the specified filename
   * @param filename The name of the file to write to
   * @param content The content to save
   * @return Whether or not the save was successful
   */
  public boolean save(String filename, String content)
  {
    boolean success = false;
    if (this.isStorageWritable())
    {
      File documents = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
      this.lastSavedFile = filename;
      filename += ".txt";
      String dictionPath = "IPANotepad/dictions";
      File dictionDir = new File(documents, dictionPath);
      //Create any necessary directories
      if (!dictionDir.exists())
      {
        dictionDir.mkdirs();
      }
      try
      {
        File diction = new File(dictionDir, filename);
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
}
