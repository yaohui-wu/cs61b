package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> implements Comparable<T> {
    private Comparator<T> comparator;
    /** Creates a MaxArrayDeque with the given comparator. */
    public MaxArrayDeque(Comparator<T> c) {
        MaxArrayDeque = new ArrayDeque();
        comparator = c;
    }

    /** Returns the maximum element in the deque as governed by the previously
     *  given comparator. If the MaxArrayDeque is empty, simply return null.
     */
    public T max() {
        if (isEmpty()) {
            return null;
        }
        T max = get(0);
        for (int i = 1; i < size; i += 1) {
            T item = get(i);
            if (c.compare(item, max) > 0) {
                max = item;
            }
        }
        return max;
    }

    /** Returns the maximum element in the deque as governed by the parameter
     *  comparator c. If the MaxArrayDeque is empty, simply return null.
     */
    public T max(Comparator<T> c) {
        comparator = c;
        return max();
    }
}
