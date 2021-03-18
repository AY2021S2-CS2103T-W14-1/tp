package seedu.address.model.person;

import seedu.address.commons.util.RecurringDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class RecurringEvent extends Event {

    private final RecurringDate recurringDate;

    public RecurringEvent(RecurringDate recurringDate, String description) {
        super(recurringDate.getSeed(), description);
        this.recurringDate = recurringDate;
    }

    public RecurringEvent(RecurringDate recurringDate, LocalTime time, String description) {
        super(recurringDate.getSeed(), time, description);
        this.recurringDate = recurringDate;
    }

    @Override
    public LocalDate getDate() {
        return recurringDate.getDate(LocalDate.now());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        RecurringEvent event = (RecurringEvent) other;
        return Objects.equals(recurringDate, event.recurringDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), recurringDate);
    }
}
