 import java.nio.charset.*;
 import java.util.*;
    
 class Encoders {
   public static void main(String args[]) {
     Map availCharSets = Charset.availableCharsets();
     Set keys = availCharSets.keySet();
     Iterator iter = keys.iterator();
     while( iter.hasNext()) {
       String key = (String) iter.next(); 
       Charset cs = (Charset) availCharSets.get(key);
       String out = "Charset " + key + " , aliases:";
       Set aliases = cs.aliases();
       Iterator aiter = aliases.iterator();
       while (aiter.hasNext()) out += " " + aiter.next(); 
       System.out.println(out);
      }
  }
 }

