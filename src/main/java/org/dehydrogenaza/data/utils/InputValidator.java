package org.dehydrogenaza.data.utils;

import org.dehydrogenaza.data.Form;

// TODO: Restrict the constructor to private, to prevent instantiation.

// TODO: Move format validation from the Form (.validateDates() method) to this class.

/**
 * This is a "static" utility class which determines if a date (taken from user input) is in supported range.
 */
public class InputValidator {
    /**
     * A reference to the input {@link Form}.
     */
    private static Form form;

    /**
     * Registers the input {@link Form}. This is necessary, because InputValidator must know the inputted
     * <i>dateOfBirth</i> (and potentially other things).
     * @param   form
     *          a reference to the input form.
     */
    public static void init(Form form) {
        InputValidator.form = form;
    }

    // TODO: Let this class work with TinyDate too.

    /**
     * Checks if the inputted date (provided as a YYYY-MM-DD <code>String</code>) falls within supported range.
     * <p>The range starts on <i>dateOfBirth</i> (taken from the {@link #form}), but no earlier than 1900-01-01; and
     * ends on 3000-12-31 (you're welcome, future mankind!).</p>
     * <p>Note that this method merely checks the <strong>values</strong> and says nothing about the format.</p>
     * @param   date
     *          a date tested if it falls within accepted range; assumed to be a valid YYYY-MM-DD <code>String</code>.
     * @return
     *          <code>true</code> if the date is no later than 3000-12-31 AND no earlier than the date of birth or
     *          1900-01-01 (whichever comes later); <code>false</code> otherwise.
     */
    public static boolean validateBounds(String date) {
        if (date.isEmpty()) {
            return true;
        }

        String[] dateMinimums = new String[]{"1900-01-01", form.getDateOfBirth()};
        String[] dateMaximums = new String[]{"3000-12-31"};
        int dateAsNumber = TinyDate.of(date).asNumber();

        for (String min : dateMinimums) {
            if (dateAsNumber < TinyDate.of(min).asNumber()) {
                return false;
            }
        }
        for (String max : dateMaximums) {
            if (dateAsNumber > TinyDate.of(max).asNumber()) {
                return false;
            }
        }
        return true;
    }
}
