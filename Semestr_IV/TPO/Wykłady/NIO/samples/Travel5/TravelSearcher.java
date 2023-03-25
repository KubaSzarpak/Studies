import java.io.*;

public class TravelSearcher implements Serializable {

  private Travel[] travel;            // tablica podró¿y
  private int lastIndex = -1;         // indeks ostatnio zapisanej 
  private final int MAX_COUNT = 5;    // max rozmiar tablicy
  private boolean sorted = false;     // czy jest posortowana

  // Konstruktor: tworzy tablicê
  public TravelSearcher() { 
    travel = new Travel[MAX_COUNT];
  }
  
  // Metoda add dodaje do tablicy
  public void add(Travel t) throws NoSpaceForTravelException {
    try {
      lastIndex++;
      travel[lastIndex] = t;
    } catch (ArrayIndexOutOfBoundsException exc) {
        lastIndex--;
        throw new NoSpaceForTravelException("Brakuje miejsca dla dodania podró¿y");   
    }
    sorted = false;    
  }
  
  // Jaki jest ostatni zapisany indeks
  public int getLastIndex() { return lastIndex; }
  
  public String toString() {
    String out = "";
    for (int i=0; i <= lastIndex; i++) out += travel[i] + "\n";
    return out;
  }  
  
  // Wyszukiwanie podró¿y na podstawie podanego celu (destynacji) 
  public Travel search(String dest) {
    if (!sorted) sortByDest();
    int low = 0;
    int high = lastIndex;
    while (low <= high) {
      int mid = (low + high) / 2;
      int compRes = dest.compareToIgnoreCase(travel[mid].getDest());
      if (compRes < 0) high = mid - 1;
      else if (compRes > 0) low = mid + 1;
           else return travel[mid];
    }
    return null;
  }

 // Sortowanie - aby mo¿na by³o stosowaæ wyszukiwanie binarne
 private void sortByDest() {
   for (int to = lastIndex; to>0; to--) {
      int i = 0;   
      for (int k=1; k <= to; k++) 
 	    if (travel[i].getDest().compareTo(travel[k].getDest()) < 0) i = k; 
      Travel temp = travel[to];
      travel[to] = travel[i];
      travel[i] = temp;
    }
   sorted = true; 
  }     


 
}

