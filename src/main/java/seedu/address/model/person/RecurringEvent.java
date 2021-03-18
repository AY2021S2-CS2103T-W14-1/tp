package seedu.address.model.person;

import seedu.address.commons.util.RecurringDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a RecurringDate event for a Person in the FriendDex.
 */
public class RecurringEvent extends Event {

    private final RecurringDate recurringDate;

    /**
     * Constructs a {@code RecurringEvent}
     * @param recurringDate The recurring dates.
     * @param description A description of the event.
     */
    public RecurringEvent(RecurringDate recurringDate, String description) {
        super(recurringDate.getSeed(), description);
        this.recurringDate = recurringDate;
    }

    /**
     * Constructs a {@code RecurringEvent}
     * @param recurringDate The recurring dates.
     * @param time A valid time.
     * @param description A description of the event.
     */
    public RecurringEvent(RecurringDate recurringDate, LocalTime time, String description) {
        super(recurringDate.getSeed(), time, description);
        this.recurringDate = recurringDate;
    }

    /**
     * @return Seed of the recurring dates.
     */
    public LocalDate getSeed() {
        return this.recurringDate.getSeed();
    }

    /**
     * @return The recurring dates.
     */
    public RecurringDate getRecurringDate() {
        return recurringDate;
    }

    /**
     * @return The immediate date of the event after the current date
     */
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
