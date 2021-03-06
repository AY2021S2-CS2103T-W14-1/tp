package seedu.address.model.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Represents a Group in FriendDex.
 */
public class Group {

    private Name groupName;
    private Set<Name> persons;

    /**
     * Constructs a {@code Group}.
     *  @param name A valid name.
     * @param persons A set of person.
     */
    public Group(Name name, Set<Name> persons) {
        requireAllNonNull(name, persons);
        this.groupName = name;
        this.persons = persons;
    }

    /**
     * Constructs a {@code Group} with an empty set of Names.
     *
     * @param name A valid name.
     */
    public Group(Name name) {
        requireNonNull(name);
        this.groupName = name;
        this.persons = new HashSet<>();
    }

    public Name getName() {
        return groupName;
    }

    public Set<Name> getPersonNames() {
        return persons;
    }

    public void addPerson(Person p) {
        persons.add(p.getName());
    }

    public void addPersonName(Name personName) {
        persons.add(personName);
    }

    public void removePersonName(Name personName) {
        persons.remove(personName);
    }

    public void setPerson(Person personToEdit, Person editedPerson) {
        if (persons.contains(personToEdit.getName())) {
            persons.remove(personToEdit.getName());
            persons.add(editedPerson.getName());
        }
    }

    public void setPersonName(Name personNameToEdit, Name editedPersonName) {
        if (persons.contains(personNameToEdit)) {
            persons.remove(personNameToEdit);
            persons.add(editedPersonName);
        }
    }

    public void setPersons(Set<Name> editedPersonSet) {
        persons = editedPersonSet;
    }

    /**
     * Deletes the given person from this {@code group}. The group object is guaranteed to have no
     * instance of the given {@code person} in this group. If person cannot be found this method
     * does nothing and no exceptions will be thrown.
     *
     * @param personNameToDelete The name of the person to delete.
     */
    public void deletePerson(Name personNameToDelete) {
        persons.remove(personNameToDelete);
    }

    public boolean isEmpty() {
        return persons.isEmpty();
    }

    @Override
    public String toString() {
        return groupName.toString();
    }

    /**
     * Returns true if both groups have the same name.
     * This defines a weaker notion of equality between two groups.
     */
    public boolean isSameGroup(Group otherGroup) {
        if (otherGroup == this) {
            return true;
        }

        return otherGroup != null
                && otherGroup.getName().equals(getName());
    }

    public String toUi() {
        return this.persons.stream().map(Name::toString).sorted().collect(Collectors.joining(", "));
    }

    /**
     * Returns true if both groups have field values.
     * This defines a stronger notion of equality between two groups.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Group)) {
            return false;
        }

        Group otherGroup = (Group) other;
        return otherGroup.getName().equals(getName())
                && otherGroup.getPersonNames().equals(getPersonNames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, persons);
    }
}
