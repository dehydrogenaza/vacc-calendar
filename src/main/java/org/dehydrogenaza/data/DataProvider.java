package org.dehydrogenaza.data;

import org.dehydrogenaza.data.datasources.FakeVaccinationSource;
import org.dehydrogenaza.data.datasources.FreeVaccinationSource;
import org.dehydrogenaza.data.datasources.IVaccineSource;

import java.util.ArrayList;
import java.util.List;


/**
 * Mock implementation of the initial data provider. Creates and serves fake data, such as vaccine names, number of
 * vaccinations etc.
 * <p>Proper implementation will probably read the data from a JSON file. <strong>Nope, that costs a ton in terms of
 * JS size!</strong>
 * </strong></p>
 */
public class DataProvider {
    private IVaccineSource vaccinesSource;

    public DataProvider(IVaccineSource vaccinesSource) {
        this.vaccinesSource = vaccinesSource;
    }

    public List<VaccinationScheme> getSchemes() {
        List<VaccinationScheme> schemes = new ArrayList<>();
        schemes.add(new VaccinationScheme("Darmowe dla plebsu", 0, true));
        schemes.add(new VaccinationScheme("5-w-1", 1, false));
        schemes.add(new VaccinationScheme("Jestę antyszczepę", 2, false));
        schemes.add(new VaccinationScheme("JESTEM GUZIKIEM :O", 3, false));

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
        return vaccinesSource.getVaccines();
    }

    public void changeChosenVaccinationScheme(IVaccineSource newSource) {
        vaccinesSource = newSource;
    }

}
