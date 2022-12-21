package org.dehydrogenaza.data.utils;

import org.dehydrogenaza.Client;
import org.dehydrogenaza.data.Dose;
import org.dehydrogenaza.data.ScheduleForDay;

import java.util.List;

/**
 * Utility class which provides a CSV export capability, allowing users to generate and download their schedules as
 * files.
 */
public class CSVWriter {
    /**
     * HTML encoding of a newline character.
     */
    private static final String ESCAPED_NEWLINE = "%0D%0A";
    /**
     * HTML encoding of ' ' (space).
     */
    private static final String ESCAPED_SPACE = "%20";
    /**
     * HTML encoding of a " (double quote) character.
     */
    private static final String ESCAPED_QUOTE = "%22";
    /**
     * Common header for CSV files, containing the names of columns.
     */
    private static final String CSV_HEADER = "Subject,Start Date,Description" + ESCAPED_NEWLINE;
    /**
     * Common beginning of all URI strings encoding CSV files.
     */
    private static final String URI_SCHEME = "data:text/csv;charset=utf-8,";

    /**
     * Generates and returns a properly formatted URI String encoding the schedule (vaccination dates) as a
     * downloadable CSV file. This URI is then supplied (through the {@link Client#getCSVExportURI()} method) as the
     * <i>href</i> attribute for the "Export to file" button.
     * @param   dates
     *          A list of {@link ScheduleForDay}s, representing the current vaccination schedule (calendar).
     * @return
     *          A CSV file encoded as a URI.
     */
    public static String getDataURI(List<ScheduleForDay> dates) {
        String csvText = makeRawCSV(dates);
        return URI_SCHEME + escapeSpecialChars(csvText);
    }

    // TODO: Check if we can use StringBuilder, it might already be "free".

    /**
     * Creates a "raw" CSV file that's not yet suited to be used in a URI because it may (and probably will) contain
     * special characters that need to be escaped accordingly for HTML.
     *
     * <p>Note: intentionally not using StringBuilder to save some JS output size.</p>
     * @param   dates
     *          A list of {@link ScheduleForDay}s, representing the current vaccination schedule (calendar).
     * @return
     *          A CSV file as a single <code>String</code>.
     */
    private static String makeRawCSV(List<ScheduleForDay> dates) {
        String csv = CSV_HEADER;

        for (ScheduleForDay date : dates) {
            String description = "";
            for (Dose dose : date.getDoses()) {
                description += dose.getType().getName() + "\n";
            }

            String csvLine = "Szczepienie," + date.getDate() + ",\"" + description + "\"\n";
            csv += csvLine;
        }

        return csv;
    }

    /**
     * Helper method which scans the CSV string and replaces relevant HTML special characters with their escaped
     * versions. Works for: '<b>\n</b>' (newline), ' ' (space) and '<b>\</b>' (backslash), which show up in CSV but
     * would break the URI.
     * @param   rawText
     *          An unescaped string.
     * @return
     *          A string with newlines, spaces and backslashes replaced with HTML-escaped codes.
     */
    private static String escapeSpecialChars(String rawText) {
        String outputText = "";

        for (int i = 0; i < rawText.length(); i++) {
            char currentChar = rawText.charAt(i);
            switch (currentChar) {
                case '\n':
                    outputText += ESCAPED_NEWLINE;
                    break;
                case ' ':
                    outputText += ESCAPED_SPACE;
                    break;
                case '\"':
                    outputText += ESCAPED_QUOTE;
                    break;
                default:
                    outputText += currentChar;
            }
        }

        return outputText;
    }
}
