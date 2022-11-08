package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.InputValidator;
import org.dehydrogenaza.data.utils.TinyDate;

public class Dose {

    private final VaccineType type;
    private final TinyDate date;

    private String tempDate;

    public Dose(VaccineType type, TinyDate date) {
        this.type = type;
        this.date = date;

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

    public String getTempDate() {
        return tempDate;
    }

    public void setTempDate(String tempDate) {
        this.tempDate = tempDate;
    }

    public boolean isSetToNew() {
        //TODO: Add full validation
        return !date.toString().equals(tempDate) && !tempDate.isEmpty();
    }

    public boolean isSetToRemove() {
        return tempDate.isEmpty();
    }

    public boolean isInBounds() {
        return InputValidator.validate(tempDate);
    }

    public boolean isSetToConfirm() {
        return isSetToNew() && isInBounds();
    }
}
