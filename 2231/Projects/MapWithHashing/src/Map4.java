import java.util.Iterator;
import java.util.NoSuchElementException;

import components.array.Array;
import components.array.Array1L;
import components.map.Map;
import components.map.Map2;
import components.map.MapSecondary;

/**
 * {@code Map} represented as a hash table using {@code Map}s for the buckets,
 * with implementations of primary methods.
 *
 * @param <K>
 *            type of {@code Map} domain (key) entries
 * @param <V>
 *            type of {@code Map} range (associated value) entries
 * @convention <pre>
 * |$this.hashTable.entries| > 0  and
 * for all i: integer, pf: PARTIAL_FUNCTION, x: K
 *     where (0 <= i  and  i < |$this.hashTable.entries|  and
 *            <pf> = $this.hashTable.entries[i, i+1)  and
 *            x is in DOMAIN(pf))
 *   ([computed result of x.hashCode()] mod |$this.hashTable.entries| = i))  and
 * |$this.hashTable.examinableIndices| = |$this.hashTable.entries|  and
 * $this.size = sum i: integer, pf: PARTIAL_FUNCTION
 *     where (0 <= i  and  i < |$this.hashTable.entries|  and
 *            <pf> = $this.hashTable.entries[i, i+1))
 *   (|pf|)
 * </pre>
 * @correspondence <pre>
 * this = union i: integer, pf: PARTIAL_FUNCTION
 *            where (0 <= i  and  i < |$this.hashTable.entries|  and
 *                   <pf> = $this.hashTable.entries[i, i+1))
 *          (pf)
 * </pre>
 *
 * @author Ryan Shaffer.555, Chris Tuttle.219
 *
 */
public class Map4<K, V> extends MapSecondary<K, V> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Default size of hash table.
     */
    private static final int DEFAULT_HASH_TABLE_SIZE = 101;

    /**
     * Buckets for hashing.
     */
    private Array<Map<K, V>> hashTable;

    /**
     * Total size of abstract {@code this}.
     */
    private int size;

    /**
     * Computes {@code a} mod {@code b} as % should have been defined to work.
     *
     * @param a
     *            the number being reduced
     * @param b
     *            the modulus
     * @return the result of a mod b, which satisfies 0 <= {@code mod} < b
     * @requires b > 0
     * @ensures <pre>
     * 0 <= mod  and  mod < b  and
     * there exists k: integer (a = k * b + mod)
     * </pre>
     */
    private static int mod(int a, int b) {
        assert b > 0 : "Violation of: b > 0";

        // Find value of a modulo b.
        int m = a % b;

        // In the case that a is negative and there is a remainder, add b to
        // get the correct value.
        if (a < 0 && m != 0) {
            m = m + b;
        }
        return m;
    }

    /**
     * Creator of initial representation.
     *
     * @param hashTableSize
     *            the size of the hash table
     * @requires hashTableSize > 0
     * @ensures <pre>
     * |$this.hashTable.entries| = hashTableSize  and
     * for all i: integer
     *     where (0 <= i  and  i < |$this.hashTable.entries|)
     *   ($this.hashTable.entries[i, i+1) = <{}>  and
     *    i is in $this.hashTable.examinableIndices)  and
     * $this.size = 0
     * </pre>
     */
    private void createNewRep(int hashTableSize) {
        assert hashTableSize > 0 : "Violation of: hashTableSize must be > 0";

        // Set size to 0 and create representation: an array of maps.
        this.size = 0;
        this.hashTable = new Array1L<Map<K, V>>(hashTableSize);

        // Fill hash-table (array) with maps.
        for (int i = 0; i < hashTableSize; i++) {
            this.hashTable.setEntry(i, new Map2<K, V>());
        }
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Map4() {
        // No-argument constructor sets hash-table to default size.
        this.createNewRep(DEFAULT_HASH_TABLE_SIZE);
    }

    /**
     * Constructor resulting in a hash table of size {@code hashTableSize}.
     *
     * @param hashTableSize
     *            size of hash table
     * @requires hashTableSize > 0
     * @ensures this = {}
     */
    public Map4(int hashTableSize) {
        assert hashTableSize > 0 : "Violation of: hashTableSize must be > 0";

        // Create hash table with desired size.
        this.createNewRep(hashTableSize);
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final Map<K, V> newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep(DEFAULT_HASH_TABLE_SIZE);
    }

    @Override
    public final void transferFrom(Map<K, V> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Map4<?, ?> : ""
                + "Violation of: source is of dynamic type Map4<?,?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Map4<?,?>, and
         * the ?,? must be K,V or the call would not have compiled.
         */
        Map4<K, V> localSource = (Map4<K, V>) source;
        this.hashTable = localSource.hashTable;
        this.size = localSource.size;
        localSource.createNewRep(DEFAULT_HASH_TABLE_SIZE);
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(K key, V value) {
        assert key != null : "Violation of: key is not null";
        assert value != null : "Violation of: value is not null";
        assert !this.hasKey(key) : "Violation of: key is not in DOMAIN(this)";

        // Hash key to find what bucket it's in, then add the pair.
        int position = mod(key.hashCode(), this.hashTable.length());
        this.hashTable.entry(position).add(key, value);

        // Update size to reflect change.
        this.size++;
    }

    @Override
    public final Pair<K, V> remove(K key) {
        assert key != null : "Violation of: key is not null";
        assert this.hasKey(key) : "Violation of: key is in DOMAIN(this)";

        // Hash key to find location.
        int position = mod(key.hashCode(), this.hashTable.length());

        // Update size to reflect the upcoming change.
        this.size--;

        // Return the map pair found at the location.
        return this.hashTable.entry(position).remove(key);
    }

    @Override
    public final Pair<K, V> removeAny() {
        assert this.size() > 0 : "Violation of: this /= empty_set";

        // Initialize pair that will hold the removed pair, boolean to find a
        // non-empty pair, and an iterator.
        Pair<K, V> removedPair = null;
        boolean nonEmpty = false;
        int i = 0;

        // Finds the first non-empty pair and removes it.
        while (!nonEmpty) {
            if (this.hashTable.entry(i).size() > 0) {
                removedPair = this.hashTable.entry(i).removeAny();
                nonEmpty = true;
            }
            i++;
        }

        // Update size to reflect change, and return the pair.
        this.size--;
        return removedPair;
    }

    @Override
    public final V value(K key) {
        assert key != null : "Violation of: key is not null";
        assert this.hasKey(key) : "Violation of: key is in DOMAIN(this)";

        // Hash key to find position.
        int position = mod(key.hashCode(), this.hashTable.length());

        // Return value of specified key using its position.
        return this.hashTable.entry(position).value(key);
    }

    @Override
    public final boolean hasKey(K key) {
        assert key != null : "Violation of: key is not null";

        // Hash key to find position.
        int position = mod(key.hashCode(), this.hashTable.length());

        // Determine whether the key is at the position, and return the result.
        boolean whetherHasKey = false;
        whetherHasKey = this.hashTable.entry(position).hasKey(key);
        return whetherHasKey;
    }

    @Override
    public final int size() {
        // Set initial size to 0.
        int s = 0;

        // Iterate through all maps/buckets in the array/hash-table,
        // and if they have size > 0, increase "total" size accordingly.
        for (Map<K, V> map : this.hashTable) {
            if (map.size() > 0) {
                s = s + map.size();
            }
        }
        return s;
    }

    @Override
    public final Iterator<Pair<K, V>> iterator() {
        return new Map4Iterator();
    }

    /**
     * Implementation of {@code Iterator} interface for {@code Map4}.
     */
    private final class Map4Iterator implements Iterator<Pair<K, V>> {

        /**
         * Number of elements seen already (i.e., |~this.seen|).
         */
        private int numberSeen;

        /**
         * Bucket from which current bucket iterator comes.
         */
        private int currentBucket;

        /**
         * Bucket iterator from which next element will come.
         */
        private Iterator<Pair<K, V>> bucketIterator;

        /**
         * No-argument constructor.
         */
        Map4Iterator() {
            this.numberSeen = 0;
            this.currentBucket = 0;
            this.bucketIterator = Map4.this.hashTable.entry(0).iterator();
        }

        @Override
        public boolean hasNext() {
            return this.numberSeen < Map4.this.size;
        }

        @Override
        public Pair<K, V> next() {
            assert this.hasNext() : "Violation of: ~this.unseen /= <>";
            if (!this.hasNext()) {
                /*
                 * Exception is supposed to be thrown in this case, but with
                 * assertion-checking enabled it cannot happen because of assert
                 * above.
                 */
                throw new NoSuchElementException();
            }
            this.numberSeen++;
            if (this.bucketIterator.hasNext()) {
                return this.bucketIterator.next();
            }
            while (!this.bucketIterator.hasNext()) {
                this.currentBucket++;
                this.bucketIterator = Map4.this.hashTable
                        .entry(this.currentBucket).iterator();
            }
            return this.bucketIterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "remove operation not supported");
        }

    }

}
