package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for (int N = 1_000; N <= 128_000; N *= 2) {
            Ns.addLast(N);
            AList<Integer> list = new AList<>();
            Stopwatch stopwatch = new Stopwatch();
            int opCount = 0;
            for (int i = 0; i < N; i++) {
                list.addLast(i);
                opCount++;
            }
            double time = stopwatch.elapsedTime();
            times.addLast(time);
            opCounts.addLast(opCount);
        }
        System.out.println("Timing table for addLast");
        printTimingTable(Ns, times, opCounts);
    }
}
