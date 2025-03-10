package task.flatexDEGIRO.solutions;

import java.util.*;

public class FlutTraderFromConsole {

    // Calculate the results for a single schuur
    public static List<int[]> calculateSchuurResults(int[] prices) {
        List<int[]> schuurResults = new ArrayList<>(); // [profit, flutCount]
        int profit = 0;

        for (int j = 0; j < prices.length; j++) {
            int currentProfit = 10 - prices[j];
            profit += currentProfit;
            schuurResults.add(new int[]{profit, j + 1}); // [total profit so far, flut count]
        }

        return schuurResults;
    }

    // Combine current schuur results into the overall results
    public static TreeMap<Integer, TreeSet<Integer>> combineResults(
            TreeMap<Integer, TreeSet<Integer>> combinedResults, List<int[]> schuurResults) {
        TreeMap<Integer, TreeSet<Integer>> newCombinedResults = new TreeMap<>();

        // Combine with previous results
        if (combinedResults.isEmpty()) {
            for (int[] schuurResult : schuurResults) {
                int profitKey = schuurResult[0];
                int flutCount = schuurResult[1];
                newCombinedResults.putIfAbsent(profitKey, new TreeSet<>());
                newCombinedResults.get(profitKey).add(flutCount);
            }
            return newCombinedResults;
        }

        for (Map.Entry<Integer, TreeSet<Integer>> entry : combinedResults.entrySet()) {
            int totalProfit = entry.getKey();
            TreeSet<Integer> totalFluts = entry.getValue();

            for (int[] schuurResult : schuurResults) {
                int combinedProfit = totalProfit + schuurResult[0];
                int combinedFluts = totalFluts.first() + schuurResult[1];

                // Update new combined results
                newCombinedResults.putIfAbsent(combinedProfit, new TreeSet<>());
                newCombinedResults.get(combinedProfit).add(combinedFluts);
            }
        }

        // Add results from just this schuur
        for (int[] schuurResult : schuurResults) {
            int profitKey = schuurResult[0];
            int flutCount = schuurResult[1];
            newCombinedResults.putIfAbsent(profitKey, new TreeSet<>());
            newCombinedResults.get(profitKey).add(flutCount);
        }

        return newCombinedResults;
    }

    // Find the maximum profit and its corresponding flut counts
    public static String getResultString(TreeMap<Integer, TreeSet<Integer>> combinedResults, int caseNumber) {
        if (combinedResults.isEmpty()) {
            return "No schuurs processed.";
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
        return sb.toString().trim() + "\n";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int caseNumber = 1;

        while (true) {
            int Z = sc.nextInt(); // Number of schuurs in this test case
            if (Z == 0) break; // End of input

            TreeMap<Integer, TreeSet<Integer>> combinedResults = new TreeMap<>();

            for (int i = 0; i < Z; i++) {
                int E = sc.nextInt(); // Number of fluts in this schuur
                int[] prices = new int[E];
                for (int j = 0; j < E; j++) {
                    prices[j] = sc.nextInt();
                }

                List<int[]> schuurResults = calculateSchuurResults(prices);
                combinedResults = combineResults(combinedResults, schuurResults);
            }

            System.out.println(getResultString(combinedResults, caseNumber));
            caseNumber++;
        }
    }
}
