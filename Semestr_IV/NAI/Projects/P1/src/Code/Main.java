package Code;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        List<Data> trainingDataList = new ArrayList<>();
        List<Data> testDataList = new ArrayList<>();

        try {
            Scanner reader = new Scanner(new File(args[1]));
            while (reader.hasNext()) {
                trainingDataList.add(readData(reader.nextLine()));
            }

            reader = new Scanner(new File(args[2]));
            while (reader.hasNext()) {
                testDataList.add(readData(reader.nextLine()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//Finding k the shortest points and prepare conclusion -------------------------------------------------------

        int types = testDataList.size();
        int correctTypes = 0;

        for (Data data : testDataList) {
            correctTypes = KNN(k, data, trainingDataList, correctTypes, false);
        }


        //Answer of given data ------------------------------------------------------------------
        System.out.println("Ilość danych: " + types + "\nIlość poprawnych danych: " + correctTypes);


        //Data given by user---------------------------------------------------------------------
        Scanner sysReader = new Scanner(System.in);

        System.out.println("Podaj wektor " + types + " wymiarowy lub napisz [exit] jeśli chcesz zakonczyc");
        String line = sysReader.nextLine();

        int newK;
        Data tmp;

        while (!line.equals("exit")) {
            System.out.println("Podaj k: ");
            newK = Integer.parseInt(sysReader.nextLine());

            tmp = readData(line);
            KNN(newK, tmp, trainingDataList, 0, true);

            System.out.println("Podaj wektor " + types + " wymiarowy lub napisz [exit] jeśli chcesz zakonczyc");
            line = sysReader.nextLine();
        }

    }

    private static DataWithDistance[] findKShortest(int k, List<Data> trainingDataList, Data data) {
        if (k > trainingDataList.size()) {
            throw new RuntimeException("k is too large");
        }

        DataWithDistance[] kShortest = new DataWithDistance[k]; //Array of k the closest points
        List<DataWithDistance> distanceList = new ArrayList<>(); //List of objects that contains distance and Data

        for (Data d : trainingDataList) {
            distanceList.add(new DataWithDistance(d, d.distanceBetween(data)));
        }

        distanceList.sort((Comparator.comparing(o -> o.distance)));

        /*
        for (DataWithDistance dwd : distanceList){ //Prints all distances sorted from closest
            System.out.println("Data: " + dwd.data.getType() + " | Dystans: " + dwd.distance);
        }
        */

        for (int i = 0; i < k; i++) {
            kShortest[i] = distanceList.get(i);
        }

        /*
        for (DataWithDistance dwd : kShortest){ //Prints three closest points
            System.out.println("Data: " + dwd.data.getType() + " | Dystans: " + dwd.distance);
        }
        System.out.println(" ");
        */

        return kShortest;
    }

    private static Data readData(String line) {
        String[] lineArr = line.split(",");
        String[] vector = new String[lineArr.length - 1]; //line without last element, which will be type name
        System.arraycopy(lineArr, 0, vector, 0, lineArr.length - 1);

        return new Data(vector, lineArr[lineArr.length - 1]);
    }

    private static int KNN(int k, Data data, List<Data> trainingDataList, int correctTypes, boolean isHandGiven) {
        DataWithDistance[] kShortest = findKShortest(k, trainingDataList, data);

        //Find correct type --------------
        List<DataWithAmount> tmpList = new ArrayList<>();
        boolean exists;

        for (DataWithDistance dwd : kShortest) {
            exists = false;
            for (DataWithAmount dataWithAmount : tmpList) {
                if (dwd.data.getType().equals(dataWithAmount.data.getType())) {
                    exists = true;
                    dataWithAmount.amount++;
                    break;
                }
            }

            if (!exists) {
                tmpList.add(new DataWithAmount(dwd.data, 1));
            }
        }

        //Searches for the type that occurred most often
        DataWithAmount maxA = null;
        int maxAmount = 0;

        for (DataWithAmount dataWithAmount : tmpList) {
            if (dataWithAmount.amount > maxAmount) {
                maxA = dataWithAmount;
                maxAmount = dataWithAmount.amount;
            }
        }

        assert maxA != null;

        if (isHandGiven)
            System.out.println("{ Expected type: " + data.getType() + "\nClassified type: " + maxA.data.getType() + " }\n"); //Print expected and classified type


        if (data.getType().equals(maxA.data.getType())) {
            correctTypes++;
        }
        return correctTypes;
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



