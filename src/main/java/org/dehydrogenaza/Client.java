package org.dehydrogenaza;

import org.dehydrogenaza.data.*;
import org.dehydrogenaza.view.DisplayState;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.ApplicationTemplate;

import java.util.ArrayList;
import java.util.List;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate {
    private final DataSource source = new DataSource();
    private final Form form = new Form(source);

//    private final VaccinationCalendar vaccinationCalendar = new VaccinationCalendar(form);

    private List<VaccinationDate> calendarDates = new ArrayList<>();

    public DataSource getSource() {
        return source;
    }

    private static String testLogger = "testLog";
    private static DisplayState displayState = DisplayState.FORM;

    public Client() {
//        List<Vaccine> mock1 = new ArrayList<>(form.getVaccines().subList(0,1));
//        List<Vaccine> mock2 = new ArrayList<>(form.getVaccines());
//        List<Vaccine> mock3 = new ArrayList<>(form.getVaccines().subList(0,5));
//        List<Vaccine> mock4 = new ArrayList<>(form.getVaccines().subList(6,6));
//        List<Vaccine> mock5 = new ArrayList<>(form.getVaccines().subList(1,3));
//        calendarDates.add(new VaccinationDate("1000-01-01", mock1));
//        calendarDates.add(new VaccinationDate("2023-01-05", mock2));
//        calendarDates.add(new VaccinationDate("2024-01-05", mock3));
//        calendarDates.add(new VaccinationDate("2025-01-05", mock4));
//        calendarDates.add(new VaccinationDate("2025-06-05", mock5));
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
            calendarDates = new VaccinationCalendar(form).get();
        }
    }

    public List<VaccinationDate> getCalendarDates() {
        return calendarDates;
    }

    public void addDate() {
        List<Vaccine> mock = new ArrayList<>(form.getVaccines().subList(6,10));
        calendarDates.add(new VaccinationDate("3000-06-05", mock));
    }

    public void addVaccine(int index) {
        calendarDates.get(index).addVaccine(new Vaccine("dodano", 1000, new int[]{0}, true));
        testLogger="" + calendarDates.get(index).getVaccines();
    }
}