<div align="center">
  <a href="https://github.com/CARLOKO98/Pokemon">
    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/International_Pok%C3%A9mon_logo.svg/2560px-International_Pok%C3%A9mon_logo.svg.png" alt="Pokémon Logo" width="400">
  </a>

  <h1 align="center">Pokémon Scala Edition</h1>

  <p align="center">
    Ein rundenbasiertes Pokémon-Spiel mit Fokus auf Clean Architecture.
    <br />
    <a href="#about-the-project"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="#usage">View Demo</a>
    ·
    <a href="https://github.com/CARLOKO98/Pokemon/issues">Report Bug</a>
    ·
    <a href="https://github.com/CARLOKO98/Pokemon/issues">Request Feature</a>
  </p>

  [![Scala CI](https://github.com/CARLOKO98/Pokemon/actions/workflows/scala.yml/badge.svg)](https://github.com/CARLOKO98/Pokemon/actions/workflows/scala.yml)
  [![Coverage Status](https://coveralls.io/repos/github/carloko98/Pokemon/badge.svg?branch=main)](https://coveralls.io/github/carloko98/Pokemon?branch=main)
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#features">Features</a></li>
    <li><a href="#contributors">Contributors</a></li>
  </ol>
</details>

---

## About The Project

Dieses Projekt ist eine Implementierung des klassischen Pokémon-Spielprinzips in **Scala**. Das Hauptziel war die Anwendung fortgeschrittener Software-Engineering-Prinzipien, insbesondere des **MVC-Musters** (Model-View-Controller) und verschiedener Design Patterns.

Es bietet sowohl eine grafische Oberfläche (**GUI**) als auch eine textbasierte Konsole (**TUI**), zwischen denen nahtlos gewechselt werden kann.

### Built With

* ![Scala](https://img.shields.io/badge/scala-%23DC322F.svg?style=for-the-badge&logo=scala&logoColor=white)
* ![SBT](https://img.shields.io/badge/sbt-%23005596.svg?style=for-the-badge&logo=sbt&logoColor=white)
* ![JavaFX](https://img.shields.io/badge/JavaFX-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=white)
* ![Coveralls](https://img.shields.io/badge/coveralls-%23C83634.svg?style=for-the-badge&logo=coveralls&logoColor=white)

---

## Getting Started

Um eine lokale Kopie zu starten, befolge diese einfachen Schritte.

### Prerequisites

Du benötigst Java und SBT (Scala Build Tool).

* **Java JDK 11+**
* **SBT**

### Installation

1.  Clone das Repo
    ```sh
    git clone [https://github.com/CARLOKO98/Pokemon.git](https://github.com/CARLOKO98/Pokemon.git)
    ```
2.  Wechsle in das Verzeichnis
    ```sh
    cd Pokemon
    ```
3.  Starte das Spiel
    ```sh
    sbt run
    ```
4.  (Optional) Tests ausführen
    ```sh
    sbt test
    ```

---

## 🐳 Docker Setup & Deployment

Dieses Projekt verwendet Docker, um die Anwendung inklusive aller Abhängigkeiten (Java, Scala, JavaFX Bibliotheken) isoliert auszuführen. Da die Anwendung eine grafische Oberfläche (GUI) besitzt, ist ein X-Server auf dem Host-System notwendig.

### 1. Image bauen

Bevor der Container gestartet werden kann, muss das Image erstellt werden. Führe diesen Befehl im Hauptverzeichnis des Projekts aus:

```bash
docker build -t pokemon:v1 .

```

---

### 2. Ausführen auf Windows 🪟

Da Docker unter Windows keine native GUI-Unterstützung hat, wird ein X-Server benötigt.

**Schritt A: X-Server installieren & konfigurieren**

1. Installiere **VcXsrv** (XLaunch).
2. Starte **XLaunch** mit folgenden Einstellungen:
* **Display settings:** "Multiple windows"
* **Client startup:** "Start no client"
* **Extra settings:** ✅ **Disable access control** (WICHTIG! Haken setzen, sonst blockiert Windows die Verbindung).



**Schritt B: Container starten**
Öffne die PowerShell und führe folgenden Befehl aus:

```powershell
docker run -ti --rm -e DISPLAY=host.docker.internal:0.0 pokemon:v1

```

*(Falls `host.docker.internal` nicht funktioniert, nutze deine IPv4-Adresse, z.B. `-e DISPLAY=192.168.178.xx:0.0`)*.

---

### 3. Ausführen auf Linux 🐧

Unter Linux (z.B. Kali, Ubuntu) kann der X-Server des Systems direkt genutzt werden.

**Schritt A: Berechtigung erteilen**
Erlaube dem Docker-Container den Zugriff auf den Bildschirm (muss einmal pro Session ausgeführt werden):

```bash
xhost +local:docker

```

**Schritt B: Container starten**

```bash
docker run -ti --rm -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix pokemon:v1

```

---

## Usage

Das Spiel kann komplett über die TUI (Terminal) oder die GUI gesteuert werden.

### Steuerung (Dashboard)

| Taste | Aktion | Beschreibung |
| :---: | :--- | :--- |
| **W** | `Wilder Kampf` | Startet einen Kampf gegen ein zufälliges wildes Pokémon. |
| **T** | `Trainer Kampf` | Startet einen schweren Kampf gegen einen Trainer (6 Pokémon). |
| **C** | `PokéCenter` | Heile dein Team oder kaufe Items (WIP). |
| **Q** | `Quit` | Beendet das Spiel. |

### Im Kampf

* Wähle Attacken mit **1-4** (TUI) oder per Mausklick (GUI).
* Wechsle Pokémon oder Fliehe bei Bedarf.
* Nutze **Z** für `Undo` und **Y** für `Redo`.

---

## Features

- [x] Vollständiges MVC-Pattern
- [x] GUI (ScalaFX) und TUI parallel
- [x] Kampfsystem mit Typ-Effektivität
- [x] PokéCenter zum Heilen
- [x] Speichern & Laden (Persistence) mit JSON/XML
- [x] Undo / Redo Manager
- [ ] Items & Shop System
- [ ] Level-Up System
- [ ] Animationen

---

## Contributors

* **Carlo Kornmeyer** - [GitHub Profil](https://github.com/carloko98)
* **Marco Pappalardo** - [GitHub Profil](https://github.com/Marco-Pappalardo)

Project Link: [https://github.com/CARLOKO98/Pokemon](https://github.com/CARLOKO98/Pokemon)