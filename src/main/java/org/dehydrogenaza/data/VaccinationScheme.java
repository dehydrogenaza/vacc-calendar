package org.dehydrogenaza.data;

public class VaccinationScheme {

    private final String name;
    private final int id;
    private boolean checked;

    public VaccinationScheme(String name, int id, boolean checked) {
        this.name = name;
        this.id = id;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
