import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Ryan Shaffer.555, Chris Tuttle.219
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
     * Tests the no-argument constructor.
     */
    @Test
    public void noArgConstructorTest() {
        // Create instances
        Map<String, String> map = this.constructorTest();
        Map<String, String> mapExpected = this.constructorRef();
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Tests add on an empty map.
     */
    @Test
    public void testAddEmpty() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest();
        Map<String, String> mapExpected = this.createFromArgsRef("C", "3");
        // Call method
        map.add("C", "3");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Tests adding a single pair.
     */
    @Test
    public void testAddNonEmpty() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2");
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1", "B",
                "2", "C", "3");
        // Call method
        map.add("C", "3");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Tests adding multiple pairs.
     */
    @Test
    public void testAddNonMultiple() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("B", "2");
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1", "B",
                "2", "C", "3");
        // Call methods
        map.add("C", "3");
        map.add("A", "1");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Tests remove on a map that will end up empty.
     */
    @Test
    public void testRemoveEmpty() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("C", "3");
        Map<String, String> mapExpected = this.createFromArgsRef();
        // Call method
        map.remove("C");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Tests remove with a single pair.
     */
    @Test
    public void testRemoveNonEmpty() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1", "B",
                "2");
        // Call method
        map.remove("C");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Tests remove with multiple pairs.
     */
    @Test
    public void testRemoveNonEmptyMultiple() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        Map<String, String> mapExpected = this.createFromArgsRef("B", "2");
        // Call methods
        map.remove("C");
        map.remove("A");
        // Assert equality
        assertEquals(map, mapExpected);
    }

    /**
     * Tests removeAny.
     */
    @Test
    public void testRemoveAny() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        Map<String, String> mapExpected = this.createFromArgsRef("A", "1", "B",
                "2", "C", "3");
        // Call method
        Map.Pair<String, String> test = map.removeAny();
        // Confirm reference has that pair
        assertTrue(mapExpected.hasKey(test.key()));
        // Remove pair from reference
        mapExpected.remove(test.key());
        // Assert equality
        assertEquals(map, mapExpected);

    }

    /**
     * Tests value with a single pair.
     */
    @Test
    public void testValueSingle() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        // Call method
        String testValue = map.value("A");
        String expectedValue = "1";
        // Assert equality
        assertEquals(testValue, expectedValue);
    }

    /**
     * Tests value with multiple pairs.
     */
    @Test
    public void testValueMultiple() {
        // Create instances
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        // Call methods
        String testValue1 = map.value("B");
        String expectedValue1 = "2";
        String testValue2 = map.value("C");
        String expectedValue2 = "3";
        // Assert equality
        assertEquals(testValue1, expectedValue1);
        assertEquals(testValue2, expectedValue2);
    }

    /**
     * Tests hasKey on a single key when true.
     */
    @Test
    public void testHasKeySingleTrue() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        // Call method
        boolean hasKey = map.hasKey("A");
        // Assert true
        assertTrue(hasKey);
    }

    /**
     * Tests hasKey on multiple pairs when true.
     */
    @Test
    public void testHasKeyMultipleTrue() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        // Call methods
        boolean hasKey1 = map.hasKey("B");
        boolean hasKey2 = map.hasKey("C");
        // Assert true
        assertTrue(hasKey1);
        assertTrue(hasKey2);
    }

    /**
     * Tests hasKey on a single pair when false.
     */
    @Test
    public void testHasKeySingleFalse() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("W", "1", "B", "2",
                "C", "3");
        // Call method
        boolean hasKey = map.hasKey("A");
        // Assert true
        assertTrue(!hasKey);
    }

    /**
     * Tests hasKey on multiple pairs when false.
     */
    @Test
    public void testHasKeyMultipleFalse() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("W", "1", "X", "2",
                "Y", "3");
        // Call methods
        boolean hasKey1 = map.hasKey("B");
        boolean hasKey2 = map.hasKey("C");
        // Assert true
        assertTrue(!hasKey1);
        assertTrue(!hasKey2);
    }

    /**
     * Tests size on an empty map.
     */
    @Test
    public void testSizeEmpty() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest();
        // Create reference size
        int mapExpected = 0;
        // Assert equality
        assertEquals(map.size(), mapExpected);
    }

    /**
     * Tests size on a map with a single pair.
     */
    @Test
    public void testSizeSingle() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("A", "1");
        // Create reference size
        int mapExpected = 1;
        // Assert equality
        assertEquals(map.size(), mapExpected);
    }

    /**
     * Tests size on a map with multiple pairs.
     */
    @Test
    public void testSizeMultiple() {
        // Create instance
        Map<String, String> map = this.createFromArgsTest("A", "1", "B", "2",
                "C", "3");
        // Create reference size
        int expectedSize = 3;
        // Assert equality
        assertEquals(map.size(), expectedSize);
    }
}
