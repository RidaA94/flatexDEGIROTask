package task.flatexDEGIRO.tests;

import org.junit.Test;
import task.flatexDEGIRO.solutions.FlutTraderFromConsole;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class FlutTraderFromConsoleTest {
    // Test the calculateSchuurResults() method
    @Test
    void testCalculateSchuurResults() {
        // Prepare sample input for a schuur
        int[] prices = {5, 7, 3};

        // Call the calculateSchuurResults() method
        List<int[]> results = FlutTraderFromConsole.calculateSchuurResults(prices);

        // Verify the output
        assertEquals(3, results.size());
        assertArrayEquals(new int[]{5, 1}, results.get(0));  // Profit 10 - 5, FlutCount: 1
        assertArrayEquals(new int[]{8, 2}, results.get(1));  // Total Profit 5 + (10 - 7)
        assertArrayEquals(new int[]{15, 3}, results.get(2)); // Total Profit 8 + (10 - 3)
    }

    // Test the combineResults() method
    @Test
    void testCombineResults() {
        // Mock previous combined results
        TreeMap<Integer, TreeSet<Integer>> combinedResults = new TreeMap<>();
        combinedResults.put(5, new TreeSet<>(Set.of(1)));
        combinedResults.put(10, new TreeSet<>(Set.of(2)));

        // Mock schuur results
        List<int[]> schuurResults = List.of(
                new int[]{3, 1},  // Profit 3, FlutCount 1
                new int[]{7, 2}   // Profit 7, FlutCount 2
        );

        // Call the combineResults() method
        TreeMap<Integer, TreeSet<Integer>> result = FlutTraderFromConsole.combineResults(combinedResults, schuurResults);

        // Verify profit combinations
        assertTrue(result.containsKey(8));    // 5 + 3
        assertTrue(result.containsKey(12));   // 5 + 7
        assertTrue(result.containsKey(13));   // 10 + 3
        assertTrue(result.containsKey(17));   // 10 + 7
        assertEquals(4, result.size());       // Total of 4 combinations
    }

    // Test the getResultString() method
    @Test
    void testGetResultString() {
        // Prepare mock combined results
        TreeMap<Integer, TreeSet<Integer>> combinedResults = new TreeMap<>();
        combinedResults.put(15, new TreeSet<>(Set.of(4, 5)));
        combinedResults.put(20, new TreeSet<>(Set.of(6)));

        // Call the getResultString() method
        String result = FlutTraderFromConsole.getResultString(combinedResults, 1);

        // Verify the output
        assertTrue(result.startsWith("Schuurs 1"));
        assertTrue(result.contains("Maximum profit is 20."));
        assertTrue(result.contains("Number of fluts to buy: 6"));
    }

    // Test the entire application flow with simulated console input
    @Test
    void testMainFlowSimulatingConsoleInput() {
        // Simulate user input for the program
        String input = "2\n3 5 7 3\n2 6 8\n0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in); // Redirect System.in to the simulated input

        // Capture the console output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        // Run the main method
        FlutTraderFromConsole.main(new String[]{});

        // Convert captured output to a string for verification
        String consoleOutput = output.toString();

        // Verify output contains the expected results
        assertTrue(consoleOutput.contains("Schuurs 1"));
        assertTrue(consoleOutput.contains("Maximum profit is 23"));
        assertTrue(consoleOutput.contains("Number of fluts to buy: 3 "));
    }
}
