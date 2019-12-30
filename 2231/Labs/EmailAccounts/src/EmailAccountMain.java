import java.util.Iterator;

import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Simple program to exercise EmailAccount functionality.
 */
public final class EmailAccountMain {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private EmailAccountMain() {
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
//        SimpleReader in = new SimpleReader1L();
//        SimpleWriter out = new SimpleWriter1L();
//        EmailAccount myAccount = new EmailAccount1("Brutus", "Buckeye");
//        /*
//         * Should print: Brutus Buckeye
//         */
//        out.println(myAccount.name());
//        /*
//         * Should print: buckeye.1@osu.edu
//         */
//        out.println(myAccount.emailAddress());
//        /*
//         * Should print: Name: Brutus Buckeye, Email: buckeye.1@osu.edu
//         */
//        out.println(myAccount);
//        in.close();
//        out.close();
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        out.println("Enter full name: ");
        String fullName = in.nextLine();
        Queue<String> q = new Queue1L<String>();
        while (!fullName.equals("")) {
            boolean space = false;
            String firstName = "";
            String lastName = "";
            for (int i = 0; i < fullName.length() && !space; i++) {
                if (fullName.charAt(i) == ' ') {
                    space = true;
                }
                firstName = fullName.substring(0, i);
                lastName = fullName.substring(i + 1, fullName.length());
            }
            EmailAccount emailAccount = new EmailAccount1(firstName, lastName);
            String email = emailAccount.toString();
            q.enqueue(email);
            Iterator<String> it = q.iterator();
            while (it.hasNext()) {
                out.println(it.next());
            }
            out.println("Enter full name: ");
            fullName = in.nextLine();
        }
        in.close();
        out.close();
    }
}
