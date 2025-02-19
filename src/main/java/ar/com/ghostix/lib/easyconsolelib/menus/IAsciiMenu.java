package ar.com.ghostix.lib.easyconsolelib.menus;

import java.io.InputStream;

import ar.com.ghostix.lib.easyconsolelib.util.AsciiUtils;
/**
 * Interface representing an ASCII menu for console-based applications.
 * Provides utility methods for rendering ASCII-styled titles, clearing the screen,
 * and running the menu logic based on user input.
 */
public interface IAsciiMenu {
    /**
     * Prints the title of the menu with default dimensions.
     * The width and height are predefined for a centered title.
     *
     * @param name The title text to be displayed.
     */
    default void printTitle(String name) {
        // Just use default width if not provided
        printTitle(name, 22, 7);
    }
    /**
     * Prints the title of the menu with customizable dimensions.
     * The title is centered within the provided width and height.
     *
     * @param name   The title text to be displayed.
     * @param width  The total width of the title area.
     * @param height The total height of the title area.
     */
    default void printTitle(String name, int width, int height) {
        int nameLength = name.length();
        width = width + nameLength;
        if (width % 2 != 0) width++;
        String borderLine = "=".repeat(width);
        String blankLine = "|" + " ".repeat(width - 2) + "|";
        // Get the center in Y Axis.
        int centerY = height % 2 == 0 ? height / 2 : (height - 1) / 2;
        System.out.println(borderLine);
        for (int y = 0; y < height; y++) {
            if (y == centerY) {
                int padding = (width - nameLength - 2) / 2;
                int extraSpace = (width - nameLength - 2) % 2; // Handle odd-width cases
                String centeredName = "|" + " ".repeat(padding) + name + " ".repeat(padding + extraSpace) + "|";
                System.out.println(centeredName);
            }
            System.out.println(blankLine);
        }
        System.out.println(borderLine);
    }
    /**
     * Clears the console screen. The exact behavior may depend on the runtime environment.
     * Uses the utility method from {@link AsciiUtils} for cross-platform support.
     */
    default void clearScreen() {
        AsciiUtils.clearScreen();
    }

    /**
     * Executes the menu logic, accepting user input to navigate or perform actions.
     *
     * @param inputStream The {@link InputStream} to read user input from.
     * @return An integer indicating the result of the menu's execution (e.g., exit code).
     */
    int run(InputStream inputStream);
}
