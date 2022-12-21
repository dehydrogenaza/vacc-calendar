package org.dehydrogenaza;

import org.dehydrogenaza.data.*;
import org.dehydrogenaza.data.datasources.*;
import org.dehydrogenaza.data.utils.*;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.ApplicationTemplate;

import java.util.List;


/**
 * The <strong>Client</strong> is the primary, and currently the only one of View {@link Templates}. It serves as the
 * main entry point, maintains the app's state and passes data between components.
 * <p>The HTML template bound to this class, <code>client.html</code>, is inserted directly to <code>index.html</code>
 * into the "application-content" <code>div</code>.</p>
 * <p><strong>Note:</strong> once proper Routing is implemented, the Client class is expected to change
 * significantly.</p>
 */
@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate {


    /**
     * The source of vaccination data. Currently, just a mock, but will be read probably from a JSON file later.
     */
    private final DataProvider dataProvider = new DataProvider(new FakeVaccinationSource());

    /**
     * The input form: date of birth, date of first vaccination, choice of vaccinations, agreements etc.
     */
    private final Form form = new Form(dataProvider);


    /**
     * Stores a mapping of unique calendar dates to vaccinations scheduled for that day.
     */
    private VaccinationCalendar calendar = new VaccinationCalendar();


    //  TODO: Remove test utility
//    /**
//     * Just a test utility. Displays a "debug log" on the bottom of the page, for easy visual reference.
//     */
//    private static String testLogger = "testLog";

    /**
     * The current state of the app. Determines which actions can be taken and which elements of the page should be
     * visible/active etc.
     */
    private static DisplayState displayState = DisplayState.FORM;


    /**
     * Not necessary for now, but potentially can be replaced by a <code>Client(DataProvider)</code> constructor.
     */
    public Client() {
        InputValidator.init(form);
    }


    public static void main(String[] args) {
        Templates.bind(new Client(), "application-content");
    }

    public List<VaccineType> getVaccines() {
        return dataProvider.getVaccines();
    }

    public List<VaccinationScheme> getSchemes() {
        return dataProvider.getSchemes();
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public Form getForm() {
        return form;
    }

    //  TODO: Remove test utility
//    public String getTestLogger() {
//        return testLogger;
//    }

    //  TODO: Remove test utility
//    public void setTestLogger(String testLogger) {
//        Client.testLogger = testLogger;
//    }

    public DisplayState getDisplayState() {
        return displayState;
    }


    /**
     * Finalizes initial user input in the main {@link Form}, queries its validity, and sets the application's state
     * accordingly. If everything is OK, the state should become {@link DisplayState#CALENDAR} and the {@link #calendar}
     * field will be set to a new {@link VaccinationCalendar} instance.
     */
    public void submit() {
        displayState = form.submit();
//        testLogger = "submitted";

        if (displayState == DisplayState.CALENDAR) {
            getVaccines().forEach(vax -> vax.applyFormDataHandlers(form));
            calendar = new VaccinationCalendar(form);
        }
    }

    public String getCSVExportURI() {
        return CSVWriter.getDataURI(calendar.get());
    }

    public List<ScheduleForDay> getCalendar() {
        return calendar.get();
    }


    /**
     * Submits the user's changes made to a single, scheduled {@link ScheduleForDay}.
     *
     * @param date The scheduled date that was changed.
     */
    public void confirmCalendarChange(ScheduleForDay date) {
        if (date.getDate().equals(date.getTempDate())) {
            return;
        }
        if (date.isInBounds()) {
            calendar.updateDate(date);
        }
    }

    public void confirmDoseChange(ScheduleForDay date, Dose dose) {
        if (dose.getDate().toString().equals(dose.getTempDate())) {
            return;
        }
        if (dose.isInBounds()) {
            calendar.updateDose(date, dose);
        }
    }

    public void removeDose(ScheduleForDay date, Dose dose) {
        calendar.removeDose(date, dose);
    }

    public void removeAllOfType(VaccineType type) {
        calendar.removeAllOfType(type);
    }

    public void setChosenScheme(String schemeID) {
        IVaccineSource newSchemeSource;
        switch (schemeID) {
            case "0":
                newSchemeSource = new FakeVaccinationSource();
                break;
            case "1":
                newSchemeSource = new FreeVaccinationSource();
                break;
            default:
                newSchemeSource = new FreeVaccinationSource();
        }

        dataProvider.changeChosenVaccinationScheme(newSchemeSource);
    }
}