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
                .withDateOffsets(0, 42, 210)
                .withDisplayBoxes(getDisplayBoxes())
                .create("HBV", true);
        VaccineType dtp = new VaccineType.Builder()
                .withDisease("Błonica, tężec, krztusiec")
                .withDateOffsets(42, 102, 162, 480, 2190, 5110, 6935)
                .withVariantNames("DTPw", "DTPw", "DTPw", "DTPw", "DTaP", "dTpa", "Td")
                .withDisplayBoxes(getDisplayBoxes())
                .create("DTP", true);
        VaccineType ipv = new VaccineType.Builder()
                .withDisease("Polio (Heinego-Medina)")
                .withDateOffsets(102, 162, 480, 2190)
                .withDisplayBoxes(getDisplayBoxes())
                .create("IPV", true);
        VaccineType hib = new VaccineType.Builder()
                .withDisease("Haemophilus influenzae typu B")
                .withDateOffsets(42, 102, 162, 480)
                .withDisplayBoxes(getDisplayBoxes())
                .create("Hib", true);
        // TODO: only children born after X year
        VaccineType pcv = new VaccineType.Builder()
                .withDisease("Pneumokoki")
                .withDateOffsets(42, 102, 390)
                .withDisplayBoxes(getDisplayBoxes())
                .create("PCV", true);
        // TODO: only children born after X year
        // TODO: exists in either 2 or 3 dose variants
        VaccineType rv = new VaccineType.Builder()
                .withDisease("Rotawirusy")
                .withDateOffsets(42, 102, 162)
                .withDisplayBoxes(getDisplayBoxes())
                .create("RV", true);
        // TODO: I found data for either 2 or 3 doses but they are inconsistent, what's actually recommended?
        VaccineType mmr = new VaccineType.Builder()
                .withDisease("Odra, świnka, różyczka")
                .withDateOffsets(390, 2190, 3650)
                .withDisplayBoxes(getDisplayBoxes())
                .create("MMR", true);

        vaccines.add(bcg);
        vaccines.add(hbv);
        vaccines.add(dtp);
        vaccines.add(ipv);
        vaccines.add(hib);
        vaccines.add(pcv);
        vaccines.add(rv);
        vaccines.add(mmr);

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
