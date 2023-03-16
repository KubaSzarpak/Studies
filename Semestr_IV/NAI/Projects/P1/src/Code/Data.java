package Code;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Double> vector;
    private String type;

    public Data(String[] dates, String type) {
        this.type = type;
        this.vector = new ArrayList<>();

        for (String s : dates) {
            vector.add(Double.parseDouble(s));
        }
    }

    public List<Double> getVector() {
        return vector;
    }

    public void setVector(List<Double> vector) {
        this.vector = vector;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public double distanceBetween(Data data) {
        double sum = 0.0;

        for (int i = 0; i < vector.size(); i++) {
            sum += Math.pow(vector.get(i) - data.getVector().get(i), 2);
        }

        return Math.sqrt(sum);
    }
}