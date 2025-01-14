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

  @Test
  public void randomizedTest() {
    AListNoResizing<Integer> L = new AListNoResizing<>();
    BuggyAList<Integer> buggyL = new BuggyAList<>();

    int N = 5_000;
    for (int i = 0; i < N; i += 1) {
        int operationNumber = StdRandom.uniform(0, 4);
        if (operationNumber == 0) {
            // addLast
            int randVal = StdRandom.uniform(0, 100);
            L.addLast(randVal);
            buggyL.addLast(randVal);
        } else if (operationNumber == 1) {
            // size
            int size = L.size();
            assertEquals(L.size(), buggyL.size());
        } else if (operationNumber == 2) {
          // getLast
          if (L.size() > 0 && buggyL.size() > 0) {
            assertEquals(L.getLast(), buggyL.getLast());
          }
        } else if (operationNumber == 3) {
          // removeLast
          if (L.size() > 0 && buggyL.size() > 0) {
            assertEquals(L.removeLast(), buggyL.removeLast());
          }
        }
    }
  }
}
