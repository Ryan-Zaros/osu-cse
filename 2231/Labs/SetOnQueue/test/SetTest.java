import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Put your name here
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor and returns the result.
     *
     * @return the new set
     * @ensures constructorRef = {}
     */
    protected abstract Set<String> constructorRef();

    /**
     * Creates and returns a {@code Set<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsTest = [entries in args]
     */
    private Set<String> createFromArgsTest(String... args) {
        Set<String> set = this.constructorTest();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Creates and returns a {@code Set<String>} of the reference implementation
     * type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsRef = [entries in args]
     */
    private Set<String> createFromArgsRef(String... args) {
        Set<String> set = this.constructorRef();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Test constructor.
     */
    @Test
    public final void testConstructor() {
        /**
         * Setup
         */
        Set<String> s = this.constructorTest();
        Set<String> sExpected = this.constructorRef();
        /**
         * Evaluation
         */
        assertEquals(sExpected, s);
    }

    /**
     * Test add.
     */
    @Test
    public final void testAdd() {
        /**
         * Setup
         */
        Set<String> s = this.createFromArgsTest("1", "2");
        Set<String> sExpected = this.createFromArgsRef("0", "1", "2");
        /**
         * Call
         */
        s.add("0");
        /**
         * Evaluation
         */
        assertEquals(sExpected, s);
    }

    /**
     * Test remove.
     */
    @Test
    public final void testRemove() {
        /**
         * Setup
         */
        Set<String> s = this.createFromArgsTest("0", "1", "2");
        Set<String> sExpected = this.createFromArgsRef("1", "2");
        /**
         * Call
         */
        s.remove("0");
        /**
         * Evaluation
         */
        assertEquals(sExpected, s);
    }

    /**
     * Test removeAny.
     */
    @Test
    public final void testRemoveAny() {
        /**
         * Setup
         */
        Set<String> s = this.createFromArgsTest("1", "2", "3");
        Set<String> sExpected = this.createFromArgsRef("0", "1", "2");
        /**
         * Call
         */
        String value = s.removeAny();
        /**
         * Evaluation
         */
        assertTrue(
                sExpected.contains(value) && s.size() == sExpected.size() - 1);
    }

    /**
     * Test contains.
     */
    @Test
    public final void testContains() {
        /**
         * Setup
         */
        Set<String> s = this.createFromArgsTest("0", "1", "2");
        Set<String> sExpected = this.createFromArgsRef("0", "1", "2");
        /**
         * Call
         */
        String value = "2";
        /**
         * Evaluation
         */
        assertTrue(s.contains(value) && sExpected.contains(value));
    }

    /**
     * Test size.
     */
    @Test
    public final void testSize() {
        /**
         * Setup
         */
        Set<String> s = this.createFromArgsTest("0", "1", "2");
        Set<String> sExpected = this.createFromArgsRef("1", "2");
        /**
         * Call
         */
        int sSize = s.size();
        int sExpectedSize = sExpected.size();
        /**
         * Evaluation
         */
        assertEquals(sExpectedSize, sSize);
    }

}
