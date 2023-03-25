import java.util.ArrayList;
import java.util.List;

public class PrcptrnVector {
    private final String type;
    private final List<Double> data;

    public PrcptrnVector(String type, List<Double> data) {
        this.type = type;
        this.data = data;
    }

    public PrcptrnVector() {
        this.data = new ArrayList<>();
        type = "";
    }

    public String getType() {
        return type;
    }

    public double getElem(int index) {
        return data.get(index);
    }

    public void setElem (int index, double value) {
        data.set(index, value);
    }


    public List<Double> getData() {
        return data;
    }

    public int lenght() {
        return data.size();
    }

    public void add(double value){
        data.add(value);
    }
}
