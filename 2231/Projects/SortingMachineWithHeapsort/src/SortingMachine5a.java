import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import components.array.Array;
import components.array.Array1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachineSecondary;

/**
 * {@code SortingMachine} represented as a {@code Queue} and an {@code Array}
 * (using an embedding of heap sort), with implementations of primary methods.
 *
 * @param <T>
 *            type of {@code SortingMachine} entries
 * @mathdefinitions <pre>
 * IS_TOTAL_PREORDER (
 *   r: binary relation on T
 *  ) : boolean is
 *  for all x, y, z: T
 *   ((r(x, y) or r(y, x))  and
 *    (if (r(x, y) and r(y, z)) then r(x, z)))
 *
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
 * @convention <pre>
 * IS_TOTAL_PREORDER([relation computed by $this.machineOrder.compare method]  and
 * if $this.insertionMode then
 *   $this.heapSize = 0
 * else
 *   $this.entries = <>  and
 *   |$this.heap.examinableIndices| = |$this.heap.entries|  and
 *   SUBTREE_IS_HEAP($this.heap, 0, $this.heapSize - 1,
 *     [relation computed by $this.machineOrder.compare method])  and
 *   0 <= $this.heapSize <= |$this.heap.entries|
 * </pre>
 * @correspondence <pre>
 * if $this.insertionMode then
 *   this = (true, $this.machineOrder, multiset_entries($this.entries))
 * else
 *   this = (false, $this.machineOrder,
 *     multiset_entries($this.heap.entries[0, $this.heapSize)))
 * </pre>
 *
 * @author Ryan Shaffer.555
 * @author Chris Tuttle.219
 *
 */
public class SortingMachine5a<T> extends SortingMachineSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Order.
     */
    private Comparator<T> machineOrder;

    /**
     * Insertion mode.
     */
    private boolean insertionMode;

    /**
     * Entries.
     */
    private Queue<T> entries;

    /**
     * Heap.
     */
    private Array<T> heap;

    /**
     * Heap size.
     */
    private int heapSize;

    /**
     * Given an {@code Array} that represents a complete binary tree and an
     * index referring to the root of a subtree that would be a heap except for
     * its root, sifts the root down to turn that whole subtree into a heap.
     *
     * @param <T>
     *            type of array entries
     * @param array
     *            the complete binary tree
     * @param top
     *            the index of the root of the "subtree"
     * @param last
     *            the index of the last entry in the heap
     * @param order
     *            total preorder for sorting
     * @updates array.entries
     * @requires <pre>
     * 0 <= top  and  last < |array.entries|  and
     * |array.examinableIndices| = |array.entries|  and
     * [subtree rooted at {@code top} is a complete binary tree]  and
     * SUBTREE_IS_HEAP(array, 2 * top + 1, last,
     *     [relation computed by order.compare method])  and
     * SUBTREE_IS_HEAP(array, 2 * top + 2, last,
     *     [relation computed by order.compare method])  and
     * IS_TOTAL_PREORDER([relation computed by order.compare method])
     * </pre>
     * @ensures <pre>
     * SUBTREE_IS_HEAP(array, top, last,
     *     [relation computed by order.compare method])  and
     * perms(array.entries, #array.entries)  and
     * SUBTREE_ARRAY_ENTRIES(array, top, last) =
     *  SUBTREE_ARRAY_ENTRIES(#array, top, last)  and
     * [the other entries in array.entries are the same as in #array.entries]
     * </pre>
     */
    private static <T> void siftDown(Array<T> array, int top, int last,
            Comparator<T> order) {
        assert array != null : "Violation of: array is not null";
        assert order != null : "Violation of: order is not null";
        assert 0 <= top : "Violation of: 0 <= top";
        assert last < array.length() : "Violation of: last < |array.entries|";
        for (int i = 0; i < array.length(); i++) {
            assert array.mayBeExamined(i) : ""
                    + "Violation of: |array.examinableIndices| = |array.entries|";
        }
        assert isHeap(array, 2 * top + 1, last, order) : ""
                + "Violation of: SUBTREE_IS_HEAP(array, 2 * top + 1, last,"
                + " [relation computed by order.compare method])";
        assert isHeap(array, 2 * top + 2, last, order) : ""
                + "Violation of: SUBTREE_IS_HEAP(array, 2 * top + 2, last,"
                + " [relation computed by order.compare method])";
        /*
         * Impractical to check last requires clause; no need to check the other
         * requires clause, because it must be true when using the Array
         * representation for a complete binary tree.
         */

        /*
         * If there's a root with children, set indices for the left and right.
         * Until the array is a heap, recursively find the smallest child and
         * switch it with the top.
         */
        if (array.length() > 1) {
            int left = 2 * top + 1;
            int right = left + 1;
            if (!isHeap(array, top, last, order)) {
                if (right <= last && order.compare(array.entry(left),
                        array.entry(right)) >= 0) {
                    array.exchangeEntries(top, right);
                    siftDown(array, right, last, order);
                } else {
                    array.exchangeEntries(top, left);
                    siftDown(array, left, last, order);
                }
            }
        }
    }

    /**
     * Heapifies the subtree of the given {@code Array} rooted at the given
     * {@code top}.
     *
     * @param <T>
     *            type of {@code Array} entries
     * @param array
     *            the {@code Array} to be turned into a heap
     * @param top
     *            the index of the root of the "subtree" to heapify
     * @param order
     *            the total preorder for sorting
     * @updates array.entries
     * @requires <pre>
     * 0 <= top  and
     * |array.examinableIndices| = |array.entries|  and
     * [subtree rooted at {@code top} is a complete binary tree]  and
     * IS_TOTAL_PREORDER([relation computed by order.compare method])
     * </pre>
     * @ensures <pre>
     * SUBTREE_IS_HEAP(array, top, |array.entries| - 1,
     *     [relation computed by order.compare method])  and
     * perms(array.entries, #array.entries)
     * </pre>
     */
    private static <T> void heapify(Array<T> array, int top,
            Comparator<T> order) {
        assert array != null : "Violation of: array is not null";
        assert order != null : "Violation of: order is not null";
        assert 0 <= top : "Violation of: 0 <= top";
        for (int i = 0; i < array.length(); i++) {
            assert array.mayBeExamined(i) : ""
                    + "Violation of: |array.examinableIndices| = |array.entries|";
        }
        /*
         * Impractical to check last requires clause; no need to check the other
         * requires clause, because it must be true when using the Array
         * representation for a complete binary tree.
         */

        /*
         * Set left and right indices; will work for every recursive call.
         */
        int left = 2 * top + 1;
        int right = left + 1;
        int last = array.length() - 1;
        /*
         * Recursively heapify the left and right trees, then sift down.
         */
        if (!isHeap(array, top, last, order)) {
            heapify(array, left, order);
            heapify(array, right, order);
            siftDown(array, top, last, order);
        }
    }

    /**
     * Constructs and returns an {@code Array} representing a heap with the
     * entries from the given {@code Queue}.
     *
     * @param <T>
     *            type of {@code Queue} and {@code Array} entries
     * @param q
     *            the {@code Queue} with the entries for the heap
     * @param order
     *            the total preorder for sorting
     * @return the {@code Array} representation of a heap
     * @clears q
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * SUBTREE_IS_HEAP(buildHeap, 0, |buildHeap.entries| - 1)  and
     * perms(buildHeap.entries, #q)  and
     * |buildHeap.examinableIndices| = |buildHeap.entries|
     * </pre>
     */
    private static <T> Array<T> buildHeap(Queue<T> q, Comparator<T> order) {
        assert q != null : "Violation of: q is not null";
        assert order != null : "Violation of: order is not null";
        /*
         * Impractical to check the requires clause.
         */

        /*
         * Fill an array (heap) with the queue and then heapify it.
         */
        Array<T> array = new Array1L<>(q.length());
        int i = 0;
        while (q.length() > 0) {
            array.setEntry(i, q.dequeue());
            i++;
        }
        heapify(array, 0, order);
        return array;
    }

    /**
     * Checks if the subtree of the given {@code Array} rooted at the given
     * {@code top} is a heap.
     *
     * @param <T>
     *            type of {@code Array} entries
     * @param array
     *            the complete binary tree
     * @param top
     *            the index of the root of the "subtree"
     * @param last
     *            the index of the last entry in the heap
     * @param order
     *            total preorder for sorting
     * @return true if the subtree of the given {@code Array} rooted at the
     *         given {@code top} is a heap; false otherwise
     * @requires <pre>
     * 0 <= top  and  last < |array.entries|  and
     * |array.examinableIndices| = |array.entries|  and
     * [subtree rooted at {@code top} is a complete binary tree]
     * </pre>
     * @ensures <pre>
     * isHeap = SUBTREE_IS_HEAP(heap, top, last,
     *     [relation computed by order.compare method])
     * </pre>
     */
    private static <T> boolean isHeap(Array<T> array, int top, int last,
            Comparator<T> order) {
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
        if (left <= last) {
            isHeap = (order.compare(array.entry(top), array.entry(left)) <= 0)
                    && isHeap(array, left, last, order);
            int right = left + 1;
            if (isHeap && (right <= last)) {
                isHeap = (order.compare(array.entry(top),
                        array.entry(right)) <= 0)
                        && isHeap(array, right, last, order);
            }
        }
        return isHeap;
    }

    /**
     * Checks that the part of the convention repeated below holds for the
     * current representation.
     *
     * @return true if the convention holds (or if assertion checking is off);
     *         otherwise reports a violated assertion
     * @convention <pre>
     * if $this.insertionMode then
     *   $this.heapSize = 0
     * else
     *   $this.entries = <>  and
     *   |$this.heap.examinableIndices| = |$this.heap.entries|  and
     *   SUBTREE_IS_HEAP($this.heap, 0, $this.heapSize - 1,
     *     [relation computed by $this.machineOrder.compare method])  and
     *   0 <= $this.heapSize <= |$this.heap.entries|
     * </pre>
     */
    private boolean conventionHolds() {
        if (this.insertionMode) {
            assert this.heapSize == 0 : ""
                    + "Violation of: if $this.insertionMode then $this.heapSize = 0";
        } else {
            assert this.entries.length() == 0 : ""
                    + "Violation of: if not $this.insertionMode then $this.entries = <>";
            assert 0 <= this.heapSize : ""
                    + "Violation of: if not $this.insertionMode then 0 <= $this.heapSize";
            assert this.heapSize <= this.heap.length() : ""
                    + "Violation of: if not $this.insertionMode then"
                    + " $this.heapSize <= |$this.heap.entries|";
            for (int i = 0; i < this.heap.length(); i++) {
                assert this.heap.mayBeExamined(i) : ""
                        + "Violation of: if not $this.insertionMode then"
                        + " |$this.heap.examinableIndices| = |$this.heap.entries|";
            }
            assert isHeap(this.heap, 0, this.heapSize - 1,
                    this.machineOrder) : ""
                            + "Violation of: if not $this.insertionMode then"
                            + " SUBTREE_IS_HEAP($this.heap, 0, $this.heapSize - 1,"
                            + " [relation computed by $this.machineOrder.compare"
                            + " method])";
        }
        return true;
    }

    /**
     * Creator of initial representation.
     *
     * @param order
     *            total preorder for sorting
     */
    private void createNewRep(Comparator<T> order) {
        this.machineOrder = order;
        this.insertionMode = true;
        this.entries = new Queue1L<T>();
        this.heap = new Array1L<T>(0);
        this.heapSize = 0;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * Constructor from order.
     *
     * @param order
     *            total preorder for sorting
     */
    public SortingMachine5a(Comparator<T> order) {
        this.createNewRep(order);
        assert this.conventionHolds();
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final SortingMachine<T> newInstance() {
        try {
            Constructor<?> c = this.getClass().getConstructor(Comparator.class);
            return (SortingMachine<T>) c.newInstance(this.machineOrder);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep(this.machineOrder);
        assert this.conventionHolds();
    }

    @Override
    public final void transferFrom(SortingMachine<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof SortingMachine5a<?> : ""
                + "Violation of: source is of dynamic type SortingMachine5a<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type
         * SortingMachine5a<?>, and the ? must be T or the call would not have
         * compiled.
         */
        SortingMachine5a<T> localSource = (SortingMachine5a<T>) source;
        this.insertionMode = localSource.insertionMode;
        this.machineOrder = localSource.machineOrder;
        this.entries = localSource.entries;
        this.heap = localSource.heap;
        this.heapSize = localSource.heapSize;
        localSource.createNewRep(localSource.machineOrder);
        assert this.conventionHolds();
        assert localSource.conventionHolds();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(T x) {
        assert x != null : "Violation of: x is not null";
        assert this.isInInsertionMode() : "Violation of: this.insertion_mode";
        /*
         * Adding to "this" while it's still in insertion mode, so enqueue.
         */
        this.entries.enqueue(x);
        assert this.conventionHolds();
    }

    @Override
    public final void changeToExtractionMode() {
        assert this.isInInsertionMode() : "Violation of: this.insertion_mode";
        /*
         * Build a heap (array) using the given queue and order. Set the size
         * and update to extraction mode.
         */
        this.heap = buildHeap(this.entries, this.machineOrder);
        this.heapSize = this.heap.length();
        this.insertionMode = false;
        assert this.conventionHolds();
    }

    @Override
    public final T removeFirst() {
        assert !this
                .isInInsertionMode() : "Violation of: not this.insertion_mode";
        assert this.size() > 0 : "Violation of: this.contents /= {}";
        /*
         * The root is the first, or top, that is being removed. Unless the tree
         * is empty or just a root, move the bottom up to be the new root. Then,
         * sift down the tree to make it a heap again. Since the size isn't
         * updated until after, we use "this.heapSize - 2" as the bottom.
         */
        T root = this.heap.entry(0);
        if (this.heapSize > 1) {
            this.heap.exchangeEntries(0, this.heapSize - 1);
            siftDown(this.heap, 0, this.heapSize - 2, this.machineOrder);
        }
        this.heapSize--;
        assert this.conventionHolds();
        return root;
    }

    @Override
    public final boolean isInInsertionMode() {
        assert this.conventionHolds();
        return this.insertionMode;
    }

    @Override
    public final Comparator<T> order() {
        assert this.conventionHolds();
        return this.machineOrder;
    }

    @Override
    public final int size() {
        /*
         * If "this" is in insertion mode, then return the length of the queue.
         * If "this" is in extraction mode, then return the size of the heap.
         */
        int result = 0;
        if (this.insertionMode) {
            result = this.entries.length();
        } else {
            result = this.heapSize;
        }
        assert this.conventionHolds();
        return result;
    }

    @Override
    public final Iterator<T> iterator() {
        return new SortingMachine5aIterator();
    }

    /**
     * Implementation of {@code Iterator} interface for
     * {@code SortingMachine5a}.
     */
    private final class SortingMachine5aIterator implements Iterator<T> {

        /**
         * Representation iterator.
         */
        private final Iterator<T> iterator;

        /**
         * Iterator count.
         */
        private int notSeenCount;

        /**
         * No-argument constructor.
         */
        private SortingMachine5aIterator() {
            if (SortingMachine5a.this.insertionMode) {
                this.iterator = SortingMachine5a.this.entries.iterator();
            } else {
                this.iterator = SortingMachine5a.this.heap.iterator();
                this.notSeenCount = SortingMachine5a.this.heapSize;
            }
            assert SortingMachine5a.this.conventionHolds();
        }

        @Override
        public boolean hasNext() {
            if (!SortingMachine5a.this.insertionMode
                    && (this.notSeenCount == 0)) {
                assert SortingMachine5a.this.conventionHolds();
                return false;
            }
            assert SortingMachine5a.this.conventionHolds();
            return this.iterator.hasNext();
        }

        @Override
        public T next() {
            assert this.hasNext() : "Violation of: ~this.unseen /= <>";
            if (!this.hasNext()) {
                /*
                 * Exception is supposed to be thrown in this case, but with
                 * assertion-checking enabled it cannot happen because of assert
                 * above.
                 */
                throw new NoSuchElementException();
            }
            if (!SortingMachine5a.this.insertionMode) {
                this.notSeenCount--;
            }
            assert SortingMachine5a.this.conventionHolds();
            return this.iterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "remove operation not supported");
        }
    }
}
