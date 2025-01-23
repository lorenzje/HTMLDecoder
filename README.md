
# Secret Decoder

## Description
The **Secret Decoder** program fetches an HTML document from a specified URL, parses a table structure to extract character positions, and displays the decoded grid. Each table row contains x and y coordinates, along with a character, which are mapped onto a 2D grid and printed.

## Features
- Fetches HTML content from a URL.
- Parses a specific HTML table format to extract coordinates and characters.
- Generates a 2D grid based on the extracted data.
- Prints the grid with characters placed at their respective coordinates.

## Requirements
- Java 8 or higher

## Usage
1. Clone the repository or download the source code.
2. Ensure Java is installed on your machine.
3. Replace the URL in the `main` method with the desired document URL containing the table.
4. Compile and run the `SecretDecoderMain.java` file.

### Example Command:
```bash
javac SecretDecoderMain.java
java SecretDecoderMain
```

## Code Explanation
- **fetchDocumentContent(String docUrl)**: Fetches HTML content from the provided URL using an HTTP GET request.
- **parseCharacterPositionsFromHtml(String htmlContent)**: Uses regular expressions to extract x and y coordinates and associated characters from the table.
- **createGrid(List<CharacterPosition> positions)**: Constructs a 2D grid of characters using the extracted positions.
- **printGrid(char[][] grid)**: Prints the final grid to the console.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

