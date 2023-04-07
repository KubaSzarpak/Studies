import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        String trainingPath = args[0];
        String testPath = args[1];
        List<PrcptrnVector> trainingData = new ArrayList<>();
        List<PrcptrnVector> testData = new ArrayList<>();

        readFile(trainingPath, trainingData);
        readFile(testPath, testData);

        Perceptron perceptron = new Perceptron(trainingData);


        while (perceptron.status != Perceptron.Status.LEARNED) {
            Thread.sleep(100);
        }

        int ilosc = testData.size();
        int celnosc = 0;

        for (PrcptrnVector vector : testData) {
            String result = perceptron.func(vector);
//            System.out.println("Oczekiwany: " + vector.getType() + " | Obliczony: " + result);

            if (vector.getType().equals(result))
                celnosc++;
        }

        System.out.println("Ilosc elementów testowych: " + ilosc + "\nIlość elementów celnych: " + celnosc);

        Scanner console = new Scanner(System.in);

        System.out.println("\nJeśli chcesz zakończyć wpisz \"exit\"");
        System.out.println("Podaj dane (vector[a1,a2,a3...],type): ");
        String line = console.nextLine();

        while (!line.equals("exit")) {
            String[] arr = line.split(",");
            List<Double> data = new ArrayList<>();

            for (int i = 0; i < arr.length - 1; i++) {
                data.add(Double.parseDouble(arr[i]));
            }

            System.out.println(perceptron.func(new PrcptrnVector(arr[arr.length - 1], data)));

            System.out.println("Podaj dane (vector[a1,a2,a3...],type: ");
            line = console.nextLine();
        }
        console.close();
    }

    public static void readFile(String path, List<PrcptrnVector> list) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(path));

        while (reader.hasNext()) {
            String[] line = reader.nextLine().split(",");
            List<Double> data = new ArrayList<>();

            for (int i = 0; i < line.length - 1; i++) {
                data.add(Double.parseDouble(line[i]));
            }

            list.add(new PrcptrnVector(line[line.length - 1], data));
        }
        reader.close();
    }
}
