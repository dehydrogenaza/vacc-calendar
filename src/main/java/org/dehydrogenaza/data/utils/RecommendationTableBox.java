package org.dehydrogenaza.data.utils;

/**
 * Represents a "cell" in the visual schedule (Section 4). Each Box represents a single vaccine (one row) and may
 * span several columns. Boxes that are <code>filled</code> show the recommended date range for a given dose of a
 * vaccine, while boxes that are <code>!filled</code> represent gaps and won't be displayed in the table.
 */
public class RecommendationTableBox {
    private final boolean filled;
    private final int span;
    /**
     * Name of the CSS class (possibly a <i>Bootstrap 5</i> class) used to represent this vaccine in the calendar.
     */
    private final String color;

    public RecommendationTableBox(boolean filled, int span, String color) {
        this.filled = filled;
        this.span = span;
        this.color = color;
    }

    public boolean isFilled() {
        return filled;
    }

    public int getSpan() {
        return span;
    }

    public String getColor() {
        return color;
    }
}
