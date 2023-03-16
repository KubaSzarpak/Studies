package Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        List<Data> trainingDataList = new ArrayList<>();
        List<Data> testDataList = new ArrayList<>();

        try {
//Training data -------------------------------------------------------------------------
            Scanner reader = new Scanner(new File(args[1]));

            while (reader.hasNext()) {

                String[] line = reader.nextLine().split(",");
                String[] vector = new String[line.length - 2]; //line without last element, which will be type name
                for (int i = 0; i < line.length - 2; i++)
                    vector[i] = line[i];


                trainingDataList.add(new Data(vector, line[line.length - 1]));

            }


//Test data -------------------------------------------------------------------------
            reader = new Scanner(new File(args[2]));

            while (reader.hasNext()) {

                String[] line = reader.nextLine().split(",");
                String[] vector = new String[line.length - 2]; //line without last element, which will be type name
                for (int i = 0; i < line.length - 2; i++)
                    vector[i] = line[i];


                testDataList.add(new Data(vector, line[line.length - 1]));

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


//Finding k the shortest points and prepare conclusion -------------------------------------------------------
        DataWithDistance[] kShortest;
        int types = testDataList.size();
        int correctTypes = 0;
        int i = 1;

        for (Data d : testDataList) {
            System.out.println(i + ".");
            kShortest = findKShortest(k, trainingDataList, d);

            //Find correct type --------------
            List<DataWithAmount> tmp = new ArrayList<>();
            boolean exists;

            for (DataWithDistance dwd : kShortest) {
                exists = false;
                for (DataWithAmount a : tmp) {
                    if (dwd.data.getType().equals(a.data.getType())) {
                        exists = true;
                        a.amount++;
                        break;
                    }
                }

                if (!exists) {
                    tmp.add(new DataWithAmount(dwd.data, 1));
                }
            }

            //Searches for the type that occurred most often
            DataWithAmount maxA = null;
            int maxAmount = 0;

            for (DataWithAmount a : tmp) {
                if (a.amount > maxAmount) {
                    maxA = a;
                    maxAmount = a.amount;
                }
            }

            System.out.println("{ Oczekiwany typ: " + d.getType() + "\nSprawdzony typ: " + maxA.data.getType() + " }\n");

            if (d.getType().equals(maxA.data.getType())){
                correctTypes++;
            }
            i++;
        }
        System.out.println("Ilość danych: " + types + "\nIlość poprawnych danych: " + correctTypes);
    }

    private static DataWithDistance[] findKShortest(int k, List<Data> trainingDataList, Data data) {
        if (k > trainingDataList.size()) {
            throw new RuntimeException("k is too large");
        }

        DataWithDistance[] kShortest = new DataWithDistance[k]; //Array of k the closest points
        List<DataWithDistance> distanceList = new ArrayList<>(); //List of objects that contains distance and Data

        for (Data d : trainingDataList) { //Prints all
            distanceList.add(new DataWithDistance(d, d.distanceBetween(data)));
        }
        distanceList.sort((Comparator.comparing(o -> o.distance)));

        for (DataWithDistance dwd : distanceList){
            System.out.println("Data: " + dwd.data.getType() + " | Dystans: " + dwd.distance);
        }
        for (int i = 0; i < k; i++) {
            kShortest[i] = distanceList.get(i);
        }

//        for (DataWithDistance dwd : kShortest){
//            System.out.println("Data: " + dwd.data.getType() + " | Dystans: " + dwd.distance);
//        }

        return kShortest;
    }
}
class DataWithAmount {
    public Data data;
    public int amount;

    public DataWithAmount(Data data, int amount) {
        this.data = data;
        this.amount = amount;
    }
}

class DataWithDistance {
    public Data data;
    public double distance;

    public DataWithDistance(Data data, double distance) {
        this.data = data;
        this.distance = distance;
    }
}



