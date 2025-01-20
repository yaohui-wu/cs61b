package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {
    
    @Test
    public void ArrayDequeTest() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        while (true) {
            int operation = StdRandom.uniform(4);
            if (operation == 0) {
                Integer n = StdRandom.uniform(10);
                student.addFirst(n);
                solution.addFirst(n);
                assertEquals(student.get(0), solution.get(0));
            } else if (operation == 1) {
                Integer n = StdRandom.uniform(10);
                student.addLast(n);
                solution.addLast(n);
                assertEquals(
                    student.get(student.size() - 1),
                    solution.get(solution.size() - 1)
                );
            } else if (operation == 2) {
                assertEquals(!student.isEmpty(), !solution.isEmpty());
                assertEquals(student.removeFirst(), solution.removeFirst());
            } else {
                assertEquals(!student.isEmpty(), !solution.isEmpty());
                assertEquals(student.removeLast(), solution.removeLast());
            }
        }
    }
}