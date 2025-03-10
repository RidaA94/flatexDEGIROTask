package task.flatexDEGIRO.tests;

import org.junit.Test;
import task.flatexDEGIRO.solutions.FlutTraderFromTxtFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class FlutTraderFromTxtFileTest {
    // Test the readFile() method
    @Test
    void testReadFile() throws IOException {
        // Simulate a temporary test file with sample input
        String tempFilePath = "D:/tempInput.txt";
        String fileContent = "2\n3 5 7 3\n2 6 8\n0"; // Input to simulate the file content

        // Write the content into the temp file
        Files.write(Paths.get(tempFilePath), fileContent.getBytes());

        // Call the readFile() method
        List<String> lines = FlutTraderFromTxtFile.readFile(tempFilePath);

        // Verify the output
        assertEquals(4, lines.size()); // 4 lines in the file
        assertEquals("2", lines.get(0));
        assertEquals("3 5 7 3", lines.get(1));
        assertEquals("0", lines.get(3));

        // Cleanup (delete the temp file)
        Files.delete(Paths.get(tempFilePath));
    }

    // Test the processTestCase() method
    @Test
    void testProcessTestCase() {
        // Prepare sample input for a single test case
        List<int[]> schuurs = List.of(
                new int[]{3, 5, 7, 3}, // First schuur
                new int[]{2, 6, 8}     // Second schuur
        );

        // Call the processTestCase() method
        String result = FlutTraderFromTxtFile.processTestCase(schuurs, 1);

        // Verify the expected output
        assertTrue(result.contains("Schuurs 1"));
        assertTrue(result.contains("Maximum profit is")); // Verifies "Maximum profit" logic is processed
        assertTrue(result.contains("Number of fluts to buy:")); // Checks the number of fluts processing
    }

    // Test the calculateSchuurResults() method
    @Test
    void testCalculateSchuurResults() {
        // Prepare sample input for a single schuur
        int[] schuur = {3, 5, 7, 3};

        // Call the method
        List<int[]> results = FlutTraderFromTxtFile.calculateSchuurResults(schuur);

        // Verify the correctness of profits and flut counts
        assertEquals(3, results.size()); // 3 valid flut counts
        assertArrayEquals(new int[]{5, 1}, results.get(0));  // Profit: 10-5, FlutCount: 1
        assertArrayEquals(new int[]{8, 2}, results.get(1));  // Profit: 5 + (10-7)
        assertArrayEquals(new int[]{15, 3}, results.get(2)); // Profit: 8 + (10-3)
    }

    // Test the combineResults() method
    @Test
    void testCombineResults() {
        // Prepare mock previous combined results
        TreeMap<Integer, TreeSet<Integer>> combinedResults = new TreeMap<>();
        combinedResults.put(5, new TreeSet<>(Set.of(1))); // Profit of 5 with 1 flut count
        combinedResults.put(10, new TreeSet<>(Set.of(2))); // Profit of 10 with 2 flut counts

        // Prepare schuur results
        List<int[]> schuurResults = List.of(
                new int[]{3, 1},  // Profit: 3 for 1 flut
                new int[]{7, 2}   // Profit: 7 for 2 fluts
        );

        // Call the combineResults() method
        TreeMap<Integer, TreeSet<Integer>> result = FlutTraderFromTxtFile.combineResults(combinedResults, schuurResults);

        // Verify combinations
        assertTrue(result.containsKey(8));    // 5 (prev) + 3
        assertTrue(result.containsKey(12));   // 5 (prev) + 7
        assertTrue(result.containsKey(13));   // 10 (prev) + 3
        assertTrue(result.containsKey(17));   // 10 (prev) + 7
        assertEquals(4, result.size());       // 4 unique profit combinations
    }

    // Test the getResultString() method
    @Test
    void testGetResultString() {
        // Prepare a mock result set
        TreeMap<Integer, TreeSet<Integer>> combinedResults = new TreeMap<>();
        combinedResults.put(15, new TreeSet<>(Set.of(4, 5))); // Profit 15 with fluts 4 and 5
        combinedResults.put(20, new TreeSet<>(Set.of(6)));    // Profit 20 with 6 fluts

        // Call the getResultString() method
        String result = FlutTraderFromTxtFile.getResultString(combinedResults, 1);

        // Verify the string output
        assertTrue(result.startsWith("Schuurs 1"));
        assertTrue(result.contains("Maximum profit is 20"));
        assertTrue(result.contains("Number of fluts to buy: 6"));
    }
}
