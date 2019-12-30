import components.sequence.Sequence;

/**
 * Implements method to smooth a {@code Sequence<Integer>}.
 *
 * @author Ryan Shaffer
 *
 */
public final class SequenceSmooth {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private SequenceSmooth() {
    }

    /**
     * Smooths a given {@code Sequence<Integer>}.
     *
     * @param s1
     *            the sequence to smooth
     * @param s2
     *            the resulting sequence
     * @replaces s2
     * @requires |s1| >= 1
     * @ensures <pre>
     * |s2| = |s1| - 1  and
     *  for all i, j: integer, a, b: string of integer
     *      where (s1 = a * <i> * <j> * b)
     *    (there exists c, d: string of integer
     *       (|c| = |a|  and
     *        s2 = c * <(i+j)/2> * d))
     * </pre>
     */
//    public static void smooth(Sequence<Integer> s1, Sequence<Integer> s2) {
//        assert s1 != null : "Violation of: s1 is not null";
//        assert s2 != null : "Violation of: s2 is not null";
//        assert s1.length() >= 1 : "Violation of: |s1| >= 1";
//
//        if (s1.length() >= 0) {
//            s2.clear();
//            for (int k = 0; k < s1.length() - 1; k++) {
//                int i = s1.entry(k);
//                int j = s1.entry(k + 1);
//                int average;
//                if ((i > 0) && (j > 0) && ((i + j) < 0)) {
//                    if (i % 2 == 1 && j % 2 == 1) {
//                        i = i / 2;
//                        j = j / 2;
//                        average = (i + j) / 2;
//                        average = average * 2;
//                        average++;
//                    } else {
//                        i = i / 2;
//                        j = j / 2;
//                        average = (i + j) / 2;
//                        average = average * 2;
//                    }
//                } else {
//                    average = (i + j) / 2;
//                }
//                s2.add(k, average);
//            }
//        }
//
//    }

    public static void smooth(Sequence<Integer> s1, Sequence<Integer> s2) {
        assert s1 != null : "Violation of: s1 is not null";
        assert s2 != null : "Violation of: s2 is not null";
        assert s1.length() >= 1 : "Violation of: |s1| >= 1";

        if (s1.length() == 1) {
            s2.clear();
        }

        else {
            int i = s1.remove(0);
            int j = s1.entry(0);
            int average = (i + j) / 2;
            smooth(s1, s2);
            s2.add(0, average);
            s1.add(0, i);
        }
    }
}
