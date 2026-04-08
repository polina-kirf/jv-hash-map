package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] buckets;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int hash = getHash(key);
        int index = getIndex(hash);

        Node<K, V> current = buckets[index];

        if (current == null) {
            buckets[index] = new Node<>(key, value, hash);
            increaseSizeAndCheckResize();
            return;
        }

        Node<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }
        prev.next = new Node<>(key, value, hash);
        increaseSizeAndCheckResize();
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);

        Node<K, V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(int hash) {
        return Math.abs(hash % buckets.length);
    }

    private void increaseSizeAndCheckResize() {
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = buckets.length * 2;
        Node<K, V>[] newBuckets = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> node : buckets) {
            while (node != null) {
                Node<K, V> next = node.next;

                int newIndex = Math.abs(node.hash % newCapacity);

                node.next = newBuckets[newIndex];
                newBuckets[newIndex] = node;

                node = next;
            }
        }
        buckets = newBuckets;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        private final int hash;

        Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
