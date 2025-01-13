package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {

  /** Adds the same value to both the correct and buggy AList implementations,
   * then checks that the results of three subsequent removeLast calls are the same. */
  @Test
  public void TestThreeAddThreeRemove() {
    AListNoResizing<Integer> correctList = new AListNoResizing<>();
    BuggyAList<Integer> buggyList = new BuggyAList<>();
    for (int i = 0; i < 3; i++) {
      correctList.addLast(i);
      buggyList.addLast(i);
    }
    assertEquals(correctList.size(), buggyList.size());
    for (int i = 0; i < 3; i++) {
      assertEquals(correctList.removeLast(), buggyList.removeLast());
    }
  }
}
