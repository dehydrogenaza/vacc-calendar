package org.dehydrogenaza.data;

public class Vaccine {

    private final String name;
    private final int id;

    private final int[] dateOffsets;
    private boolean isSelected;

    public Vaccine(String name, int id, int[] dateOffsets, boolean isSelected) {
        this.name = name;
        this.id = id;
        this.dateOffsets = dateOffsets;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int[] getDateOffsets() {
        return dateOffsets;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
