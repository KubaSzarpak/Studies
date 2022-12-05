package utp7_2;


public class StringTask implements Runnable {

    private final Thread thread;
    private final String word;
    private final long multiplications;
    private String result;
    private boolean isDone;
    private TaskState state;


    public StringTask(String word, long multiplications) {
        this.state = TaskState.CREATED;
        this.isDone = false;
        this.word = word;
        this.result = "";
        this.multiplications = multiplications;

        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        state = TaskState.RUNNING;

        for (int i = 0; i < multiplications; i++) {
            result = result + word;
            if (thread.isInterrupted()) {
                state = TaskState.ABORTED;
                isDone = true;
                return;
            }
        }
        isDone = true;
        state = TaskState.READY;
    }


    public void start() {
        thread.start();
    }

    public void abort() {
        thread.interrupt();
    }


    public TaskState getState() {
        return state;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getResult() {
        return result;
    }
}
