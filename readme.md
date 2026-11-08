# oop-rpg-game

A Pokémon-inspired **turn-based RPG** built with **Java** and **JavaFX**, developed as part of the *INFO-F307* course at **ULB (Université Libre de Bruxelles)**.

The project showcases the four core pillars of Object-Oriented Programming — **encapsulation**, **inheritance**, **abstraction**, and **polymorphism** — applied to a fully playable graphical game.

---

## Features

- **Turn-based combat** inspired by the Pokémon battle system
- **Graphical interface** built with JavaFX and FXML
- **Multiple character classes** with distinct stats and abilities
- **Data persistence** via JSON (powered by Gson)
- **Automated tests** with JUnit 4 and TestFX for UI testing
- **CI/CD pipeline** via GitLab CI

---

## Tech Stack

| Technology | Version       | Role                              |
|------------|---------------|-----------------------------------|
| Java JDK   | 25            | Core language                     |
| JavaFX     | 25.0.2        | GUI framework                     |
| Maven      | 3.9.x+        | Build & dependency management     |
| Gson       | 2.13.2        | JSON serialization / persistence  |
| JUnit      | 4.13.1        | Unit testing                      |
| TestFX     | 4.0.15-alpha  | JavaFX UI testing                 |
| GitLab CI  | —             | Continuous integration            |

---

## Project Structure

```
oop-rpg-game/
├── src/
│   ├── main/
│   │   ├── java/          # Game logic & controllers
│   │   └── resources/     # FXML views, images, assets
│   └── test/              # Unit and UI tests
├── javadoc/               # Generated API documentation
├── team/                  # Team-related documents
├── pom.xml                # Maven build configuration
├── .gitlab-ci.yml         # CI/CD pipeline definition
└── questions_groupe5.md   # Course Q&A document
```

---

## Getting Started

### Prerequisites

Make sure the following are installed before cloning the project:

- **Java JDK 25** → [Download](https://www.oracle.com/java/technologies/downloads/)
- **JavaFX SDK 25.0.2** → [Download](https://gluonhq.com/products/javafx/)
- **Maven 3.9.x or newer** → [Download](https://maven.apache.org/download.cgi)

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/lnaj23/oop-rpg-game.git
cd oop-rpg-game

# 2. Compile the project
mvn compile

# 3. Run the game
mvn exec:java
```

> **Tip:** Make sure `JAVA_HOME` points to JDK 25 and that JavaFX is on the module path if you encounter startup errors.

---

## Running Tests

The project uses **JUnit 4** for unit tests and **TestFX** for JavaFX UI tests.

```bash
# Run all tests
mvn test

# Package the application (runs tests then creates a shaded JAR)
mvn package
```

The CI pipeline defined in `.gitlab-ci.yml` runs these tests automatically on every push.

---

## OOP Concepts Demonstrated

This project was designed as a hands-on exploration of OOP principles:

- **Encapsulation** — game entities (characters, moves, items) bundle their data and behaviour into self-contained classes.
- **Inheritance** — a shared base class is extended by concrete types such as player characters and enemies, avoiding duplication.
- **Abstraction** — interfaces and abstract classes define contracts that decouple game logic from specific implementations.
- **Polymorphism** — the battle engine calls methods on references and each subclass responds according to its own rules, enabling extensible combat without branching conditionals.

The JavaDoc generated in `/javadoc` documents the full class hierarchy.

---

## Authors

Developed by **Group 5** as part of the *INFO-F307 — Software Engineering* course at **ULB**.

See the `/team` folder for individual contributor information.
