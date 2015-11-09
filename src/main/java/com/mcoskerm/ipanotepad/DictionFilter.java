package com.mcoskerm.ipanotepad;

import java.io.File;
import java.io.FilenameFilter;

public class DictionFilter implements FilenameFilter
{
  public boolean accept(File dir, String name)
  {
    return !name.equals(Diction.CACHE_NAME);
  }
}
