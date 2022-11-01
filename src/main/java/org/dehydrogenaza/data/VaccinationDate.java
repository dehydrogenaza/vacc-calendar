package org.dehydrogenaza.data;

import java.util.List;

public class VaccinationDate {
    private final String date;
    private final List<Vaccine> vaccines;

    public VaccinationDate(String date, List<Vaccine> vaccines) {
        this.date = date;
        this.vaccines = vaccines;
    }

    public String getDate() {
        return date;
    }

    public List<Vaccine> getVaccines() {
        return vaccines;
    }
}
