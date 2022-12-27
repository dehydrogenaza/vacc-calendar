package org.dehydrogenaza.data;

/**
 * Represents basic information about a "scheme" or "plan" of vaccinations. Examples are: the free, government-funded
 * plan, or the plan utilizing the 6-in-1 DTP+IPV+Hib+WZW polyvalent vaccine.
 * <p>VaccinationScheme objects do <strong>not</strong> hold actual vaccination data (that is relegated to
 * {@link VaccineType} objects, which can be shared across VaccinationSchemes), only some basic information related
 * to the radio selector in Section 1 of the input form.</p>
 * <p>However, the choice of VaccinationScheme decides the kind of vaccination data returned by the
 * {@link DataProvider}.</p>
 */
public class VaccinationScheme {
    /**
     * The display name of this Scheme.
     */
    private final String name;
    /**
     * The ID used by the radio selector in Section 1 of HTML.
     */
    private final int id;

    // TODO: This might actually not be necessary, since we're calling the setChosenScheme() method in the Client
    //  anyway in order to handle the selection... I only implemented this because that's the way I handled similar
    //  inputs earlier, but in this case it's probably just dead code.
    /**
     * <strong>Bidirectionally bound to an HTML radio selector.</strong> Indicates whether this scheme is currently
     * selected by the user.
     */
    private boolean checked;

    // TODO: Apply some abstraction to the construction process, to make sure that the "id" and "checked" fields have
    //  proper values.
    /**
     * Constructs a Scheme. No two Schemes should be simultaneously "checked", but for now the constructor doesn't
     * check it in any way, nor does it check that the IDs are unique as they should.
     * <p>The list of available VaccinationSchemes is built by the {@link DataProvider}.</p>
     * @param   name
     *          a display name.
     * @param   id
     *          a unique ID, zero-indexed.
     * @param   checked
     *          whether this Scheme starts as the default.
     */
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

    /**
     * <strong>Bidirectionally bound to an HTML radio selector.</strong> Invoked when the user switches to this Scheme.
     * @param   checked
     *          current selection status of this Scheme, based on user input.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * <strong>Bidirectionally bound to an HTML radio selector.</strong> Indicates whether this Scheme should be
     * displayed as selected.
     * @return
     *          <code>true</code> if this Scheme is currently selected, <code>false</code> otherwise.
     */
    public boolean isChecked() {
        return checked;
    }
}
