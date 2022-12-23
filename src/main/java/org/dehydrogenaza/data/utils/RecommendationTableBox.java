package org.dehydrogenaza.data.utils;

/**
 * Represents a "cell" in the visual schedule (Section 4). Each Box represents a single vaccine (one row) and may
 * span several columns. Boxes that are <code>filled</code> show the recommended date range for a given dose of a
 * vaccine, while boxes that are <code>!filled</code> represent gaps and won't be displayed in the table.
 */
public class RecommendationTableBox {
    /**
     * <code>true</code> if the box should be visible/colorful.
     */
    private final boolean filled;
    private final int span;
    /**
     * Name of the CSS class (most likely a <i>Bootstrap 5</i> class) used to represent this vaccine in the calendar.
     */
    private final String color;

    /**
     * Default constructor.
     * @param   filled
     *          <code>true</code> if this Box should be visible/colorful.
     * @param   span
     *          how many columns of the Schedule table should this Box take.
     * @param   color
     *          name(s) of the CSS class(es) used for styling (when this Box is visible).
     */
    public RecommendationTableBox(boolean filled, int span, String color) {
        this.filled = filled;
        this.span = span;
        this.color = color;
    }

    /**
     * @return
     *          <code>true</code> if this Box should be visible/colorful in the Schedule table.
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * @return
     *          the number of columns in the Schedule table taken by this Box.
     */
    public int getSpan() {
        return span;
    }

    /**
     * @return
     *          the name(s) of the CSS class(es) used for styling (when this Box is visible).
     */
    public String getColor() {
        return color;
    }
}
