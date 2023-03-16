package exec2;

import java.util.concurrent.*;


public abstract class AbstractTask<V> implements Callable<V> {
  
  private String   name;
  private FutureTask<V> task;

  public AbstractTask(String name, Object resultHandler, String handlerMethodName) throws Exception {
    this.name = name;
    task = new Ftask<V>(this, resultHandler, handlerMethodName);
  }
  
  public FutureTask<V> getTask() { return task; }
  public String getName() { return name; }
    
  }


