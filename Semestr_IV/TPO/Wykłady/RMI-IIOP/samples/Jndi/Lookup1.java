import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

class Lookup1 {
    public static void main(String[] args) {
	Hashtable env = new Hashtable(11);
	env.put(Context.INITIAL_CONTEXT_FACTORY, 
	    "com.sun.jndi.fscontext.RefFSContextFactory");

        String name = "e:/temp";

	try {

	    // Stworzenie inicjalnego kontekstu
	    Context ctx = new InitialContext(env);

	    // odnalezienie zasobu
	    Object obj = ctx.lookup(name);

	    // Zobaczmy co to jest
	    System.out.println("Nazwa " + name + " identyfikuje: " + obj);
	    
            // Zamkniecie kontekstu
	    ctx.close();
	} catch (NamingException e) {
	    System.err.println("Problem z identyfikacja " + name + ": \n" + e);
	}
    }
}
