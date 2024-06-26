# Solitaire
Implementation of the Solitaire card game with JavaFX 

Demonstration application used in the book [Introduction to Software Design with Java](https://link.springer.com/book/10.1007/978-3-030-24094-3).

## Building this application

This repository is configured to build automatically in Eclipse with Java 21 and JavaFX 21.

However, when first imported, the project will show a compilation error because the JavaFX dependency is missing.

To add JavaFX:

1. Download [JavaFX 21](https://jdk.java.net/javafx21/);
2. Create a new `User Library` under `Eclipse -> Window -> Preferences -> Java -> Build Path -> User Libraries -> New`. Name it `JavaFX21` and include the jars under the `lib` folder from the location where you extracted the JavaFX download.

The project should then build properly.

## Running this application

Right-click on the project and select `Run As -> Java Application`. Select `Solitaire` from the list. 

To run the tests, select `Run As - > JUnit Test`.

There are also two driver programs, `Driver` and `CrashTest`, which run the application in headless mode (that is, without the GUI).

