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

    }

    private void buildCalendarDates() {
        Map<String, List<Vaccine>> dateMap = new HashMap<>();

        TinyDate startDate = new TinyDate(form.getDateOfFirstVaccination());

        for (Vaccine vaccine : vaccines) {
            if (!vaccine.isSelected()) {
                continue;
            }
            for (int offset : vaccine.getDateOffsets()) {
                String offsetDate = startDate.offset(offset).toString();
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

        dateMap.forEach((date, vaccineList) -> calendarDates.add(new VaccinationDate(date, vaccineList)));
    }

    public List<VaccinationDate> get() {
        return calendarDates;
    }
}
