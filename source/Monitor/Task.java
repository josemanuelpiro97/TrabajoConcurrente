package Monitor;//Test Class of Monitor.Task

public class Task  implements Runnable{
    private Thread thread;
    private int[] transitionN;
    private Monitor monitor;

    public Task(Thread thread,int[] transitionN,Monitor monitor) {
        this.thread = thread;
        this.transitionN = transitionN;
        this.monitor = monitor;
    }

    @Override
    public void run(){
        //test variable
        final int FINAL = 100;

        for (int i =0;i<FINAL;i++){
            this.monitor.takeMonitor();
          //  this.monitor.operate(this.transitionN);
        }
    }
}
