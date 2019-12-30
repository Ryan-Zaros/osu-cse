import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;
import components.map.Map.Pair;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Ryan Shaffer
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     * Tests constructor
     */
    @Test
    public void ConstructorTest() {
        Map<String, String> s = this.constructorTest();
        Map<String, String> sExpected = this.constructorRef();

        assertEquals(s, sExpected);
    }

    /**
     * Tests single-add from empty map
     */
    @Test
    public void testAddSingle() {
        Map<String, String> s = this.constructorTest();
        Map<String, String> sExpected = this.createFromArgsRef("red", "5");
        s.add("red", "5");

        assertEquals(s, sExpected);
    }

    /**
     * Tests add with multiple pairs
     */
    @Test
    public void testAddMultiple() {
        Map<String, String> s = this.constructorTest();
        Map<String, String> sExpected = this.createFromArgsRef("red", "5",
                "blue", "6");
        s.add("red", "5");
        s.add("blue", "6");

        assertEquals(s, sExpected);
    }

    /**
     * Tests hasKey for a single key that is in the map
     */
    @Test
    public void testHasKeySingleTrue() {
        Map<String, String> s = this.createFromArgsTest("red", "5");
        Map<String, String> sExpected = this.createFromArgsRef("blue", "6");

        assertEquals(s.hasKey("red"), true);
    }

    /**
     * Tests hasKey for multiple keys that are in the map
     */
    @Test
    public void testHasKeyMultipleTrue() {
        Map<String, String> s = this.createFromArgsTest("red", "5", "blue",
                "6");
        Map<String, String> sExpected = this.createFromArgsRef("red", "5",
                "blue", "6");

        assertEquals(s.hasKey("red"), true);
        assertEquals(s.hasKey("blue"), true);
        assertEquals(s, sExpected);
    }

    /**
     * Tests hasKey for a single key that isn't in the map
     */
    @Test
    public void testHasKeySingleFalse() {
        Map<String, String> s = this.createFromArgsTest("red", "5");
        Map<String, String> sExpected = this.createFromArgsRef("red", "6");

        assertEquals(s.hasKey("blue"), false);
    }

    /**
     * Tests hasKey for a value that is in the map
     */
    @Test
    public void testHasKeyValueFalse() {
        Map<String, String> s = this.createFromArgsTest("red", "5");
        Map<String, String> sExpected = this.createFromArgsRef("blue", "6");

        assertEquals(s.hasKey("5"), false);
    }

    /**
     * Tests remove with a single pair
     */
    @Test
    public void testRemoveSingle() {
        Map<String, String> s = this.createFromArgsTest("red", "5");
        Map<String, String> sExpected = this.constructorRef();

        assertEquals(s.hasKey("red"), true);

        Pair<String, String> x = s.remove("red");
        assertEquals(x.value(), "5");

        assertEquals(s, sExpected);
    }

    /**
     * Tests remove with multiple pairs
     */
    @Test
    public void testRemoveMultiple() {
        Map<String, String> s = this.createFromArgsTest("red", "5", "blue",
                "6");
        Map<String, String> sExpected = this.constructorRef();

        assertEquals(s.hasKey("red"), true);
        Pair<String, String> x = s.remove("red");
        assertEquals(x.value(), "5");
        Pair<String, String> y = s.remove("blue");
        assertEquals(y.value(), "6");

        assertEquals(s, sExpected);
    }

    /**
     * Tests size when empty
     */
    @Test
    public void testSizeEmpty() {
        Map<String, String> s = this.constructorTest();

        assertEquals(s.size(), 0);
    }

    /**
     * Tests size with 1 pair
     */
    @Test
    public void testSizeSingle() {
        Map<String, String> s = this.createFromArgsTest("blue", "5");
        Map<String, String> sExpected = this.createFromArgsRef("red", "6");

        assertEquals(s.size(), 1);
    }

    /**
     * Tests size with multiple pairs
     */
    @Test
    public void testSizeMultiple() {
        Map<String, String> s = this.createFromArgsTest("red", "5", "blue",
                "6");
        Map<String, String> sExpected = this.createFromArgsRef("red", "5",
                "blue", "6");

        assertEquals(s.size(), 2);
        assertEquals(s, sExpected);
    }

    /**
     * Tests value of a single key
     */
    @Test
    public void testValueSingle() {
        Map<String, String> s = this.createFromArgsTest("red", "5");
        Map<String, String> sExpected = this.createFromArgsRef("blue", "6");

        assertEquals(s.value("red"), "5");
    }

    /**
     * Tests value of multiple keys
     */
    @Test
    public void testValueMultiple() {
        Map<String, String> s = this.createFromArgsTest("red", "5", "blue",
                "6");
        Map<String, String> sExpected = this.createFromArgsRef("red", "5",
                "blue", "6");

        assertEquals(s.value("red"), "5");
        assertEquals(s.value("blue"), "6");
        assertEquals(s, sExpected);
    }

    /**
     * Tests removeAny with a single pair
     */
    @Test
    public void testRemoveAnySingle() {
        Map<String, String> s = this.createFromArgsTest("red", "5");
        Map<String, String> sExpected = this.createFromArgsRef();

        Pair<String, String> x = s.removeAny();
        assertEquals(x.value(), "5");
        assertEquals(x.key(), "value");
        assertEquals(s, sExpected);
    }

    /**
     * Tests removeAny with multiple pairs
     */
    @Test
    public void removeAnyTestMultiple() {
        Map<String, String> s = this.createFromArgsTest("red", "5", "blue",
                "6");

        Pair<String, String> x = s.removeAny();
        assertEquals(s.hasKey(x.key()), false);
        assertEquals(s.size(), 1);
    }
}