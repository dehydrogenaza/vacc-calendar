package org.dehydrogenaza.data;

import org.dehydrogenaza.data.utils.RecommendationTableBox;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * The disease that this vaccine is against.
     */
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
    /**
     * Display names for individual (offset) doses. If it's null, the default {@link #name} is used instead.
     */
    private final String[] altNames; //nullable
    /**
     * Column setup for Section 4 of the HTML (recommended schedule).
     */
    private final List<RecommendationTableBox> displayBoxes;
    /**
     * Functions that run whenever this VaccineType is selected/deselected.
     */
    private final List<Runnable> vaccineSelectionHandlers;
    // TODO: Note, that the current setup won't work with "live" updating of input. If we need that,
    //  formDataHandlers need to run every time the form gets modified in some way.
    /**
     * Functions that run once when the form is submitted.
     */
    private final List<Consumer<Form>> formDataHandlers;

    /**
     * Is this vaccination selected by the user. <strong>This value is bound bidirectionally with an HTML element
     * </strong>.
     */
    private boolean selected;

    // TODO: Should probably include the recommended RANGE of dates: minimum/maximum??/optimal

    // TODO: Should include a description/tooltip for the end user

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
        this.altNames = builder.altNames;
        this.vaccineSelectionHandlers = builder.vaccineSelectionHandlers;
        this.formDataHandlers = builder.formDataHandlers;
        this.selected = builder.selected;
    }

    /**
     * Handles the construction of {@link VaccineType} instances.
     */
    public static class Builder {
        private static int currentID = 0;
        private String name;
        private String disease;
        private int id;
        private int[] dateOffsets;
        private String[] altNames;
        private List<RecommendationTableBox> displayBoxes;
        private List<Runnable> vaccineSelectionHandlers;
        private List<Consumer<Form>> formDataHandlers;
        private boolean selected;

        /**
         * Adds the name of the disease that this {@link VaccineType} works against.
         * @param   diseaseName
         *          a display name for the disease.
         * @return
         *          this Builder instance.
         */
        public Builder withDisease(String diseaseName) {
            this.disease = diseaseName;
            return this;
        }
        /**
         * Adds daily offsets of this {@link VaccineType}s doses. An offset of zero indicates that the dose should be
         * administered on the date of birth.
         * @param   dateOffsets
         *          any number of offsets (measured in days), each indicating a separate dose.
         * @return
         *          this Builder instance.
         */
        public Builder withDateOffsets(int... dateOffsets) {
            this.dateOffsets = dateOffsets;
            return this;
        }
        /**
         * Adds alternative/variant display names for the individual doses. If no alt names are provided <em>at
         * all</em>, the default {@link #name} will be used for every dose. However, if <em>any</em> alt names are
         * provided, they MUST match the number of {@link #dateOffsets}.
         * @param   altNames
         *          a number of variant display names, exactly one per offset (dose).
         * @return
         *          this Builder instance.
         */
        public Builder withAltNames(String... altNames) {
            this.altNames = altNames;
            return this;
        }
        /**
         * Adds {@link RecommendationTableBox}es which are used to set up the table in Section 4 of the HTML.
         * @param   displayBoxes
         *          a list of display boxes, with information on column setup for Section 4 of the HTML.
         * @return
         *          this Builder instance.
         */
        public Builder withDisplayBoxes(List<RecommendationTableBox> displayBoxes) {
            this.displayBoxes = displayBoxes;
            return this;
        }

        /**
         * Creates an instance of {@link VaccineType}. The ID is automatically incremented with each invocation. The
         * only nullable field is (by design) {@link #altNames}.
         * @param   name
         *          the name of this VaccineType.
         * @param   selected
         *          whether this VaccineType should be selected by default.
         * @return
         *          a properly initialized VaccineType object.
         */
        public VaccineType create(String name, boolean selected) {
            this.name = name;
            if (this.disease == null) this.disease = "";
            this.id = currentID++;
            if (this.dateOffsets == null) this.dateOffsets = new int[]{0};
            if (this.displayBoxes == null) this.displayBoxes = new ArrayList<>();
            this.vaccineSelectionHandlers = new ArrayList<>();
            this.formDataHandlers = new ArrayList<>();
            this.selected = selected;
            return new VaccineType(this);
        }
    }

    /**
     * Registers a function that will be executed whenever this VaccineType becomes selected/deselected. Does NOT
     * execute during instantiation - only when the selection changes from initial.
     * @param   handler
     *          a function for handling selection/deselection.
     */
    public void addSelectionHandler(Runnable handler) {
        vaccineSelectionHandlers.add(handler);
    }
    /**
     * Registers a function that will be executed once for this VaccineType when the input {@link Form} becomes
     * submitted.
     * @param   handler
     *          a function for handling Form submission.
     */
    public void addFormDataHandler(Consumer<Form> handler) {
        formDataHandlers.add(handler);
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

    /**
     * Returns an alt (variant) name for the indicated {@link #dateOffsets} index, if {@link #altNames} are available.
     * Otherwise, returns the default {@link #name}.
     * @param   index
     *          the index of the alt/variant name, corresponding to the index of the associated date offset (dose).
     * @return
     *          the display name for the indicated date offset.
     */
    public String getAltName(int index) {
        if (altNames == null) return name;
        return altNames[index];
    }

    /**
     * <strong>Bidirectionally bound to an HTML input check selector.</strong>
     * @return
     *          <code>true</code> if this VaccineType is currently selected by the user.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * <strong>Bidirectionally bound to an HTML input check selector.</strong>
     * @param   selected
     *          new status for this VaccineType, directly from user input.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        applySelectionHandlers();
    }

    public List<RecommendationTableBox> getBoxes() {
        return displayBoxes;
    }

    /**
     * Invokes every function registered as {@link #formDataHandlers} for this VaccineType.
     * @param   form
     *          the current input {@link Form}.
     */
    public void applyFormDataHandlers(Form form) {
        formDataHandlers.forEach(h -> h.accept(form));
    }

    /**
     * Invokes every function registered as {@link #vaccineSelectionHandlers} for this VaccineType.
     */
    private void applySelectionHandlers() {
        vaccineSelectionHandlers.forEach(Runnable::run);
    }

    // TODO: Probably not necessary in this version, just override .equals.
    /**
     * Utility method for quickly checking if two VaccineType instances refer to the same actual vaccine (by ID).
     * @param   v1
     *          first vaccine to compare.
     * @param   v2
     *          the other vaccine to compare.
     * @return
     *          <code>true</code> if the objects have the same ID.
     */
    public static boolean isSame(VaccineType v1, VaccineType v2) {
        return v1.id == v2.id;
    }
}
