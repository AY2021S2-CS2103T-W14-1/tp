package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;

/**
 * Debt stores a float that should be up to only 2 decimal places.
 * The user can only input in positive floats but the program is allowed to store negative floats.
 * A negative debt would indicate that the user owes money to the person.
 * Debt value should be in range from -999999999999 to 999999999999
 */
public class Debt {

    public static final Debt MAX_DEBT = new Debt(new BigDecimal("999999999999"));
    public static final Debt MIN_DEBT = new Debt(new BigDecimal("-999999999999"));
    public static final String MESSAGE_CONSTRAINTS = "Debt given should be a positive float,"
            + "should only be up 2 decimal places and is smaller than " + MAX_DEBT;

    public final BigDecimal value;
    /**
     * Constructs a {@code Debt}
     * @param debt a valid debt
     */
    public Debt(String debt) {
        requireNonNull(debt);
        checkArgument(isValidDebt(debt), MESSAGE_CONSTRAINTS);
        value = new BigDecimal(debt);
    }

    /**
     * Constructs a {@code Debt}.
     * @param debt
     */
    protected Debt(BigDecimal debt) {
        requireNonNull(debt);
        value = debt;
    }

    /**
     * Check whether the given string has a valid format for {@code Debt}
     * @param debt
     * @return boolean which indicate if string is a valid debt.
     */
    public static boolean isValidDebt(String debt) {
        try {
            BigDecimal debtNumber = new BigDecimal(debt);
            return debtNumber.scale() <= 2
                    && debtNumber.compareTo(MAX_DEBT.value) != 1
                    && debtNumber.compareTo(MIN_DEBT.value) != -1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check whether the given debt is out of the range stipulated
     * @param debt
     * @return boolean which indicate if debt is within range stipulated.
     */
    public static boolean isDebtOutOfRange(Debt debt) {
        return !(debt.value.compareTo(MAX_DEBT.value) != 1 && debt.value.compareTo(MIN_DEBT.value) != -1);
    }

    /**
     * Adds two {@code Debt} objects.
     * @param first
     * @param second
     * @return Debt with the value being the sum of the two given Debt.
     */
    public static Debt add(Debt first, Debt second) {
        requireNonNull(first);
        requireNonNull(second);
        return new Debt(first.value.add(second.value));
    }

    /**
     * Subtract the second {@Debt} object from the first.
     * @param first
     * @param second
     * @return Debt with the value being first.value - second.value.
     */
    public static Debt subtract(Debt first, Debt second) {
        requireNonNull(first);
        requireNonNull(second);
        return new Debt(first.value.subtract(second.value));
    }

    /**
     * Returns the string to be printed onto the Ui.
     * @return String to be printed onto the Ui.
     */
    public String toUi() {
        if (value.signum() != -1) {
            return String.format("$%.2f", value);
        } else {
            return String.format("-$%.2f", value.abs());
        }
    }

    @Override
    public String toString() {
        return String.format("%.2f", value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Debt
                && (value.equals(((Debt) other).value)));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
