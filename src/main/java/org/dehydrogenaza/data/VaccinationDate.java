package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.TinyDate;

import java.util.List;

//TODO: Maybe rename to VaccinationSchedule or sth
/**
 * A <strong>VaccinationDate</strong> represents a mapping of "calendar dates" to lists of vaccines scheduled for
 * that day.
 */
public class VaccinationDate {

    /**
     * Date as a YYYY-MM-DD <code>String</code>, which is the format used by web browsers for input fields.
     */
    private String dateISO;

    /**
     * Internal YYYY-MM-DD date field, which is <strong>bidirectionally bound to an HTML input field</strong> for
     * changing this VaccinationDate's date. After changes are confirmed by the user, if <code>tempDate</code> is
     * empty, this object is removed from the calendar. Otherwise, its {@link #dateISO} and {@link #dateInternal}
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
    private final List<Vaccine> vaccinesScheduled;

    /**
     * Whether this object is marked for removal from {@link VaccinationCalendar}s. <strong>Probably a
     * temporary solution.</strong>
     */
    public boolean flaggedForRemoval = false;


    /**
     * Constructs a <code>VaccinationDate</code> given a properly formatted input date and a list of
     * {@link Vaccine}s.
     * @param   dateISO
     *          calendar date as YYYY-MM-DD.
     * @param   vaccines
     *          the list of vaccines to administer on that date.
     */
    public VaccinationDate(String dateISO, List<Vaccine> vaccines) {
        this.dateISO = dateISO;
        this.tempDate = dateISO;
        //TODO: Defensive copying? think if this should be immutable
        this.vaccinesScheduled = vaccines;

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

    public void update() {
        dateISO = tempDate;

        dateInternal = new TinyDate(dateISO);
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
     * Getter for {@link #getVaccines()}.
     * @return
     *          a list of {@link Vaccine}s scheduled for this date.
     */
    public List<Vaccine> getVaccines() {
        return vaccinesScheduled;
    }

    /**
     * Schedules an additional {@link Vaccine} to this date. <strong>Mutates</strong> the internal list
     * {@link #vaccinesScheduled}.
     * @param   vaccine
     *          a vaccine to be added.
     */
    public void addVaccine(Vaccine vaccine) {
        vaccinesScheduled.add(vaccine);
    }


    /**
     * Checks if the user entered a new <strong>nonempty, valid (<-- NOT YET IMPLEMENTED)</strong> date for this
     * VaccinationDate.
     * @return
     *          <code>true</code> if the internal <code>tempDate</code> (bound to an HTML input field) is different
     *          from the actual date field and isn't empty; <code>false</code> otherwise.
     */
    public boolean isSetToNew() {
        //TODO: Add full validation
        return !dateISO.equals(tempDate) && !tempDate.isEmpty();
    }


    /**
     * Checks if the user marked this VaccinationDate for removal, which is equivalent with entering nothing ("") in
     * its input field.
     * @return
     *          <code>true</code> if the internal <code>tempDate</code> (bound to an HTML input field) is empty;
     *          <code>false</code> otherwise.
     */
    public boolean isSetToRemove() {
        return tempDate.isEmpty();
    }
}
