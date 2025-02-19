package ar.com.ghostix.lib.easyconsolelib.util;

import ar.com.ghostix.lib.easyconsolelib.easyconsoleinput.ConsoleInput;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for common ASCII-based console operations such as clearing the screen
 * and prompting the user for confirmation.
 * <p>
 * This class is designed to simplify console interactions in Java terminal applications.
 * It handles platform-specific screen clearing and provides methods for user input confirmation.
 */
public class AsciiUtils {
    /**
     * Clears the console screen based on the terminal environment.
     * <p>
     * If running inside a Java terminal (like an IDE), it will simulate clearing the screen by printing new lines.
     * If running in a native terminal, it uses platform-specific commands to clear the screen.
     *
     * @param isJavaTerminal {@code true} if the method should simulate screen clearing for a Java terminal (like IDE consoles).
     */
    private static void clean(boolean isJavaTerminal) {
        if (isJavaTerminal) {
            // Simulate screen clear for Java IDE consoles
            System.out.println("\n".repeat(5));
            return;
        }
        try {
            // Clear the screen using native commands based on the OS
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                return;
            }
            // Clear screen using ANSI escape codes for UNIX-like systems
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (IOException | InterruptedException error) {
            // If an error occurs while clearing the screen, print the error trace
            error.printStackTrace(System.out);
        }
    }

    /**
     * Prompts the user with a confirmation message and checks if the input matches the affirmative character.
     * <p>
     * This method reads user input and compares it with the specified affirmative character (e.g., 'y' for yes).
     *
     * @param message    The message to prompt the user.
     * @param affirmative The character to be considered as affirmative input.
     * @return {@code true} if the user's input matches the affirmative character, {@code false} otherwise.
     */
    public static boolean askSomething(String message, char affirmative) {
        ConsoleInput input = new ConsoleInput(System.in);
        return input.askSomething(message, affirmative);
    }

    /**
     * Prompts the user with a confirmation message using a specified {@link InputStream}.
     * <p>
     * This version allows using a custom input stream instead of the default {@code System.in}.
     *
     * @param inputStream The input stream from which to read user input.
     * @param message The message to prompt the user.
     * @param affirmative The character to be considered as affirmative input.
     * @return {@code true} if the user's input matches the affirmative character, {@code false} otherwise.
     */
    public static boolean askSomething(InputStream inputStream, String message, char affirmative) {
        ConsoleInput input = new ConsoleInput(inputStream);
        return input.askSomething(message, affirmative);
    }

    /**
     * Clears the console screen based on the terminal capabilities and Java version.
     * <p>
     * This method attempts to clear the screen by checking terminal capabilities.
     * - On Java 22+, it uses the `System.console().isTerminal()` method for native terminal detection.
     * - On Java 21 and below, it attempts to clear using ANSI escape codes and platform-specific commands.
     * <p>
     * If the terminal cannot be detected or an error occurs, it will print multiple new lines instead.
     */
    public static void clearScreen() {
        try {
            // Java 22+ support: Detect if the console is a terminal
            Method isTerminal = System.console().getClass().getMethod("isTerminal");
            if (!(boolean) isTerminal.invoke(System.console())) {
                clean(true); // IDE environment
                return;
            }
            clean(false); // Terminal environment
        } catch (NoSuchMethodException e) {
            // Java 21 and below: Fallback to console detection
            if (System.console() != null) {
                clean(true); // IDE or restricted terminal
                return;
            }
            clean(false); // Default clear
        } catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
            // Exception handling for reflection failures
            e.printStackTrace(System.out);
        }
    }
}
