package ar.com.ghostix.lib.easyconsolelib.easyconsoleinput;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * A utility class for simplified console input handling.
 * This class extends the functionality of {@link Scanner} by handling exceptions and offering
 * optional user confirmation for inputs.
 */
public class ConsoleInput {

    /** The {@link Scanner} instance used for handling console input. */
    protected final Scanner scan;

    // Class constructors

    /**
     * Creates a new {@code ConsoleInput} using the standard input stream {@code System.in}.
     */
    public ConsoleInput() {
        scan = new Scanner(System.in);
    }

    /**
     * Creates a new {@code ConsoleInput} using a custom {@link Scanner}.
     *
     * @param scan The scanner instance to use for input handling.
     */
    public ConsoleInput(Scanner scan) {
        this.scan = scan;
    }

    /**
     * Creates a new {@code ConsoleInput} using a specified {@link InputStream}.
     *
     * @param inputStream The input stream for reading user inputs.
     */
    public ConsoleInput(InputStream inputStream) {
        scan = new Scanner(inputStream);
    }

    // Confirmation helpers

    /**
     * Asks the user a yes/no question and checks if the response matches the affirmative character.
     *
     * @param message    The question to display to the user.
     * @param affirmative The character that represents an affirmative response (e.g., 'y' for yes).
     * @return {@code true} if the user's response matches the affirmative character, otherwise {@code false}.
     */
    public boolean askSomething(String message, char affirmative) {
        System.out.println(message);
        String option = null;
        while (option == null || option.isEmpty()) {
            option = scan.nextLine().toLowerCase();
        }
        return option.charAt(0) == affirmative;
    }

    /**
     * Asks the user for a confirmation with a predefined Spanish message.
     *
     * @return {@code true} if the user confirms with 's' (yes), otherwise {@code false}.
     */
    public boolean askConfirmation() {
        return askSomething("¿Estás seguro? (S/N)...", 's');
    }

    /**
     * Asks the user for a confirmation, displaying the provided value alongside the prompt.
     *
     * @param <T>   The type of the value being confirmed.
     * @param value The value to be shown in the confirmation prompt.
     * @return {@code true} if the user confirms with 's' (yes), otherwise {@code false}.
     */
    public <T> boolean askConfirmation(T value) {
        return askSomething("¿Estás seguro? Valor introducido: " + value + " (S/N)...", 's');
    }

    // Generic input handling

    /**
     * Prompts the user for input using a provided supplier function and optional confirmation.
     * Handles exceptions and retries input if invalid.
     *
     * @param <T>                  The type of input expected.
     * @param prompt               The message to display to the user.
     * @param supplier             A {@link Supplier} function to retrieve the user input.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The validated user input of type {@code T}.
     */
    public <T> T getInput(String prompt, Supplier<T> supplier, boolean askForUserConfirmation) {
        while (true) {
            System.out.print(prompt);
            try {
                T input = supplier.get();
                if (!askForUserConfirmation || askConfirmation(input)) {
                    return input;
                }
            } catch (InputMismatchException error) {
                System.out.println("Valor inválido. Por favor, intente de nuevo...");
                scan.nextLine(); // Clear invalid input
            }
        }
    }

    // Utility methods

    /**
     * Clears the input buffer if there is leftover input.
     */
    public void clean() {
        if (scan.hasNextLine()) {
            scan.nextLine();
        }
    }

    // Specific input methods for various data types

    /**
     * Prompts the user for a {@code String} input with optional confirmation.
     *
     * @param prompt               The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as a {@code String}.
     */
    public String next(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, scan::next, askForUserConfirmation);
    }

    /**
     * Prompts the user for a full line of text input with optional confirmation.
     *
     * @param prompt               The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as a {@code String}.
     */
    public String nextLine(String prompt, boolean askForUserConfirmation) {
        String input = null;
        while (input == null) {
            input = getInput(prompt, scan::nextLine, askForUserConfirmation);
            if (input == null) clean();
        }
        return input;
    }

    /**
     * Prompts the user for an {@code int} input with optional confirmation.
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@code int}.
     */
    public int nextInt(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, () -> {
            int value = scan.nextInt();
            clean();
            return value;
        }, askForUserConfirmation);
    }

    /**
     * Prompts the user for a {@code double} input with optional confirmation.
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@code double}.
     */
    public double nextDouble(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, () -> {
            double value = scan.nextDouble();
            clean();
            return value;
        }, askForUserConfirmation);
    }

    /**
     * Prompts the user for a {@code float} input with optional confirmation.
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@code float}.
     */
    public float nextFloat(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, () -> {
            float value = scan.nextFloat();
            clean();
            return value;
        }, askForUserConfirmation);
    }

    /**
     * Prompts the user for a {@code boolean} input with optional confirmation.
     *
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@code boolean}.
     */
    public boolean nextBoolean(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, () -> {
            boolean value = scan.nextBoolean();
            clean();
            return value;
        }, askForUserConfirmation);
    }

    /**
     * Prompts the user for a {@code long} input with optional confirmation.
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@code long}.
     */
    public long nextLong(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, () -> {
            long value = scan.nextLong();
            clean();
            return value;
        }, askForUserConfirmation);
    }

    /**
     * Prompts the user for a {@code byte} input with optional confirmation.
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@code byte}.
     */
    public byte nextByte(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, () -> {
            byte value = scan.nextByte();
            clean();
            return value;
        }, askForUserConfirmation);
    }

    /**
     * Prompts the user for a {@code short} input with optional confirmation.
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@code short}.
     */
    public short nextShort(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, () -> {
            short value = scan.nextShort();
            clean();
            return value;
        }, askForUserConfirmation);
    }

    /**
     * Prompts the user for a {@link BigDecimal} input with optional confirmation.
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@link BigDecimal}.
     */
    public BigDecimal nextBigDecimal(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, scan::nextBigDecimal, askForUserConfirmation);
    }

    /**
     * Prompts the user for a {@link BigInteger} input with optional confirmation.
     *
     * @param prompt The message to prompt the user.
     * @param askForUserConfirmation If {@code true}, the method will ask for user confirmation before returning the value.
     * @return The user's input as an {@link BigInteger}.
     */
    public BigInteger nextBigInteger(String prompt, boolean askForUserConfirmation) {
        return getInput(prompt, scan::nextBigInteger, askForUserConfirmation);
    }
}
