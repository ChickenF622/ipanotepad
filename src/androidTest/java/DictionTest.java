package com.mcoskerm.ipanotepad;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;

public class DictionTest extends ActivityInstrumentationTestCase2<IPANotepadActivity>
{
  private Diction diction;
  private EditText notepad;
  private Activity activity;
  private Intent intent;

  public DictionTest()
  {
    super(IPANotepadActivity.class);
  }

  public void setUp() throws Exception
  {
    super.setUp();
    this.injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    this.activity = this.getActivity();
    this.diction = Diction.getInstance();
    this.notepad = (EditText) this.activity.findViewById(R.id.notepad);
    this.diction.setNotepad(notepad);
  }

  private void setCursor(int pos)
  {
    this.notepad.setSelection(pos);
  }

  public void testGetDir()
  {
    assertNotNull(this.diction.getDir());
  }

  @UiThreadTest
  public void testGetText()
  {
    this.diction.setText("TEST");
    assertEquals(this.diction.getText(), "TEST");
  }

  @UiThreadTest
  public void testSetSlashing()
  {
    this.diction.setText("TEST THIS DICTION");
    this.diction.setSlashing("NONE");
    assertEquals(this.diction.getText(), "TEST THIS DICTION");
    this.diction.setSlashing("EACH_WORD");
    assertEquals(this.diction.getText(), "/TEST/ /THIS/ /DICTION/");
    this.diction.setSlashing("ENTIRE_PHRASE");
    assertEquals(this.diction.getText(), "/TEST THIS DICTION/");
  }

  @UiThreadTest
  public void testModify()
  {
    Button key = new Button(activity);
    ImageButton newline = (ImageButton) this.activity.findViewById(R.id.newline);
    ImageButton space = (ImageButton) this.activity.findViewById(R.id.spacebar);
    this.diction.setText("");
    this.diction.setSlashing("NONE");
    key.setText("a");
    this.diction.modify(key, true);
    assertEquals(this.diction.getText(), "a");
    key.setText("b");
    this.diction.modify(key, true);
    assertEquals(this.diction.getText(), "ab");
    this.setCursor(0);
    this.diction.modify(key, true);
    assertEquals(this.diction.getText(), "bab");
    this.diction.setSlashing("EACH_WORD");
    this.setCursor(2);
    this.diction.modify(space, true);
    assertEquals(this.diction.getText(), "/b/ /ab/");
    assertEquals(this.diction.getText(), "/b/ /ab/");
    this.diction.modify(key, true);
    assertEquals(this.diction.getText(), "/b/ /bab/");
    this.setCursor(8);
    this.diction.modify(space, true);
    assertEquals(this.diction.getText(), "/b/ /bab/ /");
    this.diction.modify(key, true);
    this.diction.modify(key, true);
    assertEquals(this.diction.getText(), "/b/ /bab/ /bb");
    this.diction.modify(newline, true);
    assertEquals(this.diction.getText(), "/b/ /bab/ /bb/\n/");
    this.diction.modify(key, true);
    this.diction.modify(key, true);
    this.diction.modify(space, true);
    assertEquals(this.diction.getText(), "/b/ /bab/ /bb/\n/bb/ /");
    this.diction.modify(null, false);
    assertEquals(this.diction.getText(), "/b/ /bab/ /bb/\n/bb");
    this.diction.modify(null, false);
    assertEquals(this.diction.getText(), "/b/ /bab/ /bb/\n/b");
    this.diction.modify(null, false);
    this.diction.modify(null, false);
    assertEquals(this.diction.getText(), "/b/ /bab/ /bb");
    this.setCursor(0);
    this.diction.modify(null, false);
    assertEquals(this.diction.getText(), "/b/ /bab/ /bb");
  }

  @UiThreadTest
  public void testSave()
  {
    this.diction.setSlashing("NONE");
    this.diction.setText("TEST STRING");
    this.diction.setComment("TEST COMMENT");
    this.diction.save("UnitTest");
    this.diction.setText("Different");
    this.diction.setComment("Different Comment");
    this.diction.load("UnitTest");
    assertEquals(this.diction.getText(), "TEST STRING");
    assertEquals(this.diction.getComment(), "TEST COMMENT");
  }
}
