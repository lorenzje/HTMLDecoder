

//Imports                                               				 |
//-----------------------------------------------------------------------|
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//-----------------------------------------------------------------------|
//CLASS : SecretDecoderMain                                              |
//-----------------------------------------------------------------------|
public class SecretDecoderMain 
{

    static class CharacterPosition 
    {
        char chrCharacter;
        int intX;
        int intY;

        CharacterPosition(char character, int x, int y) 
        {
            this.chrCharacter = character;
            this.intX = x;
            this.intY = y;
        }
    }

    public static void fetchAndPrintGrid(String docUrl) 
    {
        try 
        {
            // Fetch document content
            String content = fetchDocumentContent(docUrl);
            if (content == null) 
            {
                System.out.println("Failed to retrieve document.");
                return;
            }

            // Parse character positions from the HTML table
            List<CharacterPosition> positions = parseCharacterPositionsFromHtml(content);
            if (positions.isEmpty()) 
            {
                System.out.println("No valid character data found.");
                return;
            }

            // Generate the grid
            char[][] grid = createGrid(positions);

            // Print the grid
            printGrid(grid);

        } catch (Exception e) 
        	{
            	System.out.println("An error occurred: " + e.getMessage());
        	}
    }

    private static String fetchDocumentContent(String docUrl) 
    {
        StringBuilder content = new StringBuilder();
        try 
        {
            @SuppressWarnings("deprecation")
			URL url = new URL(docUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) 
            {
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    content.append(line).append("\n");
                }
            }
            
            return content.toString();
            
        } catch (Exception e) 
        	{
            	System.out.println("Error fetching document: " + e.getMessage());
            	return null; // Return null if fetching fails
        	}
    }

    private static List<CharacterPosition> parseCharacterPositionsFromHtml(String htmlContent) {
        List<CharacterPosition> positions = new ArrayList<>();

        // Extract the <table> content
        Pattern tablePattern = Pattern.compile("<table.*?>.*?</table>", Pattern.DOTALL);
        Matcher tableMatcher = tablePattern.matcher(htmlContent);

        if (tableMatcher.find()) 
        {
            String tableContent = tableMatcher.group();
            System.out.println("Extracted Table Content:\n" + tableContent);

            // Extract rows and cells
            Pattern rowPattern = Pattern.compile("<tr.*?>.*?</tr>", Pattern.DOTALL);
            Matcher rowMatcher = rowPattern.matcher(tableContent);

            while (rowMatcher.find()) 
            {
                String row = rowMatcher.group();

                // Extract each cell from the row
                Pattern cellPattern = Pattern.compile("<td.*?>.*?<span.*?>(.*?)</span>.*?</td>", Pattern.DOTALL);
                Matcher cellMatcher = cellPattern.matcher(row);

                List<String> cells = new ArrayList<>();
                while (cellMatcher.find()) 
                {
                    String cellContent = cellMatcher.group(1).trim(); // Extract content inside the <span>
                    cells.add(cellContent);
                }

                // Ensure the row has exactly 3 cells
                if (cells.size() == 3) 
                {
                    try 
                    {
                        int x = Integer.parseInt(cells.get(0));  // First cell is the x-coordinate
                        char character = cells.get(1).charAt(0); // Second cell is the character
                        int y = Integer.parseInt(cells.get(2));  // Third cell is the y-coordinate
                        positions.add(new CharacterPosition(character, x, y));
                    } catch (Exception e) 
                    {
                        System.out.println("Invalid row data: " + cells);
                    }
                }
            }
        } else 
        {
            System.out.println("No table found in the document.");
        }

        return positions;
    }

    private static char[][] createGrid(List<CharacterPosition> positions) {
        int maxX = positions.stream().mapToInt(pos -> pos.intX).max().orElse(0);
        int maxY = positions.stream().mapToInt(pos -> pos.intY).max().orElse(0);

        char[][] grid = new char[maxY + 1][maxX + 1];
        for (int i = 0; i <= maxY; i++) 
        {
            for (int j = 0; j <= maxX; j++) 
            {
                grid[i][j] = ' ';
            }
        }

        for (CharacterPosition pos : positions) 
        {
            grid[pos.intY][pos.intX] = pos.chrCharacter;
        }

        return grid;
    }

    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
    }

    public static void main(String[] args) 
    {
        String docUrl = "https://docs.google.com/document/d/e/2PACX-1vQGUck9HIFCyezsrBSnmENk5ieJuYwpt7YHYEzeNJkIb9OSDdx-ov2nRNReKQyey-cwJOoEKUhLmN9z/pub";
        fetchAndPrintGrid(docUrl);
    }
}
