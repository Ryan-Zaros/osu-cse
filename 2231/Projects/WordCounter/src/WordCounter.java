import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Program that counts word occurrences in a given input file and outputs an
 * HTML document with a table of the words and counts listed in alphabetical
 * order.
 *
 * In this, I will use maps to store each word and its count, queues in order to
 * alphabetize the words, and sets in order to create my desired set of
 * separators.
 *
 * @author Ryan Shaffer
 */

//THIS PROGRAM IS CASE-INSENSITIVE

public final class WordCounter {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private WordCounter() {
    }

    // NOTE: THIS METHOD IS TAKEN DIRECTLY FROM A 2221 LAB
    private static class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    // NOTE: This method is taken from 2221
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {

        // Check to see whether first position is a separator or not
        boolean separator = separators.contains(text.charAt(position));

        // This is the string that will be returned
        String str = "" + text.charAt(position);

        // This variable holds the value for whether or not the next character
        // is a separator.
        boolean whetherChar;

        int i = 1;
        // Checks to make sure the next position is still in string
        if (position + i < text.length()) {
            whetherChar = separators.contains(text.charAt(position + i));
        } else {
            // If the next position isn't in the string
            whetherChar = !separator;
        }

        // Once the next character isn't a separator, or the end of the string
        // has been reached
        while (whetherChar == separator) {
            str = str + text.charAt(position + i);
            i++;

            if (position + i < text.length()) {
                whetherChar = separators.contains(text.charAt(position + i));
            } else {
                whetherChar = !separator;
            }
        }
        return str;
    }

    public static void defineSeparators(Set<Character> s) {
        // Here we define our desired list of separators and add them to a set
        s.add(' ');
        s.add('.');
        s.add(',');
        s.add(':');
        s.add(';');
        s.add('?');
        s.add('!');
        s.add('"');
        s.add('(');
        s.add(')');
        s.add('-');
    }

    public static void newHeader(SimpleWriter output) {
        // Valid HTML document
        output.println("<!DOCTYPE html>");

        // Create headers for input file, format HTML page, table
        // I'm not completely sure if .name() is allowed here
        output.println(
                "<html>\n\t<head>\n\t<style>\n" + "table, th, td{\nborder: "
                        + "1px solid black;\n}th, " + "td \n\t</style>");

        // Title
        output.println("\n<title>" + "Words count"
        // Close tags
                + "</title>\n\t</head>\n\t<body>\n"
                // Heading
                + "\t<h2><b>Words counted in: " + output.name() + "</b></h2>\n"
                + "<table style='width:50%'>\n"
                // Column names
                + "<tr><th>Words<th>Counts");
    }

    public static void newFooter(SimpleWriter out) {
        // Create footers for each header
        out.print("\t\t</table>\n" + "\t</body>\n" + "</html>");
    }

    private static void newHTMLPage(Map<String, Integer> map,
            SimpleWriter output) {

        // This is where the comparator method from 2221 is used to alphabetize
        // all words
        Comparator<String> alphabetizedOrder = new StringLT();

        // Create new header
        newHeader(output);

        // Create queue to store all words
        Queue<String> allWords = new Queue1L<String>();
        // For-each loop to add every word to the queue using its map pair
        for (Pair<String, Integer> pair : map) {
            allWords.enqueue(pair.key());
        }

        // All words are sorted into alphabetized order. Bonus point for
        // how nicely this reads?
        allWords.sort(alphabetizedOrder);

        // For-each loop to output (in HTML) the count of each word
        for (String str : allWords) {
            if (map.hasKey(str)) {
                output.print("\t\t\t<tr>\n\t\t\t\t<td>" + str + "</td><td>"
                        + map.value(str) + "</td>\n\t\t\t</tr>\n");
            }
        }

        // Create new footer
        newFooter(output);
    }

    // The "main" method in the sense that it scans through the input file,
    // does the counting for each word and fills in the map pairs, then creates
    // the HTML page
    private static void count(SimpleReader input, SimpleWriter output) {

        // Initialize three variables. The first will change as we move through
        // the file, the second will change as we move through the lines, and
        // the third is the current position in each line
        String line;
        String wordOrSeparator;
        int position;

        // Creates a set of the separators
        Set1L<Character> separators = new Set1L<Character>();
        defineSeparators(separators);

        // Creates a map of every word and its corresponding count
        Map1L<String, Integer> words = new Map1L<String, Integer>();

        // While the file is not empty
        while (!input.atEOS()) {
            // Find next line and revert position back to 0
            line = input.nextLine();
            position = 0;

            while (position < line.length()) {
                // Finds next word
                wordOrSeparator = nextWordOrSeparator(line, position,
                        separators).toLowerCase();
                // Updates position to next word
                position = position + wordOrSeparator.length();

                // Check to see if it's a separator
                if (!separators.contains(wordOrSeparator.charAt(0))) {
                    // Increase the count if the word is already in the map
                    if (words.hasKey(wordOrSeparator)) {
                        words.replaceValue(wordOrSeparator,
                                words.value(wordOrSeparator) + 1);
                    } else {
                        // If not already in map, add to map with count of 1
                        words.add(wordOrSeparator, 1);
                    }
                }
            }
        }
        // Lastly, create the HTML page using the map
        newHTMLPage(words, output);
    }

    public static void main(String[] args) {
        SimpleReader input = new SimpleReader1L();
        SimpleWriter output = new SimpleWriter1L();

        // User enters input file name
        output.print("Enter input file: ");
        SimpleReader inputFile = new SimpleReader1L(input.nextLine());

        // User enters desired output file name
        output.print("Enter output file: ");
        SimpleWriter outputFile = new SimpleWriter1L(input.nextLine());

        // Method that counts all instances of words and outputs the data into
        // an HTML file. Also takes care of headers, footer, separators, etc.
        count(inputFile, outputFile);

        // Close all readers and writers
        inputFile.close();
        outputFile.close();
        input.close();
        output.close();
    }

}
