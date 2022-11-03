package org.dehydrogenaza;

import org.dehydrogenaza.data.*;
import org.dehydrogenaza.view.DisplayState;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.ApplicationTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate {

    LocalDate n = LocalDate.now();
    private final DataSource source = new DataSource();
    private final Form form = new Form(source);

    private VaccinationCalendar calendarDates = new VaccinationCalendar();

    public DataSource getSource() {
        return source;
    }

    private static String testLogger = "testLog";
    private static DisplayState displayState = DisplayState.FORM;

    public Client() {

    }

    public static void main(String[] args) {
        Templates.bind(new Client(), "application-content");
    }

    public List<Vaccine> getVaccines() {
        return form.getVaccines();
    }

    public Form getForm() {
        return form;
    }

    public String getTestLogger() {
        return testLogger;
    }

    public void setTestLogger(String testLogger) {
        Client.testLogger = testLogger;
    }

    public DisplayState getDisplayState() {
        return displayState;
    }

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

    public void changeDate(VaccinationDate oldDate) {
        calendarDates.changeDate(oldDate);
        testLogger="Invoked from: " + oldDate;
    }

    //just a fake function for testing
    public void addDate() {
        List<Vaccine> mock = new ArrayList<>(form.getVaccines().subList(6,10));
        calendarDates.get().add(new VaccinationDate("3000-06-05", mock));
    }

    //just a fake function for testing
    public void addVaccine(int index) {
        calendarDates.get().get(index).addVaccine(new Vaccine("dodano", 1000, new int[]{0}, true));
        testLogger="" + calendarDates.get().get(index).getVaccines();
    }
}