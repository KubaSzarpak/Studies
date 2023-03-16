import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;


class ZipArch {

  // rozmiar bufora dla plik�w
  static final int BUF_SIZE = 4096;

  // Znak separatora katalogu ("\" lub "/" - zale�nie od platformy)
  static final private String fileSep = System.getProperty("file.separator");

  private String zipFileName;  // archiwum ZIP

  // konstruktor; argument - nazwa archiwum (tworzonego lub rozpakowywanego) 
  public ZipArch(String fileName) { 
    zipFileName = fileName;
  }

  private boolean verbose = true; // czy pokazywa� post�py?
  public void setVerbose(boolean b) { verbose = b; }
  public boolean isVerbose() { return verbose; }


  public void unzip() throws IOException, ZipException {

    // Zbi�r createdDirs przechowuje utworzone przy rozpakowywaniu katalogi,
    // tak by szybko stwierdzi� czy ju� katalog utworzyli�my
    // i nie pr�bowa� go tworzy� jeszcze raz (zob. zbiory w rozdz.o kolekcjach)
    Set createdDirs = new HashSet();

    // Utworzenie wej�ciowego strumienia zwi�zanego z plikiem ZIP
    FileInputStream fis = new FileInputStream(zipFileName);

    // Na�o�enie na ten strumie� przetwarzania w postaci
    // - najpierw buforowania, nast�pnie - dekompresji
    ZipInputStream  zis = new  ZipInputStream(
                            new BufferedInputStream(fis));

    ZipEntry entry; // element archiwum (spakowany plik lub katalog)

    // Dop�ki s� w w archiwum elementy
    // Pobieramy je i przetwarzamy
    while((entry = zis.getNextEntry()) != null) {

      String ename = entry.getName(); // nazwa elementu archiwum
                                      // np. \windows\bum.txt
      // Gdy w�czona opcja pokazywania post�p�w (zob. pole verbose)
      if (verbose) System.out.println("Inflating " + ename);

      // Tworzenie ew. pustych katalog�w zapisanych w ZIPie
      // Tylko dla pustego katalogu entry.isDirectory() b�dzie true
      // Nazwa ka�dego innego elementu archiwum b�dzie mia�a posta�
      // [d:][\katalog1\katalog2\...\]plik
      if (entry.isDirectory()) {
        new File(ename).mkdirs(); // tworzymy pusty (z nadkatalogami)
        continue;                 // z pustego nie ma co rozpakowa�!
      }

      // Je�eli archiwum zawiera (niepuste) katalogi,
      // to musimy je utworzy� przed rozpakowaniem plik�w

      int p = ename.lastIndexOf(fileSep); // ostatni znak "/" lub "\"
      if (p != -1) {     // czy "entry" jest plikiem w katalogu?

        // Nazwa katalogu
        String dirName = ename.substring(0,p+1);
  
        // Je�li go jeszcze nie utworzyli�my ...
        if (!createdDirs.contains(dirName)) {
  
          createdDirs.add(dirName);       // rejestrujemy, �e utworzony!
          File dir = new File(dirName);
          boolean created = dir.mkdirs(); // tworzymy (wraz z nadkatalogami)
  
          if (!created) {                 // nie uda�o si� utworzy�
            int rc = JOptionPane.showConfirmDialog(null,
                       "Katalog " + dirName + " ju� istnieje." +
                       "Kontynuowa�?");
            if (rc != 0)  { // niezgoda na kontynuacj� programu
              throw
              new IOException("Unable to create directory "+ dirName);
             }
           }
         }
     }  // Koniec tworzenia ew. niezb�dnego katalogu

     // Teraz dla ka�dego elementu (pliku) archiwum tworzymy
     // buforowany strumie� wyj�ciowy (do zapisu)
     // o nazwie takiej samej jak w archiwum (ename)

     BufferedOutputStream out = new BufferedOutputStream(
                                  new FileOutputStream(ename), // plik out
                                  BUF_SIZE                // rozmiar bufora
                                 );

      byte data[] = new byte[BUF_SIZE]; // tablica. do wczytywania danych
      int count;                        // liczba przeczytanych bajt�w
      while ((count = zis.read(data, 0, BUF_SIZE)) != -1) {
        out.write(data, 0, count);
      }
      out.close();
    }
    zis.close();
  }  // Koniec metody unzip

  // Metoda kompresuj�ca: argument - plik lub katalog do spakowania
  public void zip(String srcFileName) throws IOException, ZipException {

    // Strumie� wyj�ciowy archiuwm ZIP
    // na zapisywany plik ZIP na�o�one jest buforowanie, 
    // a nast�pnie kompresja (strumie� przetwarzaj�cy ZipOutputStream)
    ZipOutputStream zos = new ZipOutputStream(
                             new BufferedOutputStream(
                                new FileOutputStream(zipFileName),
                                BUF_SIZE
                                )
                             );
     // Ze wzgl�du na ew. rekurencyjne wchodzenie w podkatalogi �r�d�a
     // wywo�ujemy wewn�trzn� metod� doZip
     // z argumentami plik (lub katalog) do archiwizacji, 
     //               zip-strumie� wyj�ciowy
     doZip(new File(srcFileName), zos);
     zos.close(); 
  }

  // Metoda rekurencyjnie archiwizuje pliki podane jako fileToZip
  // do archiwum, do kt�rego przy��czony jest strumie� zos
  private void doZip(File fileToZip, ZipOutputStream zos)
                     throws IOException, ZipException
  {
    if (fileToZip.isDirectory()) {  // Je�eli archiwizacja ma dotyczy� katalogu

      File[] listToZip = fileToZip.listFiles(); // lista obiekt�w plikowych

      for (int i=0; i<listToZip.length; i++) {
        doZip(listToZip[i], zos);  // dla ka�dego obiektu plikowego w tym
      }                            // katalogu wolamy rekurencyjnie doZip

    }
    else {  // jezeli to plik - zipujemy!

      String fname = fileToZip.toString();   // nazwa pliku

      // czy przypadkiem nie ma postaci d:\plik_lub_katalog 
      // w tym przypadku w nazwie elementu ("wej�cia") archiwum
      // pominiemy literowe oznaczenie dysku, dwukropek i separator
      int colon = fname.indexOf(":") + 1;
      if (colon!= 0) fname = fname.substring(colon); // bez "d:"
      if (fname.charAt(0) == fileSep.charAt(0)) { // zdj�� ew. separator
        fname = fname.substring(1);
      }  
      
      // Tworzymy nowe "wej�cie" - opisuj�ce nowy element archiwum
      ZipEntry entry = new ZipEntry(fname);
      // i zapisujemy to "wej�cie"
      zos.putNextEntry(entry);

      // informacja o post�pach kompresji
      if (verbose) System.out.println("Deflating " + entry);

      // Czytamy plik i zapisujemy jego zawarto��
      // w skompresowanej postaci "pod" otwartym "wej�ciem" entry 
      BufferedInputStream in = new BufferedInputStream(
                                    new FileInputStream(fileToZip),
                                    BUF_SIZE
                                   );
      byte data[] = new byte[BUF_SIZE]; // tablica. do wczytywania danych
      int count;                        // liczba przeczytanych bajt�w

      while ((count = in.read(data)) != -1) { // czytanie i zapis z kompresj�
            zos.write(data, 0, count);
      }
      in.close(); // zamkni�cie pliku wej�ciowego

      zos.closeEntry(); // zamkni�cie elementu (koniec zapisu elementu)
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