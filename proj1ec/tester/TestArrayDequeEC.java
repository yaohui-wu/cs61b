package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {
    
    @Test
    /** A randomized unit test for ArrayDeque.
     *  @author Yaohui Wu
     */
    public void ArrayDequeTest() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        String message = "";
        while (true) {
            int operation = StdRandom.uniform(4);
            if (operation == 0) {
                // Test addFirst.
                Integer n = StdRandom.uniform(10);
                student.addFirst(n);
                solution.addFirst(n);
                message += "addFirst(" + n + ")\n";
                Integer expected = solution.get(0);
                Integer actual = student.get(0);
                assertEquals(message, expected, actual);
            } else if (operation == 1) {
                // Test addLast.
                Integer n = StdRandom.uniform(10);
                student.addLast(n);
                solution.addLast(n);
                message += "addLast(" + n + ")\n";
                Integer expected = solution.get(solution.size() - 1);
                Integer actual = student.get(student.size() - 1);
                assertEquals(message, expected, actual);
            } else if (operation == 2
                    && !student.isEmpty()
                    && !solution.isEmpty()) {
                // Test removeFirst.
                Integer expected = solution.removeFirst();
                Integer actual = student.removeFirst();
                message += "removeFirst()\n";
                assertEquals(message, expected, actual);
            } else if (operation == 3
                    && !student.isEmpty()
                    && !solution.isEmpty()) {
                // Test removeLast.
                Integer expected = solution.removeLast();
                Integer actual = student.removeLast();
                message += "removeLast()\n";
                assertEquals(message, expected, actual);
            }
        }
    }
}
