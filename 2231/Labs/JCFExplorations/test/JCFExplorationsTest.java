import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber1L;
import components.utilities.FormatChecker;

/**
 * JUnit test fixture for {@code JCFExplorations}'s static methods.
 *
 * @author Ryan Shaffer
 *
 */
public class JCFExplorationsTest {

    /**
     * Invokes the {@code components.map.Map4} constructor and returns the
     * result.
     *
     * @return the new map
     * @ensures mapConstructorOSU = {}
     */
    private components.map.Map<String, Integer> mapConstructorOSU() {
        return new components.map.Map4<String, Integer>();
    }

    /**
     * Creates and returns a {@code components.map.Map<String, Integer>} with
     * the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]  and
     * [the 'value' entries in args are numbers]
     * </pre>
     * @ensures createFromArgs = [pairs in args]
     */
    private components.map.Map<String, Integer> mapCreateFromArgsOSU(
            String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        components.map.Map<String, Integer> map = this.mapConstructorOSU();
        for (int i = 0; i < args.length; i += 2) {
            assert FormatChecker.canParseInt(args[i + 1]) : ""
                    + "Violation of: the 'value' entries in args are numbers";
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], Integer.parseInt(args[i + 1]));
        }
        return map;
    }

    /**
     * Invokes the {@code java.util.HashMap} constructor and returns the result.
     *
     * @return the new map
     * @ensures mapConstructorJCF = {}
     */
    private java.util.Map<String, Integer> mapConstructorJCF() {
        return new java.util.HashMap<String, Integer>();
    }

    /**
     * Creates and returns a {@code java.util.Map<String, Integer>} with the
     * given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]  and
     * [the 'value' entries in args are numbers]
     * </pre>
     * @ensures createFromArgs = [pairs in args]
     */
    private java.util.Map<String, Integer> mapCreateFromArgsJCF(
            String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        java.util.Map<String, Integer> map = this.mapConstructorJCF();
        for (int i = 0; i < args.length; i += 2) {
            assert FormatChecker.canParseInt(args[i + 1]) : ""
                    + "Violation of: the 'value' entries in args are numbers";
            assert !map.containsKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.put(args[i], Integer.parseInt(args[i + 1]));
        }
        return map;
    }

    /**
     * Invokes the {@code components.naturalnumber.NaturalNumber1L(String)}
     * constructor and returns the result.
     *
     * @param s
     *            the string value of the new natural number
     * @return the new natural number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures nnConstructorOSU = [number with value s]
     */
    private NaturalNumber nnConstructor(String s) {
        return new NaturalNumber1L(s);
    }

    /**
     * Invokes the {@code components.set.Set4} constructor and returns the
     * result.
     *
     * @return the new set
     * @ensures setConstructorOSU = {}
     */
    private components.set.Set<NaturalNumber> setConstructorOSU() {
        return new components.set.Set4<NaturalNumber>();
    }

    /**
     * Creates and returns a {@code components.set.Set<NaturalNumber>} with the
     * given entries.
     *
     * @param args
     *            the string values of the natural numbers for the set
     * @return the constructed set
     * @requires <pre>
     * [every entry in args is unique]  and
     * [every entry in args is a natural number]
     * </pre>
     * @ensures createFromArgs = {[numbers in args]}
     */
    private components.set.Set<NaturalNumber> setCreateFromArgsOSU(
            String... args) {
        components.set.Set<NaturalNumber> set = this.setConstructorOSU();
        for (int i = 0; i < args.length; i++) {
            assert args[i].matches("0|[1-9]\\d*") : ""
                    + "Violation of: every entry in args is a natural number";
            NaturalNumber n = this.nnConstructor(args[i]);
            assert !set.contains(
                    n) : "Violation of: every entry in args is unique";
            set.add(n);
        }
        return set;
    }

    /**
     * Invokes the {@code java.util.HashSet} constructor and returns the result.
     *
     * @return the new set
     * @ensures setConstructorJCF = {}
     */
    private java.util.Set<NaturalNumber> setConstructorJCF() {
        return new java.util.HashSet<NaturalNumber>();
    }

    /**
     * Creates and returns a {@code java.util.Set<NaturalNumber>} with the given
     * entries.
     *
     * @param args
     *            the string values of the natural numbers for the set
     * @return the constructed set
     * @requires <pre>
     * [every entry in args is unique]  and
     * [every entry in args is a natural number]
     * </pre>
     * @ensures createFromArgs = {[numbers in args]}
     */
    private java.util.Set<NaturalNumber> setCreateFromArgsJCF(String... args) {
        java.util.Set<NaturalNumber> set = this.setConstructorJCF();
        for (int i = 0; i < args.length; i++) {
            assert args[i].matches("0|[1-9]\\d*") : ""
                    + "Violation of: every entry in args is a natural number";
            NaturalNumber n = this.nnConstructor(args[i]);
            assert !set.contains(
                    n) : "Violation of: every entry in args is unique";
            set.add(n);
        }
        return set;
    }

    @Test
    public final void testGiveRaiseOSUExample() {
        /* Setup */
        components.map.Map<String, Integer> m = this.mapCreateFromArgsOSU(
                "stark", "30000", "lannister", "100000", "snow", "40000");
        components.map.Map<String, Integer> mExpected = this
                .mapCreateFromArgsOSU("lannister", "100000", "stark", "33000",
                        "snow", "44000");
        /* The Call */
        JCFExplorations.giveRaise(m, 's', 10);
        /* Evaluation */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testGiveRaiseJCFExample() {
        /* Setup */
        java.util.Map<String, Integer> m = this.mapCreateFromArgsJCF("stark",
                "30000", "lannister", "100000", "snow", "40000");
        java.util.Map<String, Integer> mExpected = this.mapCreateFromArgsJCF(
                "lannister", "100000", "stark", "33000", "snow", "44000");
        /* The Call */
        JCFExplorations.giveRaise(m, 's', 10);
        /* Evaluation */
        assertEquals(mExpected, m);
    }

    // TODO - add other test cases for both versions of giveRaise

    @Test
    public final void testIncrementAllOSUExample1() {
        /* Setup */
        components.set.Set<NaturalNumber> s = this.setCreateFromArgsOSU("7",
                "3", "31", "127");
        components.set.Set<NaturalNumber> sExpected = this
                .setCreateFromArgsOSU("128", "4", "32", "8");
        /* The Call */
        JCFExplorations.incrementAll(s);
        /* Evaluation */
        assertEquals(sExpected, s);
    }

    @Test
    public final void testIncrementAllOSUExample2() {
        /* Setup */
        components.set.Set<NaturalNumber> s = this.setCreateFromArgsOSU("7",
                "3", "31", "127");
        components.set.Set<NaturalNumber> sExpected = this
                .setCreateFromArgsOSU("128", "4", "32", "8");
        /* The Call */
        JCFExplorations.incrementAll(s);
        /* Evaluation */
        assertEquals("Incremented set's size: ", sExpected.size(), s.size());
        for (NaturalNumber n : sExpected) {
            assertTrue("Incremented set fails to contain " + n + ".",
                    s.contains(n));
        }
    }

    @Test
    public final void testIncrementAllJCFExample1() {
        /* Setup */
        java.util.Set<NaturalNumber> s = this.setCreateFromArgsJCF("7", "3",
                "31", "127");
        java.util.Set<NaturalNumber> sExpected = this
                .setCreateFromArgsJCF("128", "4", "32", "8");
        /* The Call */
        JCFExplorations.incrementAll(s);
        /* Evaluation */
        assertEquals(sExpected, s);
    }

    @Test
    public final void testIncrementAllJCFExample2() {
        /* Setup */
        java.util.Set<NaturalNumber> s = this.setCreateFromArgsJCF("7", "3",
                "31", "127");
        java.util.Set<NaturalNumber> sExpected = this
                .setCreateFromArgsJCF("128", "4", "32", "8");
        /* The Call */
        JCFExplorations.incrementAll(s);
        /* Evaluation */
        assertEquals("Incremented set's size: ", sExpected.size(), s.size());
        for (NaturalNumber n : sExpected) {
            assertTrue("Incremented set fails to contain " + n + ".",
                    s.contains(n));
        }
    }

    // TODO - add other test cases for both versions of incrementAll

}
