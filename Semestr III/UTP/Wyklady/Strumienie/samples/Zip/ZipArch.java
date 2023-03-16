import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;


class ZipArch {

  // rozmiar bufora dla plików
  static final int BUF_SIZE = 4096;

  // Znak separatora katalogu ("\" lub "/" - zale¿nie od platformy)
  static final private String fileSep = System.getProperty("file.separator");

  private String zipFileName;  // archiwum ZIP

  // konstruktor; argument - nazwa archiwum (tworzonego lub rozpakowywanego) 
  public ZipArch(String fileName) { 
    zipFileName = fileName;
  }

  private boolean verbose = true; // czy pokazywaæ postêpy?
  public void setVerbose(boolean b) { verbose = b; }
  public boolean isVerbose() { return verbose; }


  public void unzip() throws IOException, ZipException {

    // Zbiór createdDirs przechowuje utworzone przy rozpakowywaniu katalogi,
    // tak by szybko stwierdziæ czy ju¿ katalog utworzyliœmy
    // i nie próbowaæ go tworzyæ jeszcze raz (zob. zbiory w rozdz.o kolekcjach)
    Set createdDirs = new HashSet();

    // Utworzenie wejœciowego strumienia zwi¹zanego z plikiem ZIP
    FileInputStream fis = new FileInputStream(zipFileName);

    // Na³o¿enie na ten strumieñ przetwarzania w postaci
    // - najpierw buforowania, nastêpnie - dekompresji
    ZipInputStream  zis = new  ZipInputStream(
                            new BufferedInputStream(fis));

    ZipEntry entry; // element archiwum (spakowany plik lub katalog)

    // Dopóki s¹ w w archiwum elementy
    // Pobieramy je i przetwarzamy
    while((entry = zis.getNextEntry()) != null) {

      String ename = entry.getName(); // nazwa elementu archiwum
                                      // np. \windows\bum.txt
      // Gdy w¹czona opcja pokazywania postêpów (zob. pole verbose)
      if (verbose) System.out.println("Inflating " + ename);

      // Tworzenie ew. pustych katalogów zapisanych w ZIPie
      // Tylko dla pustego katalogu entry.isDirectory() bêdzie true
      // Nazwa ka¿dego innego elementu archiwum bêdzie mia³a postaæ
      // [d:][\katalog1\katalog2\...\]plik
      if (entry.isDirectory()) {
        new File(ename).mkdirs(); // tworzymy pusty (z nadkatalogami)
        continue;                 // z pustego nie ma co rozpakowaæ!
      }

      // Je¿eli archiwum zawiera (niepuste) katalogi,
      // to musimy je utworzyæ przed rozpakowaniem plików

      int p = ename.lastIndexOf(fileSep); // ostatni znak "/" lub "\"
      if (p != -1) {     // czy "entry" jest plikiem w katalogu?

        // Nazwa katalogu
        String dirName = ename.substring(0,p+1);
  
        // Jeœli go jeszcze nie utworzyliœmy ...
        if (!createdDirs.contains(dirName)) {
  
          createdDirs.add(dirName);       // rejestrujemy, ¿e utworzony!
          File dir = new File(dirName);
          boolean created = dir.mkdirs(); // tworzymy (wraz z nadkatalogami)
  
          if (!created) {                 // nie uda³o siê utworzyæ
            int rc = JOptionPane.showConfirmDialog(null,
                       "Katalog " + dirName + " ju¿ istnieje." +
                       "Kontynuowaæ?");
            if (rc != 0)  { // niezgoda na kontynuacjê programu
              throw
              new IOException("Unable to create directory "+ dirName);
             }
           }
         }
     }  // Koniec tworzenia ew. niezbêdnego katalogu

     // Teraz dla ka¿dego elementu (pliku) archiwum tworzymy
     // buforowany strumieñ wyjœciowy (do zapisu)
     // o nazwie takiej samej jak w archiwum (ename)

     BufferedOutputStream out = new BufferedOutputStream(
                                  new FileOutputStream(ename), // plik out
                                  BUF_SIZE                // rozmiar bufora
                                 );

      byte data[] = new byte[BUF_SIZE]; // tablica. do wczytywania danych
      int count;                        // liczba przeczytanych bajtów
      while ((count = zis.read(data, 0, BUF_SIZE)) != -1) {
        out.write(data, 0, count);
      }
      out.close();
    }
    zis.close();
  }  // Koniec metody unzip

  // Metoda kompresuj¹ca: argument - plik lub katalog do spakowania
  public void zip(String srcFileName) throws IOException, ZipException {

    // Strumieñ wyjœciowy archiuwm ZIP
    // na zapisywany plik ZIP na³o¿one jest buforowanie, 
    // a nastêpnie kompresja (strumieñ przetwarzaj¹cy ZipOutputStream)
    ZipOutputStream zos = new ZipOutputStream(
                             new BufferedOutputStream(
                                new FileOutputStream(zipFileName),
                                BUF_SIZE
                                )
                             );
     // Ze wzglêdu na ew. rekurencyjne wchodzenie w podkatalogi ¿ród³a
     // wywo³ujemy wewnêtrzn¹ metodê doZip
     // z argumentami plik (lub katalog) do archiwizacji, 
     //               zip-strumieñ wyjœciowy
     doZip(new File(srcFileName), zos);
     zos.close(); 
  }

  // Metoda rekurencyjnie archiwizuje pliki podane jako fileToZip
  // do archiwum, do którego przy³¹czony jest strumieñ zos
  private void doZip(File fileToZip, ZipOutputStream zos)
                     throws IOException, ZipException
  {
    if (fileToZip.isDirectory()) {  // Je¿eli archiwizacja ma dotyczyæ katalogu

      File[] listToZip = fileToZip.listFiles(); // lista obiektów plikowych

      for (int i=0; i<listToZip.length; i++) {
        doZip(listToZip[i], zos);  // dla ka¿dego obiektu plikowego w tym
      }                            // katalogu wolamy rekurencyjnie doZip

    }
    else {  // jezeli to plik - zipujemy!

      String fname = fileToZip.toString();   // nazwa pliku

      // czy przypadkiem nie ma postaci d:\plik_lub_katalog 
      // w tym przypadku w nazwie elementu ("wejœcia") archiwum
      // pominiemy literowe oznaczenie dysku, dwukropek i separator
      int colon = fname.indexOf(":") + 1;
      if (colon!= 0) fname = fname.substring(colon); // bez "d:"
      if (fname.charAt(0) == fileSep.charAt(0)) { // zdj¹æ ew. separator
        fname = fname.substring(1);
      }  
      
      // Tworzymy nowe "wejœcie" - opisuj¹ce nowy element archiwum
      ZipEntry entry = new ZipEntry(fname);
      // i zapisujemy to "wejœcie"
      zos.putNextEntry(entry);

      // informacja o postêpach kompresji
      if (verbose) System.out.println("Deflating " + entry);

      // Czytamy plik i zapisujemy jego zawartoœæ
      // w skompresowanej postaci "pod" otwartym "wejœciem" entry 
      BufferedInputStream in = new BufferedInputStream(
                                    new FileInputStream(fileToZip),
                                    BUF_SIZE
                                   );
      byte data[] = new byte[BUF_SIZE]; // tablica. do wczytywania danych
      int count;                        // liczba przeczytanych bajtów

      while ((count = in.read(data)) != -1) { // czytanie i zapis z kompresj¹
            zos.write(data, 0, count);
      }
      in.close(); // zamkniêcie pliku wejœciowego

      zos.closeEntry(); // zamkniêcie elementu (koniec zapisu elementu)
    }
  }

}

class ZipArchiveTest {

  static void syntaxErrMsg() {
    System.out.println(
      "Syntax: java ZipArchiv zip archiwum plik_lub_katalog_do_pakowania\n" +
      "        java ZipArchiv unzip archiwum"
      );
    System.exit(1);
  }

  public static void main(String[] args) {
    try {
      ZipArch zip = new ZipArch(args[1]);
      if (args[0].equals("unzip")) {
         zip.unzip();
      }
      else if(args[0].equals("zip")) {
         zip.zip(args[2]);
      }
      else throw new IllegalArgumentException();
    } catch (IndexOutOfBoundsException exc) {
        syntaxErrMsg();
    } catch (IllegalArgumentException exc) {
        syntaxErrMsg();
    } catch (IOException exc) {
        exc.printStackTrace();
        System.exit(2);
    }
    System.exit(0);
  }
}