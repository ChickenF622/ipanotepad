package com.mcoskerm.ipanotepad;

import junit.framework.TestCase;

public class DictionLineTest extends TestCase
{
  private DictionLine line;
  private String comment;
  private String diction;

  public DictionLineTest()
  {
    super("DictionLine Tests");
  }

  public void setUp()
  {
    this.diction = "TEST DICTION FOR TESTING";
    this.comment =  "TEST COMMENT FOR TESTING";
    this.line = new DictionLine(diction, comment);
    DictionConfig.slashingPref = "NONE";
  }

  public void testUpdate()
  {
    DictionConfig.slashingPref = "EACH_WORD";
    line.update();
    assertEquals(line.getText(), "/TEST/ /DICTION/ /FOR/ /TESTING/ - TEST COMMENT FOR TESTING");
    DictionConfig.slashingPref = "ENTIRE_PHRASE";
    line.update();
    assertEquals(line.getText(), "/TEST DICTION FOR TESTING/ - TEST COMMENT FOR TESTING");
    DictionConfig.slashingPref = "NONE";
    line.update();
    assertEquals(line.getText(), "TEST DICTION FOR TESTING - TEST COMMENT FOR TESTING");
  }

  public void testAdd()
  {
    line.add("Y", 1, false);
    assertEquals(line.getText(), "TYEST DICTION FOR TESTING - TEST COMMENT FOR TESTING");
    line.add("Y", 1, true);
    assertEquals(line.getText(), "TYEST DICTION FOR TESTING - TYEST COMMENT FOR TESTING");
  }

  public void testRemove()
  {
    line.remove(1, false);
    assertEquals(line.getText(), "TST DICTION FOR TESTING - TEST COMMENT FOR TESTING");
    line.remove(3, true);
    assertEquals(line.getText(), "TST DICTION FOR TESTING - TES COMMENT FOR TESTING");
    line.remove(0, true);
    assertEquals(line.getText(), "TST DICTION FOR TESTING - ES COMMENT FOR TESTING");
  }
}
