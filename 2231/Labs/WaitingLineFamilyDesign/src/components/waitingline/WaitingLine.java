package components.waitingline;

/**
 * {@code WaitingLineKernel} enhanced with secondary methods.
 *
 * @param <T>
 *            type of {@code WaitingLine} entries
 *
 * @author Ryan Shaffer
 *
 */
public interface WaitingLine<T> extends WaitingLineKernel<T> {

    /**
     * Reports the front of {@code this}.
     *
     * @return the front entry of {@code this}
     * @aliases reference returned by {@code front}
     * @requires this /= <>
     * @ensures <front> is prefix of this
     */
    T front();

    /**
     * Adds {@code x} to position {@code pos} in {@code this}.
     *
     * @param x
     *            the entry to be added
     * @param pos
     *            the position at which {@code x} will be added
     * @aliases reference {@code x}
     * @updates this
     * @requires x is not in this
     * @ensures this = #this[0, pos) * <x> * #this[pos, |#this|)
     */
    void add(T x, int pos);

    /**
     * Removes and returns {@code entry} from {@code this} at position pos.
     *
     * @param pos
     *            the position of the entry which is removed from {@code this}
     * @return the entry removed
     * @updates this
     * @requires
     *
     *           <pre>
     *  this /= <> and
     *  0<=pos and pos<=length
     *           </pre>
     *
     * @ensures
     *
     *          <pre>
     * this=#this\entry and
     *  remove=entry
     *          </pre>
     */
    T remove(int pos);
}