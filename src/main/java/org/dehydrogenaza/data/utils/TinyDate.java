package org.dehydrogenaza.data.utils;

public class TinyDate {

    private final int year;
    private final int month;
    private final int day;

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

        private final int daysInMonth;
        private int daysIn(boolean isLeapYear) {
            if (this == FEBRUARY && isLeapYear) {
                return daysInMonth + 1;
            }
            return daysInMonth;
        }

        private int daysLeft(int currentDay, boolean leapYear) {
            return this.daysIn(leapYear) - currentDay;
        }

    }
    private final static Month[] monthsByValue = Month.values();

    public TinyDate(String date) {
        //yyyy-MM-dd
        this.year = Integer.parseInt(date.substring(0, 4));
        this.month = Integer.parseInt(date.substring(5, 7));
        this.day = Integer.parseInt(date.substring(8, 10));
    }

    public TinyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public TinyDate offset(int offset) {
        int y = year;
        int m = month;
        int d = day;

        int daysLeft = monthsByValue[m].daysLeft(d, isLeapYear(y));

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

    public int asNumber() {
        return year * 10000 + month * 100 + day;
    }

    @Override
    public String toString() {
        //this wouldn't work for Mieszko I
        return year + "-" + fixLength(month) + "-" + fixLength(day);
    }

    private boolean isLeapYear(int year) {
        //leap year = divisible by 4
        //except NOT a leap year if also divisible by 100
        //UNLESS also divisible by 400
        //for example: 2000 is a leap year, 1900 is not
        return (year % 4 == 0) && ((year % 400 == 0) || (year % 100 != 0));
    }

    private String fixLength(int dateFragment) {
        if (dateFragment < 10) {
            return "0" + dateFragment;
        }
        return "" + dateFragment;
    }
}
