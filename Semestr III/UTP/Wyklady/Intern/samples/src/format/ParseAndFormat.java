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
   
    // Wypisanie i podsumowanie zapisanych w pliku wydatk�w
    // formatowanie wyj�cia za pomoc� metody align (zob. dalej)
    // w kt�rej u�ywamy FieldPosition
    
    // Format wyj�ciowy - walutowy, z dwoma miejscami dziesi�tnymi
    DecimalFormat outform = new DecimalFormat("#.00 �");

    System.out.println("Wydatki w : " + 
                        cform.getCurrency().getSymbol()); // symbol waluty
    double suma = 0;  
    int i = 1;
    final int DOTPOS = 30; // pozycja separatora miejsc dziesi�tnych

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

  // Metoda wyr�wnuj�ca liczby na separatorze miejsc dziesi�tnuych
  // Liczba v formatowana zgodnie z formatem f 
  //poprzedzona jest napisem msg
  // i tak� liczb� kropek, by separatory miejsc dziesi�tnych
  // by�y wyr�wnane na pozycji width 

  static StringBuffer align(String msg, Format f, Number v, int width) {

    //Interesuje nas pole - cz�� ca�kowita liczby 
    FieldPosition fp = new FieldPosition(NumberFormat.INTEGER_FIELD);

    // Bufor do kt�rego zapisywana jest sformatowana liczba
    // na pocz�tku bufora ju� zapisujemy msg (opis pozycji wydatk�w)

    StringBuffer out = new StringBuffer(msg);
    int msgLen = out.length();

    // formatowanie: v = liczba, out - bufor wynikowy, fp - opis pola
    f.format(v, out, fp);

    // Po sformatowaniu metoda fp.getEndIndex() zwraca 
    // pozycj� ko�ca pola = cz�ci ca�kowitej liczby
    // �atwo obliczy� liczb� dodatkowych (dotNum) "wype�niaczy", 
    // potrzebnych, by separator dziesi�tny znalaz� si� na pozycji width

    int dotNum = width - fp.getEndIndex();

    // Wstawiamy do bufora - jako wype�niacze - kropki
    while (dotNum-- > 0) {
      out.insert(msgLen, '.');
    }
    return out;
  }
     
}
