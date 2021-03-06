package seedu.address.storage;

import static seedu.address.model.group.GroupHashMap.DEFAULT_GROUP_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "frienddex")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATES_IN_PERSONS = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATES_IN_GROUPS = "Groups list contains duplicate group(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedGroup> groups = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons and groups
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("groups") List<JsonAdaptedGroup> groups) {
        this.persons.addAll(persons);
        this.groups.addAll(groups);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        groups.addAll(source.getGroupMap().entrySet().stream().map(x -> x.getValue())
                .map(JsonAdaptedGroup::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            if (jsonAdaptedPerson == null) {
                continue;
            }

            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATES_IN_PERSONS);
            }
            addressBook.addPerson(person);
        }

        for (JsonAdaptedGroup jsonAdaptedGroup : groups) {
            if (jsonAdaptedGroup == null) {
                continue;
            }

            Group group = jsonAdaptedGroup.toModelType(addressBook.getPersonList());
            if (!group.getName().equals(DEFAULT_GROUP_NAME) && addressBook.hasGroupName(group.getName())) {
                throw new IllegalValueException(MESSAGE_DUPLICATES_IN_GROUPS);
            }
            if (!group.getName().equals(DEFAULT_GROUP_NAME)) {
                addressBook.addGroup(group);
            }
        }

        return addressBook;
    }

}
