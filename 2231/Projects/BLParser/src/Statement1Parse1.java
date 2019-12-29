import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Ryan Shaffer.555
 * @author Chris Tuttle.219
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer
                .isCondition(c) : "Violation of: c is a condition string";

        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF") : ""
                + "Violation of: <\"IF\"> is proper prefix of tokens";
        /*
         * Dequeue kind and condition, check each.
         */
        String startKind = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isKeyword(startKind),
                "Invalid token");
        String condition = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(condition),
                "Invalid condition");
        Condition c = parseCondition(condition);
        Reporter.assertElseFatalError(tokens.dequeue().equals("THEN"),
                "Invalid token");
        /*
         * Parse "if" block. Parse "else," if available. Assemble respectively.
         */
        Statement s1 = s.newInstance();
        s1.parseBlock(tokens);
        if (tokens.front().equals("ELSE")) {
            Reporter.assertElseFatalError(tokens.dequeue().equals("ELSE"),
                    "Invalid token");
            Statement s2 = s.newInstance();
            s2.parseBlock(tokens);
            s.assembleIfElse(c, s1, s2);
        } else {
            s.assembleIf(c, s1);
        }
        /*
         * Dequeue end.
         */
        Reporter.assertElseFatalError(tokens.dequeue().equals("END"),
                "Invalid token");
        String endKind = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isKeyword(endKind),
                "Invalid token");
    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"WHILE"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE") : ""
                + "Violation of: <\"WHILE\"> is proper prefix of tokens";
        /*
         * Dequeue kind and condition, check each.
         */
        String startKind = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isKeyword(startKind),
                "Invalid token");
        String condition = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(condition),
                "Invalid token");
        Condition c = parseCondition(condition);
        Reporter.assertElseFatalError(tokens.dequeue().equals("DO"),
                "Invalid token");
        /*
         * Parse "while" block and assemble.
         */
        Statement s1 = s.newInstance();
        s1.parseBlock(tokens);
        s.assembleWhile(c, s1);
        /*
         * Dequeue end.
         */
        Reporter.assertElseFatalError(tokens.dequeue().equals("END"),
                "Invalid token");
        String endKind = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isKeyword(endKind),
                "Invalid token");

    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0
                && Tokenizer.isIdentifier(tokens.front()) : ""
                        + "Violation of: identifier string is proper prefix of tokens";
        /*
         * Dequeue the name and assemble the call.
         */
        String name = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(name),
                "Invalid token");
        s.assembleCall(name);
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";
        /*
         * Check that the front is either a keyword or an identifier.
         */
        String name = tokens.front();
        Reporter.assertElseFatalError(
                Tokenizer.isIdentifier(name) || Tokenizer.isKeyword(name),
                "Invalid token");
        /*
         * Parse according to which keyword/identifier it is.
         */
        if (name.equals("WHILE")) {
            parseWhile(tokens, this);
        } else if (name.equals("IF")) {
            parseIf(tokens, this);
        } else {
            parseCall(tokens, this);
        }
    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";
        /*
         * Parse until it reaches an end, else, or the EOI.
         */
        Statement s = this.newInstance();
        while (!tokens.front().equals("END")
                && !tokens.front().equals(Tokenizer.END_OF_INPUT)
                && !tokens.front().equals("ELSE")) {
            this.parse(tokens);
            s.addToBlock(s.lengthOfBlock(), this);
        }
        this.transferFrom(s);
    }

    /*
     * Main test method -------------------------------------------------------
     */

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
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
