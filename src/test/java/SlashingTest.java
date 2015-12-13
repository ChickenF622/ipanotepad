package com.mcoskerm.ipanotepad;

import junit.framework.TestCase;

public class SlashingTest extends TestCase
{
  private String diction;

  public SlashingTest()
  {
    super("DictionLine Tests");
  }

  public void setUp()
  {
    this.diction = "TEST DICTION FOR TESTING";
    Slashing.setPref("NONE");
  }

  public void testUpdate()
  {
    Slashing.setPref("EACH_WORD");
    this.diction = Slashing.update(this.diction);
    assertEquals(this.diction, "/TEST/ /DICTION/ /FOR/ /TESTING/");
    Slashing.setPref("ENTIRE_PHRASE");
    this.diction = Slashing.update(this.diction);
    assertEquals(this.diction, "/TEST DICTION FOR TESTING/");
    Slashing.setPref("NONE");
    this.diction = Slashing.update(this.diction);
    assertEquals(this.diction, "TEST DICTION FOR TESTING");
  }

  public void testUpdateChar()
  {
    int end = this.diction.length() - 1;
    Slashing.update(this.diction);//Updates the diction the Slashing class is using
    Slashing.setPref("NONE");
    assertEquals(Slashing.updateChar("C", 0), "C");
    assertEquals(Slashing.updateChar("C", 5), "C");
    assertEquals(Slashing.updateChar("C", end), "C");
    Slashing.setPref("ENTIRE_PHRASE");
    assertEquals(Slashing.updateChar("C", 0), "/C");
    assertEquals(Slashing.updateChar("C", 5), "C");
    assertEquals(Slashing.updateChar("C", end), "C/");
    Slashing.setPref("EACH_WORD");
    assertEquals(Slashing.updateChar("C", 0), "/C");
    assertEquals(Slashing.updateChar("C", 5), "C");
    assertEquals(Slashing.updateChar("C", end), "C/");
    assertEquals(Slashing.updateChar(" ", 0), "/ /");
    assertEquals(Slashing.updateChar(" ", 5), "/ /");
    assertEquals(Slashing.updateChar(" ", end), "/ /");
  }
}
