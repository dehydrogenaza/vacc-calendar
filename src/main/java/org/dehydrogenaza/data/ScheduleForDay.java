package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.InputValidator;
import org.dehydrogenaza.data.utils.TinyDate;

import java.util.List;

/**
 * A <strong>ScheduleForDay</strong> represents a mapping of "calendar dates" to lists of vaccines scheduled for
 * that day.
 */
public class ScheduleForDay {

    /**
     * Date as a YYYY-MM-DD <code>String</code>, which is the format used by web browsers for input fields.
     */
    private String dateISO;

    /**
     * Internal YYYY-MM-DD date field, which is <strong>bidirectionally bound to an HTML input field</strong> for
     * changing this ScheduleForDay's date. After changes are confirmed by the user, if <code>tempDate</code> is
     * empty, this <code>ScheduleForDay</code> object is removed from the calendar. Otherwise, its {@link #dateISO} and
     * {@link #dateInternal}
     * fields are updated to reflect the <code>tempDate</code>.
     */
    private String tempDate;

    /**
     * For convenience, the same date as a {@link TinyDate}.
     */
    private TinyDate dateInternal;

    /**
     * Vaccines scheduled for this date.
     */
    private final List<Dose> doses;


    /**
     * Constructs a <code>ScheduleForDay</code> given a properly formatted input date and a list of
     * {@link Dose}s.
     * @param   dateISO
     *          calendar date as YYYY-MM-DD.
     * @param   vaccines
     *          the list of vaccines to administer on that date.
     */
    public ScheduleForDay(String dateISO, List<Dose> vaccines) {
        this.dateISO = dateISO;
        this.tempDate = dateISO;
        //TODO: Defensive copying? think if this should be immutable
        this.doses = vaccines;

        dateInternal = new TinyDate(dateISO);
    }

    /**
     * Sets the internal calendar dates to the temporary field (which is bound to user input).
     */
    public void confirmTempValue() {
        dateISO = tempDate;
        dateInternal = new TinyDate(dateISO);
    }

    /**
     * Returns the date as a <code>String</code>.
     * @return
     *          the date in YYYY-MM-DD format.
     */
    public String getDate() {
        return dateISO;
    }

    /**
     * Returns the date in its numeric representation, YYYYMMDD, where the first four digits represent the year and
     * so on.
     * @return
     *          the date as a number.
     */
    public int getDateAsNumber() {
        return dateInternal.asNumber();
    }

    /**
     * Returns the <em>temporary</em> date (as a YYYY-MM-DD <code>String</code>), which is <strong>bidirectionally
     * bound</strong> with the value entered into the related HTML input field. It's <em>not</em> the actual date of
     * this Schedule (use {@link #getDate()} or {@link #getDateAsNumber()} for that instead).
     * @return
     *          the value synchronized with the associated HTML input field, representing this Schedule's date.
     */
    public String getTempDate() {
        return tempDate;
    }

    /**
     * <strong>Bidirectionally bound with an HTML input field.</strong>
     * @param   tempDate
     *          the date inputted by the user into the associated HTML input field.
     */
    public void setTempDate(String tempDate) {
        this.tempDate = tempDate;
    }

    /**
     * Getter for {@link #doses}.
     * @return
     *          a list of {@link VaccineType}s scheduled for this date.
     */
    public List<Dose> getDoses() {
        return doses;
    }

    /**
     * Schedules an additional {@link Dose} to this date. <strong>Mutates</strong> the internal list
     * {@link #doses}.
     * @param   dose
     *          a vaccination (single application/injection) to be added to the schedule for this day.
     */
    public void addDose(Dose dose) {
        doses.add(dose);
    }

    /**
     * Removes a specified {@link Dose} from this date. <strong>Mutates</strong> the internal list {@link #doses}.
     * @param   dose
     *          the vaccination (single application/injection) to be removed from the schedule for this day.
     */
    public void removeDose(Dose dose) {
        doses.remove(dose);
    }

    /**
     * Checks if the user entered a <strong>new, nonempty</strong> date for this ScheduleForDay.
     * <p>There's no guarantee that the value is within accepted range of dates (call {@link #isInBounds()}
     * separately if you need to verify that).</p>
     * @return
     *          <code>true</code> if the internal <code>tempDate</code> (bound to an HTML input field) is different
     *          from the actual date field and isn't empty; <code>false</code> otherwise.
     */
    public boolean isSetToNew() {
        return !dateISO.equals(tempDate) && !tempDate.isEmpty();
    }

    /**
     * Checks if the user marked this ScheduleForDay for removal, which is equivalent with entering nothing ("") in
     * its input field.
     * @return
     *          <code>true</code> if the internal <code>tempDate</code> (bound to an HTML input field) is empty;
     *          <code>false</code> otherwise.
     */
    public boolean isSetToRemove() {
        return tempDate.isEmpty();
    }

    /**
     * Uses {@link InputValidator} to verify that the {@link #tempDate} (directly taken from user input) is within
     * accepted bounds: between 1900-01-01 and 3000-12-31, and no earlier than the child's date of birth.
     * <p>This does not check for malformed input <code>Strings</code> (as they generally shouldn't happen without
     * explicit tampering; we're using a standard HTML <em>input type="date"</em> tag which should return a proper
     * YYYY-MM-DD value. And if the user decides to use the console to override this, that's his problem (we're
     * running everything client-side anyway).</p>
     * @return
     *          <code>true</code> if the {@link #tempDate} is within expected bounds.
     */
    public boolean isInBounds() {
        return InputValidator.validateBounds(tempDate);
    }

    /**
     * <strong>Bound to an HTML element.</strong> This controls visibility of the button that's used to
     * <em>Confirm</em> (submit) user input for this Schedule's new date.
     * @return
     *          <code>true</code> if the date {@link #isSetToNew()} and {@link #isInBounds()}. This is a convenience
     *          shortcut for calling both of the aforementioned methods.
     */
    public boolean isSetToConfirm() {
        return isSetToNew() && isInBounds();
    }
}
