import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Program to copy a text file into another file.
 *
 * @author Ryan Shaffer
 *
 */
public final class FilterFileStdJava {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private FilterFileStdJava() {
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments: input-file-name output-file-name
     */
    public static void main(String[] args) {
        String inFile = args[0];
        String outFile = args[1];
        String filter = args[2];
        Set<String> set = new HashSet<String>();
        BufferedReader input;
        BufferedReader inputFilter;
        PrintWriter output;
        try {
            input = new BufferedReader(new FileReader(inFile));
            inputFilter = new BufferedReader(new FileReader(filter));
            output = new PrintWriter(
                    new BufferedWriter(new FileWriter(outFile)));
        } catch (IOException e) {
            System.err.println("Error opening file.");
            return;
        }
        try {
            String s = input.readLine();
            String addLine = inputFilter.readLine();
            while (addLine != null) {
                set.add(addLine);
                addLine = inputFilter.readLine();
            }
            while (s != null) {
                boolean contains = false;
                Iterator<String> it = set.iterator();
                while (it.hasNext() && !contains) {
                    if (s.contains(it.next())) {
                        contains = true;
                        output.println(s);
                        s = input.readLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing to file.");
        }
        try {
            inputFilter.close();
            input.close();
            output.close();
        } catch (IOException e) {
            System.err.println("Error closing file.");
        }
    }
}
