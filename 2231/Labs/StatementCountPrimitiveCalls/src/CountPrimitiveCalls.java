import components.statement.Statement;

/**
 * Utility class with method to count the number of calls to primitive
 * instructions (move, turnleft, turnright, infect, skip) in a given
 * {@code Statement}.
 *
 * @author Ryan Shaffer
 *
 */
public final class CountPrimitiveCalls {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private CountPrimitiveCalls() {
    }

    /**
     * Reports the number of calls to primitive instructions (move, turnleft,
     * turnright, infect, skip) in a given {@code Statement}.
     *
     * @param s
     *            the {@code Statement}
     * @return the number of calls to primitive instructions in {@code s}
     * @ensures <pre>
     * countOfPrimitiveCalls =
     *  [number of calls to primitive instructions in s]
     * </pre>
     */
    public static int countOfPrimitiveCalls(Statement s) {
        int count = 0;
        switch (s.kind()) {
            case BLOCK: {
                /*
                 * Add up the number of calls to primitive instructions in each
                 * nested statement in the BLOCK.
                 */
                int length = s.lengthOfBlock();
                for (int i = 0; i < length; i++) {
                    Statement subLabel = s.removeFromBlock(i);
                    count += countOfPrimitiveCalls(subLabel);
                    s.addToBlock(i, subLabel);
                }
                break;
            }
            case IF: {
                /*
                 * Find the number of calls to primitive instructions in the
                 * body of the IF.
                 */
                Statement subLabel = s.newInstance();
                Statement.Condition c = s.disassembleIf(subLabel);
                count = countOfPrimitiveCalls(subLabel);
                s.assembleIf(c, subLabel);
                break;
            }
            case IF_ELSE: {
                /*
                 * Add up the number of calls to primitive instructions in the
                 * "then" and "else" bodies of the IF_ELSE.
                 */
                Statement subLabelIf = s.newInstance();
                Statement subLabelElse = s.newInstance();

                Statement.Condition c = s.disassembleIfElse(subLabelIf,
                        subLabelElse);
                count = countOfPrimitiveCalls(subLabelIf)
                        + countOfPrimitiveCalls(subLabelElse);
                s.assembleIfElse(c, subLabelIf, subLabelElse);
                break;
            }
            case WHILE: {
                /*
                 * Find the number of calls to primitive instructions in the
                 * body of the WHILE.
                 */
                Statement subLabel = s.newInstance();
                Statement.Condition c = s.disassembleWhile(subLabel);
                count = countOfPrimitiveCalls(subLabel);
                s.assembleWhile(c, subLabel);
                break;
            }
            case CALL: {
                /*
                 * This is a leaf: the count can only be 1 or 0. Determine
                 * whether this is a call to a primitive instruction or not.
                 */
                String label = s.disassembleCall();
                if (label.equals("turnright") || label.equals("move")
                        || label.equals("infect") || label.equals("turnleft")
                        || label.equals("skip")) {
                    count++;
                }
                s.assembleCall(label);
                break;
            }
            default: {
                // this will never happen...can you explain why?
                break;
            }
        }
        return count;
    }

}
