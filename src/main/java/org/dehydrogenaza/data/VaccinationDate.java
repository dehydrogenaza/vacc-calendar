package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.TinyDate;

import java.util.List;

public class VaccinationDate {
    private final String dateISO;
    private final TinyDate dateInternal;
    private final List<Vaccine> vaccines;

    public VaccinationDate(String dateISO, List<Vaccine> vaccines) {
        this.dateISO = dateISO;
        this.vaccines = vaccines;

        dateInternal = new TinyDate(dateISO);
    }

    public String getDate() {
        return dateISO;
    }

    public int getDateAsNumber() {
        return dateInternal.asNumber();
    }

    public List<Vaccine> getVaccines() {
        return vaccines;
    }

    public void addVaccine(Vaccine vaccine) {
        vaccines.add(vaccine);
    }
}
