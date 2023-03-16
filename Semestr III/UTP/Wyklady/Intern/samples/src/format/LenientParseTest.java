package format;
import com.ibm.icu.text.*;  // podpakiet ICU - dla RuleBasedNumberFormat
import java.util.*;         // Locale
import java.text.*;         // ParsePosition
import javax.swing.*;       // JOptionPane


class LenientParseTest {

  public static void main(String[] args) {

    // formator typu SPELLOUT
    RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(
                                     new Locale("de"), 
                                     RuleBasedNumberFormat.SPELLOUT
                                 );

    // Ustalenie "luŸnego" parsowania
    rbnf.setLenientParseMode(true);

    // Wartoœci do parsowania
    String[] snum = { "twenty-one", "TWENTYone", 
                      "one hundred and one",
                      "one-hundred-and-one",
                      "one-hundred AND one", 
                    };

    // Przekszta³cenie opisów na liczby

    for (int i=0; i< snum.length; i++) {
      Number val = rbnf.parse(snum[i], new ParsePosition(0));
      System.out.println(snum[i] + "   =   " + val);     
    }

  }
 
}
