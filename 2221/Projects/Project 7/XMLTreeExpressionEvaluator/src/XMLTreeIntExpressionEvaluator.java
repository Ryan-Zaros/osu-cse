import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author Ryan Shaffer
 *
 */
public final class XMLTreeIntExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeIntExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static int evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        // Array to hold first nodes
        int[] children = new int[exp.numberOfChildren()];
        int number = 0;

        // Look at children of tree, evaluate subtrees with operators first,
        // then do original operation.
        if (exp.numberOfChildren() > 0) {
            int t = 0;
            for (int i = 0; i < exp.numberOfChildren(); i++) {
                if (!exp.child(i).label().equalsIgnoreCase("NUMBER")) {
                    children[t] = evaluate(exp.child(i));
                } else {
                    String value = exp.child(i).attributeValue("value");
                    children[t] = Integer.parseInt(value);
                }
                t++;
            }

            // Evaluate subtrees for operators
            if (exp.label().equalsIgnoreCase("PLUS")) {
                number = children[0] + children[1];
            } else if (exp.label().equalsIgnoreCase("MINUS")) {
                number = children[0] - children[1];
            } else if (exp.label().equalsIgnoreCase("TIMES")) {
                number = children[0] * children[1];
            } else if (exp.label().equalsIgnoreCase("DIVIDE")) {
                if (children[1] == 0) {
                    Reporter.fatalErrorToConsole(
                            "Cannot divide by 0. Terminating.");
                }
                number = children[0] / children[1];
            } else if (exp.label().equalsIgnoreCase("MOD")) {
                if (children[1] == 0) {
                    Reporter.fatalErrorToConsole(
                            "Cannot divide by 0. Terminating.");
                }
                number = children[0] % children[1];
            } else if (exp.label().equalsIgnoreCase("POWER")) {
                number = (int) Math.pow(children[0], children[1]);
            } else if (exp.label().equalsIgnoreCase("ROOT")) {
                number = (int) Math.pow(children[0], 1.0 / children[1]);
            }
        }
        return number;
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

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
