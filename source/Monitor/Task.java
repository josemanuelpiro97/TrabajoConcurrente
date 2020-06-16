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
        final int FINAL = 100;

        for (int i = 0; i < FINAL; i++) {
            this.monitor.takeMonitor();
            try {
                //log
                String msj = "El hilo N: " + Thread.currentThread().getName() + " ingresa al monitor" ;
                this.log.write2(msj);

                //operate
                this.monitor.operate(this.transitionN);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
