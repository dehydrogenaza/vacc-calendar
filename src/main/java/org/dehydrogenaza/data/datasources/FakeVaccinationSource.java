package org.dehydrogenaza.data.datasources;

import org.dehydrogenaza.data.VaccineType;
import org.dehydrogenaza.data.utils.RecommendationTableBox;

import java.util.ArrayList;
import java.util.List;

public class FakeVaccinationSource implements IVaccineSource {

    @Override
    public List<VaccineType> getVaccines() {
        List<VaccineType> vaccines = new ArrayList<>();
        int[] offset1 = {0, 5, 10, 15};
        int[] offset2 = {0, 3, 9, 17, 25};
        int[] offset3 = {2, 4, 6, 100, 2000};
        int[] offset4 = {10, 365};
        int[] offset5 = {7};
        VaccineType test0 = new VaccineType.Builder()
                .withDisease("choroba 0")
                .withDateOffsets(offset1)
                .withDisplayBoxes(getDisplayBoxes())
                .create("rtęć", Math.random() > 0.5);
        vaccines.add(test0);
        VaccineType test1 = new VaccineType.Builder()
                .withDisease("choroba 1")
                .withDateOffsets(offset2)
                .withDisplayBoxes(getDisplayBoxes())
                .create("autyzm", Math.random() > 0.5);
        vaccines.add(test1);
        VaccineType test2 = new VaccineType.Builder()
                .withDisease("choroba 2")
                .withDateOffsets(offset3)
                .withDisplayBoxes(getDisplayBoxes())
                .create("czip od Billa Gatesa", Math.random() > 0.5);
        vaccines.add(test2);
        VaccineType test3 = new VaccineType.Builder()
                .withDisease("choroba 3")
                .withDateOffsets(offset4)
                .withDisplayBoxes(getDisplayBoxes())
                .create("chip od Sorosa", Math.random() > 0.5);
        vaccines.add(test3);
        VaccineType test4 = new VaccineType.Builder()
                .withDisease("choroba 4")
                .withDateOffsets(offset5)
                .withDisplayBoxes(getDisplayBoxes())
                .create("NOP", Math.random() > 0.5);
        vaccines.add(test4);
        VaccineType test5 = new VaccineType.Builder()
                .withDisease("choroba 5")
                .withDateOffsets(offset1)
                .withDisplayBoxes(getDisplayBoxes())
                .create("sok z buraka", Math.random() > 0.5);
        vaccines.add(test5);
        VaccineType test6 = new VaccineType.Builder()
                .withDisease("choroba 6")
                .withDateOffsets(offset2)
                .withDisplayBoxes(getDisplayBoxes())
                .create("darwinizm", Math.random() > 0.5);
        vaccines.add(test6);
        VaccineType test7 = new VaccineType.Builder()
                .withDisease("choroba 7")
                .withDateOffsets(offset3)
                .withDisplayBoxes(getDisplayBoxes())
                .create("niebinarność", Math.random() > 0.5);
        vaccines.add(test7);
        VaccineType test8 = new VaccineType.Builder()
                .withDisease("choroba 8")
                .withDateOffsets(offset4)
                .withDisplayBoxes(getDisplayBoxes())
                .create("leworęczność", Math.random() > 0.5);
        vaccines.add(test8);
        VaccineType test9 = new VaccineType.Builder()
                .withDisease("choroba 9")
                .withDateOffsets(offset5)
                .withDisplayBoxes(getDisplayBoxes())
                .create("wiedźmiństwo", Math.random() > 0.5);
        vaccines.add(test9);
        VaccineType test10 = new VaccineType.Builder()
                .withDisease("choroba 10")
                .withDateOffsets(offset1)
                .withDisplayBoxes(getDisplayBoxes())
                .create("piśmienność", Math.random() > 0.5);
        vaccines.add(test10);

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
