package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        List<MyVector> dataList = new ArrayList<>();

        readFile(args[0], dataList);

        kMeans(Integer.parseInt(args[1]), dataList);
    }

    public static void kMeans(int k, List<MyVector> dataList){
        double distanceInIteration1 = -2;
        double distanceInIteration2 = -1;
        List<Group> groups = new ArrayList<>();

//        for (int i = 0; i < k; i++) {
//            groups.add(new Group(String.valueOf(i), dataList.get(0).length()));
//        }

        Group g1 = new Group("Iris-setosa", dataList.get(0).length());
        Group g2 = new Group("Iris-versicolor", dataList.get(0).length());
        Group g3 = new Group("Iris-virginica", dataList.get(0).length());

        g1.setCentroid(new MyVector(new ArrayList<>(List.of(new Double[]{5.1, 3.5, 1.4, 0.2}))));
        g2.setCentroid(new MyVector(new ArrayList<>(List.of(new Double[]{7.0,3.2,4.7,1.4}))));
        g3.setCentroid(new MyVector(new ArrayList<>(List.of(new Double[]{5.9,3.0,5.1,1.8}))));

        groups.add(g1);
        groups.add(g2);
        groups.add(g3);

        int j = 1;
        while (distanceInIteration1 != distanceInIteration2) {
            for (Group group : groups){
                group.clear();
            }

            for (MyVector vector : dataList) {
                vector.closesCentroid(groups).addVector(vector);
            }

            double tmpDistance = 0;
            for (Group group : groups) {
                group.calculateNewCentroid();
                tmpDistance += group.getDistance();
            }
            distanceInIteration1 = distanceInIteration2;
            distanceInIteration2 = tmpDistance;

            System.out.println("Iteracja " + j + ": " + tmpDistance);
            j++;


        }
        for (Group group : groups){
            System.out.println(group);
        }



    }

    public static void readFile(String path, List<MyVector> list) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(path));

        while (reader.hasNext()) {
            String[] line = reader.nextLine().split(",");
            List<Double> data = new ArrayList<>();

            for (int i = 0; i < line.length - 1; i++) {
                data.add(Double.parseDouble(line[i]));
            }

            list.add(new MyVector(data));
        }
        reader.close();
    }
}