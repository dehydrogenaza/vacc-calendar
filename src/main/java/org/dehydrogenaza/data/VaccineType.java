package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.RecommendationTableBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A <strong>VaccineType</strong> represents all the data associated with a single type of vaccination, such as the name
 * of the product, its numeric identifier, number of doses and the recommended delay between doses.
 * <p>VaccineType objects also track whether the user chose to add the given vaccination to their calendar.</p>
 */
public class VaccineType {

    /**
     * Name of the product.
     */
    private final String name;

    private final String disease;

    /**
     * Unique number, for internal identification, not visible to the end user.
     */
    private final int id;

    /**
     * Recommended dates of administering individual doses, represented as <emphasis>offsets in days</emphasis> from the
     * start point (which is currently equal to the date of the first vaccination).
     * <p>An offset of "0" means that the given dose should be administered right at the start point.</p>
     */
    private int[] dateOffsets;

    private final String[] variantNames; //can be null

    private final List<RecommendationTableBox> displayBoxes;

    //private final Map<VaccineType[], Consumer<VaccineType[]>> vaccineSelectionHandlers;
    private final List<Runnable> vaccineSelectionHandlers;

    /**
     * Is this vaccination selected by the user. <strong>This value is bound bidirectionally with an HTML element
     * </strong>.
     */
    private boolean selected;


    /**
     * Constructs a VaccineType based on Builder data.
     *
     * @param builder the internal Builder instance that takes care of this object's initialization.
     */
    private VaccineType(Builder builder) {
        this.name = builder.name;
        this.disease = builder.disease;
        this.id = builder.id;
        this.dateOffsets = builder.dateOffsets;
        this.displayBoxes = builder.displayBoxes;
        this.variantNames = builder.variantNames;
        this.vaccineSelectionHandlers = builder.vaccineSelectionHandlers;
        this.selected = builder.selected;
    }

    public static class Builder {
        private static int currentID = 0;
        private String name;
        private String disease;
        private int id;
        private int[] dateOffsets;
        private String[] variantNames;
        private List<RecommendationTableBox> displayBoxes;
        //        private Map<VaccineType[], Consumer<VaccineType[]>> vaccineSelectionHandlers;
        private List<Runnable> vaccineSelectionHandlers;
        private boolean selected;

        public Builder withDisease(String diseaseName) {
            this.disease = diseaseName;
            return this;
        }

        public Builder withDateOffsets(int... dateOffsets) {
            this.dateOffsets = dateOffsets;
            return this;
        }

        public Builder withVariantNames(String... variantNames) {
            this.variantNames = variantNames;
            return this;
        }

        public Builder withDisplayBoxes(List<RecommendationTableBox> displayBoxes) {
            this.displayBoxes = displayBoxes;
            return this;
        }

        public VaccineType create(String name, boolean selected) {
            this.name = name;
            if (this.disease == null) this.disease = "";
            this.id = currentID++;
            if (this.dateOffsets == null) this.dateOffsets = new int[]{0};
            if (this.displayBoxes == null) this.displayBoxes = new ArrayList<>();
            this.vaccineSelectionHandlers = new ArrayList<>();
            this.selected = selected;
            return new VaccineType(this);
        }
    }

    //    public void addSelectionHandler(Consumer<VaccineType[]> action, VaccineType... vax) {
//        vaccineSelectionHandlers.put(vax, action);
//    }
    public void addSelectionHandler(Runnable action) {
        vaccineSelectionHandlers.add(action);
    }

    public String getName() {
        return name;
    }

    public String getDisease() {
        return disease;
    }

    public int getId() {
        return id;
    }

    public int[] getDateOffsets() {
        return dateOffsets;
    }

    public void setDateOffsets(int... dateOffsets) {
        this.dateOffsets = dateOffsets;
    }

    public String getVariant(int index) {
        if (variantNames == null) return name;
        return variantNames[index];
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        applySelectionHandlers();
    }

    public List<RecommendationTableBox> getBoxes() {
        return displayBoxes;
    }

    public static boolean isSame(VaccineType v1, VaccineType v2) {
        return v1.id == v2.id;
    }

    //    private void applySelectionHandlers() {
//        vaccineSelectionHandlers.forEach((vaxArray, action) -> action.accept(vaxArray));
//    }
    private void applySelectionHandlers() {
        vaccineSelectionHandlers.forEach(Runnable::run);
    }
}
