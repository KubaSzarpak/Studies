package exec2;

import java.lang.reflect.Method;
import java.util.concurrent.*;

public class Ftask<V> extends FutureTask<V> {
  
  private Method handlerMethod;
  private Object handlerObject;
  
  public Ftask(Callable<V> callable, Object handler, String mname) throws Exception {
    super(callable);
    handlerObject = handler;
    handlerMethod = handler.getClass().getDeclaredMethod(mname, Object.class, Exception.class);
  }
  
  public void done() {
    V result = null;
    try {
      result = (V) this.get();
    } catch(Exception exc) {
      try {
        handlerMethod.invoke(handlerObject, null, exc);
      } catch(Exception ex) {
          ex.printStackTrace();
      }
      return;
    }
    try {
      handlerMethod.invoke(handlerObject, result, null);
    } catch(Exception exc) {
      exc.printStackTrace();
  }
}
      
    
  


}
