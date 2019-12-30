import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.stack.Stack;

/**
 * Example of JUnit test fixture for {@code Stack<String>}. Note: these are just
 * some sample test cases. The fixture is neither complete nor systematically
 * designed.
 *
 * @author Paolo Bucci
 *
 */
public abstract class StackTest {

    /**
     * Invokes the appropriate {@code Stack} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new stack
     * @ensures constructorTest = <>
     */
    protected abstract Stack<String> constructorTest();

    /**
     * Invokes the appropriate {@code Stack} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new stack
     * @ensures constructorRef = <>
     */
    protected abstract Stack<String> constructorRef();

    /**
     *
     * Creates and returns a {@code Stack<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the stack
     * @return the constructed stack
     * @ensures createFromArgsTest = [entries in args]
     */
    private Stack<String> createFromArgsTest(String... args) {

        Stack<String> s1 = this.constructorTest();
        Stack<String> s2 = this.constructorTest();
        for (String str : args) {
            s1.push(str);
        }
        for (String str : s1) {
            s2.push(str);
        }
        return s2;
    }

    /**
     *
     * Creates and returns a {@code Stack<String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the entries for the stack
     * @return the constructed stack
     * @ensures createFromArgsRef = [entries in args]
     */
    private Stack<String> createFromArgsRef(String... args) {

        Stack<String> s1 = this.constructorRef();
        Stack<String> s2 = this.constructorRef();
        for (String str : args) {
            s1.push(str);
        }
        for (String str : s1) {
            s2.push(str);
        }
        return s2;
    }

    /*
     * Test StackKernel constructor(s) and methods. Note: these are just some
     * sample test cases, not complete nor systematic.
     */

    @Test
    public final void testNoArgumentConstructor() {
        /*
         * Set up variables and call method under test
         */
        Stack<String> s = this.constructorTest();
        Stack<String> sExpected = this.constructorRef();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(sExpected, s);
    }

    @Test
    public final void testPushNonEmpty() {
        /*
         * Set up variables
         */
        Stack<String> s = this.createFromArgsTest("red", "blue");
        Stack<String> sExpected = this.createFromArgsRef("green", "red",
                "blue");
        /*
         * Call method under test
         */
        s.push("green");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(sExpected, s);
    }

    @Test
    public final void testPopLeavingEmpty() {
        /*
         * Set up variables
         */
        Stack<String> s = this.createFromArgsTest("red");
        Stack<String> sExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        String x = s.pop();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(sExpected, s);
        assertEquals("red", x);
    }

    @Test
    public final void testLengthNonEmpty() {
        /*
         * Set up variables
         */
        Stack<String> s = this.createFromArgsTest("red", "blue");
        Stack<String> sExpected = this.createFromArgsRef("red", "blue");
        /*
         * Call method under test
         */
        int i = s.length();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(2, i);
        assertEquals(sExpected, s);
    }

    /*
     * Test StackCommon methods. Note: these are just some sample test cases,
     * not complete nor systematic.
     */

    @Test
    public final void testEqualsNonEmptyEmpty() {
        /*
         * Set up variables
         */
        Stack<String> s1 = this.createFromArgsTest("red", "blue");
        Stack<String> s2 = this.createFromArgsTest();
        Stack<String> s1Expected = this.createFromArgsRef("red", "blue");
        Stack<String> s2Expected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        boolean eq = s1.equals(s2);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(false, eq);
        assertEquals(s1Expected, s1);
        assertEquals(s2Expected, s2);
    }

    @Test
    public final void testToStringEmpty() {
        /*
         * Set up variables
         */
        Stack<String> s = this.createFromArgsTest();
        Stack<String> sExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        String str = s.toString();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals("<>", str);
        assertEquals(sExpected, s);
    }

    @Test
    public final void testHashCodeSame() {
        /*
         * Set up variables
         */
        Stack<String> s1 = this.createFromArgsTest("red", "blue");
        Stack<String> s2 = this.createFromArgsTest("red", "blue");
        Stack<String> s1Expected = this.createFromArgsRef("red", "blue");
        Stack<String> s2Expected = this.createFromArgsRef("red", "blue");
        /*
         * Call method under test
         */
        int hc1 = s1.hashCode();
        int hc2 = s2.hashCode();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(hc1, hc2);
        assertEquals(s1Expected, s1);
        assertEquals(s2Expected, s2);
    }

    /*
     * Test Standard methods. Note: these are just some sample test cases, not
     * complete nor systematic.
     */

    @Test
    public final void testNewInstanceNonEmpty() {
        /*
         * Set up variables
         */
        Stack<String> s1 = this.createFromArgsTest("red", "blue");
        Stack<String> s1Expected = this.createFromArgsRef("red", "blue");
        Stack<String> s2Expected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        Stack<String> s2 = s1.newInstance();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(s2Expected, s2);
        assertEquals(s1.getClass(), s2.getClass());
        assertEquals(s1Expected, s1);
    }

    @Test
    public final void testTransferFromNonEmpty() {
        /*
         * Set up variables
         */
        Stack<String> s1 = this.createFromArgsTest("red", "blue");
        Stack<String> s2 = this.createFromArgsTest();
        Stack<String> s1Expected = this.createFromArgsRef();
        Stack<String> s2Expected = this.createFromArgsRef("red", "blue");
        /*
         * Call method under test
         */
        s2.transferFrom(s1);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(s2Expected, s2);
        assertEquals(s1Expected, s1);
    }

    @Test
    public final void testClearNonEmpty() {
        /*
         * Set up variables
         */
        Stack<String> s = this.createFromArgsTest("red", "blue");
        Stack<String> sExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        s.clear();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(sExpected, s);
    }

    /*
     * Test StackSecondary methods. Note: these are just some sample test cases,
     * not complete nor systematic.
     */

    @Test
    public final void testTop() {
        /*
         * Set up variables
         */
        Stack<String> s = this.createFromArgsTest("red");
        Stack<String> sExpected = this.createFromArgsRef("red");
        /*
         * Call method under test
         */
        String x = s.top();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals("red", x);
        assertEquals(sExpected, s);
    }

    @Test
    public final void testFlipThree() {
        /*
         * Set up variables
         */
        Stack<String> s = this.createFromArgsTest("red", "green", "blue");
        Stack<String> sExpected = this.createFromArgsRef("blue", "green",
                "red");
        /*
         * Call method under test
         */
        s.flip();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(sExpected, s);
    }

}