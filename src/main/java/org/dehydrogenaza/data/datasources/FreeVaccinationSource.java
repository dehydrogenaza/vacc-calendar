package org.dehydrogenaza.data.datasources;

import org.dehydrogenaza.data.VaccineType;
import org.dehydrogenaza.data.utils.RecommendationTableBox;

import java.util.ArrayList;
import java.util.List;

public class FreeVaccinationSource implements IVaccineSource {
    private static final int WITHIN_24H = 0;
    private static final int VISIT_6_WEEKS = 42;
    private static final int VISIT_2TO3_MONTHS = 72;
    private static final int VISIT_3TO4_MONTHS = 102;
    private static final int VISIT_4TO5_MONTHS = 132;
    private static final int VISIT_5TO6_MONTHS = 162;
    private static final int VISIT_13_MONTHS = 390;
    private static final int VISIT_16_MONTHS = 480;
    private static final int VISIT_6_YEARS = 2190;

    @Override
    public List<VaccineType> getVaccines() {
        List<VaccineType> vaccines = new ArrayList<>();

        //MANDATORY
        VaccineType bcg = new VaccineType.Builder()
                .withDisease("Gruźlica")
                .withDateOffsets(WITHIN_24H)
                .withDisplayBoxes(getDisplayBoxes())
                .create("BCG", true);
        VaccineType hbv = new VaccineType.Builder()
                .withDisease("Wirusowe Zapalenie Wątroby typu B")
                .withDateOffsets(WITHIN_24H, VISIT_6_WEEKS, 210)
                .withDisplayBoxes(getDisplayBoxes())
                .create("HBV", true);
        VaccineType dtp = new VaccineType.Builder()
                .withDisease("Błonica, tężec, krztusiec")
                .withDateOffsets(VISIT_6_WEEKS, VISIT_3TO4_MONTHS, VISIT_5TO6_MONTHS,
                        VISIT_16_MONTHS, VISIT_6_YEARS, 5110, 6935)
                .withVariantNames("DTPw", "DTPw", "DTPw", "DTPw", "DTaP", "dTpa", "Td")
                .withDisplayBoxes(getDisplayBoxes())
                .create("DTP", true);
        VaccineType ipv = new VaccineType.Builder()
                .withDisease("Polio (Heinego-Medina)")
                .withDateOffsets(VISIT_3TO4_MONTHS, VISIT_5TO6_MONTHS, VISIT_16_MONTHS, VISIT_6_YEARS)
                .withDisplayBoxes(getDisplayBoxes())
                .create("IPV", true);
        VaccineType hib = new VaccineType.Builder()
                .withDisease("Haemophilus influenzae typu B")
                .withDateOffsets(VISIT_6_WEEKS, VISIT_3TO4_MONTHS, VISIT_5TO6_MONTHS, VISIT_16_MONTHS)
                .withDisplayBoxes(getDisplayBoxes())
                .create("Hib", true);
        // TODO: only children born after X year
        VaccineType pcv = new VaccineType.Builder()
                .withDisease("Pneumokoki")
                .withDateOffsets(VISIT_6_WEEKS, VISIT_3TO4_MONTHS, VISIT_13_MONTHS)
                .withDisplayBoxes(getDisplayBoxes())
                .create("PCV", true);
        // TODO: only children born after X year
        // TODO: exists in either 2 or 3 dose variants
        VaccineType rv = new VaccineType.Builder()
                .withDisease("Rotawirusy")
                .withDateOffsets(VISIT_6_WEEKS, VISIT_3TO4_MONTHS, VISIT_5TO6_MONTHS)
                .withVariantNames("RV", "RV", "RV3")
                .withDisplayBoxes(getDisplayBoxes())
                .create("RV", true);
        // TODO: 2 doses, 390/2190 for children born >= 2013(?), 390/3650 for children born < 2013
        VaccineType mmr = new VaccineType.Builder()
                .withDisease("Odra, świnka, różyczka")
                .withDateOffsets(VISIT_13_MONTHS, VISIT_6_YEARS)
                .withDisplayBoxes(getDisplayBoxes())
                .create("MMR", true);
        //OPT-IN
        // TODO: not recommended to do menB + menC/ACWY simultaneously, add ~2 weeks gap (add to other schedules)
        int[] menBOffsetsNormal = {VISIT_2TO3_MONTHS, VISIT_3TO4_MONTHS, VISIT_4TO5_MONTHS, VISIT_13_MONTHS};
        int[] menBOffsetsDelayed = {VISIT_3TO4_MONTHS, VISIT_4TO5_MONTHS, VISIT_5TO6_MONTHS, VISIT_13_MONTHS};
        VaccineType menb = new VaccineType.Builder()
                .withDisease("Meningokoki grupy B")
                .withDateOffsets(menBOffsetsNormal)
                .withDisplayBoxes(getDisplayBoxes())
                .create("MenB", false);
        VaccineType menc = new VaccineType.Builder()
                .withDisease("Meningokoki grupy C")
                .withDateOffsets(VISIT_2TO3_MONTHS, VISIT_4TO5_MONTHS, VISIT_13_MONTHS)
                .withDisplayBoxes(getDisplayBoxes())
                .create("MenC", false);
        menc.addRelationship(menb, vaxMenB -> {
            if (menc.isSelected()) vaxMenB.setDateOffsets(menBOffsetsDelayed);
            else vaxMenB.setDateOffsets(menBOffsetsNormal);
        });
        VaccineType menacwy = new VaccineType.Builder()
                .withDisease("Meningokoki grup A, C, W, Y")
                .withDateOffsets(VISIT_2TO3_MONTHS, VISIT_4TO5_MONTHS, VISIT_13_MONTHS)
                .withDisplayBoxes(getDisplayBoxes())
                .create("MenACWY", false);

        vaccines.add(bcg);
        vaccines.add(hbv);
        vaccines.add(dtp);
        vaccines.add(ipv);
        vaccines.add(hib);
        vaccines.add(pcv);
        vaccines.add(rv);
        vaccines.add(mmr);
        vaccines.add(menb);
        vaccines.add(menc);
        vaccines.add(menacwy);

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
