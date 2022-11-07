public abstract class command {
    String name;
    String[] args;

    command(String name, String... args){
        this.name = name;
        this.args = args;
    }

    public String toCSV(){
        String out = name;
        for (String arg:args) {
            out+=","+arg;
        }
        return out;
    }
}