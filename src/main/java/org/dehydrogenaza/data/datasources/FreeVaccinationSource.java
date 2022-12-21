package org.dehydrogenaza.data.datasources;

import org.dehydrogenaza.data.VaccineType;
import org.dehydrogenaza.data.utils.RecommendationTableBox;
import org.dehydrogenaza.data.utils.TinyDate;

import java.util.ArrayList;
import java.util.List;

// TODO: Break this up into methods, extract common parts of different vaccination plans

// TODO: Add missing opt-in vaccines

/**
 * Supplies data (such as schedules, interdependencies between vaccines) for the "default", free mandatory vaccination
 * plan, financed by the Polish government, with optional "recommended" vaccinations. This plan uses
 * single-ingredient vaccines.
 * <p>
 * Note, that this data is intentionally hard-coded in a class instead of being kept in database. This approach
 * allows us to generate fully static JS that executes 100% in the user's browser. This is easier both from the
 * programming and server-management perspectives, and the downside is minimal, because this data is not dynamic (in
 * fact it's just a bunch of constants that are expected to have some slight updates AT MOST once per year).
 * </p><p>
 * Even though we're storing data in the JS script itself (instead of loading it asynchronously), I've tested that this
 * actually makes the script smaller, because we don't have to include any database-specific code.</p>
 */
public class FreeVaccinationSource implements IVaccineSource {
    //Offsets in days (starting from 0 = date of birth) for "default" recommended visits.
    private static final int WITHIN_24H = 0;
    private static final int VISIT_6_WEEKS = 42;
    private static final int VISIT_2TO3_MONTHS = 72;
    private static final int VISIT_3TO4_MONTHS = 102;
    private static final int VISIT_4TO5_MONTHS = 132;
    private static final int VISIT_5TO6_MONTHS = 162;
    private static final int VISIT_7TO8_MONTHS = 222;
    private static final int VISIT_13_MONTHS = 390;
    private static final int VISIT_16_MONTHS = 480;
    private static final int VISIT_6_YEARS = 2190;
    private static final int VISIT_10_YEARS = 3650;


    // TODO: Think on how to apply the Singleton pattern here.
    /**
     * Supplies data for this vaccination plan, which uses single-ingredient vaccines.
     * @return The list of {@link VaccineType}s in this plan with their default settings. Includes vaccines that are
     * optional and not selected by default.
     */
    @Override
    public List<VaccineType> getVaccines() {
        List<VaccineType> vaccines = new ArrayList<>();

        // MANDATORY VACCINES

        VaccineType bcg = new VaccineType.Builder()
                .withDisease("Gruźlica")
                .withDateOffsets(WITHIN_24H)
                .withDisplayBoxes(getDisplayBoxes())
                .create("BCG", true);
        VaccineType hbv = new VaccineType.Builder()
                .withDisease("Wirusowe Zapalenie Wątroby typu B")
                .withDateOffsets(WITHIN_24H, VISIT_6_WEEKS, VISIT_7TO8_MONTHS)
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
        // Recommended schedules depend on date of birth
        VaccineType mmr = new VaccineType.Builder()
                .withDisease("Odra, świnka, różyczka")
                .withDisplayBoxes(getDisplayBoxes())
                .create("MMR", true);

        // OPT-IN VACCINES

        // Not recommended to do menB + menC/ACWY simultaneously,
        // interdependency delays menB if both selected
        int[] menBOffsetsNormal = {VISIT_2TO3_MONTHS, VISIT_3TO4_MONTHS, VISIT_4TO5_MONTHS, VISIT_13_MONTHS};
        int[] menBOffsetsDelayed = {VISIT_3TO4_MONTHS, VISIT_5TO6_MONTHS, VISIT_7TO8_MONTHS, VISIT_13_MONTHS};
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
        VaccineType menacwy = new VaccineType.Builder()
                .withDisease("Meningokoki grup A, C, W, Y")
                .withDateOffsets(VISIT_2TO3_MONTHS, VISIT_4TO5_MONTHS, VISIT_13_MONTHS)
                .withDisplayBoxes(getDisplayBoxes())
                .create("MenACWY", false);

        // SETUP INTERDEPENDENCIES BETWEEN VACCINES

        // If menC is selected, deselect menACWY
        menc.addSelectionHandler(() -> {
            if (menc.isSelected()) menacwy.setSelected(false);
        });

        // If menACWY is selected, deselect menC
        menacwy.addSelectionHandler(() -> {
            if (menacwy.isSelected()) menc.setSelected(false);
        });

        // If either menC or menACWY is selected, use the delayed schedule for menB
        Runnable chooseMenBSchedule = () -> {
            if (menc.isSelected() || menacwy.isSelected()) menb.setDateOffsets(menBOffsetsDelayed);
            else menb.setDateOffsets(menBOffsetsNormal);
        };
        menc.addSelectionHandler(chooseMenBSchedule);
        menacwy.addSelectionHandler(chooseMenBSchedule);

        //SETUP VACCINES DEPENDENT ON INPUT DATA, for example child's date of birth

        // Ministry of Health changed recommendations for the second dose; applies to children born >= 2013
        mmr.addFormDataHandler(form -> {
            if (TinyDate.of(form.getDateOfBirth()).after("2013-01-01")) {
                mmr.setDateOffsets(VISIT_13_MONTHS, VISIT_6_YEARS);
            } else {
                mmr.setDateOffsets(VISIT_13_MONTHS, VISIT_10_YEARS);
            }
        });

        //Populate the list with ALL VaccineTypes (including ones that are optional/not selected)
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

// TODO: Figure out how Section 4 is going to work and rework this as needed.
    /**
     * Total placeholder, we don't even know how Section 4 is going to work yet.
     *
     * <p>Generates and returns data used in Section 4 to display a table of recommended date ranges for
     * a specific vaccine.</p>
     * @return  A list of {@link RecommendationTableBox}es representing this vaccine visually. Each box indicates
     * either an empty or filled range of columns in the schedule.
     */
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
