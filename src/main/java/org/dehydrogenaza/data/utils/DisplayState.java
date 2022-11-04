package org.dehydrogenaza.data.utils;


/**
 * Signifies the current internal status of the webapp, which in turn determines which DOM elements should be
 * visible/active. This might eventually get replaced by proper routing, once I figure out how routing works in Flavour.
 */
public enum DisplayState {

    /**
     * User is inputting data.
     */
    FORM,

    /**
     * Proper data was submitted, results are displayed.
     */
    CALENDAR,

    /**
     * Invalid data was submitted, UI shows what went wrong.
     */
    BAD_SUBMIT
}
