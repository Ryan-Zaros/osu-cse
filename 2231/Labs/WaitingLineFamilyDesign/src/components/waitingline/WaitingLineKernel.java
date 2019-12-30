package components.waitingline;

import components.standard.Standard;

/**
 * First-in-first-out (FIFO) waitingline kernel component with primary methods.
 *
 * @param <T>
 *            type of {@code WaitingLineKernel} entries
 * @mathmodel type WaitingLineKernel is modeled by unique strings of T
 * @initially
 *
 *            <pre>
 * default:
 *  ensures
 *   this = <>
 *            </pre>
 *
 * @iterator ~this.seen * ~this.unseen = this
 *
 * @author Ryan Shaffer
 *
 */
public interface WaitingLineKernel<T>
        extends Standard<WaitingLine<T>>, Iterable<T> {

    /**
     * Adds {@code x} to the end of {@code this}.
     *
     * @param x
     *            the entry to be added
     * @aliases reference {@code x}
     * @updates this
     * @requires x is not in this
     * @ensures this = #this * x
     */
    void add(T x);

    /**
     * Removes and returns the entry at the front of {@code this}.
     *
     * @return the entry removed
     * @updates this
     * @requires this /= <>
     * @ensures #this = <removeFirst> * this
     */
    T removeFirst();

    /**
     * Reports length of {@code this}.
     *
     * @return the length of {@code this}
     * @ensures length = |this|
     */
    int length();

    /**
     * Reports whether {@code entry} is in {@code this}.
     *
     * @param entry
     *            the element to be checked
     * @return true iff {@code entry} is in {@code this}
     * @ensures contains = (entry is in this)
     */
    boolean contains(T entry);

    /**
     * Reports position of {@code entry} in waiting line {@code this}.
     *
     * @param entry
     *            the entry which position is reported
     *
     * @return the position of {@code entry}
     * @requires entry is in this
     * @ensures entry = this[pos, pos+1)
     */
    int pos(T entry);
}