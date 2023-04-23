package code;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final String type;
    private MyVector centroid;
    private final List<MyVector> vectors;
    private double distance;

    public Group(String type) {
        this.type = type;
        vectors = new ArrayList<>();
        centroid = new MyVector();
        distance = 0;
    }

    public MyVector getCentroid() {
        return centroid;
    }

    public void setCentroid(MyVector centroid) {
        this.centroid = centroid;
    }

    public double getDistance() {
        double tmp = 0;
        for (MyVector vector : vectors){
            try {
                double d = MyVector.distance(centroid, vector);
                tmp += d;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        distance = tmp;
        return distance;
    }

    public void addVector(MyVector vector){
        vectors.add(vector);
    }

    public void calculateNewCentroid(){
        if (vectors.size() == 0)
            return;
        for (int i = 0; i < centroid.length(); i++){
            double avg = 0;
            for (MyVector vector : vectors){
                avg += vector.getData().get(i);
            }
            centroid.getData().set(i, avg/vectors.size());
        }
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();
        resultString.append("type: ").append(type);

        for (MyVector vector : vectors){
            resultString.append("\n\t").append(vector);
        }

        resultString.append("\n");

        return resultString.toString();
    }

    public void clear() {
        vectors.clear();
    }
}
