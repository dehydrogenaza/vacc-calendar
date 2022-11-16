package org.dehydrogenaza.data.utils;

import org.dehydrogenaza.data.Dose;
import org.dehydrogenaza.data.ScheduleForDay;

import java.util.List;

public class CSVWriter {
    private static final String ESCAPED_NEWLINE = "%0D%0A";
    private static final String ESCAPED_SPACE = "%20";
    private static final String ESCAPED_QUOTE = "%22";
    private static final String CSV_HEADER = "Subject,Start Date,Description" + ESCAPED_NEWLINE;

    public static String getDataURI(List<ScheduleForDay> dates) {
        String csvText = makeRawCSV(dates);
        return "data:text/csv;charset=utf-8," + escapeSpecialChars(csvText);
    }

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
