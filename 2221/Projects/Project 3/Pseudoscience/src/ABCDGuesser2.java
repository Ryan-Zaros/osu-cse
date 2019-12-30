import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.FormatChecker;

/**
 * Uses de Jager formula to calculate 4 exponents that will give the closest
 * value to a user input.
 *
 * @author Ryan Shaffer
 *
 */
public final class ABCDGuesser2 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private ABCDGuesser2() {
    }

    /**
     * Repeatedly asks the user for a positive real number until the user enters
     * one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number entered by the user
     */
    private static double getPositiveDouble(SimpleReader in, SimpleWriter out) {
        String input;
        double check = 0.0;

        //asks for input until a real positive is entered. Converts to double
        while (check <= 0) {
            out.print("Enter a real positive number: ");
            input = in.nextLine();
            if (FormatChecker.canParseDouble(input)) {
                check = Double.parseDouble(input);
            }
        }
        return check;
    }

    /**
     * Repeatedly asks the user for a positive real number not equal to 1.0
     * until the user enters one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number not equal to 1.0 entered by the user
     */
    private static double getPositiveDoubleNotOne(SimpleReader in,
            SimpleWriter out) {
        String input;
        double check = 0.0;

        //asks for input until a real positive number (except 1) is entered.
        //converts to double
        while (check <= 0 || check == 1) {
            out.print("Enter your personal number: ");
            input = in.nextLine();
            if (FormatChecker.canParseDouble(input)) {
                check = Double.parseDouble(input);
            }
        }
        return check;
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

        //get user inputs
        double constant = getPositiveDouble(in, out);
        double w = getPositiveDoubleNotOne(in, out);
        double x = getPositiveDoubleNotOne(in, out);
        double y = getPositiveDoubleNotOne(in, out);
        double z = getPositiveDoubleNotOne(in, out);

        //initialize possible exponents
        final double[] exponents = { -5, -4, -3, -2, -1, -0.5, -1.0 / 3.0,
                -0.25, 0, 0.25, 1.0 / 3.0, 0.5, 1, 2, 3, 4, 5 };
        //initialize desired error and multiplier
        final double error = .01;
        final int multiplier = 100;
        //counters for array and loops
        int a = 0, b = 0, c = 0, d = 0;
        double e1, e2, e3, e4;

        //first attempt with user inputs
        double firstAttempt = Math.pow(w, exponents[a])
                * Math.pow(x, exponents[b]) * Math.pow(y, exponents[c])
                * Math.pow(z, exponents[d]);
        double loopAttempt;

        //loop through all combinations
        for (a = 0; a < exponents.length; a++) {
            for (b = 0; b < exponents.length; b++) {
                for (c = 0; c < exponents.length; c++) {
                    for (d = 0; d < exponents.length; d++) {
                        loopAttempt = Math.pow(w, exponents[a])
                                * Math.pow(x, exponents[b])
                                * Math.pow(y, exponents[c])
                                * Math.pow(z, exponents[d]);
                        //if better error, set attempt
                        if (Math.abs(loopAttempt - constant)
                                / constant <= Math.abs(firstAttempt - constant)
                                        / constant) {
                            firstAttempt = loopAttempt;
                            e1 = exponents[a];
                            e2 = exponents[b];
                            e3 = exponents[c];
                            e4 = exponents[d];
                        }
                        d++;
                    }
                    c++;
                }
                b++;
            }
            a++;
        }
        a = 0;

        //print all data
        out.println("Best exponents: " + a + " " + b + " " + c + " " + d);
        out.print("Percent error: ");
        out.print(firstAttempt * 100, 2, false);
        out.println("%");
        out.println("Value of formula: " + (Math.pow(w, a) * (Math.pow(x, b))
                * (Math.pow(y, c)) * (Math.pow(z, d))));
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
