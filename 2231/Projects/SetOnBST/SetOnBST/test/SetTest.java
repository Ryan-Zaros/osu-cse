import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Chris Tuttle.219, Ryan Shaffer.555
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor for the reference
     * implementation and returns the result.
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
     * Tests add when the initial set is empty.
     */
    @Test
    public void testAddEmpty() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest();
        Set<String> nExpected = this.createFromArgsRef("b");
        /*
         * Call method.
         */
        n.add("b");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
    }

    /**
     * Tests add when the initial set is non-empty, and is added to the end of
     * the right subtree.
     */
    @Test
    public void testAddRightTree1() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "d", "a", "c");
        /*
         * Call method.
         */
        n.add("d");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
    }

    /**
     * Tests add when the initial set is non-empty, and is added to the middle
     * of the right subtree
     */
    @Test
    public void testAddRightTree2() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "d");
        Set<String> nExpected = this.createFromArgsRef("b", "a", "c", "d");
        /*
         * Call method.
         */
        n.add("c");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
    }

    /**
     * Tests add when the initial set is non-empty, and is added to the left
     * subtree.
     */
    @Test
    public void testAddLeftTree() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b");
        Set<String> nExpected = this.createFromArgsRef("b", "a");
        /*
         * Call method.
         */
        n.add("a");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
    }

    /**
     * Tests remove on the root with no children (removing to an empty set).
     */
    @Test
    public void testRemoveToEmpty() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("a");
        Set<String> nExpected = this.createFromArgsRef();
        /*
         * Call method.
         */
        String removed = n.remove("a");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "a");
    }

    /**
     * Tests remove on a nonempty set. It is removed from the middle of the
     * right subtree.
     */
    @Test
    public void testRemoveRightTree1() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("a", "b", "c");
        Set<String> nExpected = this.createFromArgsRef("a", "c");
        /*
         * Call method.
         */
        String removed = n.remove("b");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "b");
    }

    /**
     * Tests remove on a nonempty set. It is removed from the end of the right
     * subtree.
     */
    @Test
    public void testRemoveRightTree2() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("a", "b", "c");
        Set<String> nExpected = this.createFromArgsRef("a", "b");
        /*
         * Call method.
         */
        String removed = n.remove("c");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "c");
    }

    /**
     * Tests remove on the root when there is no left tree.
     */
    @Test
    public void testRemoveRootEmptyLeft() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("a", "b", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "c");
        /*
         * Call method.
         */
        String removed = n.remove("a");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "a");
    }

    /**
     * Tests remove on the root with an empty right tree.
     */
    @Test
    public void testRemoveRootEmptyRight() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b", "a");
        Set<String> nExpected = this.createFromArgsRef("a");
        /*
         * Call method.
         */
        String removed = n.remove("b");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "b");
    }

    /**
     * Tests remove on the root with two non-empty children.
     */
    @Test
    public void testRemoveRootNonEmptyBoth() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("c", "a");
        /*
         * Call method.
         */
        String removed = n.remove("b");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "b");
    }

    /**
     * Tests remove in the left subtree when there are 2 children.
     */
    @Test
    public void testRemoveLeftNonEmptyBoth() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "c");
        /*
         * Call method.
         */
        String removed = n.remove("a");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "a");
    }

    /**
     * Tests remove in the right subtree when there are 2 children.
     */
    @Test
    public void testRemoveRightNonEmptyBoth() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        Set<String> nExpected = this.createFromArgsRef("b", "a");
        /*
         * Call method.
         */
        String removed = n.remove("c");
        /*
         * Assert equality.
         */
        assertEquals(n, nExpected);
        assertEquals(removed, "c");
    }

    /**
     * Tests removeAny resulting in an empty set.
     */
    @Test
    public void testRemoveAnyToEmpty() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b");
        Set<String> nExpected = this.createFromArgsRef("b");

        String removed = n.removeAny();
        boolean isIn = nExpected.contains(removed);
        String temp = nExpected.remove("b");
        /*
         * Assert equality.
         */
        assertEquals(true, isIn);
        assertEquals(n, nExpected);
        assertEquals(removed, temp);
    }

    /**
     * Tests removeAny on a non-empty set.
     */
    @Test
    public void testRemoveAnyNonEmpty() {
        /*
         * Create instances.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c", "d");
        Set<String> nExpected = this.createFromArgsRef("b", "a", "c", "d");
        /*
         * Call method.
         */
        String removed = n.removeAny();
        boolean isIn = nExpected.contains(removed);
        String temp = nExpected.remove("a");
        /*
         * Assert equality.
         */
        assertEquals(true, isIn);
        assertEquals(n, nExpected);
        assertEquals(removed, temp);
    }

    /**
     * Tests contains on a set of 1 (just the root) when true.
     */
    @Test
    public void testContainsTrueSingle() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest("b");
        /*
         * Call method.
         */
        boolean contain = n.contains("b");
        /*
         * Assert equality.
         */
        assertEquals(true, contain);
    }

    /**
     * Tests contains on the left subtree when true.
     */
    @Test
    public void testContainsTrueLeftTree() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        /*
         * Call method.
         */
        boolean contain = n.contains("a");
        /*
         * Assert equality.
         */
        assertEquals(true, contain);
    }

    /**
     * Tests contains on the right subtree when true.
     */
    @Test
    public void testContainsTrueRightTree() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        /*
         * Call method.
         */
        boolean contain = n.contains("c");
        /*
         * Assert equality.
         */
        assertEquals(true, contain);
    }

    /**
     * Tests contains on the root when both children are non-empty, when true.
     */
    @Test
    public void testContainsTrueRoot() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        /*
         * Call method.
         */
        boolean contain = n.contains("b");
        /*
         * Assert equality.
         */
        assertEquals(true, contain);
    }

    /**
     * Tests contains on a set with a single element when it's false.
     */
    @Test
    public void testContainsFalseSingle() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest("b");
        /*
         * Call method.
         */
        boolean contain = n.contains("w");
        /*
         * Assert equality.
         */
        assertEquals(false, contain);

    }

    /**
     * Tests contains on a set with multiple elements when it's false.
     */
    @Test
    public void testContainsFalseMultiple() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest("b", "a", "c");
        /*
         * Call method.
         */
        boolean contain = n.contains("w");
        /*
         * Assert equality.
         */
        assertEquals(false, contain);
    }

    /**
     * Tests size on empty set.
     */
    @Test
    public void testSizeEmpty() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest();
        /*
         * Call method.
         */
        int size = 0;
        int nSize = n.size();
        /*
         * Assert equality.
         */
        assertEquals(nSize, size);
    }

    /**
     * Tests size on a set with a single element.
     */
    @Test
    public void testSizeSingle() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest("a");
        /*
         * Call method.
         */
        int size = 1;
        int nSize = n.size();
        /*
         * Assert equality.
         */
        assertEquals(nSize, size);
    }

    /**
     * Tests size on a set with multiple elements.
     */
    @Test
    public void testSize3() {
        /*
         * Create instance.
         */
        Set<String> n = this.createFromArgsTest("a", "b", "c");
        /*
         * Call method.
         */
        int size = 3;
        int nSize = n.size();
        /*
         * Assert equality.
         */
        assertEquals(nSize, size);
    }
}
