package org.dehydrogenaza.data.utils;

import org.dehydrogenaza.data.Form;

public class InputValidator {
    private static Form form;

    public static void init(Form form) {
        InputValidator.form = form;
    }

    public static boolean validate(String date) {
        if (date.isEmpty()) {
            return true;
        }

        String[] dateMinimums = new String[]{"1900-01-01", form.getDateOfBirth()};
        String[] dateMaximums = new String[]{"3000-12-31"};
        int dateAsNumber = TinyDate.of(date).asNumber();

        for (String min : dateMinimums) {
            if (dateAsNumber < TinyDate.of(min).asNumber()) {
                return false;
            }
        }
        for (String max : dateMaximums) {
            if (dateAsNumber > TinyDate.of(max).asNumber()) {
                return false;
            }
        }
        return true;
    }
}
