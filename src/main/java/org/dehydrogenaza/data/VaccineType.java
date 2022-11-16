package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.RecommendationTableBox;

import java.util.List;

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
     * Recommended dates of administering individual doses, represented as <emphasis>offsets in days</emphasis> from
     * the start point (which is currently equal to the date of the first vaccination).
     * <p>An offset of "0" means that the given dose should be administered right at the start point.</p>
     */
    private final int[] dateOffsets;

    private final List<RecommendationTableBox> displayBoxes;

    /**
     * Is this vaccination selected by the user. <strong>This value is bound bidirectionally with an HTML element
     * </strong>.
     */
    private boolean selected;


    /**
     * Constructs a VaccineType given its full data.
     * @param   name
     *          name of the product.
     * @param   id
     *          unique numeric identifier.
     * @param   dateOffsets
     *          offsets of individual doses, in days.
     * @param   isSelected
     *          initial selection status.
     */
    public VaccineType(String name, String disease, int id, int[] dateOffsets, List<RecommendationTableBox> displayBoxes,
                       boolean isSelected) {
        this.name = name;
        this.disease = disease;
        this.id = id;
        this.dateOffsets = dateOffsets;
        this.displayBoxes = displayBoxes;
        this.selected = isSelected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<RecommendationTableBox> getBoxes() {
        return displayBoxes;
    }

    public static boolean isSame(VaccineType v1, VaccineType v2) {
        return v1.id == v2.id;
    }
}
