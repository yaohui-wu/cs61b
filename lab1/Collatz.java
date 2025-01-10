/** Class that prints the Collatz sequence starting from a given number.
 *  @author Yaohui Wu
 */
public class Collatz {

    /**
     * Compute the next number in the Collatz sequence.
     * @param n
     * @return Next number in the Collatz sequence.
     */
    public static int nextNumber(int n) {
        if (n % 2 == 0) {
            // If the number is even, divide it by two.
            return n / 2;
        } else {
            // If the number is odd, triple it and add one.
            return 3 * n + 1;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

