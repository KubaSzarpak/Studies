import java.util.*;

public class Visitor1 {

  public static void main(String[] args) {
    List<Pacjent> lista = new ArrayList<Pacjent>();
    lista.add(new ChoryNaGłowę("Jan Kowalski"));
    lista.add(new ChoryNaGłowę("Stefan Kowalewski"));
    lista.add(new ChoryNaNogę("Janusz Malinowski"));
    lista.add(new ChoryNaŻołądek("Adam Mickiewicz"));

    // Leczenie
    Visitor leczenie = new Leczenie2();
    for(Pacjent p : lista) p.accept(leczenie);

    // Wypisy
    Visitor wypis = new Wypis();
    for(Pacjent p : lista) p.accept(wypis);

    // Aha! Konkretna forma dzialania p.accept(v) polimorficznie
    // zależy od klas dwóch obiektow: pacjenta i visitora
    // To jest "double dispatching".


    wykonajOperację(leczenie, lista);
    wykonajOperację(wypis, lista);


  }


  static void wykonajOperację(Visitor v, List<Pacjent> lista) {
    System.out.println("Wykonuję operację - " +  v);
    for(Pacjent p : lista) p.accept(v);
  }


}