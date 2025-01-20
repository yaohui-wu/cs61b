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
        String message = "";
        while (true) {
            int operation = StdRandom.uniform(4);
            if (operation == 0) {
                Integer n = StdRandom.uniform(10);
                student.addFirst(n);
                solution.addFirst(n);
                message += "addFirst(" + n + ")\n";
                assertEquals(message, student.get(0), solution.get(0));
            } else if (operation == 1) {
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
                Integer studentItem = student.removeFirst();
                Integer solutionItem = solution.removeFirst();
                message += "removeFirst(): " + studentItem + '\n';
                assertEquals(message, studentItem, solutionItem);
            } else if (operation == 3
                    && !student.isEmpty()
                    && !solution.isEmpty()) {
                Integer studentItem = student.removeLast();
                Integer solutionItem = solution.removeLast();
                message += "removeLast(): " + studentItem + "\n";
                assertEquals(message, studentItem, solutionItem);            }
        }
    }
}
