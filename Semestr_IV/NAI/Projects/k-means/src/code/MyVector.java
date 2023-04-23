package code;

import java.util.ArrayList;
import java.util.List;

public class MyVector {
    private final List<Double> data;

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
            distance += Math.sqrt(Math.pow(v1.getData().get(i) - v2.getData().get(i), 2));
        }

        return distance;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
