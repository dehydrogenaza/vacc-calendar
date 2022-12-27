package org.dehydrogenaza.data;


import org.dehydrogenaza.data.utils.DisplayState;

import java.util.List;


/**
 * Stores and manages data submitted by the user, such as their choice of vaccinations, child's birthdate etc.
 */
public class Form {

    /**
     * Source of internal data.
     */
    private final DataProvider dataProvider;


    /**
     * List of supported vaccines, taken from the {@link #dataProvider}.
     */
    private final List<VaccineType> vaccines;

    /**
     * List of supported "schemes" ("plans", such as the government-funded free vaccination plan), taken from the
     * {@link #dataProvider}.
     */
    private final List<VaccinationScheme> schemes;

    /**
     * Child's birthdate.
     * <br>This value is (through its getters and setters) bidirectionally bound to an HTML input field.
     */
    private String dateOfBirth = "";

    /**
     * Chosen date of the first vaccination.
     * <br>This value is (through its getters and setters) bidirectionally bound to
     * an HTML input field.
     */
    private String dateOfFirstVaccination = "";

    /**
     * Whether the user accepted the license/terms of service.
     * <br>This value is (through its getters and setters)
     * bidirectionally bound to an HTML input field.
     */
    private boolean licenseAccepted = false;

    /**
     * <code>true</code> if the user submits a value and then removes it. Used in input validation. Read by an HTML
     * element, to control its visibility.
     */
    private boolean dateOfBirthMissing = false;

    /**
     * <code>true</code> if the user submits a value and then removes it. Used in input validation. Read by an HTML
     * element, to control its visibility.
     */
    private boolean dateOfFirstVaccinationMissing = false;

    /**
     * <code>true</code> if the user submits a date of 1st vaccination that's before the child's birthdate. Used in
     * input validation. Read by an HTML element, to control its visibility.
     */
    private boolean dateOfFirstVaccinationTooEarly = false;


//  TODO: Remove test utility
//    /**
//     * Just a quick "debug logger" for visual reference, will be removed in the release version.
//     */
//    private String formLog = "form";


    /**
     * Constructs the initial Form, pulling vaccine data from the provided {@link DataProvider}.
     * @param   dataProvider
     *          Source of supported vaccine data.
     */
    public Form(DataProvider dataProvider) {
        this.dataProvider = dataProvider;

        this.vaccines = dataProvider.getVaccines();
        this.schemes = dataProvider.getSchemes();
    }

//  TODO: Refactor so that the SOURCE provides the full list, and FORM only the selected ones
    public List<VaccineType> getVaccines() {
        return vaccines;
    }

    public List<VaccinationScheme> getSchemes() {
        return schemes;
    }

    /**
     * <strong>Bidirectionally bound to an HTML input element</strong> (in Section 1, the initial input form).
     * @return
     *          The child's date of birth, synchronized with the one in the HTML input form.
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * <strong>Bidirectionally bound to an HTML input element</strong> (in Section 1, the initial input form).
     * <p>Updating this field also affects the {@link #dateOfFirstVaccination}, which is automatically set to the
     * same value.</p>
     * @param   dateOfBirth
     *          the date of birth inputted by the user.
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        dateOfBirthMissing = dateOfBirth.isEmpty();

        setDateOfFirstVaccination(dateOfBirth);
    }

    /**
     * <strong>Bidirectionally bound to an HTML input element</strong> (in Section 1, the initial input form).
     * <p>This value might be set to an incorrect date (such as any date before the date of birth) and should not be
     * accepted without validation.</p>
     * @return
     *          The child's chosen date of first vaccination, synchronized with the one in the HTML input form.
     */
    public String getDateOfFirstVaccination() {
        return dateOfFirstVaccination;
    }

    /**
     * <strong>Bidirectionally bound to an HTML input element</strong> (in Section 1, the initial input form).
     * <p>Simple validation will be performed by the {@link #validateDates()} method to see if this date falls
     * (correctly) after the child's date of birth, but the value of this field won't be changed even if it's
     * incorrect (instead, the {@link #dateOfFirstVaccinationTooEarly} flag will be set to <code>true</code>.</p>
     * @param   dateOfFirstVaccination
     *          the chosen date of first vaccination, inputted by the user.
     */
    public void setDateOfFirstVaccination(String dateOfFirstVaccination) {
        this.dateOfFirstVaccination = dateOfFirstVaccination;
        dateOfFirstVaccinationMissing = dateOfBirth.isEmpty();

        if (!dateOfFirstVaccinationMissing) {
            validateDates();
        }
    }

    public boolean getLicenseAccepted() {
        return licenseAccepted;
    }

    public void setLicenseAccepted(boolean licenseAccepted) {
        this.licenseAccepted = licenseAccepted;
    }

    public boolean isDateOfBirthMissing() {
        return dateOfBirthMissing;
    }

    public boolean isDateOfFirstVaccinationMissing() {
        return dateOfFirstVaccinationMissing;
    }

    public boolean isDateOfFirstVaccinationTooEarly() {
        return dateOfFirstVaccinationTooEarly;
    }

    //  TODO: Remove test utility
//    public String getFormLog() {
//        return formLog;
//    }

    //  TODO: Remove test utility
//    public void setFormLog(String formLog) {
//        this.formLog = formLog;
//    }


//    TODO: check if any vaccines are selected at all
//    TODO: possibly return a different State depending on what went wrong
//    TODO: maybe also check if dates are within reasonable ranges
    /**
     * Attempts to validate and submit the currently input data. If validation succeeds, the app moves on to the next
     * phase, that is: displaying the suggested vaccination dates.
     * @return
     *          {@link DisplayState#BAD_SUBMIT} if the data is incorrect, {@link DisplayState#CALENDAR} if the app
     *          should proceed.
     */
    public DisplayState submit() {
        if (!licenseAccepted) return DisplayState.BAD_SUBMIT;

        dateOfBirthMissing = dateOfBirth.isEmpty();
        dateOfFirstVaccinationMissing = dateOfFirstVaccination.isEmpty();

        if (dateOfBirthMissing || dateOfFirstVaccinationMissing) return DisplayState.BAD_SUBMIT;

        if (!validateDates()) return DisplayState.BAD_SUBMIT;

        return DisplayState.CALENDAR;
    }


//    TODO: perform more robust checks, for example with malformed Strings etc.
//    TODO: possibly check if dates are within reasonable ranges
    // TODO: move some or all of this to the InputValidator
    /**
     * Checks if the current input dates ({@link #dateOfBirth} and {@link #dateOfFirstVaccination}) are properly
     * formatted AND if the first vaccination doesn't come earlier than the birthdate.
     * @return
     *          <code>true</code> if it's OK to proceed, <code>false</code> otherwise.
     */
    private boolean validateDates() {
        try {
            //TODO: Use TinyDate instead, possibly allows us to get rid of Integer usage
            //YYYY-MM-DD
            int parsedBirth = Integer.parseInt(dateOfBirth.substring(0, 4)
                    + dateOfBirth.substring(5, 7)
                    + dateOfBirth.substring(8, 10));
            if (parsedBirth < 19000101) {
                dateOfBirthMissing = true;
                return false;
            }

            int parsedFirstVaccination = Integer.parseInt(dateOfFirstVaccination.substring(0, 4)
                    + dateOfFirstVaccination.substring(5, 7)
                    + dateOfFirstVaccination.substring(8, 10));

            dateOfFirstVaccinationTooEarly = parsedFirstVaccination < parsedBirth;
            return !dateOfFirstVaccinationTooEarly;
        } catch (Exception e) {
            return false;
        }
    }
}
