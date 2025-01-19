package gh2;

import deque.Deque;

public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        // Initializes a buffer with the capacity and fills it with zeros.
        buffer = new ArrayDeque<>(capacity);
        for (int i = 0; i < capacity; i += 1) {
            buffer.addLast(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        /*
         * Dequeue everything in buffer, and replace with random numbers
         * between -0.5 and 0.5.
         */
        for (int i = 0; i < buffer.size(); i += 1) {
            buffer.removeFirst();
            double num = Math.random() - 0.5;
            buffer.addLast(num);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        /*
         * Dequeue the front sample and enqueue a new sample that is
         * the average of the two multiplied by the DECAY factor.
         */
        double front = buffer.removeFirst();
        double back = buffer.get(buffer.size() - 1);
        double newSample = (front + back) / 2 * DECAY;
        buffer.addLast(newSample);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}
