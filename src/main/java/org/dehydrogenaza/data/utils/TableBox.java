package org.dehydrogenaza.data.utils;

public class TableBox {

    private final boolean filled;
    private final int span;
    private final String color;

    public TableBox(boolean filled, int span, String color) {
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
