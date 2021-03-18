package seedu.address.storage;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.util.RecurringDate;
import seedu.address.model.person.Event;
import seedu.address.model.person.RecurringEvent;

/**
 * Jackson-friendly version of {@link Event}.
 */
public class JsonAdaptedEvent {

    private final LocalDate date;
    private final LocalTime time;
    private final String description;
    private final RecurringDate.RecurrenceType recurrenceType;

    /**
     * Constructs a {@code JsonAdaptedEvent} with the given {@code date}, {@code time} and {@code description}.
     */
    @JsonCreator
    public JsonAdaptedEvent(@JsonProperty("date") LocalDate date, @JsonProperty("time") LocalTime time,
            @JsonProperty("description") String description, @JsonProperty("recurrenceType") RecurringDate.RecurrenceType recurrenceType) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.recurrenceType = recurrenceType;
    }

    /**
     * Converts a given {@code Event} into this class for Jackson use.
     */
    public JsonAdaptedEvent(Event source) {
        if (source.getClass().equals(RecurringEvent.class)) {
            date = ((RecurringEvent)source).getSeed();
        } else {
            date = source.getDate();
        }
        time = source.getTime();
        description = source.getDescription();
        if (source.getClass().equals(RecurringEvent.class)) {
            recurrenceType = ((RecurringEvent) source).getRecurringDate().getRecurrenceType();
        } else {
            recurrenceType = RecurringDate.RecurrenceType.NONE;
        }
    }

    private boolean hasTime() {
        return time != null;
    }

    /**
     * Converts this Jackson-friendly adapted event object into the model's {@code Event} object.
     */
    public Event toModelType() {
        if (recurrenceType.equals(RecurringDate.RecurrenceType.NONE)) {
            if (hasTime()) {
                return new Event(date, time, description);
            }
            return new Event(date, description);
        }
        if (hasTime()) {
            return new RecurringEvent(new RecurringDate(date, recurrenceType), time, description);
        }
        return new RecurringEvent(new RecurringDate(date, recurrenceType), description);
    }
}
