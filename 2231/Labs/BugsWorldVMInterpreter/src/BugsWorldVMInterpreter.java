import components.program.Program;
import components.program.Program.Instruction;
import components.program.Program1;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.FormatChecker;

/**
 * Program to test method to interpret a BugsWorld virtual machine program.
 *
 * @author Ryan Shaffer
 *
 */
public final class BugsWorldVMInterpreter {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * BugsWorld possible cell states.
     */
    enum CellState {
        EMPTY, WALL, FRIEND, ENEMY;
    }

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private BugsWorldVMInterpreter() {
    }

    /**
     * Gets a file name from the user and loads a BL compiled program from the
     * corresponding file and returns an array containing the compiled program.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return the compiled BL program loaded from a file specified by the user
     * @updates in.content
     * @updates out.content
     * @requires in.is_open and out.is_open
     * @ensures <pre>
     * [prompts the user to enter a file name, inputs it, and loads a
     *  compiled BL program from the corresponding file and returns the
     *  compiled program]
     * </pre>
     */
    private static int[] loadProgram(SimpleReader in, SimpleWriter out) {
        int[] cp;
        out.print("Enter compiled BL program file name: ");
        String fileName = in.nextLine();
        SimpleReader file = new SimpleReader1L(fileName);
        int length = file.nextInteger();
        cp = new int[length];
        for (int i = 0; i < length; i++) {
            cp[i] = file.nextInteger();
        }
        file.close();
        return cp;
    }

    /**
     * Returns whether the given integer is the byte code of a BugsWorld virtual
     * machine primitive instruction (MOVE, TURNLEFT, TURNRIGHT, INFECT, SKIP,
     * HALT).
     *
     * @param byteCode
     *            the integer to be checked
     * @return true if {@code byteCode} is the byte code of a primitive
     *         instruction or false otherwise
     * @ensures <pre>
     * isPrimitiveInstructionByteCode =
     *  [true iff byteCode is the byte code of a primitive instruction]
     * </pre>
     */
    private static boolean isPrimitiveInstructionByteCode(int byteCode) {
        return (byteCode == Instruction.MOVE.byteCode())
                || (byteCode == Instruction.TURNLEFT.byteCode())
                || (byteCode == Instruction.TURNRIGHT.byteCode())
                || (byteCode == Instruction.INFECT.byteCode())
                || (byteCode == Instruction.SKIP.byteCode())
                || (byteCode == Instruction.HALT.byteCode());
    }

    /**
     * Returns the value of the condition in the given conditional jump
     * {@code condJump} given what the bug sees {@code wbs}. Note that if
     * {@code condJump} is the byte code for the conditional jump
     * JUMP_IF_NOT_condition, the value returned is the value of the "condition"
     * part of the jump instruction.
     *
     * @param wbs
     *            the {@code CellState} indicating what the bug sees
     * @param condJump
     *            the byte code of a conditional jump
     * @return the value of the conditional jump condition
     * @requires [condJump is the byte code of a conditional jump]
     * @ensures <pre>
     * conditionalJumpCondition =
     *  [the value of the condition of condJump given what the bug sees wbs]
     * </pre>
     */
    private static boolean conditionalJumpCondition(CellState wbs,
            int condJump) {
        final double half = 0.5;
        boolean answer = true;
        if (condJump == Instruction.JUMP_IF_NOT_NEXT_IS_EMPTY.byteCode()) {
            answer = (wbs == CellState.EMPTY);
        } else if (condJump == Instruction.JUMP_IF_NOT_NEXT_IS_NOT_EMPTY
                .byteCode()) {
            answer = (wbs != CellState.EMPTY);
        } else if (condJump == Instruction.JUMP_IF_NOT_NEXT_IS_WALL
                .byteCode()) {
            answer = (wbs == CellState.WALL);
        } else if (condJump == Instruction.JUMP_IF_NOT_NEXT_IS_NOT_WALL
                .byteCode()) {
            answer = (wbs != CellState.WALL);
        } else if (condJump == Instruction.JUMP_IF_NOT_NEXT_IS_FRIEND
                .byteCode()) {
            answer = (wbs == CellState.FRIEND);
        } else if (condJump == Instruction.JUMP_IF_NOT_NEXT_IS_NOT_FRIEND
                .byteCode()) {
            answer = (wbs != CellState.FRIEND);
        } else if (condJump == Instruction.JUMP_IF_NOT_NEXT_IS_ENEMY
                .byteCode()) {
            answer = (wbs == CellState.ENEMY);
        } else if (condJump == Instruction.JUMP_IF_NOT_NEXT_IS_NOT_ENEMY
                .byteCode()) {
            answer = (wbs != CellState.ENEMY);
        } else if (condJump == Instruction.JUMP_IF_NOT_RANDOM.byteCode()) {
            answer = (Math.random() < half);
        } else if (condJump == Instruction.JUMP_IF_NOT_TRUE.byteCode()) {
            answer = true;
        } else {
            assert false : "Violation of: condJump is a conditional jump byte code";
        }
        return answer;
    }

    /**
     * Checks whether the given location {@code loc} is the location of an
     * instruction byte code in the given program {@code cp}.
     *
     * @param cp
     *            the compiler program
     * @param loc
     *            the location to check
     * @return true iff {@code loc} is the address of an instruction byte code
     * @requires [cp is a valid compiled BL program]
     * @ensures <pre>
     * isValidInstructionLocation =
     *  [true iff loc is the address of an instruction byte code in cp]
     * </pre>
     */
    private static boolean isValidInstructionLocation(int[] cp, int loc) {
        boolean found = false;
        int pos = 0;
        while ((pos < cp.length) && !found) {
            if (pos == loc) {
                found = true;
            } else {
                if (!isPrimitiveInstructionByteCode(cp[pos])) {
                    /*
                     * It must be a jump instruction, increment pos one extra
                     * time
                     */
                    pos++;
                }
                pos++;
            }
        }
        return found;
    }

    /*
     * Public members ---------------------------------------------------------
     */

    /**
     * Returns the location of the next primitive instruction to execute in
     * compiled program {@code cp} given what the bug sees {@code wbs} and
     * starting from location {@code pc}.
     *
     * @param cp
     *            the compiled program
     * @param wbs
     *            the {@code CellState} indicating what the bug sees
     * @param pc
     *            the program counter
     * @return the location of the next primitive instruction to execute
     * @requires <pre>
     * [cp is a valid compiled BL program]  and
     * 0 <= pc < cp.length  and
     * [pc is the location of an instruction byte code in cp, that is, pc
     *  cannot be the location of an address]
     * </pre>
     * @ensures <pre>
     * [return the address of the next primitive instruction that
     *  should be executed in program cp given what the bug sees wbs and
     *  starting execution at address pc in program cp]
     * </pre>
     */
    public static int nextPrimitiveInstructionAddress(int[] cp, CellState wbs,
            int pc) {
        assert cp != null : "Violation of: cp is not null";
        assert wbs != null : "Violation of: wbs is not null";
        assert cp.length > 0 : "Violation of: cp is a valid compiled BL program";
        assert 0 <= pc : "Violation of: 0 <= pc";
        assert pc < cp.length : "Violation of: pc < cp.length";
        assert isValidInstructionLocation(cp, pc) : ""
                + "Violation of: pc is the location of an instruction byte code in cp";
        int end = 0;
        if (isPrimitiveInstructionByteCode(cp[pc])) {
            end = pc;
        } else {
            if (cp[pc] == Instruction.JUMP.byteCode()) {
                end = nextPrimitiveInstructionAddress(cp, wbs, cp[pc + 1]);
            } else if (conditionalJumpCondition(wbs, cp[pc])) {
                end = nextPrimitiveInstructionAddress(cp, wbs, pc + 2);
            } else {
                end = nextPrimitiveInstructionAddress(cp, wbs, cp[pc + 1]);
            }
        }
        return end;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Load compiled BL program
         */
        int[] cp = loadProgram(in, out);

        int pc = 0;
        out.println();
        out.println("Enter program counter outside the [0," + cp.length
                + ") range to quit.");
        /*
         * Output disassembled program with marked address
         */
        out.println();
        Program1.disassembleProgram(out, cp, pc);
        while (true) {
            /*
             * Input new program counter
             */
            out.println();
            out.print("Enter program counter (Enter => pc = " + pc + "): ");
            String input = in.nextLine();
            if (input.length() > 0 && !FormatChecker.canParseInt(input)) {
                out.println("Program counter must be a number in the [0,"
                        + cp.length + ") range");
                continue;
            } else if (FormatChecker.canParseInt(input)) {
                int pcCandidate = Integer.parseInt(input);
                if (pcCandidate < 0 || pcCandidate >= cp.length) {
                    break;
                } else if (!isValidInstructionLocation(cp, pcCandidate)) {
                    out.println("Program counter must be the location of an "
                            + "instruction byte code in the program");
                    continue;
                } else {
                    pc = pcCandidate;
                }
            }
            /*
             * Input what bug sees and convert to CellState value
             */
            out.print("Enter what bug sees "
                    + "(EMPTY=0, WALL=1, FRIEND=2, ENEMY=3): ");
            String wbs = in.nextLine();
            CellState cs;
            switch (wbs) {
                case "0": {
                    cs = CellState.EMPTY;
                    break;
                }
                case "1": {
                    cs = CellState.WALL;
                    break;
                }
                case "2": {
                    cs = CellState.FRIEND;
                    break;
                }
                case "3": {
                    cs = CellState.ENEMY;
                    break;
                }
                default: {
                    out.println("What bug sees must be a number"
                            + " in the [0,3] range");
                    continue;
                }
            }
            /*
             * Interpret program to find next primitive instruction
             */
            pc = nextPrimitiveInstructionAddress(cp, cs, pc);
            out.println();
            out.println("  Next primitive instruction: "
                    + Program.Instruction.values()[cp[pc]].toString()
                    + " at address " + pc);
            /*
             * Output disassembled program with marked address
             */
            out.println();
            Program1.disassembleProgram(out, cp, pc);
            /*
             * Increment program counter pc to make progress
             */
            pc++;
        }
        out.println("Goodbye!");

        in.close();
        out.close();
    }

}
