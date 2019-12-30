import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;

/**
 * JUnit test fixture for {@code BugsWorldVMInterpreter}'s
 * nextPrimitiveInstructionAddress static method.
 *
 * @author Paolo Bucci
 *
 */
public final class BugsWorldVMInterpreterTest {

    /**
     * The name of a file containing a valid compiled BL program.
     */
    private static final String FILE_NAME = "data/TestProgram.bo";

    /**
     * Loads a BL compiled program from the given file and returns an array
     * containing the compiled program.
     *
     * @param fileName
     *            the name of the input file
     * @return the compiled BL program loaded from the given file
     * @requires <pre>
     * [fileName is the name of a file containing a valid compiled BL program]
     * </pre>
     * @ensures loadProgram = [compiled BL program loaded from given file]
     */
    private static int[] loadProgram(String fileName) {
        int[] cp;
        SimpleReader file = new SimpleReader1L(fileName);
        int length = file.nextInteger();
        cp = new int[length];
        for (int i = 0; i < length; i++) {
            cp[i] = file.nextInteger();
        }
        file.close();
        return cp;
    }

    @Test
    public void test0Empty() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.EMPTY, 0);
        assertEquals(4, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test0Wall() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.WALL, 0);
        assertTrue(pc == 16 || pc == 19);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test0Friend() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.FRIEND, 0);
        assertEquals(22, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test0Enemy() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.ENEMY, 0);
        assertEquals(9, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test5Empty() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.EMPTY, 5);
        assertEquals(4, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test5Wall() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.WALL, 5);
        assertTrue(pc == 16 || pc == 19);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test5Friend() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.FRIEND, 5);
        assertEquals(22, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test5Enemy() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.ENEMY, 5);
        assertEquals(9, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test10Empty() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.EMPTY, 10);
        assertEquals(4, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test10Wall() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.WALL, 10);
        assertTrue(pc == 16 || pc == 19);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test10Friend() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.FRIEND, 10);
        assertEquals(22, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test10Enemy() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.ENEMY, 10);
        assertEquals(9, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test17Empty() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.EMPTY, 17);
        assertEquals(4, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test17Wall() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.WALL, 17);
        assertTrue(pc == 16 || pc == 19);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test17Friend() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.FRIEND, 17);
        assertEquals(22, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test17Enemy() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.ENEMY, 17);
        assertEquals(9, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test20Empty() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.EMPTY, 20);
        assertEquals(4, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test20Wall() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.WALL, 20);
        assertTrue(pc == 16 || pc == 19);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test20Friend() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.FRIEND, 20);
        assertEquals(22, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test20Enemy() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.ENEMY, 20);
        assertEquals(9, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test23Empty() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.EMPTY, 23);
        assertEquals(4, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test23Wall() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.WALL, 23);
        assertTrue(pc == 16 || pc == 19);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test23Friend() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.FRIEND, 23);
        assertEquals(22, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test23Enemy() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.ENEMY, 23);
        assertEquals(9, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test4Empty() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.EMPTY, 4);
        assertEquals(4, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test9Wall() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.WALL, 9);
        assertEquals(9, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test16Friend() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.FRIEND, 16);
        assertEquals(16, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test19Enemy() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.ENEMY, 19);
        assertEquals(19, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test22Empty() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.EMPTY, 22);
        assertEquals(22, pc);
        assertArrayEquals(cpExpected, cp);
    }

    @Test
    public void test25Wall() {
        int[] cp = loadProgram(FILE_NAME);
        int[] cpExpected = Arrays.copyOf(cp, cp.length);
        int pc = BugsWorldVMInterpreter.nextPrimitiveInstructionAddress(cp,
                BugsWorldVMInterpreter.CellState.WALL, 25);
        assertEquals(25, pc);
        assertArrayEquals(cpExpected, cp);
    }

}
