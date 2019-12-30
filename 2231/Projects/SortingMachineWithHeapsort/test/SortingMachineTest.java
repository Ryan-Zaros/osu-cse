import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Ryan Shaffer.555
 * @author Chris Tuttle.219
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /*
     * Constructor Test -------------------------------------------------------
     */

    /*
     * Tests the constructor.
     */
    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    /*
     * Add Tests --------------------------------------------------------------
     */

    /*
     * Tests add when it's empty.
     */
    @Test
    public final void testAddEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("green");
        assertEquals(mExpected, m);
    }

    /*
     * Tests add when there is a single entry.
     */
    @Test
    public final void testAddSingle() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "blue");
        m.add("blue");
        assertEquals(mExpected, m);
    }

    /*
     * Tests add when there are multiple entries.
     */
    @Test
    public final void testAddMultiple() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "red",
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "red", "green", "blue");
        m.add("blue");
        assertEquals(mExpected, m);
    }

    /*
     * ChangeToExtractionMode Tests -------------------------------------------
     */

    /*
     * Tests changeToExtractionMode when empty.
     */
    @Test
    public final void testChangeToExtractionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        /*
         * Initializing as false instead of changing to extraction mode.
         */
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    /*
     * Tests changeToExtractionMode when non-empty.
     */
    @Test
    public final void testChangeToExtractionModeNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "red",
                "green", "blue");
        /*
         * Initializing as false instead of changing to extraction mode.
         */
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "red", "green", "blue");
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    /*
     * RemoveFirst Tests ------------------------------------------------------
     */

    /*
     * Tests removeFirst into empty.
     */
    @Test
    public final void testRemoveFirstToEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        String value = m.removeFirst();
        assertEquals(value, "green");
        assertEquals(mExpected, m);
    }

    /*
     * Tests removeFirst when there are multiple entries.
     */
    @Test
    public final void testRemoveFirstMultiple() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "red",
                "green", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "red", "green");
        String value = m.removeFirst();
        assertEquals(value, "blue");
        assertEquals(mExpected, m);
    }

    /*
     * Tests removeFirst until empty.
     */
    @Test
    public final void testRemoveFirstAll() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "red",
                "green", "blue", "purple", "black");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        /*
         * Remove until empty.
         */
        String value1 = m.removeFirst();
        String value2 = m.removeFirst();
        String value3 = m.removeFirst();
        String value4 = m.removeFirst();
        String value5 = m.removeFirst();
        /*
         * Assert equalities.
         */
        assertEquals(value1, "black");
        assertEquals(value2, "blue");
        assertEquals(value3, "green");
        assertEquals(value4, "purple");
        assertEquals(value5, "red");
        assertEquals(mExpected, m);
    }

    /*
     * IsInInsertionMode Tests ------------------------------------------------
     */

    /*
     * Tests isInInsertionMode when empty and true.
     */
    @Test
    public final void testIsInInsertionModeEmptyTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        assertEquals(m.isInInsertionMode(), true);
    }

    /*
     * Tests isInInsertionMode when non-empty and true.
     */
    @Test
    public final void testIsInInsertionModeNonEmptyTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "red",
                "green", "blue");
        assertEquals(m.isInInsertionMode(), true);
    }

    /*
     * Tests isInInsertionMode when empty and false.
     */
    @Test
    public final void testIsInInsertionModeEmptyFalse() {
        /*
         * Initializing as false instead of changing to extraction mode.
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        assertEquals(m.isInInsertionMode(), false);
    }

    /*
     * Tests isInInsertionMode when non-empty and false.
     */
    @Test
    public final void testIsInInsertionModeNonEmptyFalse() {
        /*
         * Initializing as false instead of changing to extraction mode.
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "red",
                "green", "blue");
        assertEquals(m.isInInsertionMode(), false);
    }

    /*
     * Order Tests ------------------------------------------------------------
     */

    /*
     * Tests order while in insertion mode.
     */
    @Test
    public final void testOrderInsertion() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        assertEquals(m.order(), ORDER);
    }

    /*
     * Tests order while in extraction mode.
     */
    @Test
    public final void testOrderExtraction() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        assertEquals(m.order(), ORDER);
    }

    /*
     * Size Tests -------------------------------------------------------------
     */

    /*
     * Tests size when empty while in insertion mode.
     */
    @Test
    public final void testSizeInsertionEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        assertEquals(m.size(), 0);
    }

    /*
     * Tests size when there is a single entry while in insertion mode.
     */
    @Test
    public final void testSizeInsertionSingle() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        assertEquals(m.size(), 1);
    }

    /*
     * Tests size when there are multiple entries while in insertion mode.
     */
    @Test
    public final void testSizeInsertionMultiple() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "red",
                "green", "blue");
        assertEquals(m.size(), 3);
    }

    /*
     * Tests size when empty while in extraction mode.
     */
    @Test
    public final void testSizeExtractionEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        assertEquals(m.size(), 0);
    }

    /*
     * Tests size when there is a single entry while in extraction mode.
     */
    @Test
    public final void testSizeExtractionSingle() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        assertEquals(m.size(), 1);
    }

    /*
     * Tests size when there are multiple entries while in extraction mode.
     */
    @Test
    public final void testSizeExtractionMultiple() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "red",
                "green", "blue");
        assertEquals(m.size(), 3);
    }
}
