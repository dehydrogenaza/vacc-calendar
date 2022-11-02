package org.dehydrogenaza.data;


import org.dehydrogenaza.view.DisplayState;

import java.util.List;

public class Form {
    private final DataSource source;

    private final List<Vaccine> vaccines;
    private String dateOfBirth = "";
    private String dateOfFirstVaccination = "";
    private boolean licenseAccepted = false;

    private String formLog = "form";


    public Form(DataSource source) {
        this.source = source;

        this.vaccines = source.getVaccines();
    }

    public List<Vaccine> getVaccines() {
        return vaccines;
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

    public boolean getLicenseAccepted() {
        return licenseAccepted;
    }

    public void setLicenseAccepted(boolean licenseAccepted) {
        this.licenseAccepted = licenseAccepted;
    }

    public String getFormLog() {
        return formLog;
    }

    public DisplayState submit() {
        if (!licenseAccepted) return DisplayState.BAD_SUBMIT;

        if (dateOfBirth.isEmpty() || dateOfFirstVaccination.isEmpty()) return DisplayState.BAD_SUBMIT;

        if (!validateDates()) return DisplayState.BAD_SUBMIT;

        return DisplayState.CALENDAR;
    }

    private boolean validateDates() {
        try {
            //YYYY-MM-DD
            int parsedFirstVaccination = Integer.parseInt(dateOfFirstVaccination.substring(0, 4)
                    + dateOfFirstVaccination.substring(5, 7)
                    + dateOfFirstVaccination.substring(8, 10));
            int parsedBirth = Integer.parseInt(dateOfBirth.substring(0, 4)
                    + dateOfBirth.substring(5, 7)
                    + dateOfBirth.substring(8, 10));

            return parsedFirstVaccination >= parsedBirth;
        } catch (Exception e) {
            return false;
        }
    }
}
