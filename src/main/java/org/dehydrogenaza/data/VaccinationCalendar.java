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
    private final List<VaccineType> vaccines;

    // TODO: try to reimplement using TreeSets and see if it costs a lot in terms of JS
    /**
     * The main point of this class. A sorted list of {@link ScheduleForDay}s.
     */
    private final List<ScheduleForDay> scheduledDates = new ArrayList<>();


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
     * {@link #scheduledDates}, which is initialized to an empty {@link ArrayList}.
     */
    public VaccinationCalendar() {
        this.form = null;
        this.vaccines = null;
    }


    /**
     * Fully removes a {@link ScheduleForDay} (with every dose scheduled during that day) from this calendar.
     * @param   date
     *          {@link ScheduleForDay} object to be removed, usually supplied by Flavour from an HTML input field.
     */
    public void removeDate(ScheduleForDay date) {
        scheduledDates.remove(date);
    }


//    TODO: Redo the whole thing.
    /**
     * <strong>MOCK IMPLEMENTATION</strong>, changes the given date to a preset one. Just for testing.
     * @param   oldDate
     *          {@link ScheduleForDay} to be replaced.
     */
    public void changeDate(ScheduleForDay oldDate) {
        //for testing
        List<VaccineType> mockTypes = new ArrayList<>(form.getVaccines().subList(3, 6));
        List<Dose> mockDoses = new ArrayList<>();
        for (VaccineType type : mockTypes) {
            mockDoses.add(new Dose(type, new TinyDate("1999-12-31")));
        }
        ScheduleForDay newDate = new ScheduleForDay("1999-12-31", mockDoses);

        scheduledDates.replaceAll(date
                -> date.getDateAsNumber() == oldDate.getDateAsNumber() ? newDate : date);
        sortByDate();

    }


    /**
     * Updates the calendar by either removing a given {@link ScheduleForDay} instance (if its calendar date was set
     * to ""), or by rescheduling it to a new day (temporarily stored in its <code>tempDate</code> field). If the new
     * calendar date already has some doses scheduled, the two ScheduleForDay objects are conflated in place of the
     * existing (older) object, and the changedDate is removed from the calendar.
     * @param   changedDate
     *          a {@link ScheduleForDay} object which has its internal <code>tempDate</code> field changed and is
     *          contained in this {@link VaccinationCalendar}.
     */
    public void updateDate(ScheduleForDay changedDate) {
        //empty input = remove from calendar completely
        if (changedDate.getTempDate().isEmpty()) {
            removeDate(changedDate);
        } else {
            //submit temp (input) value
            changedDate.confirmTempValue();

            boolean flaggedForRemoval = false;
            for (ScheduleForDay d : scheduledDates) {
                //don't do anything for the "changedDate" object itself
                if (d == changedDate) {
                    continue;
                }
                //if *another* ScheduleForDay has the same actual "date", merge their content
                if (d.getDate().equals(changedDate.getDate())) {
                    for (Dose dose : changedDate.getDoses()) {
                        d.addDose(dose);
                    }
                    flaggedForRemoval = true;
                }
            }

            //if "changedDate"'s content was merged into another date, remove it
            if (flaggedForRemoval) {
                removeDate(changedDate);
            }

            sortByDate();
        }
    }

    public void updateDose(ScheduleForDay changedDate, Dose changedDose) {
        if (changedDose.isSetToNew()) {
            Dose updatedDose = new Dose(changedDose.getType(), changedDose.getTempDate());

            //search the calendar to see if a Schedule already exists for this new date
            boolean addedToExisting = false;
            for (ScheduleForDay date : scheduledDates) {
                if (date.getDate().equals(updatedDose.getTempDate())) {
                    date.addDose(updatedDose);
                    addedToExisting = true;
                    break;
                }
            }
            if (!addedToExisting) {
                List<Dose> vaccinesAtDate = new ArrayList<>();
                vaccinesAtDate.add(updatedDose);
                scheduledDates.add(new ScheduleForDay(updatedDose.getTempDate(), vaccinesAtDate));

                sortByDate();
            }
        }
        removeDose(changedDate, changedDose);

    }

    private void removeDose(ScheduleForDay changedDate, Dose dose) {
        changedDate.removeDose(dose);
        if (changedDate.getDoses().isEmpty()) {
            removeDate(changedDate);
        }
    }


    /**
     * Sorts in ascending order, earliest to latest. <strong>Mutates</strong> the original list.
     */
    private void sortByDate() {
        //intentionally NOT using a Comparator (saves a few kB in the resulting JS)
        scheduledDates.sort((d1, d2)
                -> d1.getDateAsNumber() - d2.getDateAsNumber());
    }


//  TODO: Refactor so that the SOURCE provides the full list, and FORM only the selected ones
//  TODO: Also, should probably be split into two methods for readability
//  TODO: Refactor to use either TinyDate or its int value in the HashMap
    /**
     * Populates the {@link #scheduledDates} list with {@link ScheduleForDay}s, unsorted at this point.
     */
    private void buildCalendarDates() {
        //key: unique date --> values: vaccines scheduled for that date
        Map<String, List<Dose>> mapOfAllVaccinationsOnGivenDates = new HashMap<>();

        TinyDate startDate = new TinyDate(form.getDateOfFirstVaccination());

        //TODO: Some of this should probably be moved to VaccineType
        for (VaccineType type : vaccines) {
            if (!type.isSelected()) {
                //skip if this vaccine is not selected
                continue;
            }
            //each vaccine can have multiple doses, that are OFFSET by a certain number of days
            for (int offsetInDays : type.getDateOffsets()) {
                //when the dose should be administered
                String dateOfVaccination = startDate.addDays(offsetInDays).toString();

                if (mapOfAllVaccinationsOnGivenDates.containsKey(dateOfVaccination)) {
                    //something is already scheduled for this date
                    //therefore, we add this "dose" to the list instead of duplicating the date
                    List<Dose> vaccinesAtDate = mapOfAllVaccinationsOnGivenDates.get(dateOfVaccination);
                    vaccinesAtDate.add(new Dose(type, new TinyDate(dateOfVaccination)));

                } else {
                    //nothing is scheduled for this date, yet
                    //create a new list and add this "dose" as its first item
                    List<Dose> vaccinesAtDate = new ArrayList<>();
                    vaccinesAtDate.add(new Dose(type, new TinyDate(dateOfVaccination)));
                    mapOfAllVaccinationsOnGivenDates.put(dateOfVaccination, vaccinesAtDate);

                }
            }
        }

//      TODO: This could be a separate method (maybe, could be overthinking)
        //translate each key-value pair of our HashMap to a ScheduleForDay instance
        //add them all to .calendarDates
        mapOfAllVaccinationsOnGivenDates.forEach(
                (date, vaccinesAtDate) -> scheduledDates.add(new ScheduleForDay(date, vaccinesAtDate)));
    }

    /**
     * Getter for {@link #scheduledDates}.
     * @return
     *          the (sorted) list of {@link ScheduleForDay}s.
     */
    public List<ScheduleForDay> get() {
        return scheduledDates;
    }
}
