package bstmap;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** Map based on a binary search tree (BST).
 *  @author Yaohui Wu
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root; // Root of the BST.
    private int size; // Numbers of nodes in the BST.

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

    /** Inserts a node with the given key value pair into the BSTMap using
     *  recursion and returns the root of the BST after the insertion.
     */
    private Node insert(Node root, K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            return root;
        }
        int comparison = key.compareTo(root.key);
        if (comparison < 0) {
            // If the key is smaller, inserts it into the left subtree.
            root.left = insert(root.left, key, value);
        } else if (comparison > 0) {
            // If the key is larger, inserts it into the right subtree.
            root.right = insert(root.right, key, value);
        } else {
            /* If the key already exists, updates the value associated with
             * it.
             */
            root.value = value;
        }
        // Returns the root of the BST after the insertion.
        return root;
    }

    /** Deletes a node with the given key using recursion if it exists and
     *  returns the root of the BST after the deletion.
     */
    private Node delete(Node root, K key) {
        if (root == null) {
            return null;
        }
        int comparison = key.compareTo(root.key);
        if (comparison < 0) {
            root.left = delete(root.left, key);
        } else if (comparison > 0) {
            root.right = delete(root.right, key);
        } else {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            Node temp = root;
            root = min(root.right);
            root.right = deleteMin(temp.right);
            root.left = temp.left;
        }
        return root;
    }

    /** Returns the smallest node in the BST. */
    private Node min(Node root) {
        if (root == null) {
            return null;
        }
        if (root.left == null) {
            return root;
        }
        return min(root.left);
    }

    /** Deletes the smallest node in the BST and returns the root of the BST
     *  after the deletion.
     */
    private Node deleteMin(Node root) {
        if (root.left == null) {
            return root.right;
        }
        root.left = deleteMin(root.left);
        return root;
    }

    /** Searches the node that has the given key using recursion. Returns the
     *  node if it exists, otherwise returns null.
     */
    private Node search(Node root, K key) {
        if (root == null) {
            return null;
        }
        int comparison = key.compareTo(root.key);
        if (comparison < 0) {
            // If the key is smaller, searches the left subtree.
            return search(root.left, key);
        } else if (comparison > 0) {
            // If the key is larger, searches the right subtree.
            return search(root.right, key);
        } else {
            // If the key is equal, returns the current node.
            return root;
        }
    }

    @Override
    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return search(root, key) != null;
    }

    @Override
    /** Returns the value to which the specified key is mapped, or null if
     *  this map contains no mapping for the key.
     */
    public V get(K key) {
        if (root == null) {
            return null;
        }
        Node node = search(root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    @Override
    /** Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = insert(root, key, value);
        size += 1;
    }
    
    /** Prints the BSTMap in comparison of increasing key. */
    public void printIncomparison() {
        throw new UnsupportedOperationException();
    }

    @Override
    /** Returns a Set view of the keys contained in this map. Not required for
     *  Lab 7.
     *  If you don't implement this, throw an UnsupportedOperationException.
     */
    public Set<K> keySet() {
        HashSet<K> keySet = new HashSet<>();
        addKeys(root, keySet);
        return keySet;
    }

    public void addKeys(Node root, Set<K> keySet) {
        if (root == null) {
            return;
        }
        K key = root.key;
        if (!keySet.contains(key)) {
            keySet.add(key);
        }
        addKeys(root.left, keySet);
        addKeys(root.right, keySet);
    }

    @Override
    /** Removes the mapping for the specified key from this map if present.
     *  Not required for Lab 7. If you don't implement this, throw an
     *  UnsupportedOperationException.
     */
    public V remove(K key) {
        if (containsKey(key)) {
            V value = get(key);
            root = delete(root, key);
            size -= 1;
            return value;
        }
        return null;
    }

    @Override
    /** Removes the entry for the specified key only if it is currently mapped to
     *  the specified value. Not required for Lab 7. If you don't implement this,
     *  throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        if (containsKey(key)) {
            V keyValue = get(key);
            if (value.equals(keyValue)) {
                return remove(key);
            }
        }
        return null;
    }

    /** Returns an iterator over the keys of the BSTMap. */
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
