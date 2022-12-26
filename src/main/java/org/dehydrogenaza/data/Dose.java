package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.InputValidator;
import org.dehydrogenaza.data.utils.TinyDate;

/**
 * <strong>Dose</strong> objects are used to represent individual administrations of a vaccine of a certain
 * {@link VaccineType}.
 * <p>A single VaccineType may have any number of doses at different dates, and with different
 * "variant" names (in cases where it's not worth it or necessary to distinguish related, but not identical
 * medications, such as for example with DTP vaccination, which consists 4 injections of DTP followed by DTaP, Tdap
 * and Td - the difference between which is not that relevant to the parent).</p>
 */
public class Dose {
    /**
     * The {@link VaccineType}.
     */
    private final VaccineType type;
    /**
     * The calendar date scheduled for this Dose.
     */
    private final TinyDate date;
    /**
     * The display name used for this dose. Useful in more complex vaccinations, which involve a number of slightly
     * different medications. This allows us to display the accurate name without having to treat the variants as
     * brand new {@link VaccineType}s. If this parameter is not provided during construction, the default name will
     * be taken from the VaccineType object itself.
     */
    private final String altName;

    /**
     * Internal YYYY-MM-DD date field, which is <strong>bidirectionally bound to an HTML input field</strong> (in
     * Section 3) for changing this Dose's date. After changes are confirmed by the user, if <code>tempDate</code> is
     * empty, this <code>Dose</code> object is removed from the calendar. Otherwise, its {@link #date} field is updated
     * to reflect the <code>tempDate</code>.
     */
    private String tempDate;

    /**
     * Constructs a Dose with no alternate display name (variant name). The default name of the associated
     * {@link VaccineType} will be used instead.
     * @param   type
     *          the vaccination that this is a dose of.
     * @param   date
     *          the date scheduled for this dose.
     */
    public Dose(VaccineType type, TinyDate date) {
        this(type, date, type.getName());
    }

    /**
     * Constructs a Dose with an alternate display name (variant name). This alternate name can be used to visually
     * distinguish related, but technically different medications, but they are not taken into account for internal
     * processing of the schedule (they're used only for display purposes).
     * @param   type
     *          the vaccination that this is a dose of.
     * @param   date
     *          the date scheduled for this dose.
     * @param   altName
     *          the display name of this dose.
     */
    public Dose(VaccineType type, TinyDate date, String altName) {
        this.type = type;
        this.date = date;
        this.altName = altName;

        this.tempDate = date.toString();
    }

    /**
     * Constructs a Dose based on a date provided as a YYYY-MM-DD <code>String</code>. <i>Consider this constructor
     * deprecated and try not to use it.</i> The constructor assumes that the input String is properly formatted and
     * within accepted range of dates.
     * @param   type
     *          the vaccination that this is a dose of.
     * @param   date
     *          the date scheduled for this dose, as a YYYY-MM-DD <code>String</code>.
     */
    public Dose(VaccineType type, String date) {
        this(type, new TinyDate(date));
    }

    public VaccineType getType() {
        return type;
    }

    public TinyDate getDate() {
        return date;
    }

    public String getAltName() {
        return altName;
    }

    /**
     * This value is bidirectionally bound to an HTML input field, in Section 3 (individual doses display). Note
     * that the <strong>tempDate</strong> does not represent the actual date scheduled for this Dose, but is merely
     * visual. The user needs to submit it first before it becomes the actual date.
     * @return
     *          The temporary date <code>String</code>, in YYYY-MM-DD format. Used for display purposes.
     */
    public String getTempDate() {
        return tempDate;
    }

    /**
     * This value is bidirectionally bound to an HTML input field, in Section 3 (individual doses display). Note
     * that the <strong>tempDate</strong> does not represent the actual date scheduled for this Dose, but is
     * merely visual. The user needs to submit it first before it becomes the actual date.
     * @param   tempDate
     *          The temporary date placed in the input field for this Dose. It's not submitted yet.
     */
    public void setTempDate(String tempDate) {
        this.tempDate = tempDate;
    }

    /**
     * Checks whether this Dose's {@link #tempDate} was updated to a new value. Note that if the <i>tempDate</i>
     * field is empty, this Dose is marked for removal (via the {@link #isSetToRemove()} method) and not for being
     * rescheduled.
     * @return
     *          <code>true</code> if the {@link #tempDate} field is not empty AND is different from the original
     *          {@link #date}, <code>false</code> otherwise.
     */
    public boolean isSetToNew() {
        return !date.toString().equals(tempDate) && !tempDate.isEmpty();
    }

    /**
     * Checks whether this Dose's {@link #tempDate} was cleared (set to an empty <code>String</code>), which marks it
     * for removal once the input is submitted.
     * @return
     *          <code>true</code> if {@link #tempDate} is empty.
     */
    public boolean isSetToRemove() {
        return tempDate.isEmpty();
    }

    /**
     * Invokes {@link InputValidator} to check whether the {@link #tempDate} inputted by the user is correct.
     * @return
     *          <code>true</code> if the input is valid.
     */
    public boolean isInBounds() {
        return InputValidator.validateBounds(tempDate);
    }

    /**
     * <strong>Controls the display status of an HTML element</strong> (confirmation button for rescheduling this
     * Dose, in Section 3).
     * <p>Checks whether the user input sets this Dose as {@link #isSetToNew()} and {@link #isInBounds()}.</p>
     * @return
     *          <code>true</code> if the input is a valid date that's different from the old one.
     */
    public boolean isSetToConfirm() {
        return isSetToNew() && isInBounds();
    }
}
