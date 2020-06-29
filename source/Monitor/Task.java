package Monitor;//Test Class of Monitor.Task

public class Task implements Runnable {

    private int transitionN;
    private Monitor monitor;
    private int disparosTotales;
    public static int tareasRealizadas;

    public Task( int transitionN, Monitor monitor, int cant) {
        this.transitionN = transitionN;
        this.monitor = monitor;
        this.disparosTotales = cant;
    }

    @Override
    public void run() {
        int i = 0;
        while(i < this.disparosTotales || this.disparosTotales == 0){
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
