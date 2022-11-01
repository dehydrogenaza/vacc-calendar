package org.dehydrogenaza;

import org.dehydrogenaza.data.DataSource;
import org.dehydrogenaza.data.Form;
import org.dehydrogenaza.data.VaccinationDate;
import org.dehydrogenaza.data.Vaccine;
import org.dehydrogenaza.view.DisplayState;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.ApplicationTemplate;

import java.util.List;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate {
    private final DataSource source = new DataSource();
    private final Form form = new Form(source);

    public DataSource getSource() {
        return source;
    }

    private static String testLogger = "testLog";
    private static DisplayState displayState = DisplayState.FORM;

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
    }

    public List<VaccinationDate> getCalendarDates() {
        return List.of(
                new VaccinationDate(form.getDateOfFirstVaccination(), form.getVaccines().subList(0, 1)),
                new VaccinationDate("2023-01-05", form.getVaccines()),
                new VaccinationDate("2024-01-05", form.getVaccines().subList(0, 5)),
                new VaccinationDate("2025-01-05", form.getVaccines().subList(6, 6)),
                new VaccinationDate("2025-06-05", form.getVaccines().subList(1, 3)));
    }
}