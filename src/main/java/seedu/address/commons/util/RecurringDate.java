package seedu.address.commons.util;

import java.time.LocalDate;
import java.util.Objects;

public class RecurringDate {

    public enum RecurrenceType {
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

    public LocalDate getSeed() {
        return this.seed;
    }

    /**
     * Gets the date for the next meeting that is immediately greater than or equal to the given date.
     *
     * @param date The lower bound date.
     * @return The date for the next meeting.
     */
    public LocalDate getDate(LocalDate date) {
        if (date.equals(seed)) {
            return this.seed;
        }
        int seedDay;
        int day;
        if (this.recurrenceType.equals(RecurrenceType.MONTHLY)) {
            seedDay = this.seed.getDayOfMonth();
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
            seedDay = this.seed.getDayOfWeek().getValue();
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RecurringDate r = (RecurringDate) object;
        return Objects.equals(seed, r.seed) && recurrenceType == r.recurrenceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seed, recurrenceType);
    }
}
