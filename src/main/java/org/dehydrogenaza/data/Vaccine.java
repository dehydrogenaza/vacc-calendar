package org.dehydrogenaza.data;

public class Vaccine {

    private final String name;
    private final int id;
    private boolean isSelected;

    public Vaccine(String name, int id, boolean isSelected) {
        this.name = name;
        this.id = id;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
