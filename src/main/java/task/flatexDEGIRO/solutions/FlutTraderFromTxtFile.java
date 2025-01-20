package task.flatexDEGIRO.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FlutTraderFromTxtFile {
    public static void main(String[] args) {
        // I have made this path to the file for my own system
        // please change it to your own
        String filePath = "D:/input.txt"; // Absolute path to the input file

        try {
            List<String> inputLines = readFile(filePath); // Read from file
            if (inputLines.isEmpty()) {
                System.out.println("The file is empty or contains no valid data.");
                return;
            }
            processInput(inputLines); // Process the data
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Reads the contents of the file and returns it as a list of strings
    public static List<String> readFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Read line: " + line); // Debug statement
                lines.add(line.trim());
            }
            System.out.println("Total lines read: " + lines.size());
        }
        return lines;
    }

    // Processes the entire input
    public static void processInput(List<String> inputLines) {
        System.out.println("Input lines received for processing: " + inputLines.size()); // Debug

        int caseNumber = 1;
        int index = 0;

        while (index < inputLines.size()) {
            System.out.println("Processing line index: " + index); // Debug
            int Z;
            try {
                Z = Integer.parseInt(inputLines.get(index++)); // Number of schuurs
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse line as integer: " + inputLines.get(index - 1));
                break;
            }

            if (Z == 0) {
                System.out.println("End of test case input detected.");
                break; // End of input
            }

            List<int[]> schuurs = new ArrayList<>();
            for (int i = 0; i < Z; i++) {
                if (index >= inputLines.size()) break; // Prevent IndexOutOfBounds
                System.out.println("Parsing schuur data at index: " + index); // Debug
                String[] parts = inputLines.get(index++).split("\\s+");
                try {
                    int[] schuur = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
                    schuurs.add(schuur);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse schuur data: " + Arrays.toString(parts));
                }
            }

            // Process this test case
            String result = processTestCase(schuurs, caseNumber);
            System.out.println(result);
            caseNumber++;
        }
    }

    // Processes a single test case and returns the result as a string
    public static String processTestCase(List<int[]> schuurs, int caseNumber) {
        TreeMap<Integer, TreeSet<Integer>> combinedResults = new TreeMap<>(); // Profit -> Flut counts

        for (int[] schuur : schuurs) {
            List<int[]> schuurResults = calculateSchuurResults(schuur);

            combinedResults = combineResults(combinedResults, schuurResults);
        }

        return getResultString(combinedResults, caseNumber);
    }

    // Calculates the individual schuur results
    public static List<int[]> calculateSchuurResults(int[] schuur) {
        List<int[]> schuurResults = new ArrayList<>();
        int profit = 0;

        for (int j = 1; j < schuur.length; j++) { // Start at index 1 (skip the first number which is flut count)
            int currentProfit = 10 - schuur[j];
            profit += currentProfit;
            schuurResults.add(new int[]{profit, j});
        }

        return schuurResults;
    }

    // Combines the results of a schuur with the overall result set
    public static TreeMap<Integer, TreeSet<Integer>> combineResults(
            TreeMap<Integer, TreeSet<Integer>> combinedResults, List<int[]> schuurResults) {
        TreeMap<Integer, TreeSet<Integer>> newCombinedResults = new TreeMap<>();

        // Add combination of previous results with the current schuur
        for (Map.Entry<Integer, TreeSet<Integer>> entry : combinedResults.entrySet()) {
            int totalProfit = entry.getKey();
            TreeSet<Integer> totalFluts = entry.getValue();

            for (int[] schuurResult : schuurResults) {
                int combinedProfit = totalProfit + schuurResult[0];
                int combinedFluts = totalFluts.first() + schuurResult[1];

                newCombinedResults.putIfAbsent(combinedProfit, new TreeSet<>());
                newCombinedResults.get(combinedProfit).add(combinedFluts);
            }
        }

        // Add the current schuur's standalone results
        for (int[] schuurResult : schuurResults) {
            int profitKey = schuurResult[0];
            int flutCountValue = schuurResult[1];
            newCombinedResults.putIfAbsent(profitKey, new TreeSet<>());
            newCombinedResults.get(profitKey).add(flutCountValue);
        }

        return newCombinedResults;
    }

    // Generates the result string for a single test case
    public static String getResultString(TreeMap<Integer, TreeSet<Integer>> combinedResults, int caseNumber) {
        if (combinedResults.isEmpty()) {
            return "Schuurs " + caseNumber + "\nNo results found.";
        }

        int maxProfit = combinedResults.lastKey();
        TreeSet<Integer> flutCounts = combinedResults.get(maxProfit);

        StringBuilder sb = new StringBuilder();
        sb.append("Schuurs ").append(caseNumber).append("\n");
        sb.append("Maximum profit is ").append(maxProfit).append(".\n");

        sb.append("Number of fluts to buy: ");
        int count = 0;
        for (int flutCount : flutCounts) {
            sb.append(flutCount);
            count++;
            if (count == 10) break; // Limit to 10 smallest values
            sb.append(" ");
        }

        return sb.toString().trim();
    }
}
