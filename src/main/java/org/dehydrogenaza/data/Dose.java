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
    private final String variantName;

    /**
     * Internal YYYY-MM-DD date field, which is <strong>bidirectionally bound to an HTML input field</strong> (in
     * Section 3) for changing this Dose's date. After changes are confirmed by the user, if <code>tempDate</code> is
     * empty, this <code>Dose</code> object is removed from the calendar. Otherwise, its {@link #date} field is updated
     * to reflect the <code>tempDate</code>.
     */
    private String tempDate;

    public Dose(VaccineType type, TinyDate date) {
        this(type, date, type.getName());
    }

    public Dose(VaccineType type, TinyDate date, String variantName) {
        this.type = type;
        this.date = date;
        this.variantName = variantName;

        this.tempDate = date.toString();
    }

    public Dose(VaccineType type, String date) {
        this(type, new TinyDate(date));
    }

    public VaccineType getType() {
        return type;
    }

    public TinyDate getDate() {
        return date;
    }

    public String getVariantName() {
        return variantName;
    }

    public String getTempDate() {
        return tempDate;
    }

    public void setTempDate(String tempDate) {
        this.tempDate = tempDate;
    }

    public boolean isSetToNew() {
        return !date.toString().equals(tempDate) && !tempDate.isEmpty();
    }

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
