package datyczas;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Strefy {
 
  public static void main(String[] args) {
     String[] ids = TimeZone.getAvailableIDs();
     Arrays.sort(ids);
    /* 
     for (int i=0; i < ids.length; i++) {
       TimeZone tz = TimeZone.getTimeZone(ids[i]);
       System.out.println(ids[i] + " czyli " + tz.getDisplayName());
     }
    */
    TimeZone myTz = TimeZone.getDefault();
    TimeZone java = TimeZone.getTimeZone("Asia/Jakarta");
    TimeZone cuba = TimeZone.getTimeZone("America/Havana"); 

    /*
    showUTC_offset(myTz);
    showUTC_offset(java);
    showUTC_offset(cuba);
    
    showDiff(myTz, java);
    showDiff(myTz, cuba);
    showDiff(cuba, java);
    showDiff(java, cuba);
    */
    
    // Testowanie  
    final JCheckBox[] cb = new JCheckBox[ids.length];
    final TimeZone[] tz = new TimeZone[ids.length]; 
    JPanel zp = new JPanel(new GridLayout(0, 5));
    for (int i=0; i < ids.length; i++) {
       tz[i] = TimeZone.getTimeZone(ids[i]);
       cb[i] = new JCheckBox(ids[i]);
       zp.add(cb[i]);
    }
    JFrame f = new JFrame();
    Container cp = f.getContentPane();
    cp.add(new JScrollPane(zp));
    JButton b = new JButton("Poka¿ ró¿nicê czasu");
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         int i, j;
         for (i = 0; i<cb.length; i++) 
          if (cb[i].isSelected()) break;
         for (j = i+1; j < cb.length; j++) 
          if (cb[j].isSelected()) break;
         try {
           JOptionPane.showMessageDialog(null, getDiffMsg(tz[i], tz[j]));
         } catch (ArrayIndexOutOfBoundsException ex) { return; }
      }   
     });
     JButton b2 = new JButton("Clear");
     b2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i<cb.length; i++) cb[i].setSelected(false);
      }
      });  
           
     JPanel p = new JPanel();
     p.add(b); p.add(b2);
     cp.add(p, "South");
     f.setSize(800, 500);
     f.show();
  } 

  static void showUTC_offset(TimeZone z) {
    System.out.println(z.getID());
    System.out.println(z.getDisplayName(true, TimeZone.LONG));
    System.out.println(z.getDisplayName());
    System.out.println("Ró¿nica do UTC bez uwzgl. czasu letniego: " + 
                        z.getRawOffset()/3600000.0);
    Date teraz = new Date();
    System.out.println("Teraz ró¿nica do UTC wynosi: " +
                        z.getOffset(teraz.getTime())/3600000.0);
  }
  
  static String getDiffMsg(TimeZone z1, TimeZone z2) {
    Date data = new Date();
    long teraz = data.getTime();
    double offset1 = z1.getOffset(teraz)/3600000.0; 
    double offset2 = z2.getOffset(teraz)/3600000.0; 
    double diff;
    if (offset1 > offset2)  diff = -(offset1 - offset2);
    else diff = offset2 - offset1;
    String out =  "Ró¿nica czasu pomiêdzy" + '\n' +
                  z1.getID() + " i " + z2.getID()  + '\n' +
                  "wynosi teraz : " + diff + " godz."  + '\n' +
                  "W strefie " + z1.getID() +  '\n' +
                       (z1.inDaylightTime(data) ? " " : " nie ") +
                       "dzia³a czas letni"  + '\n' + 
                  "W strefie " + z2.getID() + 
                       (z2.inDaylightTime(data) ? " " : " nie ") +
                       "dzia³a czas letni";
                       
    return out;
  }     
 
}
