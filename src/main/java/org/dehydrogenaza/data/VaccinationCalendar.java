package org.dehydrogenaza.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VaccinationCalendar {
    private final Form form;
    private final List<Vaccine> vaccines;


    private final List<VaccinationDate> calendarDates = new ArrayList<>();

    public VaccinationCalendar(Form form) {
        this.form = form;
        this.vaccines = form.getVaccines();

        buildCalendarDates();

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

    private void buildCalendarDates() {
        Map<String, List<Vaccine>> dateMap = new HashMap<>();
        int startDate = dateToInt(form.getDateOfFirstVaccination());
        for (Vaccine vaccine : vaccines) {
            if (!vaccine.isSelected()) {
                continue;
            }
            for (int offset : vaccine.getDateOffsets()) {
                String offsetDate = intToDate(offsetDate(startDate, offset));
                if (dateMap.containsKey(offsetDate)) {
                    List<Vaccine> vaccinesAtDate = dateMap.get(offsetDate);
                    vaccinesAtDate.add(vaccine);
                } else {
                    List<Vaccine> firstVaccineAtDate = new ArrayList<>();
                    firstVaccineAtDate.add(vaccine);
                    dateMap.put(offsetDate, firstVaccineAtDate);
                }
            }
        }

        dateMap.forEach((k, v) -> {
            calendarDates.add(new VaccinationDate(k, v));
        });
    }

    private int offsetDate(int start, int offset) {
        //not implemented yet...
        return start + offset;
    }

    private int dateToInt(String dateString) {
            //YYYY-MM-DD
        return Integer.parseInt(dateString.substring(0, 4)
                + dateString.substring(5, 7)
                + dateString.substring(8, 10));
    }

    private String intToDate(int dateInt) {
        String parsed = String.valueOf(dateInt);
        return parsed.substring(0, 4) + "-" + parsed.substring(4, 6) + "-" + parsed.substring(6);
    }

    public List<VaccinationDate> get() {
        return calendarDates;
    }
}
