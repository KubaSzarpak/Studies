import java.util.List;


public class Perceptron {
    private final double ALFA;
    private final List<PrcptrnVector> trainingData;
    private final PrcptrnVector weight;
    private final Values values;
    public Status status;
    private double teta;

    public Perceptron(List<PrcptrnVector> trainingData) {
        ALFA = 0.01;
        this.trainingData = trainingData;
        this.values = new Values();
        this.weight = new PrcptrnVector();
        this.status = Status.NULL;
        this.teta = 1.0;

        new Learn().start();
    }

    public Perceptron(double alfa, List<PrcptrnVector> trainingData) {
        ALFA = alfa;
        this.trainingData = trainingData;
        this.values = new Values();
        this.weight = new PrcptrnVector();
        this.status = Status.NULL;
        this.teta = 1.0;

        new Learn().start();
    }

    private double Net(PrcptrnVector vector) {
        double net = 0;

        for (int i = 0; i < Math.max(vector.lenght(), weight.lenght()); i++) {
            net += vector.getElem(i) * weight.getElem(i);
        }
        net -= teta;

        return net;
    }

    private int activationFunc(PrcptrnVector vector) {
        if (Net(vector) >= 0) {
            return 1;
        }
        return 0;
    }

    public String func(PrcptrnVector vector) {
        if (status != Status.LEARNED){
            return "ERROR";
        }

        int x = activationFunc(vector);

        if (x == values.getV1()) {
            return values.getT1();
        } else if (x == values.getV2()) {
            return values.getT2();
        }
        return "ERROR";
    }

    private class Learn extends Thread {
        @Override
        public void run() {

            {
                int i = 0;
                while (values.getT1() == null || values.getT2() == null) {
                    values.setType(trainingData.get(i).getType());
                    i++;
                }
            } //normalization

            status = Status.LEARNING;
            weightDraw(trainingData.get(0).lenght());
            double Emax = 0.0;
            double E = 0.0;
            int i = 0;

            while (E >= Emax && i < 1000) {
                double sum = 0.0;
                for (PrcptrnVector vector : trainingData) {
                    int y = activationFunc(vector);
                    int value;

                    //assigning numeric value of vector's type
                    if (vector.getType().equals(values.t1)){
                        value = values.getV1();
                    } else if (vector.getType().equals(values.getT2())){
                        value = values.getV2();
                    } else {
                        break;
                    }

                    newWeight(value, y, vector);
                    newTeta(value, y);

                    sum += Math.pow(value - y, 2);
                }

                E = (1.0 / trainingData.size()) * sum;
                i++;
            }

            status = Status.LEARNED;
        }

        private void weightDraw(int lenght) {
            for (int i = 0; i < lenght; i++) {
                weight.add(Math.random());
            }
        }

        private void newWeight(int d, int y, PrcptrnVector vector) {
            for (int i = 0; i < Math.max(vector.lenght(), weight.lenght()); i++) {
                weight.setElem(i, weight.getElem(i) + ALFA * (d - y) * vector.getElem(i));
            }
        }

        private void newTeta(int d, int y) {
            teta -= ALFA * (d - y);
        }
    }

    private static class Values {
        private final int v1;
        private final int v2;
        private String t1;
        private String t2;

        public Values() {
            v1 = 1;
            v2 = 0;
        }

        public void setType(String type) {
            if (t1 == null){
                t1 = type;
            } else if (!t1.equals(type) && t2 == null){
                t2 = type;
            }
        }

        public int getV1() {
            return v1;
        }

        public int getV2() {
            return v2;
        }

        public String getT1() {
            return t1;
        }

        public String getT2() {
            return t2;
        }
    }

    protected enum Status {
        NULL, LEARNING, LEARNED;
    }
}