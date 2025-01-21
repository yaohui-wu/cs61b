package bstmap;

/** Map based on a binary search tree (BST).
 *  @author Yaohui Wu
 */
public class BSTMap implements Map61B<K, V> {
    private Node root; // Root of the BST.

    /** BST node with a key value pair. */
    private class Node {
        private K key; // Sorts by key.
        private V value; // Associated data.
        private Node left, right; // Left and right subtrees.
        private int size; // Numbers of nodes in the subtrees.

        public Node(K newKey, V newValue, int newSize) {
            key = newKey;
            value = newValue;
            size = newSize;
            left = null;
            right = null;
        }
    }

    /** Initializes an empty map. */
    public BSTMap() {
        root = null;
    }
    
    /** Removes all of the mappings from this map. */
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        throw new UnsupportedOperationException();
    }

    /** Returns the value to which the specified key is mapped, or null if
     *  this map contains no mapping for the key.
     */
    public V get(K key) {
        throw new UnsupportedOperationException();
    }

    /** Returns the number of key-value mappings in this map. */
    int size() {
        throw new UnsupportedOperationException();
    }

    /** Associates the specified value with the specified key in this map. */
    void put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /** Returns a Set view of the keys contained in this map. Not required for
     *  Lab 7.
     *  If you don't implement this, throw an UnsupportedOperationException.
     */
    public Set<K> keySet(); {
        throw new UnsupportedOperationException();
    }

    /** Removes the mapping for the specified key from this map if present.
     *  Not required for Lab 7. If you don't implement this, throw an
     *  UnsupportedOperationException.
     */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /** Removes the entry for the specified key only if it is currently mapped to
     *  the specified value. Not required for Lab 7. If you don't implement this,
     *  throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /** Prints the BSTMap in order of increasing key. */
    public void printInOrder() {
        throw new UnsupportedOperationException();
    }
}
