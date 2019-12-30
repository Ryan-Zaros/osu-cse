
package components.waitingline;

import java.util.Iterator;

import components.sequence.Sequence;
import components.sequence.Sequence1L;

/**
 * {@code WaitingLine} represented as a {@code Sequence} of entries, with
 * implementations of primary methods.
 *
 * @author Ryan Shaffer
 *
 * @param <T>
 *            type of {@code WaitingLine} entries
 * @correspondence this = $this.entries
 */
public class WaitingLine1<T> extends WaitingLineSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Entries included in {@code this}.
     */
    private Sequence<T> entries;

    /**
     * length of WaitingLine<T>.
     */
    private int length;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.entries = new Sequence1L<T>();
        this.length = 0;
    }

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public WaitingLine1() {
        this.createNewRep();
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final WaitingLine<T> newInstance() {
        try {
            return this.getClass().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(WaitingLine<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof WaitingLine1<?> : ""
                + "Violation of: source is of dynamic type WaitingLineSecondary<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Queue3<?>, and
         * the ? must be T or the call would not have compiled.
         */
        WaitingLine1<T> localSource = (WaitingLine1<T>) source;
        this.entries = localSource.entries;

        localSource.createNewRep();
    }

    /**
     * Kernel methods
     */

    @Override
    public void add(T x) {
        this.entries.add(this.length, x);
        this.length++;
    }

    @Override
    public T removeFirst() {
        T x = this.entries.remove(0);
        this.length--;
        return x;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public boolean contains(T entry) {
        boolean b = false;
        int i = 0;
        while (i < this.length && !b) {
            T x = this.entries.remove(i);
            if (x.equals(entry)) {
                b = !b;
            }
            this.entries.add(i, x);
            i++;

        }
        return b;
    }

    @Override
    public int pos(T x) {
        int pos = 0;
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            T tmp = it.next();
            pos++;
            if (tmp.equals(x)) {
                break;
            }
        }
        return pos;
    }

    /**
     * Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return this.entries.iterator();
    }

    /**
     * Other methods
     */

    @Override
    public T front() {
        Iterator<T> it = this.iterator();
        return it.next();
    }

    @Override
    public void add(T x, int pos) {
        this.entries.add(pos, x);
        this.length++;
    }

    @Override
    public T remove(int pos) {
        T x = this.entries.remove(pos);
        this.length--;
        return x;
    }
}