import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Iterator;

import components.queue.Queue;
import components.queue.Queue1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachineSecondary;

/**
 * {@code SortingMachine} represented as a {@code Queue} (using an embedding of
 * quicksort), with implementations of primary methods.
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
 * IS_SORTED (
 *   s: string of T,
 *   r: binary relation on T
 *  ) : boolean is
 *  for all x, y: T where (<x, y> is substring of s) (r(x, y))
 * </pre>
 * @convention <pre>
 * IS_TOTAL_PREORDER([relation computed by $this.machineOrder.compare method])  and
 * if not $this.insertionMode then
 *  IS_SORTED($this.entries, [relation computed by $this.machineOrder.compare method])
 * </pre>
 * @correspondence <pre>
 * this =
 *   ($this.insertionMode, $this.machineOrder, multiset_entries($this.entries))
 * </pre>
 */
public class SortingMachine4<T> extends SortingMachineSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Insertion mode.
     */
    private boolean insertionMode;

    /**
     * Order.
     */
    private Comparator<T> machineOrder;

    /**
     * Entries.
     */
    private Queue<T> entries;

    /**
     * Partitions {@code q} into two parts: entries no larger than
     * {@code partitioner} are put in {@code front}, and the rest are put in
     * {@code back}.
     *
     * @param <T>
     *            type of {@code Queue} entries
     * @param q
     *            the {@code Queue} to be partitioned
     * @param partitioner
     *            the partitioning value
     * @param front
     *            upon return, the entries no larger than {@code partitioner}
     * @param back
     *            upon return, the entries larger than {@code partitioner}
     * @param order
     *            ordering by which to separate entries
     * @clears q
     * @replaces front, back
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * perms(#q, front * back)  and
     * for all x: T where (<x> is substring of front)
     *  ([relation computed by order.compare method](x, partitioner))  and
     * for all x: T where (<x> is substring of back)
     *  (not [relation computed by order.compare method](x, partitioner))
     * </pre>
     */
    private static <T> void partition(Queue<T> q, T partitioner, Queue<T> front,
            Queue<T> back, Comparator<T> order) {
        assert q != null : "Violation of: q is not null";
        assert partitioner != null : "Violation of: partitioner is not null";
        assert front != null : "Violation of: front is not null";
        assert back != null : "Violation of: back is not null";
        assert order != null : "Violation of: order is not null";

        back.clear();
        front.clear();
        while (q.length() > 0) {
            if (order.compare(q.front(), partitioner) < 0) {
                front.enqueue(q.dequeue());
            } else {
                back.enqueue(q.dequeue());
            }
        }
        q.clear();

    }

    /**
     * Sorts {@code q} according to the ordering provided by the {@code compare}
     * method from {@code order}.
     *
     * @param <T>
     *            type of {@code Queue} entries
     * @param q
     *            the {@code Queue} to be sorted
     * @param order
     *            ordering by which to sort
     * @updates q
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures IS_SORTED(q, [relation computed by order.compare method])
     */
    private static <T> void sort(Queue<T> q, Comparator<T> order) {
        assert order != null : "Violation of: order is not null";
        if (q.length() > 1) {
            /*
             * Dequeue the partitioning entry from this
             */
            T partitioner = q.dequeue();

            /*
             * Partition this into two queues as discussed above (you will need
             * to declare and initialize two new queues)
             */
            Queue<T> front = new Queue1L<T>();
            Queue<T> back = new Queue1L<T>();
            partition(q, partitioner, front, back, order);

            /*
             * Recursively sort the two queues
             */
            sort(front, order);
            sort(back, order);

            /*
             * Reconstruct this by combining the two sorted queues and the
             * partitioning entry in the proper order
             */
            q.append(front);
            q.enqueue(partitioner);
            q.append(back);
        }
    }

    /**
     * Creator of initial representation.
     *
     * @param order
     *            total preorder for sorting
     */
    private void createNewRep(Comparator<T> order) {
        this.insertionMode = true;
        this.machineOrder = order;
        this.entries = new Queue1L<T>();
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
    public SortingMachine4(Comparator<T> order) {
        this.createNewRep(order);
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
    }

    @Override
    public final void transferFrom(SortingMachine<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof SortingMachine4<?> : ""
                + "Violation of: source is of dynamic type SortingMachine4<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type
         * SortingMachine4<?>, and the ? must be T or the call would not have
         * compiled.
         */
        SortingMachine4<T> localSource = (SortingMachine4<T>) source;
        this.insertionMode = localSource.insertionMode;
        this.machineOrder = localSource.machineOrder;
        this.entries = localSource.entries;
        localSource.createNewRep(localSource.machineOrder);
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(T x) {
        assert x != null : "Violation of: x is not null";
        assert this.isInInsertionMode() : "Violation of: this.insertion_mode";

        this.entries.enqueue(x);
        sort(this.entries, this.machineOrder);
    }

    @Override
    public final void changeToExtractionMode() {
        assert this.isInInsertionMode() : "Violation of: this.insertion_mode";

        this.insertionMode = false;
        sort(this.entries, this.machineOrder);
    }

    @Override
    public final T removeFirst() {
        assert !this
                .isInInsertionMode() : "Violation of: not this.insertion_mode";
        assert this.size() > 0 : "Violation of: this.contents /= {}";

        return this.entries.dequeue();
    }

    @Override
    public final boolean isInInsertionMode() {

        return this.insertionMode;
    }

    @Override
    public final Comparator<T> order() {

        return this.machineOrder;
    }

    @Override
    public final int size() {

        return this.entries.length();
    }

    @Override
    public final Iterator<T> iterator() {
        return this.entries.iterator();
    }

}
