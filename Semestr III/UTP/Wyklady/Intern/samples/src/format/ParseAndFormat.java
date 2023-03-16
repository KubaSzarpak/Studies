package format;
import java.io.*;
import java.text.*;
import java.util.*;
 
public class ParseAndFormat {
 
  public static void main(String[] args) {

    NumberFormat cform = NumberFormat.getCurrencyInstance();

    List numList = new ArrayList();

    try {
      BufferedReader br = new BufferedReader(
                            new FileReader("testdata.txt")
                          );
      
      String in;
      while ((in = br.readLine()) != null) {  

        int p = 0;                    
        int last  = in.length() - 1;  

        ParsePosition ppos = new ParsePosition(0);        
        
        while (p <= last) {
           Number num = cform.parse(in, ppos);
           if (num == null)              
             p = ppos.getErrorIndex()+1; 
           else {                        
             numList.add(num);           
             p =  ppos.getIndex();   
           }
           ppos.setIndex(p);         
        }                            
      }  
      br.close();
    } catch(Exception exc) {
        exc.printStackTrace();
        System.exit(1);
    }
   
    // Wypisanie i podsumowanie zapisanych w pliku wydatków
    // formatowanie wyjœcia za pomoc¹ metody align (zob. dalej)
    // w której u¿ywamy FieldPosition
    
    // Format wyjœciowy - walutowy, z dwoma miejscami dziesiêtnymi
    DecimalFormat outform = new DecimalFormat("#.00 ¤");

    System.out.println("Wydatki w : " + 
                        cform.getCurrency().getSymbol()); // symbol waluty
    double suma = 0;  
    int i = 1;
    final int DOTPOS = 30; // pozycja separatora miejsc dziesiêtnych

    for (Iterator iter = numList.iterator(); iter.hasNext(); i++ ) {
      Number val  = (Number) iter.next(); 
      suma += val.doubleValue();
      System.out.println(
        align("Pozycja " + i, outform, val, DOTPOS)
      );  
    }
    System.out.println(
        align("Wydano w sumie", outform, new Double(suma), DOTPOS)
    );
  } 

  // Metoda wyrównuj¹ca liczby na separatorze miejsc dziesiêtnuych
  // Liczba v formatowana zgodnie z formatem f 
  //poprzedzona jest napisem msg
  // i tak¹ liczb¹ kropek, by separatory miejsc dziesiêtnych
  // by³y wyrównane na pozycji width 

  static StringBuffer align(String msg, Format f, Number v, int width) {

    //Interesuje nas pole - czêœæ ca³kowita liczby 
    FieldPosition fp = new FieldPosition(NumberFormat.INTEGER_FIELD);

    // Bufor do którego zapisywana jest sformatowana liczba
    // na pocz¹tku bufora ju¿ zapisujemy msg (opis pozycji wydatków)

    StringBuffer out = new StringBuffer(msg);
    int msgLen = out.length();

    // formatowanie: v = liczba, out - bufor wynikowy, fp - opis pola
    f.format(v, out, fp);

    // Po sformatowaniu metoda fp.getEndIndex() zwraca 
    // pozycjê koñca pola = czêœci ca³kowitej liczby
    // ³atwo obliczyæ liczbê dodatkowych (dotNum) "wype³niaczy", 
    // potrzebnych, by separator dziesiêtny znalaz³ siê na pozycji width

    int dotNum = width - fp.getEndIndex();

    // Wstawiamy do bufora - jako wype³niacze - kropki
    while (dotNum-- > 0) {
      out.insert(msgLen, '.');
    }
    return out;
  }
     
}
