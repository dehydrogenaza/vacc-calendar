package org.dehydrogenaza.data;

import java.util.HashMap;
import java.util.Set;

public class Form {
    private final DataSource source;

    private final HashMap<String, Boolean> vaccines = new HashMap<>();
    private String dateOfBirth = "";
    private String dateOfFirstVaccination = "";


    public Form(DataSource source) {
        this.source = source;

        for (String vaccine : source.getVaccines()) {
            vaccines.put(vaccine, Math.random() > 0.5);
        }
    }

    public boolean isVaccineSelected(String name) {
        return vaccines.get(name);
    }

    public void changeVaccineSelection(String name, boolean newSelectionStatus) {
        vaccines.replace(name, newSelectionStatus);
    }

    public Set<String> getVaccines() {
        return vaccines.keySet();
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.dateOfFirstVaccination = dateOfBirth;
    }

    public String getDateOfFirstVaccination() {
        return dateOfFirstVaccination;
    }

    public void setDateOfFirstVaccination(String dateOfFirstVaccination) {
        this.dateOfFirstVaccination = dateOfFirstVaccination;
    }

}
