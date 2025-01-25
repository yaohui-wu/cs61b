package hashmap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Yaohui Wu
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private static final int DEFAULT_INITIAL_SIZE = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int RESIZE_FACTOR = 2;
    private int size; // Number of elements.
    private int capacity; // Number of buckets.
    /*
     * Number of elements / number of buckets cannot exceed this maximum load
     * factor.
     */
    private double loadFactor;
    private Set<K> keys;

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INITIAL_SIZE, DEFAULT_INITIAL_SIZE);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        size = 0;
        capacity = initialSize;
        loadFactor = maxLoad;
        keys = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] hashTable = new Collection[tableSize];
        for (int i = 0; i < tableSize; i += 1) {
            hashTable[i] = createBucket();
        }
        return hashTable;
    }

    /** Returns the hash value for the given key. */
    private int hash(K key) {
        return Math.floorMod(key.hashCode(), capacity);
    }

    private Collection<Node> getBucket(K key) {
        return buckets[hash(key)];
    }

    private Node getNode(Collection<Node> bucket, K key) {
        for (Node node : bucket) {
            if (key.equals(node.key)) {
                return node;
            }
        }
        return null;
    }

    /** Resizes the hash table by a geometric factor. */
    private void resize() {
        MyHashMap<K, V> hashTable = new MyHashMap<>(
            capacity * RESIZE_FACTOR,
            loadFactor
        );
        for (K key : keys) {
            hashTable.put(key, get(key));
        }
        buckets = hashTable.buckets;
        keys = hashTable.keys;
        capacity *= RESIZE_FACTOR;
    }

    @Override
    /** Removes all of the mappings from this map. */
    public void clear() {
        buckets = createTable(DEFAULT_INITIAL_SIZE);
        size = 0;
        capacity = DEFAULT_INITIAL_SIZE;
        loadFactor = DEFAULT_LOAD_FACTOR;
        keys.clear();
    }

    @Override
    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    @Override
    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        if (containsKey(key)) {
            Collection<Node> bucket = getBucket(key);
            Node node = getNode(bucket, key);
            return node.value;
        }
        return null;
    }

    @Override
    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    @Override
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value) {
        Collection<Node> bucket = getBucket(key);
        if (containsKey(key)) {
            Node node = getNode(bucket, key);
            node.value = value;
            return;
        }
        bucket.add(createNode(key, value));
        keys.add(key);
        size += 1;
        if (size / capacity > loadFactor) {
            resize();
        }
    }

    @Override
    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return keys;
    }

    @Override
    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        if (containsKey(key)) {
            return remove(key, get(key));
        }
        return null;
    }

    @Override
    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        if (containsKey(key)) {
            Collection<Node> bucket = getBucket(key);
            for (Node node : bucket) {
                V val = node.value;
                if (key.equals(node.key) && val.equals(value)) {
                    bucket.remove(node);
                    keys.remove(key);
                    size -= 1;
                    return val;
                }
            }
        }
        return null;
    }

    /** Returns an iterator over the keys of the hash table. */
    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }
}
