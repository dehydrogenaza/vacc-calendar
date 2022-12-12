package org.dehydrogenaza.data.datasources;

import org.dehydrogenaza.data.VaccineType;
import org.dehydrogenaza.data.utils.RecommendationTableBox;

import java.util.ArrayList;
import java.util.List;

public class FreeVaccinationSource implements IVaccineSource {

    @Override
    public List<VaccineType> getVaccines() {
        List<VaccineType> vaccines = new ArrayList<>();

        VaccineType bcg = new VaccineType.Builder()
                .withDisease("Gruźlica")
                .withDateOffsets(0)
                .withDisplayBoxes(getDisplayBoxes())
                .create("BCG", true);
        VaccineType hbv = new VaccineType.Builder()
                .withDisease("Wirusowe Zapalenie Wątroby typu B")
                .withDateOffsets(0, 42, 180)
                .withDisplayBoxes(getDisplayBoxes())
                .create("BCG", true);
        VaccineType dtpw = new VaccineType.Builder()
                .withDisease("Błonica, tężec, krztusiec (szczepionka całokomórkowa)")
                .withDateOffsets(42, 102, 162, 480)
                .withDisplayBoxes(getDisplayBoxes())
                .create("DTPw", true);
        VaccineType dtap = new VaccineType.Builder()
                .withDisease("Błonica, tężec, krztusiec (szczepionka bezkomórkowa)")
                .withDateOffsets(2190)
                .withDisplayBoxes(getDisplayBoxes())
                .create("DTaP", true);
        VaccineType dtpa = new VaccineType.Builder()
                .withDisease("Błonica, tężec, krztusiec (szczepionka zawierająca toksoid tężcowy, zmniejszoną dawkę " +
                        "toksoidu błoniczego i bezkomórkowe komponenty krztuśca)")
                .withDateOffsets(5110)
                .withDisplayBoxes(getDisplayBoxes())
                .create("dTpa", true);
        VaccineType td = new VaccineType.Builder()
                .withDisease("Błonica, tężec")
                .withDateOffsets(6935)
                .withDisplayBoxes(getDisplayBoxes())
                .create("Td", true);

        vaccines.add(bcg);
        vaccines.add(hbv);
        vaccines.add(dtpw);
        vaccines.add(dtap);
        vaccines.add(dtpa);
        vaccines.add(td);


        return vaccines;
    }


    private List<RecommendationTableBox> getDisplayBoxes() {
        List<RecommendationTableBox> boxes = new ArrayList<>();

        if (Math.random() > 0.5) {
            for (int i = 0; i < 11; i++) {
                boolean filled = Math.random() > 0.5;
                boxes.add(new RecommendationTableBox(filled, 1, "text-bg-danger"));
            }
        } else if (Math.random() > 0.5) {
            boxes.add(new RecommendationTableBox(false, 2, ""));
            boxes.add(new RecommendationTableBox(true, 3, "text-bg-success"));
            boxes.add(new RecommendationTableBox(false, 1, ""));
            boxes.add(new RecommendationTableBox(true, 1, "text-bg-warning"));
            boxes.add(new RecommendationTableBox(false, 2, ""));
            boxes.add(new RecommendationTableBox(true, 2, "text-bg-success"));
        } else {
            boxes.add(new RecommendationTableBox(true, 11, "text-bg-info"));
        }

        return boxes;
    }
}
