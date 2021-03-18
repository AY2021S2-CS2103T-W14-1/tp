package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.RecurringDate;

public class RecurringEventTest {

    private static final LocalDate SEED_1 = LocalDate.of(2021, 03, 14);
    private static final LocalDate SEED_2 = LocalDate.of(2021, 03, 14);

    private static final String DESCRIPTION_1 = "Sample recurring event";

    @Test
    public void recurringEvent_equalTest_success() {
        RecurringEvent recurringEvent1 = new RecurringEvent(
                new RecurringDate(
                        SEED_1, RecurringDate.RecurrenceType.WEEKLY), DESCRIPTION_1);
        RecurringEvent recurringEvent2 = new RecurringEvent(
                new RecurringDate(
                        SEED_2, RecurringDate.RecurrenceType.WEEKLY), DESCRIPTION_1);
        assertEquals(recurringEvent1, recurringEvent2);
    }
}
