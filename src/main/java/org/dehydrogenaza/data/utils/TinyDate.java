package org.dehydrogenaza.data.utils;

/**
 * <strong>TinyDate</strong> provides a simple representation of a date, formatted as YYYY-MM-DD (which is
 * the standard used by web browsers for input fields).
 *
 * <p>Existing classes (LocalDate, Time etc.) have GIGANTIC dependency lists, which means they transpile to a massive
 * .js file. We don't need most of their functionality, and since we're making a web app (essentially a web page), we
 * want to keep our files small, so we're basically re-inventing the wheel here in order to save ~150 kB of resulting
 * JavaScript.</p>
 */
public class TinyDate {

    private final int year;
    private final int month;
    private final int day;

    /**
     * Maps the month to its length in days. <strong>The order (index) is important</strong> (for example, JANUARY at
     * index 1), because it's used to (also) map the month to its numeric representation. Which is a bit
     * <emphasis>hacky</emphasis>, but it works for our purposes.
     */
    private enum Month {
        INVALID(0),
        JANUARY(31),
        FEBRUARY(28),
        MARCH(31),
        APRIL(30),
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31);

        Month(final int daysIn) {
            this.daysInMonth = daysIn;
        }

        /**
         * The length of this month, in days, <strong>NOT</strong> accounting for leap years. For internal use.
         */
        private final int daysInMonth;


        /**
         * Gets the length of this month, in days, accounting for <emphasis>leap years</emphasis>.
         * @param   isLeapYear
         *          <code>true</code> for a leap (366 day) year, <code>false</code> for a normal (365 day) year.
         * @return
         *          the number of days in this month.
         */
        private int daysIn(boolean isLeapYear) {
            if (this == FEBRUARY && isLeapYear) {
                return daysInMonth + 1;
            }
            return daysInMonth;
        }


        /**
         * Calculates the number of days from a given day to the end of this month ("0" if it's the last day of the
         * month, "1" if there's one day left and so on). Accounts for leap years.
         * @param   currentDay
         *          the current day of the month.
         * @param   leapYear
         *          <code>true</code> for a leap (366 day) year, <code>false</code> for a normal (365 day) year.
         * @return
         *          the number of days left until the end of this month.
         */
        private int daysLeft(int currentDay, boolean leapYear) {
            return this.daysIn(leapYear) - currentDay;
        }

    }


    /**
     * Array of every Month enum field. Index 0 is reserved for "INVALID" months, index 1 is JANUARY etc. This array
     * is used to reference Months by their index, for example:
     * <br><code>monthsByValue[2] == Month.FEBRUARY</code>
     * <br><code>monthsByValue[12] == Month.DECEMBER</code>
     */
    private final static Month[] monthsByValue = Month.values();


    /**
     * Constructs a TinyDate from a YYYY-MM-DD <code>String</code>.
     *
     * <p>This constructor assumes that input is properly formatted.</p>
     * @param date
     */
    public TinyDate(String date) {
        //YYYY-MM-DD
        this.year = Integer.parseInt(date.substring(0, 4));
        this.month = Integer.parseInt(date.substring(5, 7));
        this.day = Integer.parseInt(date.substring(8, 10));
    }


    /**
     * Constructs a TinyDate from individual year/month/day values.
     * @param   year
     *          year, assumed to be >= 1000
     * @param   month
     *          month in natural indexing (January is 1, February 2 and so on)
     * @param   day
     *          day, assumed to fall within bounds of the given month
     */
    public TinyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static TinyDate of(String date) {
        return new TinyDate(date);
    }


    /**
     * Returns a new TinyDate that represents a date that's some <code>offset</code> days after this date
     * (represented by this TinyDate object).
     * @param   offset
     *          the number of days to add to this date.
     * @return
     *          the new TinyDate, <code>offset</code> days later.
     */
    public TinyDate addDays(int offset) {
        int y = year;
        int m = month;
        int d = day;

        int daysLeft = monthsByValue[m].daysLeft(d, isLeapYear(y));

        //add days month after month
        while (offset > daysLeft) {
            offset -= daysLeft;
            d = 0;

            if (m == 12) {
                m = 1;
                y++;
            } else {
                m++;
            }

            daysLeft = monthsByValue[m].daysLeft(d, isLeapYear(y));
        }
        d += offset;

        return new TinyDate(y, m, d);
    }

    public boolean after(TinyDate otherDate) {
        return this.asNumber() > otherDate.asNumber();
    }

    public boolean after(String otherDate) {
        return after(new TinyDate(otherDate));
    }


    /**
     * Returns this date as a single number. Useful for sorting. The first 4 digits stand for the year, the next 2
     * for the month (06 -> June etc.), and the last 2 for the day, <strong>YYYYMMDD</strong>.
     * @return
     *          the numeric representation of this date.
     */
    public int asNumber() {
        return year * 10000 + month * 100 + day;
    }


    /**
     * Returns this date as a YYYY-MM-DD String. The result will be properly formatted, with the month and day always
     * taking two characters (i.e. "01" instead of "1" for January), except for the year, which is assumed to be >=
     * 1000 naturally.
     * @return
     *          the <code>String</code> representation of this date.
     */
    @Override
    public String toString() {
        //this wouldn't work for Mieszko I
        return year + "-" + fixLength(month) + "-" + fixLength(day);
    }


    /**
     * Helper method to determine if the given year is a leap (366 day) year.
     * @param   year
     *          the year in question.
     * @return
     *          <code>true</code> for a leap (366 day) year, <code>false</code> for a normal (365 day) year.
     */
    private boolean isLeapYear(int year) {
        //leap year = divisible by 4
        //except NOT a leap year if also divisible by 100
        //UNLESS also divisible by 400
        //for example: 2000 is a leap year, 1900 is not
        return (year % 4 == 0) && ((year % 400 == 0) || (year % 100 != 0));
    }


    /**
     * Helper method to add a leading "0" to a single-digit day/month, for example turning January "1" to "01". Used
     * to represent days/months as proper MM-DD <code>String</code>s.
     * @param dateFragment
     *          either day or month to convert.
     * @return
     *          the day/month as a DD/MM <code>String</code>.
     */
    private String fixLength(int dateFragment) {
        if (dateFragment < 10) {
            return "0" + dateFragment;
        }
        return "" + dateFragment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TinyDate tinyDate = (TinyDate) o;

        if (year != tinyDate.year) return false;
        if (month != tinyDate.month) return false;
        return day == tinyDate.day;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }
}
