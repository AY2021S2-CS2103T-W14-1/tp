package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_DATE_AFTER_TODAY;
import static seedu.address.commons.core.Messages.MESSAGE_DATE_BEFORE_BIRTHDAY;
import static seedu.address.commons.core.Messages.MESSAGE_TIME_AFTER_NOW;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.DateUtil;
import seedu.address.commons.util.TimeUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Event;
import seedu.address.model.person.Person;

public class AddMeetingCommand extends Command {

    public static final String COMMAND_WORD = "add-meeting";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Records a meeting of the person identified "
            + "by the index number used in the last person listing. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + PREFIX_DESCRIPTION + "DESCRIPTION \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "12-12-2021 "
            + PREFIX_TIME + "1240 "
            + PREFIX_DESCRIPTION + "We went to the beach!";

    public static final String MESSAGE_ADD_MEETING_SUCCESS = "Added meeting for %1$s";
    public static final String MESSAGE_ADD_MEETING_FAILURE_DATE_AFTER_TODAY = "Failed to add meeting: " +
            MESSAGE_DATE_AFTER_TODAY;
    public static final String MESSAGE_ADD_MEETING_FAILURE_TIME_AFTER_NOW = "Failed to add meeting: " +
            MESSAGE_TIME_AFTER_NOW;
    public static final String MESSAGE_ADD_MEETING_FAILURE_DATE_BEFORE_BIRTHDAY = "Failed to add meeting: " +
            MESSAGE_DATE_BEFORE_BIRTHDAY;


    private final Index index;
    private final Event meeting;

    /**
     * @param index of the person in the filtered person list to add the meeting to
     * @param meeting the meeting to add
     */
    public AddMeetingCommand(Index index, Event meeting) {
        requireAllNonNull(index, meeting);

        this.index = index;
        this.meeting = meeting;
    }

    private static Person createEditedPerson(Person personToEdit, Event meeting) {
        assert personToEdit != null;
        List<Event> meetingsToEdit = new ArrayList<>(personToEdit.getMeetings());
        meetingsToEdit.add(meeting);
        meetingsToEdit = meetingsToEdit.stream()
                .sorted(Comparator.comparing(Event::getDate).reversed())
                .collect(Collectors.toList());

        return personToEdit.withMeetings(meetingsToEdit);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(index.getZeroBased());

        LocalDate meetingDate = meeting.getDate();
        LocalTime meetingTime = meeting.getTime();

        if (person.beforeBirthday(meetingDate)) {
            throw new CommandException(String.format(MESSAGE_ADD_MEETING_FAILURE_DATE_BEFORE_BIRTHDAY, meetingDate));
        }

        if (DateUtil.afterToday(meetingDate)) {
            throw new CommandException(String.format(MESSAGE_ADD_MEETING_FAILURE_DATE_AFTER_TODAY, meetingDate));
        }

        if (DateUtil.isToday(meetingDate) && TimeUtil.afterNow(meetingTime)) {
            throw new CommandException(String.format(MESSAGE_ADD_MEETING_FAILURE_TIME_AFTER_NOW, meetingTime));
        }

        Person editedPerson = createEditedPerson(person, meeting);

        model.setPerson(person, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_MEETING_SUCCESS, editedPerson.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddMeetingCommand)) {
            return false;
        }

        AddMeetingCommand e = (AddMeetingCommand) other;
        return index.equals(e.index) && meeting.equals(e.meeting);
    }
}
