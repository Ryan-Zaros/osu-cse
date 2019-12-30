import components.array.Array;
import components.array.Array1L;
import components.map.Map;
import components.map.Map1L;
import components.random.Random;
import components.random.Random1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Program to test {@code siftDown} on {@code Array<Integer>}.
 *
 * @mathdefinitions <pre>
 * SUBTREE_IS_HEAP (
 *   a: ARRAY_MODEL,
 *   start: integer,
 *   stop: integer,
 *   r: binary relation on T
 *  ) : boolean is
 *  [the subtree of a (when a is interpreted as a complete binary tree) rooted
 *   at index start and only through entry stop of a satisfies the heap
 *   ordering property according to the relation r]
 *
 * SUBTREE_ARRAY_ENTRIES (
 *   a: ARRAY_MODEL,
 *   start: integer,
 *   stop: integer
 *  ) : finite multiset of T is
 *  [the multiset of entries in a that belong to the subtree of a
 *   (when a is interpreted as a complete binary tree) rooted at
 *   index start and only through entry stop]
 * </pre>
 *
 * @author Ryan Shaffer
 *
 */
public final class ArraySiftDownMain {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private ArraySiftDownMain() {
    }

    /**
     * Number of junk entries at the end of the array.
     */
    private static final int JUNK_SIZE = 5;

    /**
     * Checks if the subtree of the given {@code Array} rooted at the given
     * {@code top} is a heap.
     *
     * @param array
     *            the complete binary tree
     * @param top
     *            the index of the root of the "subtree"
     * @param last
     *            the index of the last entry in the heap
     * @return true if the subtree of the given {@code Array} rooted at the
     *         given {@code top} is a heap; false otherwise
     * @requires <pre>
     * 0 <= top  and  last < |array.entries|  and
     * |array.examinableIndices| = |array.entries|  and
     * [subtree rooted at {@code top} is a complete binary tree]
     * </pre>
     * @ensures isHeap = SUBTREE_IS_HEAP(heap, top, last, <=)
     */
    private static boolean isHeap(Array<Integer> array, int top, int last) {
        assert array != null : "Violation of: array is not null";
        assert 0 <= top : "Violation of: 0 <= top";
        assert last < array.length() : "Violation of: last < |array.entries|";
        for (int i = 0; i < array.length(); i++) {
            assert array.mayBeExamined(i) : ""
                    + "Violation of: |array.examinableIndices| = |array.entries|";
        }
        /*
         * No need to check the other requires clause, because it must be true
         * when using the Array representation for a complete binary tree.
         */
        int left = 2 * top + 1;
        boolean isHeap = true;
        if (left <= last) { // there is non-empty left subtree
            isHeap = (array.entry(top) <= array.entry(left))
                    && isHeap(array, left, last);
            int right = left + 1;
            if (isHeap && (right <= last)) { // there is non-empty right subtree
                isHeap = (array.entry(top) <= array.entry(right))
                        && isHeap(array, right, last);
            }
        }
        return isHeap;
    }

    /**
     * Finds {@code item} in DOMAIN({@code m}) and, if such exists, adds 1 to
     * the value in {@code m} associated with key {@code item}; otherwise places
     * new key {@code item} in {@code m} with associated value 1.
     *
     * @param <K>
     *            the type of the map's key
     * @param item
     *            the item whose count is to be incremented
     * @param m
     *            the {@code Map} to be updated
     * @aliases reference item
     * @updates m
     * @ensures <pre>
     * if item is in DOMAIN(m) then
     *   there exists count: integer ((item, count) is in #m
     *     and m = (#m \ (item, count)) union {(item, count + 1)})
     * else
     *   m = #m union {(item, 1)}
     * </pre>
     */
    private static <K> void incrementCountFor(K item, Map<K, Integer> m) {
        assert item != null : "Violation of: item is not null";
        assert m != null : "Violation of: m is not null";

        if (m.hasKey(item)) {
            Map.Pair<K, Integer> pair = m.remove(item);
            m.add(pair.key(), pair.value() + 1);
        } else {
            m.add(item, 1);
        }

    }

    /**
     * Given an {@code Array} that represents a complete binary tree and an
     * index referring to the root of a subtree that would be a heap except for
     * its root, sifts the root down to turn that whole subtree into a heap.
     *
     * @param array
     *            the complete binary tree
     * @param top
     *            the index of the root of the "subtree"
     * @param last
     *            the index of the last entry in the heap
     * @updates array.entries
     * @requires <pre>
     * 0 <= top  and  last < |array.entries|  and
     * |array.examinableIndices| = |array.entries|  and
     * [subtree rooted at {@code top} is a complete binary tree]  and
     * SUBTREE_IS_HEAP(array, 2 * top + 1, last, <=)  and
     * SUBTREE_IS_HEAP(array, 2 * top + 2, last, <=)
     * </pre>
     * @ensures <pre>
     * SUBTREE_IS_HEAP(array, top, last, <=)  and
     * perms(array.entries, #array.entries)  and
     * SUBTREE_ARRAY_ENTRIES(array, top, last) =
     *  SUBTREE_ARRAY_ENTRIES(#array, top, last)  and
     * [the other entries in array.entries are the same as in #array.entries]
     * </pre>
     */
    private static void siftDown(Array<Integer> array, int top, int last) {
        assert array != null : "Violation of: array is not null";
        assert 0 <= top : "Violation of: 0 <= top";
        assert last < array.length() : "Violation of: last < |array.entries|";
        for (int i = 0; i < array.length(); i++) {
            assert array.mayBeExamined(i) : ""
                    + "Violation of: |array.examinableIndices| = |array.entries|";
        }
        assert isHeap(array, 2 * top + 1, last) : ""
                + "Violation of: SUBTREE_IS_HEAP(array, 2 * top + 1, last, <=)";
        assert isHeap(array, 2 * top + 2, last) : ""
                + "Violation of: SUBTREE_IS_HEAP(array, 2 * top + 2, last, <=)";
        /*
         * No need to check the other requires clause, because it must be true
         * when using the Array representation for a complete binary tree.
         */

        if (array.length() > 1) {
            int left = 2 * top + 1;
            int right = left + 1;
            if (!isHeap(array, top, last)) {
                if (right <= last && array.entry(left)
                        .compareTo(array.entry(right)) >= 0) {
                    array.exchangeEntries(top, right);
                    siftDown(array, right, last);
                } else {
                    array.exchangeEntries(top, left);
                    siftDown(array, left, last);
                }
            }
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Input array size from user
         */
        out.print("Enter (non-negative) heap size: ");
        int heapSize = in.nextInteger();
        /*
         * Construct array as follows. Make its length be heapSize + JUNK_SIZE.
         * The prefix of length heapSize of array represents a complete binary
         * tree and contains pseudo-random integers in the range [JUNK_SIZE,
         * heapSize + JUNK_SIZE). The suffix of length JUNK_SIZE is junk. These
         * junk values are specifically set so as to count down from one less
         * than JUNK_SIZE to 0.
         *
         * Also, build a Map<Integer, Integer> named original to represent the
         * multiset of values present in the initial complete binary tree.
         */
        Map<Integer, Integer> original = new Map1L<>();
        Random rnd = new Random1L();
        Array<Integer> array = new Array1L<Integer>(heapSize + JUNK_SIZE);
        for (int i = 0; i < heapSize; i++) {
            int entry = JUNK_SIZE + ((int) (rnd.nextDouble() * heapSize));
            array.setEntry(i, entry);
            incrementCountFor(entry, original);
        }
        for (int i = heapSize; i < heapSize + JUNK_SIZE; i++) {
            array.setEntry(i, heapSize + JUNK_SIZE - i - 1);
        }
        /*
         * Output initial array
         */
        out.println("              initial array: " + array);
        /*
         * Heapify the heapSize-length prefix of array by repeatedly calling
         * siftDown (this is an iterative implementation of heapify--it should
         * start with i = heapSize / 2 - 1, but since it is intended to test
         * siftDown, we call it on the leaves as well)
         */
        for (int i = heapSize - 1; i >= 0; i--) {
            siftDown(array, i, heapSize - 1);
        }
        /*
         * Make sure the heapSize-length prefix of array is now a heap
         */
        assert isHeap(array, 0, heapSize - 1) : ""
                + "Violation of: SUBTREE_IS_HEAP(array, 0, heapSize - 1, <=)";
        /*
         * Make sure the current multiset of values in the heapSize-length
         * prefix of array is the same as the original
         */
        Map<Integer, Integer> current = original.newInstance();
        for (int i = 0; i < heapSize; i++) {
            incrementCountFor(array.entry(i), current);
        }
        assert current.equals(original) : ""
                + "Method siftDown caused different values to be in the heap "
                + "than were in the original complete binary tree, "
                + "perhaps by failing to ignore the junk at "
                + "the far end of the array.";
        /*
         * Make sure the junk at the far end of array was not changed by
         * siftDown
         */
        for (int i = heapSize; i < heapSize + JUNK_SIZE; i++) {
            assert heapSize + JUNK_SIZE - i - 1 == array.entry(i) : ""
                    + "Method siftDown changed the junk at "
                    + "the far end of the array: Expected "
                    + (heapSize + JUNK_SIZE - i - 1) + " but was "
                    + array.entry(i);
        }
        /*
         * If everything worked, output the array with a heapified prefix
         */
        out.println("array with heapified prefix: " + array);
        /*
         * Close streams
         */
        in.close();
        out.close();
    }

}
