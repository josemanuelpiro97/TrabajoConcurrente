package Monitor;//Test Class of Monitor.Task

import Monitor.Logger.Log;

public class Task implements Runnable {
    private int transitionN;
    private Monitor monitor;
    private Log log;

    public Task( int transitionN, Monitor monitor,Log log) {
        this.transitionN = transitionN;
        this.monitor = monitor;
        this.log = log;
    }

    @Override
    public void run() {
        //test variable
        final int FINAL = 10000;

        for (int i = 0; i < FINAL; i++) {
            try {
                //operate
                this.monitor.takeMonitor(this.transitionN);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
