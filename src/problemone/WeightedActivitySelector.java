package problemone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WeightedActivitySelector {
    public static void main(String[] args) throws IOException {
        // generating random test cases to test the algorithm. just give another argument after the path = true.
        if (args[1] != null && args[1] == "true") {
            generateRandomTestCases(args[1]);
        }

        // getting the input file path to create an output path in the same directory.
        String inputPath = args[0];
        String[] inputPathArray = inputPath.split("\\\\");
        StringBuilder sb = new StringBuilder();
        int inputLength = inputPathArray.length;
        for (int i = 0; i < inputLength - 1; i++)
            sb.append(inputPathArray[i]).append("\\");
        sb.append(inputPathArray[inputLength - 1].split("\\.")[0]);
        sb.append("_19015709.txt");
        String outputPath = sb.toString();

        // initializing activities and solutions arrays
        ArrayList<ArrayList<Integer>> activities;
        ArrayList<Integer> solutions = new ArrayList<>();

        Scanner scanner = new Scanner(new File(inputPath));
        while (scanner.hasNext()) {
            activities = new ArrayList<>();
            int n = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < n; i++) {
                String nextLine = scanner.nextLine();
                String[] activity = nextLine.split(" ");
                ArrayList<Integer> activityInteger = new ArrayList<>();
                for (String string : activity)
                    activityInteger.add(Integer.parseInt(string));
                activities.add(activityInteger);
            }
            solutions.add(solve(activities, n));
            String solution = solutions.toString().replace(", ", "\n");
            solution = solution.replace("[", "");
            solution = solution.replace("]", "");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            writer.write(solution);
            writer.close();
        }
        scanner.close();
    }

    /**
     * Bottom-up approach - Dynamic Programming
     *
     * @param activities ArrayList of Integers
     * @param n          size of the array (int)
     * @return int: solution to weighted activity selection
     */
    private static int solve(List<ArrayList<Integer>> activities, int n) {
        int[] solutions = new int[n];
        int w;

        // Sort activities with respect to their finishing time
        activities.sort(Comparator.comparingInt(o -> o.get(1)));
        for (int i = 1; i < n; i++) {
            w = activities.get(i).get(2);   // 2 to retrieve the weight.
            int solved = binarySearch(activities, i);
            if (solved > -1) w += solutions[solved];
            solutions[i] = Math.max(w, solutions[i - 1]);
        }
        return solutions[n - 1];
    }

    private static int binarySearch(List<ArrayList<Integer>> activities, int i) {
        int lowerBound = 0;
        int upperBound = i - 1;
        while (lowerBound < upperBound) {
            int midPoint = (lowerBound + upperBound) / 2;
            if (activities.get(midPoint).get(1) <= activities.get(i).get(1)) {  // 1 to retrieve the finish time
                if (activities.get(midPoint + 1).get(1) <= activities.get(i).get(1)) lowerBound = midPoint + 1;
                else return midPoint;

            } else upperBound = midPoint;
        }
        return -1;  // -1 indicates no occurrence.
    }

    private static void generateRandomTestCases(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        Random random = new Random();
        int testCasesLength = random.nextInt(1, 100); // upper bound for the number of test cases is 100
        System.out.println("Generated " + testCasesLength + " random test cases.");
        StringBuilder testCases = new StringBuilder();
        for (int i = 0; i < testCasesLength; i++) {
            int n = random.nextInt(1, 100); // upper bound for the number of activities is 100
            testCases.append(n).append("\n");
            for (int j = 0; j < n; j++) {
                int startTime = random.nextInt(100); // upper bound for start time
                int finishTime = random.nextInt(startTime + 1, 10 * (startTime + 1)); // upper bound for finish time
                int weight = random.nextInt(1, random.nextInt(2, 100)); // upper bound for weight
                testCases.append(startTime).append(" ").append(finishTime).append(" ").append(weight).append("\n");
            }
        }
        String inputsString = testCases.toString();
        writer.write(inputsString);
        writer.close();
    }
}
