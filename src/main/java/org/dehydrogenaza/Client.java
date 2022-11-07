package org.dehydrogenaza;

import org.dehydrogenaza.data.*;
import org.dehydrogenaza.data.utils.DisplayState;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.ApplicationTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * The <strong>Client</strong> is the primary, and currently the only one of View {@link Templates}.
 * It serves as the main entry point, maintains the app's state and passes data between components.
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
    private final DataSource source = new DataSource();

    /**
     * The input form: date of birth, date of first vaccination, choice of vaccinations, agreements etc.
     */
    private final Form form = new Form(source);


    /**
     * Stores a mapping of unique calendar dates to vaccinations scheduled for that day.
     */
    private VaccinationCalendar calendarDates = new VaccinationCalendar();


    //  TODO: Remove test utility
    /**
     * Just a test utility. Displays a "debug log" on the bottom of the page, for easy visual reference.
     */
    private static String testLogger = "testLog";

    /**
     * The current state of the app. Determines which actions can be taken and which elements of the page should be
     * visible/active etc.
     */
    private static DisplayState displayState = DisplayState.FORM;


    /**
     * Not necessary for now, but potentially can be replaced by a <code>Client(DataSource)</code> constructor.
     */
    public Client() {
    }


    public static void main(String[] args) {
        Templates.bind(new Client(), "application-content");
    }

    public List<Vaccine> getVaccines() {
        //TODO: This should probably be taken from the SOURCE instead (the FORM takes it from there anyway)
        return form.getVaccines();
    }

    public DataSource getSource() {
        return source;
    }

    public Form getForm() {
        return form;
    }

//  TODO: Remove test utility
    public String getTestLogger() {
        return testLogger;
    }

//  TODO: Remove test utility
    public void setTestLogger(String testLogger) {
        Client.testLogger = testLogger;
    }

    public DisplayState getDisplayState() {
        return displayState;
    }


    /**
     * Finalizes initial user input in the main {@link Form}, queries its validity, and sets the application's state
     * accordingly. If everything is OK, the state should become {@link DisplayState#CALENDAR} and the
     * {@link #calendarDates} field will be set to a new {@link VaccinationCalendar} instance.
     */
    public void submit() {
        displayState = form.submit();
        testLogger = "submitted";

        if (displayState == DisplayState.CALENDAR) {
            calendarDates = new VaccinationCalendar(form);
        }
    }

    public List<VaccinationDate> getCalendarDates() {
        return calendarDates.get();
    }


    /**
     * Submits the user's changes made to a single, scheduled {@link VaccinationDate}.
     * @param   date
     *          The scheduled date that was changed.
     */
    public void confirmCalendarChange(VaccinationDate date) {
        if (date.getDate().equals(date.getTempDate())) {
            return;
        }
        calendarDates.updateDate(date);
    }


//    TODO: Create an actual implementation
    /**
     * <strong>MOCK IMPLEMENTATION</strong>
     * <p>Replaces a {@link VaccinationDate} with a new one (which should be probably passed as parameter, but
     * currently isn't). This <strong>mutates</strong> the contents of {@link #calendarDates}.</p>
     * @param   date
     *          The date to be replaced (representing a unique calendar date and every vaccination scheduled for that
     *          date).
     */
    public void changeDate(VaccinationDate date) {
        calendarDates.changeDate(date);
        testLogger = "Invoked from: " + date;
    }


//    TODO: Create an actual implementation, maybe
    /**
     * <strong>MOCK IMPLEMENTATION</strong>
     * <p>Appends a new, FAKE {@link VaccinationDate} directly to the list in {@link #calendarDates}. Useful for
     * testing.</p>
     * <p>If this functionality is actually desired in the release version (which is somewhat likely), it should
     * probably be implemented in the {@link VaccinationCalendar}</p> class instead, and called from here.
     */
    public void addDate() {
        List<Vaccine> mock = new ArrayList<>(form.getVaccines().subList(6,10));
        calendarDates.get().add(new VaccinationDate("3000-06-05", mock));
    }


//    TODO: Create an actual implementation, maybe
    /**
     * <strong>MOCK IMPLEMENTATION</strong>
     * <p>Adds a single FAKE {@link Vaccine} "dose" to the specified schedule (position in the {@link #calendarDates}.
     * Useful for testing.</p>
     * <p>If this functionality is actually desired in the release version (which is somewhat likely), it should
     * probably be implemented in the {@link VaccinationCalendar}</p> class instead, and called from here.
     * @param   index
     *          position in the list of unique calendar dates, taken from the calling HTML component.
     */
    public void addVaccine(int index) {
        calendarDates.get().get(index).addVaccine(new Vaccine("dodano", 1000, new int[]{0}, true));
        testLogger="" + calendarDates.get().get(index).getVaccines();
    }
}