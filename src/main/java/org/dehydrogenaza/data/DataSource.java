package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.TableBox;

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
        vaccines.add(new VaccineType("rtęć",                  "choroba 0", 0,
                offset1, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("autyzm",                "choroba 1", 1,
                offset2, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("czip od Billa Gatesa",  "choroba 2", 2,
                offset3, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("chip od Sorosa",        "choroba 3", 3,
                offset4, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("NOP",                   "choroba 4", 4,
                offset5, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("sok z buraka",          "choroba 5", 5,
                offset1, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("darwinizm",             "choroba 6", 6,
                offset2, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("niebinarność",          "choroba 7", 7,
                offset3, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("leworęczność",          "choroba 8", 8,
                offset4, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("wiedźmiństwo",          "choroba 9", 9,
                offset5, displayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("piśmienność",           "choroba 10", 10,
                offset1, displayBoxes(), Math.random() > 0.5));

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

    private List<TableBox> displayBoxes() {
        List<TableBox> boxes = new ArrayList<>();

        if (Math.random() > 0.5) {
            for (int i = 0; i < 11; i++) {
                boolean filled = Math.random() > 0.5;
                boxes.add(new TableBox(filled, 1, "text-bg-danger"));
            }
        } else if (Math.random() > 0.5) {
            boxes.add(new TableBox(false, 2, ""));
            boxes.add(new TableBox(true, 3, "text-bg-success"));
            boxes.add(new TableBox(false, 1, ""));
            boxes.add(new TableBox(true, 1, "text-bg-warning"));
            boxes.add(new TableBox(false, 2, ""));
            boxes.add(new TableBox(true, 2, "text-bg-success"));
        } else {
            boxes.add(new TableBox(true, 11, "text-bg-info"));
        }

        return boxes;
    }
}
