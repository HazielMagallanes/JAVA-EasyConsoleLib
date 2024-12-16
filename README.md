# EasyConsoleLib

🎉 EasyConsoleLib is a Java library designed to simplify the creation and management of ASCII menus in console applications. It provides utilities for automating menus, handling user input, and dynamically executing methods, making it ideal for college or high school projects. By eliminating the need for manually coding menus, EasyConsoleLib ensures a standardized, reusable, and user-friendly approach to program testing. 🎉

---

## Features

🎯 **Main Menu Automation**:
  - Dynamically execute `SubMenu` objects or other menu-compatible classes.
  - Customizable menu titles and options.
  - Integrated object management and dynamic method invocation.

🎯 **SubMenu Utility**:
  - Execute all or selected methods of a given object.
  - Customize method visibility and execution.
  - Automatic user input handling for method parameters.

🎯 **IAsciiMenu Interface**:
  - Provides utility methods for ASCII menu design, including title printing and screen clearing.
  - Standardized `run` method for uniform menu execution.

---

## Installation

📦 1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
📦 2. Add the library to your Java project:
   - Include the JAR file in your project's build path.
   - Alternatively, copy the source files into your project.

---

## Usage

### Example: Main Menu

```java
package ar.com.ghostix.lib.easyconsolelib;

import ar.com.ghostix.lib.easyconsolelib.examples.HiddenOptionsExample;
import ar.com.ghostix.lib.easyconsolelib.examples.Point;
import ar.com.ghostix.lib.easyconsolelib.menus.MainMenu;

import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Object[] options = {new HiddenOptionsExample(), new Point(0,0)};
        MainMenu menu = new MainMenu("Easy Console Lib!", options);
        HashSet<String> optionsNames = new HashSet<>(List.of(
                "Ejemplo de opciones ocultas",
                "Ejemplo de opciones custom y auto rellenado"));
        menu.updateOptionsDisplayNames(optionsNames);
        menu.setWidth(128);
        menu.run(System.in);
    }
}
```

### Example: SubMenu

```java
package ar.com.ghostix.lib.easyconsolelib.examples;

import ar.com.ghostix.lib.easyconsolelib.menus.SubMenu;

import java.io.InputStream;

public class HiddenOptionsExample {
    private int foo;
    private String bar;

    //Constructors.
    public HiddenOptionsExample() {
        foo = 0;
        bar = "";
    }

    //Getters and Setters.
    public int getFoo() {
        return foo;
    }
    public void setFoo(int foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }
    public void setBar(String bar) {
        this.bar = bar;
    }

    //Instance methods.
    public void updateFoo(int value){
        setFoo(value);
    }
    public void updateBar(String value){
        setBar(value);
    }
    public void printFoo(){
        System.out.println("Foo: " + getFoo());
    }
    public void printBar(){
        System.out.println("Bar: " + getBar());
    }
    public void printValues(){
        System.out.println("Foo: " + getFoo());
        System.out.println("Bar: " + getBar());
    }
    //Run method
    public void run(InputStream inputStream){
        String[] hiddenOptions = {"printFoo", "printBar"};
        SubMenu menu = new SubMenu("Hidden options Example", this, null, hiddenOptions);
        // Simple example using while loop instead of for.
        int option = 0;
        while(option != menu.getExit()){
            option = menu.run(inputStream);
        }
    }
}
```

```java
package ar.com.ghostix.lib.easyconsolelib.examples;

import java.io.InputStream;
import java.lang.Math;
import java.util.*;

import ar.com.ghostix.lib.easyconsolelib.menus.SubMenu;
import ar.com.ghostix.lib.easyconsoleinput.GenericInput;

public class Point
{
    
    private final int[] coordinates = new int[2];
    
    
    // Class constructors
    public Point(){
        this(0,0);
    }
    public Point(int x, int y){
        coordinates[0] = x;
        coordinates[1] = y;
    }
    
    public int[] getCoordinates(){
        return coordinates;
    }
    public void setCoordinates(int x, int y){
        coordinates[0] = x;
        coordinates[1] = y;
    }
    
    public void addValue(int value){
        setCoordinates(getCoordinates()[0] + value, getCoordinates()[1] + value);
    }
    public void addPoint(Point point){
        setCoordinates(getCoordinates()[0] + point.getCoordinates()[0], getCoordinates()[1] + point.getCoordinates()[1]);
    }
    public double getDistanceFrom(Point point){
        return Math.sqrt((Math.pow(point.getCoordinates()[0] - getCoordinates()[0], 2)
        + Math.pow(point.getCoordinates()[1] - getCoordinates()[1], 2))); // Pythagorean distance calc.
    }
    public double getOriginDistance(){
        return Math.sqrt(Math.pow(getCoordinates()[0], 2) + Math.pow(getCoordinates()[1], 2));
    }
    public void showValues(){
        System.out.println("Coordinates: " + getCoordinates()[0] + ", " + getCoordinates()[1]);
        System.out.println("Distance from origin: " + getOriginDistance());
    }
    public void run(InputStream inputStream){
        GenericInput input = new GenericInput(inputStream);
        String[] customOptions = {"getOriginDistance"};
        String[] hiddenOptions = {"getDistanceFrom"};
        SubMenu menu = new SubMenu("Ejercicio 1", this, customOptions, hiddenOptions);
        menu.updateMethodDisplayNames(new TreeMap<String, String>(Map.of(
                "addPoint", "Sumar un punto",
                "addValue", "Sumar un valor",
                "showValues", "Mostrar valores actuales")));
        menu.updateCustomOptionDisplayName("getOriginDistance", "Mostrar distancia al origen");
        for(int option = 0; option != menu.getExit(); option = menu.run(inputStream)){
            switch(option){
                case 4 -> {
                    System.out.println("Distancia al origen: " + getOriginDistance());
                }
            }
        }
    }
}
```

---

## Documentation

### 1. **IAsciiMenu**
🎨 **Purpose**: A common interface for menus, providing utility methods and a standardized `run` method. 🎨
- **Methods**:
  - `void printTitle(String name)`: Prints a centered title with a default border size.
  - `void printTitle(String name, int width, int height)`: Prints a centered title with custom width and height.
  - `void clearScreen()`: Clears the console screen (with IDE compatibility).
  - `int run(InputStream inputStream)`: Executes the menu logic.

### 2. **MainMenu**
🛠️ **Purpose**: A menu for managing and executing multiple objects. 🛠️
- **Constructors**:
  - `MainMenu(String name, String pattern, Object[] objects, boolean handlingTitle)`: Full constructor.
  - `MainMenu(String name, String pattern, Object[] objects)`: Constructor with default title handling.
  - `MainMenu(String name, Object[] objects)`: Constructor with default pattern.
  - `MainMenu(Object[] objects)`: Constructor with default name and pattern.
- **Methods**:
  - `void setName(String name)`: Sets the menu name.
  - `String getName()`: Gets the menu name.
  - `void setObjects(Object[] objects)`: Sets menu objects.
  - `TreeMap<String, Object> getObjects()`: Gets menu objects.
  - `void setPattern(String pattern)`: Sets the option pattern.
  - `String getPattern()`: Gets the option pattern.
  - `void setWidth(int width)`: Sets the menu width.
  - `int getWidth()`: Gets the menu width.
  - `void setHeight(int height)`: Sets the menu height.
  - `int getHeight()`: Gets the menu height.
  - `boolean isHandlingTitle()`: Checks if the menu handles its title.
  - `void setHandlingTitle(boolean handlingTitle)`: Sets title handling.
  - `int run(InputStream inputStream)`: Executes the menu loop.
  - `void updateOptionDisplayName(int index, String newName)`: Updates an option's display name.
  - `void updateOptionsDisplayNames(Set<String> newKeys)`: Updates display names in batch.

### 3. **SubMenu**
📜 **Purpose**: A menu for executing methods of a single object. 📜
- **Constructors**:
  - `SubMenu(String name, Object object, String[] customOptions, String[] hiddenOptions, boolean handlingTitle)`: Full constructor.
  - `SubMenu(String name, Object object, String[] customOptions, String[] hiddenOptions)`: Constructor with default title handling.
  - `SubMenu(String name, Object object, String[] customOptions)`: Constructor with default hidden options.
  - `SubMenu(String name, Object object)`: Constructor with default custom options and hidden options.
  - `SubMenu(Object object)`: Constructor with default name.
- **Methods**:
  - `void setName(String name)`: Sets the submenu name.
  - `String getName()`: Gets the submenu name.
  - `void setMethods(TreeMap<String, Method> methods)`: Sets the methods list.
  - `TreeMap<String, Method> getMethods()`: Gets the methods list.
  - `void setObject(Object object)`: Sets the target object.
  - `Object getObject()`: Gets the target object.
  - `void setOptions(int options)`: Sets the number of options.
  - `int getOptions()`: Gets the number of options.
  - `boolean isCustom()`: Checks if custom options are used.
  - `void setCustom(boolean custom)`: Sets custom options usage.
  - `TreeMap<String, String> getCustomOptions()`: Gets custom options.
  - `void setCustomOptions(String[] customOptions)`: Sets custom options.
  - `void updateCustomOptionDisplayName(String currentName, String newName)`: Updates a custom option's display name.
  - `void updateCustomOptionDisplayNames(Set<String> newKeys)`: Updates custom options display names in batch.
  - `int run(InputStream inputStream)`: Executes the submenu logic.

---

## License

📝 **This project is licensed under the MIT License. See the LICENSE file for details.** 📝

