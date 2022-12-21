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


    // TODO: BUGGED. Moving the entire date does not effect dates (maybe just tempDates?) of individual Doses.
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

    // TODO: not sure if this should allow to remove a dose since that function has a separate UI button
    /**
     * Updates the calendar by either removing a given {@link Dose} instance (if its calendar date was set
     * to ""), or by rescheduling it to a new day (temporarily stored in its <code>tempDate</code> field). If
     * the new calendar date already has some doses scheduled, this one is simply appended to the list, otherwise a
     * new {@link ScheduleForDay} instance is created. If this was the only dose on a given day, its original
     * ScheduleForDay is removed.
     * @param   changedDate
     *          the {@link ScheduleForDay} to which this Dose belongs.
     * @param   changedDose
     *          a {@link Dose} object which has its internal <code>tempDate</code> field changed and is contained in
     *          <code>changedDate</code>
     */
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

    /**
     * Removes a {@link Dose} from its {@link ScheduleForDay}. If this was the only dose scheduled on that day, the
     * ScheduleForDay object is removed from this calendar too.
     * @param   changedDate
     *          a ScheduleForDay for which the <code>dose</code> belongs.
     * @param   dose
     *          a Dose to be removed, usually supplied by Flavour from an HTML input field.
     */
    public void removeDose(ScheduleForDay changedDate, Dose dose) {
        changedDate.removeDose(dose);
        if (changedDate.getDoses().isEmpty()) {
            removeDate(changedDate);
        }
    }


    /**
     * Removes all {@link Dose}s of a given {@link VaccineType} from this calendar, removing empty
     * {@link ScheduleForDay}s as needed.
     * @param   type
     *          a VaccineType to be purged from this calendar.
     */
    public void removeAllOfType(VaccineType type) {
        for (ScheduleForDay date : scheduledDates) {
            date.getDoses().removeIf(dose -> VaccineType.isSame(dose.getType(), type));
        }
        scheduledDates.removeIf(date -> date.getDoses().isEmpty());
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
            int[] dateOffsets = type.getDateOffsets();
            for (int i = 0; i < dateOffsets.length; i++) {
                int offsetInDays = dateOffsets[i];
                String variantName = type.getVariant(i);
                //when the dose should be administered
                String dateOfVaccination = startDate.addDays(offsetInDays).toString();

                if (mapOfAllVaccinationsOnGivenDates.containsKey(dateOfVaccination)) {
                    //something is already scheduled for this date
                    //therefore, we add this "dose" to the list instead of duplicating the date
                    List<Dose> vaccinesAtDate = mapOfAllVaccinationsOnGivenDates.get(dateOfVaccination);
                    vaccinesAtDate.add(new Dose(type, new TinyDate(dateOfVaccination), variantName));
                } else {
                    //nothing is scheduled for this date, yet
                    //create a new list and add this "dose" as its first item
                    List<Dose> vaccinesAtDate = new ArrayList<>();
                    vaccinesAtDate.add(new Dose(type, new TinyDate(dateOfVaccination), variantName));
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
