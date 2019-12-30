import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.binarytree.BinaryTree;

/**
 * JUnit test fixture for {@code BinaryTree<String>}'s methods.
 *
 * @author Paolo Bucci
 *
 */
public final class BinaryTreeMethodsTest {

    @Test
    public void testHeightEmpty() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility.treeFromString("()");
        BinaryTree<String> tExpected = BinaryTreeUtility.treeFromString("()");
        /*
         * Call method under test
         */
        int h = BinaryTreeMethods.height(t);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(0, h);
        assertEquals(tExpected, t);
    }

    @Test
    public void testHeightOne() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility.treeFromString("a(()())");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(()())");
        /*
         * Call method under test
         */
        int h = BinaryTreeMethods.height(t);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(1, h);
        assertEquals(tExpected, t);
    }

    @Test
    public void testHeightTwo() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility.treeFromString("a(b(()())())");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(b(()())())");
        /*
         * Call method under test
         */
        int h = BinaryTreeMethods.height(t);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(2, h);
        assertEquals(tExpected, t);
    }

    @Test
    public void testHeightMany() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        /*
         * Call method under test
         */
        int h = BinaryTreeMethods.height(t);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(5, h);
        assertEquals(tExpected, t);
    }

    @Test
    public void testHeightMore() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility.treeFromString(
                "a(b(d(g(j(()())())())())c(e(h(()())i(()()))f(()())))");
        BinaryTree<String> tExpected = BinaryTreeUtility.treeFromString(
                "a(b(d(g(j(()())())())())c(e(h(()())i(()()))f(()())))");
        /*
         * Call method under test
         */
        int h = BinaryTreeMethods.height(t);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(5, h);
        assertEquals(tExpected, t);
    }

    @Test
    public void testIsInTreeREmpty() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility.treeFromString("()");
        BinaryTree<String> tExpected = BinaryTreeUtility.treeFromString("()");
        /*
         * Call method under test
         */
        boolean found = BinaryTreeMethods.isInTreeRecursive(t, "a");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(false, found);
        assertEquals(tExpected, t);
    }

    @Test
    public void testIsInTreeROneTrue() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility.treeFromString("a(()())");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(()())");
        /*
         * Call method under test
         */
        boolean found = BinaryTreeMethods.isInTreeRecursive(t, "a");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(true, found);
        assertEquals(tExpected, t);
    }

    @Test
    public void testIsInTreeROneFalse() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility.treeFromString("a(()())");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(()())");
        /*
         * Call method under test
         */
        boolean found = BinaryTreeMethods.isInTreeRecursive(t, "b");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(false, found);
        assertEquals(tExpected, t);
    }

    @Test
    public void testIsInTreeRManyLeftTrue() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        /*
         * Call method under test
         */
        boolean found = BinaryTreeMethods.isInTreeRecursive(t, "b");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(true, found);
        assertEquals(tExpected, t);
    }

    @Test
    public void testIsInTreeRManyRightTrue() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        /*
         * Call method under test
         */
        boolean found = BinaryTreeMethods.isInTreeRecursive(t, "c");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(true, found);
        assertEquals(tExpected, t);
    }

    @Test
    public void testIsInTreeRManyLeafTrue() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        /*
         * Call method under test
         */
        boolean found = BinaryTreeMethods.isInTreeRecursive(t, "f");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(true, found);
        assertEquals(tExpected, t);
    }

    @Test
    public void testIsInTreeRManyRootTrue() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        /*
         * Call method under test
         */
        boolean found = BinaryTreeMethods.isInTreeRecursive(t, "a");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(true, found);
        assertEquals(tExpected, t);
    }

    @Test
    public void testIsInTreeRManyFalse() {
        /*
         * Set up variables
         */
        BinaryTree<String> t = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        BinaryTree<String> tExpected = BinaryTreeUtility
                .treeFromString("a(b(()())c(d(()e(f(()())()))()))");
        /*
         * Call method under test
         */
        boolean found = BinaryTreeMethods.isInTreeRecursive(t, "g");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(false, found);
        assertEquals(tExpected, t);
    }

}
