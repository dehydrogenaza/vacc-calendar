package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.InputValidator;
import org.dehydrogenaza.data.utils.TinyDate;

public class Dose {

    private final VaccineType type;
    private final TinyDate date;

    private final String variantName;

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
