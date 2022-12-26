package org.dehydrogenaza.data;

import org.dehydrogenaza.data.datasources.IVaccineSource;

import java.util.ArrayList;
import java.util.List;


/**
 * Mock implementation of the initial data provider. Creates and serves fake data, such as vaccine names, number of
 * vaccinations etc.
 * <p><s>Proper implementation will probably read the data from a JSON file.</s> <strong>Nope, that costs a ton in terms of
 * JS size!</strong>
 * </strong></p>
 */
public class DataProvider {
    /**
     * Currently used Source of vaccination data.
     */
    private IVaccineSource vaccinesSource;
    /**
     * All available vaccines in the currently selected vaccination plan. Includes defaults and optionals.
     */
    private final List<VaccineType> vaccines;
    /**
     * A list of all {@link VaccinationScheme}s that can be selected by the user. The schemes only *visually*
     * represent the vaccination plans and don't hold any vaccination data themselves (the data is provided by
     * {@link IVaccineSource}s instead).
     */
    private final List<VaccinationScheme> schemes;

    public DataProvider(IVaccineSource vaccinesSource) {
        // TODO: Should probably copy the list of vaccines from the source!

        this.vaccinesSource = vaccinesSource;
        vaccines = vaccinesSource.getVaccines();

        schemes = buildSchemesList();
    }


    //TODO: Switch to an immutable list (unmodifiableList).
    /**
     * Helper function for creating a list of vaccination "plan" names and basic data. An example of a "plan" would
     * be the government-funded free plan, or the plan which uses the paid 6-in-1 polyivalent DTP+IPV+Hib+WZW vaccine.
     * @return
     *          A list containing the supported {@link VaccinationScheme}s.
     */
    private List<VaccinationScheme> buildSchemesList() {
        List<VaccinationScheme> schemesList = new ArrayList<>();
        schemesList.add(new VaccinationScheme("Darmowe dla plebsu", 0, true));
        schemesList.add(new VaccinationScheme("5-w-1", 1, false));
        schemesList.add(new VaccinationScheme("Jestę antyszczepę", 2, false));
        schemesList.add(new VaccinationScheme("JESTEM GUZIKIEM :O", 3, false));

        return schemesList;
    }

    /**
     * Gets the list of all supported {@link VaccinationScheme}s. <strong>The list is currently mutable.</strong> It's
     * used for displaying the "vaccination plan" radio select group.
     * @return
     *          a list of all supported {@link VaccinationScheme}s.
     */
    public List<VaccinationScheme> getSchemes() {
        return schemes;
    }

    /**
     * Gets the list of all potential {@link VaccineType}s. <strong>The list is mutable.</strong> It's used for
     * displaying the vaccine selection, the results (suggested vaccination dates), as well as tracking the selection
     * toggle.
     * @return
     *          all supported vaccines.
     */
    public List<VaccineType> getVaccines() {
        return vaccines;
    }


    // TODO: Copy the list from the source. Make the original list immutable.
    /**
     * Updates the currently selected {@link VaccinationScheme}, which involves replacing the list of available
     * {@link VaccineType}s.
     * @param   newSource
     *          the newly selected source of data, corresponding to the chosen scheme.
     */
    public void changeChosenVaccinationScheme(IVaccineSource newSource) {
        vaccinesSource = newSource;

        vaccines.clear();
        vaccines.addAll(vaccinesSource.getVaccines());
    }

}
