package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.TinyDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 * A <strong>VaccinationCalendar</strong> represents a mapping of suggested vaccination dates to a list of vaccines
 * that should be administered on that day. The mapping can be empty, and the internal values can be <code>null</code>.
 */
public class VaccinationCalendar {

    /**
     * Reference to the input form, from which data should be taken.
     */
    private final Form form;

    /**
     * Reference to the list of ALL supported vaccines (including ones that are not selected).
     */
    private final List<Vaccine> vaccines;

    /**
     * The main point of this class. A sorted list of {@link VaccinationDate}s.
     */
    private final List<VaccinationDate> calendarDates = new ArrayList<>();


    /**
     * Constructs a calendar with input data from the given {@link Form}.
     * @param   form
     *          source of input data.
     */
    public VaccinationCalendar(Form form) {
        this.form = form;
        this.vaccines = form.getVaccines();

        buildCalendarDates();

        sortByDate();

    }


    /**
     * Constructs an empty calendar. Internal fields will be <code>null</code>s, except for
     * {@link #calendarDates}, which is initialized to an empty {@link ArrayList}.
     */
    public VaccinationCalendar() {
        this.form = null;
        this.vaccines = null;
    }

    public void removeDate(VaccinationDate date) {
        calendarDates.remove(date);
    }


//    TODO: Redo the whole thing.
    /**
     * <strong>MOCK IMPLEMENTATION</strong>, changes the given date to a preset one. Just for testing.
     * @param   oldDate
     *          {@link VaccinationDate} to be replaced.
     */
    public void changeDate(VaccinationDate oldDate) {
        //for testing
        List<Vaccine> mockList = new ArrayList<>(form.getVaccines().subList(3, 6));
        VaccinationDate newDate = new VaccinationDate("1999-12-31", mockList);

        calendarDates.replaceAll(date
                -> date.getDateAsNumber() == oldDate.getDateAsNumber() ? newDate : date);
        sortByDate();

    }

    public void updateDate(VaccinationDate changedDate) {
        if (changedDate.getTempDate().isEmpty()) {
            removeDate(changedDate);
        } else {
            //TODO: validation?
            changedDate.update();

            calendarDates.forEach(d -> {
                if (d.equals(changedDate)) {
                    return;
                }
                if (d.getDate().equals(changedDate.getDate())) {
                    for (Vaccine vaccine : changedDate.getVaccines()) {
                        d.addVaccine(vaccine);
                    }
                    changedDate.flaggedForRemoval = true;
                }
            });

            if (changedDate.flaggedForRemoval) {
                removeDate(changedDate);
            }

            sortByDate();
        }
    }


    /**
     * Sorts in ascending order, earliest to latest. <strong>Mutates</strong> the original list.
     */
    private void sortByDate() {
        //intentionally NOT using a Comparator (saves a few kB in the resulting JS)
        calendarDates.sort((d1, d2)
                -> d1.getDateAsNumber() - d2.getDateAsNumber());
    }


//  TODO: Refactor so that the SOURCE provides the full list, and FORM only the selected ones
//  TODO: Also, should probably be split into two methods for readability
//  TODO: Refactor to use either TinyDate or its int value in the HashMap
    /**
     * Populates the {@link #calendarDates} list with {@link VaccinationDate}s, unsorted at this point.
     */
    private void buildCalendarDates() {
        //key: unique date --> values: vaccines scheduled for that date
        Map<String, List<Vaccine>> mapOfAllVaccinationsOnGivenDates = new HashMap<>();

        TinyDate startDate = new TinyDate(form.getDateOfFirstVaccination());

        for (Vaccine vaccine : vaccines) {
            if (!vaccine.isSelected()) {
                //skip if this vaccine is not selected
                continue;
            }
            //each vaccine can have multiple doses, that are OFFSET by a certain number of days
            for (int offsetInDays : vaccine.getDateOffsets()) {
                //when the dose should be administered
                String dateOfVaccination = startDate.addDays(offsetInDays).toString();

                if (mapOfAllVaccinationsOnGivenDates.containsKey(dateOfVaccination)) {
                    //something is already scheduled for this date
                    //therefore, we add this "dose" to the list instead of duplicating the date
                    List<Vaccine> vaccinationsOnThisDate = mapOfAllVaccinationsOnGivenDates.get(dateOfVaccination);
                    vaccinationsOnThisDate.add(vaccine);

                } else {
                    //nothing is scheduled for this date, yet
                    //create a new list and add this "dose" as its first item
                    List<Vaccine> firstVaccineAtDate = new ArrayList<>();
                    firstVaccineAtDate.add(vaccine);
                    mapOfAllVaccinationsOnGivenDates.put(dateOfVaccination, firstVaccineAtDate);

                }
            }
        }

//      TODO: This could be a separate method
        //translate each key-value pair of our HashMap to a VaccinationDate instance
        //add them all to .calendarDates
        mapOfAllVaccinationsOnGivenDates.forEach(
                (date, vaccinesAtDate) -> calendarDates.add(new VaccinationDate(date, vaccinesAtDate)));
    }

    /**
     * Getter for {@link #calendarDates}.
     * @return
     *          the (sorted) list of {@link VaccinationDate}s.
     */
    public List<VaccinationDate> get() {
        return calendarDates;
    }
}
