package ar.com.ghostix.lib.easyconsolelib.menus;

import ar.com.ghostix.lib.easyconsolelib.easyconsoleinput.ConsoleInput;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeMap;
/**
 * Represents the main menu in a console application, capable of managing and executing submenus or other objects.
 * Provides options to customize the display, dynamically add objects, and invoke methods using reflection.
 */
public class MainMenu implements IAsciiMenu {
    private String name; // The title of the menu.
    private TreeMap<String, Object> objects; // The menu options mapped to their display names.
    private String pattern; // The naming pattern for the menu options.
    private int options; // Total number of menu options, including the exit option.
    private int width = 22; // Default width for the menu.
    private int height = 7; // Default height for the menu.
    private boolean handlingTitle; // Indicates whether the title is automatically displayed.

    // Class constructors
    /**
     * Constructs a MainMenu with a specific title, option pattern, objects, and title handling behavior.
     *
     * @param name          The title of the menu.
     * @param pattern       The naming pattern for menu options.
     * @param objects       An array of objects to include in the menu.
     * @param handlingTitle Whether the title is automatically displayed.
     */
    public MainMenu(String name, String pattern, Object[] objects, boolean handlingTitle) {
        this.name = name;
        this.pattern = pattern;
        this.handlingTitle = handlingTitle;
        initializeMenu(objects);
    }
    /**
     * Constructs a MainMenu with a specific title, option pattern, and objects. Title handling defaults to true.
     *
     * @param name    The title of the menu.
     * @param pattern The naming pattern for menu options.
     * @param objects An array of objects to include in the menu.
     */
    public MainMenu(String name, String pattern, Object[] objects) {
        this(name, pattern, objects, true);
    }

    /**
     * Constructs a MainMenu with a specific title and objects. The option pattern defaults to ""Option "".
     *
     * @param name    The title of the menu.
     * @param objects An array of objects to include in the menu.
     */
    public MainMenu(String name, Object[] objects) {
        this(name, "Option ", objects);
    }

    /**
     * Constructs a MainMenu with default settings. Title defaults to "Main menu".
     *
     * @param objects An array of objects to include in the menu.
     */
    public MainMenu(Object[] objects) {
        this("Main menu", objects);
    }


    // Getters and Setters

    /**
     * Gets the title of the menu.
     *
     * @return The title of the menu.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the title of the menu.
     *
     * @param name The title to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the objects displayed in the menu.
     *
     * @return A TreeMap containing the menu options and their corresponding objects.
     */
    public TreeMap<String, Object> getObjects() {
        return objects;
    }

    /**
     * Sets the objects to be displayed in the menu.
     *
     * @param objects An array of objects to include in the menu.
     */
    public void setObjects(Object[] objects) {
        initializeMenu(objects);
    }

    /**
     * Gets the naming pattern for the menu options.
     *
     * @return The naming pattern as a String.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Sets the naming pattern for the menu options.
     *
     * @param pattern The naming pattern to set.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Gets the width of the menu.
     *
     * @return The width of the menu.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the menu.
     *
     * @param width The width to set.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the menu.
     *
     * @return The height of the menu.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the menu.
     *
     * @param height The height to set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Checks if the menu automatically displays the title.
     *
     * @return True if the title is automatically displayed, false otherwise.
     */
    public boolean isHandlingTitle() {
        return handlingTitle;
    }

    /**
     * Sets whether the menu automatically displays the title.
     *
     * @param handlingTitle True to enable automatic title display, false otherwise.
     */
    public void setHandlingTitle(boolean handlingTitle) {
        this.handlingTitle = handlingTitle;
    }
    // Private methods

    /**
     * Initializes the menu by mapping objects to their display names.
     *
     * @param objects An array of objects to include in the menu.
     */
    private void initializeMenu(Object[] objects) {
        this.objects = new TreeMap<>();
        int i = 1;
        for(Object o : objects) {
            this.objects.put(getPattern() + i, o);
            i++;
        }
        this.options = objects.length + 1; // Includes the exit option.
    }
    /**
     * Prints the available menu options to the console.
     * This includes the titles of the objects and the exit option.
     */
    private void printMenuOptions() {
        if(isHandlingTitle()){ printTitle(getName(), getWidth(), getHeight()); }
        for (int i = 1; i < options; i++) {
            System.out.println(i + "- " + getObjects().keySet().toArray()[i-1] + ".");
        }
        System.out.println(options + "- Salir.");
        System.out.println("==========================");
    }

    /**
     * Invokes the "run" method on the selected object using reflection.
     * The selected object must have a public "run" method that accepts an {@link InputStream} as its parameter.
     *
     * @param inputStream The input stream to pass to the "run" method of the selected object.
     * @param index       The index of the selected object in the menu.
     */
    private void invokeObjectRunMethod(InputStream inputStream, int index) {
        try {
            Object selected = objects.values().toArray()[index];
            Method method = selected.getClass().getMethod("run", InputStream.class);
            method.invoke(selected, inputStream);
        } catch (Exception error) {
            error.printStackTrace(System.out);
        }
    }

    // Public methods
    // Change display names related
    // By one

    /**
     * Updates the display name of a single option in the menu.
     *
     * @param index   The index of the option to update (zero-based).
     * @param newName The new display name for the option.
     * @throws IllegalArgumentException If the index is out of bounds.
     */
    public void updateOptionDisplayName(int index, String newName) throws IllegalArgumentException{
        if (index >= objects.size() || index < 0){
            throw new IllegalArgumentException("Error: Option index out of bounds. TIP: Option indexes start from zero.");
        }
        String selectedOption = (String) objects.keySet().toArray()[index];
        // Save the stored value for removal and insert.
        Object temporarySave = objects.get(selectedOption);
        objects.remove(selectedOption);
        objects.put(newName, temporarySave);
    }

    // In batch
    /**
     * Updates the display names of all options in the menu in bulk.
     *
     * @param newKeys A {@link Set} of new display names for the options.
     * @throws IllegalArgumentException If the number of new keys does not match the number of options.
     */
    public void updateOptionsDisplayNames(Set<String> newKeys) throws IllegalArgumentException{
        if (newKeys.size() != objects.size()) {
            throw new IllegalArgumentException("Error: Mismatch in the number of new keys provided.");
        }
        TreeMap<String, Object> updatedMethods = new TreeMap<>();
        int i = 0;
        for (String currentKey : objects.keySet()) {
            String newKey = (String) newKeys.toArray()[i++];
            updatedMethods.put(newKey, objects.get(currentKey));
        }
        objects = updatedMethods;
    }



    /**
     * Executes the main menu, allowing the user to navigate and select options.
     * Make sure objects in the menu have a public run method with {@link InputStream} as parameter.
     *
     * @param inputStream The input stream to read user input from.
     * @return An integer indicating the result of the menu's execution (e.g., exit code).
     */
    @Override
    public int run(InputStream inputStream) {
        ConsoleInput scan = new ConsoleInput(inputStream);
        while (true) {
            printMenuOptions();
            int selectedOption = scan.nextInt("Selecciona una opción.\n", false);
            clearScreen();
            if (selectedOption > 0 && selectedOption < options) {
                invokeObjectRunMethod(inputStream, selectedOption - 1);
            }else if (selectedOption == options) {
                System.out.println("Cerrando el programa.");
                return 0;
            }else {
                System.out.println("Opción inválida.");
            }
        }
    }

}
