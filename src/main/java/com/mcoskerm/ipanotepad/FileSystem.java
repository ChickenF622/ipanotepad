package com.mcoskerm.ipanotepad;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

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
  private FileSystem()
  {
    this.lastSavedFile = "";
  }

  /**
   * Determines whether or not the storage is currently writable
   * @return Whether or not the storage is currently writable
   */
  private boolean isStorageWritable()
  {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state);
  }

  /**
   * Gets the singleton instance of the class
   * @return The singleton instance of the class
   */
  public static FileSystem getInstance()
  {
    if (instance == null)
    {
      instance = new FileSystem();
    }
    return instance;
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
      File diction = new File(dictionDir, filename);
      try
      {
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
