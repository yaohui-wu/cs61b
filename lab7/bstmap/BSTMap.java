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
        private int size; // Numbers of nodes in the tree.

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
        root = null;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        if (key == root.key) {
            return true;
        } else if (key < root.key) {
            Node left = root.left;
            return left.containsKey(key);
        } else {
            Node right = root.right;
            return right.containsKey(key);
        }
        return false;
    }

    /** Returns the value to which the specified key is mapped, or null if
     *  this map contains no mapping for the key.
     */
    public V get(K key) {
        if (!containsKey()) {
            return null;
        }
        if (key == root.key) {
            return root.value;
        } else if (key < root.key) {
            Node left = root.left;
            left.get(key);
        } else {
            Node right = root.right;
            right.get(key);
        }
    }

    /** Returns the number of key-value mappings in this map. */
    int size() {
        int size;
        if (root == null) {
            size = 0;
        } else {
            size = 1;
            Node left = root.left;
            size += left.size();
            Node right = root.right;
            size += right.size();
        }
        return size;
    }

    /** Associates the specified value with the specified key in this map. */
    void put(K key, V value) {
        if (containsKey(key)) {
            return;
        }
        if (key == root.key) {
            root.value = value;
        } else if (key < root.key) {
            Node left = root.left;
            left.put(key, value);
        } else {
            Node right = root.right;
            right.put(key, value);
        }
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
