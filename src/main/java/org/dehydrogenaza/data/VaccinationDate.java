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

    private String tempDate;

    /**
     * For convenience, the same date as a {@link TinyDate}.
     */
    private TinyDate dateInternal;

    /**
     * Vaccines scheduled for this date.
     */
    private final List<Vaccine> vaccinesScheduled;

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
        this.vaccinesScheduled = vaccines;
        this.tempDate = dateISO;

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
}
