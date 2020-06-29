package Monitor;//Test Class of Monitor.Task

import Monitor.Logger.Log;

public class Task implements Runnable {

    private int transitionN;
    private Monitor monitor;
    private int disparos;

    public Task( int transitionN, Monitor monitor, int cant) {
        this.transitionN = transitionN;
        this.monitor = monitor;
        this.disparos = cant;
    }

    @Override
    public void run() {
        int i = 0;
        while(i < this.disparos || this.disparos == 0){
            try {
                //operate
                this.monitor.operate(this.transitionN);
                i++;
                Thread.sleep(100);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
