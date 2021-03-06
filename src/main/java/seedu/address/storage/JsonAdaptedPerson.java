package seedu.address.storage;

import static seedu.address.commons.core.Messages.MESSAGE_DATE_BEFORE_BIRTHDAY;
import static seedu.address.commons.core.Messages.MESSAGE_DESERIALIZE_ERROR_DUMP_DATA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.DateUtil;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Debt;
import seedu.address.model.person.Email;
import seedu.address.model.person.Event;
import seedu.address.model.person.Goal;
import seedu.address.model.person.Meeting;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Picture;
import seedu.address.model.person.SpecialDate;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private static final Logger logger = LogsCenter.getLogger(JsonAdaptedPerson.class);
    private final String name;
    private final String phone;
    private final String email;
    private final LocalDate birthday;
    private final String debt;
    private final String goal;
    private final String address;
    private final JsonAdaptedPicture picture;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();
    private final List<JsonAdaptedSpecialDate> dates = new ArrayList<>();
    private final List<JsonAdaptedMeeting> meetings = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("birthday") LocalDate birthday,
            @JsonProperty("goal") String goal, @JsonProperty("address") String address,
            @JsonProperty("picture") JsonAdaptedPicture picture,
            @JsonProperty("debt") String debt, @JsonProperty("tagged") List<JsonAdaptedTag> tagged,
            @JsonProperty("dates") List<JsonAdaptedSpecialDate> dates,
            @JsonProperty("meetings") List<JsonAdaptedMeeting> meetings) {

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.goal = goal;
        this.address = address;
        this.picture = picture;
        this.debt = debt;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
        if (dates != null) {
            this.dates.addAll(dates);
        }
        if (meetings != null) {
            this.meetings.addAll(meetings);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        birthday = source.getBirthday().getDate();
        goal = source.getGoal().toString();
        Optional<Picture> srcPic = source.getPicture();
        picture = srcPic.isEmpty() ? null : new JsonAdaptedPicture(srcPic.get());
        debt = source.getDebt().value.toString();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        dates.addAll(source.getDates().stream()
                .map(JsonAdaptedSpecialDate::new)
                .collect(Collectors.toList()));
        meetings.addAll(source.getMeetings().stream()
                .map(JsonAdaptedMeeting::new)
                .collect(Collectors.toList()));
    }

    private IllegalValueException internalIllegalValueException(String message) {
        logger.warning(String.format(MESSAGE_DESERIALIZE_ERROR_DUMP_DATA, "Person"));
        logger.warning(this.toString());
        return new IllegalValueException(message);
    }

    private String errorMsgForEventBeforeBirthday(Name name, Event event, String eventType) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Error deserializing %s. ", name))
                .append(String.format("Problem with one of the %s.\n", eventType))
                .append(String.format("%s: %s\n", eventType, event.toString()))
                .append(String.format(MESSAGE_DATE_BEFORE_BIRTHDAY, DateUtil.toErrorMessage(event.getDate())));
        return sb.toString();
    }

    private Set<Tag> tagsToModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {

            // Ignores null values in tagged list in json file
            if (tag == null) {
                continue;
            }

            personTags.add(tag.toModelType());
        }
        return new HashSet<>(personTags);
    }

    private List<SpecialDate> datesToModelType(Birthday modelBirthday, Name modelName) throws IllegalValueException {
        final List<SpecialDate> modelDates = new ArrayList<>();
        for (JsonAdaptedSpecialDate date : dates) {

            // Ignores null values in dates list in json file
            if (date == null) {
                continue;
            }

            SpecialDate dateModel = date.toModelType();
            LocalDate dateToCheck = dateModel.getDate();

            if (dateToCheck.isBefore(modelBirthday.getDate())) {
                throw internalIllegalValueException(
                        errorMsgForEventBeforeBirthday(modelName, dateModel, "Special Date"));
            }

            modelDates.add(dateModel);
        }

        return modelDates;
    }

    private List<Meeting> meetingsToModelType(Birthday modelBirthday, Name modelName) throws IllegalValueException {
        final List<Meeting> modelMeetings = new ArrayList<>();
        for (JsonAdaptedMeeting meeting : meetings) {

            // Ignores null values in meetings list in json file
            if (meeting == null) {
                continue;
            }

            Meeting modelMeeting = meeting.toModelType();
            LocalDate dateToCheck = modelMeeting.getDate();

            if (dateToCheck.isBefore(modelBirthday.getDate())) {
                throw internalIllegalValueException(
                        errorMsgForEventBeforeBirthday(modelName, modelMeeting, "Meeting"));
            }

            modelMeetings.add(modelMeeting);
        }

        return modelMeetings;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        if (name == null) {
            throw internalIllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw internalIllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(trimmedName);

        if (phone == null) {
            throw internalIllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Phone.class.getSimpleName()));
        }
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw internalIllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(trimmedPhone);

        if (email == null) {
            throw internalIllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Email.class.getSimpleName()));
        }
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw internalIllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(trimmedEmail);

        if (birthday == null) {
            throw internalIllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Birthday.class.getSimpleName()));
        }
        if (!Birthday.isValidBirthday(birthday)) {
            throw internalIllegalValueException(Birthday.MESSAGE_CONSTRAINTS);
        }
        final Birthday modelBirthday = new Birthday(birthday);

        if (goal == null) {
            throw internalIllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Goal.class.getSimpleName()));
        }
        String trimmedGoal = goal.trim();
        if (!Goal.isValidGoal(trimmedGoal)) {
            throw internalIllegalValueException(Goal.MESSAGE_CONSTRAINTS);
        }
        final Goal modelGoal = new Goal(Goal.parseFrequency(trimmedGoal.toLowerCase(Locale.ROOT)));

        if (address == null) {
            throw internalIllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw internalIllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(trimmedAddress);

        Picture modelPicture = null;
        if (picture != null) {
            modelPicture = picture.toModelType();
        }

        if (debt == null) {
            throw internalIllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Debt.class.getSimpleName()));
        }
        String trimmedDebt = debt.trim();
        if (!Debt.isValidDebt(trimmedDebt)) {
            throw internalIllegalValueException(Debt.MESSAGE_CONSTRAINTS);
        }
        final Debt modelDebt = new Debt(trimmedDebt);

        final Set<Tag> modelTags = tagsToModelType();
        final List<SpecialDate> rawModelDates = datesToModelType(modelBirthday, modelName);
        final List<Meeting> rawModelMeetings = meetingsToModelType(modelBirthday, modelName);

        // sort dates here as well in case JSON data is modified externally.
        final List<SpecialDate> modelDates = rawModelDates.stream()
                .sorted(Comparator.comparing(SpecialDate::getDate).reversed())
                .collect(Collectors.toList());

        final List<Meeting> modelMeetings = rawModelMeetings.stream()
                .sorted(Comparator.comparing(Meeting::getDate).reversed())
                .collect(Collectors.toList());

        return new Person(modelName, modelPhone, modelEmail, modelBirthday, modelGoal, modelAddress, modelPicture,
                modelDebt, modelTags, modelDates, modelMeetings);
    }

    @Override
    public String toString() {
        return "JsonAdaptedPerson{"
                + "name='" + name + '\''
                + ", phone='" + phone + '\''
                + ", email='" + email + '\''
                + ", birthday='" + birthday + '\''
                + ", debt='" + debt + '\''
                + ", goal='" + goal + '\''
                + ", address='" + address + '\''
                + ", picture=" + (picture != null ? picture.toString() : null)
                + ", tagged=" + tagged.toString()
                + ", dates=" + dates.toString()
                + ", meetings=" + meetings.toString()
                + "}";
    }


}
