package seedu.address.commons.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RecurringDateTest {

    private final LocalDate SEED_1 = LocalDate.of(2021, 03, 14);
    private final LocalDate SEED_2 = LocalDate.of(2021, 03, 31);

    // leap years
    private final LocalDate LEAP_YEAR_1 = LocalDate.of(2024, 02, 29);
    private final LocalDate LEAP_YEAR_2 = LocalDate.of(2028, 02, 29);

    @Test
    public void recurringDate_weekTest_success() {
        RecurringDate recurringDate = new RecurringDate(SEED_1, RecurringDate.RecurrenceType.WEEKLY);
        assertEquals(SEED_1, recurringDate.getDate(SEED_1));
        assertEquals(SEED_1.plusWeeks(1), recurringDate.getDate(SEED_1.plusDays(1)));
        assertEquals(SEED_1, recurringDate.getDate(SEED_1.minusDays(6)));
        assertNotEquals(SEED_1, recurringDate.getDate(SEED_1.minusDays(7)));
    }

    @Test
    void recurringDate_monthTest_success() {
        RecurringDate recurringDate = new RecurringDate(SEED_1, RecurringDate.RecurrenceType.MONTHLY);
        assertEquals(SEED_1, recurringDate.getDate(SEED_1));
        assertEquals(SEED_1.plusMonths(1), recurringDate.getDate(SEED_1.plusMonths(1)));
        assertEquals(SEED_1.plusMonths(1), recurringDate.getDate(SEED_1.plusDays(1)));
        assertEquals(SEED_1, recurringDate.getDate(SEED_1.minusDays(6)));
        assertEquals(SEED_1, recurringDate.getDate(SEED_1.minusMonths(1).plusDays(1)));
    }

    @Test
    void recurringDate_shortMonthTest_success() {
        RecurringDate recurringDate = new RecurringDate(SEED_2, RecurringDate.RecurrenceType.MONTHLY);
        assertEquals(LEAP_YEAR_1, recurringDate.getDate(LEAP_YEAR_1));
    }

    @Test
    void recurringDate_leapYearTest_success() {
        RecurringDate recurringDate = new RecurringDate(LEAP_YEAR_1, RecurringDate.RecurrenceType.MONTHLY);
        assertEquals(
                LocalDate.of(2023, 02, 28),
                recurringDate.getDate(LocalDate.of(2023, 02, 10)));
        assertEquals(LEAP_YEAR_2, recurringDate.getDate(LocalDate.of(2028, 02, 1)));
    }
}
