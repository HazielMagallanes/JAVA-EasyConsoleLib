package ar.com.ghostix.lib.easyconsolelib.easyconsoleinput;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Scanner;

/**
 * A specialized console input utility class extending {@link ConsoleInput}.
 * This class provides additional functionality for dynamic object creation and array handling using reflection.
 * It simplifies user input for constructor parameters and automatically handles different data types.
 */
public class GenericInput extends ConsoleInput {
    // Class constructors

    /**
     * Constructs a {@code GenericInput} instance using the standard input stream {@code System.in}.
     */
    public GenericInput() {
        super(System.in);
    }

    /**
     * Constructs a {@code GenericInput} instance using a custom {@link InputStream}.
     *
     * @param inputStream The input stream for reading user inputs.
     */
    public GenericInput(InputStream inputStream) {
        super(inputStream);
    }

    /**
     * Constructs a {@code GenericInput} instance using a specified {@link Scanner}.
     *
     * @param scan The {@link Scanner} object used for input handling.
     */
    public GenericInput(Scanner scan) {
        super(scan);
    }
    // Private

    /**
     * Retrieves the most suitable constructor for a given class, preferring the one with the highest number of parameters.
     *
     * @param <T>   The type of the class.
     * @param clazz The class whose constructor will be retrieved.
     * @return The most parameterized {@link Constructor} for the specified class.
     * @throws IllegalArgumentException If no public constructor is found.
     */
    private <T> Constructor<?> getConstructor(Class<T> clazz) {
        // Get the class constructor with the most parameters
        Constructor<?>[] constructors = clazz.getConstructors();
        Constructor<?> constructor;
        if (constructors.length == 0) {
            throw new IllegalArgumentException("No public constructor found for class: " + clazz.getName());
        }
        if (constructors.length == 1) {
            constructor = constructors[0];
        } else {
            Constructor<?> highestNumberOfParameters = constructors[0];
            for (Constructor<?> i : constructors) {
                if (i.getParameterCount() > highestNumberOfParameters.getParameterCount()) {
                    highestNumberOfParameters = i;
                }
            }
            constructor = highestNumberOfParameters;
        }
        return constructor;
    }
    // Public

    /**
     * Creates an instance of a given class by prompting the user to provide values for the constructor parameters.
     *
     * @param <T>   The type of object to create.
     * @param clazz The class to create an instance of.
     * @return A new instance of the specified class, with constructor arguments provided by the user.
     * @throws RuntimeException If the instance creation fails due to reflection errors.
     */
    public <T> T createInstance(Class<T> clazz) {
        try {
            // Get the right constructor
            Constructor<?> constructor = getConstructor(clazz);
            Parameter[] parameters = constructor.getParameters();

            // Array to store user-provided constructor arguments
            Object[] args = new Object[parameters.length];

            // Ask the user for each parameter.
            for (int i = 0; i < parameters.length; i++) {
                Class<?> paramType = parameters[i].getType();
                args[i] = getParameterInput(paramType, "Ingrese valor para el parametro: " + parameters[i].getName() + " (" + paramType.getSimpleName() + ")\n");
            }

            // Create a new instance of the class with the arguments
            return (T) constructor.newInstance(args);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException error) {
            error.printStackTrace(System.out);
            throw new RuntimeException("Failed to create instance of " + clazz.getName());
        }
    }

    /**
     * Prompts the user to provide input for a given parameter type and returns the value as an {@code Object}.
     * If the parameter type is an array, it will call {@link #createArray(Class, String)} instead.
     *
     * @param parameterType The type of the parameter.
     * @param parameterName The name of the parameter for user display purposes.
     * @return The user-provided value for the parameter.
     */
    public Object getParameterInput(Class<?> parameterType, String parameterName) {
        String prompt = "Introduzca un valor para: " + parameterName + " (" + parameterType.getSimpleName() + "): \n";
        boolean confirm = true;

        if (parameterType.isArray()) {
            // If the parameter is an array, call createArray.
            return createArray(parameterType.getComponentType(), parameterName);
        }
        return switch (parameterType.getSimpleName()) {
            case "String" -> nextLine(prompt, confirm);
            case "int", "Integer" -> nextInt(prompt, confirm);
            case "double", "Double" -> nextDouble(prompt, confirm);
            case "float", "Float" -> nextFloat(prompt, confirm);
            case "boolean", "Boolean" -> nextBoolean(prompt, confirm);
            case "long", "Long" -> nextLong(prompt, confirm);
            case "byte", "Byte" -> nextByte(prompt, confirm);
            case "short", "Short" -> nextShort(prompt, confirm);
            case "BigDecimal" -> nextBigDecimal(prompt, confirm);
            case "BigInteger" -> nextBigInteger(prompt, confirm);
            case "InputStream" -> System.in;
            case "PrintStream" -> System.out;
            default -> {
                System.out.println("Creando instancia del tipo personalizado: " + parameterType.getSimpleName());
                yield createInstance(parameterType);
            }
        };
    }

    /**
     * Prompts the user to create and fill an array with the specified component type.
     *
     * @param componentType The type of the array elements.
     * @param parameterName The name of the array parameter for user display purposes.
     * @return An array of the specified component type filled with user-provided values.
     */
    public Object createArray(Class<?> componentType, String parameterName) {
        String prompt = "Introduzca el tamaño del array para: " + parameterName + " (" + componentType.getSimpleName() + "): ";
        int size = nextInt(prompt, true);
        Object array = Array.newInstance(componentType, size);
        for (int i = 0; i < size; i++) {
            String elementPrompt = parameterName + "[" + i + "]\n";
            Array.set(array, i, getParameterInput(componentType, elementPrompt));
        }
        return array;
    }

    /**
     * Fills an existing array by prompting the user to provide values for each element.
     *
     * @param <T>           The type of the array elements.
     * @param elementsName  The name of the array elements for user display purposes.
     * @param array         The array to be filled with user-provided values.
     * @param elementsClass The class of the array elements.
     * @return The modified array with user-filled values.
     */
    public <T> T[] fillArray(String elementsName, T[] array, Class<T> elementsClass) {
        int size = array.length;
        for (int i = 0; i < size; i++) {
            String elementPrompt = elementsName + "[" + i + "]\n";
            Array.set(array, i, getParameterInput(elementsClass, elementPrompt));
        }
        return array;
    }
}

