<h1 align="center"><img src="https://github.com/EpitechMscProPromo2026/T-JAV-501-STG_7/blob/main/assets/ui/main/title.png" alt="Odyssey" width="auto" height="auto"/></h1>

Welcome to Odyssey, an exciting space exploration game built with [`libGDX`](https://libgdx.com) and powered by **Gradle**. In this Java-based game, players embark on a thrilling journey through the vastness of space, piloting their own ship, exploring planets, collecting resources, and uncovering mysterious artifacts in dungeons.

## Features

- **Space Exploration:** Traverse a vast and open space filled with unique planets, each offering different challenges and opportunities for exploration.

  <div align="center"><img src="https://github.com/EpitechMscProPromo2026/T-JAV-501-STG_7/blob/main/github/assets/travel_animation.gif" alt="Travel Map" width="auto" height="288"/></div>

- **Ship Upgrades:** Collect resources to enhance and upgrade your spaceship. Improve its capabilities to reach new planets and overcome obstacles.

  <div align="center"><img src="https://github.com/EpitechMscProPromo2026/T-JAV-501-STG_7/blob/main/github/assets/ressources.png" alt="Ressources" width="auto" height="216"/> <img src="https://github.com/EpitechMscProPromo2026/T-JAV-501-STG_7/blob/main/github/assets/upgrade.png" alt="Upgrade" width="auto" height="216"/></div>

- **Dungeon Delving:** Discover hidden dungeons on planets[^1], where valuable artifacts await. Solve puzzles and face challenges to claim these coveted treasures.

  <div align="center"><img src="https://github.com/EpitechMscProPromo2026/T-JAV-501-STG_7/blob/main/github/assets/cave.png" alt="Cave Planet" width="auto" height="216"/> <img src="https://github.com/EpitechMscProPromo2026/T-JAV-501-STG_7/blob/main/github/assets/cave_in.png" alt="Cave In" width="auto" height="216"/></div>

- **Round-Based Fighting System:** A dynamic round-based fighting system to enhance the excitement when facing enemy threats on planets. Engage in strategic battles as you encounter hostile entities during your exploration.

  <div align="center"><img src="https://github.com/EpitechMscProPromo2026/T-JAV-501-STG_7/blob/main/github/assets/fight_animation.gif" alt="Fight" width="auto" height="216"/></div>

- **Artifact Collection:** The ultimate goal is to collect all the artifacts scattered across the universe. Will you be able to uncover every secret hidden in the depths of space?

---

### Semi-Procedural Cave Generation

One of the exciting features of Odyssey is the semi-procedurally generated caves using Cellular Automaton. This technique adds a dynamic and ever-changing element to the game, providing players with unique cave layouts for each playthrough.

#### How it Works

[^1]:The cave generation process utilizes Cellular Automaton, a powerful algorithm that operates on a grid of cells. Through a series of iterative steps, the algorithm creates intricate cave structures by evolving the state of each cell based on predefined rules.

This semi-procedural approach ensures a balance between handcrafted design and unpredictable elements, offering players a fresh and challenging experience in every dungeon.

Feel free to explore the depths of these semi-procedurally generated caves and uncover the hidden artifacts within!

<div align="center"><img src="https://github.com/EpitechMscProPromo2026/T-JAV-501-STG_7/blob/main/github/assets/cave_generation_animation.gif" alt="Cave Generation" width="auto" height="288"/></div>

> In the above image, you can see the semi-procedurally generated caves evolving through the Cellular Automaton algorithm.

---

## Requirements

- Java 8 (Note: The game was developed with Java 17, but it's compatible with Java 8 for execution)

## Getting Started

### Prerequisites

Make sure you have Java 8 installed on your system.

## Run the Game

To play Odyssey, follow these steps:

1. Clone the GitHub repository to your local machine:

    ```bash
    git clone git@github.com:EpitechMscProPromo2026/T-JAV-501-STG_7.git
    ```

2. Open the project using your preferred Integrated Development Environment (IDE) such as **IntelliJ IDEA** or **Eclipse**.

3. Locate and open the `build.gradle` file in the root of the project.

4. Build the project using the IDE's build tools.

5. Navigate to the `desktop/src/odyssey/game` directory within the project.

6. Find the `DesktopLauncher.java` file in the `odyssey/game` directory.

7. Run the `DesktopLauncher` class to start the game.

Now you're ready to embark on your intergalactic adventure in Odyssey!

Now you should be ready to embark on your space exploration adventure with Odyssey!

---

## Roadmap

The development roadmap for Odyssey includes the following milestones:

- **Version 1.0.0: Artifact Hunt**
  - Collect all artifacts and complete the game's primary objective.

- **Future Updates:**
  - Expand the universe with new planets and challenges.
  - Introduce additional ship upgrades and customization options.
  - Enhance the dungeon experience with more intricate puzzles and enemies.

Stay tuned for updates and exciting new features (that will never come)!

## Author
- **Lucas Fixari**
  - GitHub: [github.com/lfixas](https://github.com/lfixas)

## Acknowledgments

Odyssey wouldn't be possible without the support and contributions of the following:

- Thanks to the [libGDX](https://libgdx.com) team for providing a powerful and flexible game development framework.
- Special appreciation to the [Gradle](https://gradle.org) team for simplifying the build process.
- A special thanks to [Epitech Strasbourg](https://www.epitech.eu/) for the opportunity to create this game.

## License

This project is licensed under the [MIT License](LICENSE).

##

Enjoy your intergalactic adventure with Odyssey!
