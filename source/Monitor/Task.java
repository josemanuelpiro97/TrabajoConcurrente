package Monitor;//Test Class of Monitor.Task

public class Task {
    private Thread thread;
    private int transitionN;

    public Task(Thread thread,int transitionN) {
        this.thread = thread;
        this.transitionN = transitionN;
    }

    public int getTransitionN() {
        return transitionN;
    }
}
