/*
  A rudimentary implementation of a hashtable with chaining to resolve collisions
  and no provision for resizing to accomodate increased load factor

  Hashtables: https://en.wikipedia.org/wiki/Hash_table
*/
import java.util.*;

public class ImplementAHashtable {
  public static void main(String args[]) {
    Hashtable<String, Integer> studentToGrade = new Hashtable<>();

    String[] keys = new String[]{
      "John",
      "Gracie",
      "Cara",
      "Polly",
      "Adelina",
      "Owen",
      "Federico",
      "Craig",
      "Ishan",
      "Michael",
      "Zack",
      "Zach",
      "Luis",
      "Joshua",
      "Kyrie",
      "Adit",
      "Amit"
    };

    int[] values = new int[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };

    for (int i = 0; i < keys.length; i++) {
      System.out.println("Inserting " + keys[i] + "'s grade.");
      studentToGrade.put(keys[i], values[i]);
    }

    System.out.println();

    for (int i = 0; i < keys.length; i++) {
      System.out.println(keys[i] + "'s grade is: " + studentToGrade.get(keys[i]));
    }

    System.out.println();

    int updateValue = 1000;
    System.out.println("Updating " + keys[0] + " with a new grade of " + updateValue);
    studentToGrade.put(keys[0], updateValue);
    System.out.println(keys[0] + "'s new grade is: " + studentToGrade.get(keys[0]));
  }

  /*
    We will use generics so that the keys and values can be whatever the
    caller would like. We can implement the buckets with a plain array, but
    here we use an ArrayList (which uses an array underneath) for convenience
  */
  private static class Hashtable<K, V> {
    List<Entry<K, V>> buckets;
    int capacity = 20;
    int size = 0;

    public Hashtable() {
      buckets = new ArrayList<>();

      // Initialize all of the buckets to empty
      for (int i = 0; i < capacity; i++) {
        buckets.add(null);
      }
    }

    public int size() {
      return size;
    }

    public boolean isEmpty() {
      return size() == 0;
    }

    public void put(K key, V value) {
      int bucketIndex = getAccessIndex(key);
      Entry<K, V> head = buckets.get(bucketIndex);

      /*
        If we can find the entry, update it. If we cannot find it, perform
        the insertion into the bucket.
      */
      Entry<K, V> bucketEntryFound = getReferenceIfKeyExists(head, key);
      if (bucketEntryFound == null) {
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = head;
        buckets.set(bucketIndex, newEntry);
        size++;
      } else {
        bucketEntryFound.value = value;
      }

      // Here we would have provision for resizing according to load factor
    }

    public V get(K key) {
      int bucketIndex = getAccessIndex(key);
      Entry<K, V> head = buckets.get(bucketIndex);

      Entry<K, V> bucketEntryFound = getReferenceIfKeyExists(head, key);

      return bucketEntryFound == null ? null : bucketEntryFound.value;
    }

    /*
      Here we default to the hash function provided by .hashCode() from the
      key object's implementation.
      
      By default, the base Object class hashes the Object's memory address
      if the 'key' K type does not implement .hashCode() itself.
      
      This is why we cannot use primitive types for K nor V, they must be
      reference types that point to objects since Object has .hashCode()
      implemented by default and we need that to exist.

      A good hash function:
      1.) Is fully determined by the data being hashed
      2.) Uses all of the input data
      3.) Minimizes collisions by uniformly distributing the hash results across
      the entire set of possible hash values
      4.) Generates very different hash values for similar strings

      Reference: https://en.wikipedia.org/wiki/Hash_function
      Reference: https://www.sparknotes.com/cs/searching/hashtables/section2
    */
    private int getAccessIndex(K key) {
      int hashCode = key.hashCode();
      int accessIndex = Math.abs(hashCode) % capacity; // hashCode can be negative

      return accessIndex;
    }

    private Entry<K, V> getReferenceIfKeyExists(Entry<K, V> head, K key) {
      Entry<K, V> curr = head;
      while (curr != null) {
        if (curr.key.equals(key)) {
          return curr;
        }

        curr = curr.next;
      }

      return null;
    }

    /*
      These are the individual nodes stored in the buckets of the hashtable,
      each entry consists of a key and a value.
    */
    private class Entry<K, V> {
      K key;
      V value;

      // Each entry is a linked list node since we are chaining
      Entry<K, V> next;

      private Entry(K key, V value) {
        this.key = key;
        this.value = value;
      }
    }
  }
}
