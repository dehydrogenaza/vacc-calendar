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

    public String getTempDate() {
        return tempDate;
    }


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
     * Schedules an additional {@link VaccineType} to this date. <strong>Mutates</strong> the internal list
     * {@link #doses}.
     * @param   dose
     *          a vaccination to be added.
     */
    public void addDose(Dose dose) {
        doses.add(dose);
    }

    public void removeDose(Dose dose) {
        doses.remove(dose);
    }


    /**
     * Checks if the user entered a new <strong>nonempty, valid (<-- NOT YET IMPLEMENTED)</strong> date for this
     * ScheduleForDay.
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

    public boolean isInBounds() {
        return InputValidator.validateBounds(tempDate);
    }

    public boolean isSetToConfirm() {
        return isSetToNew() && isInBounds();
    }
}
