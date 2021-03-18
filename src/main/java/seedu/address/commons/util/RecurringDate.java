package seedu.address.commons.util;

import java.time.LocalDate;

public class RecurringDate {
    enum RecurrenceType {
        WEEKLY, MONTHLY
    };

    private final LocalDate seed;
    private final RecurrenceType recurrenceType;

    public RecurringDate(LocalDate seed, RecurrenceType recurrenceType) {
        this.seed = seed;
        this.recurrenceType = recurrenceType;
    }

    private static boolean isSameDayOfMonth(LocalDate date1, LocalDate date2) {
        return date1.getDayOfMonth() == date2.getDayOfMonth();
    }

    private static boolean isSameDayOfWeek(LocalDate date1, LocalDate date2) {
        return date1.getDayOfWeek().equals(date2.getDayOfWeek());
    }

    /**
     * Gets the date for the next meeting that is immediately greater than or equal to the given date.
     *
     * @param date
     * @return
     */
    public LocalDate getDate(LocalDate date) {
        if (date.equals(seed)) {
            return this.seed;
        }
        int seedDay;
        int day;
        if (this.recurrenceType.equals(RecurrenceType.MONTHLY)) {
            seedDay = seed.getDayOfMonth();
            day = date.getDayOfMonth();
            if (isSameDayOfMonth(date, this.seed)) {
                return date;
            }
            if (day < seedDay) {
                if (seedDay > date.lengthOfMonth()) {
                    return date.plusDays(date.lengthOfMonth() - day);
                }
                return date.plusDays(seedDay - day);
            }
            return date.plusMonths(1).minusDays(day - seedDay);
        } else {
            seedDay = seed.getDayOfWeek().getValue();
            day = date.getDayOfWeek().getValue();
            if (isSameDayOfWeek(date, this.seed)) {
                return date;
            }
            if (day < seedDay) {
                return date.plusDays(seedDay - day);
            }
            return date.plusWeeks(1).minusDays(day - seedDay);
        }
    }
}
