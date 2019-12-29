import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine2;
import components.utilities.Reporter;

/**
 * Generates an HTML tag cloud using a user input file.
 *
 * @author Ryan Shaffer.555
 * @author Chris Tuttle.219
 *
 */
public final class TagCloudGenerator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloudGenerator() {
    }

    /**
     * Sorts Integer values of Map Pairs in decreasing order.
     */
    private static class IntegerSort
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o2.value().compareTo(o1.value());
        }
    }

    /**
     * Sorts String keys of Map Pairs in alphabetical order.
     */
    private static class StringSort
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o1.key().compareTo(o2.key());
        }
    }

    /**
     * String for the list of possible separators (except for ").
     */
    private static final String SEPARATORS = ". ,:;'{][}|/><?!`~1234567890"
            + "@#$%^&*()-_=+";

    /**
     * Generates the set of character separators from {@code String} (the
     * separators) into {@code Set}, along with an additional separator (").
     *
     * @param str
     *            the given {@code String}
     * @return set of characters consisting of chars in str + "
     * @ensures elements = chars in str + "
     *
     **/
    private static Set<Character> generateSetofSeparators(String str) {
        /*
         * Set to hold separators. Iterate through the string of separators and
         * add each unique one to the set.
         */
        Set<Character> separators = new Set1L<Character>();
        for (int i = 0; i < str.length(); i++) {
            if (!separators.contains(str.charAt(i))) {
                separators.add(str.charAt(i));
            }
        }
        /*
         * Due to string limitations, then add ".
         */
        separators.add('"');
        return separators;
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires <pre>
     * {@code 0 <= position < |text|}
     * </pre>
     * @ensures <pre>
     * {@code nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)}
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null.";
        assert separators != null : "Violation of: separators is not null.";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";
        /*
         * First, check if the char at the position is a separator.
         */
        boolean isSeparator = separators.contains(text.charAt(position));
        /*
         * Increase position until the next char is a separator.
         */
        int finalPosition = position + 1;
        while (finalPosition < text.length() && (separators
                .contains(text.charAt(finalPosition)) == isSeparator)) {
            finalPosition++;
        }
        /*
         * Return the word or separator.
         */
        return text.substring(position, finalPosition);
    }

    /**
     * Scans through the input file and adds words and their counts to a map.
     * Words are based off of given separators.
     *
     * @param countMap
     *            map that holds words and counts
     * @param input
     *            input file to be counted
     * @param separators
     *            set of characters that can separate words
     * @replaces countMap
     * @requires input is open
     * @ensures <pre>every key in the map is a word from input and each key
     *               has a value for how many times it appears in input
     *          </pre>
     */
    private static void fillMap(Map<String, Integer> countMap,
            Set<Character> separators, SimpleReader input) {
        /*
         * Clear existing map in case it's not empty.
         */
        countMap.clear();
        while (!input.atEOS()) {
            String line = input.nextLine().toLowerCase();
            int position = 0;
            while (position < line.length()) {
                /*
                 * Declared as "word" for simplicity, even though it could also
                 * be a separator.
                 */
                String word = nextWordOrSeparator(line, position, separators);
                if (!separators.contains(word.charAt(0))) {
                    if (!countMap.hasKey(word)) {
                        countMap.add(word, 1);
                    } else {
                        int newCount = countMap.value(word) + 1;
                        countMap.remove(word);
                        countMap.add(word, newCount);
                    }
                }
                /*
                 * Update position in line.
                 */
                position += word.length();
            }
        }
    }

    /**
     * Using an input range of font sizes, gives a word a font corresponding to
     * its count.
     *
     * @param maxCount
     *            maximum word count
     * @param minCount
     *            minimum word count
     * @param count
     *            count of the word that needs a font size
     * @requires maxCount =< count =< minCount, maxCount > 0, and minCount > 0
     * @ensures wordFontSize is a font size (given by CSS file) that varies
     *          based on its count relative to the scale/range
     * @return correct font size for word that appears "count" number of times
     */

    private static String wordFontSize(int maxCount, int minCount, int count) {
        /*
         * Initialize interval.
         */
        final int maxFont = 48;
        final int minFont = 11;
        /*
         * Find font size.
         */
        int fontSize = maxFont - minFont;
        fontSize *= (count - minCount);
        fontSize /= (maxCount - minCount);
        fontSize += minFont;
        return "f" + fontSize;
    }

    /**
     * Outputs HTML text for n words from countMap. Words printer will be those
     * that appear the most times and will be sorted alphabetically.
     *
     * @param output
     *            HTML file to print n words to
     * @param n
     *            number of highest-count words
     * @param countMap
     *            map of words and counts
     * @clears countMap
     * @ensures output content = #output content * n words from countMap
     */
    private static void doubleSort(SimpleWriter output, int n,
            Map<String, Integer> countMap) {
        /*
         * Sort words numerically by count.
         */
        Comparator<Pair<String, Integer>> countOrder = new IntegerSort();
        SortingMachine<Map.Pair<String, Integer>> countSort;
        countSort = new SortingMachine2<Map.Pair<String, Integer>>(countOrder);
        while (countMap.size() > 0) {
            countSort.add(countMap.removeAny());
        }
        countSort.changeToExtractionMode();
        /*
         * Set up to sort words alphabetically.
         */
        Comparator<Pair<String, Integer>> alphabeticalOrder = new StringSort();
        SortingMachine<Map.Pair<String, Integer>> letterSort;
        letterSort = new SortingMachine2<Map.Pair<String, Integer>>(
                alphabeticalOrder);
        /*
         * Retrieve largest count.
         */
        int highestCount = 0;
        if (countSort.size() > 0) {
            Map.Pair<String, Integer> maxPair = countSort.removeFirst();
            highestCount = maxPair.value();
            letterSort.add(maxPair);
        }
        /*
         * Transfer between sorts.
         */
        int topCounter = 0;
        while (topCounter < n && countSort.size() > 1) {
            Map.Pair<String, Integer> wordAndCount = countSort.removeFirst();
            letterSort.add(wordAndCount);
            topCounter++;
        }
        /*
         * Retrieve smallest count.
         */
        int lowestCount = 0;
        if (countSort.size() > 0) {
            Map.Pair<String, Integer> minPair = countSort.removeFirst();
            lowestCount = minPair.value();
            letterSort.add(minPair);
        }
        letterSort.changeToExtractionMode();
        /*
         * Get font size for each word and output result.
         */
        while (letterSort.size() > 0) {
            Map.Pair<String, Integer> wordAndCount = letterSort.removeFirst();
            String fontSize = wordFontSize(highestCount, lowestCount,
                    wordAndCount.value());
            String tag = "<span style=\"cursor:default\" class=\"" + fontSize
                    + "\" title=\"count: " + wordAndCount.value() + "\">"
                    + wordAndCount.key() + "</span>";
            output.println(tag);
        }
    }

    /**
     * Outputs opening tags for the HTML file.
     *
     * @param output
     *            output stream
     * @param inputFile
     *            name of the input file to read from
     * @param n
     *            number of words with highest counts
     * @updates {@code output}
     * @requires <pre>
     * {@code output is open and inputFile is not null}
     * </pre>
     * @ensures <pre>
     * {@code output content = #output content * tags}
     * </pre>
     */
    private static void outputHeader(SimpleWriter output, String inputFile,
            int n) {
        assert output != null : "Violation of: output is not null.";
        assert output.isOpen() : "Violation of: output is open.";
        assert inputFile != null : "Violation of: inputFile is not null.";
        /*
         * Output HTML opening tags/header.
         */
        output.println("<html><head><title>Top " + n + " words in " + inputFile
                + "</title>");
        output.println(
                "<link href=\"doc/tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        output.println("</head><body><h2>Top " + n + " words in " + inputFile
                + "</h2>");
        output.println("<hr>");
        output.println("<div class = \"cdiv\">");
        output.println("<p class = \"cbox\">");
    }

    /**
     * Outputs HTML tags for top numWords words from the file represented by
     * input to output, followed by closing HTML tags.
     *
     * @param output
     *            HTML file being printed to
     * @param input
     *            input file that will be read
     * @param n
     *            desired number of words
     * @requires n > 0 and input is open
     * @ensures input.content = #output.content * tags for top n words
     */
    private static void outputTagCloud(SimpleWriter output, SimpleReader input,
            int n) {

        /*
         * Map to hold each word and its count.
         */
        Map<String, Integer> countMap = new Map1L<String, Integer>();
        /*
         * Call methods to fill map and sort each word by its count and
         * alphabetically.
         */
        fillMap(countMap, generateSetofSeparators(SEPARATORS), input);
        doubleSort(output, n, countMap);
    }

    /**
     * Outputs closing tags for the HTML file.
     *
     * @param output
     *            output stream
     * @updates {@code output}
     * @requires <pre>
     * {@code output is open}
     * </pre>
     * @ensures <pre>
     * {@code output content = #output content * [the closing tags]}
     * </pre>
     */
    private static void outputFooter(SimpleWriter output) {
        /*
         * Output HTML closing tags/footer.
         */
        output.println("</p></div></body></html>");
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
         * Method arguments.
         */
        out.print("Enter the input file name: ");
        String inputFile = in.nextLine();
        out.print("Enter the output file name: ");
        String outputFile = in.nextLine();
        out.print("Enter the number of words to be in the tag cloud: ");
        int n = in.nextInteger();
        /*
         * Assert validity of n.
         */
        Reporter.assertElseFatalError(n > 0,
                "Number of words must be positive (n > 0).");
        /*
         * Writer for the HTML output file.
         */
        SimpleWriter output = new SimpleWriter1L(outputFile);
        outputHeader(output, inputFile, n);
        /*
         * Reader for processing the input file and generating tag cloud.
         */
        SimpleReader input = new SimpleReader1L(inputFile);
        /*
         * Call methods to generate tag cloud.
         */
        outputTagCloud(output, input, n);
        outputFooter(output);
        /*
         * Close readers and writers.
         */
        input.close();
        output.close();
        in.close();
        out.close();
    }
}
