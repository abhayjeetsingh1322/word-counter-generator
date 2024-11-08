# Word Counter HTML Generator

## Description
The **Word Counter HTML Generator** is a Java application that processes a text file, counts the occurrences of each word, and generates an HTML file displaying the words and their counts in a well-organized table. The project emphasizes precision, modularity, and clean design while leveraging core Java components.

---

## Objectives
1. Design and implement a complete Java application for text processing.
2. Apply knowledge of string manipulation and dynamic HTML generation.
3. Ensure high-quality, modular, and maintainable code.

---

## Features
### 1. Input File
- Accepts any plain text file as input.
- Words are identified based on a customizable definition of separators.
- Reads file paths as either relative or absolute.

### 2. HTML Output
- Generates a well-structured HTML file containing:
  - A heading with the name of the input file.
  - A table listing words and their respective counts in **alphabetical order**.

### 3. User Interaction
- Prompts the user for:
  - The name (or path) of the input text file.
  - The desired name (or path) of the output HTML file.

### 4. Error Handling
- Ensures user-provided paths are respected without modification or augmentation.
- Handles edge cases such as empty input files or invalid file paths gracefully.

---

## Technologies Used
- **Java**: For text processing and HTML generation.
- **Standard libraries**: For file I/O and data structures.

---

## How to Run
### Prerequisites
- Java Development Kit (JDK)
- Any Java-compatible IDE or terminal

### Steps
1. Clone the repository:
   ```bash
   git clone [repository URL]
