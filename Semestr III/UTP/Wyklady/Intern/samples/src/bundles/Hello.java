package bundles;
import java.util.*; 

public class Hello {

  static void sayHello() {
    Locale defLoc = Locale.getDefault();
    ResourceBundle msgs =
                   ResourceBundle.getBundle("HelloMessages", defLoc);
    String powitaj  = msgs.getString("hello");
    String pozegnaj = msgs.getString("bye");
    System.out.println(powitaj); 
    System.out.println(pozegnaj); 
 }  

 
  public static void main(String[] args) {
    sayHello(); // tutaj dzia�a domy�lna lokalizacja pl_PL
    // zmieniamy domy�ln� lokalizacj�
    Locale.setDefault(new Locale("en"));
    sayHello();
  }
    
}
