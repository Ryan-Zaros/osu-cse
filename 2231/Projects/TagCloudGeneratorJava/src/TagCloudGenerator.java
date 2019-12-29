import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generates an HTML tag cloud using a user input file. Uses standard Java
 * components.
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
            implements Serializable, Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> i1,
                Map.Entry<String, Integer> i2) {
            /*
             * Compare integer values of map entry.
             */
            int a = i2.getValue().compareTo(i1.getValue());
            /*
             * Consistency with equals.
             */
            if (a == 0) {
                int b = i2.getKey().compareToIgnoreCase(i1.getKey());
                return b;
            }
            return a;
        }
    }

    /**
     * Sorts String keys of Map Pairs in alphabetical order.
     */
    private static class StringSort
            implements Serializable, Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> s1,
                Map.Entry<String, Integer> s2) {
            /*
             * Compare string values of map entry.
             */
            int a = s1.getKey().compareToIgnoreCase(s2.getKey());
            /*
             * Consistency with equals.
             */
            if (a == 0) {
                int b = s1.getValue().compareTo(s2.getValue());
                return b;
            }
            return a;
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
        Set<Character> separators = new HashSet<Character>();
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
            Set<Character> separators, BufferedReader input)
            throws IOException {
        /*
         * Clear existing map in case it's not empty.
         */
        countMap.clear();
        /*
         * Read through each line.
         */
        String line = input.readLine();
        while (line != null) {
            line = line.toLowerCase();
            int position = 0;
            while (position < line.length()) {
                /*
                 * Declared as "word" for simplicity, even though it could also
                 * be a separator.
                 */
                String word = nextWordOrSeparator(line, position, separators);
                if (!separators.contains(word.charAt(0))) {
                    if (!countMap.containsKey(word)) {
                        countMap.put(word, 1);
                    } else {
                        int newCount = countMap.get(word) + 1;
                        countMap.remove(word);
                        countMap.put(word, newCount);
                    }
                }
                /*
                 * Update position in line.
                 */
                position += word.length();
            }
            line = input.readLine();
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
        if (maxCount > minCount) {
            fontSize *= (count - minCount);
            fontSize /= (maxCount - minCount);
            fontSize += minFont;
        } else {
            fontSize = maxFont;
        }
        return "f" + fontSize;
    }

    /**
     * Fills an ArrayList full of the entries from countMap sorted by count.
     *
     * @clears countMap
     * @param countMap
     *            map of words and counts
     * @return a list of map pairs sorted numerically by count
     * @ensures countSorted (list) has all of the map pairs from the map
     *
     */
    private static List<Map.Entry<String, Integer>> sort1(
            Map<String, Integer> countMap) {
        /*
         * Create a list to add map pairs to, using an iterator.
         */
        List<Map.Entry<String, Integer>> countSorted;
        countSorted = new ArrayList<Map.Entry<String, Integer>>();
        Set<Map.Entry<String, Integer>> entrySet = countMap.entrySet();
        Iterator<Map.Entry<String, Integer>> it = entrySet.iterator();

        /*
         * Transfer entries from the map to the list.
         */
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            it.remove();
            countSorted.add(entry);
        }
        /*
         * Sort words numerically by count.
         */
        Comparator<Map.Entry<String, Integer>> numericalOrder = new IntegerSort();
        countSorted.sort(numericalOrder);
        return countSorted;
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
    private static void sort2(PrintWriter output, int n,
            Map<String, Integer> countMap) {
        /*
         * Sort words numerically by count.
         */
        List<Map.Entry<String, Integer>> countOrder = sort1(countMap);
        List<Map.Entry<String, Integer>> top;
        top = new ArrayList<Map.Entry<String, Integer>>();
        while (countOrder.size() > 0 && top.size() < n) {
            Map.Entry<String, Integer> topWord = countOrder.get(0);
            countOrder.remove(0);
            top.add(topWord);
        }
        /*
         * Minimum and maximum.
         */
        int highestCount = 1;
        int lowestCount = 1;
        if (top.size() > 0) {
            lowestCount = top.get(top.size() - 1).getValue();
            highestCount = top.get(0).getValue();
        }
        /*
         * Sort words alphabetically.
         */
        Comparator<Map.Entry<String, Integer>> alphabeticalOrder = new StringSort();
        top.sort(alphabeticalOrder);
        /*
         * Get font size for each word and output result.
         */
        for (Map.Entry<String, Integer> wordAndCount : top) {
            String fontSize = wordFontSize(highestCount, lowestCount,
                    wordAndCount.getValue());
            String tag = "<span style=\"cursor:default\" class=\"" + fontSize
                    + "\" title=\"count: " + wordAndCount.getValue() + "\">"
                    + wordAndCount.getKey() + "</span>";
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
    private static void outputHeader(PrintWriter output, String inputFile,
            int n) {
        assert output != null : "Violation of: output is not null.";
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
    private static void outputTagCloud(PrintWriter output, BufferedReader input,
            int n) {

        /*
         * Map to hold each word and its count.
         */
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        /*
         * Call methods to fill map and sort each word by its count and
         * alphabetically.
         */
        try {
            fillMap(countMap, generateSetofSeparators(SEPARATORS), input);
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        sort2(output, n, countMap);
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
    private static void outputFooter(PrintWriter output) {
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
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));
        /*
         * Input file.
         */
        String file = "";
        System.out.println("Enter the input file name: ");
        BufferedReader input = null;
        while (input == null) {
            try {
                file = in.readLine();
                input = new BufferedReader(new FileReader(file));
            } catch (IOException e) {
                System.out.println("Invalid file due to " + e);
            }
        }
        String inputFile = file;
        /*
         * Output file.
         */
        String outputFile = "";
        System.out.print("Enter the output file name: ");
        PrintWriter out = null;
        while (out == null) {
            try {
                outputFile = in.readLine();
                out = new PrintWriter(
                        new BufferedWriter(new FileWriter(outputFile)));
            } catch (IOException e) {
                System.out.println("Invalid file due to " + e);
            }
        }
        /*
         * User input to find n, number of words in tag cloud.
         */
        int n = 0;
        System.out.print("Enter the number of words to be in the tag cloud: ");
        try {
            n = Integer.parseInt(in.readLine());
        } catch (NumberFormatException e) {
            System.err.println("Error. Number is wrong format.");
        } catch (IOException e) {
            System.err.println("Error reading input.");
        }
        if (n < 0) {
            n = 0;
        }
        outputHeader(out, inputFile, n);
        /*
         * Call methods to generate tag cloud/HTML.
         */
        outputTagCloud(out, input, n);
        outputFooter(out);
        /*
         * Close readers and writers.
         */
        out.close();
        try {
            in.close();
        } catch (IOException e) {
            System.err.println("Error closing file.");
        }
    }
}
