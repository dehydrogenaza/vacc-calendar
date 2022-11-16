package org.dehydrogenaza.data.datasources;

import org.dehydrogenaza.data.VaccineType;
import org.dehydrogenaza.data.utils.RecommendationTableBox;

import java.util.ArrayList;
import java.util.List;

public class FakeVaccinationSource implements IVaccineSource{

    @Override
    public List<VaccineType> getVaccines() {
        List<VaccineType> vaccines = new ArrayList<>();
        int[] offset1 = {0, 5, 10, 15};
        int[] offset2 = {0, 3, 9, 17, 25};
        int[] offset3 = {2, 4, 6, 100, 2000};
        int[] offset4 = {10, 365};
        int[] offset5 = {7};
        vaccines.add(new VaccineType("rtęć",                  "choroba 0", 0,
                offset1, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("autyzm",                "choroba 1", 1,
                offset2, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("czip od Billa Gatesa",  "choroba 2", 2,
                offset3, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("chip od Sorosa",        "choroba 3", 3,
                offset4, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("NOP",                   "choroba 4", 4,
                offset5, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("sok z buraka",          "choroba 5", 5,
                offset1, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("darwinizm",             "choroba 6", 6,
                offset2, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("niebinarność",          "choroba 7", 7,
                offset3, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("leworęczność",          "choroba 8", 8,
                offset4, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("wiedźmiństwo",          "choroba 9", 9,
                offset5, getDisplayBoxes(), Math.random() > 0.5));
        vaccines.add(new VaccineType("piśmienność",           "choroba 10", 10,
                offset1, getDisplayBoxes(), Math.random() > 0.5));

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
