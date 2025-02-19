package ar.com.ghostix.lib.easyconsolelib.menus;

import ar.com.ghostix.lib.easyconsolelib.easyconsoleinput.GenericInput;

import java.io.InputStream;
import java.lang.reflect.*;
import java.util.*;

/**
 * Represents a submenu in a console application that manages and executes methods or options for a specific object.
 * Provides support for custom options, method exclusion, and dynamic interaction with the user.
 */
public class SubMenu implements IAsciiMenu {

    private String name; // Title of the submenu
    private TreeMap<String, Method> methods; // Methods available for interaction
    private Object object; // The target object whose methods are invoked
    private int options; // Number of available options
    private boolean custom; // Indicates if custom options are present
    private boolean handlingTitle; // Controls automatic title rendering
    private TreeMap<String, String> customOptions; // Custom options mapped by display name
    private int exit; // Index of the exit option
    private int width = 22; // Width of the submenu
    private int height = 7; // Height of the submenu
    private Object[] methodKeysAsArray; // Helper to store method keys for quick access

    /**
     * Constructs a SubMenu with default settings.
     *
     * @param object The object whose methods are invoked by this submenu.
     */
    public SubMenu(Object object) {
        this("SubMenu", object, null, null);
    }

    /**
     * Constructs a SubMenu with a specified title and object.
     *
     * @param name   The title of the submenu.
     * @param object The object whose methods are invoked by this submenu.
     */
    public SubMenu(String name, Object object) {
        this(name, object, null, null);
    }

    /**
     * Constructs a SubMenu with custom options.
     *
     * @param name          The title of the submenu.
     * @param object        The object whose methods are invoked by this submenu.
     * @param customOptions Array of custom options to include in the submenu.
     */
    public SubMenu(String name, Object object, String[] customOptions) {
        this(name, object, customOptions, null);
    }

    /**
     * Constructs a SubMenu with custom and hidden options.
     *
     * @param name          The title of the submenu.
     * @param object        The object whose methods are invoked by this submenu.
     * @param customOptions Array of custom options to include in the submenu.
     * @param hiddenOptions Array of methods to hide from the submenu.
     */
    public SubMenu(String name, Object object, String[] customOptions, String[] hiddenOptions) {
        this(name, object, customOptions, hiddenOptions, true);
    }

    /**
     * Constructs a SubMenu with full customization options.
     *
     * @param name          The title of the submenu.
     * @param object        The object whose methods are invoked by this submenu.
     * @param customOptions Array of custom options to include in the submenu.
     * @param hiddenOptions Array of methods to hide from the submenu.
     * @param handlingTitle Whether the submenu automatically renders its title.
     */
    public SubMenu(String name, Object object, String[] customOptions, String[] hiddenOptions, boolean handlingTitle) {
        this.name = name;
        this.object = object;
        this.options = 0;
        this.custom = customOptions != null;
        this.customOptions = new TreeMap<>();
        this.handlingTitle = handlingTitle;
        initializeMethodsList(hiddenOptions);
        initializeCustomOptions(customOptions);
    }

    /**
     * Gets the title of the submenu.
     *
     * @return The title of the submenu.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new title for the submenu.
     *
     * @param newValue The new title to set.
     */
    public void setName(String newValue) {
        name = newValue;
    }

    /**
     * Retrieves the list of methods available in the submenu.
     *
     * @return A {@link TreeMap} containing method names as keys and their respective {@link Method} objects as values.
     */
    public TreeMap<String, Method> getMethods() {
        return methods;
    }

    /**
     * Sets a new list of methods for the submenu and reinitializes the methods list.
     *
     * @param newValues A {@link TreeMap} containing method names and their respective {@link Method} objects.
     */
    public void setMethods(TreeMap<String, Method> newValues) {
        this.methods = newValues;
        options = 0;
        initializeMethodsList(null);
    }

    /**
     * Retrieves the target object associated with the submenu.
     *
     * @return The object whose methods can be invoked through the submenu.
     */
    public Object getObject() {
        return object;
    }

    /**
     * Sets a new target object for the submenu.
     *
     * @param newValue The new object whose methods will be managed by the submenu.
     */
    public void setObject(Object newValue) {
        this.object = newValue;
    }

    /**
     * Gets the number of options available in the submenu (including standard methods and custom options).
     *
     * @return The total number of options available in the submenu.
     */
    public int getOptions() {
        return options;
    }

    /**
     * Sets the number of options available in the submenu.
     *
     * @param newValue The new number of options to set.
     */
    public void setOptions(int newValue) {
        this.options = newValue;
    }

    /**
     * Checks whether the submenu has custom options enabled.
     *
     * @return {@code true} if custom options are enabled, {@code false} otherwise.
     */
    public boolean isCustom() {
        return custom;
    }

    /**
     * Sets whether the submenu should allow custom options.
     *
     * @param newValue {@code true} to enable custom options, {@code false} to disable them.
     */
    public void setCustom(boolean newValue) {
        this.custom = newValue;
    }

    /**
     * Retrieves the list of custom options available in the submenu.
     *
     * @return A {@link TreeMap} containing the custom options with their names as keys.
     */
    public TreeMap<String, String> getCustomOptions() {
        return customOptions;
    }

    /**
     * Sets the custom options available in the submenu and reinitializes them.
     *
     * @param newValues An array of strings representing the new custom options.
     */
    public void setCustomOptions(String[] newValues) {
        initializeCustomOptions(newValues);
    }

    /**
     * Retrieves the index of the exit option in the submenu.
     *
     * @return The index of the exit option.
     */
    public int getExit() {
        return this.exit;
    }

    /**
     * Sets the index of the exit option for the submenu.
     *
     * @param newValue The new index for the exit option.
     */
    public void setExit(int newValue) {
        this.exit = newValue;
    }

    /**
     * Updates the exit option based on the current number of options and custom options.
     */
    public void updateExit() {
        calculateExit();
    }

    /**
     * Retrieves the current width of the submenu display.
     *
     * @return The width of the submenu display.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the submenu display.
     *
     * @param width The new width to set for the submenu display.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Retrieves the current height of the submenu display.
     *
     * @return The height of the submenu display.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the submenu display.
     *
     * @param height The new height to set for the submenu display.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Checks whether the submenu automatically displays its title.
     *
     * @return {@code true} if the submenu automatically displays its title, {@code false} otherwise.
     */
    public boolean isHandlingTitle() {
        return handlingTitle;
    }

    /**
     * Sets whether the submenu should automatically display its title.
     *
     * @param handlingTitle {@code true} to enable automatic title rendering, {@code false} to disable it.
     */
    public void setHandlingTitle(boolean handlingTitle) {
        this.handlingTitle = handlingTitle;
    }
    // Private methods
    /**
     * Initializes the list of methods available in the submenu, filtering out hidden options and irrelevant methods.
     *
     * @param hiddenOptions An array of method names to exclude from the menu.
     */
    private void initializeMethodsList(String[] hiddenOptions) {
        Method[] methodsToCheck = this.object.getClass().getMethods();
        methods = new TreeMap<>();
        HashSet<String> excludedMethods = new HashSet<>(Set.of(
                "run", "wait", "equals", "toString", "hashCode", "getClass", "notify", "notifyAll"
        ));

        if (hiddenOptions != null) {
            Collections.addAll(excludedMethods, hiddenOptions);
        }
        // Exclude non-relevant methods from the list.
        for (Method method : methodsToCheck) {
            String methodName = method.getName();
            if (methodName.startsWith("get") || methodName.startsWith("set")) continue;
            if (excludedMethods.contains(methodName)) continue;
            if (customOptions.containsKey(methodName)) continue;
            methods.put(methodName, method);
            options++;
        }
        methodKeysAsArray = methods.keySet().toArray();
        exit = calculateExit();
    }
    /**
     * Initializes the custom options for the submenu.
     *
     * @param customOptionsArray An array of custom options to add to the submenu.
     */
    private void initializeCustomOptions(String[] customOptionsArray) {
        customOptions.clear();
        if (customOptionsArray != null) {
            for (String option : customOptionsArray) {
                customOptions.put(option, option);
            }
        }
    }
    /**
     * Calculates the index of the exit option based on the number of methods and custom options available.
     *
     * @return The index of the exit option.
     */
    private int calculateExit() {
        return isCustom() ? getOptions() + customOptions.size()  + 1 : getOptions();
    }

    // Public instance methods
    // Change display names related

    /**
     * Updates the display name of a single method in the submenu.
     *
     * @param currentName The current name of the method.
     * @param newName     The new display name to assign.
     * @throws IllegalArgumentException If the method name does not exist in the current list.
     */
    public void updateMethodDisplayName(String currentName, String newName) throws IllegalArgumentException{
        if (methods.containsKey(currentName)) {
            Method method = methods.remove(currentName);
            methods.put(newName, method);
            methodKeysAsArray = methods.keySet().toArray();
            return;
        }
        throw new IllegalArgumentException("Error: Method display name not found.");
    }

    /**
     * Updates the display name of a single custom option in the submenu.
     *
     * @param currentName The current name of the custom option.
     * @param newName     The new display name to assign.
     * @throws IllegalArgumentException If the custom option does not exist in the current list.
     */
    public void updateCustomOptionDisplayName(String currentName, String newName) throws IllegalArgumentException{
        if (customOptions.containsKey(currentName)) {
            String optionValue = customOptions.remove(currentName);
            customOptions.put(newName, optionValue);
            return;
        }
        throw new IllegalArgumentException("Error: Custom option display name not found.");
    }

    /**
     * Updates the display names of all methods in batch using a map of new keys.
     *
     * @param newKeys A {@link TreeMap} where the key is the old method name, and the value is the new name.
     * @throws IllegalArgumentException If the size of the map does not match the number of methods.
     */
    public void updateMethodDisplayNames(TreeMap<String, String> newKeys) throws IllegalArgumentException{
        if (newKeys.size() != methods.size()) {
            throw new IllegalArgumentException("Error: Mismatch in the number of new keys provided.");
        }
        TreeMap<String, Method> updatedMethods = new TreeMap<>();
        for (String currentKey : newKeys.keySet()) {
            if(methods.containsKey(currentKey)){
                updatedMethods.put(newKeys.get(currentKey), methods.get(currentKey));
            }
        }
        methods = updatedMethods;
        methodKeysAsArray = methods.keySet().toArray();
    }

    /**
     * Updates the display names for all custom options in batch using a set of new keys.
     *
     * @param newKeys A {@link Set<String>} containing the new display names for the custom options.
     * @throws IllegalArgumentException If the size of the set does not match the number of custom options.
     */
    public void updateCustomOptionDisplayNames(Set<String> newKeys) throws IllegalArgumentException {
        if (newKeys.size() != customOptions.size()) {
            throw new IllegalArgumentException("Error: Mismatch in the number of new keys provided.");
        }

        TreeMap<String, String> updatedCustomOptions = new TreeMap<>();
        int i = 0;
        for (String currentKey : customOptions.keySet()) {
            String newKey = (String) newKeys.toArray()[i++];
            updatedCustomOptions.put(newKey, customOptions.get(currentKey));
        }
        customOptions = updatedCustomOptions;
    }

    /**
     * Prompts the user for method parameters using the provided {@link GenericInput}.
     *
     * @param methodKey The key of the method to collect parameters for.
     * @param input     The input handler for reading user inputs.
     * @return An array of objects representing the collected parameters.
     */
    // TODO: Make this reusable and add it to AsciiUtils class.
    private Object[] askParameters(String methodKey, GenericInput input) {
        Parameter[] methodParams = methods.get(methodKey).getParameters();
        Object[] parameters = new Object[methodParams.length];

        int i = 0;
        while (i < methodParams.length) {
            System.out.println("==========================");
            System.out.printf("ARGUMENTO NÚMERO: %d%n", (i + 1));
            System.out.println("==========================");
            System.out.println("Introducción automatica de parametros.");
            System.out.println("1- Seguir introduciendo. ");
            System.out.println("2- Retroceder. ");
            System.out.println("==========================");

            int type = input.nextInt("Selecciona una opción.\n", false);
            String isSure = input.nextLine("Estas seguro?. Si no es así escribe DESHACER\n", false);

            if ("DESHACER".equals(isSure)) {
                System.out.println("Retrocediendo.");
                if (i > 0) i--;  // Step back
                continue;
            }

            switch (type) {
                case 1 -> {
                    parameters[i] = input.getParameterInput(methodParams[i].getType(), methodParams[i].getName());
                    i++;
                }
                case 2 -> {
                    System.out.println("Retrocediendo...");
                    if (i > 0) i--;  // Only go back if not at start
                }
                default -> System.out.println("Opción invalida. Vuelve a ingresarla.");
            }
        }
        return parameters;
    }


    /**
     * Runs the submenu, displaying options and handling user selections.
     *
     * @param inputStream The input stream to read user inputs.
     * @return The index of the selected option or the exit code.
     */
    @Override
    public int run(InputStream inputStream) {
        GenericInput input = new GenericInput(inputStream);
        printTitle(getName(), getWidth(), getHeight());

        int index = 1;
        // Display all the options, first the normal ones and then the custom.
        for (String methodName : methods.keySet()) {
            System.out.println(index + "- " + methodName + ".");
            index++;
        }
        for (String customOption : customOptions.keySet()) {
            System.out.println(index + "- " + customOption + ".");
            index++;
        }
        System.out.println(getExit() + "- Salir.");
        System.out.println("==========================");

        int option = input.nextInt("Selecciona una opción.\n", false);
        clearScreen();
        if (option > 0 && option < getOptions()) {
            String selectedMethodKey = (String) methodKeysAsArray[option - 1];
            Method selectedMethod = methods.get(selectedMethodKey);
            try {
                // Invoke the selected method if not one of the customs, the askParameters method retrieves all parameters.
                Object[] parameters = askParameters(selectedMethodKey, input);
                selectedMethod.invoke(object, parameters);
            } catch (Exception error) {
                error.printStackTrace(System.out);
                System.out.println("Algo salió mal...");
            }
        } else if (option == getExit()) {
            System.out.println("Cerrando el programa.");
        } else if (option > getExit() || option < 1){
            System.out.println("Opción inválida.");
        }
        // Return the option for menus with custom options.
        return option;
    }
}
