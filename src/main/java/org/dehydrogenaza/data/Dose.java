package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.TinyDate;

public class Dose {

    private final VaccineType type;
    private final TinyDate date;

    public Dose(VaccineType type, TinyDate date) {
        this.type = type;
        this.date = date;
    }

    public VaccineType getType() {
        return type;
    }

    public TinyDate getDate() {
        return date;
    }
}
