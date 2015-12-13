package com.mcoskerm.ipanotepad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
   * Gets the directory that holds all the saved dictions
   * @return The directory that holds all the saved dictions
   * @throws FileNotFoundException If there is no context for the FileSystem instance
   */
  public File getDictionDir() throws FileNotFoundException
  {
    this.checkForContext();
    return new File(this.context.getFilesDir(), "dictions");
  }

  /**
   * Checks to see if the FileSystem instance has a Context instance to operate on if it doesn't
   * a FileNotFoundException is thrown since the file cannot be access
   * @throws FileNotFoundException If the FileSystem instance doesn't have a Context instance
   */
  private void checkForContext() throws FileNotFoundException
  {
    if (this.context == null)
    {
      throw new FileNotFoundException("Unable to access file without context");
    }
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
   * @param filename The name of the file to save
   * @param content The content to write to the file
   * @throws IOException If there is any error writing the file
   */
  public void save(String filename, String content) throws IOException
  {
    this.checkForContext();
    File dictionDir = this.getDictionDir();
    if (!dictionDir.exists())
    {
      dictionDir.mkdirs();
    }
    File diction = new File(dictionDir, filename);
    FileOutputStream fout = new FileOutputStream(diction);
    try
    {
      fout.write(content.getBytes());
    }
    finally
    {
      fout.close();
    }
  }

  /**
   * Saves the current content to the most recently saved document
   * @param content The content to save
   * @throws IOException If there is any error writing the file
   */
  public void save(String content) throws IOException
  {
    this.save(this.lastSavedFile, content);
  }

  /**
   * Reads the file of the given filename and returns its contents
   * @param filename The name of the file to read
   * @return The content of the file of the given filename
   * @throws IOException If there is any error reading the file
   */
  public String read(String filename) throws IOException
  {
    this.checkForContext();
    File diction = new File(this.getDictionDir(), filename);
    BufferedReader reader = new BufferedReader(new FileReader(diction));
    try
    {
      StringBuilder builder = new StringBuilder();
      String line = reader.readLine();
      while (line != null)
      {
        builder.append(line);
        builder.append("\n");
        line = reader.readLine();
      }
      return new String(builder.toString());
    }
    finally
    {
      reader.close();
    }
  }
}
