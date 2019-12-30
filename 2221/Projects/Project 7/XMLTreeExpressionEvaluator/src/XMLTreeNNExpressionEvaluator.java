import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
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
public final class XMLTreeNNExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeNNExpressionEvaluator() {
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
    private static NaturalNumber evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";
        assert exp
                .label() != "expression" : "Violation of: root label is not expression";

        // Hold top of tree
        NaturalNumber num1 = new NaturalNumber2();
        NaturalNumber num2 = new NaturalNumber2();

        // Look at children of tree, evaluate subtrees with operators first,
        // then do original operation.
        if (exp.numberOfChildren() > 0) {
            if (!exp.child(0).label().equalsIgnoreCase("NUMBER")
                    && !exp.child(1).label().equalsIgnoreCase("NUMBER")) {
                String s = evaluate(exp.child(0)).toString();
                num1.setFromString(s);
                s = evaluate(exp.child(1)).toString();
                num2.setFromString(s);
            } else if (!exp.child(0).label().equalsIgnoreCase("NUMBER")
                    && exp.child(1).label().equalsIgnoreCase("NUMBER")) {
                String s = evaluate(exp.child(0)).toString();
                num1.setFromString(s);
                String value = exp.child(1).attributeValue("value");
                num2.setFromString(value);
            } else {
                String value = exp.child(0).attributeValue("value");
                num1.setFromString(value);
                value = exp.child(1).attributeValue("value");
                num2.setFromString(value);
            }

            // Evaluate subtrees for operators
            if (exp.label().equalsIgnoreCase("PLUS")) {
                num1.add(num2);
            } else if (exp.label().equalsIgnoreCase("MINUS")) {
                if (num2.compareTo(num1) > 0) {
                    Reporter.fatalErrorToConsole(
                            "Cannot have num greater than this. Terminating.");
                }
                num1.subtract(num2);
            } else if (exp.label().equalsIgnoreCase("TIMES")) {
                num1.multiply(num2);
            } else if (exp.label().equalsIgnoreCase("DIVIDE")) {
                if (num2.isZero()) {
                    Reporter.fatalErrorToConsole(
                            "Cannot divide by 0. Terminating.");
                }
                num1.divide(num2);
            } else if (exp.label().equalsIgnoreCase("MOD")) {
                if (num2.isZero()) {
                    Reporter.fatalErrorToConsole(
                            "Cannot divide by 0. Terminating.");
                }
                NaturalNumber temp2 = num1.divide(num2);
                num1.copyFrom(temp2);
            } else if (exp.label().equalsIgnoreCase("POWER")) {
                num1.power(num2.toInt());
            } else if (exp.label().equalsIgnoreCase("ROOT")) {
                num1.root(num2.toInt());
            }
        }
        return num1;
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
