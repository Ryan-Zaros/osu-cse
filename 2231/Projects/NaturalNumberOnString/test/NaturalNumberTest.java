import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;

/**
 * JUnit test fixture for {@code NaturalNumber}'s constructors and kernel
 * methods.
 *
 * @author shaffer.555, tuttle.219
 *
 */
public abstract class NaturalNumberTest {

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new number
     * @ensures constructorTest = 0
     */
    protected abstract NaturalNumber constructorTest();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorTest = i
     */
    protected abstract NaturalNumber constructorTest(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorTest)
     */
    protected abstract NaturalNumber constructorTest(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorTest = n
     */
    protected abstract NaturalNumber constructorTest(NaturalNumber n);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @return the new number
     * @ensures constructorRef = 0
     */
    protected abstract NaturalNumber constructorRef();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorRef = i
     */
    protected abstract NaturalNumber constructorRef(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorRef)
     */
    protected abstract NaturalNumber constructorRef(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorRef = n
     */
    protected abstract NaturalNumber constructorRef(NaturalNumber n);

    /**
     * Tests empty constructor.
     */
    @Test
    public void testConstructorEmpty() {
        // Create instances
        NaturalNumber n = this.constructorTest();
        NaturalNumber nExpected = this.constructorRef();
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Tests constructor with positive integer.
     */
    @Test
    public void testConstructorIntPositive() {
        // Create instances
        int i = 1;
        NaturalNumber n = this.constructorTest(i);
        NaturalNumber nExpected = this.constructorRef(i);
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Tests constructor with zero integer.
     */
    @Test
    public void testConstructorIntZero() {
        // Create instances
        int i = 0;
        NaturalNumber n = this.constructorTest(i);
        NaturalNumber nExpected = this.constructorRef(i);
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Tests constructor with max integer.
     */
    @Test
    public void testConstructorIntMax() {
        // Create instances
        int i = Integer.MAX_VALUE;
        NaturalNumber n = this.constructorTest(i);
        NaturalNumber nExpected = this.constructorRef(i);
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Tests constructor with string.
     */
    @Test
    public void testConstructorString() {
        // Create instances
        String s = "645";
        NaturalNumber n = this.constructorTest(s);
        NaturalNumber nExpected = this.constructorRef(s);
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Test constructor empty String.
     */
    @Test
    public void testConstructorStringEmpty() {
        // Create instances
        String s = "0";
        NaturalNumber n = this.constructorTest(s);
        NaturalNumber nExpected = this.constructorRef(s);
        // Assert equality
        assertEquals(n, nExpected);
    }

    /**
     * Tests constructor with natural number.
     */
    @Test
    public void testConstructorNN() {
        // Create instances
        String s = "123";
        NaturalNumber test = this.constructorTest(s);
        NaturalNumber test2 = this.constructorRef(s); //create a natural number

        NaturalNumber n = this.constructorTest(test);
        NaturalNumber nExpected = this.constructorRef(test2); //test NN constructor
        // Assert equality
        assertEquals(n, nExpected);
        assertEquals(test, test2);

    }

    /**
     * Tests constructor with natural number.
     */
    @Test
    public void testConstructorNNZero() {
        // Create instances
        String s = "0";
        NaturalNumber test = this.constructorTest(s); // Create a natural number
        NaturalNumber test2 = this.constructorRef(s);

        NaturalNumber n = this.constructorTest(test); //test NN constructor
        NaturalNumber nExpected = this.constructorRef(test2);
        // Assert equality
        assertEquals(n, nExpected);
        assertEquals(test, test2);

    }

    /**
     * Tests multiplyBy10 with empty rep.
     */
    @Test
    public void testMultiplyBy10Empty() {
        // Create instances
        NaturalNumber n = this.constructorTest();
        final int i = 5;
        NaturalNumber nExpected = this.constructorRef(i);
        // Call method
        n.multiplyBy10(i);
        // Assert equality
        assertEquals(nExpected, n);

    }

    /**
     * Tests multiplyBy10 with a routine case.
     */
    @Test
    public void testMultiplyBy10() {
        // Create instances
        NaturalNumber n = this.constructorTest(12);
        final int i = 6;
        NaturalNumber nExpected = this.constructorRef(126);
        // Call method
        n.multiplyBy10(i);
        // Assert equality
        assertEquals(nExpected, n);
    }

    /**
     * Tests divideBy10 with a digit < 10.
     */
    @Test
    public void testDivideBy10() {
        // Create instances
        NaturalNumber n = this.constructorTest(7);
        NaturalNumber nExpected1 = this.constructorRef(0);
        final int nExpected2 = 7;
        // Call method
        int n2 = n.divideBy10();
        // Assert equality
        assertEquals(nExpected1, n);
        assertEquals(nExpected2, n2);

    }

    /**
     * Tests divideBy10 with zero.
     */
    @Test
    public void testDivideBy10Zero() {
        // Create instances
        NaturalNumber n = this.constructorTest();
        NaturalNumber nExpected1 = this.constructorRef(0);
        final int nExpected2 = 0;
        // Call method
        int n2 = n.divideBy10();
        // Assert equality
        assertEquals(nExpected1, n);
        assertEquals(nExpected2, n2);

    }

    /**
     * Tests divideBy10 when remainder is zero.
     */
    @Test
    public void testDivideBy10EndZero() {
        // Create instances
        NaturalNumber n = this.constructorTest(350);
        NaturalNumber nExpected1 = this.constructorRef(35);
        final int nExpected2 = 0;
        // Call method
        int n2 = n.divideBy10();
        // Assert equality
        assertEquals(nExpected1, n);
        assertEquals(nExpected2, n2);

    }

    /**
     * Tests divideBy10 with a routine case.
     */
    @Test
    public void testDivideBy10EndRoutine() {
        // Create instances
        NaturalNumber n = this.constructorTest(456);
        NaturalNumber nExpected1 = this.constructorRef(45);
        final int nExpected2 = 6;
        // Call method
        int n2 = n.divideBy10();
        // Assert equality
        assertEquals(nExpected1, n);
        assertEquals(nExpected2, n2);

    }

    /**
     * Tests IsZero when true.
     */
    @Test
    public void testIsZeroTrue() {
        // Create instances
        NaturalNumber n = this.constructorTest();
        NaturalNumber nExpected = this.constructorRef();
        // Assert equality/call methods
        assertEquals(n, nExpected);
        assertTrue(n.isZero());
        assertTrue(nExpected.isZero());
    }

    /**
     * Tests IsZero when false.
     */
    @Test
    public void testIsZeroFalse() {
        // Create instances
        NaturalNumber n = this.constructorTest(6);
        NaturalNumber nExpected = this.constructorRef(6);
        // Assert equality/call methods
        assertEquals(n, nExpected);
        assertTrue(!n.isZero());
        assertTrue(!nExpected.isZero());
    }
}
