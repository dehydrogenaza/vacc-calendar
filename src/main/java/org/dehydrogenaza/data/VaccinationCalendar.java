package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.TinyDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class VaccinationCalendar {
    private final Form form;
    private final List<Vaccine> vaccines;
    private final List<VaccinationDate> calendarDates = new ArrayList<>();



    public VaccinationCalendar(Form form) {
        this.form = form;
        this.vaccines = form.getVaccines();

        buildCalendarDates();

        sortByDate();

    }

    public VaccinationCalendar() {
        this.form = null;
        this.vaccines = null;
    }

    public void changeDate(VaccinationDate oldDate) {
        //MOCK IMPLEMENTATION, for testing
        List<Vaccine> mockList = new ArrayList<>(form.getVaccines().subList(3, 6));
        VaccinationDate newDate = new VaccinationDate("1999-12-31", mockList);

        calendarDates.replaceAll(date
                -> date.getDateAsNumber() == oldDate.getDateAsNumber() ? newDate : date);
        sortByDate();
    }

    private void sortByDate() {
        //intentionally NOT using Comparator (saves a few kB in the resulting JS)
        calendarDates.sort((d1, d2)
                -> d1.getDateAsNumber() - d2.getDateAsNumber());
    }

    private void buildCalendarDates() {
        Map<String, List<Vaccine>> mapOfAllVaccinationsOnGivenDates = new HashMap<>();

        TinyDate startDate = new TinyDate(form.getDateOfFirstVaccination());

        for (Vaccine vaccine : vaccines) {
            if (!vaccine.isSelected()) {
                continue;
            }
            for (int offsetInDays : vaccine.getDateOffsets()) {
                String dateOfVaccination = startDate.offset(offsetInDays).toString();

                if (mapOfAllVaccinationsOnGivenDates.containsKey(dateOfVaccination)) {
                    List<Vaccine> vaccinationsOnThisDate = mapOfAllVaccinationsOnGivenDates.get(dateOfVaccination);
                    vaccinationsOnThisDate.add(vaccine);

                } else {
                    List<Vaccine> firstVaccineAtDate = new ArrayList<>();
                    firstVaccineAtDate.add(vaccine);
                    mapOfAllVaccinationsOnGivenDates.put(dateOfVaccination, firstVaccineAtDate);

                }
            }
        }

        mapOfAllVaccinationsOnGivenDates.forEach(
                (date, vaccinesAtDate) -> calendarDates.add(new VaccinationDate(date, vaccinesAtDate)));
    }

    public List<VaccinationDate> get() {
        return calendarDates;
    }
}
