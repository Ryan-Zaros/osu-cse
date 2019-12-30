import components.map.Map;
import components.map.Map1L;

/**
 * Implementation of {@code EmailAccount}.
 *
 * @author Ryan Shaffer
 *
 */
public final class EmailAccount1 implements EmailAccount {

    /*
     * Private members --------------------------------------------------------
     */

    /*
     * String to hold the first name.
     */
    private String firstName;

    /*
     * String to store the last name.
     */
    private String lastName;

    /*
     * String to store the email address.
     */
    private String email;

    /*
     * Map to store last names and their respective values.
     */
    private static Map<String, Integer> dotNumber = new Map1L<String, Integer>();

    /*
     * Constructor ------------------------------------------------------------
     */

    /**
     * Constructor.
     *
     * @param firstName
     *            the first name
     * @param lastName
     *            the last name
     */
    public EmailAccount1(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        String lastNameIgnoreCase = lastName.toLowerCase();
        /*
         * Add if not in map. Else, increase key value by 1.
         */
        if (!dotNumber.hasKey(lastNameIgnoreCase)) {
            dotNumber.add(lastNameIgnoreCase, 1);
        } else {
            int n = dotNumber.value(lastNameIgnoreCase);
            dotNumber.replaceValue(lastNameIgnoreCase, n++);
        }
        int dotValue = dotNumber.value(lastNameIgnoreCase);
        this.email = lastNameIgnoreCase + "." + Integer.toString(dotValue)
                + "@osu.edu";
    }

    /*
     * Methods ----------------------------------------------------------------
     */

    @Override
    public String name() {
        String fullName = this.firstName + " " + this.lastName;
        return fullName;
    }

    @Override
    public String emailAddress() {
        return this.email;
    }

    @Override
    public String toString() {
        String name = "Name: " + this.firstName + " " + this.lastName;
        String email = "Email: " + this.email;
        return name + ", " + email;
    }
}
