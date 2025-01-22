package bstmap;

import java.util.Set;
import java.util.Iterator;

/** Map based on a binary search tree (BST).
 *  @author Yaohui Wu
 */
public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    private Node root; // Root of the BST.
    private int size; // Numbers of nodes in the tree.

    /** BST node with a key value pair. */
    private class Node {
        private K key; // Sorts by key.
        private V value; // Associated data.
        private Node left; // Root of left subtree.
        private Node right; // Root of right subtree.
        
        public Node(K newKey, V newValue) {
            key = newKey;
            value = newValue;
            left = null;
            right = null;
        }
    }

    /** Initializes an empty BST map. */
    public BSTMap() {
        root = null;
        size = 0;
    }
    
    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        int relation = key.compareTo(root.key);
        if (relation == 0) {
            return true;
        } else if (relation < 0) {
            root = root.left;
        } else {
            root = root.right;
        }
        return containsKey(key);
    }

    /** Returns the value to which the specified key is mapped, or null if
     *  this map contains no mapping for the key.
     */
    public V get(K key) {
        if (root == null) {
            return null;
        }
        int relation = key.compareTo(root.key);
        if (relation == 0) {
            return root.value;
        } else if (relation < 0) {
            root = root.left;
        } else {
            root = root.right;
        }
        return get(key);
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /** Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            size += 1;
            return;
        }
        int relation = key.compareTo(root.key);
        if (relation < 0) {
            root = root.left;
        } else {
            root = root.right; 
        }
        put(key, value);
    }

    /** Returns a Set view of the keys contained in this map. Not required for
     *  Lab 7.
     *  If you don't implement this, throw an UnsupportedOperationException.
     */
    public Set<K> keySet() {
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

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    /** Prints the BSTMap in order of increasing key. */
    public void printInOrder() {
        throw new UnsupportedOperationException();
    }
}
