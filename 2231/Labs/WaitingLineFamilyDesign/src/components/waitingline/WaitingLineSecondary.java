package components.waitingline;

import java.util.Iterator;

/**
 * @author Ryan Shaffer
 *
 * @param <T>
 * @param T
 *            type of {@code WaitingLine} entries
 *
 */
public abstract class WaitingLineSecondary<T> implements WaitingLine<T> {

    @Override
    public int hashCode() {
        final int samples = 2;
        final int a = 37;
        final int b = 17;
        int result = 0;
        int n = 0;
        Iterator<T> it = this.iterator();
        while (n < samples && it.hasNext()) {
            n++;
            T x = it.next();
            result = a * result + b * x.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof WaitingLine<?>)) {
            return false;
        }
        WaitingLine<?> w = (WaitingLine<?>) obj;
        if (this.length() != w.length()) {
            return false;
        }
        Iterator<T> it1 = this.iterator();
        Iterator<?> it2 = w.iterator();
        while (it1.hasNext()) {
            T x1 = it1.next();
            Object x2 = it2.next();
            if (!x1.equals(x2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("<");
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            result.append(it.next());
            if (it.hasNext()) {
                result.append(",");
            }
        }
        result.append(">");
        return result.toString();
    }
}