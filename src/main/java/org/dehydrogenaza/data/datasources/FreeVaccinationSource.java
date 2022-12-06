package org.dehydrogenaza.data.datasources;

import org.dehydrogenaza.data.VaccineType;
import org.dehydrogenaza.data.utils.RecommendationTableBox;

import java.util.ArrayList;
import java.util.List;

public class FreeVaccinationSource implements IVaccineSource {

    @Override
    public List<VaccineType> getVaccines() {
        List<VaccineType> vaccines = new ArrayList<>();

        int[] offset1 = {0, 365, 5000};
        int[] offset2 = {3, 17, 61};
        int[] offset3 = {3, 4, 10, 20};
        int[] offset4 = {10, 365};

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
        vaccines.add(bcg);
        vaccines.add(hbv);

//        vaccines.add(new VaccineType("AAA", "choroba A", 0, offset1, getDisplayBoxes(), Math.random() > 0.5));
//        vaccines.add(new VaccineType("BBB", "choroba B", 1, offset2, getDisplayBoxes(), Math.random() > 0.5));
//        vaccines.add(new VaccineType("CCC", "choroba C", 2, offset3, getDisplayBoxes(), Math.random() > 0.5));
//        vaccines.add(new VaccineType("DDD", "choroba D", 3, offset4, getDisplayBoxes(), Math.random() > 0.5));
//        vaccines.add(new VaccineType("EEE", "choroba E", 4, offset2, getDisplayBoxes(), Math.random() > 0.5));

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
