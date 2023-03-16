package bundles;

import java.util.*;

import javax.swing.*;

public class CountryInfo_pl_PL extends ListResourceBundle {
   
   public Object[][] getContents() {
	return contents;
    }
   
    private Object[][] contents = {
	{ "name", "Polska" },
	{ "flag",  new ImageIcon("PolskaFlaga.gif") },
    };
}
 
