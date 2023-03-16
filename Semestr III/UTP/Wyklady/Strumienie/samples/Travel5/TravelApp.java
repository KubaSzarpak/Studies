import java.io.*;
import java.util.*;
import javax.swing.*;  

public class TravelApp {

  private String travFileName;
  private TravelSearcher travels;
  private boolean dataSaved = false;

  public TravelApp(String[] tfn) {
    try {
      travFileName = tfn[0];
      ObjectInputStream in = new ObjectInputStream(
                                 new FileInputStream(travFileName)
                             );
      travels = (TravelSearcher) in.readObject();
      in.close();

    } catch(ArrayIndexOutOfBoundsException exc) {
        showMsg("Syntax: java TravelApp plik_kartoteki");
        System.exit(1);

    } catch(FileNotFoundException exc) {
        showMsg("Nowa kartoteka!!!");
        travels = new TravelSearcher();

    } catch(IOException exc) {
        exc.printStackTrace();
        System.exit(2);

    } catch(ClassNotFoundException exc) {
       showMsg("Brak klasy dostêpu do klasy TravelSearcher");
       System.exit(3);
    }

    String[] modes = { "Wprowadzanie", "Szukanie", "Zapis", "Pokaz", "Koniec" };
    while (true) {
      switch ( select("Wybierz tryb dzia³ania", modes)) {
        case 'W' : inputData(); break;
        case 'S' : searchData(); break;
        case 'Z' : saveData(); break;
        case 'P' : showData(); break;
        case 'K' : finish(); break;
        default : break;
      } 
    }
  }

  private char select(String msg, String[] modes) {
    int sel = JOptionPane.showOptionDialog(null, msg, 
                         "Travel App", 0, JOptionPane.QUESTION_MESSAGE, 
                          null, modes, modes[1]);
    if (sel == JOptionPane.CLOSED_OPTION) return 0;                       
    return modes[sel].charAt(0); 
  }

  public void inputData() {
    String data = "";
    String msg = "WprowadŸ dane"; 
    while((data = ask(msg, data)) != null) { 
      StringTokenizer st = new StringTokenizer(data);
      try {
        String dest = st.nextToken();
        int price = Integer.parseInt(st.nextToken());
        travels.add(new Travel(dest, price));
        dataSaved = false;
      } catch(NoSpaceForTravelException exc) {
          showMsg(exc.getMessage());
          return; 
      } catch(Exception exc) {
          msg = "Dane wadliwe - popraw";  
          continue;
      }

      msg = "WprowadŸ dane";
      data = "";
    }
  }    
  
  public void searchData() {
    if (travels.getLastIndex() >= 0) {
      String dest = "";
      String msg = "Podaj miejsce podró¿y"; 
      while((dest = ask(msg, "")) != null) { 
        Travel t = travels.search(dest);
        String info = (t == null ? "Nie ma takiej podró¿y!" : t.toString() );
        showMsg(info);
      }
    }
    else showMsg("Nie ma ¿adnych danych do przeszukiwania!");
  }  
         
  public void saveData() {
    ObjectOutputStream out = null;
    try { 
      out = new ObjectOutputStream(
                new FileOutputStream(travFileName)
                );
      out.writeObject(travels);
    } catch(IOException exc) {
        showMsg(exc.getMessage());
    } finally {
        try { out.close(); } catch (Exception exc) {}
    }
    dataSaved = true;
  }
  
  public void showData() {
    System.out.println("Dane\n" + travels);
  }  
  
  public void finish() {
    while (!dataSaved) {
      char ans = select("Czy zapisaæ dane?", new String[] { "Tak", "Nie" } );
      if (ans == 'T')  saveData();
      else if (ans == 'N') break;
    } 
    System.exit(0); 
  }  

  private void showMsg(String msg) {
    JOptionPane.showMessageDialog(null, msg);
  }
  
  private String ask(String msg, String initVal) {
    return JOptionPane.showInputDialog(null, msg, initVal);
  }          

  public static void main(String[] args) {
    new TravelApp(args);
  }   

}