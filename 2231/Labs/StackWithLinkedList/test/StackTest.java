import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.stack.Stack;

/**
 * JUnit test fixture for {@code Stack<String>}'s constructor and kernel
 * methods.
 *
 * @author Ryan Shaffer
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
        Stack<String> stack = this.constructorTest();
        for (String s : args) {
            stack.push(s);
        }
        stack.flip();
        return stack;
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
        Stack<String> stack = this.constructorRef();
        for (String s : args) {
            stack.push(s);
        }
        stack.flip();
        return stack;
    }

    /*
     * Constructor Tests ---------------------------------------------------
     */

    /**
     * Tests constructor.
     */
    @Test
    public final void TestConstructor() {
        Stack<String> s = this.constructorTest();
        Stack<String> sExpected = this.constructorRef();

        assertEquals(sExpected, s);
    }

    /*
     * Push Tests ----------------------------------------------------------
     */

    /**
     * Tests push() from an empty stack.
     */
    @Test
    public final void TestPushFromEmpty() {
        Stack<String> s = this.createFromArgsTest();
        Stack<String> sExpected = this.createFromArgsRef("seven");

        s.push("seven");

        assertEquals(sExpected, s);
    }

    /**
     * Tests push() from a non-empty stack.
     */
    @Test
    public final void TestPushFromNonEmpty() {
        Stack<String> s = this.createFromArgsTest("seven");
        Stack<String> sExpected = this.createFromArgsRef("six", "seven");

        s.push("six");

        assertEquals(sExpected, s);
    }
    /*
     * Pop Tests -----------------------------------------------------------
     */

    /**
     * Tests pop() making an empty stack.
     */
    @Test
    public final void TestPopToEmpty() {
        Stack<String> s = this.createFromArgsTest("seven");
        Stack<String> sExpected = this.createFromArgsRef();

        String str = s.pop();

        assertEquals(sExpected, s);
        assertEquals("seven", str);
    }

    /**
     * Tests pop() making a non-empty stack.
     */
    @Test
    public final void TestPopToNonEmpty() {
        Stack<String> s = this.createFromArgsTest("six", "seven");
        Stack<String> sExpected = this.createFromArgsRef("seven");

        String str = s.pop();

        assertEquals(sExpected, s);
        assertEquals("six", str);
    }
    /*
     * Length Tests --------------------------------------------------
     */

    /**
     * Tests length() on an empty stack.
     */
    @Test
    public final void TestLengthEmpty() {
        Stack<String> s = this.createFromArgsTest();

        int length = s.length();

        assertEquals(length, 0);
    }

    /**
     * Tests length() on a stack with a single element.
     */
    @Test
    public final void TestLengthSingle() {
        Stack<String> s = this.createFromArgsTest("seven");

        int length = s.length();

        assertEquals(length, 1);
    }

    /**
     * Tests length() on a stack with multiple elements.
     */
    @Test
    public final void TestLengthMultiple() {
        Stack<String> s = this.createFromArgsTest("six", "seven");

        int length = s.length();

        assertEquals(length, 2);
    }

}
