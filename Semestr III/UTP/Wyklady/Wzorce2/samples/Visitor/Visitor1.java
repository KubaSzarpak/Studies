import java.util.*;

public class Visitor1 {

  public static void main(String[] args) {
    List<Pacjent> lista = new ArrayList<Pacjent>();
    lista.add(new ChoryNaG³owê("Jan Kowalski"));
    lista.add(new ChoryNaG³owê("Stefan Kowalewski"));
    lista.add(new ChoryNaNogê("Janusz Malinowski"));
    lista.add(new ChoryNa¯o³¹dek("Adam Mickiewicz"));

    // Leczenie
    Visitor leczenie = new Leczenie2();
    for(Pacjent p : lista) p.accept(leczenie);

    // Wypisy
    Visitor wypis = new Wypis();
    for(Pacjent p : lista) p.accept(wypis);

    // Aha! Konkretna forma dzialania p.accept(v) polimorficznie
    // zale¿y od klas dwóch obiektow: pacjenta i visitora
    // To jest "double dispatching".


    wykonajOperacjê(leczenie, lista);
    wykonajOperacjê(wypis, lista);


  }


  static void wykonajOperacjê(Visitor v, List<Pacjent> lista) {
    System.out.println("Wykonujê operacjê - " +  v);
    for(Pacjent p : lista) p.accept(v);
  }


}