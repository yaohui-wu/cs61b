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
                assertEquals(message, student.get(0), solution.get(0));
            } else if (operation == 1) {
                // Test addLast.
                Integer n = StdRandom.uniform(10);
                student.addLast(n);
                solution.addLast(n);
                message += "addLast(" + n + ")\n";
                assertEquals(
                    message,
                    student.get(student.size() - 1),
                    solution.get(solution.size() - 1)
                );
            } else if (operation == 2
                    && !student.isEmpty()
                    && !solution.isEmpty()) {
                // Test removeFirst.
                Integer expected = student.removeFirst();
                Integer actual = solution.removeFirst();
                message += "removeFirst()\n";
                assertEquals(message, expected, actual);
            } else if (operation == 3
                    && !student.isEmpty()
                    && !solution.isEmpty()) {
                // Test removeLast.
                Integer expected = student.removeLast();
                Integer actual = solution.removeLast();
                message += "removeLast()\n";
                assertEquals(message, expected, actual);
            }
        }
    }
}
