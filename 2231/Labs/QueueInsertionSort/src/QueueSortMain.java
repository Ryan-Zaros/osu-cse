import java.util.Comparator;

import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;

/**
 * Program to sort lines from an input file in lexicographic order by using
 * insertion sort on {@code Queue<String>}.
 *
 * @author Paolo Bucci
 */
public final class QueueSortMain {

    /**
     * Compare {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private QueueSortMain() {
        // no code needed here
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Get input file name and open input stream
         */
        out.print("Enter an input file name: ");
        String fileName = in.nextLine();
        SimpleReader file = new SimpleReader1L(fileName);

        Comparator<String> cs = new StringLT();

        /*
         * Get lines from input
         */
        SortingMachine<String> sm = new SortingMachine3<String>(cs);
        while (!file.atEOS()) {
            String str = file.nextLine();
            sm.add(str);
        }
        file.close();

        /*
         * Sort lines into non-decreasing lexicographic order
         */
        sm.changeToExtractionMode();

        /*
         * Output lines in sorted order
         */
        out.println();
        while (sm.size() > 0) {
            String str = sm.removeFirst();
            out.println(str);
        }

        in.close();
        out.close();
    }

}
