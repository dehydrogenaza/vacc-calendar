package org.dehydrogenaza.data;

import java.util.ArrayList;
import java.util.List;


/**
 * Mock implementation of the initial data provider. Creates and serves fake data, such as vaccine names, number of
 * vaccinations etc.
 * <p>Proper implementation will probably read the data from a JSON file.</p>
 */
public class DataSource {


    /**
     * Gets the list of all potential {@link VaccineType}s. <strong>The list is mutable.</strong> It's used for
     * displaying the vaccine selection, the results (suggested vaccination dates), as well as tracking the selection
     * toggle.
     * @return
     *          all supported vaccines.
     */
    public List<VaccineType> getVaccines() {
        List<VaccineType> vaccines = new ArrayList<>();
        int[] offset1 = {0, 5, 10, 15};
        int[] offset2 = {0, 3, 9, 17, 25};
        int[] offset3 = {2, 4, 6, 100, 2000};
        int[] offset4 = {10, 365};
        int[] offset5 = {7};
        vaccines.add(new VaccineType("rtęć",                  0, offset1, Math.random() > 0.5));
        vaccines.add(new VaccineType("autyzm",                1, offset2, Math.random() > 0.5));
        vaccines.add(new VaccineType("czip od Billa Gatesa",  2, offset3, Math.random() > 0.5));
        vaccines.add(new VaccineType("chip od Sorosa",        3, offset4, Math.random() > 0.5));
        vaccines.add(new VaccineType("NOP",                   4, offset5, Math.random() > 0.5));
        vaccines.add(new VaccineType("sok z buraka",          5, offset1, Math.random() > 0.5));
        vaccines.add(new VaccineType("darwinizm",             6, offset2, Math.random() > 0.5));
        vaccines.add(new VaccineType("niebinarność",          7, offset3, Math.random() > 0.5));
        vaccines.add(new VaccineType("leworęczność",          8, offset4, Math.random() > 0.5));
        vaccines.add(new VaccineType("wiedźmiństwo",          9, offset5, Math.random() > 0.5));
        vaccines.add(new VaccineType("piśmienność",           10, offset1, Math.random() > 0.5));

        return vaccines;
    }

    public List<VaccinationScheme> getSchemes() {
        List<VaccinationScheme> schemes = new ArrayList<>();
        schemes.add(new VaccinationScheme("Darmowe dla plebsu", 0, true));
        schemes.add(new VaccinationScheme("5-w-1", 1, false));
        schemes.add(new VaccinationScheme("Jestę antyszczepę", 2, false));
        schemes.add(new VaccinationScheme("JESTEM GUZIKIEM :O", 3, false));

        return schemes;
    }
}
