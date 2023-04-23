package code;

import java.util.ArrayList;
import java.util.List;

public class MyVector {
    private List<Double> data;

    public MyVector(List<Double> data) {
        this.data = data;
    }
    public MyVector() {
        data = new ArrayList<>();
    }

    public List<Double> getData() {
        return data;
    }

    public int length(){
        return data.size();
    }

    public Group closesCentroid(List<Group> groups){
        double minDistance = 100000;
        Group closestCentroid = null;
        for (Group group : groups){
            try {
                double tmp = MyVector.distance(this, group.getCentroid());
                if (tmp < minDistance){
                    minDistance = tmp;
                    closestCentroid = group;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return closestCentroid;
    }

    public static double distance(MyVector v1, MyVector v2) throws Exception {
        if (v1.length() != v2.length())
            throw new Exception("Not the same size");
        double distance = 0;

        for (int i = 0; i < v1.length(); i++){
            distance += Math.sqrt(Math.pow(v1.getData().get(i), 2) + Math.pow(v2.getData().get(i), 2));
        }
        distance *= 100;
        distance = Math.round(distance);
        distance /= 100;
//        System.out.println(distance);

        return distance;
    }

    public boolean equals(MyVector vector) {
        boolean equality = true;
        for (int i = 0; i < this.length(); i++) {
            if (this.data.get(i) != vector.getData().get(i)){
                equality = false;
                break;
            }
        }
        return equality;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
